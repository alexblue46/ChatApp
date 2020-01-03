package common.packet;

import common.msg.IMsg;
import common.receivers.IRoomMember;
import provided.datapacket.DataPacket;

/**
 * Data packets to be sent at the room level
 *
 * @param <TMsg> Type of message
 */
public class RoomDataPacket<TMsg extends IMsg> extends DataPacket<TMsg, IRoomMember> {

	/**
	 * Serialization number. 
	 */
	private static final long serialVersionUID = -8494204184527203597L;
	
	/**
	 * Constructor for the class.
	 * @param data Message to send at the user level.
	 * @param sender IRoomMember sending the message.
	 */
	public RoomDataPacket(TMsg data, IRoomMember sender) {
		super(data, sender);
	}

}
