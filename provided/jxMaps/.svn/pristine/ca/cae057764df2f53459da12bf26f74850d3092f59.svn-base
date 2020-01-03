package provided.jxMaps.utils.enhanced;

import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.Polygon;
import com.teamdev.jxmaps.PolygonOptions;

import provided.jxMaps.utils.IOptionedMapObject;
import provided.jxMaps.utils.IVisibleMapObject;

/**
 * A subclass of Polygon that adds additional functionality and transparently fixes a bug in 
 * its use of its PolygonOptions where IllegalArgument (unsupported parameter) exception in 
 * Polygon.setOptions(options) if the paths were set in the PolygonOptions.  Note also that 
 * there is a bug in PolygonOptions.getPaths() where it will throw a null pointer exception
 * if PolygonOptions.setPaths() was never called. 
 * @author swong
 *
 */
public class EnhancedPolygon extends Polygon implements IVisibleMapObject, IOptionedMapObject<PolygonOptions> {

	/**
	 * The options that were given to setOptions() which may not reflect subsequent mutations.
	 */
	private PolygonOptions options;


	/**
	 * Constructor for the class
	 * @param map The Map to which this Polygon is associated.
	 */
	public EnhancedPolygon(Map map) {
		super(map);
	}

	@Override
	public PolygonOptions getOptions() {
		return this.options;
	}

	@Override
	public void setOptions(PolygonOptions options) {
		
		// The following code is to fix the bug in JxMaps where PolygonOptions.setPaths() 
		// results in an IllegalArgument (unsupported parameter) exception in Polygon.setOptions(options)
		LatLng[][] paths = null;
		try {
			// save the paths if it they were set
			paths = options.getPaths(); // will throw a null pointer exception if setPaths() was never called.
			System.out.println("[EnhancedPolygon.setOptions()] paths = "+paths);
			if(null != paths){
				options.setPaths(new LatLng[][] {}); // clear the paths in the options so that no exceptions are thrown
			}
		}
		catch(NullPointerException e) {
			// this is normal if options.setPaths() was never called.
			System.out.println("[EnhancedPolygon.setOptions()] NORMAL null pointer exception if PolygonOptons.setPaths() was never called: "+e);
		}
		
		super.setOptions(options); // set the options
		
		if(null != paths) {
			// install the paths if they were originally set in the options
			this.setPaths(paths);  // set the paths here 
			options.setPaths(paths);  // put the original paths back into the options
		}
		
		this.options = options;   

	}

}
