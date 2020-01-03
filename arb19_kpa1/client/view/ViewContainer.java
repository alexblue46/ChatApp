package arb19_kpa1.client.view;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ViewContainer<?> other = (ViewContainer<?>) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}
	
}
