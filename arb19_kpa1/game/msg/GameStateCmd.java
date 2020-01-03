package arb19_kpa1.game.msg;

import java.util.Map;

import common.packet.RoomDataPacket;
import provided.datapacket.IDataPacketID;
import provided.mixedData.MixedDataKey;

/**
 * The command for handling a GameStateMsg
 *
 */
public class GameStateCmd extends AGameCmd<GameStateMsg> {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -8349119778211394749L;

	/**
	 * Creates a new GameStateCmd
	 * 
	 * @param key the main MDD key for the room
	 */
	@SuppressWarnings("rawtypes")
	public GameStateCmd(MixedDataKey<Map> key) {
		super(key);
	}

	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<GameStateMsg> host, Void... params) {
		super.mddUtil.getGameController().setGameState(host.getData().getState());
		return null;
	}

}
