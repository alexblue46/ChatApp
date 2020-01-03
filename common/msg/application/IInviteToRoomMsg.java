package common.msg.application;

import common.identity.IRoomIdentity;
import common.msg.IAppMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Invite a user to a room
 *
 */
public interface IInviteToRoomMsg extends IAppMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IInviteToRoomMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IInviteToRoomMsg.GetID();
	}	
	
	/**
	 * @return Room to invite user to
	 */
	public IRoomIdentity getRoom();
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param roomInfo info to invite user to
	 * @return An IInviteToRoomMsg object
	 */
	static IInviteToRoomMsg make(final IRoomIdentity roomInfo) {
		return new IInviteToRoomMsg() {
			/**
			 * For Serialization.
			 */
			private static final long serialVersionUID = 32098423487893L;
			@Override
			public String toString() {
				return "[InviteToRoomMsg \""+roomInfo.getName()+"\"";
			}
			@Override
			public IRoomIdentity getRoom() {
				return roomInfo;
			}			
		};
	}
	
}
