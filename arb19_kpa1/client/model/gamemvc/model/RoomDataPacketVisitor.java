package arb19_kpa1.client.model.gamemvc.model;

import common.cmd.ARoomMsgCmd;
import provided.datapacket.ADataPacket;
import provided.datapacket.IDataPacketID;
import provided.extvisitor.AExtVisitor;

/**
 * A visitor for handling received datapackets in the client
 *
 */
public class RoomDataPacketVisitor extends AExtVisitor<Void, IDataPacketID, Void, ADataPacket>{
	
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -973036363850122867L;

	/**
	 * Create a new visitor algorithm
	 * @param aRoomMsgCmd the command to execute for unknown datapackets
	 */
	public RoomDataPacketVisitor(ARoomMsgCmd<?> aRoomMsgCmd) {
		super(aRoomMsgCmd);
	}
	

}


