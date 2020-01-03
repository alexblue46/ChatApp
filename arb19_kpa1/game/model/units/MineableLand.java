package arb19_kpa1.game.model.units;

import arb19_kpa1.game.model.utility.LatLng;

/**
 * Represents mineable land
 */
public class MineableLand extends AGameUnit {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -2240350294952577824L;

	/**
	 * Creates an instance of Mineableland
	 * 
	 * @param position the unit's position
	 */
	public MineableLand(LatLng position) {
		super(position);
	}

	@Override
	public void unitKilled() {

	}

}
