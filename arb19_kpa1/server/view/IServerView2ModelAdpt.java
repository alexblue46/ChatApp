package arb19_kpa1.server.view;

/**
 * @author alexbluestein
 */
public interface IServerView2ModelAdpt {
	
	/**
	 * @param serverName name of the server
	 */
	void setServerName(String serverName);

	/**
	 * Quit the application
	 */
	void quitApplication();
	
	/**
	 * @param roomName Name of room to create
	 */
	void createRoom(String roomName);

	/**
	 * @param category Category to use in the discovery panel
	 */
	void setCategory(String category);

}
