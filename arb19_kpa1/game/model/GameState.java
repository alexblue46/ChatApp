package arb19_kpa1.game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import arb19_kpa1.game.model.units.AGameUnit;
import arb19_kpa1.game.model.units.APlayerOwnedUnit;
import arb19_kpa1.game.model.units.City;
import arb19_kpa1.game.model.units.MineableLand;
import arb19_kpa1.game.model.utility.LatLng;
import common.receivers.IRoomMember;

/**
 * @author Owner
 * Game state to be sent between players
 */
public class GameState implements Serializable {
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -2583004699892340064L;

	/**
	 * Number of mines to be placed per player
	 */
	private static final int NUM_MINES_PER_PLAYER = 2;

	/**
	 * List of game units in the game
	 */
	private List<AGameUnit> gameUnits = new ArrayList<>();

	/**
	 * Player whose turn it currently is
	 */
	private IRoomMember currentPlayer;

	/**
	 * Adapterto allow game units to modify the game state
	 */
	private transient IGameUnit2GameAdpt adpt;

	/**
	 * Private constructor
	 */
	private GameState() {

	}

	/**
	 * @param teams Map of teams for which to create the initial state
	 * @return the initial game state
	 */
	public static GameState initialGameState(Map<Integer, Collection<IRoomMember>> teams) {
		Random rand = new Random();
		GameState state = new GameState();
		for (IRoomMember player : teams.get(1)) {
			state.addUnit(new City(new LatLng(getLat(rand), getTeam1Long(rand)), player, 0));
		}
		for (IRoomMember player : teams.get(2)) {
			state.addUnit(new City(new LatLng(getLat(rand), getTeam2Long(rand)), player, 0));
		}
		
		for (int i = 0; i < (teams.get(1).size() + teams.get(2).size()) * NUM_MINES_PER_PLAYER; i++) {
			state.addUnit(new MineableLand(new LatLng(getLat(rand), getLong(rand))));
		}
		
		
		return state;
	}
	
	/**
	 * @param adpt Adapter to give to all the game units
	 */
	public void setGameAdpt(IGameUnit2GameAdpt adpt) {
		this.adpt = adpt;
		for (AGameUnit unit : gameUnits) {
			System.out.println("Setting game adapter on: " + unit);
			unit.setUnit2GameAdpt(adpt);
		}
	}

	/**
	 * Sets the current player whose turn it is. This should be called with the next
	 * player at the end of the turn, before this GameState is sent to all players.
	 * 
	 * @param player the player whose turn it is
	 */
	public void setCurrentPlayer(IRoomMember player) {
		this.currentPlayer = player;
	}

	/**
	 * Gets the current player whose turn it is 
	 * @return the player
	 */
	public IRoomMember getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Gets all game units
	 * @return the units
	 */
	public List<AGameUnit> getGameUnits() {
		return gameUnits;
	}

	/**
	 * Adds a game unit
	 * @param unit the unit to be added
	 */
	public void addUnit(AGameUnit unit) {
		gameUnits.add(unit);
	}

	/**
	 * Removes a game unit
	 * @param unit the unit
	 */
	public void removeUnit(AGameUnit unit) {
		gameUnits.remove(unit);
	}

	/**
	 * Initializes all units who are owned by currentPlayer every turn
	 */
	public void startTurn() {
		for (int i = 0; i < gameUnits.size(); i++) {
			AGameUnit unit = gameUnits.get(i);
			if (unit instanceof APlayerOwnedUnit && isCurrentPlayerUnit((APlayerOwnedUnit) unit)) {
				((APlayerOwnedUnit) unit).turnBegins();
			}
		}
	}

	/**
	 * Gets the city of the current player
	 * @return the city
	 */
	public City getCurrentPlayerCity() {
		for (int i = 0; i < gameUnits.size(); i++) {
			AGameUnit unit = gameUnits.get(i);
			if (unit instanceof City) {
				City city = (City) unit;
				if (isCurrentPlayerUnit(city)) {
					return city;
				}
			}
		}
		return null;
	}

	/**
	 * @param unit Unit to check if owned by current player
	 * @return boolean indicating whether unit is owned by the player
	 */
	public boolean isCurrentPlayerUnit(APlayerOwnedUnit unit) {
		return unit.getOwner().equals(currentPlayer);
	}

	/**
	 * Gets the winning team
	 * 
	 * @return the number corresponding to the winning team or -1 if there is no
	 *         winning team
	 */
	public int getWinningTeam() {
		boolean allCitiesTeam1Dead = true;
		boolean allCitiesTeam2Dead = true;
		for (AGameUnit unit : gameUnits) {
			if (unit instanceof City) {
				City city = (City) unit;
				if (!city.isDead()) {
					if (adpt.getTeam(city.getOwner()) == 1) {
						allCitiesTeam1Dead = false;
					} else {
						allCitiesTeam2Dead = false;
					}
				}
			}
		}
		if (allCitiesTeam1Dead) {
			return 2;
		} else if (allCitiesTeam2Dead) {
			return 1;
		} else {
			// There is no winning team
			return -1;
		}
	}
	
	/**
	 * Utility fn for getting the longitude of team1
	 * @param rand random object
	 * @return the longitude of a city in team1
	 */
	public static double getTeam1Long(Random rand) {
		return -1*(102.7761 + rand.nextDouble()*14.385);
	}
	
	/**
	 * @param rand Random object
	 * @return random longitude value for a team 2 city
	 */
	public static double getTeam2Long(Random rand) {
		return -1*(74.0060 + rand.nextDouble()*14.385);
	}
	
	/**
	 * @param rand Random object
	 * @return random latitude in the game bounds
	 */
	public static double getLat(Random rand) {
		return 32.7157 + rand.nextDouble()*7.9971;
	}
	
	/**
	 * @param rand Random object
	 * @return random longitude in the game bounds
	 */
	public static double getLong(Random rand) {
		return -1*(74.0060 + rand.nextDouble()*(117.1611 - 74.0060));
	}

}
