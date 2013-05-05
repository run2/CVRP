package ga.core.terminate;

import ga.core.CommonGA;
import ga.core.Population;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Checks and terminates if the number of iterations is more than a specific value
 * Copyright 2013 Academic Free License 3.0
 */
public class Iterations<T> implements Termination<T>{
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	private int maxIterations;
	private int counter;
	public Iterations(int maxIterations){
		this.maxIterations = maxIterations;
		counter = 0;
	}
	@Override
	public boolean isTerminationConditionReached(Population<T> population) {
		// TODO Auto-generated method stub
		if(counter >= maxIterations){
			CommonGA.logger.info("Reached max iterations");
			return true;
		}else{
			counter++;
			return false;
		}
	}
	
	

}
