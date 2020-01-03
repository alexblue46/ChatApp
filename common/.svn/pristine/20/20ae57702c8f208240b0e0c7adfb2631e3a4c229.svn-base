package common.packet;

import common.msg.IMsg;
import common.receivers.IApplication;
import provided.datapacket.DataPacket;

/**
 * Data packets to be sent at the application level
 *
 * @param <TMsg> Type of message
 */
public class AppDataPacket<TMsg extends IMsg> extends DataPacket<TMsg, IApplication> {

	/**
	 * Serialization number
	 */
	private static final long serialVersionUID = 1450849791204469768L;
	
	/**
	 * Constructor for the class.
	 * @param data Message to send at the user level.
	 * @param sender IApplication sending the message.
	 */
	public AppDataPacket(TMsg data, IApplication sender) {
		super(data, sender);
	}

}
