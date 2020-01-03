package arb19_kpa1.server.view;

import common.virtualNetwork.IRoom;

/**
 * A wrapper for IRoom
 * 
 * @author Owner
 *
 */
public class ViewRoomContainer extends ViewContainer<IRoom> {

	/**
	 * The cached room name
	 */
	private String name;

	/**
	 * Create a wrapper for a iroom
	 * 
	 * @param item the IRoom
	 */
	public ViewRoomContainer(IRoom item) {
		super(item);

		this.name = item.getInfo().getName();
	}

	@Override
	public String toString() {
		return name;
	}
}
