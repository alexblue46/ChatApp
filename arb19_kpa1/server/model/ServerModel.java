package arb19_kpa1.server.model;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import arb19_kpa1.server.model.mvc.controller.IServerController2ServerModelAdpt;
import arb19_kpa1.server.model.mvc.controller.ServerRoomController;
import common.cmd.AAppMsgCmd;
import common.identity.IRoomIdentity;
import common.msg.IAppMsg;
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
import common.packet.AppDataPacket;
import common.receivers.IApplication;
import common.receivers.IMsgReceiver;
import common.virtualNetwork.IRoom;
import provided.datapacket.IDataPacketID;
import provided.discovery.impl.model.DiscoveryConnector;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;


/**
 * The mvc model for the server application
 *
 */
public class ServerModel {
	
	/**
	 * The server model logger
	 */
	private static final Logger LOGGER = Logger.getLogger(ServerModel.class.getName());
	
	/**
	 * the rmi object
	 */
	private RMIUtils rmi;

	/**
	 * the rmi registry
	 */
	private Registry registry;
	
	/**
	 * the discovery connector
	 */
	private DiscoveryConnector discConn;
	
	/**
	 * IServer object to receive messages
	 */
	private Server server = new Server(null);
	
	/**
	 * IServer stub to bind to local registry
	 */
	private IServer serverStub;
	
	/**
	 * Adapter from the server model to the server view
	 */
	private IServerModel2ViewAdpt model2view;
	
	/**
	 * Collection of rooms and their identities
	 */
	private Map<IRoomIdentity, IRoom> rooms = new HashMap<IRoomIdentity, IRoom>();
	
	
	/**
	 * Collection of connected IApplications
	 */
	private Collection<IApplication> connectedApps = new HashSet<IApplication>();
	
	/**
	 * All rooms
	 */
	private Collection<ServerRoomController> roomControllers = new HashSet<ServerRoomController>();
	
	/**
	 * Processing thread
	 */
	private Thread processingThread;
	
	/**
	 * Boolean to indicate whether to process messages
	 */
	private boolean processMessages;
	
	/**
	 * Datapacket handler for the server application
	 */
	private ServerDataPacketVisitor msgProcessor = new ServerDataPacketVisitor(new AAppMsgCmd<IAppMsg>() {

		/**
		 * Serialization
		 */
		private static final long serialVersionUID = -1891444802323433828L;

		@Override
		public Void apply(IDataPacketID index, AppDataPacket<IAppMsg> host, Void... params) {
			LOGGER.info("Server tried to process unknown message with ID " + index + " with data " + host.getData());
			return null;
		}
	});
	
	/**
	 * @param model2view Adapter from the server model to the server view
	 */
	public ServerModel(IServerModel2ViewAdpt model2view) {
		this.model2view = model2view;
		processMessages = true;
		processingThread = new Thread(() -> {
			while (processMessages) {
				AppDataPacket<?> msg = server.getMsgToProcess();
				if (msg == null) {
					break;
				}
				LOGGER.info("MESSAGE RECEIVED: " + msg.getData().getClass());
				msg.execute(msgProcessor);
			}
			LOGGER.info("Message processing thread shutting down...");
		});
	}
	
	/**
	 * Start the server model
	 */
	public void start() {
		
		msgProcessor.setCmd(IConnectMsg.GetID(), new AAppMsgCmd<IConnectMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -3327728366471536931L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IConnectMsg> host, Void... params) {
				try {
					LOGGER.info("Connect msg from " + host.getSender().getName());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				connectedApps.add(host.getSender());
				model2view.updateConnectedApps(connectedApps);
				return null;
			}

		});
		
		msgProcessor.setCmd(IQuitMsg.GetID(), new AAppMsgCmd<IQuitMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 1391250824242647268L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IQuitMsg> host, Void... params) {
				try {
					LOGGER.info("Quit msg from " + host.getSender().getName());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				connectedApps.remove(host.getSender());
				model2view.updateConnectedApps(connectedApps);
				return null;
			}

		});
		
		msgProcessor.setCmd(IRequestRoomsMsg.GetID(), new AAppMsgCmd<IRequestRoomsMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 8896213915236590710L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IRequestRoomsMsg> host, Void... params) {
				try {
					LOGGER.info("Request rooms msg from " + host.getSender().getName());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				try {
					host.getSender().receiveMsg(new AppDataPacket<IAddRoomInfosMsg>(IAddRoomInfosMsg.make(new HashSet<IRoomIdentity>(rooms.keySet())), serverStub));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return null;
			}});
		
		msgProcessor.setCmd(IRequestAppsMsg.GetID(), new AAppMsgCmd<IRequestAppsMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -6894508648806169601L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IRequestAppsMsg> host, Void... params) {
				try {
					LOGGER.info("Request apps msg from " + host.getSender().getName());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				try {
					host.getSender().receiveMsg(new AppDataPacket<IAddAppsMsg>(IAddAppsMsg.make(connectedApps), serverStub));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return null;
			}});
		
		msgProcessor.setCmd(IAddRoomMsg.GetID(), new AAppMsgCmd<IAddRoomMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 6717914882873414154L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IAddRoomMsg> host, Void... params) {
				try {
					LOGGER.info("Add room msg from " + host.getSender().getName());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return null;
			}
			
		});
		
		msgProcessor.setCmd(IInviteToRoomMsg.GetID(), new AAppMsgCmd<IInviteToRoomMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = -8088502240671864505L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IInviteToRoomMsg> host, Void... params) {
				try {
					LOGGER.info("Invite to room msg from " + host.getSender().getName());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return null;
			}
			
		});
		
		msgProcessor.setCmd(IRequestToJoinMsg.GetID(), new AAppMsgCmd<IRequestToJoinMsg>() {

			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 1469835748982525672L;

			@Override
			public Void apply(IDataPacketID index, AppDataPacket<IRequestToJoinMsg> host, Void... params) {
				try {
					LOGGER.info("Request to join msg from " + host.getSender().getName());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				
				if (rooms.get(host.getData().getRoomIdentity()) == null) {
					try {
						host.getSender().receiveMsg(new AppDataPacket<IFailureMsg>(IFailureMsg.make("Requested room does not exist"), serverStub));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				} else {
					try {
						host.getSender().receiveMsg(new AppDataPacket<IAddRoomMsg>(IAddRoomMsg.make(rooms.get(host.getData().getRoomIdentity())), serverStub));
						LOGGER.info("Room sent to " + host.getSender().getName());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				return null;
			}
			
		});
		
		processingThread.start();
	}
	
	/**
	 * @param name Name of the server
	 */
	public void setName(String name) {
		
		this.server.setName(name);
		
		this.rmi = new RMIUtils(model2view::displayText);
		rmi.startRMI(IRMI_Defs.CLASS_SERVER_PORT_SERVER);
		registry = rmi.getLocalRegistry();
		
		try {
			serverStub = (IServer) UnicastRemoteObject.exportObject(server, IRMI_Defs.STUB_PORT_SERVER);
			registry.bind(IMsgReceiver.BOUND_NAME, serverStub);
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
		
		try {
			discConn = new DiscoveryConnector(rmi, name, IMsgReceiver.BOUND_NAME);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Quit the application
	 */
	public void quitApplication() {
		processMessages = false;
		processingThread.interrupt();
		try {
			processingThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		roomControllers.forEach(controller -> controller.getModel().deleteRoom());
		connectedApps.forEach(app -> {
			try {
				app.receiveMsg(new AppDataPacket<IQuitMsg>(IQuitMsg.make(serverStub), serverStub));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		});
		
		rmi.stopRMI();
	}
	
	/**
	 * Creates a new game room
	 * @param roomName the name of the room
	 */
	public void createRoom(String roomName) {
		
		ServerRoomController gameController = new ServerRoomController(
				new IServerController2ServerModelAdpt() {

					@Override
					public void removeRoom(ServerRoomController gameController) {
						model2view.removeRoom(gameController.getView());
						rooms.remove(gameController.getModel().getGameRoom().getInfo());
						roomControllers.remove(gameController);
					}}, 
				roomName, serverStub);
		
		rooms.put(gameController.getModel().getGameRoom().getInfo(), gameController.getModel().getGameRoom());
		
		roomControllers.add(gameController);
		
		model2view.addRoom(gameController.getModel().getGameRoom().getInfo().getName(), gameController.getView());
	}
	
	/**
	 * Sets the discovery server category
	 * @param category the category
	 */
	public void setCategory(String category) {
		try {
			this.discConn.connectToDiscoveryServer(category, false, (endpts)->{});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	

}
