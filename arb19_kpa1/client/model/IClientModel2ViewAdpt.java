package arb19_kpa1.client.model;

import java.util.Collection;

import javax.swing.JPanel;

import common.identity.IRoomIdentity;
import common.receivers.IApplication;

/**
 * The adapter for communication from the client model to the client view
 *
 */
public interface IClientModel2ViewAdpt {
	/**
	 * Adds the panel as a new game window in the view
	 * @param panel the mini-mvc view
	 */
	public void createNewGameWindow(JPanel panel);
	
	/**
	 * Remove the game view
	 * @param panel JPanel to remove from the client view
	 */
	public void removeGameWindow(JPanel panel);

	/**
	 * Adds a given server to the dropdown list of connected servers
	 * @param server the server to add
	 */
	public void addConnectedApplication(IApplication server);

	/**
	 * @param collection Rooms to add to view
	 */
	public void addConnectedRooms(Collection<IRoomIdentity> collection);

	/**
	 * @param sender App to remove connection from on the view
	 */
	public void removeConnectedApplication(IApplication sender);

}
