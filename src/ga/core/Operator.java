package ga.core;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * A parent class for all operators (Selection/Reproduction)
 * Copyright 2013 Academic Free License 3.0
 */
public interface Operator<T> {
	/**
	 * Operators like cross over, mutation etc are applied to the population
	 */
	Population<T> operate(Population<T> population);
}
