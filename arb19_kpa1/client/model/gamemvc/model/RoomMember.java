package arb19_kpa1.client.model.gamemvc.model;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import common.msg.IRoomMsg;
import common.packet.RoomDataPacket;
import common.receivers.IRoomMember;

/**
 * Unit for handling game communications
 *
 */
public class RoomMember implements IRoomMember {

	/**
	 * The name of the player
	 */
	private String name;
	
	/**
	 * Received message queue to be processed
	 */
	private BlockingQueue<RoomDataPacket<? extends IRoomMsg>> msgQueue = new LinkedBlockingQueue<>();

	/**
	 * Creates a new team member
	 * 
	 * @param name the player's name
	 */
	public RoomMember(String name) {
		this.name = name;
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public void receiveMsg(RoomDataPacket<? extends IRoomMsg> msg) throws RemoteException {
		msgQueue.add(msg);

	}

	/**
	 * @return Next message to process
	 */
	public RoomDataPacket<?> getMsgToProcess() {
		try {
			return msgQueue.take();
		} catch (InterruptedException e) {
			// This is ok, this means we're shutting down
		}
		return null;
	}


}
