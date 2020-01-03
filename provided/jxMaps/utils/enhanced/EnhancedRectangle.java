package provided.jxMaps.utils.enhanced;

import com.teamdev.jxmaps.RectangleOptions;

import provided.jxMaps.utils.IOptionedMapObject;
import provided.jxMaps.utils.IVisibleMapObject;

import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.Rectangle;

/**
 * An extension of com.teamdev.jxmaps.Rectangle that adds additional functionality.
 * @author swong
 *
 */
public class EnhancedRectangle extends Rectangle implements IOptionedMapObject<RectangleOptions>, IVisibleMapObject {

	/**
	 * Retained reference to the options used to configure this Rectangle
	 */
	private RectangleOptions options = new RectangleOptions();
	
	/**
	 * Constructor for the Rectangle.
	 * @param map The Map to which this rectangle is associated.
	 */
	public EnhancedRectangle(Map map) {
		super(map);
	}
	
	@Override
	public RectangleOptions getOptions() {
		return this.options;
	}
	
	@Override
	public void setOptions(RectangleOptions options) {
		super.setOptions(options);
		this.options = options;
	}
	
}
