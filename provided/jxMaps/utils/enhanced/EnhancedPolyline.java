package provided.jxMaps.utils.enhanced;

import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.Polyline;
import com.teamdev.jxmaps.PolylineOptions;

import provided.jxMaps.utils.IOptionedMapObject;
import provided.jxMaps.utils.IVisibleMapObject;

/**
 * A subclass of Polyline that adds additional functionality
 * @author swong
 *
 */
public class EnhancedPolyline extends Polyline implements IVisibleMapObject, IOptionedMapObject<PolylineOptions> {

	/**
	 * The options object given to setOptions() which may not reflect subsequent mutations.
	 */
	private PolylineOptions options;

	/**
	 * Constructor for the class
	 * @param map The Map to which this polyline is associated.
	 */
	public EnhancedPolyline(Map map) {
		super(map);
	}

	@Override
	public PolylineOptions getOptions() {
		return this.options;
	}
	
	@Override
	public void setOptions(PolylineOptions options) {
		super.setOptions(options);
		this.options = options;
	}

}
