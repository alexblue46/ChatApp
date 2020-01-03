package common.msg.application;

import common.msg.IAppMsg;
import common.receivers.IApplication;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Tells receiver to remove connection to sender.
 * Sent on leaving the application
 *
 */
public interface IQuitMsg extends IAppMsg {

	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IQuitMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IQuitMsg.GetID();
	}	
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param requestor IApplication that wants others to disconnect from them
	 * @return An IQuitMsg object
	 */
	static IQuitMsg make(final IApplication requestor) {
		return new IQuitMsg() {
			/**
			 * For Serialization.
			 */
			private static final long serialVersionUID = 32098423487893L;
			@Override
			public String toString() {
				return requestor.toString();
			}
		};
	}
}
