package arb19_kpa1.game.view;

/**
 * An abstraction of a typical action in our game view and the functionality
 * that should go along with it.
 *
 */
public class UnitAction {
	/**
	 * A task called when an action is clicked in the combo box
	 */
	private Runnable actionWhenClicked;
	/**
	 * A task called when an action is selected using the select action button
	 */
	private Runnable actionWhenSelected;
	/**
	 * A task called when an action is unclicked in the combo box
	 */
	private Runnable actionWhenUnclicked;
	/**
	 * A description of this action
	 */
	private String desc;

	/**
	 * Creates a new UnitAction
	 * 
	 * @param desc                the description
	 * @param actionWhenClicked   the action to perform when clicked
	 * @param actionWhenUnclicked the actino to perform when unclicked
	 * @param actionWhenSelected  the action to perform when selected
	 */
	public UnitAction(String desc, Runnable actionWhenClicked, Runnable actionWhenUnclicked,
			Runnable actionWhenSelected) {

		this.actionWhenClicked = actionWhenClicked;
		this.actionWhenUnclicked = actionWhenUnclicked;
		this.actionWhenSelected = actionWhenSelected;
		this.desc = desc;
	}

	@Override
	public String toString() {
		return desc;
	}

	/**
	 * Gets the action when clicked
	 * 
	 * @return a runnable
	 */
	public Runnable getActionWhenClicked() {
		return actionWhenClicked;
	}

	/**
	 * Gets the action to perform when unclicked
	 * 
	 * @return the action
	 */
	public Runnable getActionWhenUnclicked() {
		return actionWhenUnclicked;
	}

	/**
	 * Gets the action to perform when selected
	 * 
	 * @return the action
	 */
	public Runnable getActionWhenSelected() {
		return actionWhenSelected;
	}
}
