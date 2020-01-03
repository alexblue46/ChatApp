package arb19_kpa1.game.msg;

import java.util.Map;

import arb19_kpa1.game.model.GameState;
import common.msg.IRoomMsg;
import common.receivers.IRoomMember;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Message for starting the game
 * 
 * @author Owner
 *
 */
public class StartGameMsg implements IRoomMsg {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -6377627892843355499L;

	/**
	 * The turn ordering
	 */
	private Map<IRoomMember, IRoomMember> turnOrder;

	/**
	 * The initial game state
	 */
	private GameState initialState;

	/**
	 * The stub of the player
	 */
	private IRoomMember stub;

	/**
	 * Creates a new StartGameMsg
	 * 
	 * @param turnOrder    the turn order mapping
	 * @param initialState the iniail game state
	 * @param stub         the player's stub
	 */
	public StartGameMsg(Map<IRoomMember, IRoomMember> turnOrder, GameState initialState, IRoomMember stub) {
		this.turnOrder = turnOrder;
		this.initialState = initialState;
		this.stub = stub;
	}

	@Override
	public IDataPacketID getID() {
		return StartGameMsg.GetID();
	}

	/**
	 * Gets this message ID
	 * 
	 * @return the ID
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(StartGameMsg.class);
	}

	/**
	 * Gets the turn order mapping
	 * 
	 * @return the mapping
	 */
	public Map<IRoomMember, IRoomMember> getTurnOrder() {
		return turnOrder;
	}

	/**
	 * Gets the initial game state
	 * 
	 * @return the game state
	 */
	public GameState getInitialState() {
		return initialState;
	}

	/**
	 * Gets the player's stub
	 * 
	 * @return the stub
	 */
	public IRoomMember getStub() {
		return stub;
	}

}
