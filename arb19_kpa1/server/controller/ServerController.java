package arb19_kpa1.server.controller;

import java.awt.EventQueue;
import java.util.Collection;

import arb19_kpa1.server.model.IServerModel2ViewAdpt;
import arb19_kpa1.server.model.ServerModel;
import arb19_kpa1.server.model.mvc.view.ServerRoomView;
import arb19_kpa1.server.view.IServerView2ModelAdpt;
import arb19_kpa1.server.view.ServerViewFrame;
import common.receivers.IApplication;

/**
 * The mvc controller for the server application
 *
 */
public class ServerController {

	/**
	 * The mvc model for the server application
	 */
	private ServerModel model;

	/**
	 * The mvc view for the server application
	 */
	private ServerViewFrame view;

	/**
	 * Create a new server controller
	 */
	public ServerController() {
		this.model = new ServerModel(new IServerModel2ViewAdpt() {

			@Override
			public void displayText(String text) {
				view.displayText(text);

			}

			@Override
			public void addRoom(String name, ServerRoomView gameView) {
				view.addGame(name, gameView);
			}

			@Override
			public void removeRoom(ServerRoomView gameView) {
				view.removeGame(gameView);
			}

			@Override
			public void updateConnectedApps(Collection<IApplication> apps) {
				view.updateConnectedApps(apps);
			}

		});

		this.view = new ServerViewFrame(new IServerView2ModelAdpt() {

			@Override
			public void setServerName(String serverName) {
				model.setName(serverName);
			}

			@Override
			public void quitApplication() {
				model.quitApplication();
			}

			@Override
			public void createRoom(String roomName) {
				model.createRoom(roomName);
			}

			@Override
			public void setCategory(String category) {
				model.setCategory(category);
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
	 * Launch the server application.
	 * 
	 * @param args Arguments given by the system or command line. N/A.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerController controller = new ServerController(); // instantiate the system
					controller.start(); // start the system
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
