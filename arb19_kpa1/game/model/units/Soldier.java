package arb19_kpa1.game.model.units;

import arb19_kpa1.game.model.utility.LatLng;
import common.receivers.IRoomMember;

/**
 * REpresents a soldier
 * 
 * @author Owner
 *
 */
public class Soldier extends AMovableUnit {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -5964326536218039277L;
	/**
	 * The soldiers attack on movable units
	 */
	private static final float NON_MOVABLE_UNIT_ATTACK = 30f;
	/**
	 * ATtack stat on movable units
	 */
	private static final float MOVABLE_UNIT_ATTACK = 150f;
	/**
	 * Movement radius
	 */
	private static final float MOVEMENT_RADIUS = 350f;
	/**
	 * Defense stat
	 */
	private static final float DEFENSE = 0.8f;
	/**
	 * Cost to expedite production per turn
	 */
	private static final int EXPEDITE_GOLD_COST_PER_TURN = 100;
	/**
	 * Number of turns to produce a soldier
	 */
	private static final int NUMBER_TURNS_TO_PRODUCE = 2;

	/**
	 * Multiplier for how much stronger merged soldiers are
	 */
	private static final float MERGE_ATTACK_CONSTANT = 2f;

	/**
	 * Creates a new soldier
	 * 
	 * @param position the unit's position
	 * @param owner    the unit's owner
	 */
	public Soldier(LatLng position, IRoomMember owner) {
		super(position, owner, NON_MOVABLE_UNIT_ATTACK, MOVABLE_UNIT_ATTACK, DEFENSE, 100, MOVEMENT_RADIUS,
				EXPEDITE_GOLD_COST_PER_TURN, NUMBER_TURNS_TO_PRODUCE);
	}

	/**
	 * Merges this soldier with another
	 * @param other the other soldier
	 */
	public void merge(Soldier other) {
		if (position.distance(other.position) > MOVEMENT_RADIUS) {
			return;
		}
		this.health = (this.health + other.health) / 2;
		this.movableUnitAttack = (float) (this.movableUnitAttack + other.movableUnitAttack
				+ MERGE_ATTACK_CONSTANT * Math.log(this.movableUnitAttack + other.movableUnitAttack));
		this.nonmovableUnitAttack = (float) (this.nonmovableUnitAttack + other.nonmovableUnitAttack
				+ MERGE_ATTACK_CONSTANT * Math.log(this.nonmovableUnitAttack + other.nonmovableUnitAttack));
		this.defense = 1 - (1 - this.defense) * (1 - other.defense);
		gameAdpt.removeUnit(other);
		this.alreadyMoved = true;
	}

	@Override
	public void unitKilled() {
		if (gameAdpt.isCurrentPlayerUnit(this)) {
			gameAdpt.displayNotification("A soldier has died", position);
		}
	}

	@Override
	public void turnBegins() {
		super.turnBegins();

	}

	@Override
	public String toString() {
		return "Soldier";
	}

}
