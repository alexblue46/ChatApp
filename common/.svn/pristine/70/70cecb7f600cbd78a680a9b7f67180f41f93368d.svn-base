package common.msg.status;

import common.msg.IMsg;
import common.receivers.IMsgReceiver;
import provided.datapacket.ADataPacket;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * A message that lets the user known an action failed.
 *
 */
public interface IFailureMsg extends IStatusMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IFailureMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IFailureMsg.GetID();
	}	
	
	/**
	 * Data packet that failed!
	 * @return data packet consisting of T data and an S sender
	 */
	public ADataPacket getDataPacket();
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param text description of the message
	 * @return An IFailureMsg object
	 */
	public static IFailureMsg make(final String text) {
		return new IFailureMsg() {

			/**
			 * Version number for serialization.
			 */
			private static final long serialVersionUID = 3700296955812766432L;

			@Override
			public String getDescription() {
				return text;
			}

			@Override
			public ADataPacket getDataPacket() {
				return null;
			}			
		};
	}
	
	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param <M> type of msg
	 * @param <K> sender of msg
	 * @param text description of the message
	 * @param dp datapacket with M data and K sender
	 * @return An IFailureMsg object
	 */
	public static <M extends IMsg, K extends IMsgReceiver<DataPacket<? super M, K>>> IFailureMsg make(final String text, DataPacket<? super M, K> dp) {
		return new IFailureMsg() {

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

			@Override
			public ADataPacket getDataPacket() {
				return dp;
			}			
		};
	}

	
}
