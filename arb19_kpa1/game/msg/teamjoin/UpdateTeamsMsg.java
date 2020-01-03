package arb19_kpa1.game.msg.teamjoin;

import java.util.Collection;
import java.util.Map;

import common.msg.IRoomMsg;
import common.receivers.IRoomMember;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * Creates a new UpdateTeamsMsg
 *
 */
public class UpdateTeamsMsg implements IRoomMsg {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 5193501914137091880L;
	/**
	 * The team mapping
	 */
	private Map<Integer, Collection<IRoomMember>> teams;

	/**
	 * Creates a new UpdateTeamsMsg
	 * 
	 * @param teams the team mapping
	 */
	public UpdateTeamsMsg(Map<Integer, Collection<IRoomMember>> teams) {
		this.teams = teams;
	}

	/**
	 * Gets the ID for this msg
	 * @return the id
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(UpdateTeamsMsg.class);
	}

	@Override
	public IDataPacketID getID() {
		return UpdateTeamsMsg.GetID();
	}

	/**
	 * Gets the team mapping
	 * @return the mapping from team number to members of the team
	 */
	public Map<Integer, Collection<IRoomMember>> getTeams() {
		return teams;
	}

}
