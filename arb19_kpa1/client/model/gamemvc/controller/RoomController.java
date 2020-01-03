package arb19_kpa1.client.model.gamemvc.controller;

import java.util.function.Supplier;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;

import arb19_kpa1.client.model.gamemvc.model.IRoomModel2RoomViewAdpt;
import arb19_kpa1.client.model.gamemvc.model.RoomModel;
import arb19_kpa1.client.model.gamemvc.view.IRoomModel2RoomControllerAdpt;
import arb19_kpa1.client.model.gamemvc.view.IRoomView2RoomModelAdpt;
import arb19_kpa1.client.model.gamemvc.view.RoomView;
import common.ICmd2ModelAdapter;
import common.msg.IRoomMsg;
import common.receivers.IApplication;
import common.receivers.IRoomMember;
import common.virtualNetwork.IRoom;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import arb19_kpa1.client.model.IClient;

/**
 * The mini-mvc controller for a game
 *
 */
public class RoomController {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = Logger.getLogger(RoomController.class.getName());
	
	/**
	 * The game model
	 */
	private RoomModel model;

	/**
	 * The game view
	 */
	private RoomView view;

	/**
	 * Create a new mini-mvc controller
	 * 
	 * @param adpt            Adapter to the model
	 * @param clientName      Name of the client
	 * @param worldwindAPIKey Key to use the world wind API
	 * @param room			  Room that you are in
	 * @param mixedDataDictionary MDD for the room
	 * @param clientStub          Own client stub
	 */
	public RoomController(IRoomController2ModelAdpt adpt, String clientName, String worldwindAPIKey, IRoom room,
			IMixedDataDictionary mixedDataDictionary, IClient clientStub) {
		RoomController self = this;
		this.model = new RoomModel(new IRoomModel2RoomViewAdpt() {

			@Override
			public void displayGlobalMsg(String senderName, String text) {
				view.displayText(senderName + ": " + text + "\n");
			}

			@Override
			public void setMembers(IRoom room) {
				view.setMembers(room);	
			}
		}, new IRoomModel2RoomControllerAdpt() {
			
			@Override
			public void removeGame() {
				adpt.removeGameController(self);
			}

			@Override
			public IApplication getClientStub() {
				return clientStub;
			}
		}, new ICmd2ModelAdapter() {

			@Override
			public void displayComponent(Supplier<JComponent> compFac, String title) {
				view.addGameTab(compFac.get(), title);
			}

			@Override
			public void displayText(String text) {
				view.displayText(text);
			}

			@Override
			public void sendMessageToMember(IRoomMsg message, IRoomMember member) {
				LOGGER.info("Sending message from cmd: " + message);
				model.getNetworkUtil().sendMessageAsync(member, message);
			}

			@Override
			public void sendMessageToAll(IRoomMsg message) {
				model.getNetworkUtil().sendMessageToAll(message);
			}

			@Override
			public <T> void putItemInDB(MixedDataKey<T> key, T value) {
				LOGGER.info("Storing key " + key + " in DB with value " + value);

				mixedDataDictionary.put(key, value);
			}

			@Override
			public <T> T getItemInDB(MixedDataKey<T> key) {
				return mixedDataDictionary.get(key);
			}

			@Override
			public String getAPIKey() {
				return worldwindAPIKey;
			}
		}, clientName, room);

		this.view = new RoomView(new IRoomView2RoomModelAdpt() {

			@Override
			public void sendStringMessage(String text) {
				model.sendStringMessage(text);
			}

			@Override
			public void leaveRoom() {
				model.stop();
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
	 * Gets the mini-mvc game view
	 * 
	 * @return the view
	 */
	public JPanel getView() {
		return view;
	}

	/**
	 * Quit the game
	 */
	public void quit() {
		model.stop();
	}

	/**
	 * Add member
	 */
	public void sendAddMemberMessages() {
		model.sendAddMemberMessages();
	}

	/**
	 * @return room that you are in
	 */
	public IRoom getRoom() {
		return model.getRoom();
	}
}
