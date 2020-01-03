package provided.util.loader.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import provided.util.loader.IObjectLoader;


/**
 * An IObjectLoader implementation that will walk through multiple locations, defined by multiple classname prefixes, to load an object of 
 * the specified type.
 * @author swong
 *
 * @param <ReturnT>   The type of object loaded by this IObjectLoader
 */
public class ObjectLoaderPath<ReturnT> implements IObjectLoader<ReturnT>  {	
	

	/**
	 * List of of the given classname prefixes, which is effectively a path list
	 */
	private ArrayList<String> classnamePrefixes  = new ArrayList<String>(Arrays.asList(new String[] {""})); // default to one entry
	
	/**
	 * The error factory to use in case the class is never found
	 */
	Function<Object[], ReturnT> errorFac;
	

	
	/**
	 * Constructor for the class.   The given errorFac is used to generate instances when the loadInstance() method
	 * is otherwise unable to do so because of a processing error.
	 * @param errorFac A factory method that takes the same array of input parameters that loadInstance() 
	 * takes and returns an instance of ReturnT.
	 * @param classnamePrefixes Vararg of the prefixes to search for the given classnames.  
	 * The prefixes should complete the fully qualified pathname when pre=pended to the className given to loadInstance().
	 * The prefixes are searched in the order given.
	 */
	public ObjectLoaderPath(Function<Object[], ReturnT> errorFac, String...classnamePrefixes) {
		this.errorFac = errorFac;
		
		if(classnamePrefixes.length > 0) {  // Want at least one element in the classnamePrefixes list
			this.classnamePrefixes = new ArrayList<String>(Arrays.asList(classnamePrefixes)); // convert the varargs array into an ArrayList
		}

	}	
	
	
	/**
	 * Instantiates and then delegates to a recursive IObjectLoader instance that goes through the saved list of classname prefixes searching
	 * for the partial name, className.   If the  class is never found, the exception printed and the saved errorFac will be invoked to 
	 * generate an error object result.
	 */
	@Override
	public ReturnT loadInstance(String className, Object... args) {
		return (new AObjectLoader<ReturnT>() {
			int prefixIdx = 0; // current prefix index.  A field is being used here so that prefixIdx is mutable.
			
			@Override
			protected ReturnT errorHandler(Exception ex, String fullClassName, Object... params) {
				if( prefixIdx < classnamePrefixes.size()-1) { // Check if any more prefixes to use.  At this point, prefixIdx is the index that was just tried. 
					prefixIdx++;  // increment to the next prefix
					return this.loadInstance(classnamePrefixes.get(prefixIdx)+className, params); // recurse using the next prefix
				}
				else {   // No more prefixes to check.
					// Print the exception to stderr
					System.err.println("ObjectLoaderPath.loadInstance(" + className + ", "
							+ (new ArrayList<Object>(Arrays.asList(args))) + "):\n   Exception = " + ex);
					ex.printStackTrace();
					return errorFac.apply(params);   // Use the errorFac to generate an error object.
				}
			}
		}).loadInstance(classnamePrefixes.get(0)+className, args); // Make initial recursive call, presuming at least one entry in the prefixes list
	}
	
}

