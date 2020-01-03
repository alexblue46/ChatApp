package arb19_kpa1.game.model.units;

import arb19_kpa1.game.model.utility.LatLng;

import common.receivers.IRoomMember;

/**
 * Represents a siege unit
 *
 */
public class SiegeUnit extends AMovableUnit {
	/**
	 * UID
	 */
	private static final long serialVersionUID = -1768482921800824214L;
	/**
	 * The unit attack on nonmovable units
	 */
	private static final float NON_MOVABLE_UNIT_ATTACK = 160f;
	/**
	 * The unit's attack stat on movable units
	 */
	private static final float MOVABLE_UNIT_ATTACK = 40f;
	/**
	 * The movement radius for the unit
	 */
	private static final float MOVEMENT_RADIUS = 250;
	/**
	 * The defense stat for the unit
	 */
	private static final float DEFENSE = 0.65f;
	/**
	 * The gold cost for the unit
	 */
	private static final int EXPEDITE_GOLD_COST_PER_TURN = 200;
	/**
	 * The number of turns to produce the unit
	 */
	private static final int NUMBER_TURNS_TO_PRODUCE = 4;

	/**
	 * Creates a a new siege unit
	 * 
	 * @param position the unit's position
	 * @param owner    the unit's owner
	 */
	public SiegeUnit(LatLng position, IRoomMember owner) {
		super(position, owner, NON_MOVABLE_UNIT_ATTACK, MOVABLE_UNIT_ATTACK, DEFENSE, 100, MOVEMENT_RADIUS,
				EXPEDITE_GOLD_COST_PER_TURN, NUMBER_TURNS_TO_PRODUCE);
	}

	@Override
	public void unitKilled() {
		if (gameAdpt.isCurrentPlayerUnit(this)) {
			gameAdpt.displayNotification("A siege unit has been destroyed", position);
		}
	}

	@Override
	public void turnBegins() {
		super.turnBegins();

	}

	@Override
	public String toString() {
		return "Siege Unit";
	}

}
