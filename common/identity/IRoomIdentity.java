package common.identity;

/**
 * Identity class for a room. A user requesting access to a room will
 * first send this class to the receiver before getting back the actual room.
 *
 */
public interface IRoomIdentity extends IIdentity{}
