package arb19_kpa1.client.model.gamemvc.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import common.virtualNetwork.IRoom;

/**
 * The mini-mvc view for the game
 *
 */
public class RoomView extends JPanel {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 2448972497688484774L;
	/**
	 * The adapter for communication with the game model
	 */
	private IRoomView2RoomModelAdpt view2Model;
	/**
	 * The tabbed panel containing all of the games
	 */
	private final JTabbedPane gameTabs = new JTabbedPane(JTabbedPane.TOP);
	/**
	 * The panel containing the game chat
	 */
	private final JPanel gameChatPanel = new JPanel();
	/**
	 * The list model containing all players in the game
	 */
	private DefaultListModel<ViewTeamMemberContainer> gameMembersListModel = new DefaultListModel<>();
	/**
	 * JList holding all players in the game
	 */
	private final JList<ViewTeamMemberContainer> gameMembersList = new JList<>(gameMembersListModel);
	/**
	 * The message field for sending game chat
	 */
	private final JTextField gameChatMessageField = new JTextField();
	/**
	 * The button for sending messages to all players
	 */
	private final JButton sendGameMessageBtn = new JButton("Send Message");
	/**
	 * The text area for displaying the game chat
	 */
	private final JTextArea gameChatTextArea = new JTextArea();
	/**
	 * Leave room button
	 */
	private final JButton btnLeaveRoom = new JButton("Leave Room");
	/**
	 * Control panel
	 */
	private final JPanel ctrlPanel = new JPanel();


	/**
	 * Create the view for the game
	 * @param view2Model the adapter for communication with the game model
	 */
	public RoomView(IRoomView2RoomModelAdpt view2Model) {
		gameChatMessageField.setColumns(10);
		this.view2Model = view2Model;
		initGUI();
	}
	
	/**
	 * Initializes all GUI components
	 */
	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		
		add(gameTabs);
		
		gameTabs.addTab("Game Chat", null, gameChatPanel, null);
		GridBagLayout gbl_gameChatPanel = new GridBagLayout();
		gbl_gameChatPanel.columnWidths = new int[] {240, 80};
		gbl_gameChatPanel.rowHeights = new int[] {200, 20};
		gbl_gameChatPanel.columnWeights = new double[]{1.0, 1.0};
		gbl_gameChatPanel.rowWeights = new double[]{1.0, 0.0};
		gameChatPanel.setLayout(gbl_gameChatPanel);
		
		GridBagConstraints gbc_gameChatTextArea = new GridBagConstraints();
		gbc_gameChatTextArea.insets = new Insets(0, 0, 5, 5);
		gbc_gameChatTextArea.fill = GridBagConstraints.BOTH;
		gbc_gameChatTextArea.gridx = 0;
		gbc_gameChatTextArea.gridy = 0;
		gameChatPanel.add(gameChatTextArea, gbc_gameChatTextArea);
		
		GridBagConstraints gbc_gameMembersList = new GridBagConstraints();
		gbc_gameMembersList.insets = new Insets(0, 0, 5, 0);
		gbc_gameMembersList.fill = GridBagConstraints.BOTH;
		gbc_gameMembersList.gridx = 1;
		gbc_gameMembersList.gridy = 0;
		gameMembersList.setBorder(new TitledBorder(null, "Game Members", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		gameChatPanel.add(gameMembersList, gbc_gameMembersList);
		
		GridBagConstraints gbc_gameChatMessageField = new GridBagConstraints();
		gbc_gameChatMessageField.insets = new Insets(0, 0, 0, 5);
		gbc_gameChatMessageField.fill = GridBagConstraints.BOTH;
		gbc_gameChatMessageField.gridx = 0;
		gbc_gameChatMessageField.gridy = 1;
		gameChatPanel.add(gameChatMessageField, gbc_gameChatMessageField);
		
		GridBagConstraints gbc_ctrlPanel = new GridBagConstraints();
		gbc_ctrlPanel.fill = GridBagConstraints.BOTH;
		gbc_ctrlPanel.gridx = 1;
		gbc_ctrlPanel.gridy = 1;
		gameChatPanel.add(ctrlPanel, gbc_ctrlPanel);
		ctrlPanel.setLayout(new BorderLayout(0, 0));
		ctrlPanel.add(sendGameMessageBtn);
		sendGameMessageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2Model.sendStringMessage(gameChatMessageField.getText());
			}
		});
		ctrlPanel.add(btnLeaveRoom, BorderLayout.WEST);
		btnLeaveRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2Model.leaveRoom();
			}
		});
	}

	/**
	 * Starts the view
	 */
	public void start() {}
	
	/**
	 * @param component JComponent to add to the tabbed pane
	 * @param title String title of the component added
	 */
	public void addGameTab(JComponent component, String title) {
		gameTabs.addTab(title, component);
	}
	

	/**
	 * Sets the members in the game room members list
	 * @param room the current room
	 */
	public void setMembers(IRoom room) {
		gameMembersListModel.clear();
		room.getMembers().forEach((p) -> gameMembersListModel.addElement(new ViewTeamMemberContainer(p)));
	}
	
	/**
	 * Displays a string in the main game chat panel
	 * @param text String of the message sent
	 */
	public void displayText(String text) {
		gameChatTextArea.append(text);
	}

}
