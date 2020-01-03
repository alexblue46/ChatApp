package common.receivers;

import common.msg.IAppMsg;
import common.packet.AppDataPacket;

/**
 * Persistent user for a single FinalProject application. Exists in discovery server
 * for others to find and connect to. Only capable of receiving application-level
 * data packets
 */
//public interface IApplication extends IMsgReceiver<IAppMsg>{}
public interface IApplication extends IMsgReceiver<AppDataPacket<? extends IAppMsg>>{}
