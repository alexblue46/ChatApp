package arb19_kpa1.client.model.gamemvc.model;

import common.virtualNetwork.IRoom;

/**
 * Adapter for communication from the game model to the game view
 */
public interface IRoomModel2RoomViewAdpt {

	
	/**
	 * @param senderName String of the name of who sent the message
	 * @param text String of the message sent
	 */
	public void displayGlobalMsg(String senderName, String text);

	/**
	 * Sets the room members in the view
	 * @param room the room with current members
	 */
	public void setMembers(IRoom room);
}
