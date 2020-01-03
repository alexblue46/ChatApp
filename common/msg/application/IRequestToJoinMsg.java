package common.msg.application;

import common.identity.IRoomIdentity;
import common.msg.IAppMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Requests that the receiver send them a specified room
 *
 */
public interface IRequestToJoinMsg extends IAppMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IRequestToJoinMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IRequestToJoinMsg.GetID();
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
	 * @return An IRequestToJoinMsg object
	 */
	static IRequestToJoinMsg make(final IRoomIdentity room) {
		return new IRequestToJoinMsg() {
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
