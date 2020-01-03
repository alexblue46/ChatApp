package arb19_kpa1.game.msg;

import java.rmi.RemoteException;
import java.util.Map;

import common.packet.RoomDataPacket;
import provided.datapacket.IDataPacketID;
import provided.mixedData.MixedDataKey;

/**
 * Command for handling team string messages
 * @author Owner
 *
 */
public class TeamStringCmd extends AGameCmd<TeamStringMsg> {
	

	/**
	 * UID
	 */
	private static final long serialVersionUID = -8990427924102253373L;

	/**
	 * Creates a new TeamStringCmd
	 * @param key the main MDD key
	 */
	@SuppressWarnings("rawtypes")
	public TeamStringCmd(MixedDataKey<Map> key) {
		super(key);
	}

	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<TeamStringMsg> host, Void... params) {
		try {
			super.mddUtil.getGameController().getView().displayTeamMsg((host.getSender().getName() + ": " + host.getData().getText() + "\n"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

}
