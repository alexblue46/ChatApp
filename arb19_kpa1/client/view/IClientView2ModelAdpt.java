package arb19_kpa1.client.view;

/**
 * The adapter for communication from the client view to the client model
 *
 * @param <ServerT> the type of the server
 */
public interface IClientView2ModelAdpt<ServerT> {
	/**
	 * Initialize the discovery server
	 * @param name the player's name
	 */
	public void startDiscoveryServer(String name);
	/**
	 * Connects to a given server
	 * @param serverStub the server to connect to
	 */
	public void getAppGames(ViewApplicationContainer serverStub);
	/**
	 * Sets the client's name
	 * @param name the client's name
	 */
	public void setName(String name);
	/**
	 * Safely quits the model
	 */
	public void quitApplication();
	/**
	 * @param apiKey Key to use the world wind API
	 */
	public void setAPIKey(String apiKey);
	/**
	 * @param serverStub Stub of server
	 * @param viewRoomIdentityContainer Container for the room identity
	 */
	public void joinGame(ViewApplicationContainer serverStub, ViewRoomIdentityContainer viewRoomIdentityContainer);
	/**
	 * @param itemAt Container of app to get connected apps from
	 */
	public void getConnectedApplications(ViewApplicationContainer itemAt);
}
