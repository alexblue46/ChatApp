package arb19_kpa1.game.msg;

import java.util.Map;

import arb19_kpa1.game.MDDGameKeys;
import common.msg.IRoomMsg;
import provided.datapacket.DataPacketIDFactory;
import provided.datapacket.IDataPacketID;
import provided.mixedData.MixedDataKey;

/**
 * A message that holds a number of mixed data keys to be put in the application's MDD
 * @author Owner
 *
 */
public class SetKeysMsg implements IRoomMsg{
	
	/**
	 * UID
	 */
	private static final long serialVersionUID = 2970793527956924313L;

	/**
	 * The key map to be put into the MDD
	 */
	private Map<MDDGameKeys, MixedDataKey<?>> keyMap;
	
	/**
	 * The main room key
	 */
	@SuppressWarnings("rawtypes")
	private MixedDataKey<Map> key;
	
	/**
	 * Creates a new SetKeysMsg
	 * @param keyMap2 the map from game keys to mdd keys
	 * @param key the main room mdd key
	 */
	@SuppressWarnings("rawtypes")
	public SetKeysMsg(Map<MDDGameKeys, MixedDataKey<?>> keyMap2,  MixedDataKey<Map> key) {
		this.keyMap = keyMap2;
		this.key = key;
	}

	@Override
	public IDataPacketID getID() {
		return SetKeysMsg.GetID();
	}
	
	/**
	 * Gets this messages ID
	 * @return ID
	 */
	public static IDataPacketID GetID() {
		return DataPacketIDFactory.Singleton.makeID(SetKeysMsg.class);
	}
	
	/**
	 * Gets the key mapping
	 * @return the map
	 */
	public Map<MDDGameKeys, MixedDataKey<?>> getKeyMap() {
		return keyMap;
	}
	
	/**
	 * Gets the main room key
	 * @return the MDD key
	 */
	@SuppressWarnings("rawtypes")
	public  MixedDataKey<Map> getKey() {
		return key;
	}

}
