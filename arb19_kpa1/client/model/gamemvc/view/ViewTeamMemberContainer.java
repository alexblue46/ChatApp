package arb19_kpa1.client.model.gamemvc.view;

import java.rmi.RemoteException;

import arb19_kpa1.server.view.ViewContainer;
import common.receivers.IRoomMember;

/**
 * Container for the view to hold the server object
 *
 */
public class ViewTeamMemberContainer extends ViewContainer<IRoomMember> {

	/**
	 * The server name
	 */
	private String name;
	
	/**
	 * Creates a new view container for a given server
	 * @param team the server object
	 */
	public ViewTeamMemberContainer(IRoomMember team) {
		super(team);
		try {
			this.name = team.getName();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
