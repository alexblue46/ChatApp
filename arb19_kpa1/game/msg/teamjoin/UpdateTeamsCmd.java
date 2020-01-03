package arb19_kpa1.game.msg.teamjoin;

import java.util.Map;

import arb19_kpa1.game.msg.AGameCmd;
import common.packet.RoomDataPacket;
import provided.datapacket.IDataPacketID;
import provided.mixedData.MixedDataKey;

/**
 * Command to update teams
 *
 */
public class UpdateTeamsCmd extends AGameCmd<UpdateTeamsMsg> {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 3843592622047472500L;

	/**
	 * Creates a new UpdateTeamsCmd
	 * @param key the main mixed data dictionary key
	 */
	@SuppressWarnings("rawtypes")
	public UpdateTeamsCmd(MixedDataKey<Map> key) {
		super(key);
	}

	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<UpdateTeamsMsg> host, Void... params) {
		super.mddUtil.setTeams(host.getData().getTeams());
		super.mddUtil.getTeamJoiningView().updateRosters(host.getData().getTeams());
		return null;
	}

}
