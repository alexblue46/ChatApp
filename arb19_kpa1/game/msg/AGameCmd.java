package arb19_kpa1.game.msg;

import java.util.Map;

import arb19_kpa1.game.model.utility.MDDUtility;
import common.ICmd2ModelAdapter;
import common.cmd.ARoomMsgCmd;
import common.msg.IRoomMsg;
import provided.mixedData.MixedDataKey;

/**
 * Represents a command for our game.
 *
 * @param <T> the type of the message it responds to
 */
public abstract class AGameCmd<T extends IRoomMsg> extends ARoomMsgCmd<T> {
	/**
	 * UID
	 */
	private static final long serialVersionUID = -5786421966338710302L;
	/**
	 * The utility class for accessing the mixed data dictionary
	 */
	protected MDDUtility mddUtil;

	/**
	 * Creates a new game command
	 * 
	 * @param key the main key for the MDD map
	 */
	@SuppressWarnings("rawtypes")
	public AGameCmd(MixedDataKey<Map> key) {
		mddUtil = new MDDUtility(key);
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		super.setCmd2ModelAdpt(cmd2ModelAdpt);
		mddUtil.setCmd2ModelAdpt(cmd2ModelAdpt);
	}

}
