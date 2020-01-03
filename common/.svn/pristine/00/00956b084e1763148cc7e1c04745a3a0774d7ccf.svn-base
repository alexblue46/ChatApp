package common.msg.status.network;

import java.util.UUID;
import common.msg.IRoomMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Response message for an IRoomCheckStatusMsg
 * @author Travis Benedict
 *
 */
public interface IRoomOkStatusMsg extends IRoomMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IRoomOkStatusMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return GetID();
	}	
	
	/**
	 * 
	 * @return the UUID obtained from the IRoomCheckStatusMsg
	 */
	public UUID getUUID();

	/**
	 * Factory method to create the data type from an input value from an existing UUID.
	 * @param uuid uuid to use
	 * @return An IRoomOkStatusMsg object
	 */
	public static IRoomOkStatusMsg make(UUID uuid) {
		return new IRoomOkStatusMsg() {
			private static final long serialVersionUID = -3559393659988315135L;
			@Override
			public UUID getUUID() {
				return uuid;
			}
		};
	}
}
