package arb19_kpa1.client.model;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import arb19_kpa1.client.model.gamemvc.controller.RoomController;
import arb19_kpa1.client.model.gamemvc.controller.IRoomController2ModelAdpt;
import common.cmd.AAppMsgCmd;
import common.identity.IRoomIdentity;
import common.msg.IAppMsg;
import common.msg.application.IAcceptInviteMsg;
import common.msg.application.IAddRoomMsg;
import common.msg.application.IConnectMsg;
import common.msg.application.IInviteToRoomMsg;
import common.msg.application.IQuitMsg;
import common.msg.application.IRequestToJoinMsg;
import common.msg.collection.IAddAppsMsg;
import common.msg.collection.IAddRoomInfosMsg;
import common.msg.collection.IRequestAppsMsg;
import common.msg.collection.IRequestRoomsMsg;
import common.msg.status.IFailureMsg;
import common.msg.status.network.IRemoteExceptionMsg;
import common.msg.status.network.IServerCheckStatusMsg;
import common.packet.AppDataPacket;
import common.receivers.IApplication;
import common.receivers.IMsgReceiver;
import common.virtualNetwork.IRoom;
import provided.datapacket.IDataPacketID;
import provided.discovery.IEndPointData;
import provided.discovery.impl.model.DiscoveryConnector;
import provided.discovery.impl.model.RemoteAPIStubFactory;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataDictionary;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;

/**
 * The mvc model for the client
 *
 */
public class ClientModel {
	/**
	 * The client model logger
	 */
	private final static Logger LOGGER = Logger.getLogger(ClientModel.class.getName());

	/**
	 * The adapter for communication from the client model to the client view
	 */
	private IClientModel2ViewAdpt model2view;

	/**
	 * The client object for handling application level communications
	 */
	private Client client = new Client(null);

	/**
	 * A list of games the client is in
	 */
	private List<RoomController> games = new ArrayList<>();

	/**
	 * Map from connected applications (whom we have sent IConnect to) and
	 * associated rooms
	 */
	private Map<IApplication, Collection<IRoomIdentity>> connectedAppsWithRooms = new HashMap<>();

	/**
	 * The client stub for sending in datapackets
	 */
	private IClient clientStub;

	/**
	 * Discovery connector
	 */
	private DiscoveryConnector discoveryConnector;

	/**
	 * Factory of remote users' stub
	 */
	private RemoteAPIStubFactory<IMsgReceiver<?>> remoteClientFactory;

	/**
	 * RMI utilities for starting RMI and for getting the Registry
	 */
	private IRMIUtils rmiUtils = new RMIUtils((s) -> LOGGER.info(s));

	/**
	 * Processing thread
	 */
	private Thread processingThread;

	/**
	 * Boolean to indicate whether to process messages
	 */
	private boolean processMessages;

	/**
	 * The application mixed data dictionary for commands to use
	 */
	private IMixedDataDictionary mixedDataDictionary = new MixedDataDictionary();

	/**
	 * Handles application level messages
	 */
	private ClientDataPacketVisitor msgProcessor = new ClientDataPacketVisitor(new AAppMsgCmd<IAppMsg>() {
		/**
		 * UID
		 */
		private static final long serialVersionUID = -996275112103944943L;

		@Override
		public Void apply(IDataPacketID index, AppDataPacket<IAppMsg> host, Void... params) {
			LOGGER.info("Client tried to process unknown message with ID " + index + " with data " + host.getData());
			return null;
		}
	});

	/**
	 * Key to use world wind API
	 */
	private String apiKey;

	/**
	 * Create a new client model
	 * 
	 * @param model2view the adapter for communication with the client view
	 */
	public ClientModel(IClientModel2ViewAdpt model2view) {
		this.model2view = model2view;
		// implementCommands();
		processMessages = true;
		processingThread = new Thread(() -> {
			while (processMessages) {
				AppDataPacket<?> msg = client.getMsgToProcess();
				if (msg == null) {
					break;
				}
				LOGGER.info("Processing application message: " + msg.getData().getClass());
				msg.execute(msgProcessor);
			}
			LOGGER.info("Message processing thread shutting down...");
		});
	}

	/**
	 * Sets the player's name
	 * 
	 * @param name the player's name
	 */
	public void setName(String name) {
		this.client.setName(name);
	}

	/**
	 * @param apiKey key to use the world wind API
	 */
	public void setAPIKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * Starts the connection to the discovery server. Binds our client stub to the
	 * discovery server.
	 * 
	 * @param username the player's name to be bound in the discovery server
	 */
	public void startDiscoveryConnector(String username) {
		// Setup RMI garbage
		rmiUtils.startRMI(IRMI_Defs.CLASS_SERVER_PORT_CLIENT);

		try {
			discoveryConnector = new DiscoveryConnector(rmiUtils, username, IMsgReceiver.BOUND_NAME);
			clientStub = (IClient) UnicastRemoteObject.exportObject(client, IRMI_Defs.STUB_PORT_CLIENT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		remoteClientFactory = new RemoteAPIStubFactory<>(rmiUtils);
	}

	/**
	 * Quits the application safely
	 */
	public void quitApplication() {
		processMessages = false;
		processingThread.interrupt();
		try {
			processingThread.join();
		} catch (InterruptedException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		if (discoveryConnector != null) {
			discoveryConnector.disconnect();
		}

		while (!games.isEmpty()) {
			games.get(0).quit();
		}

		connectedAppsWithRooms.keySet().forEach((app) -> {
			System.out.println("Sending IQUITMSG");
			sendMessageAsync(app, IQuitMsg.make(clientStub));
		});

		// Quit RMI stuff
		rmiUtils.stopRMI();
	}

	/**
	 * Starts the application
	 */
	public void start() {
		msgProcessor.setCmd(IAcceptInviteMsg.GetID(), new AAppMsgCmd<IAcceptInviteMsg>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2446255969476181989L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IAcceptInviteMsg> host, Void... params) {
				sendRequestedRoom(host.getData().getRoomIdentity(), host.getSender());
				return null;
			}
		});
		msgProcessor.setCmd(IAddRoomMsg.GetID(), new AAppMsgCmd<IAddRoomMsg>() {

			/**
			 * UID
			 */
			private static final long serialVersionUID = 5759304877209669702L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IAddRoomMsg> host, Void... params) {
				IRoom room = host.getData().getRoom();
				RoomController game = createGameMVC(room);
				game.sendAddMemberMessages();
				return null;
			}
		});
		msgProcessor.setCmd(IConnectMsg.GetID(), new AAppMsgCmd<IConnectMsg>() {

			/**
			 * UID
			 */
			private static final long serialVersionUID = 6409727356842462113L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IConnectMsg> host, Void... params) {
				LOGGER.info("Received IConnect from " + host.getData().appName());
				if (!connectedAppsWithRooms.containsKey(host.getSender())) {
					connectedAppsWithRooms.put(host.getSender(), Collections.emptyList());
					model2view.addConnectedApplication(host.getSender());
				}
				return null;
			}
		});
		msgProcessor.setCmd(IInviteToRoomMsg.GetID(), new AAppMsgCmd<IInviteToRoomMsg>() {

			/**
			 * UID
			 */
			private static final long serialVersionUID = -6947662358679329760L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IInviteToRoomMsg> host, Void... params) {
				IRoomIdentity requestedIdentity = host.getData().getRoom();
				new Thread(() -> {
					IApplication sender = host.getSender();
					// Get sender name
					String senderName = null;
					try {
						senderName = sender.getName();
					} catch (Exception e) {
						LOGGER.log(Level.SEVERE, e.getMessage(), e);
					}
					if (senderName == null) {
						return;
					}

					// Get user input
					int result = JOptionPane.showConfirmDialog(
							null, "Application \"" + senderName + "\" invited you to room \""
									+ requestedIdentity.getName() + "\". Allow?",
							"Room Invitation", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						sendMessageSync(sender, IAcceptInviteMsg.make(requestedIdentity));
					}
				}).start();
				return null;
			}
		});
		msgProcessor.setCmd(IQuitMsg.GetID(), new AAppMsgCmd<IQuitMsg>() {

			/**
			 * UID
			 */
			private static final long serialVersionUID = -1938856130205000930L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IQuitMsg> host, Void... params) {
				connectedAppsWithRooms.remove(host.getSender());
				model2view.removeConnectedApplication(host.getSender());
				return null;
			}
		});
		msgProcessor.setCmd(IRequestToJoinMsg.GetID(), new AAppMsgCmd<IRequestToJoinMsg>() {

			/**
			 * UID
			 */
			private static final long serialVersionUID = -124561552483179992L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IRequestToJoinMsg> host, Void... params) {
				IRoomIdentity requestedIdentity = host.getData().getRoomIdentity();
				new Thread(() -> {
					IApplication sender = host.getSender();
					// Get sender name
					String senderName = null;
					try {
						senderName = sender.getName();
					} catch (Exception e) {
						LOGGER.log(Level.SEVERE, e.getMessage(), e);
					}
					if (senderName == null) {
						return;
					}

					// Get user input
					int result = JOptionPane.showConfirmDialog(
							null, "Application \"" + senderName + "\" asked to join room \""
									+ requestedIdentity.getName() + "\". Allow?",
							"Join Request", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						sendRequestedRoom(requestedIdentity, sender);
					}
				}).start();
				return null;
			}
		});
		msgProcessor.setCmd(IAddAppsMsg.GetID(), new AAppMsgCmd<IAddAppsMsg>() {

			/**
			 * UID
			 */
			private static final long serialVersionUID = -285561467952695222L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IAddAppsMsg> host, Void... params) {
				Collection<IApplication> apps = host.getData().getCollection();
				for (IApplication app : apps) {
					if (!app.equals(client)) {
						model2view.addConnectedApplication(app);
					}
				}
				return null;
			}
		});
		msgProcessor.setCmd(IAddRoomInfosMsg.GetID(), new AAppMsgCmd<IAddRoomInfosMsg>() {

			/**
			 * UID
			 */
			private static final long serialVersionUID = 5716087157357687196L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IAddRoomInfosMsg> host, Void... params) {
				connectedAppsWithRooms.put(host.getSender(), host.getData().getCollection());
				model2view.addConnectedRooms(host.getData().getCollection());
				return null;
			}
		});
		msgProcessor.setCmd(IRequestAppsMsg.GetID(), new AAppMsgCmd<IRequestAppsMsg>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 5949389102945490755L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IRequestAppsMsg> host, Void... params) {
				sendMessageSync(host.getSender(), IAddAppsMsg.make(new HashSet<>(connectedAppsWithRooms.keySet())));
				return null;
			}
		});
		msgProcessor.setCmd(IRequestRoomsMsg.GetID(), new AAppMsgCmd<IRequestRoomsMsg>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5400806142456260781L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IRequestRoomsMsg> host, Void... params) {
				List<IRoomIdentity> rooms = games.stream().map((g) -> g.getRoom().getInfo())
						.collect(Collectors.toList());
				sendMessageSync(host.getSender(), IAddRoomInfosMsg.make(rooms));
				return null;
			}
		});
		msgProcessor.setCmd(IRemoteExceptionMsg.GetID(), new AAppMsgCmd<IRemoteExceptionMsg>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 5413232726966920768L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IRemoteExceptionMsg> host, Void... params) {

				return null;
			}
		});
		msgProcessor.setCmd(IServerCheckStatusMsg.GetID(), new AAppMsgCmd<IServerCheckStatusMsg>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7284933962889893320L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IServerCheckStatusMsg> host, Void... params) {

				return null;
			}
		});
		msgProcessor.setCmd(IFailureMsg.GetID(), new AAppMsgCmd<IFailureMsg>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -6882048427073598645L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IFailureMsg> host, Void... params) {
				LOGGER.severe("Failure message received: " + host.getData().getDescription());
				return null;
			}
		});
		processingThread.start();
	}

	/**
	 * Sends an IRoom with the requested identity to the given requester with an
	 * IAddRoomMsg
	 * 
	 * @param requestedIdentity the IRoom identity
	 * @param requester         the IApplication requesting the room
	 */
	private void sendRequestedRoom(IRoomIdentity requestedIdentity, IApplication requester) {
		// Allow user to join requested room
		IRoom requestedRoom = null;
		// Get requested room
		for (RoomController game : games) {
			if (game.getRoom().getInfo().equals(requestedIdentity)) {
				requestedRoom = game.getRoom();
				break;
			}
		}
		if (requestedRoom == null) {
			LOGGER.warning("Was requested to give room we do not have: " + requestedIdentity.getName());
			sendMessageAsync(requester, IFailureMsg.make("Requested room does not exist"));
		} else {
			sendMessageAsync(requester, IAddRoomMsg.make(requestedRoom));
		}
	}

	/**
	 * @param connectionPoint IApplication who is in the room you want to join
	 * @param roomIdentity Identity of room you want to join
	 */
	public void joinGame(IApplication connectionPoint, IRoomIdentity roomIdentity) {
		LOGGER.info("Joining room " + roomIdentity.getName());
		sendMessageSync(connectionPoint, IRequestToJoinMsg.make(roomIdentity));
	}

	/**
	 * @param room IRoom object that mvc will model
	 * @return controller of mvc
	 */
	public RoomController createGameMVC(IRoom room) {
		// Create new mini-mvc
		RoomController game = null;
		String clientName;
		try {
			clientName = client.getName();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			clientName = "Unknown client name";
		}

		game = new RoomController(new IRoomController2ModelAdpt() {

			@Override
			public void removeGameController(RoomController gameController) {
				model2view.removeGameWindow(gameController.getView());
				games.remove(gameController);
			}
		}, clientName, apiKey, room, mixedDataDictionary, clientStub);
		games.add(game);
		game.start();
		// Create view portion of mini-mvc
		model2view.createNewGameWindow(game.getView());
		return game;
	}

	/**
	 * Connect to the client discovery server
	 * 
	 * @param category  the category
	 * @param watchOnly if only watch in the chatroom
	 * @param updateFn  A Consumer of an iterable of endpoints
	 */
	public void connectToDiscoveryServer(String category, boolean watchOnly,
			Consumer<Iterable<IEndPointData>> updateFn) {
		try {
			discoveryConnector.connectToDiscoveryServer(category, watchOnly, updateFn);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	/**
	 * Handles connection to an arbitrary endpoint on the discovery server, which
	 * can either be an IClient or IServer
	 * 
	 * @param endPoint the endpoint to handle connection to
	 */
	public void connectToEndPoint(IEndPointData endPoint) {
		IMsgReceiver<?> remoteMsgReceiver = null;
		try {
			remoteMsgReceiver = remoteClientFactory.get(endPoint);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		if (remoteMsgReceiver == null) {
			LOGGER.warning("Tried to connect to endpoint, was null");
			return;
		}
		try {
			LOGGER.info("Connecting to remote msg receiver " + remoteMsgReceiver.getName() + " class "
					+ remoteMsgReceiver.getClass());
		} catch (Exception e) {
			LOGGER.warning("Failed to get remote msg receiver name");
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		if (remoteMsgReceiver instanceof IApplication) {
			IApplication app = (IApplication) remoteMsgReceiver;
			connectToApplication(app);
		} else {
			LOGGER.warning(
					"Tried to connect to endpoint, was not an IApplication. Was " + remoteMsgReceiver.getClass());
		}

	}

	/**
	 * @param app Application to connect to
	 */
	public void connectToApplication(IApplication app) {
		if (connectedAppsWithRooms.containsKey(app)) {
			LOGGER.info("Tried to connect to application we were already connected to");
			return;
		}
		boolean successful = false;
		try {
			successful = sendMessageSync(app, IConnectMsg.make(client, client.getName()));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		if (successful) {
			model2view.addConnectedApplication(app);
		}
		connectedAppsWithRooms.put(app, new ArrayList<>());
	}

	/**
	 * @param receiver App to receive message
	 * @param msg Msg to send
	 * @return boolean indicating successful transmission
	 */
	public boolean sendMessageSync(IApplication receiver, IAppMsg msg) {
		try {
			receiver.receiveMsg(new AppDataPacket<IAppMsg>(msg, clientStub));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * Asynchronously sends an application message to the given receiver
	 * 
	 * @param receiver the message receiver
	 * @param msg      the application message
	 */
	public void sendMessageAsync(IApplication receiver, IAppMsg msg) {
		new Thread(() -> {
			try {
				receiver.receiveMsg(new AppDataPacket<IAppMsg>(msg, clientStub));
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
		}).start();
	}

	/**
	 * Sends an IConnectMsg to the given server, and creates a new mini-mvc for
	 * handling the game.
	 * 
	 * @param server the server to connect to
	 */
	public void requestAppRooms(IApplication server) {
		sendMessageAsync(server, IRequestRoomsMsg.make());
	}

	/**
	 * @param app Get connected applications of this application
	 */
	public void getConnectedApplications(IApplication app) {
		sendMessageAsync(app, IRequestAppsMsg.make());
	}

}
