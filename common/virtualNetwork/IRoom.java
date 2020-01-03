package common.virtualNetwork;

import common.identity.IRoomIdentity;
import common.receivers.IRoomMember;

/**
 * Collection of message receivers within a single room
 *
 */
public interface IRoom extends IVirtualNetwork<IRoomMember, IRoomIdentity> {}
