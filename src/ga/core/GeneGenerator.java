package ga.core;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Copyright 2013 Academic Free License 3.0
 */
public interface GeneGenerator<T> {
	
	/**
	 * Gets a random gene of the specific type
	 * using some kind of seed and randomness
	 */
	T getNewRandomGene();
	abstract void reInitialize();
}
