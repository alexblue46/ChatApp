package arb19_kpa1.game.model.units;

import arb19_kpa1.game.model.utility.LatLng;
import common.receivers.IRoomMember;

/**
 * A movable unit in the game, which can be produced in a city, and can attack
 * things.
 *
 */
public abstract class AMovableUnit extends APlayerOwnedUnit {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -3096141458661414977L;
	/**
	 * This unit's cost to expedite production in a city, per turn
	 */
	protected int expediteGoldCostPerTurn;
	/**
	 * The number of turns to produce this unit in a city
	 */
	protected int numberTurnsToProduce;
	/**
	 * The movement radius of this unit
	 */
	protected float movementRadius;
	/**
	 * Whether this unit has moved this turn
	 */
	protected boolean alreadyMoved;

	/**
	 * Creates a new MovableUnit
	 * @param position the position
	 * @param owner the unit's IRoomMember owner
	 * @param nonmovableUnitAttack the attack value for nonmovable units
	 * @param movableUnitAttack the attack value for movable units
	 * @param defense the defense value
	 * @param health the unit's initial health
	 * @param movementRadius the unit's radius of attack and movement
	 * @param expediteGoldCostPerTurn the unit's gold cost per turn to expedite production
	 * @param numberTurnsToProduce the number of turns it takes to produce
	 */
	protected AMovableUnit(LatLng position, IRoomMember owner, float nonmovableUnitAttack, float movableUnitAttack,
			float defense, int health, float movementRadius, int expediteGoldCostPerTurn, int numberTurnsToProduce) {
		super(position, owner, nonmovableUnitAttack, movableUnitAttack, defense, health);
		this.movementRadius = movementRadius;
		this.expediteGoldCostPerTurn = expediteGoldCostPerTurn;
		this.numberTurnsToProduce = numberTurnsToProduce;
	}

	/**
	 * Move this unit to a given location if possible
	 * @param newPos the new location
	 * @return whether the move was successful
	 */
	public boolean move(LatLng newPos) {
		if (alreadyMoved || position.distance(newPos) > movementRadius || !gameAdpt.getGameBounds().contains(newPos)) {
			return false;
		}
		position = newPos;
		alreadyMoved = true;
		gameAdpt.updateView();
		return true;
	}

	/**
	 * Attacks another movable unit.
	 * 
	 * @param other the other movable unit
	 * @return whether the attack was successful
	 */
	public boolean attack(AMovableUnit other) {
		if (alreadyMoved || position.distance(other.position) > movementRadius) {
			return false;
		}
		other.health -= this.movableUnitAttack * (1 - other.defense);
		this.health -= other.movableUnitAttack * (1 - this.defense);
		other.health = other.health < 0 ? 0 : other.health;
		this.health = this.health < 0 ? 0 : this.health;
		this.alreadyMoved = true;
		gameAdpt.checkUnitDeath(this);
		gameAdpt.checkUnitDeath(other);
		gameAdpt.updateView();
		return true;
	}

	/**
	 * Attacks a non movable unit
	 * 
	 * @param other the non movable unit
	 * @return whether the attack was successful
	 */
	public boolean attack(ANonMovableUnit other) {
		if (alreadyMoved || position.distance(other.position) > movementRadius) {
			return false;
		}
		other.health -= this.nonmovableUnitAttack * (1 - other.defense);
		other.health = other.health < 0 ? 0 : other.health;
		this.alreadyMoved = true;
		gameAdpt.checkUnitDeath(this);
		gameAdpt.checkUnitDeath(other);
		gameAdpt.updateView();
		return true;
	}

	/**
	 * Gets whether the other unit is within this game unit's radius
	 * 
	 * @param other the other unit
	 * @return whether the other unit is within the radius
	 */
	public boolean withinRadius(AGameUnit other) {
		return !(alreadyMoved || position.distance(other.position) > movementRadius);
	}

	/**
	 * Returns whether the location is a possible location to move to
	 * 
	 * @param newPos the location
	 * @return whether the location is possible
	 */
	public boolean isMovePossible(LatLng newPos) {
		return !(alreadyMoved || position.distance(newPos) > movementRadius
				|| !gameAdpt.getGameBounds().contains(newPos));
	}

	@Override
	public void turnBegins() {
		alreadyMoved = false;
	}

	/**
	 * Gets whether this unit has already moved this turn
	 * 
	 * @return whether the unit has already moved
	 */
	public boolean getAlreadyMoved() {
		return alreadyMoved;
	}

	/**
	 * Gets the unit's movement radius
	 * 
	 * @return the unit's movement radius
	 */
	public float getMovementRadius() {
		return movementRadius;
	}

	/**
	 * Gets this unit's cost to expedite production in a city, per turn
	 * 
	 * @return the per turn cost
	 */
	public int getExpediteGoldCostPerTurn() {
		return expediteGoldCostPerTurn;
	}

	/**
	 * Gets the number of turns it takes to produce this unit in a city
	 * 
	 * @return the number of turns
	 */
	public int getNumberTurnsToProduce() {
		return numberTurnsToProduce;
	}

}
