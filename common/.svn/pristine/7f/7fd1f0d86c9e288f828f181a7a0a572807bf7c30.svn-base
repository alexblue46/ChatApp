package common.msg.room;

import common.msg.IRoomMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * A message containing the ID of an unknown message.
 * The receiver of an unknown message should send an IRequestCommandMsg back to the unknown message sender
 * when the receiver does not know how to process the message.
 *
 */
public interface IRequestCommandMsg extends IRoomMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IRequestCommandMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IRequestCommandMsg.GetID();
	}	
	
	/**
	 * Get the ID of the unknown message type.
	 * @return ID of the unknown message type.
	 */
	public IDataPacketID getMsgId();
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param msgId ID of the message type
	 * @return An IRequestCommandMsg object
	 */
	static IRequestCommandMsg make(final IDataPacketID msgId) {
		return new IRequestCommandMsg() {

			/**
			 * Version number for serialization.
			 */
			private static final long serialVersionUID = 747827911439426644L;

			@Override
			public String toString() {
				return msgId.toString();
			}

			@Override
			public IDataPacketID getMsgId() {
				return msgId;
			}			
		};
	}
}
