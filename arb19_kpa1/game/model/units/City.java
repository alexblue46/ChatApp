package arb19_kpa1.game.model.units;

import arb19_kpa1.game.model.utility.LatLng;

import common.receivers.IRoomMember;

/**
 * A city in the game
 *
 */
public class City extends ANonMovableUnit {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 3989921608913986848L;
	/**
	 * The attack for cities against nonmovable units
	 */
	private static final float NON_MOVABLE_UNIT_ATTACK = 0;
	/**
	 * The attack for cities against movable units
	 */
	private static final float MOVABLE_UNIT_ATTACK = 40.0f;
	/**
	 * The defense stat for cities
	 */
	private static final float DEFENSE = 0.8f;
	/**
	 * The attack radius for cities
	 */
	private static final float ATTACK_RADIUS = 250;

	/**
	 * The unit currently under production, or null if none
	 */
	protected AMovableUnit currentProduction;
	/**
	 * The number of turns left in the production
	 */
	protected int productionNumTurnsLeft;

	/**
	 * Whether the city has already attacked this turn
	 */
	protected boolean alreadyAttacked;
	/**
	 * The amount of gold the city has
	 */
	protected int goldAmt;

	/**
	 * Creates a new city
	 * @param position the cities position
	 * @param owner the city's owner
	 * @param goldAmt the city's amount of gold
	 */
	public City(LatLng position, IRoomMember owner, int goldAmt) {
		super(position, owner, NON_MOVABLE_UNIT_ATTACK, MOVABLE_UNIT_ATTACK, DEFENSE, 100);
		this.goldAmt = goldAmt;
	}

	/**
	 * Attacks a movable unit
	 * @param other the movable unit
	 * @return whether the attack was successful
	 */
	public boolean attack(AMovableUnit other) {
		if (alreadyAttacked || position.distance(other.position) > ATTACK_RADIUS) {
			return false;
		}
		other.health -= this.movableUnitAttack * (1 - other.defense);
		alreadyAttacked = true;
		gameAdpt.checkUnitDeath(this);
		gameAdpt.checkUnitDeath(other);
		gameAdpt.updateView();
		return true;
	}

	/**
	 * Tests whether the unit is within the city's attack radius
	 * @param other the unit
	 * @return yes or no
	 */
	public boolean withinRadius(AGameUnit other) {
		return position.distance(other.position) <= ATTACK_RADIUS;
	}

	/**
	 * Sets the city's current production
	 * @param unitType the unit type
	 */
	public void setProduction(Class<?> unitType) {
		if (unitType.equals(Miner.class)) {
			this.currentProduction = new Miner(this.position, this.owner);
		} else if (unitType.equals(Soldier.class)) {
			this.currentProduction = new Soldier(this.position, this.owner);
		} else {
			this.currentProduction = new SiegeUnit(this.position, this.owner);
		}
		this.productionNumTurnsLeft = this.currentProduction.getNumberTurnsToProduce();
		System.out.println("Setting production to: " + this.currentProduction);
		gameAdpt.updateView();
	}

	/**
	 * Speeds up production using gold
	 * @return whether this was successful
	 */
	public boolean expediteProduction() {
		if (this.currentProduction == null
				|| goldAmt < this.currentProduction.getExpediteGoldCostPerTurn() * productionNumTurnsLeft) {
			return false;
		} else {
			gameAdpt.createUnit(currentProduction);
			goldAmt -= currentProduction.getExpediteGoldCostPerTurn() * productionNumTurnsLeft;
			currentProduction = null;
			gameAdpt.updateView();
		}
		return true;
	}

	@Override
	public void unitKilled() {
		if (gameAdpt.isCurrentPlayerUnit(this)) {
			gameAdpt.displayNotification("Your city has been destroyed!", position);
		} else {
			gameAdpt.displayNotification("A city has been destroyed", position);
		}
	}

	@Override
	public void turnBegins() {
		alreadyAttacked = false;
		productionNumTurnsLeft = Math.max(0, productionNumTurnsLeft - 1);
		if (productionNumTurnsLeft <= 0 && currentProduction != null) {
			System.out.println("CREATING UNIT " + currentProduction + ", " + currentProduction.position);
			gameAdpt.createUnit(currentProduction);
			currentProduction = null;
		}
	}

	/**
	 * The city's gold amount
	 * @return the gold
	 */
	public int getGoldAmt() {
		return goldAmt;
	}

	/**
	 * Gets the city's current production
	 * @return the unit
	 */
	public AMovableUnit getCurrentProduction() {
		return currentProduction;
	}

	/**
	 * Gets the number of turns left in production
	 * @return the number of turns
	 */
	public int getProductionNumTurnsLeft() {
		return productionNumTurnsLeft;
	}

	/**
	 * Sets the city's current gold
	 * @param goldAmt the amount
	 */
	public void setGoldAmt(int goldAmt) {
		this.goldAmt = goldAmt;
	}

	@Override
	public String toString() {
		return "City ";
	}

}
