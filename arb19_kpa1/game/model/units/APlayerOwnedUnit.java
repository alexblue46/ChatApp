package arb19_kpa1.game.model.units;

import common.receivers.IRoomMember;
import arb19_kpa1.game.model.utility.LatLng;

/**
 * A player owned unit
 *
 */
public abstract class APlayerOwnedUnit extends AGameUnit {
	/**
	 * UID
	 */
	private static final long serialVersionUID = 5509390155327343228L;
	/**
	 * THe unit's owner
	 */
	protected IRoomMember owner;
	/**
	 * The unit's attack stat against nonmovable units
	 */
	protected float nonmovableUnitAttack;
	/**
	 * The unit's attack stat against movable units
	 */
	protected float movableUnitAttack;
	/**
	 * The unit's defense stat
	 */
	protected float defense;
	/**
	 * The unit's health
	 */
	protected int health;

	/**
	 * Creates a new player owned unit
	 * @param position the unit's position
	 * @param owner the unit's owner
	 * @param nonmovableUnitAttack the unit's attack stat against nonmovable units
	 * @param movableUnitAttack the unit's attack stat against movable units
	 * @param defense the unit's defense stat
	 * @param health the unit's initial health
	 */
	protected APlayerOwnedUnit(LatLng position, IRoomMember owner, float nonmovableUnitAttack, float movableUnitAttack,
			float defense, int health) {
		super(position);
		this.owner = owner;
		this.nonmovableUnitAttack = nonmovableUnitAttack;
		this.movableUnitAttack = movableUnitAttack;
		this.defense = defense;
		this.health = health;
	}

	/**
	 * Called when the owner's turn begins
	 */
	public abstract void turnBegins();

	/**
	 * Gets this unit's owner
	 * @return the owner
	 */
	public IRoomMember getOwner() {
		return owner;
	}

	/**
	 * Gets this unit's nonmovable attack stat
	 * @return the stat
	 */
	public float getNonmovableUnitAttack() {
		return nonmovableUnitAttack;
	}

	/**
	 * Gets this unit's movable attack stat
	 * @return the movable attack stat
	 */
	public float getMovableUnitAttack() {
		return movableUnitAttack;
	}

	/**
	 * Gets this unit's defense stat
	 * @return the unit's defense
	 */
	public float getDefense() {
		return defense;
	}

	/**
	 * Gets the unit's current health
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Gets whether this unit is dead
	 * @return whether this unit is dead
	 */
	public boolean isDead() {
		return health <= 0;
	}

}
