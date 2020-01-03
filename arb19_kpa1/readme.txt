How the game works ---------------------------------------------------------------------
	War is War is a Civilization-esque game where each player begins the game with a city, 
	and the goal of the game is to destroy all of the cities on the opposing team. Each turn, 
	a player can build from a selection of units with different purposes, and move and take 
	actions with their existing units. Civilian units are used to gather resources. Military 
	units can be used to attack enemy civilians, military units, or cities. The purpose of 
	gathering resources is to speed up production in cities, which allows for a strategic 
	advantage.

Typical game ---------------------------------------------------------------------------
	At the beginning of the game, each player on each team gets its own city, spread out 
	across the map. Each player goes one at a time, alternating between teams. Players may 
	choose to focus on resource acquisition with civilians, or harassing enemy players with 
	military units. Once some players have acquired enough resources to expedite the 
	production of siege units, players may begin trying to attack enemy cities. Once a 
	player's city is destroyed, the player does not play for the rest of the game. Once all 
	of a team's cities have been destroyed, the game ends and that team loses.

Implementing the proposed API ----------------------------------------------------------
	In our current implementation of our API, we developed two MVCs - one for the server 
	and one for the client. Both put themselves on the discovery server on startup. The
	client can connect to the server, which allows the server to get the stub of the client
	and its associated ITeamMember. The client also creates an associated game tab that
	corresponds to the server. The server can then send a map through the ITeamMember that
	will be displayed on the client. ITeamMembers can also send text messages to all players in
	a game or to just their team, and they are displayed properly within the GameView in
	two seperete chat panels.


Basic classes --------------------------------------------------------------------------
	GameUnit:
		Represents an entity in the game with a position, serializable
	FarmableLand:
		Subclass of GameUnit, represents a possible location to build a farm
	PlayerOwnedUnit:
		Subclass of GameUnit with an owning player
	City:
		Subclass of PlayerOwnedUnit, represents the player's city with a current production and turns left in production  
	Farm:
		Subclass of PlayerOwnedUnit, represents a farm that gives passive resources to the owning player
	MovableUnit:
		subclass of PlayerOwnedUnit, represents a unit the player owns, boolean whether it has moved this turn
	Soldier:
		Subclass of MovableUnit, with capability of attacking units or cities
	Farmer:
		Subclass of MovableUnit, with capability of building farms
	GameState:
		A serializable collection of GameUnits along with the turn number, represents the state of the game at any time.

Messages --------------------------------------------------------------------------------
	GameStart:
		Message from the server to all ITeamMembers. It contains the starting GameState
		and the order of turns for each player.
	TurnBegin:
		Message from one ITeamMember to another when the first ends its turn, and it is 
		the second player's turn as dictated by the ordering of turns. When a turn begins,
		the player's city's production counter gets reduced, and when this becomes 0, the
		city creates the unit. 
	TurnOver:
		Message from the ITeamMember to all players when the turn is over. It contains the
		local GameState just in case any player does not have an updated GameState.
	GameWin:
		Message from ITeamMember to all teammates when the player destroys the last city 
		of the enemy team. 
	GameLose:
		Message from ITeamMember to all enemy players when the player destroys the last city 
		of the enemy team.
	UnitDestroyed:
		Message from ITeamMember to all other players when a unit is destroyed, with a 
		reference to the unit that has been destroyed.
	UnitCreated:
		Message from ITeamMember to all other players when a unit is created, with a reference 
		to the unit created.
	UnitMoved:
		Message from ITeamMember to all other players when a unit is moved, with a 
		reference to the unit moved.
	IStringMsg:
		A text message that can either be to all players in the game or to just the team
		of the sender.

Visual elements -------------------------------------------------------------------------
	The game needs a different visual element corresponding to each type of GameUnit. This
	visual indicator can change based on the unit type, whether it has moved, and whether 
	it is a possible target for attack. There is also a visual indicator for moving range
	for a given unit. There is also a visual indicator for possible actions for a unit, 
	represented as a list. There is also an end turn button.
 
Human interactions with the interface and effects ---------------------------------------
	Click on a unit:
		When a unit is clicked, possible actions for that unit pop up as a list.
	Action selected;
		When an action is selected, the possible actions list goes away. Depending on the
		action, there is different effect as follows.
		
		Set city production:
			When this is selected, a new pop-up of possible productions comes up along with
			their time. When one of these is selected, the city's production gets set to
			the selected unit.
		Move unit:
			A region representing the possible locations to move to (probably a circle) pops
			up, and the player is allowed to click on a location within this circle. When
			this is selected, the unit moves to this location, and this unit's boolean variable 
			representing the fact that it has moved is set to true. A UnitMoved message is 
			sent for this unit to all players.
		Attack other unit:
			All units that can be attacked by this unit are visually indicated to the player.
			When one of these is selected, the unit gets destroyed, and the attacking unit is
			moved to the location of the destroyed unit. The attacking unit is then
			set to be tired. A UnitDestroyed message is then sent to all players with the 
			destroyed unit, and a UnitMoved message is sent to all players with the attacking
			unit.
		Build farm:
			All FarmableLand units that are within range of this unit are visually indicated 
			to the player. When one of these is selected, the worker gets destroyed and a 
			UnitDestroyed message is sent to all players with a reference to the worker. The
			FarmableLand unit is also destroyed and the same happens as with the worker. A 
			Farm is then created with the same location as the FarmableLand and a UnitCreated
			message is sent to all players.
	End Turn:
		When the end turn button is clicked, several things happen. All MoveableUnits' boolean 
		variable representing whether it has moved is set to false. A TurnOver message is sent
		to all players with this player's GameState. A TurnBegin message is sent to the player
		whose turn is next.

What processing takes place on the client vs what processing takes place on the server -------
	The server first sends the commands for handling different messages to all players.
	The server then calculates the initial state of the game. This includes the turn order and the
	positions for Cities and FarmableLand. It then sends a GameStart message to all players with
	the initial GameState. It then sends a TurnBegin message to the player whose turn is first.
	After this, all processing happens between clients.

What sorts of command-to-command communications (on the same machine) are needed -------------
	Our commands just need to maintain a state object, which can be done on our proposed API 
	with the mixed-data dictionary. All of a player's turn can be done while processing the
	BeginTurn command, which will in turn send all of the other messages in a player's turn.
	
	
Changes from Milestone 1:
The terminology was changed from resource to gold. Instead of having farmers, our game now has miners 
which can mine gold at certain mineable locations. Also, our messages were changed to send the entire game 
state at the end of every turn.