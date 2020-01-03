package arb19_kpa1.game.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.teamdev.jxmaps.CircleOptions;
import com.teamdev.jxmaps.MapMouseEvent;
import com.teamdev.jxmaps.MouseEvent;

import arb19_kpa1.game.model.GameState;
import arb19_kpa1.game.model.units.*;
import arb19_kpa1.game.model.utility.LatLng;
import arb19_kpa1.game.model.utility.LatLngBounds;
import arb19_kpa1.game.model.utility.MDDUtility;
import arb19_kpa1.server.view.ViewRoomMemberContainer;
import common.receivers.IRoomMember;
import provided.jxMaps.ui.JxMapsPanel;
import provided.jxMaps.utils.IAction;
import provided.jxMaps.utils.IJxMaps_Defs.IEvent;
import provided.jxMaps.utils.enhanced.EnhancedCircle;

/**
 * @author alexbluestein
 * View for the game
 */
public class GameView extends JPanel {
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -2578015370572927749L;
	/**
	 * Tabbed pane to display chat and map
	 */
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	/**
	 * Panel for the map and map controls
	 */
	private final JPanel gamePanel = new JPanel();
	/**
	 * Panel for team chat
	 */
	private final JPanel teamChatPanel = new JPanel();
	/**
	 * Text area for team text messages
	 */
	private final JTextArea teamChatTextArea = new JTextArea();
	/**
	 * Text field to send team text messages
	 */
	private final JTextField teamChatTextField = new JTextField();
	/**
	 * Button to send team text message
	 */
	private final JButton sendTeamMsgButton = new JButton("Send Message");
	/**
	 * Panel to display team rosters
	 */
	private final JPanel teamRosterPanel = new JPanel();
	/**
	 * Display team 1 order
	 */
	private final JList<ViewRoomMemberContainer> team1Order = new JList<ViewRoomMemberContainer>();
	/**
	 * Display team 2 order
	 */
	private final JList<ViewRoomMemberContainer> team2Order = new JList<ViewRoomMemberContainer>();
	/**
	 * Control panel for the map
	 */
	private final JPanel cntrlPanel = new JPanel();
	/**
	 * List of attributes for selected game unit
	 */
	private final JList<String> attributesList = new JList<String>();
	/**
	 * Panel for game unit actions
	 */
	private final JPanel actionsPanel = new JPanel();
	/**
	 * List of actions for selected game unit
	 */
	private final JComboBox<UnitAction> actionSelectList = new JComboBox<UnitAction>();
	/**
	 * Button to select action
	 */
	private final JButton selectActionBtn = new JButton("Select Action");
	/**
	 * Panel to control and display turn information
	 */
	private final JPanel turnPanel = new JPanel();
	/**
	 * Label to display who's turn it is
	 */
	private final JLabel currentTurnLbl = new JLabel("Current Turn:");
	/**
	 * Button to end the turn
	 */
	private final JButton endTurnBtn = new JButton("End Turn");
	/**
	 * Object that has the view state
	 */
	private final GameMapView map;
	/**
	 * The actual MapView object
	 */
	private final JxMapsPanel jxMapView;
	/**
	 * State that the view is displaying
	 */
	private GameState state;
	/**
	 * Utility to get items from player's database
	 */
	private MDDUtility mddUtil;
	/**
	 * Currently selected unit
	 */
	private AGameUnit selectedUnit;
	/**
	 * Event to get moving location
	 */
	private MapMouseEvent eventForMoving;
	/**
	 * Circle to display movement radius
	 */
	private EnhancedCircle circleForMoving;

	/**
	 * Create the panel.
	 * @param apiKey Google API key
	 * @param initialState The initial game state
	 * @param mddUtil Utility to get items from player's database
	 * @param bounds Game bounds
	 */
	public GameView(String apiKey, GameState initialState, MDDUtility mddUtil, LatLngBounds bounds) {
		this.state = initialState;
		this.map = new GameMapView(new IMapView2MapAdpt() {

			@Override
			public void putUnitInCntrlPanel(AGameUnit unit) {
				actionSelectList.removeAllItems();
				selectedUnit = unit;
				updateAttributes(unit);
				if (unit instanceof APlayerOwnedUnit && state.isCurrentPlayerUnit((APlayerOwnedUnit)unit)) {
					if (unit instanceof City || (! (unit instanceof AMovableUnit && ((AMovableUnit)unit).getAlreadyMoved()))) {
						updateActions((APlayerOwnedUnit)unit);
					}
				}
			}
			
		}, apiKey, initialState, bounds);
		this.jxMapView = map.getMapView();
		this.jxMapView.start();
		this.mddUtil = mddUtil;
		initGUI();
	}
	/**
	 * Initialize the GUI
	 */
	private void initGUI() {
		setLayout(new BorderLayout(0, 0));

		add(tabbedPane);

		tabbedPane.addTab("Team Chat", null, teamChatPanel, null);
		GridBagLayout gbl_teamChatPanel = new GridBagLayout();
		gbl_teamChatPanel.columnWidths = new int[] { 50, 130, 0 };
		gbl_teamChatPanel.rowHeights = new int[] { 50, 0, 0 };
		gbl_teamChatPanel.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_teamChatPanel.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		teamChatPanel.setLayout(gbl_teamChatPanel);

		GridBagConstraints gbc_teamChatTextArea = new GridBagConstraints();
		gbc_teamChatTextArea.fill = GridBagConstraints.BOTH;
		gbc_teamChatTextArea.insets = new Insets(0, 0, 5, 5);
		gbc_teamChatTextArea.gridx = 0;
		gbc_teamChatTextArea.gridy = 0;
		teamChatPanel.add(teamChatTextArea, gbc_teamChatTextArea);

		GridBagConstraints gbc_teamRosterPanel = new GridBagConstraints();
		gbc_teamRosterPanel.insets = new Insets(0, 0, 5, 0);
		gbc_teamRosterPanel.fill = GridBagConstraints.BOTH;
		gbc_teamRosterPanel.gridx = 1;
		gbc_teamRosterPanel.gridy = 0;
		teamRosterPanel
				.setBorder(new TitledBorder(null, "Turn Order", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		teamChatPanel.add(teamRosterPanel, gbc_teamRosterPanel);
		teamRosterPanel.setLayout(new GridLayout(1, 0, 0, 0));
		team1Order.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Team 1",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));

		teamRosterPanel.add(team1Order);
		team2Order.setBorder(new TitledBorder(null, "Team 2", TitledBorder.CENTER, TitledBorder.TOP, null, null));

		teamRosterPanel.add(team2Order);

		GridBagConstraints gbc_teamChatTextField = new GridBagConstraints();
		gbc_teamChatTextField.fill = GridBagConstraints.BOTH;
		gbc_teamChatTextField.insets = new Insets(0, 0, 0, 5);
		gbc_teamChatTextField.gridx = 0;
		gbc_teamChatTextField.gridy = 1;
		teamChatTextField.setColumns(10);
		teamChatPanel.add(teamChatTextField, gbc_teamChatTextField);

		GridBagConstraints gbc_sendTeamMsgButton = new GridBagConstraints();
		gbc_sendTeamMsgButton.fill = GridBagConstraints.BOTH;
		gbc_sendTeamMsgButton.gridx = 1;
		gbc_sendTeamMsgButton.gridy = 1;
		teamChatPanel.add(sendTeamMsgButton, gbc_sendTeamMsgButton);

		tabbedPane.addTab("Game", null, gamePanel, null);
		GridBagLayout gbl_gamePanel = new GridBagLayout();
		gbl_gamePanel.columnWidths = new int[] {400};
		gbl_gamePanel.rowHeights = new int[] {20, 200, 150};
		gbl_gamePanel.columnWeights = new double[] { 1.0 };
		gbl_gamePanel.rowWeights = new double[] { 1.0, 1.0, 1.0 };
		gamePanel.setLayout(gbl_gamePanel);

		GridBagConstraints gbc_turnPanel = new GridBagConstraints();
		gbc_turnPanel.ipady = -250;
		gbc_turnPanel.insets = new Insets(0, 0, 5, 0);
		gbc_turnPanel.fill = GridBagConstraints.BOTH;
		gbc_turnPanel.gridx = 0;
		gbc_turnPanel.gridy = 0;
		gamePanel.add(turnPanel, gbc_turnPanel);
		GridBagLayout gbl_turnPanel = new GridBagLayout();
		gbl_turnPanel.columnWidths = new int[] { 111, 84, 117, 0 };
		gbl_turnPanel.rowHeights = new int[] { 29, 0 };
		gbl_turnPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_turnPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		turnPanel.setLayout(gbl_turnPanel);

		GridBagConstraints gbc_currentTurnLbl = new GridBagConstraints();
		gbc_currentTurnLbl.weighty = 1.0;
		gbc_currentTurnLbl.gridwidth = 2;
		gbc_currentTurnLbl.insets = new Insets(0, 0, 0, 5);
		gbc_currentTurnLbl.gridx = 0;
		gbc_currentTurnLbl.gridy = 0;
		turnPanel.add(currentTurnLbl, gbc_currentTurnLbl);

		GridBagConstraints gbc_endTurnBtn = new GridBagConstraints();
		gbc_endTurnBtn.fill = GridBagConstraints.BOTH;
		gbc_endTurnBtn.gridwidth = 2;
		gbc_endTurnBtn.gridx = 2;
		gbc_endTurnBtn.gridy = 0;
		turnPanel.add(endTurnBtn, gbc_endTurnBtn);
		
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		gamePanel.add(jxMapView, gbc_panel);

		GridBagConstraints gbc_cntrlPanel = new GridBagConstraints();
		gbc_cntrlPanel.ipady = -100;
		gbc_cntrlPanel.fill = GridBagConstraints.BOTH;
		gbc_cntrlPanel.gridx = 0;
		gbc_cntrlPanel.gridy = 2;
		gamePanel.add(cntrlPanel, gbc_cntrlPanel);
		GridBagLayout gbl_cntrlPanel = new GridBagLayout();

		gbl_cntrlPanel.columnWidths = new int[] {100, 100};
		gbl_cntrlPanel.rowHeights = new int[] {200};
		gbl_cntrlPanel.columnWeights = new double[]{1.0, 1.0};
		gbl_cntrlPanel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};

		gbl_cntrlPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_cntrlPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_cntrlPanel.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_cntrlPanel.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };

		cntrlPanel.setLayout(gbl_cntrlPanel);

		GridBagConstraints gbc_attributesList = new GridBagConstraints();
		gbc_attributesList.ipady = -10;
		gbc_attributesList.ipadx = 50;
		gbc_attributesList.gridheight = 2;
		gbc_attributesList.weighty = 1.0;
		gbc_attributesList.fill = GridBagConstraints.BOTH;
		gbc_attributesList.insets = new Insets(0, 0, 5, 5);
		gbc_attributesList.gridx = 0;
		gbc_attributesList.gridy = 0;
		attributesList.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Unit Attributes",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		cntrlPanel.add(attributesList, gbc_attributesList);

		GridBagConstraints gbc_actionsPanel = new GridBagConstraints();
		gbc_actionsPanel.fill = GridBagConstraints.BOTH;
		gbc_actionsPanel.ipady = -10;
		gbc_actionsPanel.ipadx = -50;
		gbc_actionsPanel.gridheight = 2;
		gbc_actionsPanel.insets = new Insets(0, 0, 5, 0);
		gbc_actionsPanel.gridx = 1;
		gbc_actionsPanel.gridy = 0;
		actionsPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Unit Actions",
				TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		cntrlPanel.add(actionsPanel, gbc_actionsPanel);
		actionsPanel.setLayout(new BorderLayout(0, 0));
		actionSelectList.setVisible(true);
		actionSelectList.setLightWeightPopupEnabled(false);
		actionsPanel.add(actionSelectList, BorderLayout.CENTER);
		actionsPanel.add(selectActionBtn, BorderLayout.SOUTH);
		
		actionSelectList.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) 
					((UnitAction)e.getItem()).getActionWhenUnclicked().run();
				
				
				if (e.getStateChange() == ItemEvent.SELECTED) 
					((UnitAction)e.getItem()).getActionWhenClicked().run();
			}
			
		});
		
		selectActionBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (actionSelectList.getSelectedItem() != null) {
					((UnitAction)actionSelectList.getSelectedItem()).getActionWhenSelected().run();
				}
			}
			
		});
		
		teamChatTextArea.setText(GAME_INSTRUCTIONS);
	}

	/**
	 * Displays a string in the main game chat panel
	 * 
	 * @param text String of the message sent
	 */
	public void displayTeamMsg(String text) {
		teamChatTextArea.append(text);
	}

	/**
	 * @return the team chat text area
	 */
	public JTextArea getTeamChatTextArea() {
		return teamChatTextArea;
	}

	/**
	 * @return the team chat text field
	 */
	public JTextField getTeamChatTextField() {
		return teamChatTextField;
	}

	/**
	 * @return the button that send text message to team
	 */
	public JButton getSendTeamMsgButton() {
		return sendTeamMsgButton;
	}

	/**
	 * @return the button that ends the turn
	 */
	public JButton getEndTurnBtn() {
		return endTurnBtn;
	}

	/**
	 * @param playerName sets the label for whose turn it is
	 */
	public void setCurrentTurn(String playerName) {
		currentTurnLbl.setText("Current Turn: " + playerName);
	}

	/**
	 * @param order Order of turns for a team
	 * @param team The team number
	 */
	public void setTeamOrder(Vector<IRoomMember> order, int team) {
		Vector<ViewRoomMemberContainer> viewOrder = new Vector<ViewRoomMemberContainer>(order.size());
		for (IRoomMember member : order) {
			viewOrder.add(new ViewRoomMemberContainer(member));
		}
		if (team == 1)
			team1Order.setListData(viewOrder);
		else
			team2Order.setListData(viewOrder);
	}

	/**
	 * This is called to either allow or disallow state-changing actions from being
	 * taken on the view. This includes clicking selectActionBtn and endTurnBtn.
	 * 
	 * @param enabled whether actions are enabled or not
	 */
	public void setActionsEnabled(boolean enabled) {
		endTurnBtn.setEnabled(enabled);
		selectActionBtn.setEnabled(enabled);
	}
	
	/**
	 * @param state Game state to update the view with
	 */
	public void update(GameState state) {
		map.update(state);
		this.state = state;
		actionSelectList.removeAllItems();
		if (selectedUnit != null && state.getGameUnits().contains(selectedUnit)) {
			updateAttributes(selectedUnit);
			if (selectedUnit instanceof APlayerOwnedUnit && state.isCurrentPlayerUnit((APlayerOwnedUnit)selectedUnit)) {
				if (selectedUnit instanceof City || ! ((AMovableUnit)selectedUnit).getAlreadyMoved()) {
					updateActions((APlayerOwnedUnit)selectedUnit);
				} 
			}
		}
		else {
			selectedUnit = null;
			attributesList.setListData(new Vector<String>());
		}
	}
	
	/**
	 * @param msg Message to display on the map
	 * @param notificationLoc Location to display the message
	 */
	public void displayNotification(String msg, LatLng notificationLoc) {
		map.displayNotification(msg, notificationLoc);
	}
	
	/**
	 * @param unit Game unit the update the attributes for
	 */
	public void updateAttributes(AGameUnit unit) {
		ArrayList<String> attributeStrList = new ArrayList<String>();
		
		if (unit instanceof MineableLand) {
			attributeStrList.add("Mineable Land");
		} else {
			APlayerOwnedUnit playerOwnedUnit = (APlayerOwnedUnit) unit;
			try {
				attributeStrList.add("Owner: " + playerOwnedUnit.getOwner().getName());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			attributeStrList.add("Health: " + playerOwnedUnit.getHealth());
			attributeStrList.add("Defense: " + playerOwnedUnit.getDefense());
			attributeStrList.add("Movable Unit Attack: " + playerOwnedUnit.getMovableUnitAttack());
			attributeStrList.add("Nonmovable Unit Attack: " + playerOwnedUnit.getNonmovableUnitAttack());
			if (unit instanceof City) {
				City city = (City) unit;
				attributeStrList.add(0, "City");
				attributeStrList.add(2, "Gold Amount: " + city.getGoldAmt());
				attributeStrList.add(2, "Turns Left in Production: " + city.getProductionNumTurnsLeft());
				attributeStrList.add(2, "Current Production: " + city.getCurrentProduction());
			} else if (unit instanceof Mine) {
				Mine mine = (Mine) unit;
				attributeStrList.add(0, "Mine");
				attributeStrList.add(2, "Gold per Turn: " + mine.getGoldPerTurn());
			} else if (unit instanceof Miner) {
				attributeStrList.add(0, "Miner");
			} else if (unit instanceof Soldier) {
				attributeStrList.add(0, "Soldier");
			} else {
				attributeStrList.add(0, "Siege Unit");
			}
		}
		
		attributesList.setListData(new Vector<String>(attributeStrList));
	}
	
	/**
	 * @param unit Game unit to update the actions for
	 */
	public void updateActions(APlayerOwnedUnit unit) {
		if (unit instanceof AMovableUnit) {
			generateAttackActions((AMovableUnit)unit);
			generateMoveAction((AMovableUnit)unit);
			if (unit instanceof Soldier)
				generateMergeActions((Soldier)unit);
			if (unit instanceof Miner)
				generateBuildMineActions((Miner)unit);
				
		} else if(unit instanceof City) {
			City city = (City) unit;

			
			actionSelectList.addItem(new UnitAction("Set city production to miner", ()->{},()->{},()->city.setProduction(Miner.class)));
			actionSelectList.addItem(new UnitAction("Set city production to soldier", ()->{},()->{},()->city.setProduction(Soldier.class)));
			actionSelectList.addItem(new UnitAction("Set city production to siege unit", ()->{},()->{},()->city.setProduction(SiegeUnit.class)));

			if(city.getCurrentProduction() != null && city.getGoldAmt() >= city.getCurrentProduction().getExpediteGoldCostPerTurn() * city.getProductionNumTurnsLeft()) {
				final int goldNeeded = city.getCurrentProduction().getExpediteGoldCostPerTurn() * city.getProductionNumTurnsLeft();
				actionSelectList.addItem(new UnitAction("Expedite production for " + goldNeeded + " gold", ()->{},()->{}, ()->city.expediteProduction()));
			}
			generateAttackActions(city);
			
		}
	}
	
	/**
	 * @param unit Game Unit to make the attack actions
	 */
	public void generateAttackActions(AMovableUnit unit) {
		
		int num = 1;
		
		for(AGameUnit otherUnit : state.getGameUnits()) {
			if (otherUnit instanceof APlayerOwnedUnit && ! onMyTeam((APlayerOwnedUnit)otherUnit)) {
				APlayerOwnedUnit playerOwnedUnit = (APlayerOwnedUnit) otherUnit;
				if (unit.withinRadius(playerOwnedUnit)) {
					final int myNum = num;
					actionSelectList.addItem(new UnitAction("Attack Option " + myNum, 
							()-> map.setMarkerLabel(otherUnit, "Attack Option " + myNum),
							()->map.setMarkerLabel(otherUnit, null),
							()->{
								if (playerOwnedUnit instanceof AMovableUnit)
									unit.attack((AMovableUnit)playerOwnedUnit);
								else
									unit.attack((ANonMovableUnit)playerOwnedUnit);
								}));
					num++;
				}
			}
		}
	}
	
	/**
	 * @param unit City to generate the attack actions
	 */
	public void generateAttackActions(City unit) {
			
		int num = 1;
		
		for(AGameUnit otherUnit : state.getGameUnits()) {
			if (otherUnit instanceof AMovableUnit && ! onMyTeam((AMovableUnit)otherUnit)) {
				AMovableUnit movableUnit = (AMovableUnit) otherUnit;
				if (unit.withinRadius(movableUnit)) {
					final int myNum = num;
					actionSelectList.addItem(new UnitAction("Attack Option " + myNum, 
							()-> map.setMarkerLabel(otherUnit, "Attack Option " + myNum),
							()->map.setMarkerLabel(otherUnit, null),
							()->{
								unit.attack(movableUnit);
								}));
					num++;
				}
			}
		}
	}
	
	
	
	/**
	 * @param unit Game unit to make the move action for
	 */
	public void generateMoveAction(AMovableUnit unit) {
		actionSelectList.addItem(new UnitAction("Move (click location to move to)", 
				()->{},()->{
					if (circleForMoving != null)
						circleForMoving.setVisible(false);
				},()->{
					CircleOptions circleOpts = new CircleOptions();
					circleOpts.setCenter(unit.getPosition().getJxMapsEquiv());
					circleOpts.setRadius(unit.getMovementRadius()*1609.344);
					circleOpts.setVisible(true);
					circleOpts.setClickable(false);
					
					circleForMoving = map.getCompFactory().makeCircle(circleOpts);
					
					map.removeMouseEvents();
					eventForMoving = map.getNavigator().addMapMouseEvent(IEvent.CLICK, new IAction<MouseEvent>() {
						@Override
						public void accept(MouseEvent t) {
							LatLng locToMove = new LatLng(t.latLng().getLat(), t.latLng().getLng());
							if (unit.isMovePossible(locToMove)) {
								System.out.println("VALID MOVE LOCATION");
								unit.move(locToMove);
							} else {
								map.displayNotification("Cannot move to selected location", locToMove);
							}
							circleForMoving.setVisible(false);
							map.getNavigator().removeMapMouseEvent(eventForMoving);
							map.setMouseEvents();
						}
					});
				}));
	}
	
	/**
	 * @param unit Game unit to generate the merge action for
	 */
	public void generateMergeActions(Soldier unit) {
		int num = 1;
		
		for(AGameUnit otherUnit : state.getGameUnits()) {
			if (otherUnit instanceof Soldier && ((Soldier) otherUnit).getOwner().equals(mddUtil.getStub()) && !((Soldier) otherUnit).getAlreadyMoved() &&  ! otherUnit.equals(unit)) {
				Soldier soldier = (Soldier) otherUnit;
				if (unit.withinRadius(soldier)) {
					final int myNum = num;
					actionSelectList.addItem(new UnitAction("Merge Option " + myNum, 
							()-> map.setMarkerLabel(otherUnit, "Merge Option " + myNum),
							()->map.setMarkerLabel(otherUnit, null),
							()->unit.merge(soldier)));
					num++;
				}
			}
		}
	}
	
	/**
	 * @param unit Game unit to make the build mine action for
	 */
	public void generateBuildMineActions(Miner unit) {
		int num = 1;
		
		for(AGameUnit otherUnit : state.getGameUnits()) {
			if (otherUnit instanceof MineableLand) {
				MineableLand land = (MineableLand) otherUnit;
				if (unit.withinRadius(land)) {
					final int myNum = num;
					actionSelectList.addItem(new UnitAction("Build Mine Option " + myNum, 
							()-> map.setMarkerLabel(otherUnit, "Build Mine Option " + myNum),
							()->map.setMarkerLabel(otherUnit, null),
							()->unit.buildMine(land)));
					num++;
				}
			}
		}
	}
	
	/**
	 * @param unit Game Unit to check
	 * @return boolean indicating whether the game unit is on the player's team
	 */
	public boolean onMyTeam(APlayerOwnedUnit unit) {
		return mddUtil.getTeams().get(mddUtil.getMyTeamNum()).contains(unit.getOwner());
	}
	
	/**
	 * String that gives the game instruction
	 */
	private static final String GAME_INSTRUCTIONS = "You and your comrades are on a quest to take over the United States.\nYou each have a city that to protect.\nIf your team destroys your cities before they destroy yours, you win!!\n\nThere are multiple kinds of units you may build.\nYour city can create these units by setting the production of your city to a specific unit.\nYou can create multiple types of units:\n\nSoldiers\nMiners\nSiege Units\n\nSoldiers are good at attacking enemy units but are not good at attacking cities.\nSoldier units can be merged to create powerful armies.\nMiners can build mines at mineable locations which give you gold every round,\nbut they are unable to defend themselves from soldiers and siege units.\nSiege units are slow and expensive but are excellent at attacking cities.\n\nEach unit takes a certain number of turns to produce, but you can expedite this production and get a unit instantly by using your gold.\n\nEach turn you can assign each of your units to an action (move to a certain point on the map, attack an enemy, and so forth).\nYou can also set your city to attack nearby units, set its production to a certain unit, or expedite its current production.\n\nWhen it is your turn click through the units and select actions for the units that are yours.\nOnce you have assigned actions to all your units, click the \"End Turn\" button to conclude your turn.\n\n\n\n";

}
