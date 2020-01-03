package arb19_kpa1.game.msg.teamjoin;


import arb19_kpa1.game.msg.SetKeysMsg;
import common.cmd.ARoomMsgCmd;
import common.packet.RoomDataPacket;
import provided.datapacket.IDataPacketID;

/**
 * @author Owner
 *	Command to set the keys in the database
 */
public class SetKeysCmd extends ARoomMsgCmd<SetKeysMsg> {

	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 7758832048355647550L;

	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<SetKeysMsg> host, Void... params) {
		cmd2model.putItemInDB(host.getData().getKey(), host.getData().getKeyMap());
		return null;
	}

}
