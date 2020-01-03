package arb19_kpa1.server.model.mvc.model;

import java.util.Collection;
import java.util.Map;

import common.receivers.IRoomMember;

/**
 * Adapter from the server room model to the server room view
 *
 */
public interface IServerRoomModel2ServerRoomViewAdpt {

	/**
	 * Updates the room members in the view
	 * 
	 * @param newRoomMembers the new collection of room members
	 */
	void updateRoomMembers(Collection<IRoomMember> newRoomMembers);

	/**
	 * Updates the teams in the view
	 * 
	 * @param teams the new mapping of teams
	 */
	void updateTeams(Map<Integer, Collection<IRoomMember>> teams);

	/**
	 * Updates the progress of the game
	 * 
	 * @param gameInProgress whether the game is in progress
	 */
	void updateGameStatus(Boolean gameInProgress);
}
