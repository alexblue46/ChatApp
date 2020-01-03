package provided.jxMaps.utils;

import java.util.function.BiConsumer;


/**
 * A synonym for BiConsumer&lt;T, U&gt; that helps convey the semantic of an 
 * action based on two inputs of types T and U.
 * @author swong
 *
 * @param <T> The type of the first input.
 * @param <U> The type of the second input.
 */
public interface IBiAction<T, U> extends BiConsumer<T, U> {
	
}





