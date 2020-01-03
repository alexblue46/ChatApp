package arb19_kpa1.client.model.gamemvc.view;

/**
 * Adapter for communication from the game view to the game model
 *
 */
public interface IRoomView2RoomModelAdpt {

	/**
	 * @param text String message contents to be sent to the entire game
	 */
	void sendStringMessage(String text);

	/**
	 * Leave the game
	 */
	void leaveRoom();

}
