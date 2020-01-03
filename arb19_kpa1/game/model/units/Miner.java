package arb19_kpa1.game.model.units;

import arb19_kpa1.game.model.utility.LatLng;

import common.receivers.IRoomMember;

/**
 * Represents a miner
 *
 */
public class Miner extends AMovableUnit {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 7417559474194427927L;
	/**
	 * The miner's mvmt radius
	 */
	private static final float MOVEMENT_RADIUS = 400;
	/**
	 * The miner's defense
	 */
	private static final float DEFENSE = 0.4f;
	/**
	 * The miner's cost to expedite production
	 */
	private static final int EXPEDITE_GOLD_COST_PER_TURN = 50;
	/**
	 * Number of turns required to produce a miner
	 */
	private static final int NUMBER_TURNS_TO_PRODUCE = 1;

	/**
	 * Creates a new miner
	 * 
	 * @param position the miner's postition
	 * @param owner    the miner's owner
	 */
	public Miner(LatLng position, IRoomMember owner) {
		super(position, owner, 0, 0, DEFENSE, 100, MOVEMENT_RADIUS, EXPEDITE_GOLD_COST_PER_TURN,
				NUMBER_TURNS_TO_PRODUCE);
	}

	@Override
	public void unitKilled() {
		if (gameAdpt.isCurrentPlayerUnit(this)) {
			gameAdpt.displayNotification("A miner has died", position);
		}
	}

	/**
	 * Creates a mine on an instance of mineable land
	 * 
	 * @param mineableLand the land
	 */
	public void buildMine(MineableLand mineableLand) {
		if (position.distance(mineableLand.position) > MOVEMENT_RADIUS) {
			return;
		}

		gameAdpt.createUnit(new Mine(mineableLand.position, owner));
		gameAdpt.removeUnit(mineableLand);
		gameAdpt.removeUnit(this);
	}

	/**
	 * Checks whether an isntance of land is within the reachable distance from the
	 * miner
	 * 
	 * @param mineableLand the ladn
	 * @return whether it is
	 */
	public boolean withinRadius(MineableLand mineableLand) {
		return position.distance(mineableLand.position) <= MOVEMENT_RADIUS;
	}

	@Override
	public void turnBegins() {
		super.turnBegins();
	}

}
