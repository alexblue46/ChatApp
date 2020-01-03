package common.msg.room;

import common.msg.IRoomMsg;
import common.receivers.IRoomMember;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * A message that lets a user be added to a room. Process it by adding the sender's IRoomMember
 * stub to the receiver's list of room members for a given room.
 *
 */
public interface IAddMemberMsg extends IRoomMsg {
	
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IAddMemberMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IAddMemberMsg.GetID();
	}	
	
	/**
	 * Get user requesting to be added to a room
	 * @return user requesting to be added to a room
	 */
	public IRoomMember getUserToAdd();
	
	/** Name for player
	 * @return name of player
	 */
	public String getName();
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param requestor user requesting to be added to a chat room.
	 * @param name name of user
	 * @return An IAddMemberMsg object
	 */
	static IAddMemberMsg make(final IRoomMember requestor, String name) {
		return new IAddMemberMsg() {

			/**
			 * For Serialization.
			 */
			private static final long serialVersionUID = 3254203217630041443L;

			@Override
			public IRoomMember getUserToAdd() {
				return requestor;
			}			
			
			public String getName() {
				return name;
			}
		};
	}
}
