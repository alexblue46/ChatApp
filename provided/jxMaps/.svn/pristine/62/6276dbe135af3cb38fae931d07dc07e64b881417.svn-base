package provided.jxMaps.utils.impl;

import java.lang.reflect.Method;
import java.util.HashSet;
import com.teamdev.jxmaps.MapObject;


import provided.jxMaps.utils.IMapLayer;

/**
 * A Set of MapObjects where one can control the map objects' visibility (for those that have that ability)
 * as a whole. <br>
 * WARNING:  Do NOT serialize this object because in general, MapObjects are NOT Serializable!
 * @author swong
 *
 */
public class MapLayer extends HashSet<MapObject> implements IMapLayer {
	

	/**
	 * For the Serializable HashSet but do NOT serialize because in general, MapObjects are NOT Serializable!
	 */
	private static final long serialVersionUID = 4217992334773836335L;

	@Override
	public void setVisible(boolean isVisible) {
		this.forEach((mapObj)->{
			// setVisible() is not a method of MapObject, only of certain of its subclasses.
			// Unfortunately, there is no abstract way to tell if a MapObject can be set visible or not, so must use reflection.
			// This code will cause problems in Java 9+ due to restrictions on using reflection.
			try {
				Method setVisibleMethod = mapObj.getClass().getMethod("setVisible", boolean.class); // Try to find a setVisible() method
				setVisibleMethod.invoke(mapObj, isVisible);  // Invoke the setVisible() method.
			} catch(NoSuchMethodException e) {
				// The object doesn't have a setVisible() method.
				System.out.println("[MapLayer.setVisible("+isVisible+")] No setVisible() method for MapObject = "+mapObj);
			}
			catch (Exception e) {
				System.err.println("[MapLayer.setVisible("+isVisible+")] Exception while invoking the setVisible() method of MapObject, "+mapObj+": "+e);
				e.printStackTrace();
			}
		});

	}

}
