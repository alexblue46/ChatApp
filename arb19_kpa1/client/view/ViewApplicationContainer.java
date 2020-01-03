package arb19_kpa1.client.view;

import java.rmi.RemoteException;

import common.receivers.IApplication;

/**
 * Container for the view to hold the server object
 *
 */
public class ViewApplicationContainer extends ViewContainer<IApplication> {

	/**
	 * The server name
	 */
	private String name;
	
	/**
	 * Creates a new view container for a given server
	 * @param server the server object
	 */
	public ViewApplicationContainer(IApplication server) {
		super(server);
		
		try {
			this.name = server.getName();
		} catch (RemoteException e) {
			e.printStackTrace();
			this.name = server.toString();
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	
}
