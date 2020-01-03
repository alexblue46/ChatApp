package provided.jxMaps.utils.enhanced;

import com.teamdev.jxmaps.Circle;
import com.teamdev.jxmaps.CircleOptions;
import com.teamdev.jxmaps.Map;

import provided.jxMaps.utils.IOptionedMapObject;
import provided.jxMaps.utils.IVisibleMapObject;

/**
 * An extension of com.teamdev.jxmaps.Circle that adds additional functionality.
 * @author swong
 *
 */
public class EnhancedCircle extends Circle implements IOptionedMapObject<CircleOptions>, IVisibleMapObject {

	/**
	 * Retained reference to the options used to configure this Circle
	 */
	private CircleOptions options = new CircleOptions();
	
	/**
	 * Constructor for the circle.
	 * @param map The Map to which this circle is associated.
	 */
	public EnhancedCircle(Map map) {
		super(map);
	}
	
	@Override
	public CircleOptions getOptions() {
		return this.options;
	}
	
	@Override
	public void setOptions(CircleOptions options) {
		super.setOptions(options);
		this.options = options;
	}
	
}
