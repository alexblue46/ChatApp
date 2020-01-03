package arb19_kpa1.game;

/**
 * A mapping of keys in the client mixed data dictionary. When a client accesses the MDD, 
 * they first access 
 *
 */
public enum MDDGameKeys {
	/**
	 * The key for the team join view object
	 */
	TEAM_JOIN_VIEW,
	/**
	 * The key for a Map<Integer, Collection<IRoomMember>> from team number to teams
	 */
	TEAMS,
	/**
	 * The integer corresponding the the current client team
	 */
	MY_TEAM_NUM,
	/**
	 * The game view
	 */
	GAME_VIEW,
	/**
	 * The game controller
	 */
	GAME_CONTROLLER,
	/**
	 * Order of players
	 */
	TURN_ORDER,
	/**
	 * Stub of player
	 */
	MEMBER_STUB
	;
	
}
