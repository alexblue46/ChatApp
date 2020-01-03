package arb19_kpa1.server.model;

import common.cmd.AAppMsgCmd;
import common.msg.IAppMsg;
import provided.datapacket.ADataPacket;
import provided.datapacket.IDataPacketID;
import provided.extvisitor.AExtVisitor;

/**
 * visitor that receives datapackets and processes them for the server
 * @author Owner
 *
 */
public class ServerDataPacketVisitor extends AExtVisitor<Void, IDataPacketID, Void, ADataPacket>{
	
	/**
	 * UID
	 */
	private static final long serialVersionUID = 3662507055230998406L;

	/**
	 * Create a new visitor algorithm
	 * @param defaultCmd the command to execute for unknown datapackets
	 */
	public ServerDataPacketVisitor(AAppMsgCmd<? extends IAppMsg> defaultCmd) {
		super(defaultCmd);
	}
	


}
