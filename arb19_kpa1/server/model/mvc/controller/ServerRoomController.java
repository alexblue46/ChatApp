package arb19_kpa1.server.model.mvc.controller;

import java.util.Collection;
import java.util.Map;

import arb19_kpa1.server.model.IServer;
import arb19_kpa1.server.model.mvc.model.IServerRoomModel2ServerRoomViewAdpt;
import arb19_kpa1.server.model.mvc.model.ServerGameModel;
import arb19_kpa1.server.model.mvc.view.IServerRoomView2ServerRoomModelAdpt;
import arb19_kpa1.server.model.mvc.view.ServerRoomView;
import common.receivers.IRoomMember;

/**
 * @author alexbluestein Controller for server mini-mvc
 */
public class ServerRoomController {

	/**
	 * The server mini-mvc view
	 */
	private ServerRoomView view;

	/**
	 * the server mini-mvc model
	 */
	private ServerGameModel model;

	/**
	 * Creates a new Server mini-mvc
	 * 
	 * @param controller2model mvc controller to model adapter
	 * @param roomName         Name of room to make mvc for
	 * @param serverStub       Stub of server application
	 */
	public ServerRoomController(IServerController2ServerModelAdpt controller2model, String roomName,
			IServer serverStub) {

		this.model = new ServerGameModel(new IServerRoomModel2ServerRoomViewAdpt() {

			@Override
			public void updateRoomMembers(Collection<IRoomMember> newRoomMembers) {
				view.updateRoomMembers(newRoomMembers);

			}

			@Override
			public void updateTeams(Map<Integer, Collection<IRoomMember>> teams) {
				view.updateTeams(teams.get(1), teams.get(2));
			}

			@Override
			public void updateGameStatus(Boolean inProgress) {
				view.updateGameStatus(inProgress);
			}
		}, roomName, serverStub);

		ServerRoomController self = this;

		this.view = new ServerRoomView(new IServerRoomView2ServerRoomModelAdpt() {

			@Override
			public void startGame() {
				model.startGame();
			}

			@Override
			public void deleteRoom() {
				model.deleteRoom();
				controller2model.removeRoom(self);
			}
		});

		model.start();
	}

	/**
	 * @return get the mvc view
	 */
	public ServerRoomView getView() {
		return view;
	}

	/**
	 * @return get the mvc model
	 */
	public ServerGameModel getModel() {
		return model;
	}
}
