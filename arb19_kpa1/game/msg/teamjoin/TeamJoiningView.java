package arb19_kpa1.game.msg.teamjoin;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import arb19_kpa1.server.view.ViewRoomMemberContainer;
import common.receivers.IRoomMember;

/**
 * The team joining view
 *
 */
public class TeamJoiningView extends JPanel {
	/**
	 * UID
	 */
	private static final long serialVersionUID = -4659383328219947325L;
	/**
	 * The panel fro joining teams
	 */
	private final JPanel teamJoiningPanel = new JPanel();
	/**
	 * Button for joining team 1
	 */
	private final JButton joinTeam1Btn = new JButton("Join Team 1");
	/**
	 * Button for joining team 2
	 */
	private final JButton joinTeam2Btn = new JButton("Join Team 2");
	/**
	 * Roster for team 1
	 */
	private final JComboBox<ViewRoomMemberContainer> team1Roster = new JComboBox<ViewRoomMemberContainer>();
	/**
	 * Roster for team2
	 */
	private final JComboBox<ViewRoomMemberContainer> team2Roster = new JComboBox<ViewRoomMemberContainer>();

	/**
	 * Create the panel.
	 */
	public TeamJoiningView() {
		initGUI();
	}
	
	/**
	 * Initialize GUI components
	 */
	private void initGUI() {
		System.out.println("Initializing team joining view");
		setLayout(new BorderLayout(0, 0));
		
		add(teamJoiningPanel, BorderLayout.CENTER);
		GridBagLayout gbl_teamJoiningPanel = new GridBagLayout();
		gbl_teamJoiningPanel.columnWidths = new int[] {220, 220};
		gbl_teamJoiningPanel.rowHeights = new int[] {100, 100};
		gbl_teamJoiningPanel.columnWeights = new double[]{0.0, 0.0};
		gbl_teamJoiningPanel.rowWeights = new double[]{0.0, 0.0};
		teamJoiningPanel.setLayout(gbl_teamJoiningPanel);
		
		GridBagConstraints gbc_joinTeam1Btn = new GridBagConstraints();
		gbc_joinTeam1Btn.fill = GridBagConstraints.HORIZONTAL;
		gbc_joinTeam1Btn.anchor = GridBagConstraints.NORTHWEST;
		gbc_joinTeam1Btn.insets = new Insets(0, 0, 0, 5);
		gbc_joinTeam1Btn.gridx = 0;
		gbc_joinTeam1Btn.gridy = 0;
		teamJoiningPanel.add(joinTeam1Btn, gbc_joinTeam1Btn);
		
		GridBagConstraints gbc_joinTeam2Btn = new GridBagConstraints();
		gbc_joinTeam2Btn.fill = GridBagConstraints.HORIZONTAL;
		gbc_joinTeam2Btn.anchor = GridBagConstraints.NORTHWEST;
		gbc_joinTeam2Btn.insets = new Insets(0, 0, 0, 5);
		gbc_joinTeam2Btn.gridx = 1;
		gbc_joinTeam2Btn.gridy = 0;
		teamJoiningPanel.add(joinTeam2Btn, gbc_joinTeam2Btn);
		GridBagConstraints gbc_team1Roster = new GridBagConstraints();
		gbc_team1Roster.fill = GridBagConstraints.HORIZONTAL;
		gbc_team1Roster.anchor = GridBagConstraints.NORTHWEST;
		gbc_team1Roster.insets = new Insets(0, 0, 0, 5);
		gbc_team1Roster.gridx = 0;
		gbc_team1Roster.gridy = 1;
		teamJoiningPanel.add(team1Roster, gbc_team1Roster);
		GridBagConstraints gbc_team2Roster = new GridBagConstraints();
		gbc_team2Roster.fill = GridBagConstraints.HORIZONTAL;
		gbc_team2Roster.anchor = GridBagConstraints.NORTHWEST;
		gbc_team2Roster.gridx = 1;
		gbc_team2Roster.gridy = 1;
		teamJoiningPanel.add(team2Roster, gbc_team2Roster);

	}
	
	/**
	 * Update team rosters
	 * @param teams the new teams
	 */
	public void updateRosters(Map<Integer, Collection<IRoomMember>> teams) {
		teams.get(1).forEach(member -> {
			try {
				System.out.println(member.getName());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		teams.get(2).forEach(member -> {
			try {
				System.out.println(member.getName());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		team1Roster.removeAllItems();
		team2Roster.removeAllItems();
		
		teams.get(1).forEach(member -> team1Roster.addItem(new ViewRoomMemberContainer(member)));
		teams.get(2).forEach(member -> team2Roster.addItem(new ViewRoomMemberContainer(member)));
	}
	
	/**
	 * get the team1 button
	 * @return the button
	 */
	public JButton getTeam1Btn() {
		return joinTeam1Btn;
	}
	
	/**
	 * Gets the team2 button
	 * @return the button
	 */
	public JButton getTeam2Btn() {
		return joinTeam2Btn;
	}

}
