/**
 * 
 */
package provided.util.struct.impl;

import provided.util.struct.IDyad;

/**
 * A class that represents a dyad (pair) of elements of possibly different types.
 * This class is a substitute for javafx.Pair since the JavaFX project is no longer an
 * official part of Java.
 * @author swong
 * @param <F> The type of the first element
 * @param <S> The type of the second element
 *
 */
public class Dyad<F,S> implements IDyad<F, S> {
	/**
	 * The first element
	 */
	private F first;
	/**
	 * The second element
	 */
	private S second;
	
	/**
	 * Construct a dyad of the two given elements.
	 * @param first  The first element
	 * @param second The second element
	 */
	public Dyad(F first, S second) {
		this.first = first;
		this.second = second;
	}
	
	/* (non-Javadoc)
	 * @see provided.util.struct.IDyad#getFirst()
	 */
	@Override
	public F getFirst() {
		return this.first;
	}

	/* (non-Javadoc)
	 * @see provided.util.struct.IDyad#getSecond()
	 */
	@Override
	public S getSecond() {
		return this.second;
	}

}
