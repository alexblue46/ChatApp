package arb19_kpa1.game.model.units;

import java.io.Serializable;

import arb19_kpa1.game.model.IGameUnit2GameAdpt;
import arb19_kpa1.game.model.utility.LatLng;

/**
 * Base class for units in the game.
 *
 */
public abstract class AGameUnit implements Serializable {
	/**
	 * UID
	 */
	private static final long serialVersionUID = -6196205912594368291L;
	/**
	 * The unit's position
	 */
	protected LatLng position;
	/**
	 * The adapter for communicating with the game
	 */
	protected transient IGameUnit2GameAdpt gameAdpt;

	/**
	 * Creates a game unit 
	 * @param position the game unit's position
	 */
	protected AGameUnit(LatLng position) {
		this.position = position;
	}

	/**
	 * Sets the adapter for the units to use to communicate with the game state
	 * @param adpt the adapter
	 */
	public void setUnit2GameAdpt(IGameUnit2GameAdpt adpt) {
		this.gameAdpt = adpt;
	}

	/**
	 * This method is called when a unit is killed, but before it is removed from
	 * the list of units.
	 */
	public abstract void unitKilled();

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [position=" + position + "]";
	}

	/**
	 * Gets the game units position
	 * @return the position
	 */
	public LatLng getPosition() {
		return position;
	}
	
}
