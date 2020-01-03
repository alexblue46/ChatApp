package common.msg.room;

import common.msg.IRoomMsg;
import common.receivers.IRoomMember;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * A message that lets a user leave a room. Process it by removing the sender's 
 * IRoomMember stub from the receiver's list of room members for a given room.
 *
 */
public interface ILeaveMsg extends IRoomMsg {
	
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(ILeaveMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return ILeaveMsg.GetID();
	}	
	
	/**
	 * Get user requesting to leave a room.
	 * @return user requesting to leave a room.
	 */
	public IRoomMember getUserToLeave();
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param requestor user requesting to leave a chat room.
	 * @return An ILeaveMsg object
	 */
	static ILeaveMsg make(final IRoomMember requestor) {
		return new ILeaveMsg() {

			/**
			 * For Serialization.
			 */
			private static final long serialVersionUID = 3254203217630041443L;

			@Override
			public String toString() {
				return requestor.toString();
			}

			@Override
			public IRoomMember getUserToLeave() {
				return requestor;
			}			
		};
	}
}
