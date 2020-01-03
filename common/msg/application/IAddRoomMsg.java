package common.msg.application;

import common.msg.IAppMsg;
import common.virtualNetwork.IRoom;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Sends the receiver a room
 *
 */
public interface IAddRoomMsg extends IAppMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IAddRoomMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IAddRoomMsg.GetID();
	}	
	
	/**
	 * @return Room to invite user to
	 */
	public IRoom getRoom();
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param room to invite user to
	 * @return An IAddRoomMsg object
	 */
	static IAddRoomMsg make(final IRoom room) {
		return new IAddRoomMsg() {
			/**
			 * For Serialization.
			 */
			private static final long serialVersionUID = 32098423487893L;
			@Override
			public String toString() {
				return room.toString();
			}
			@Override
			public IRoom getRoom() {
				return room;
			}			
		};
	}
	
}
