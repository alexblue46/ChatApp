package arb19_kpa1.client.model.gamemvc.view;

import common.receivers.IApplication;

/**
 * Adapter for communication from the game view to the game model
 *
 */
public interface IRoomModel2RoomControllerAdpt {

	/**
	 * Leave the game
	 */
	void removeGame();

	/**
	 * @return Application-level stub
	 */
	IApplication getClientStub();

}
