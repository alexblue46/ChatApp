package provided.jxMaps.utils;

/**
 * Interface that represents a MapObject whose configuration options be both get and set.
 * @author swong
 *
 * @param <TOptions>   The type of the options object used by the implementor of this interface
 */
public interface IOptionedMapObject<TOptions> {
	/**
	 * Get the currently set configuration options object.  This is the object that was 
	 * set using the setOptions() method and may not fully reflect the current state of the 
	 * entity if the methods on the entity itself were subsequently used to mutate it. 
	 * @return  The TOptions-type options object given to the setOptions() method.
	 */
	public TOptions getOptions();
	
	
	/**
	 * Set the options of the entity.  The given options object is the one that will be returned by 
	 * the getOptions() method and may not fully reflect the current state of the 
	 * entity if the methods on the entity itself were subsequently used to mutate it.
	 * @param options  The TOptions-type options object to configure the entity.
	 */
	public void setOptions(TOptions options);
}
