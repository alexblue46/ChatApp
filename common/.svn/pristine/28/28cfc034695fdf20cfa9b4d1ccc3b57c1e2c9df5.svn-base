package common.msg.collection;

import common.identity.IRoomIdentity;
import common.msg.IAppMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Requests a collection of rooms' info receiver is currently in
 *
 */
public interface IRequestRoomsMsg extends IRequestCollectionMsg<IRoomIdentity>, IAppMsg {

	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IRequestRoomsMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IRequestRoomsMsg.GetID();
	}	
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @return An IRequestRoomsMsg object
	 */
	static IRequestRoomsMsg make() {
		return new IRequestRoomsMsg() {
			/**
			 * For Serialization.
			 */
			private static final long serialVersionUID = 123456789L;
		};
	}
	
}
