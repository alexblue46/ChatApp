package arb19_kpa1.game.model.units;

import arb19_kpa1.game.model.utility.LatLng;
import common.receivers.IRoomMember;

/**
 * A mine in the game
 *
 */
public class Mine extends ANonMovableUnit {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -2395470412122252048L;

	/**
	 * The mine's defense
	 */
	private static final float DEFENSE = 0.2f;

	/**
	 * Amount of gold a mine gives per turn
	 */
	private static final int GOLD_PER_TURN = 100;

	/**
	 * Creates a new mine
	 * @param position the mine's position
	 * @param owner the mine's owner
	 */
	protected Mine(LatLng position, IRoomMember owner) {
		super(position, owner, 0, 0, DEFENSE, 100);
	}

	@Override
	public void unitKilled() {
		if (gameAdpt.isCurrentPlayerUnit(this)) {
			gameAdpt.displayNotification("Your mine has been destroyed", position);
		}
		gameAdpt.createUnit(new MineableLand(position));
	}

	@Override
	public void turnBegins() {
		gameAdpt.setGoldAmt(gameAdpt.getGoldAmt() + GOLD_PER_TURN);
	}
	
	/**
	 * Gets the amount of gold per turn
	 * @return gpt
	 */
	public int getGoldPerTurn() {
		return GOLD_PER_TURN;
	}

}
