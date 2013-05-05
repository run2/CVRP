package ga.core.evaluate;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * This is the interface for computing fitness of a generic chromosome
 * Copyright 2013 Academic Free License 3.0
 */
public interface FitnessAndCost<T> {
	double computeFitness(T chromosome);
	double computeActualCost(T chromosome);

}
