package arb19_kpa1.game.view;

import arb19_kpa1.game.model.units.AGameUnit;

/**
 * Adapter from the map view to the map
 * @author Owner
 *
 */
public interface IMapView2MapAdpt {
	
	/**
	 * Puts the unit in teh contorl panel
	 * @param unit the unit
	 */
	void putUnitInCntrlPanel(AGameUnit unit);
}
