package common.msg.room;

import java.util.Collection;

import common.msg.IRoomMsg;
import common.receivers.IRoomMember;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;
/**
 * Message to update room with most up-to-date version of room
 * @author nquazi
 *
 */
public interface ISyncRoomMsg extends IRoomMsg {

	/**
	 * Members in room
	 * @return collection of room members
	 */
	public Collection<IRoomMember> getMembers();
	
	/**
	 * Retrieve the ID value directly from the interface.
	 * getID() merely delegates to this method.
	 * @return The host ID value associated with this data type.
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(ISyncRoomMsg.class);
	}
	
	@Override
	public default IDataPacketID getID() {
		return ISyncRoomMsg.GetID();
	}	

	
	/** Convenient maker for message
	 * @param membs source-of-truth roster
	 * @return ISyncRoomMsg
	 */
	public static ISyncRoomMsg make(final Collection<IRoomMember> membs) {
		return new ISyncRoomMsg() {
			/**
			 * For Serialization.
			 */
			private static final long serialVersionUID = 32098423487893L;

			@Override
			public Collection<IRoomMember> getMembers() {
				return membs;
			}
			
		};
	}
}
