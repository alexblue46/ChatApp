package common.cmd;

import common.ICmd2ModelAdapter;
import common.msg.IRoomMsg;
import common.msg.IMsg;
import common.packet.RoomDataPacket;
import provided.datapacket.ADataPacketAlgoCmd;


/**
 * A type-narrowed definition of the ADataPacketAlgoCmd. <br>
 * 
 * This type of command processes data packets containing
 * IMsg's and returns an IStatusMsg indicating the result of the processing. It communicates with the local
 * model using our ICmd2ModelAdapter.
 * @param <M> message to be processed
 */
public abstract class ARoomMsgCmd<M extends IMsg>
		extends ADataPacketAlgoCmd <Void, IRoomMsg, Void, ICmd2ModelAdapter, RoomDataPacket<M>> {
	
	/**
	 * Version number for serialization.
	 */
	private static final long serialVersionUID = 4251598138202580034L;
	
	/**
	 * Adapter to allow communication with model
	 */
	protected transient ICmd2ModelAdapter cmd2model;

	@Override
    public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
        cmd2model = cmd2ModelAdpt;
    }
	
}
