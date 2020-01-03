package common.receivers;

import java.rmi.Remote;
import java.rmi.RemoteException;

import provided.datapacket.ADataPacket;

/**
 * General interface for all message receivers.
 * @param <TPacket> A data packet typing
 */
public interface IMsgReceiver<TPacket extends ADataPacket> extends Remote {
	
	/**
	 * The name the ICompute object will be bound to in the RMI Registry
	 */
	public static final String BOUND_NAME = "User";
	
	 /**
      * Receive a message sent to the IApplication and adds it to processing queue
      * @param msg - data packet containing the IMsg being received
      * @throws RemoteException if remote communication failed             
      */
    //public void receiveMsg(DataPacket<? super TMsg, IMsgReceiver<TMsg>> msg) throws RemoteException;
	public void receiveMsg(TPacket msg) throws RemoteException;
     
     /** Get name of remote entity
     * @return name - name of remote entity
     * @throws RemoteException if remote communication failed 
     */
    public String getName() throws RemoteException;

}
