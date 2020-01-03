package common;

import java.util.function.Supplier;


import javax.swing.JComponent;
import common.msg.IRoomMsg;
import common.receivers.IRoomMember;
import provided.mixedData.MixedDataKey;

/**
 * Adapter that enables a command to display a GUI component 
 * on the user interface of a local system.<br>
 * Whenever a command to process an unknown message is received, the local system should immediately give an 
 * instance of this adapter to the received command to connect it into 
 * the local system. Then the command should be installed into the message processing visitor. <br>
 *  
 * In the case of a foreign command (user 1) using this adapter (user 2) to send a message to another user (user 3),
 * the sender of the message to user 3 will be user 1
 *
 */
public interface ICmd2ModelAdapter {
	
	/**
	 * Displays the given GUI component on the local system's user interface.
	 * @param componentFac Factory that builds the GUI component to display. It is created by the same
	 * 		commands that process messages sent by other users.
	 * @param title The title of the newly created component
	 */
	public void displayComponent(Supplier<JComponent> componentFac, String title);

	/**
	 * Displays the given text on the local system's user interface (more specifically, the chat window text area.)
	 * @param text Text to display in the chat window text area.
	 */
	public void displayText(String text);
	
	/**
	 * Send a message to another IRoomMember in the room.
	 * @param message Message to send to the other IRoomMember.
	 * @param member The IRoomMember to send a message to.
	 */
	public void sendMessageToMember(IRoomMsg message, IRoomMember member);
	
	/**
	 * Send a message to all IroomMembers in the room.
	 * @param message Message to send to everyone in the room.
	 */
	public void sendMessageToAll(IRoomMsg message);
	
	/**
	 * Put item in the database
	 * @param <T> type of item
	 * @param key item key
	 * @param value The adapter to install
	 */		
	public <T> void putItemInDB(MixedDataKey<T> key, T value);
	
    /**
     * Retreives an item from the database
     * @param <T> type of item
     * @param key item key
     * @return item of type t
     */
	public <T> T getItemInDB(MixedDataKey<T> key);
	
	/**
	 * Retrieves the API key from the local system
	 * @return The API key
	 */
	public String getAPIKey();
	
	/**
	 * No-op singleton implementation of ICmd2ModelAdapter.
	 */
	public static final ICmd2ModelAdapter NULL_OBJECT = new ICmd2ModelAdapter() {

		@Override
		public void displayComponent(Supplier<JComponent> componentFac, String title) {}

		@Override
		public void displayText(String text) {}

		@Override
		public <T> void putItemInDB(MixedDataKey<T> key, T value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public <T> T getItemInDB(MixedDataKey<T> key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void sendMessageToMember(IRoomMsg message, IRoomMember member) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sendMessageToAll(IRoomMsg message) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getAPIKey() {
			// TODO Auto-generated method stub
			return null;
		}
	};

}
