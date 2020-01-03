package common.msg.room;

import common.msg.IRoomMsg;
import common.receivers.IApplication;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * A message that contains the IApplication stub associated 
 * with the message's sender.
 *
 */
public interface IProvideAppMsg extends IRoomMsg{
	
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IProvideAppMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IProvideAppMsg.GetID();
	}
	
	/**
	 * Get IApplication stub of the sender
	 * @return The IApplication stub
	 */
	public IApplication getAppStub();
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param appStub The IApplication stub
	 * @return An IProvideAppMsg object
	 */
	static IProvideAppMsg make(final IApplication appStub) {
		return new IProvideAppMsg() {

			/**
			 * Version number for serialization.
			 */
			private static final long serialVersionUID = 747827911439326644L;
			
			@Override
			public String toString() {
				return appStub.toString();
			}

			@Override
			public IApplication getAppStub() {
				return appStub;
			}			
		};
	}

}
