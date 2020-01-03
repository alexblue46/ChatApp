package arb19_kpa1.server.model.mvc.model;

import common.cmd.ARoomMsgCmd;
import common.msg.IRoomMsg;
import provided.datapacket.ADataPacket;
import provided.datapacket.IDataPacketID;
import provided.extvisitor.AExtVisitor;

/**
 * A processor for DataPackets for the server
 *
 */
public class ServerMemberDataPacketVisitor extends AExtVisitor<Void, IDataPacketID, Void, ADataPacket>{
	
	/**
	 * UID
	 */
	private static final long serialVersionUID = 1539815782936672585L;

	/**
	 * Create a new visitor algorithm
	 * @param aRoomMsgCmd the command to execute for unknown datapackets
	 */
	public ServerMemberDataPacketVisitor(ARoomMsgCmd<? extends IRoomMsg> aRoomMsgCmd) {
		super(aRoomMsgCmd);
	}

	

}
