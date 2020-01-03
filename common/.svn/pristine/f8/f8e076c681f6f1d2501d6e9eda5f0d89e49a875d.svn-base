package common.msg.status.network;

import java.util.UUID;

import common.msg.IRoomMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Response message from Server to an IServerCheckStatusMsg. This message is sent by the Server only 
 * to the client that sent the IServerCheckStatusMsg.
 * @author S. Eshvar Chandran
 *
 */
public interface IServerOkStatusMsg extends IRoomMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IServerOkStatusMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return GetID();
	}	
	
	/**
	 *
	 * @return - the UUID obtained from the IServerCheckStatusMsg
	 */
	public UUID getUUID();

	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param uuid uuid to use
	 * @return An IRemoteExceptionMsg object
	 */
	public static IServerOkStatusMsg make(UUID uuid) {
		return new IServerOkStatusMsg() {
			private static final long serialVersionUID = 4449374536388013404L;
			@Override
			public UUID getUUID() {
				return uuid;
			}
		};
	}

}
