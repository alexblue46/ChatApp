package arb19_kpa1.server.model;

import java.util.Collection;

import arb19_kpa1.server.model.mvc.view.ServerRoomView;
import common.receivers.IApplication;

/**
 * Adapter from the server model to the server view.
 *
 */
public interface IServerModel2ViewAdpt {
	/**
	 * Displays a given string in the server view
	 * 
	 * @param text the string to display
	 */
	void displayText(String text);

	/**
	 * Adds a room to the view
	 * 
	 * @param name     the room title
	 * @param gameView the room view
	 */
	void addRoom(String name, ServerRoomView gameView);

	/**
	 * Removes a given room
	 * 
	 * @param gameView the view
	 */
	void removeRoom(ServerRoomView gameView);

	/**
	 * Updates the list of connection applications
	 * 
	 * @param apps the new list
	 */
	void updateConnectedApps(Collection<IApplication> apps);
}
