package arb19_kpa1.game.msg;

import common.msg.IRoomMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Message for sending strings to teammates
 * @author Owner
 *
 */
public class TeamStringMsg implements IRoomMsg{
	
	/**
	 * UID
	 */
	private static final long serialVersionUID = -7888892767277430831L;
	
	/**
	 * The text to send
	 */
	private String text;
	
	/**
	 * Creates a new TeamStringMsg with the given text
	 * @param text teh text
	 */
	public TeamStringMsg(String text) {
		this.text = text;
	}
	
	/**
	 * Gets this message ID
	 * @return this id
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(TeamStringMsg.class);
	}

	@Override
	public IDataPacketID getID() {
		return TeamStringMsg.GetID();
	}

	/**
	 * GEts the text in this message
	 * @return the text
	 */
	public String getText() {
		return text;
	}
}
