package arb19_kpa1.game.msg;

import common.msg.IRoomMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * The message for signaling the game is over
 *
 */
public class GameOverMsg implements IRoomMsg {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -135262663753947872L;
	/**
	 * The number of the winning team
	 */
	private int winningTeam;

	/**
	 * Create a new GameOverMsg
	 * 
	 * @param winningTeam the winning team
	 */
	public GameOverMsg(int winningTeam) {
		this.winningTeam = winningTeam;
	}

	/**
	 * Gets this datapacket's ID
	 * 
	 * @return the ID
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(GameOverMsg.class);
	}

	@Override
	public IDataPacketID getID() {
		return GameOverMsg.GetID();
	}

	/**
	 * Gets the number of the winning team
	 * 
	 * @return 1 or 2
	 */
	public int getWinningTeam() {
		return winningTeam;
	}

}
