package arb19_kpa1.game.model;

import arb19_kpa1.game.model.units.AGameUnit;
import arb19_kpa1.game.model.units.APlayerOwnedUnit;
import arb19_kpa1.game.model.utility.LatLng;
import common.receivers.IRoomMember;
import arb19_kpa1.game.model.utility.LatLngBounds;

/**
 * An adapter that allows communication from individual game units to the game
 * as a whole.
 *
 */
public interface IGameUnit2GameAdpt {
	/**
	 * Updates the game view
	 */
	public void updateView();
	
	/**
	 * @param unit Game unit to create
	 */
	public void createUnit(AGameUnit unit);

	/**
	 * @param unit Game unit to remove
	 */
	public void removeUnit(AGameUnit unit);

	/**
	 * @param unit Game unit to check if dead
	 */
	public void checkUnitDeath(APlayerOwnedUnit unit);

	/**
	 * @return the game bounds
	 */
	public LatLngBounds getGameBounds();

	/**
	 * @return gold amount of current player
	 */
	public int getGoldAmt();

	/**
	 * @param gold amount of gold to set for current player
	 */
	public void setGoldAmt(int gold);

	/**
	 * @param msg String to display on map
	 * @param notificationLoc Location to display location
	 */
	public void displayNotification(String msg, LatLng notificationLoc);

	/**
	 * @param unit Game unit to check if owned by current player
	 * @return boolean indicating whether unit owned by current player
	 */
	public boolean isCurrentPlayerUnit(APlayerOwnedUnit unit);

	/**
	 * @param owner IRoomMember stub
	 * @return team that stub is on
	 */
	public int getTeam(IRoomMember owner);
}
