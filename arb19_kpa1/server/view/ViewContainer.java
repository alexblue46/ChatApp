package arb19_kpa1.server.view;

/**
 * A container object for the view 
 *
 * @param <T> the type to hold
 */
public class ViewContainer<T> {
	/**
	 * The item held in the container
	 */
	private T item;
	
	/**
	 * Creates a new container for the given item
	 * @param item the item to hold
	 */
	public ViewContainer(T item) {
		this.item = item;
	}
	
	/**
	 * Gets the held item
	 * @return the item
	 */
	public T getItem() {
		return item;
	}

	@Override
	public String toString() {
		return item.toString();
	}
	
}
