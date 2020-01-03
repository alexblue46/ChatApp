package common.msg.status;

import common.msg.*;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * A general status message indicating the result of sending one of the other type of messages
 *
 */
public interface IStatusMsg extends IAppMsg, IRoomMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IStatusMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IStatusMsg.GetID();
	}
	
	/**
	 * Get the description of the message.
	 * @return message
	 */
	public String getDescription();

	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * 
	 * @param text description of the message
	 * @return An IStatusMsg object
	 */
	public static IStatusMsg make(final String text) {
		return new IStatusMsg() {
			/**
			 * Version number for serialization.
			 */
			private static final long serialVersionUID = 3700296955812766432L;

			@Override
			public String toString() {
				return text;
			}

			@Override
			public String getDescription() {
				return text;
			}
		};
	}
}
