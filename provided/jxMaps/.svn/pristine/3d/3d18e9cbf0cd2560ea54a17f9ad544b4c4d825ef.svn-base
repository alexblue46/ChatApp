package provided.jxMaps.demo.model;

import provided.jxMaps.utils.IJxMapsComponentsFactory;
import provided.jxMaps.utils.IPlace;

/**
 * The adapter from the JxMAps demo model to the view
 * @author swong
 *
 */
public interface IModel2ViewAdapter {

	/**
	 * Get the map components factory from the view
	 * @return A map components factory associated with the map being displayed.
	 */
	public IJxMapsComponentsFactory getMapComponentsFactory();

	/**
	 * Add a place to the view's drop lists of available places.
	 * @param place  The IPlace to add
	 */
	public void addPlace(IPlace place);

	/**
	 * Display a status string on the view
	 * @param str  The string to display
	 */
	public void displayStatus(String str);
	
	/**
	 * Refresh the view to take make sure that any new components, etc. are properly laid out and displayed.
	 */
	public void refresh();
}
