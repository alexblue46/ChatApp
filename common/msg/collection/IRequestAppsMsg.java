package common.msg.collection;

import common.msg.IAppMsg;
import common.receivers.IApplication;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Requests a collection of Applications receiver is currently connected to
 *
 */
public interface IRequestAppsMsg extends IRequestCollectionMsg<IApplication>, IAppMsg {

	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IRequestAppsMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IRequestAppsMsg.GetID();
	}
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @return An IRequestAppsMsg object
	 */
	static IRequestAppsMsg make() {
		return new IRequestAppsMsg() {

			/**
			 * For Serialization.
			 */
			private static final long serialVersionUID = 9484283L;
	
		};
	}
	
}
