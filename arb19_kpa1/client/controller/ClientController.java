package arb19_kpa1.client.controller;

import java.awt.EventQueue;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import arb19_kpa1.client.model.ClientModel;
import arb19_kpa1.client.model.IClient;
import arb19_kpa1.client.model.IClientModel2ViewAdpt;
import arb19_kpa1.client.view.ClientView;
import arb19_kpa1.client.view.IClientView2ModelAdpt;
import arb19_kpa1.client.view.ViewApplicationContainer;
import arb19_kpa1.client.view.ViewRoomIdentityContainer;
import arb19_kpa1.server.model.IServer;
import common.identity.IRoomIdentity;
import common.receivers.IApplication;
import provided.discovery.IEndPointData;
import provided.discovery.impl.view.IDiscoveryPanelAdapter;

/**
 * The main mvc controller for the client
 *
 */
public class ClientController {
	
	/**
	 * The logger for ClientController
	 */
	private final static Logger LOGGER = Logger.getLogger(ClientController.class.getName());

	/**
	 * The client's model
	 */
	private ClientModel model;

	/**
	 * The client's view
	 */
	private ClientView<IServer, IClient> view;

	/**
	 * Create a new client controller
	 */
	public ClientController() {
		LOGGER.setLevel(Level.ALL);
		this.model = new ClientModel(new IClientModel2ViewAdpt() {

			@Override
			public void createNewGameWindow(JPanel panel) {
				view.createNewGameWindow(panel);
			}

			@Override
			public void addConnectedApplication(IApplication app) {
				view.addConnectedApplication(new ViewApplicationContainer(app));
			}

			@Override
			public void removeGameWindow(JPanel panel) {
				view.removeGameWindow(panel);
			}

			@Override
			public void addConnectedRooms(Collection<IRoomIdentity> rooms) {
				view.addRooms(rooms.stream().map((r) -> new ViewRoomIdentityContainer(r)).collect(Collectors.toList()));
			}

			@Override
			public void removeConnectedApplication(IApplication app) {
				view.removeConnectedApplication(new ViewApplicationContainer(app));
			}

		});
		this.view = new ClientView<IServer, IClient>(new IClientView2ModelAdpt<IServer>() {

			@Override
			public void getAppGames(ViewApplicationContainer serverStub) {
				model.requestAppRooms(serverStub.getItem());
			}

			@Override
			public void setName(String name) {
				model.setName(name);
			}

			@Override
			public void quitApplication() {
				model.quitApplication();
			}

			@Override
			public void startDiscoveryServer(String name) {
				model.startDiscoveryConnector(name);
				
			}

			@Override
			public void setAPIKey(String apiKey) {
				model.setAPIKey(apiKey);
			}

			@Override
			public void joinGame(ViewApplicationContainer serverStub, ViewRoomIdentityContainer game) {
				model.joinGame(serverStub.getItem(), game.getItem());
			}


			@Override
			public void getConnectedApplications(ViewApplicationContainer app) {
				model.getConnectedApplications(app.getItem());
			}
		}, new IDiscoveryPanelAdapter<IEndPointData>() {
			@Override
			public void connectToDiscoveryServer(String category, boolean watchOnly,
					Consumer<Iterable<IEndPointData>> endPtsUpdateFn) {
				model.connectToDiscoveryServer(category, watchOnly, endPtsUpdateFn);
			}

			@Override
			public void connectToEndPoint(IEndPointData clientEndPoint) {
				model.connectToEndPoint(clientEndPoint);
			}
		});
	}

	/**
	 * Start the controller.
	 */
	public void start() {
		model.start();
		view.start();
	}

	/**
	 * Launch the application.
	 * 
	 * @param args Arguments given by the system or command line.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientController controller = new ClientController(); // instantiate the system
					controller.start(); // start the system
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
