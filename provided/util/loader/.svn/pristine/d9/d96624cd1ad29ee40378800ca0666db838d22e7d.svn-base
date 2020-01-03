package provided.util.loader.impl;


import java.util.ArrayList;
import java.util.Arrays;

import java.util.function.Function;



/**
 * Factory that dynamically class loads and instantiates an object of type ReturnT
 * This is a non-recursive object loader that should be used when the fully qualified name of the desired class can be determined BEFORE the 
 * object loader is invoked.
 * @author Stephen Wong
 *
 * @param <ReturnT>  The type of object to be created.
 */
public class ObjectLoader<ReturnT> extends AObjectLoader<ReturnT> {


	/**
	 * Lambda function to return an error object of type ReturnT
	 */
	private Function<Object[], ReturnT> errorFac;

	/**
	 * Constructor for the class.   The given errorFac is used to generate instances when the loadInstance() method
	 * is otherwise unable to do so because of a processing error.
	 * @param errorFac A factory method that takes the same array of input parameters that loadInstance() 
	 * takes and returns an instance of ReturnT.
	 */
	public ObjectLoader(Function<Object[], ReturnT> errorFac) {
		super();
		this.errorFac = errorFac;
	}

	/**
	 * Prints the given Exception to stderr and then invokes the stored errorFac to generate an error object.
	 */
	@Override
	protected ReturnT errorHandler(Exception ex, String className, Object... args) {
		System.err.println("ObjectLoader.loadInstance(" + className + ", "
				+ (new ArrayList<Object>(Arrays.asList(args))) + "):\n   Exception = " + ex);
		ex.printStackTrace();
		return errorFac.apply(args); // Make the error object
	}
}
