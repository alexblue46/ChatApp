package arb19_kpa1.client.model.gamemvc.model;

import java.util.concurrent.locks.ReentrantLock;

import common.msg.status.network.IServerCheckStatusMsg;
import common.msg.status.network.IServerOkStatusMsg;

/**
 * @author alexbluestein
 * Monitors servers activity
 */
public class ServerHeartbeat {

	/**
	 * Time between server beats
	 */
	private static final int SERVER_HEARTBEAT_MS = 50000;
	/**
	 * Time of server timeout
	 */
	private static final int SERVER_HEARTBEAT_TIMEOUT_MS = 10000;
	/**
	 * Thread for server heart beat
	 */
	private Thread serverHeartbeatThread;
	/**
	 * Lock on heart beat
	 */
	private ReentrantLock lock = new ReentrantLock();
	/**
	 * Last sent message 
	 */
	private IServerCheckStatusMsg lastSentMsg;
	/**
	 * Boolean to indicate running
	 */
	private boolean isRunning = true;

	/**
	 * @param roomModel Client room-level model
	 */
	public ServerHeartbeat(RoomModel roomModel) {
		serverHeartbeatThread = new Thread(() -> {
			while (isRunning) {
				try {
					Thread.sleep(SERVER_HEARTBEAT_MS);
				} catch (InterruptedException e) {
					// Interrupt means we might be quitting
				}
				try {
					lock.lock();
					lastSentMsg = IServerCheckStatusMsg.make();
				} finally {
					lock.unlock();
				}
				roomModel.getNetworkUtil().sendMessageToAll(lastSentMsg);
				try {
					Thread.sleep(SERVER_HEARTBEAT_TIMEOUT_MS);
				} catch (InterruptedException e) {
					// Interrupt means we might be quitting
				}
				try {
					lock.lock();
					if (lastSentMsg != null) {
						System.out.println("Server failed heartbeat check :(");
						//roomModel.stop();
						lastSentMsg = null;
					} else {
						System.out.println("Server passed heartbeat check :)");
					}
				} finally {
					lock.unlock();
				}
			}
		});
	}

	/**
	 * Start heart beat thread
	 */
	public void start() {
		serverHeartbeatThread.start();
	}

	/**
	 * @param msg ServerOKStatusMsg
	 */
	public void acceptServerOk(IServerOkStatusMsg msg) {
		try {
			lock.lock();
			if (lastSentMsg != null && lastSentMsg.getUUID().equals(msg.getUUID())) {
				lastSentMsg = null;
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Stop server heart beat
	 */
	public void stop() {
		System.out.println("Stopping heartbeat...");
		isRunning = false;
		serverHeartbeatThread.interrupt();
		try {
			serverHeartbeatThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Stopped heartbeat");

	}
}
