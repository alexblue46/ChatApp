package arb19_kpa1.client.view;

import common.identity.IRoomIdentity;

/**
 * Container for the view to hold the server object
 *
 */
public class ViewRoomIdentityContainer extends ViewContainer<IRoomIdentity> {

	/**
	 * The server name
	 */
	private String name;
	
	/**
	 * Creates a new view container for a given server
	 * @param identity the server object
	 */
	public ViewRoomIdentityContainer(IRoomIdentity identity) {
		super(identity);
		
		this.name = identity.getName();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	
}
