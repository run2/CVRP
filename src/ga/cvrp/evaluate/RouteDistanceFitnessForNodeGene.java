package ga.cvrp.evaluate;

import ga.core.evaluate.FitnessAndCost;
import ga.cvrp.chromosomes.CVRPChromosomeWithNodeGene;
import ga.input.Input;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Fitness of route
 * If there are two depos one after another (meaning a truck not going anywhere)
 * the fitness value will be Double.Max_Value since the distance between the two
 * depos will be Double.Max_Value - in such a case the fitness is set to 0
 * Copyright 2013 Academic Free License 3.0
 */
public class RouteDistanceFitnessForNodeGene implements FitnessAndCost<CVRPChromosomeWithNodeGene> {
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	@Override
	public double computeFitness(CVRPChromosomeWithNodeGene chromosome) {
		// TODO Auto-generated method stub
		double result = chromosome.getFitness();
		if(result == Double.MAX_VALUE)
			return 0;
		else
			return (1/(result-Input.optimumMinimalCost));
	}

	@Override
	public double computeActualCost(CVRPChromosomeWithNodeGene chromosome) {
		// TODO Auto-generated method stub
		return chromosome.getActualCost();
	}
	
}
