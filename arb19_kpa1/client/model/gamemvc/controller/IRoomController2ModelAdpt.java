package arb19_kpa1.client.model.gamemvc.controller;

/**
 * @author alexbluestein
 * Adapter from the game controller to the client model
 */
public interface IRoomController2ModelAdpt {
	
	/**
	 * @param gameController Game to remove from the client
	 */
	void removeGameController(RoomController gameController);

}
