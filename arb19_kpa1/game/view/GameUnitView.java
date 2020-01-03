package arb19_kpa1.game.view;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.teamdev.jxmaps.Icon;
import com.teamdev.jxmaps.MarkerOptions;

import arb19_kpa1.game.model.units.*;
import provided.jxMaps.utils.IJxMapsComponentsFactory;
import provided.jxMaps.utils.enhanced.EnhancedMarker;

/**
 * Object that encapsulates a unit and a visible marker
 *
 */
public class GameUnitView {

	/**
	 * The game unit
	 */
	private AGameUnit unit;

	/**
	 * The marker
	 */
	private EnhancedMarker marker;

	/**
	 * Icon for a City
	 */
	private static final Icon CITY_ICON = new Icon();
	/**
	 * Icond for a soldier
	 */
	private static final Icon SOLDIER_ICON = new Icon();
	/**
	 * Icon for a miner
	 */
	private static final Icon MINER_ICON = new Icon();
	/**
	 * Icon for a mine
	 */
	private static final Icon MINE_ICON = new Icon();
	/**
	 * Icon for a seige unit
	 */
	private static final Icon SIEGE_UNIT_ICON = new Icon();
	/**
	 * Icon for mineable land
	 */
	private static final Icon MINEABLE_LAND_ICON = new Icon();

	static {
		CITY_ICON.loadFromFile("com/teamdev/jxmaps/demo/res/ic_events_sample.png");
		SOLDIER_ICON.loadFromFile("com/teamdev/jxmaps/demo/res/close_black.png");
		MINER_ICON.loadFromFile("com/teamdev/jxmaps/demo/res/ic_options_sample.png");
		MINE_ICON.loadFromFile("com/teamdev/jxmaps/demo/res/menu_retina.png");
		SIEGE_UNIT_ICON.loadFromFile("com/teamdev/jxmaps/demo/res/close.png");
		MINEABLE_LAND_ICON.loadFromFile("com/teamdev/jxmaps/demo/res/ic_markers_sample.png");
	}

	/**
	 * A collection of functions for making markers for units
	 */
	private static final Map<Class<?>, Function<AGameUnit, MarkerOptions>> MAKE_MARKER = new HashMap<Class<?>, Function<AGameUnit, MarkerOptions>>();

	static {
		MAKE_MARKER.put(City.class, (unit) -> makeMarkerOptions(CITY_ICON, "City", unit));
		MAKE_MARKER.put(Soldier.class, (unit) -> makeMarkerOptions(SOLDIER_ICON, "Soldier", unit));
		MAKE_MARKER.put(Miner.class, (unit) -> makeMarkerOptions(MINER_ICON, "Miner", unit));
		MAKE_MARKER.put(SiegeUnit.class, (unit) -> makeMarkerOptions(SIEGE_UNIT_ICON, "Siege Unit", unit));
		MAKE_MARKER.put(MineableLand.class, (unit) -> makeMarkerOptions(MINEABLE_LAND_ICON, "Mineable Land", unit));
		MAKE_MARKER.put(Mine.class, (unit) -> makeMarkerOptions(MINE_ICON, "Mine", unit));
	}

	/**
	 * Creates a new view for a unit
	 * 
	 * @param unit        the unit
	 * @param compFactory the component factory
	 */
	public GameUnitView(AGameUnit unit, IJxMapsComponentsFactory compFactory) {
		this.unit = unit;
		this.marker = compFactory.makeMarker(MAKE_MARKER.get(unit.getClass()).apply(unit));
	}

	/**
	 * Creates the marker options for a unit
	 * 
	 * @param icon  the marker icon
	 * @param title the title of the marker
	 * @param unit  the unit
	 * @return the marker options
	 */
	private static MarkerOptions makeMarkerOptions(Icon icon, String title, AGameUnit unit) {
		MarkerOptions opts = new MarkerOptions();
		opts.setClickable(false);
		opts.setPosition(unit.getPosition().getJxMapsEquiv());
		opts.setIcon(icon);

		if (unit instanceof APlayerOwnedUnit) {
			try {
				title = ((APlayerOwnedUnit) unit).getOwner().getName() + ": " + title;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		opts.setLabelString(title);
		return opts;
	}

	/**
	 * Gets the unit stored in the object
	 * @return the unit
	 */
	public AGameUnit getUnit() {
		return unit;
	}

	/**
	 * Gets the marker stored in the object
	 * @return the marker
	 */
	public EnhancedMarker getMarker() {
		return marker;
	}

	/**
	 * Sets the marker to a new title
	 * @param title the new title
	 * @param compFactory the component factory
	 */
	public void setMarker(String title, IJxMapsComponentsFactory compFactory) {
		MarkerOptions opts = MAKE_MARKER.get(unit.getClass()).apply(unit);
		if (title != null)
			opts.setLabelString(title);
		marker.remove();
		marker = compFactory.makeMarker(opts);
	}

}
