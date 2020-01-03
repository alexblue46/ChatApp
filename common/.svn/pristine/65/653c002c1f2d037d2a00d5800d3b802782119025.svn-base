package common.msg.status.network;

import java.util.UUID;

import common.msg.IRoomMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * IRoomCheckStatusMsg is sent to a specific message receiver to check their status.
 * @author S. Eshvar Chandran
 *
 */
public interface IRoomCheckStatusMsg extends IRoomMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IRoomCheckStatusMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return GetID();
	}	

	
	/**
	 * @return Unique ID identifying the message. This unique ID should be generated when this message is created.
	 */
	public UUID getUUID();

	/**
	 * Factory method to create the data type from an input value from an existing UUID.
	 * @return An IRemoteExceptionMsg object
	 */
	public static IRoomCheckStatusMsg make() {
		return new IRoomCheckStatusMsg() {
			private static final long serialVersionUID = 358941800277311254L;
			private UUID uuid = UUID.randomUUID();
			@Override
			public UUID getUUID() {
				return uuid;
			}
		};
	}

}
