package common.msg.room;

import common.cmd.ARoomMsgCmd;
import common.msg.IRoomMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * A message containing the type ID of an unknown message and a command to process a message of that type.<br>
 * Whenever a room member receives a IRequestCommandMsg from another member in the room, they should 
 * respond by sending back an IAddCommandMsg containing the requested command.
 * @param <M> Subclass of IRoomMsg to process
 *
 */
public interface IAddCommandMsg<M extends IRoomMsg> extends IRoomMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IAddCommandMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return IAddCommandMsg.GetID();
	}	
	
	/**
	 * Get the command to process one type of unknown message.
	 * @return Command to process one type of unknown message.
	 */
	public ARoomMsgCmd<M> getCommand();
	
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
	 * @param <M> Subclass of IMsg to process
	 * @param cmd Command to process the unknown message.
	 * @param msgId ID of the unknown message type
	 * @return An IAddCommandMsg object
	 */
	static <M extends IRoomMsg> IAddCommandMsg<M> make(final ARoomMsgCmd<M> cmd, final IDataPacketID msgId) {
		return new IAddCommandMsg<M>() {

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

			@Override
			public ARoomMsgCmd<M> getCommand() {
				return cmd;
			}			
		};
	}
}
