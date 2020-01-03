package arb19_kpa1.game.controller;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import arb19_kpa1.game.model.GameState;
import arb19_kpa1.game.model.IGameUnit2GameAdpt;
import arb19_kpa1.game.model.units.AGameUnit;
import arb19_kpa1.game.model.units.APlayerOwnedUnit;
import arb19_kpa1.game.model.units.City;
import arb19_kpa1.game.model.utility.MDDUtility;
import arb19_kpa1.game.msg.GameOverMsg;
import arb19_kpa1.game.view.GameView;
import common.ICmd2ModelAdapter;
import common.receivers.IRoomMember;
import arb19_kpa1.game.model.utility.LatLng;
import arb19_kpa1.game.model.utility.LatLngBounds;

/**
 * The main GameController for game interactions
 *
 */
public class GameController {
	/**
	 * GameController logger
	 */
	private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
	/**
	 * The game's view
	 */
	private GameView view;
	/**
	 * The current game's state
	 */
	private GameState gameState;
	/**
	 * The boundaries of the game
	 */
	private static final LatLngBounds GAME_BOUNDS = new LatLngBounds(new LatLng(32.7157, -117.1611),
			new LatLng(40.7128, -74.0060));
	/**
	 * The cmd2model adapter
	 */
	private ICmd2ModelAdapter cmd2ModelAdpt;
	/**
	 * Utility for accessing MDD objects
	 */
	private MDDUtility mddUtil;

	/**
	 * Creates a new GameController
	 * 
	 * @param gameState     the starting game state
	 * @param cmd2ModelAdpt the cmd2model adapter
	 * @param mddUtil       the MDD utility object
	 */
	public GameController(GameState gameState, ICmd2ModelAdapter cmd2ModelAdpt, MDDUtility mddUtil) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
		this.mddUtil = mddUtil;

		view = new GameView(cmd2ModelAdpt.getAPIKey(), gameState, mddUtil, GAME_BOUNDS);
		setGameState(gameState);
	}

	/**
	 * Displays a message to the player's team
	 * 
	 * @param text the message
	 */
	public void displayTeamMsg(String text) {
		view.displayTeamMsg(text);
	}

	/**
	 * Called when this player's turn is started
	 */
	public void startTurn() {
		this.gameState.startTurn();
	}

	/**
	 * Returns the game view
	 * 
	 * @return the game view
	 */
	public GameView getView() {
		return view;
	}

	/**
	 * Gets the current GameState
	 * 
	 * @return the game state
	 */
	public GameState getState() {
		return gameState;
	}

	/**
	 * Updates the current game state. Does the main processing and setting up of
	 * the interactions of game objects.
	 * 
	 * @param gameState the new game state
	 */
	public void setGameState(GameState gameState) {
		LOGGER.info("setGameState called");
		this.gameState = gameState;
		view.update(gameState);
		try {
			view.setCurrentTurn(gameState.getCurrentPlayer().getName());
		} catch (RemoteException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		this.gameState.setGameAdpt(new IGameUnit2GameAdpt() {

			@Override
			public void createUnit(AGameUnit unit) {
				LOGGER.info("createUnit " + unit + " called");
				gameState.addUnit(unit);
				unit.setUnit2GameAdpt(this);
				updateView();
			}

			@Override
			public void checkUnitDeath(APlayerOwnedUnit unit) {
				LOGGER.entering(IGameUnit2GameAdpt.class.getName(), "updateUnit");
				// If dead, remove unit and notify unit
				if (unit.isDead()) {
					unit.unitKilled();
					removeUnit(unit);
					// Check if game is over
					if (unit instanceof City) {
						int winningTeam = gameState.getWinningTeam();
						System.out.println("City destroyed, winningTeam = " + winningTeam);
						if (winningTeam > 0) {
							cmd2ModelAdpt.sendMessageToAll(new GameOverMsg(winningTeam));
						}
					}
					updateView();
				}
			}

			@Override
			public void removeUnit(AGameUnit unit) {
				gameState.removeUnit(unit);
				updateView();
			}

			@Override
			public LatLngBounds getGameBounds() {
				return GAME_BOUNDS;
			}

			@Override
			public int getGoldAmt() {
				City currentPlayerCity = gameState.getCurrentPlayerCity();
				if (currentPlayerCity == null) {
					return 0;
				}
				return currentPlayerCity.getGoldAmt();
			}

			@Override
			public void setGoldAmt(int gold) {
				City currentPlayerCity = gameState.getCurrentPlayerCity();
				if (currentPlayerCity != null) {
					currentPlayerCity.setGoldAmt(gold);
				}
			}

			@Override
			public void displayNotification(String msg, LatLng notificationLoc) {
				view.displayNotification(msg, notificationLoc);
			}

			@Override
			public boolean isCurrentPlayerUnit(APlayerOwnedUnit unit) {
				return gameState.isCurrentPlayerUnit(unit);
			}

			@Override
			public int getTeam(IRoomMember player) {
				Map<Integer, Collection<IRoomMember>> teams = mddUtil.getTeams();
				if (teams.get(1).contains(player)) {
					return 1;
				} else if (teams.get(2).contains(player)) {
					return 2;
				} else {
					LOGGER.severe("GameController.getTeam() could not find player in teams");
					return -1;
				}
			}

			@Override
			public void updateView() {
				view.update(gameState);
			}
		});
		if (gameState.getCurrentPlayer().equals(mddUtil.getStub())) {
			view.setActionsEnabled(true);
			startTurn();
		} else {
			view.setActionsEnabled(false);
		}
	}

	/**
	 * Called when the game is ending
	 */
	public void endGame() {
		view.setEnabled(false);
	}
}
