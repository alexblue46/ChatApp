package arb19_kpa1.game.model.utility;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import arb19_kpa1.game.MDDGameKeys;
import arb19_kpa1.game.controller.GameController;
import arb19_kpa1.game.msg.teamjoin.TeamJoiningView;
import common.ICmd2ModelAdapter;
import common.receivers.IRoomMember;
import provided.mixedData.MixedDataKey;

/**
 * Utility for interacting with the mixed data dictionary
 *
 */
public final class MDDUtility implements Serializable {
	/**
	 * UID
	 */
	private static final long serialVersionUID = 7492422367754065165L;
	/**
	 * The main key for getting the map to other keys
	 */
	@SuppressWarnings("rawtypes")
	private MixedDataKey<Map> key;
	/**
	 * THe cmd2model adapter
	 */
	private transient ICmd2ModelAdapter cmd2model;

	/**
	 * Creates a new MDDUtility object
	 * 
	 * @param key the main key
	 */
	public MDDUtility(@SuppressWarnings("rawtypes") MixedDataKey<Map> key) {
		this.key = key;
	}

	/**
	 * Sets the command 2 model addapter, must be called on deserialization
	 * 
	 * @param cmd2ModelAdpt the adapter
	 */
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2model = cmd2ModelAdpt;
	}

	/**
	 * Gets the map of keys to important values
	 * 
	 * @return the map
	 */
	@SuppressWarnings("unchecked")
	protected Map<MDDGameKeys, MixedDataKey<?>> getKeys() {
		return cmd2model.getItemInDB(key);
	}

	/**
	 * Gets a value corresponding to a gamekey
	 * 
	 * @param key the gamekey
	 * @return the value
	 */
	protected Object getMDDValue(MDDGameKeys key) {
		return cmd2model.getItemInDB(getKeys().get(key));
	}

	/**
	 * Gets the TeamJoinView panel
	 * 
	 * @return the panel
	 */
	public TeamJoiningView getTeamJoiningView() {
		return (TeamJoiningView) getMDDValue(MDDGameKeys.TEAM_JOIN_VIEW);
	}

	/**
	 * Gets the team mapping
	 * 
	 * @return the teams
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, Collection<IRoomMember>> getTeams() {
		return (Map<Integer, Collection<IRoomMember>>) getMDDValue(MDDGameKeys.TEAMS);
	}

	/**
	 * Gets the current player's team number
	 * 
	 * @return integer
	 */
	public int getMyTeamNum() {
		return (int) getMDDValue(MDDGameKeys.MY_TEAM_NUM);
	}

	/**
	 * Gets the game controller object
	 * 
	 * @return the game controller
	 */
	public GameController getGameController() {
		return (GameController) getMDDValue(MDDGameKeys.GAME_CONTROLLER);
	}

	/**
	 * Gets the local users stub
	 * 
	 * @return the stub
	 */
	public IRoomMember getStub() {
		return (IRoomMember) getMDDValue(MDDGameKeys.MEMBER_STUB);
	}

	/**
	 * Gets the turn order
	 * 
	 * @return the turn order mapping
	 */
	@SuppressWarnings("unchecked")
	public Map<IRoomMember, IRoomMember> getTurnOrder() {
		return (Map<IRoomMember, IRoomMember>) getMDDValue(MDDGameKeys.TURN_ORDER);
	}

	/**
	 * Sets the teams in the mixed data dictionary
	 * 
	 * @param teams the teams
	 */
	@SuppressWarnings("unchecked")
	public void setTeams(Map<Integer, Collection<IRoomMember>> teams) {
		cmd2model.putItemInDB((MixedDataKey<Map<Integer, Collection<IRoomMember>>>) getKeys().get(MDDGameKeys.TEAMS),
				teams);
	}

	/**
	 * Sets the teamJoinView panel
	 * 
	 * @param view the panel
	 */
	@SuppressWarnings("unchecked")
	public void setTeamJoiningView(TeamJoiningView view) {
		cmd2model.putItemInDB((MixedDataKey<TeamJoiningView>) getKeys().get(MDDGameKeys.TEAM_JOIN_VIEW), view);
	}

	/**
	 * Sets the local users team
	 * 
	 * @param i the team
	 */
	@SuppressWarnings("unchecked")
	public void setMyTeam(Integer i) {
		cmd2model.putItemInDB((MixedDataKey<Integer>) getKeys().get(MDDGameKeys.MY_TEAM_NUM), i);
	}

	/**
	 * Sets the game controller object
	 * 
	 * @param controller the object
	 */
	@SuppressWarnings("unchecked")
	public void setGameController(GameController controller) {
		cmd2model.putItemInDB((MixedDataKey<GameController>) getKeys().get(MDDGameKeys.GAME_CONTROLLER), controller);
	}

	/**
	 * Sets the turn mapping in the mdd
	 * 
	 * @param turnOrder the mapping
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setTurnOrder(Map<IRoomMember, IRoomMember> turnOrder) {
		cmd2model.putItemInDB((MixedDataKey<Map>) getKeys().get(MDDGameKeys.TURN_ORDER), turnOrder);
	}

	/**
	 * Sets the stub in teh mDD
	 * 
	 * @param stub the stub
	 */
	@SuppressWarnings("unchecked")
	public void setStub(IRoomMember stub) {
		cmd2model.putItemInDB((MixedDataKey<IRoomMember>) getKeys().get(MDDGameKeys.MEMBER_STUB), stub);
	}
}
