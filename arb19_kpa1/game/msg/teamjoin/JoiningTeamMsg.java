package arb19_kpa1.game.msg.teamjoin;

import common.msg.IRoomMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;

/**
 * @author Owner
 * Msg to initialize team joining
 */
public class JoiningTeamMsg implements IRoomMsg{
	
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 7876227727865582718L;
	/**
	 * Team to join
	 */
	private int teamToJoin;
	
	/**
	 * @param teamToJoin Team that sender wants to join
	 */
	public JoiningTeamMsg(int teamToJoin) {
		this.teamToJoin = teamToJoin;
	}
	
	/**
	 * @return ID for the message
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(JoiningTeamMsg.class);
	}

	@Override
	public IDataPacketID getID() {
		return JoiningTeamMsg.GetID();
	}

	/**
	 * @return team sender wants to join
	 */
	public int getTeam() {
		return teamToJoin;
	}
}
