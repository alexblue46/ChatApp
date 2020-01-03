package arb19_kpa1.client.model.gamemvc.model;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import arb19_kpa1.client.model.gamemvc.view.IRoomModel2RoomControllerAdpt;
import arb19_kpa1.utility.RoomNetworkUtility;
import common.ICmd2ModelAdapter;
import common.cmd.ARoomMsgCmd;
import common.msg.IRoomMsg;
import common.msg.IStringMsg;
import common.msg.room.IAddCommandMsg;
import common.msg.room.IAddMemberMsg;
import common.msg.room.ILeaveMsg;
import common.msg.room.IProvideAppMsg;
import common.msg.room.IRequestAppMsg;
import common.msg.room.IRequestCommandMsg;
import common.msg.room.ISyncRoomMsg;
import common.msg.status.IFailureMsg;
import common.msg.status.network.IRemoteExceptionMsg;
import common.msg.status.network.IRoomCheckStatusMsg;
import common.msg.status.network.IRoomOkStatusMsg;
import common.msg.status.network.IServerCheckStatusMsg;
import common.msg.status.network.IServerOkStatusMsg;
import common.packet.RoomDataPacket;
import common.receivers.IRoomMember;
import common.virtualNetwork.IRoom;
import provided.datapacket.IDataPacketID;
import provided.rmiUtils.IRMI_Defs;

/**
 * The mini-mvc model for the game
 *
 */
public class RoomModel {

	/**
	 * Logger for game model
	 */
	private final static Logger LOGGER = Logger.getLogger(RoomModel.class.getName());

	/**
	 * The team member that handles communication for the game
	 */
	private RoomMember member;
	/**
	 * Our member stub to send out
	 */
	private IRoomMember memberStub;
	/**
	 * The adapter to the game view
	 */
	private IRoomModel2RoomViewAdpt model2view;

	/**
	 * Adapter from commands to model
	 */
	private ICmd2ModelAdapter cmd2ModelAdpt;

	/**
	 * Holds the room for the current game
	 */
	private final GameRoom gameRoom;

	/**
	 * Processing thread
	 */
	private Thread processingThread;

	/**
	 * Boolean to indicate whether to process messages
	 */
	private boolean processMessages;

	/**
	 * Network utility object for sending messages
	 */
	private final RoomNetworkUtility networkUtil;

	/**
	 * Object for pinging the server
	 */
	private ServerHeartbeat serverHeartbeat;

	/**
	 * Mapping from DataPacketIDs to corresponding unprocessed datapackets, which
	 * were unknown at the time of reception
	 */
	private Map<IDataPacketID, List<RoomDataPacket<IRoomMsg>>> unprocessedUnknownDatapackets = new HashMap<>();

	/**
	 * Processes messages coming into our GameMember
	 */
	private RoomDataPacketVisitor msgProcessor = new RoomDataPacketVisitor(new ARoomMsgCmd<IRoomMsg>() {

		/**
		 * Serialization
		 */
		private static final long serialVersionUID = -7406707443684008561L;

		@Override
		public Void apply(IDataPacketID index, RoomDataPacket<IRoomMsg> host, Void... params) {
			LOGGER.info("GameModel tried to process unknown message with ID " + index + " with data " + host.getData());
			IDataPacketID unknownID = host.getData().getID();
			getNetworkUtil().sendMessageAsync(host.getSender(), IRequestCommandMsg.make(unknownID));
			// Keep datapackets for later processing
			if (!unprocessedUnknownDatapackets.containsKey(unknownID)) {
				unprocessedUnknownDatapackets.put(unknownID, new ArrayList<>());
			}
			unprocessedUnknownDatapackets.get(unknownID).add(host);
			return null;
		}

	});

	/**
	 * Adapter to the room controller
	 */
	private IRoomModel2RoomControllerAdpt model2control;

	/**
	 * A set of all well known message IDs
	 */
	private Set<IDataPacketID> wellKnownMessageIDs;

	/**
	 * Create a model for the game
	 * 
	 * @param model2view    the adapter to the game view
	 * @param model2control the adapter to controller
	 * @param cmd2ModelAdpt adapter from commands to the model
	 * @param clientName    the team member for handling communications in this game
	 * @param room          the room for the game
	 */
	public RoomModel(IRoomModel2RoomViewAdpt model2view, IRoomModel2RoomControllerAdpt model2control,
			ICmd2ModelAdapter cmd2ModelAdpt, String clientName, IRoom room) {
		this.model2control = model2control;
		this.model2view = model2view;
		this.cmd2ModelAdpt = cmd2ModelAdpt;
		// Create new team member
		member = new RoomMember(clientName);
		// Get stub to send to server
		try {
			memberStub = (IRoomMember) UnicastRemoteObject.exportObject(member, IRMI_Defs.STUB_PORT_EXTRA);// RMIUtils.BOUND_PORT_SERVER);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		this.gameRoom = new GameRoom(room);
		processMessages = true;
		processingThread = new Thread(() -> {
			messageProcessingTask();
		});

		serverHeartbeat = new ServerHeartbeat(this);
		networkUtil = new RoomNetworkUtility(memberStub, gameRoom);
	}

	/**
	 * Task that processes messages
	 */
	private void messageProcessingTask() {
		while (processMessages) {
			RoomDataPacket<?> msg = member.getMsgToProcess();
			if (msg == null) {
				break;
			}
			LOGGER.log(Level.FINE, "Processing room message: " + msg.getData().getClass());
			msg.execute(msgProcessor);
		}
		LOGGER.info("Room message processing thread shutting down...");
	}

	/**
	 * Starts the model
	 */
	public void start() {
		msgProcessor.setCmd(IAddCommandMsg.GetID(), new ARoomMsgCmd<IAddCommandMsg<?>>() {

			/**
			 * UID
			 */
			private static final long serialVersionUID = -2417892449917547643L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IAddCommandMsg<?>> msg, Void... params) {
				if (wellKnownMessageIDs.contains(msg.getData().getMsgId())) {
					LOGGER.info("Install failure - tried to install well-known message type: "
							+ msg.getData().getCommand().getClass());
					getNetworkUtil().sendMessageAsync(msg.getSender(),
							IFailureMsg.make("Install failure - tried to install well-known message type"));
				} else {
					LOGGER.info("Installing cmd: " + msg.getData().getCommand());
					IDataPacketID installMsgID = msg.getData().getMsgId();
					ARoomMsgCmd<?> installMsgCmd = msg.getData().getCommand();
					installMsgCmd.setCmd2ModelAdpt(cmd2ModelAdpt);
					msgProcessor.setCmd(installMsgID, installMsgCmd);
					// Loop through unprocessed commands and process them
					if (unprocessedUnknownDatapackets.containsKey(installMsgID)) {
						List<RoomDataPacket<IRoomMsg>> unprocessedMsgs = unprocessedUnknownDatapackets
								.get(installMsgID);
						for (RoomDataPacket<IRoomMsg> dp : unprocessedMsgs) {
							dp.execute(msgProcessor);
						}
						unprocessedUnknownDatapackets.remove(installMsgID);
					}
				}
				return null;
			}
		});
		msgProcessor.setCmd(IAddMemberMsg.GetID(), new ARoomMsgCmd<IAddMemberMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 295571760649889100L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IAddMemberMsg> msg, Void... params) {
				// No-op
				return null;
			}
		});
		msgProcessor.setCmd(ILeaveMsg.GetID(), new ARoomMsgCmd<ILeaveMsg>() {

			/**
			 * UID
			 */
			private static final long serialVersionUID = 6313035254028812758L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<ILeaveMsg> msg, Void... params) {
				if (msg.getData().getUserToLeave().equals(member)) {
					// We must leave the room
					stop();
				}
				return null;
			}
		});
		msgProcessor.setCmd(IProvideAppMsg.GetID(), new ARoomMsgCmd<IProvideAppMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 6313035254028812758L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IProvideAppMsg> msg, Void... params) {
				LOGGER.info("No-oping on IProvideAppMsg");
				return null;
			}
		});
		msgProcessor.setCmd(IRequestAppMsg.GetID(), new ARoomMsgCmd<IRequestAppMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 5611185744031059368L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IRequestAppMsg> msg, Void... params) {
				getNetworkUtil().sendMessageAsync(msg.getSender(), IProvideAppMsg.make(model2control.getClientStub()));
				return null;
			}
		});
		msgProcessor.setCmd(IRequestCommandMsg.GetID(), new ARoomMsgCmd<IRequestCommandMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 7635384175966531749L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IRequestCommandMsg> msg, Void... params) {
				@SuppressWarnings("unchecked")
				ARoomMsgCmd<? extends IRoomMsg> unknownCmd = (ARoomMsgCmd<? extends IRoomMsg>) msgProcessor
						.getCmd(msg.getData().getMsgId());
				if (unknownCmd == null) {
					getNetworkUtil().sendMessageAsync(msg.getSender(), IFailureMsg
							.make("Requested command with ID " + msg.getData().getMsgId() + " was not found"));
				} else {
					getNetworkUtil().sendMessageAsync(msg.getSender(),
							IAddCommandMsg.make(unknownCmd, msg.getData().getMsgId()));
				}
				return null;
			}
		});
		msgProcessor.setCmd(ISyncRoomMsg.GetID(), new ARoomMsgCmd<ISyncRoomMsg>() {

			/**
			 * UID
			 */
			private static final long serialVersionUID = 5987673186583476606L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<ISyncRoomMsg> msg, Void... params) {
				ISyncRoomMsg sendTeamsMsg = msg.getData();
				setGameMembers(sendTeamsMsg.getMembers());
				return null;
			}
		});
		msgProcessor.setCmd(IRemoteExceptionMsg.GetID(), new ARoomMsgCmd<IRemoteExceptionMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -148909739820148903L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IRemoteExceptionMsg> msg, Void... params) {
				LOGGER.info("IRemoteExceptionMsg received on stub " + msg.getData().getBadStub());
				getNetworkUtil().sendMessageAsync(msg.getData().getBadStub(), IRoomCheckStatusMsg.make());
				return null;
			}
		});
		msgProcessor.setCmd(IRoomCheckStatusMsg.GetID(), new ARoomMsgCmd<IRoomCheckStatusMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 3347340195647920660L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IRoomCheckStatusMsg> msg, Void... params) {
				getNetworkUtil().sendMessageToAll(IRoomOkStatusMsg.make(msg.getData().getUUID()));
				return null;
			}
		});
		msgProcessor.setCmd(IRoomOkStatusMsg.GetID(), new ARoomMsgCmd<IRoomOkStatusMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 6261814941349468827L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IRoomOkStatusMsg> msg, Void... params) {
				LOGGER.info("IRoomOkStatusMsg received from stub " + msg.getSender());
				return null;
			}
		});
		msgProcessor.setCmd(IServerCheckStatusMsg.GetID(), new ARoomMsgCmd<IServerCheckStatusMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -7496476305059977286L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IServerCheckStatusMsg> msg, Void... params) {
				LOGGER.info("No-oping on IServerCheckStatusMsg");
				return null;
			}
		});
		msgProcessor.setCmd(IServerOkStatusMsg.GetID(), new ARoomMsgCmd<IServerOkStatusMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 8792820797843843887L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IServerOkStatusMsg> msg, Void... params) {
				serverHeartbeat.acceptServerOk(msg.getData());
				return null;
			}
		});
		msgProcessor.setCmd(IFailureMsg.GetID(), new ARoomMsgCmd<IFailureMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 5370549360633461700L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IFailureMsg> msg, Void... params) {
				LOGGER.severe("IFailureMsg received: " + msg.getData().getDescription());
				return null;
			}
		});
		msgProcessor.setCmd(IStringMsg.GetID(), new ARoomMsgCmd<IStringMsg>() {

			/**
			 * UID
			 */
			private static final long serialVersionUID = -1842448928035315789L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IStringMsg> msg, Void... params) {
				IStringMsg stringMsg = msg.getData();
				try {
					displayStringMsg(stringMsg, msg.getSender().getName());
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, e.getMessage(), e);
				}
				return null;
			}
		});
		wellKnownMessageIDs = msgProcessor.getAllIndices();
		processingThread.start();
		serverHeartbeat.start();
	}

	/**
	 * @param stringMsg  String message to display
	 * @param senderName String of the name who sent the message
	 */
	public void displayStringMsg(IStringMsg stringMsg, String senderName) {
		model2view.displayGlobalMsg(senderName, stringMsg.getMessage());
	}

	/**
	 * Leave the game
	 */
	public void stop() {
		sendLeaveMessages();
		serverHeartbeat.stop();
		processMessages = false;
		processingThread.interrupt();
		try {
			processingThread.join();
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		model2control.removeGame();
	}

	/**
	 * Notify members that you are leaving
	 */
	public void sendLeaveMessages() {
		getNetworkUtil().sendMessageToAll(ILeaveMsg.make(memberStub));
	}

	/**
	 * @param members Set members to room
	 */
	public void setGameMembers(Collection<IRoomMember> members) {
		this.gameRoom.setMembers(members);
		model2view.setMembers(gameRoom);
	}

	/**
	 * @return IRoom implementation
	 */
	public IRoom getRoom() {
		return gameRoom;
	}

	/**
	 * Sends a string message to all team members
	 * 
	 * @param text String message to send
	 */
	public void sendStringMessage(String text) {
		getNetworkUtil().sendMessageToAll(IStringMsg.make(text));
	}

	/**
	 * Notify members that you joined room
	 */
	public void sendAddMemberMessages() {
		try {
			getNetworkUtil().sendMessageToAll(IAddMemberMsg.make(memberStub, member.getName()));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * @return Get network utility object
	 */
	public RoomNetworkUtility getNetworkUtil() {
		return networkUtil;
	}

}
