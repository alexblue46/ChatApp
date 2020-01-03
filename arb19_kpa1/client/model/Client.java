package arb19_kpa1.client.model;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import common.msg.IAppMsg;
import common.packet.AppDataPacket;

/**
 * Client for handling application level communications with the client
 *
 */
public class Client implements IClient {

	/**
	 * Received message queue to be processed
	 */
	private BlockingQueue<AppDataPacket<? extends IAppMsg>> msgQueue = new LinkedBlockingQueue<>();
	
	/**
	 * The player's name
	 */
	private String name;

	/**
	 * Creates a new client
	 * @param name the player's name
	 */
	public Client(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the client's name
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() throws RemoteException {
		return this.name;
	}

	@Override
	public void receiveMsg(AppDataPacket<? extends IAppMsg> msg) throws RemoteException {
		msgQueue.add(msg);
	}

	/**
	 * @return The message being processed or null if no message is being processed
	 */
	public AppDataPacket<? extends IAppMsg> getMsgToProcess() {
		try {
			return msgQueue.take();
		} catch (InterruptedException e) {
			// This is ok, this means we're shutting down
		}
		return null;
	}


}
