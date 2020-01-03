package common.msg.collection;

import java.util.Collection;

import common.msg.IMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * A message containing requested information about the IMsgReceiver sender in the form of a collection.
 * @param <TData> Data to be sent back
 *
 */
public interface IAddCollectionMsg<TData> extends IMsg {
	
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IAddCollectionMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IAddCollectionMsg.GetID();
	}	
	
	/**
	 * @return Collection of Data objects
	 */
	public Collection<TData> getCollection();
	
}
