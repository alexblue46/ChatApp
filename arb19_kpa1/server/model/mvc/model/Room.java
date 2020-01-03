package arb19_kpa1.server.model.mvc.model;

import java.util.Collection;
import java.util.UUID;

import common.receivers.IRoomMember;
import common.virtualNetwork.IRoom;

/**
 * Server implementation of IRoom
 *
 */
public class Room implements IRoom {
	
	/**
	 * UID
	 */
	private static final long serialVersionUID = -3137153709242831567L;

	/**
	 * Members in the room
	 */
	private Collection<IRoomMember> members;
	
	/**
	 * Identity of the room
	 */
	private Identity id;
	
	/**
	 * Creates a new room
	 * @param members the room members
	 * @param roomName the room name
	 */
	public Room(Collection<IRoomMember> members, String roomName) {
		this.members = members;
		this.id = new Identity(roomName, UUID.randomUUID());
	}

	@Override
	public Collection<IRoomMember> getMembers() {
		return members;
	}

	@Override
	public Identity getInfo() {
		return id;
	}
	
	/**
	 * Adds a member to the room
	 * @param member the member to add
	 */
	public void addMember(IRoomMember member) {
		members.add(member);
	}
	/**
	 * Removes a member to the room
	 * @param member the member to remove
	 */
	public void removeMember(IRoomMember member) {
		members.remove(member);
	}

}
