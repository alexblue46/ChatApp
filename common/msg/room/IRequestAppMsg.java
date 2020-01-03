package common.msg.room;

import common.msg.IRoomMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * A message requesting that the corresponding IApplication stub be sent 
 * back to the sender using an IProvideAppMsg.
 *
 */
public interface IRequestAppMsg extends IRoomMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IRequestAppMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IRequestAppMsg.GetID();
	}
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @return An IRequestAppMsg object
	 */
	static IRequestAppMsg make() {
		return new IRequestAppMsg() {

			/**
			 * Version number for serialization.
			 */
			private static final long serialVersionUID = 747827911439326644L;
			
		};
	}
}
