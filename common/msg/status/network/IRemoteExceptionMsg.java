package common.msg.status.network;

import common.msg.IRoomMsg;
import common.receivers.IRoomMember;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Special message for handling exceptions thrown while trying to reach a remote entity. 
 * This message should be sent from someone who has tried to contact a remote entity 
 * and needs to alert the members in its network that the remote entity is unresponsive.
 * @author nquazi
 */
public interface IRemoteExceptionMsg extends IRoomMsg {
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(IRemoteExceptionMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return GetID();
	}	

	/**
	 * Get problematic stub causing remote exception error
	 * @return TReceiver
	 */
	public IRoomMember getBadStub();

	/**
	 * Factory method to create the data type from an input value.   
	 * The problem with this technique is while it does hide the data type
	 * implementation, defining it at this interface level means that 
	 * the implementation is invariant, which is an unnecessary restriction.
	 * @param badStub stub that failed
	 * @return An IRemoteExceptionMsg object
	 */
	public static IRemoteExceptionMsg make(IRoomMember badStub) {
		return new IRemoteExceptionMsg() {
			private static final long serialVersionUID = -7218098141070601508L;
			@Override
			public IRoomMember getBadStub() {
				return badStub;
			}
		};
	}
}
