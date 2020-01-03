package arb19_kpa1.server.model.mvc.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import arb19_kpa1.game.MDDGameKeys;
import arb19_kpa1.game.controller.GameController;
import arb19_kpa1.game.model.GameState;
import arb19_kpa1.game.msg.GameOverCmd;
import arb19_kpa1.game.msg.GameOverMsg;
import arb19_kpa1.game.msg.GameStateCmd;
import arb19_kpa1.game.msg.GameStateMsg;
import arb19_kpa1.game.msg.SetKeysMsg;
import arb19_kpa1.game.msg.StartGameCmd;
import arb19_kpa1.game.msg.StartGameMsg;
import arb19_kpa1.game.msg.TeamStringCmd;
import arb19_kpa1.game.msg.TeamStringMsg;
import arb19_kpa1.game.msg.teamjoin.JoiningTeamMsg;
import arb19_kpa1.game.msg.teamjoin.SetKeysCmd;
import arb19_kpa1.game.msg.teamjoin.TeamJoiningCmd;
import arb19_kpa1.game.msg.teamjoin.TeamJoiningMsg;
import arb19_kpa1.game.msg.teamjoin.TeamJoiningView;
import arb19_kpa1.game.msg.teamjoin.UpdateTeamsCmd;
import arb19_kpa1.game.msg.teamjoin.UpdateTeamsMsg;
import arb19_kpa1.server.model.IServer;
import arb19_kpa1.utility.RoomNetworkUtility;
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
import provided.mixedData.MixedDataKey;
import provided.rmiUtils.IRMI_Defs;

/**
 * The model for the server in the game room
 *
 */
public class ServerGameModel {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = Logger.getLogger(ServerGameModel.class.getName());

	/**
	 * Commands that represent the game
	 */
	private Map<IDataPacketID, ARoomMsgCmd<? extends IRoomMsg>> cmdsToSend = new HashMap<IDataPacketID, ARoomMsgCmd<? extends IRoomMsg>>();

	/**
	 * Adapter to the view
	 */
	private IServerRoomModel2ServerRoomViewAdpt model2view;

	/**
	 * Teams in the game
	 */
	private Map<Integer, Collection<IRoomMember>> teams = new HashMap<Integer, Collection<IRoomMember>>();

	/**
	 * Whether the game is in progress
	 */
	private boolean gameInProgress = false;

	/**
	 * The room member corresponding to the server
	 */
	private final ServerMember serverMember = new ServerMember();

	/**
	 * The stub for our server room member
	 */
	private IRoomMember serverMemberStub;

	/**
	 * The stub for the server application
	 */
	private final IServer serverStub;

	/**
	 * The turn ordering mapping, where each (key, value) pair means value comes
	 * after key in the ordering
	 */
	private Map<IRoomMember, IRoomMember> turnOrder = new HashMap<IRoomMember, IRoomMember>();

	/**
	 * The player who goes first in the game
	 */
	private IRoomMember firstPlayer;

	/**
	 * Network utility for sending messages
	 */
	private RoomNetworkUtility networkUtil;

	/**
	 * Processing thread
	 */
	private Thread processingThread;

	/**
	 * Boolean to indicate whether to process messages
	 */
	private boolean processMessages;

	/**
	 * Utility for handling received datapackets
	 */
	private ServerMemberDataPacketVisitor msgProcessor = new ServerMemberDataPacketVisitor(new ARoomMsgCmd<IRoomMsg>() {

		/**
		 * Serialization
		 */
		private static final long serialVersionUID = -1394743972554900464L;

		@Override
		public Void apply(IDataPacketID index, RoomDataPacket<IRoomMsg> host, Void... params) {
			LOGGER.info(
					"Server member tried to process unknown message with ID " + index + " with data " + host.getData());
			return null;
		}
	});

	/**
	 * The current room
	 */
	private Room room;

	/**
	 * The main MDD key
	 */
	@SuppressWarnings("rawtypes")
	private final MixedDataKey<Map> key;

	/**
	 * The inner mapping from game keys to MDD keys
	 */
	private final Map<MDDGameKeys, MixedDataKey<?>> keyMap = new HashMap<MDDGameKeys, MixedDataKey<?>>();

	/**
	 * The initial teams at the start of the game, null if game not started.
	 */
	private Map<Integer, Collection<IRoomMember>> initialTeams = null;

	/**
	 * Creates a new ServerGameModel
	 * 
	 * @param model2view the adapter to the view
	 * @param roomName   the name of the room
	 * @param serverStub the application stub of the server
	 */
	@SuppressWarnings("rawtypes")
	public ServerGameModel(IServerRoomModel2ServerRoomViewAdpt model2view, String roomName, IServer serverStub) {
		this.model2view = model2view;

		this.serverStub = serverStub;

		processMessages = true;
		processingThread = new Thread(() -> {
			while (processMessages) {
				RoomDataPacket<?> msg = serverMember.getMsgToProcess();
				if (msg == null) {
					break;
				}
				LOGGER.info("MESSAGE RECEIVED: " + msg.getData().getClass());
				msg.execute(msgProcessor);
			}
			LOGGER.info("Message processing thread shutting down...");
		});

		try {
			this.serverMemberStub = (IRoomMember) UnicastRemoteObject.exportObject(serverMember,
					IRMI_Defs.STUB_PORT_SERVER);
		} catch (RemoteException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		room = new Room(new HashSet<IRoomMember>(), roomName);
		room.addMember(serverMemberStub);

		teams.put(1, new HashSet<IRoomMember>());
		teams.put(2, new HashSet<IRoomMember>());

		key = new MixedDataKey<Map>(room.getInfo().getID(), "", Map.class);
		keyMap.put(MDDGameKeys.MY_TEAM_NUM, new MixedDataKey<Integer>(UUID.randomUUID(), "Team Number", Integer.class));
		keyMap.put(MDDGameKeys.TEAM_JOIN_VIEW,
				new MixedDataKey<TeamJoiningView>(UUID.randomUUID(), "Joining View", TeamJoiningView.class));
		keyMap.put(MDDGameKeys.TEAMS, new MixedDataKey<Map>(UUID.randomUUID(), "Teams", Map.class));
		keyMap.put(MDDGameKeys.GAME_CONTROLLER,
				new MixedDataKey<GameController>(UUID.randomUUID(), "Game Controller", GameController.class));
		keyMap.put(MDDGameKeys.TURN_ORDER, new MixedDataKey<Map>(UUID.randomUUID(), "Turn Order", Map.class));
		keyMap.put(MDDGameKeys.MEMBER_STUB,
				new MixedDataKey<IRoomMember>(UUID.randomUUID(), "Member Stub", IRoomMember.class));

		cmdsToSend.put(TeamJoiningMsg.GetID(), new TeamJoiningCmd(key));
		cmdsToSend.put(UpdateTeamsMsg.GetID(), new UpdateTeamsCmd(key));
		cmdsToSend.put(SetKeysMsg.GetID(), new SetKeysCmd());
		cmdsToSend.put(TeamStringMsg.GetID(), new TeamStringCmd(key));
		cmdsToSend.put(GameStateMsg.GetID(), new GameStateCmd(key));
		cmdsToSend.put(StartGameMsg.GetID(), new StartGameCmd(key));
		cmdsToSend.put(GameOverMsg.GetID(), new GameOverCmd(key));

		networkUtil = new RoomNetworkUtility(serverMemberStub, room);
	}

	/**
	 * Gets the game room
	 * 
	 * @return the game room
	 */
	public IRoom getGameRoom() {
		return room;
	}

	/**
	 * Start the room level server
	 */
	public void start() {
		msgProcessor.setCmd(IAddCommandMsg.GetID(), new ARoomMsgCmd<IAddCommandMsg<?>>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -3963811796023461089L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IAddCommandMsg<?>> host, Void... params) {
				try {
					LOGGER.warning("Add command message from " + host.getSender().getName());
				} catch (RemoteException e) {
					LOGGER.log(Level.SEVERE, e.getMessage(), e);
				}
				return null;
			}

		});

		msgProcessor.setCmd(IAddMemberMsg.GetID(), new ARoomMsgCmd<IAddMemberMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 2359755189332545872L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IAddMemberMsg> host, Void... params) {
				try {
					LOGGER.info("Add member messge from " + host.getSender().getName());
				} catch (RemoteException e) {
					LOGGER.log(Level.SEVERE, e.getMessage(), e);
				}

				room.addMember(host.getData().getUserToAdd());

				syncRoom();
				sendAllCmds(host.getData().getUserToAdd());

				networkUtil.sendMessageAsync(host.getData().getUserToAdd(), new SetKeysMsg(keyMap, key));
				networkUtil.sendMessageAsync(host.getData().getUserToAdd(), new TeamJoiningMsg(teams));

				return null;
			}

		});

		msgProcessor.setCmd(ILeaveMsg.GetID(), new ARoomMsgCmd<ILeaveMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -4600961958739802273L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<ILeaveMsg> host, Void... params) {
				try {
					LOGGER.info("Leave message from " + host.getSender().getName());
				} catch (RemoteException e) {
					LOGGER.log(Level.SEVERE, e.getMessage(), e);
				}

				room.removeMember(host.getData().getUserToLeave());
				checkIfRemovedPlayerIsCurrentTurn(host.getData().getUserToLeave());
				teams.get(1).remove(host.getData().getUserToLeave());
				teams.get(2).remove(host.getData().getUserToLeave());
				syncRoom();
				updateTeams();
				return null;
			}

		});

		msgProcessor.setCmd(IProvideAppMsg.GetID(), new ARoomMsgCmd<IProvideAppMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 7135653174453507053L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IProvideAppMsg> host, Void... params) {
				try {
					LOGGER.info("Provide app message from " + host.getSender().getName());
				} catch (RemoteException e) {
					LOGGER.log(Level.SEVERE, e.getMessage(), e);
				}
				return null;
			}

		});

		msgProcessor.setCmd(IRequestAppMsg.GetID(), new ARoomMsgCmd<IRequestAppMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -6921668637776976427L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IRequestAppMsg> host, Void... params) {
				try {
					LOGGER.info("Request app message from " + host.getSender().getName());
				} catch (RemoteException e) {
					LOGGER.log(Level.SEVERE, e.getMessage(), e);
				}
				networkUtil.sendMessageAsync(host.getSender(), IProvideAppMsg.make(serverStub));

				return null;
			}

		});

		msgProcessor.setCmd(IRequestCommandMsg.GetID(), new ARoomMsgCmd<IRequestCommandMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -1576515072435261489L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IRequestCommandMsg> host, Void... params) {
				try {
					LOGGER.info("Request command message from " + host.getSender().getName());
				} catch (RemoteException e) {
					LOGGER.log(Level.SEVERE, e.getMessage(), e);
				}
				LOGGER.info("ID of requested command is " + host.getData().getMsgId());

				ARoomMsgCmd<? extends IRoomMsg> cmdToSend = cmdsToSend.get(host.getData().getMsgId());

				if (cmdToSend == null) {
					LOGGER.warning("Could not find requested command");
					networkUtil.sendMessageAsync(host.getSender(),
							IFailureMsg.make("Could not find requested command"));
				} else {
					networkUtil.sendMessageAsync(host.getSender(),
							IAddCommandMsg.make(cmdToSend, host.getData().getMsgId()));
				}

				return null;
			}

		});

		msgProcessor.setCmd(ISyncRoomMsg.GetID(), new ARoomMsgCmd<ISyncRoomMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 2628632644707339197L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<ISyncRoomMsg> host, Void... params) {
				try {
					LOGGER.warning("Sync room message from " + host.getSender().getName());
				} catch (RemoteException e) {
					LOGGER.log(Level.SEVERE, e.getMessage(), e);
				}
				return null;
			}

		});

		msgProcessor.setCmd(JoiningTeamMsg.GetID(), new ARoomMsgCmd<JoiningTeamMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -3963811796023461089L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<JoiningTeamMsg> host, Void... params) {
				if (!gameInProgress) {
					if (host.getData().getTeam() == 1) {
						teams.get(1).add(host.getSender());
						teams.get(2).remove(host.getSender());
					} else {
						teams.get(2).add(host.getSender());
						teams.get(1).remove(host.getSender());
					}

					updateTeams();
				} else {
					LOGGER.info("Client tried to join team while game was in progress");
				}
				return null;
			}

		});
		msgProcessor.setCmd(IRemoteExceptionMsg.GetID(), new ARoomMsgCmd<IRemoteExceptionMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 7135653174453507053L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IRemoteExceptionMsg> host, Void... params) {
				pingMember(host.getData().getBadStub());
				return null;
			}

		});
		msgProcessor.setCmd(IRoomCheckStatusMsg.GetID(), new ARoomMsgCmd<IRoomCheckStatusMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -6921668637776976427L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IRoomCheckStatusMsg> host, Void... params) {
				networkUtil.sendMessageToAll(IRoomOkStatusMsg.make(host.getData().getUUID()));
				return null;
			}

		});
		msgProcessor.setCmd(IRoomOkStatusMsg.GetID(), new ARoomMsgCmd<IRoomOkStatusMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -1576515072435261489L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IRoomOkStatusMsg> host, Void... params) {
				if (!room.getMembers().contains(host.getSender())) {
					pingMember(host.getSender());
				}
				return null;
			}

		});
		msgProcessor.setCmd(IServerCheckStatusMsg.GetID(), new ARoomMsgCmd<IServerCheckStatusMsg>() {
			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 2628632644707339197L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IServerCheckStatusMsg> host, Void... params) {
				networkUtil.sendMessageAsync(host.getSender(), IServerOkStatusMsg.make(host.getData().getUUID()));
				return null;
			}

		});
		msgProcessor.setCmd(IServerOkStatusMsg.GetID(), new ARoomMsgCmd<IServerOkStatusMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 4791871249363874388L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IServerOkStatusMsg> host, Void... params) {
				// This really should not ever be called
				LOGGER.severe("IServerOkStatusMsg received");
				return null;
			}

		});
		msgProcessor.setCmd(IFailureMsg.GetID(), new ARoomMsgCmd<IFailureMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -8204762887935507308L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IFailureMsg> host, Void... params) {
				LOGGER.info("IFailureMsg received: " + host.getData().getDescription());
				return null;
			}

		});
		msgProcessor.setCmd(IStringMsg.GetID(), new ARoomMsgCmd<IStringMsg>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 625041870341744534L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<IStringMsg> host, Void... params) {
				LOGGER.info("IStringMsg received: " + host.getData().getMessage());
				return null;
			}

		});
		msgProcessor.setCmd(GameStateMsg.GetID(), new ARoomMsgCmd<GameStateMsg>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 178720217253366319L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<GameStateMsg> host, Void... params) {
				LOGGER.info("Received GameStateMsg");
				return null;
			}

		});
		msgProcessor.setCmd(GameOverMsg.GetID(), new ARoomMsgCmd<GameOverMsg>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2380258237790445746L;

			@Override
			public Void apply(IDataPacketID index, RoomDataPacket<GameOverMsg> host, Void... params) {
				gameInProgress = false;
				model2view.updateGameStatus(gameInProgress);
				teams.put(1, new HashSet<IRoomMember>());
				teams.put(2, new HashSet<IRoomMember>());
				updateTeams();
				return null;
			}

		});

		processingThread.start();
		syncRoom();
	}

	/**
	 * Deletes the current room
	 */
	public void deleteRoom() {
		processMessages = false;
		processingThread.interrupt();
		try {
			processingThread.join();
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		networkUtil.sendMessageToAll(ILeaveMsg.make(serverMemberStub));
	}

	/**
	 * Starts the game
	 */
	public void startGame() {
		if (teams.get(1).size() == 0 || teams.get(2).size() == 0) {
			networkUtil.sendMessageToAll(IStringMsg.make("Teams must be have members to start game"));
		} else {
			gameInProgress = true;
			model2view.updateGameStatus(gameInProgress);
			makeTurnOrder();
			GameState initialState = GameState.initialGameState(teams);
			initialState.setCurrentPlayer(firstPlayer);

			teams.get(1).forEach(member -> {
				new Thread(() -> {
					tryNTimes(member, new StartGameMsg(turnOrder, initialState, member), new GameStateMsg(initialState), 5);
					//networkUtil.sendMessageSync(member, new StartGameMsg(turnOrder, initialState, member));
					//networkUtil.sendMessageSync(member, new GameStateMsg(initialState));
				}).start();
			});
			teams.get(2).forEach(member -> {
				new Thread(() -> {
					tryNTimes(member, new StartGameMsg(turnOrder, initialState, member), new GameStateMsg(initialState), 5);
					//networkUtil.sendMessageSync(member, new StartGameMsg(turnOrder, initialState, member));
					//networkUtil.sendMessageSync(member, new GameStateMsg(initialState));
				}).start();
			});
			initialTeams = new HashMap<>(teams);
		}

	}
	
	private void tryNTimes(IRoomMember member, IRoomMsg msg1, IRoomMsg msg2, int n) {
		for (int i = 0; i < n; i++) {
			try {
				networkUtil.sendMessageSync(member, msg1);
				break;
			} catch (Exception e) {
				
			}
		}
		for (int i = 0; i < n; i++) {
			try {
				networkUtil.sendMessageSync(member, msg2);
				break;
			} catch (Exception e) {
				
			}
		}
	}

	/**
	 * Updates the room in the view and sends the ISyncRoomMsg
	 */
	private void syncRoom() {
		networkUtil.sendMessageToAll(ISyncRoomMsg.make(room.getMembers()));
		model2view.updateRoomMembers(room.getMembers());
	}

	/**
	 * Updates the teams in the view and sends out UpdateTeamsMsg
	 */
	private void updateTeams() {
		networkUtil.sendMessageToAll(new UpdateTeamsMsg(teams));
		model2view.updateTeams(teams);
	}

	/**
	 * Pings a problematic stub to find out whether to remove it or not
	 * 
	 * @param badStub the stub
	 */
	private void pingMember(IRoomMember badStub) {
		boolean successful = networkUtil.sendMessageSyncNoRemoteException(badStub, IRoomCheckStatusMsg.make());

		if (successful && !room.getMembers().contains(badStub)) {
			room.addMember(badStub);
		} else if (!successful && room.getMembers().contains(badStub)) {
			room.removeMember(badStub);
			checkIfRemovedPlayerIsCurrentTurn(badStub);
		}

		syncRoom();
		// Remove members from team if not in room
		teams.values().forEach((team) -> {
			team.removeIf((m) -> !room.getMembers().contains(m));
		});
		if (gameInProgress) {
			// Add members to their team if in room
			teams.forEach((teamnum, team) -> {
				for (IRoomMember initialMember : initialTeams.get(teamnum)) {
					if (room.getMembers().contains(initialMember) && !team.contains(initialMember)) {
						team.add(initialMember);
					}
				}
			});
		}
		updateTeams();
	}

	/**
	 * Checks if a removed player is the one who is currently going in the game, and
	 * acts accordingly
	 * 
	 * @param badStub the removed player
	 */
	private void checkIfRemovedPlayerIsCurrentTurn(IRoomMember badStub) {
		/*
		 * if (mostRecentGameState != null &&
		 * mostRecentGameState.getCurrentPlayer().equals(badStub)) { // We removed the
		 * player whose turn it currently is, pass it on to the next player IRoomMember
		 * nextPlayer = turnOrder.get(badStub); // send new game state }
		 */
	}

	/**
	 * Sends all commands relevant to playing the game
	 * 
	 * @param receiver the member to send it to
	 */
	private void sendAllCmds(IRoomMember receiver) {
		cmdsToSend.forEach((msgId, cmd) -> {
			networkUtil.sendMessageAsync(receiver, IAddCommandMsg.make(cmd, msgId));
		});
	}

	/**
	 * Creates the initial turn ordering
	 */
	private void makeTurnOrder() {
		boolean first = true;
		firstPlayer = null;
		IRoomMember prevPlayer = null;

		for (IRoomMember player : teams.get(1)) {
			if (first) {
				firstPlayer = player;
				first = false;
			} else {
				turnOrder.put(prevPlayer, player);
			}
			prevPlayer = player;
		}

		for (IRoomMember player : teams.get(2)) {
			turnOrder.put(prevPlayer, player);
			prevPlayer = player;
		}

		turnOrder.put(prevPlayer, firstPlayer);
	}

}
