package common.msg.collection;

import java.util.Collection;

import common.msg.IAppMsg;
import common.receivers.IApplication;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Requests a collection of Applications that sender is connected to.
 *
 */
public interface IAddAppsMsg extends IAddCollectionMsg<IApplication>, IAppMsg {

	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IAddAppsMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IAddAppsMsg.GetID();
	}
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param users A Collection of users sender is connected to
	 * @return An IAddAppsMsg object
	 */
	static IAddAppsMsg make(final Collection<IApplication> users) {
		return new IAddAppsMsg() {
			/**
			 * For Serialization.
			 */
			private static final long serialVersionUID = 420982873248L;
			@Override
			public String toString() {
				return users.toString();
			}
			@Override
			public Collection<IApplication> getCollection() {
				return users;
			}			
		};
	}
	
}
