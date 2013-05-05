package ga.core.terminate;

import ga.core.Population;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Generic termination interface
 * Copyright 2013 Academic Free License 3.0
 */
public interface Termination<T> {
	boolean isTerminationConditionReached(Population<T> population);
}
