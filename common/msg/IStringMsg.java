package common.msg;

import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * A message containing text. Process it by displaying it in the chat window.
 *
 */
public interface IStringMsg extends IAppMsg, IRoomMsg {
	
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IStringMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IStringMsg.GetID();
	}	
	
	/**
	 * Get the text message.
	 * @return text message
	 */
	public String getMessage();
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param text text in the message
	 * @return An IStringMsg object
	 */
	static IStringMsg make(final String text) {
		return new IStringMsg() {

			/**
			 * Version number for serialization.
			 */
			private static final long serialVersionUID = 2116830322237925265L;

			@Override
			public String toString() {
				return text;
			}

			@Override
			public String getMessage() {
				return text;
			}			
		};
	}
}
