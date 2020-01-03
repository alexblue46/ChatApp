package provided.config;

import java.util.HashMap;

/**
 * An map of configuration objects (AppConfigMap.AppConfig derivatives) keyed by the 
 * name associated with the configuration. 
 * Use putConfig(config) to store configurations and get(name) to retrieve them. 
 * @author Stephen Wong
 *
 * @param <V> The type of AppConfig configuration being held by this mapping.
 */
public class AppConfigMap<V extends AppConfig> {

	/**
	 * Fully encapsulated configuration object storage to prevent outside mutation
	 */
	HashMap<String, V> configMap = new HashMap<String, V>();
	
	/**
	 * Constructor for the class that assumes that a subclass will load the configuration information.   
	 */
	public AppConfigMap() {
	}
	
	/**
	 * Alternate constructor for the class that allows an outside entity to supply
	 * the configuration objects.
	 * @param configs Vararg of configuration objects to load into the protected internal storage.
	 */
	@SafeVarargs
	public AppConfigMap(V... configs) {
		for(V config: configs) {
			putConfig(config);
		};
	}
	
	/**
	 * Automatically store the given configuration object keyed to its name.
	 * Only this class or a subclass can load the configurations.
	 * @param config  The configuration object to store.
	 */
	protected void putConfig(V config) {
		configMap.put(config.name, config);
	}

	/**
	 * Retrieve the configuration object given its name.   
	 * @param configName The name of the desired configuration object
	 * @return The associated configuration object or null if it could not be found.
	 */
	public V get(String configName) {
		return configMap.get(configName);
	}
}
