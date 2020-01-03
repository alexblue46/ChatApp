package arb19_kpa1.game.msg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;
import java.util.function.Supplier;

import javax.swing.JComponent;

import arb19_kpa1.game.controller.GameController;
import arb19_kpa1.game.model.GameState;
import arb19_kpa1.game.view.GameView;
import common.packet.RoomDataPacket;
import common.receivers.IRoomMember;
import provided.datapacket.IDataPacketID;
import provided.mixedData.MixedDataKey;

/**
 * Command to start the game
 *
 */
public class StartGameCmd extends AGameCmd<StartGameMsg> {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -4873354782678133399L;

	/**
	 * Starts the game command
	 * @param key the main key
	 */
	@SuppressWarnings("rawtypes")
	public StartGameCmd(MixedDataKey<Map> key) {
		super(key);
	}

	@Override
	public Void apply(IDataPacketID index, RoomDataPacket<StartGameMsg> host, Void... params) {

		Map<Integer, Collection<IRoomMember>> teams = super.mddUtil.getTeams();

		IRoomMember stub = host.getData().getStub();
		super.mddUtil.setStub(stub);

		Map<IRoomMember, IRoomMember> turnOrder = host.getData().getTurnOrder();
		super.mddUtil.setTurnOrder(turnOrder);

		GameController controller = new GameController(host.getData().getInitialState(), cmd2model, mddUtil);
		super.mddUtil.setGameController(controller);

		GameView view = controller.getView();

		Collection<IRoomMember> myTeam = teams.get(super.mddUtil.getMyTeamNum());

		view.getSendTeamMsgButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = view.getTeamChatTextField().getText();
				myTeam.forEach(member -> cmd2model.sendMessageToMember(new TeamStringMsg(msg), member));
				view.getTeamChatTextField().setText("");
			}

		});

		view.getEndTurnBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GameState state = controller.getState();
				if (state.getCurrentPlayer().equals(stub)) {
					// Get the next player in the turn sequence
					IRoomMember nextPlayer = turnOrder.get(stub);
					// Keep iterating if the next player is not in either team anymore
					while (nextPlayer != state.getCurrentPlayer() && !teams.get(1).contains(nextPlayer)
							&& !teams.get(2).contains(nextPlayer)) {
						nextPlayer = turnOrder.get(nextPlayer);
					}
					state.setCurrentPlayer(nextPlayer);
					GameStateMsg msg = new GameStateMsg(state);
					teams.get(1).forEach(member -> cmd2model.sendMessageToMember(msg, member));
					teams.get(2).forEach(member -> cmd2model.sendMessageToMember(msg, member));
				}
			}

		});

		Vector<IRoomMember> team1Order = new Vector<IRoomMember>(turnOrder.size() / 2);
		Vector<IRoomMember> team2Order = new Vector<IRoomMember>(turnOrder.size() / 2);

		GameState state = controller.getState();
		team1Order.add(state.getCurrentPlayer());
		IRoomMember prev = state.getCurrentPlayer();
		while (!turnOrder.get(prev).equals(state.getCurrentPlayer())) {
			IRoomMember next = turnOrder.get(prev);
			if (teams.get(1).contains(next))
				team1Order.add(next);
			else
				team2Order.add(next);
			prev = next;
		}
		view.setTeamOrder(team1Order, 1);
		view.setTeamOrder(team2Order, 2);

		cmd2model.displayComponent(new Supplier<JComponent>() {

			@Override
			public JComponent get() {
				return mddUtil.getGameController().getView();
			}

		}, "War Is War");
		return null;
	}

}
