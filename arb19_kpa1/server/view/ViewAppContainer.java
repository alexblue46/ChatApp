package arb19_kpa1.server.view;

import common.receivers.IApplication;

/**
 * Container wrapping an IApplication
 * @author Owner
 *
 */
public class ViewAppContainer extends ViewContainer<IApplication>{
	
	/**
	 * Cached IApplication name
	 */
	private String name;

	/**
	 * Create a new wrapper
	 * @param item the IApplication
	 */
	public ViewAppContainer(IApplication item) {
		super(item);
		
		try {
			this.name = item.getName();
		} catch (Exception e) {
			e.printStackTrace();
			this.name = item.toString();
		}
	}
	
	@Override
	public String toString() {
		return name;
	}

}
