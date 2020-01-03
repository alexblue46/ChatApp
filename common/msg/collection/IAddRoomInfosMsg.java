package common.msg.collection;

import java.util.Collection;

import common.identity.IRoomIdentity;
import common.msg.IAppMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Sends a collection of IRooms' info that sender is in
 *
 */
public interface IAddRoomInfosMsg extends IAddCollectionMsg<IRoomIdentity>, IAppMsg {

	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IAddRoomInfosMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IAddRoomInfosMsg.GetID();
	}
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param rooms A Collection of chat rooms sender is in
	 * @return An IAddRoomInfosMsg object
	 */
	static IAddRoomInfosMsg make(final Collection<IRoomIdentity> rooms) {
		return new IAddRoomInfosMsg() {
			/**
			 * For Serialization.
			 */
			private static final long serialVersionUID = 2409980483238L;
			@Override
			public String toString() {
				return rooms.toString();
			}
			@Override
			public Collection<IRoomIdentity> getCollection() {
				return rooms;
			}
		};
	}
	
}
