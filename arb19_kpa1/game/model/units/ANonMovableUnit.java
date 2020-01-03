package arb19_kpa1.game.model.units;

import arb19_kpa1.game.model.utility.LatLng;

import common.receivers.IRoomMember;

/**
 * A nonmovable unit in the game
 *
 */
public abstract class ANonMovableUnit extends APlayerOwnedUnit {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -600758387355650597L;

	/**
	 * Creates a new nonmovable unit
	 * 
	 * @param position             the units position
	 * @param owner                the units owner
	 * @param nonmovableUnitAttack the units attack against nonmovable units
	 * @param movableUnitAttack    the units attack against movable units
	 * @param defense              the units defense
	 * @param health               the units initial health
	 */
	protected ANonMovableUnit(LatLng position, IRoomMember owner, float nonmovableUnitAttack, float movableUnitAttack,
			float defense, int health) {
		super(position, owner, nonmovableUnitAttack, movableUnitAttack, defense, health);
	}

}
