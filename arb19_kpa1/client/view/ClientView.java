package arb19_kpa1.client.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import provided.discovery.IEndPointData;
import provided.discovery.impl.view.DiscoveryPanel;
import provided.discovery.impl.view.IDiscoveryPanelAdapter;

/**
 * The mvc view for the client
 *
 * @param <ServerT> the type of the server  
 * @param <ClientT> the type of the client 
 */
public class ClientView<ServerT, ClientT> extends JFrame {
	
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -736161715739379476L;

	/**
	 * The adapter for communication with the model
	 */
	private IClientView2ModelAdpt<ServerT> view2model;

	/**
	 * The main panel for the view
	 */
	private JPanel contentPane;
	/**
	 * Panel for holding controls
	 */
	private final JPanel allCtrlsPanel = new JPanel();
	/**
	 * The tabbed pane for containing the games
	 */
	private final JTabbedPane gamesTabbedPane = new JTabbedPane(JTabbedPane.TOP);
	/**
	 * The panel for containing controls for joining servers
	 */
	private final JPanel serverJoinPanel = new JPanel();
	/**
	 * Combo box for containing servers to connect to
	 */
	private final JComboBox<ViewApplicationContainer> connectedServerComboBox = new JComboBox<>();
	/**
	 * Set of connected servers
	 */
	private Set<ViewApplicationContainer> connectedServerSet = new HashSet<>();
	/**
	 * Button for joining the selected game
	 */
	private final JButton btnGetApplicationGames = new JButton("Get App Games");
	
	/**
	 * Discovery panel for getting clients and servers
	 */
	private DiscoveryPanel<IEndPointData> clientsDiscoveryPanel;
	/**
	 * Panel holding controls for joining games
	 */
	private final JPanel gameJoinPanel = new JPanel();
	/**
	 * Combo box holding games from the given IApplication
	 */
	private final JComboBox<ViewRoomIdentityContainer> serverGamesComboBox = new JComboBox<>();
	/**
	 * Set of server's games
	 */
	private Set<ViewRoomIdentityContainer> serverGamesSet = new HashSet<>();
	/**
	 * Join game button
	 */
	private final JButton btnJoinGame = new JButton("Join Game");
	/**
	 * Get connected applications button
	 */
	private final JButton btnGetConnectedApplications = new JButton("Get Connected Apps");

	/**
	 * Creates the view
	 * @param view2model the adapter for communicating with the client model
	 * @param discAdpt Adapter for handling discovery server connections
	 */
	public ClientView(IClientView2ModelAdpt<ServerT> view2model, IDiscoveryPanelAdapter<IEndPointData> discAdpt) {
		this.view2model = view2model;
		this.clientsDiscoveryPanel = new DiscoveryPanel<IEndPointData>(discAdpt);
		initGUI();
	}

	/**
	 * Initialize GUI components
	 */
	private void initGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 579, 566);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] {450};
		gbl_contentPane.rowHeights = new int[] {200, 200};
		gbl_contentPane.columnWeights = new double[]{1.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0};
		contentPane.setLayout(gbl_contentPane);
		
		GridBagConstraints gbc_allCtrlsPanel = new GridBagConstraints();
		gbc_allCtrlsPanel.insets = new Insets(0, 0, 5, 5);
		gbc_allCtrlsPanel.fill = GridBagConstraints.BOTH;
		gbc_allCtrlsPanel.gridx = 0;
		gbc_allCtrlsPanel.gridy = 0;
		contentPane.add(allCtrlsPanel, gbc_allCtrlsPanel);
		GridBagLayout gbl_allCtrlsPanel = new GridBagLayout();
		gbl_allCtrlsPanel.columnWidths = new int[] {180, 80};
		gbl_allCtrlsPanel.rowHeights = new int[] {100, 30, 30};
		gbl_allCtrlsPanel.columnWeights = new double[]{1.0, 0.0};
		gbl_allCtrlsPanel.rowWeights = new double[]{1.0, 1.0, 0.0};
		allCtrlsPanel.setLayout(gbl_allCtrlsPanel);

		GridBagConstraints gbc_clientsDiscoveryPanel = new GridBagConstraints();
		gbc_clientsDiscoveryPanel.gridwidth = 2;
		gbc_clientsDiscoveryPanel.insets = new Insets(0, 0, 5, 5);
		gbc_clientsDiscoveryPanel.gridx = 0;
		gbc_clientsDiscoveryPanel.gridy = 0;
		allCtrlsPanel.add(clientsDiscoveryPanel, gbc_clientsDiscoveryPanel);
		
		GridBagConstraints gbc_gameJoinPanel = new GridBagConstraints();
		gbc_gameJoinPanel.gridwidth = 2;
		gbc_gameJoinPanel.fill = GridBagConstraints.BOTH;
		gbc_gameJoinPanel.gridx = 0;
		gbc_gameJoinPanel.gridy = 2;
		allCtrlsPanel.add(gameJoinPanel, gbc_gameJoinPanel);
		GridBagLayout gbl_gameJoinPanel = new GridBagLayout();
		gbl_gameJoinPanel.columnWidths = new int[] {300, 130};
		gbl_gameJoinPanel.rowHeights = new int[] {20};
		gbl_gameJoinPanel.columnWeights = new double[]{0.0, 0.0};
		gbl_gameJoinPanel.rowWeights = new double[]{0.0};
		gameJoinPanel.setLayout(gbl_gameJoinPanel);
		
		GridBagConstraints gbc_serverGamesComboBox = new GridBagConstraints();
		gbc_serverGamesComboBox.fill = GridBagConstraints.BOTH;
		gbc_serverGamesComboBox.anchor = GridBagConstraints.NORTHWEST;
		gbc_serverGamesComboBox.gridx = 0;
		gbc_serverGamesComboBox.gridy = 0;
		gameJoinPanel.add(serverGamesComboBox, gbc_serverGamesComboBox);
		
		GridBagConstraints gbc_btnJoinGame = new GridBagConstraints();
		gbc_btnJoinGame.insets = new Insets(0, 0, 0, 5);
		gbc_btnJoinGame.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnJoinGame.fill = GridBagConstraints.BOTH;
		gbc_btnJoinGame.gridx = 1;
		gbc_btnJoinGame.gridy = 0;
		btnJoinGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2model.joinGame(connectedServerComboBox.getItemAt(connectedServerComboBox.getSelectedIndex()), 
						serverGamesComboBox.getItemAt(serverGamesComboBox.getSelectedIndex()));
			}
		});
		gameJoinPanel.add(btnJoinGame, gbc_btnJoinGame);
		
		GridBagConstraints gbc_serverJoinPanel = new GridBagConstraints();
		gbc_serverJoinPanel.gridwidth = 2;
		gbc_serverJoinPanel.fill = GridBagConstraints.BOTH;
		gbc_serverJoinPanel.gridx = 0;
		gbc_serverJoinPanel.gridy = 1;
		allCtrlsPanel.add(serverJoinPanel, gbc_serverJoinPanel);
		GridBagLayout gbl_serverJoinPanel = new GridBagLayout();
		gbl_serverJoinPanel.columnWidths = new int[] {250, 100, 100};
		gbl_serverJoinPanel.rowHeights = new int[] {20};
		gbl_serverJoinPanel.columnWeights = new double[]{0.0, 0.0, 0.0};
		gbl_serverJoinPanel.rowWeights = new double[]{0.0};
		serverJoinPanel.setLayout(gbl_serverJoinPanel);
		
		GridBagConstraints gbc_connectedServerComboBox = new GridBagConstraints();
		gbc_connectedServerComboBox.insets = new Insets(0, 0, 0, 5);
		gbc_connectedServerComboBox.fill = GridBagConstraints.BOTH;
		gbc_connectedServerComboBox.anchor = GridBagConstraints.NORTHWEST;
		gbc_connectedServerComboBox.gridx = 0;
		gbc_connectedServerComboBox.gridy = 0;
		serverJoinPanel.add(connectedServerComboBox, gbc_connectedServerComboBox);
		
		GridBagConstraints gbc_btnGetApplicationGames = new GridBagConstraints();
		gbc_btnGetApplicationGames.insets = new Insets(0, 0, 0, 5);
		gbc_btnGetApplicationGames.fill = GridBagConstraints.BOTH;
		gbc_btnGetApplicationGames.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnGetApplicationGames.gridx = 2;
		gbc_btnGetApplicationGames.gridy = 0;
		btnGetApplicationGames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2model.getAppGames(connectedServerComboBox.getItemAt(connectedServerComboBox.getSelectedIndex()));
			}
		});
		
		GridBagConstraints gbc_btnGetConnectedApplications = new GridBagConstraints();
		gbc_btnGetConnectedApplications.insets = new Insets(0, 0, 0, 5);
		gbc_btnGetConnectedApplications.fill = GridBagConstraints.BOTH;
		gbc_btnGetConnectedApplications.gridx = 1;
		gbc_btnGetConnectedApplications.gridy = 0;
		btnGetConnectedApplications.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2model.getConnectedApplications(connectedServerComboBox.getItemAt(connectedServerComboBox.getSelectedIndex()));
			}
		});
		serverJoinPanel.add(btnGetConnectedApplications, gbc_btnGetConnectedApplications);
		serverJoinPanel.add(btnGetApplicationGames, gbc_btnGetApplicationGames);
		
		GridBagConstraints gbc_gamesTabbedPane = new GridBagConstraints();
		gbc_gamesTabbedPane.insets = new Insets(0, 0, 0, 5);
		gbc_gamesTabbedPane.fill = GridBagConstraints.BOTH;
		gbc_gamesTabbedPane.gridx = 0;
		gbc_gamesTabbedPane.gridy = 1;
		contentPane.add(gamesTabbedPane, gbc_gamesTabbedPane);
		
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				view2model.quitApplication();
			}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}
			
		});
	}
	
	
	/**
	 * Starts the view
	 */
	public void start() {
		setVisible(true);
		String name = JOptionPane.showInputDialog("User name: ");
		String apiKey = JOptionPane.showInputDialog("API key: ");
		view2model.setName(name);
		view2model.setAPIKey(apiKey);
		view2model.startDiscoveryServer(name);
		clientsDiscoveryPanel.start();
	}
	

	/**
	 * Adds the given game view to the tabbed pane for holding games
	 * @param panel the game view
	 */
	public void createNewGameWindow(JPanel panel) {
		gamesTabbedPane.addTab("Game", panel);
	}
	
	/**
	 * Remove the game view
	 * @param panel JPanel to removed from the tabbed pane
	 */
	public void removeGameWindow(JPanel panel) {
		gamesTabbedPane.remove(panel);
	}

	/**
	 * Adds a given server to the list of servers
	 * @param server the server to add
	 */
	public void addConnectedApplication(ViewApplicationContainer server) {
		connectedServerSet.add(server);
		connectedServerComboBox.removeAllItems();
		for (ViewApplicationContainer game : connectedServerSet) {
			connectedServerComboBox.addItem(game);
		}
	}

	/**
	 * @param app App to remove from connected applications
	 */
	public void removeConnectedApplication(ViewApplicationContainer app) {
		connectedServerSet.remove(app);
		connectedServerComboBox.removeAllItems();
		for (ViewApplicationContainer game : connectedServerSet) {
			connectedServerComboBox.addItem(game);
		}
	}

	/**
	 * @param games Rooms to add to view
	 */
	public void addRooms(Collection<ViewRoomIdentityContainer> games) {
		for (ViewRoomIdentityContainer game : games) {
			serverGamesSet.add(game);
		}
		serverGamesComboBox.removeAllItems();
		for (ViewRoomIdentityContainer game : serverGamesSet) {
			serverGamesComboBox.addItem(game);
		}
	}

}
