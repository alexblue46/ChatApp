HOW TO USE AppConfig and AppConfigMap:

AppConfigs are general purpose data structures designed to enable multiple instances 
of an application to be decoupled from the information and processes that make them 
unique.    In conjunction with AppConfigMap, the differentiating information about 
multiple configurations of an application can be held in an AppConfigMap object holding 
multiple instances of AppConfig subclasses.  The information for any configuration
can thus be easily retrieved using a String name from the AppConfigMap and utilized by 
the application.   The application is decoupled from its configurations because it uses
an AppConfig object without it ever having to know the specific data values and processes
that any particular configuration entails. 



RMI CONFIGURATION EXAMPLE:

For instance, to run multiple instances of an RMI application, each instance of the 
app must be configured to use different stub and class file server ports.  Using the 
AppConfig derivative, provided.rmiUtils.RMIPortConfig and a corresponding AppConfigMap<RMIPortConfig> 
instance, multiple application configurations using different ports can be stored 
and retrieved.   A string corresponding to one of the RMIPortConfig object's name, 
coming perhaps from the command line or the GUI, can used to retrieve a particular 
configuration.   The application then simply reads and uses the port information from the 
RMIPortConfig object without being coupled to the actual port number values.

--------- start Example Code -----------------------
/**
 * An application that needs configuration
 */
public class MyApp {
	/**
	 * Define the configurations and the mapping to their names.  
	 * There's nothing special about the names being used here -- any string will work.
	 */
	private AppConfigMap<RMIPortConfig> configMap = new AppConfigMap<RMIPortConfig>(
			new RMIPortConfig("server", IRMI_Defs.STUB_PORT_SERVER, IRMI_Defs.CLASS_SERVER_PORT_SERVER),
			new RMIPortConfig("client", IRMI_Defs.STUB_PORT_CLIENT, IRMI_Defs.CLASS_SERVER_PORT_CLIENT),
			new RMIPortConfig("extra", IRMI_Defs.STUB_PORT_EXTRA, IRMI_Defs.CLASS_SERVER_PORT_EXTRA));
	   );
	
	/**
	 * The current configuration the application will use
	 */
	private RMIPortConfig currentConfig;
	
	/**
	 * Use a String configuration name to set the current configuration in the app's constructor
	 */
	public MyApp(String configName) {
		currentConfig = configMap.getConfig(configName);
	}
	
	/**
	 * Use the data in the configuration instead of hard-coded values to set the RMI ports
	 */
	public void start() {
		rmiUtils.startRMI(currentConfig.classServerPort);
		...
		
		IMyRemoteEntity myRemoteEntityStub = (IMyRemoteEntity) UnicastRemoteObject.exportObject(myRemoteEntity, currentConfig.stubPort); 
	}

}

--------- end Example Code -----------------------


CUSTOM CONFIGURATION PROCESSES:

More complex configuration processes can be implemented by storing configuration strategies 
just like any other configuration data.  The application can then run the strategy held 
by its current configuration to perform whatever custom operations are needed for that 
configuration.   It may be helpful to include a method in the AppConfig subclass that helps
ensure that the strategy is running in the context of the corresponding configuration.

This technique can be used to perform any configuration-dependent processes, both in the initial
configuring of the application as well as while the application is running.

--------- start Example Code -----------------------

public class MyAppConfig extends RMIPortConfig {
	/**
	 * A variant configuration process.  Not public because the app is supposed to use
	 * the runConfig() method below.
	 */
	private BiConsumer<MyAppConfig, MyApp> configStrategy = (config, app) -> {};  // defaults to a no-op for safety.
	
	/**
	 * Constructor for the configuration object takes a configuration strategy
	 * in addition to other data values.
	 */
	public MyAppConfig(String name, int stubPort, int classServerPort,  Consumer<MyAppConfig, MyApp> configStrategy) {
		super(name, stubPort, classServerPort);
		this.configStrategy = configStrategy;
	}
	
	/**
	 * Use the stored strategy to configure the app
	 */
	final public void runConfig(MyApp myApp) {
		configStrategy.accept(this, myApp);  // The strategy is running in the context of both the appn and the configuration object. 
	} 
	
}


public class MyApp {
	/**
	 * Define the configurations and the mapping to their names
	 * There's nothing special about the names being used here -- any string will work.
	 */
	private AppConfigMap<MyAppConfig> configMap = new AppConfigMap<MyAppConfig>(
			new MyAppConfig("server", IRMI_Defs.STUB_PORT_SERVER, IRMI_Defs.CLASS_SERVER_PORT_SERVER, (config, app)->{ 
					// Do some "server" config processes
				}),
			new MyAppConfig("client", IRMI_Defs.STUB_PORT_CLIENT, IRMI_Defs.CLASS_SERVER_PORT_CLIENT, (config, app)->{ 
					// Do some "client" config processes
				}),
			new MyAppConfig("extra", IRMI_Defs.STUB_PORT_EXTRA, IRMI_Defs.CLASS_SERVER_PORT_EXTRA, (config, app)->{ 
					// Do some "extra" config processes
				}));
	   );

	// The constructor is the same as in the above example
	
	/**
	 * Use the data in the configuration instead of hard-coded values to set the RMI ports
	 */
	public void start() {
		currentConfig.runConfig(this);  // Do the configuration-dependent custom processes.
		
		// The RMI configuration is probably the same as the above example, though it may need to
		// be before or after the runConfig() invocation, depending on the system.   Note that 
		// in complex situations, the runConfig() process could handle the RMI configuration as well.
	}
}
--------- end Example Code -----------------------

