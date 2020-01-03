package arb19_kpa1.server.model.mvc.view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;

import arb19_kpa1.server.view.ViewRoomMemberContainer;
import common.receivers.IRoomMember;

import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * @author alexbluestein
 * Room-level server view
 */
public class ServerRoomView extends JPanel {
	
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -5750720297195902696L;
	/**
	 * Adapter from the view to the model
	 */
	private IServerRoomView2ServerRoomModelAdpt view2model;
	/**
	 * Label indicating the status of the game
	 */
	private final JLabel gameStatusLabel = new JLabel("Game Status: Not in Progress");
	/**
	 * Panel to display the teams' rosters
	 */
	private final JPanel rostersPanel = new JPanel();
	/**
	 * Panel to display the members of the room
	 */
	private final JPanel roomMembersPanel = new JPanel();
	/**
	 * JComboBox for room members
	 */
	private final JComboBox<ViewRoomMemberContainer> roomMembers = new JComboBox<ViewRoomMemberContainer>();
	/**
	 * Panel to display team 1's roster
	 */
	private final JPanel team1Panel = new JPanel();
	/**
	 * JComboBox for team 1's roster
	 */
	private final JComboBox<ViewRoomMemberContainer> team1Roster = new JComboBox<ViewRoomMemberContainer>();
	/**
	 * Panel to display team 2's roster
	 */
	private final JPanel team2Panel = new JPanel();
	/**
	 * JComboBox for team 2's roster
	 */
	private final JComboBox<ViewRoomMemberContainer> team2Roster = new JComboBox<ViewRoomMemberContainer>();
	/**
	 * Panel for the buttons
	 */
	private final JPanel btnsPanel = new JPanel();
	/**
	 * Button to start the game
	 */
	private final JButton startGameBtn = new JButton("Start Game");
	/**
	 * Button to leave the room
	 */
	private final JButton endRoomBtn = new JButton("Leave Room");

	/**
	 * Create the panel.
	 * @param view2model Adapter from the view to the model
	 */
	public ServerRoomView(IServerRoomView2ServerRoomModelAdpt view2model) {
		this.view2model = view2model;
		initGUI();
	}
	
	/**
	 * Initialize the GUI
	 */
	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		
		add(rostersPanel, BorderLayout.CENTER);
		
		add(gameStatusLabel, BorderLayout.NORTH);
		GridBagLayout gbl_rostersPanel = new GridBagLayout();
		gbl_rostersPanel.columnWidths = new int[] {150, 150, 150};
		gbl_rostersPanel.rowHeights = new int[] {150};
		gbl_rostersPanel.columnWeights = new double[]{0.0, 0.0, 0.0};
		gbl_rostersPanel.rowWeights = new double[]{0.0};
		rostersPanel.setLayout(gbl_rostersPanel);
		roomMembersPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Room Members", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		GridBagConstraints gbc_roomMembersPanel = new GridBagConstraints();
		gbc_roomMembersPanel.fill = GridBagConstraints.BOTH;
		gbc_roomMembersPanel.anchor = GridBagConstraints.NORTHWEST;
		gbc_roomMembersPanel.insets = new Insets(0, 0, 0, 5);
		gbc_roomMembersPanel.gridx = 0;
		gbc_roomMembersPanel.gridy = 0;
		rostersPanel.add(roomMembersPanel, gbc_roomMembersPanel);
		roomMembersPanel.setLayout(new BorderLayout(0, 0));
		
		roomMembersPanel.add(roomMembers);
		team1Panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Team 1 Roster", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		team1Panel.setLayout(new BorderLayout(0, 0));
		team1Panel.add(team1Roster);
		team1Panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Team 1 Roster", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		GridBagConstraints gbc_team1Panel = new GridBagConstraints();
		gbc_team1Panel.fill = GridBagConstraints.BOTH;
		gbc_team1Panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_team1Panel.insets = new Insets(0, 0, 0, 5);
		gbc_team1Panel.gridx = 1;
		gbc_team1Panel.gridy = 0;
		rostersPanel.add(team1Panel, gbc_team1Panel);
		
		team1Panel.add(team1Roster);
		
		team2Panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Team 2 Roster", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		team2Panel.setLayout(new BorderLayout(0, 0));
		team2Panel.add(team2Roster);
		team2Panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Team 2 Roster", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		GridBagConstraints gbc_team2Panel = new GridBagConstraints();
		gbc_team2Panel.fill = GridBagConstraints.BOTH;
		gbc_team2Panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_team2Panel.gridx = 2;
		gbc_team2Panel.gridy = 0;
		rostersPanel.add(team2Panel, gbc_team2Panel);
		
		team2Panel.add(team2Roster);
		
		add(btnsPanel, BorderLayout.SOUTH);
		
		btnsPanel.add(startGameBtn);
		
		btnsPanel.add(endRoomBtn);
		
		startGameBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				view2model.startGame();
			}
			
		});
		
		endRoomBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				view2model.deleteRoom();
			}
			
		});
	}
	
	/**
	 * Update the status of the game
	 * @param inProgress boolean indicating whether the game is in progress
	 */
	public void updateGameStatus(Boolean inProgress) {
		gameStatusLabel.setText("Game Status: " + (inProgress ? "In Progress" : "Not In Progress"));
	}
	
	/**
	 * @param team1 Roster of team 1
	 * @param team2 Roster of team 2
	 */
	public void updateTeams(Collection<IRoomMember> team1, Collection<IRoomMember> team2) {
		team1Roster.removeAllItems();
		team2Roster.removeAllItems();
		
		team1.forEach(member -> team1Roster.addItem(new ViewRoomMemberContainer(member)));
		team2.forEach(member -> team2Roster.addItem(new ViewRoomMemberContainer(member)));
	}
	
	/**
	 * @param newRoomMembers Members in the room
	 */
	public void updateRoomMembers(Collection<IRoomMember> newRoomMembers) {
		roomMembers.removeAllItems();
		
		newRoomMembers.forEach(member -> roomMembers.addItem(new ViewRoomMemberContainer(member)));
	}

}
