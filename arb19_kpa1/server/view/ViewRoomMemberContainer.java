package arb19_kpa1.server.view;

import java.rmi.RemoteException;

import common.receivers.IRoomMember;



/**
 * Container for the view to hold the server object
 *
 */
public class ViewRoomMemberContainer extends ViewContainer<IRoomMember> {

	/**
	 * The room member name
	 */
	private String name;
	
	/**
	 * Creates a new view container for a given team member
	 * @param roomMember the room member object
	 */
	public ViewRoomMemberContainer(IRoomMember roomMember) {
		super(roomMember);
		
		try {
			this.name = roomMember.getName();
		} catch (RemoteException e) {
			e.printStackTrace();
			this.name = roomMember.toString();
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
