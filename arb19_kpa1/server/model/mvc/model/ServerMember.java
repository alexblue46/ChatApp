package arb19_kpa1.server.model.mvc.model;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import common.msg.IRoomMsg;
import common.packet.RoomDataPacket;
import common.receivers.IRoomMember;

/**
 * A IRoomMember that represents the server
 *
 */
public class ServerMember implements IRoomMember {
	
	/**
	 * Received message queue to be processed
	 */
	private BlockingQueue<RoomDataPacket<? extends IRoomMsg>> msgQueue = new LinkedBlockingQueue<>();

	@Override
	public void receiveMsg(RoomDataPacket<? extends IRoomMsg> msg) throws RemoteException {
		msgQueue.add(msg);
	}

	@Override
	public String getName() throws RemoteException {
		return "SERVER";
	}
	
	/**
	 * @return The message being processed or null if no message is being processed
	 */
	public RoomDataPacket<? extends IRoomMsg> getMsgToProcess() {
		try {
			return msgQueue.take();
		} catch (InterruptedException e) {
			// This is ok, this means we're shutting down
		}
		return null;
	}

}
