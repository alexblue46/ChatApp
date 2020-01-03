package arb19_kpa1.game.msg;

import java.util.Map;

import arb19_kpa1.game.view.GameOverView;
import common.packet.RoomDataPacket;
import provided.datapacket.IDataPacketID;
import provided.mixedData.MixedDataKey;

/**
 * Message that ends the game
 *
 */
public class GameOverCmd extends AGameCmd<GameOverMsg> {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -1364723510934490129L;

	/**
	 * Creates a new game over command
	 * 
	 * @param key the main MDD key
	 */
	@SuppressWarnings("rawtypes")
	public GameOverCmd(MixedDataKey<Map> key) {
		super(key);
	}

	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<GameOverMsg> host, Void... params) {
		mddUtil.getGameController().endGame();
		int winningTeam = host.getData().getWinningTeam();
		int playerTeam = mddUtil.getMyTeamNum();

		cmd2model.displayComponent(() -> new GameOverView(winningTeam == playerTeam), "Game Over");

		return null;
	}

}
