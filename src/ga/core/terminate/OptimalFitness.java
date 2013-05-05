package ga.core.terminate;

import ga.core.CommonGA;
import ga.core.Population;
import ga.core.evaluate.FitnessAndCost;

import java.util.List;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Checks and terminates if a optimal fitness has been reached
 * Copyright 2013 Academic Free License 3.0
 */
public class OptimalFitness<T> implements Termination<T> {
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	
	private double optimumFitness;
	private FitnessAndCost<T> fitness;
	public OptimalFitness(double optimumFitness,FitnessAndCost<T> fitness){
		this.optimumFitness = optimumFitness;
		this.fitness = fitness;
	}
	@Override
	public boolean isTerminationConditionReached(Population<T> population) {
		// TODO Auto-generated method stub
		List<T> chromosomes = population.getAllChromosomes();
		boolean fitEnough = false;
		for (T chromosome : chromosomes){
			if(fitness.computeFitness(chromosome) >= optimumFitness){
				CommonGA.logger.info("Reached optimal fitness");
				fitEnough = true;
				break;
			}
		}
		return fitEnough;
	}

}
