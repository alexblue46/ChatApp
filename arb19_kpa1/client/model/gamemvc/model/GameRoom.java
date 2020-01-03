package arb19_kpa1.client.model.gamemvc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import common.identity.IRoomIdentity;
import common.receivers.IRoomMember;
import common.virtualNetwork.IRoom;

/**
 * @author alexbluestein
 * Client implementation of IRoom
 */
public class GameRoom implements IRoom {

	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -7806593559634818523L;
	/**
	 * Members in room
	 */
	private Collection<IRoomMember> members = new ArrayList<>();
	/**
	 * Room id
	 */
	private IRoomIdentity identity;
	
	/**
	 * @param room IRoom to make implementation with
	 */
	public GameRoom(IRoom room) {
		members = room.getMembers();
		identity = room.getInfo();
	}
	
	/**
	 * @param members Stubs in room
	 * @param roomName Name of the room
	 */
	public GameRoom(Collection<IRoomMember> members, String roomName) {
		this.members = members;
		UUID roomUUID = UUID.randomUUID();
		identity = new IRoomIdentity() {
			
			/**
			 * Serialization
			 */
			private static final long serialVersionUID = 6707765796757322209L;

			@Override
			public String getName() {
				return roomName;
			}
			
			@Override
			public UUID getID() {
				return roomUUID;
			}
		};
	}

	@Override
	public Collection<IRoomMember> getMembers() {
		return members;
	}
	
	/**
	 * @param members Members to set room with
	 */
	public void setMembers(Collection<IRoomMember> members) {
		this.members.clear();
		this.members.addAll(members);
	}

	@Override
	public IRoomIdentity getInfo() {
		return identity;
	}

	@Override
	public String toString() {
		return identity.getName();
	}
}
