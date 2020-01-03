package provided.jxMaps.utils.enhanced;

import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.MarkerOptions;

import provided.jxMaps.utils.IOptionedMapObject;
import provided.jxMaps.utils.IVisibleMapObject;

/**
 * A Marker subclass that adds additional functionality
 * @author swong
 *
 */
public class EnhancedMarker extends Marker implements IOptionedMapObject<MarkerOptions>, IVisibleMapObject {

	/**
	 * The options given to setOptions() which may not reflect subsequent mutations to the marker.
	 */
	private MarkerOptions options;

	/**
	 * Constructor for the class
	 * @param map The Map to which this marker is associated.
	 */
	public EnhancedMarker(Map map) {
		super(map);
	}

	@Override
	public MarkerOptions getOptions() {
		return this.options;
	}

	@Override
	public void setOptions(MarkerOptions options) {
		super.setOptions(options);
		this.options = options;
	}

}
