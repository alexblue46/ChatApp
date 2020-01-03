package arb19_kpa1.game.msg;

import arb19_kpa1.game.model.GameState;
import common.msg.IRoomMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * A message for sending an updated GameState object
 * @author Owner
 *
 */
public class GameStateMsg implements IRoomMsg {
	
	/**
	 * UID
	 */
	private static final long serialVersionUID = 5554101347207347722L;
	/**
	 * The Game state
	 */
	private GameState state;
	
	/**
	 * Creates a new GameStateMsg
	 * @param state the game state
	 */
	public GameStateMsg(GameState state) {
		this.state  = state;
	}

	@Override
	public IDataPacketID getID() {
		return GameStateMsg.GetID();
	}
	
	/**
	 * Gets this messages ID
	 * @return the ID
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(GameStateMsg.class);
	}
	
	/**
	 * Gets the game state
	 * @return the GameState object
	 */
	public GameState getState() {
		return state;
	}

}
