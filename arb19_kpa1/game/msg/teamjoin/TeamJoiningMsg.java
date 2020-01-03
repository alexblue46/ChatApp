package arb19_kpa1.game.msg.teamjoin;

import java.util.Collection;
import java.util.Map;

import common.receivers.IRoomMember;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * The message for joining teams
 *
 */
public class TeamJoiningMsg extends UpdateTeamsMsg {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 7740679458627073670L;
	/**
	 * The team join view
	 */
	private TeamJoiningView view;

	/**
	 * Creates a new TeamJoinMsg
	 * 
	 * @param teams the teams
	 */
	public TeamJoiningMsg(Map<Integer, Collection<IRoomMember>> teams) {
		super(teams);
		this.view = new TeamJoiningView();
	}

	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(TeamJoiningMsg.class);
	}

	@Override
	public IDataPacketID getID() {
		return TeamJoiningMsg.GetID();
	}

	/**
	 * Gets the view
	 * 
	 * @return the view
	 */
	public TeamJoiningView getView() {
		return view;
	}
}
