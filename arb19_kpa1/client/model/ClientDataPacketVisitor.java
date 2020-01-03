package arb19_kpa1.client.model;

import common.cmd.AAppMsgCmd;
import common.msg.IAppMsg;
import provided.datapacket.ADataPacket;
import provided.datapacket.IDataPacketID;
import provided.extvisitor.AExtVisitor;

/**
 * A visitor for handling received datapackets in the client
 *
 */
public class ClientDataPacketVisitor extends AExtVisitor<Void, IDataPacketID, Void, ADataPacket>{
	
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -4604419118672240240L;

	/**
	 * Create a new visitor algorithm
	 * @param defaultCmd the command to execute for unknown datapackets
	 */
	public ClientDataPacketVisitor(AAppMsgCmd<? extends IAppMsg> defaultCmd) {
		super(defaultCmd);
	}
	

}


