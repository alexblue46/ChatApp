package arb19_kpa1.server.model.mvc.controller;

/**
 * @author alexbluestein
 * Adapter from server room controller to server model
 */
public interface IServerController2ServerModelAdpt {
	/**
	 * @param gameController Controller to remove room of
	 */
	void removeRoom(ServerRoomController gameController);
}
