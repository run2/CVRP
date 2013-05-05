package ga.core.terminate;

import ga.core.CommonGA;
import ga.core.Population;
import ga.core.evaluate.FitnessAndCost;

import java.util.List;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Checks and terminates if the fitness of the population is not improving
 * Copyright 2013 Academic Free License 3.0
 */
public class FitnessImprovement<T> implements Termination<T> {
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	private double minimalFitnessImprovement;
	private double prevFitness;
	private FitnessAndCost<T> fitness;
	boolean avgOrAbsolute;
	public FitnessImprovement(FitnessAndCost<T> fitness, double minimalFitnessImprovement,double prevFitness ,boolean avgOrAbsolute){
		this.minimalFitnessImprovement = minimalFitnessImprovement;
		this.fitness = fitness;
		this.prevFitness = prevFitness;
		this.avgOrAbsolute = avgOrAbsolute;
	}
	@Override
	public boolean isTerminationConditionReached(Population<T> population) {
		// TODO Auto-generated method stub
		List<T> chromosomes = population.getAllChromosomes();
		boolean fitEnough = false;
		double runningFitness = 0;
		double sumFitness = 0;
		double avgFitness = 0;
		for (T chromosome : chromosomes){
			runningFitness = fitness.computeFitness(chromosome);
			sumFitness = sumFitness + runningFitness;
			if(!avgOrAbsolute && runningFitness >= prevFitness + minimalFitnessImprovement){
				CommonGA.logger.info("Current population is fitter");
				fitEnough = true;
				break;
			}
		}
		avgFitness = sumFitness/chromosomes.size();
		if(avgFitness > prevFitness + minimalFitnessImprovement){
			fitEnough = true;
			CommonGA.logger.info("Current population is fitter");
		}
		
		return !fitEnough;
	}
}
