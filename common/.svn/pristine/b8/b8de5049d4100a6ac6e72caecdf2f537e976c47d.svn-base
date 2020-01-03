package common.msg.application;

import common.identity.IRoomIdentity;
import common.msg.IAppMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Message that indicates accepting an invitation. Receipt of this message should result in an IAddRoomMsg.
 * This is semantically different from a IRequestToJoinMsg; a request to join can be denied the room, but an accepted invitation should never
 * be denied the room if the room is still available.
 * @author nquazi
 *
 */
public interface IAcceptInviteMsg extends IAppMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IAcceptInviteMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IAcceptInviteMsg.GetID();
	}	
	
	/**
	 * @return Room to invite user to
	 */
	public IRoomIdentity getRoomIdentity();
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param room to sender is requesting to be added to
	 * @return An IAcceptInviteMsg object
	 */
	static IAcceptInviteMsg make(final IRoomIdentity room) {
		return new IAcceptInviteMsg() {
			/**
			 * For Serialization.
			 */
			private static final long serialVersionUID = 32098423487893L;
			@Override
			public String toString() {
				return room.toString();
			}
			@Override
			public IRoomIdentity getRoomIdentity() {
				return room;
			}			
		};
	}

}
