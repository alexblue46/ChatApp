package arb19_kpa1.server.model.mvc.view;

/**
 * Adapter from the server room view to the server room model
 * @author Owner
 *
 */
public interface IServerRoomView2ServerRoomModelAdpt {
	
	/**
	 * Starts the game
	 */
	void startGame();
	
	/**
	 * Deletes the current room
	 */
	void deleteRoom();

}
