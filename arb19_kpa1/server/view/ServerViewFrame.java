package arb19_kpa1.server.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import arb19_kpa1.server.model.mvc.view.ServerRoomView;
import common.receivers.IApplication;

/**
 * @author alexbluestein
 */
public class ServerViewFrame extends JFrame {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 8992316744410678352L;

	/**
	 * Adapter from the server view to the server model
	 */
	private IServerView2ModelAdpt view2model;

	/**
	 * The main content pane
	 */
	private JPanel contentPane;

	/**
	 * The text area for server information
	 */
	private final JTextArea chatTextArea = new JTextArea();
	/**
	 * The tabbed pane containing games
	 */
	private final JTabbedPane gameTabs = new JTabbedPane(JTabbedPane.TOP);
	/**
	 * Panel containing connected applications
	 */
	private final JPanel connectedAppsPanel = new JPanel();
	/**
	 * COmboBox containing connected applications
	 */
	private final JComboBox<ViewAppContainer> connectedApps = new JComboBox<ViewAppContainer>();
	/**
	 * panel containing create room controls
	 */
	private final JPanel createRoomPanel = new JPanel();
	/**
	 * Text field for created room name
	 */
	private final JTextField createRoomTextField = new JTextField();
	/**
	 * Button for creating room
	 */
	private final JButton createRoomBtn = new JButton("Create Room");

	/**
	 * Initializes all GUI components
	 */
	private void initGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				view2model.quitApplication();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

		});

		contentPane.add(gameTabs, BorderLayout.CENTER);

		gameTabs.add("Chat", chatTextArea);
		connectedAppsPanel.setBorder(
				new TitledBorder(null, "Connected Applications", TitledBorder.CENTER, TitledBorder.TOP, null, null));

		contentPane.add(connectedAppsPanel, BorderLayout.NORTH);

		connectedAppsPanel.add(connectedApps);

		contentPane.add(createRoomPanel, BorderLayout.SOUTH);
		createRoomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		createRoomPanel.add(createRoomTextField);

		createRoomPanel.add(createRoomBtn);

		createRoomBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				view2model.createRoom(createRoomTextField.getText());
				createRoomTextField.setText("");
			}

		});
	}

	/**
	 * Start the server view
	 */
	public void start() {
		setVisible(true);
		view2model.setServerName(JOptionPane.showInputDialog("Server name:"));
		view2model.setCategory(JOptionPane.showInputDialog("Category:"));
	}

	/**
	 * @param text String to add to chat
	 */
	public void displayText(String text) {
		chatTextArea.append(text);
		chatTextArea.append("\n");
	}

	/**
	 * Adds a given game view to the games
	 * 
	 * @param name the game name
	 * @param view the view object
	 */
	public void addGame(String name, ServerRoomView view) {
		gameTabs.add(name, view);
	}

	/**
	 * Removes a given game view
	 * 
	 * @param view the game
	 */
	public void removeGame(ServerRoomView view) {
		gameTabs.remove(view);
	}

	/**
	 * Update connected applications
	 * 
	 * @param apps the new connected applications
	 */
	public void updateConnectedApps(Collection<IApplication> apps) {
		connectedApps.removeAllItems();
		apps.forEach(app -> connectedApps.addItem(new ViewAppContainer(app)));
	}

	/**
	 * Create the frame.
	 * 
	 * @param view2model Adapter from the server view to the server model
	 */
	public ServerViewFrame(IServerView2ModelAdpt view2model) {
		createRoomTextField.setColumns(10);
		this.view2model = view2model;
		initGUI();
	}

}
