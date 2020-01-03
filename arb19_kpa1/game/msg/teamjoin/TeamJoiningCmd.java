package arb19_kpa1.game.msg.teamjoin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.JComponent;

import arb19_kpa1.game.msg.AGameCmd;
import common.packet.RoomDataPacket;
import provided.datapacket.IDataPacketID;
import provided.mixedData.MixedDataKey;


/**
 * Command to handle the initial team joining
 */
public class TeamJoiningCmd extends AGameCmd<TeamJoiningMsg> {
	

	/**
	 * UID
	 */
	private static final long serialVersionUID = -7711723662614967090L;

	/**
	 * Creates a new TeamJoin command
	 * @param key the MDD key
	 */
	@SuppressWarnings("rawtypes")
	public TeamJoiningCmd(MixedDataKey<Map> key) {
		super(key);
	}

	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<TeamJoiningMsg> host, Void... params) {
		TeamJoiningView view = host.getData().getView();
		
		super.mddUtil.setTeamJoiningView(view);
		super.mddUtil.setTeams(host.getData().getTeams());
		
		view.getTeam1Btn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cmd2model.sendMessageToMember(new JoiningTeamMsg(1), host.getSender());
				mddUtil.setMyTeam(1);
			}
			
		});
		
		view.getTeam2Btn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cmd2model.sendMessageToMember(new JoiningTeamMsg(2), host.getSender());
				mddUtil.setMyTeam(2);
			}
			
		});
		
		cmd2model.displayComponent(new Supplier<JComponent>() {

			@Override
			public JComponent get() {
				return view;
			}
			
		}, "Team Joining");

		mddUtil.getTeamJoiningView().updateRosters(host.getData().getTeams());
		
		return null;
	}

}
