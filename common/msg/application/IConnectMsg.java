package common.msg.application;

import common.msg.IAppMsg;
import common.receivers.IApplication;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Sends message so that receiver can connect to sender
 *
 */
public interface IConnectMsg extends IAppMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IConnectMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IConnectMsg.GetID();
	}	
	
	/** Return name of requestor for connection
	 * @return name
	 */
	public String appName();
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param requestor user sending the IConnectMsg
	 * @param name name of app making connection
	 * @return An IConnectMsg object
	 */
	static IConnectMsg make(final IApplication requestor, String name) {
		return new IConnectMsg() {
			/**
			 * For Serialization.
			 */
			private static final long serialVersionUID = 231242332544323L;
			

			@Override
			public String appName() {
				return name;
			}
			
		};
	}
	
}
