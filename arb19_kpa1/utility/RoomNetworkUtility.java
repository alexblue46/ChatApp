package arb19_kpa1.utility;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.msg.IRoomMsg;
import common.msg.status.network.IRemoteExceptionMsg;
import common.packet.RoomDataPacket;
import common.receivers.IRoomMember;
import common.virtualNetwork.IRoom;

/**
 * Network Utility class for sending room messages
 * 
 * @author Owner
 *
 */
public class RoomNetworkUtility {
	/**
	 * Logger
	 */
	private static final Logger LOGGER = Logger.getLogger(RoomNetworkUtility.class.getName());
	/**
	 * Delay before sending IRemoteExceptionMsg accorindg to API
	 */
	private static final long REMOTE_EXCEPTION_DELAY_MS = 1000;
	/**
	 * The stub of the message sender
	 */
	private IRoomMember senderStub;
	/**
	 * The IRoom to send messages in
	 */
	private IRoom room;

	/**
	 * Creates a new instance of the utility class
	 * 
	 * @param senderStub the stub of the sender
	 * @param room       the room
	 */
	public RoomNetworkUtility(IRoomMember senderStub, IRoom room) {
		this.senderStub = senderStub;
		this.room = room;
	}

	/**
	 * Asynchronously send a message to a receiver
	 * @param receiver msgReceiver
	 * @param msg the message
	 */
	public void sendMessageAsync(IRoomMember receiver, IRoomMsg msg) {
		new Thread(() -> {
			try {
				receiver.receiveMsg(new RoomDataPacket<>(msg, senderStub));
			} catch (RemoteException e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
				LOGGER.warning("Contacting receiver failed.");
				sendRemoteExceptionMsg(receiver);
			}
		}).start();
	}

	/**
	 * Sends an IRemoteException for a given bad stub
	 * @param badStub the bad stub
	 */
	private void sendRemoteExceptionMsg(IRoomMember badStub) {
		new Thread(() -> {
			try {
				Thread.sleep(REMOTE_EXCEPTION_DELAY_MS);
			} catch (InterruptedException e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
			for (IRoomMember member : room.getMembers()) {
				try {
					member.receiveMsg(
							new RoomDataPacket<IRemoteExceptionMsg>(IRemoteExceptionMsg.make(badStub), senderStub));
				} catch (RemoteException e) {
					LOGGER.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}).start();
	}

	/**
	 * Sends a message to the whole room
	 * @param msg the message
	 */
	public void sendMessageToAll(IRoomMsg msg) {
		for (IRoomMember other : room.getMembers()) {
			sendMessageAsync(other, msg);
		}
	}
	/**
	 * Sends a message synchronously to the receiver
	 * @param receiver the message receiver
	 * @param msg the message
	 * @return whether it was successful
	 */
	public boolean sendMessageSync(IRoomMember receiver, IRoomMsg msg) {
		LOGGER.info("sendMessageSync: " + msg);
		try {
			receiver.receiveMsg(new RoomDataPacket<IRoomMsg>(msg, senderStub));
		} catch (RemoteException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			sendRemoteExceptionMsg(receiver);
			return false;
		}
		return true;
	}

	/**
	 * @param receiver Stub to receive message
	 * @param msg Message to send
	 * @return boolean indicating success
	 */
	public boolean sendMessageSyncNoRemoteException(IRoomMember receiver, IRoomMsg msg) {
		try {
			receiver.receiveMsg(new RoomDataPacket<IRoomMsg>(msg, senderStub));
		} catch (RemoteException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
		return true;
	}
}
