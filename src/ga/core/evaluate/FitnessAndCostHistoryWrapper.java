package ga.core.evaluate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * This is a caching wrapper to store the fitness and costs for chromosomes
 * Copyright 2013 Academic Free License 3.0
 */
public class FitnessAndCostHistoryWrapper<T> implements FitnessAndCost<T>{
	
	private final Map<T,Double> fitnessMap;
	private final Map<T,Double> costMap;
	private FitnessAndCost<T> fitness;
	public FitnessAndCostHistoryWrapper(FitnessAndCost<T> fitness){
		fitnessMap = new HashMap<T,Double>();
		costMap = new HashMap<T,Double>();
		this.fitness = fitness;
	}
	@Override
	public double computeFitness(T chromosome) {
		// TODO Auto-generated method stub
		if(fitnessMap.containsKey(chromosome)){
			//logger.info("fitness " + fitnessMap.get(chromosome));
			return fitnessMap.get(chromosome);
		}else{
			double fitnessValue = fitness.computeFitness(chromosome);
			fitnessMap.put(chromosome, fitnessValue);
			return fitnessValue;
		}
	}
	@Override
	public double computeActualCost(T chromosome) {
		// TODO Auto-generated method stub
		if(costMap.containsKey(chromosome)){
			//logger.info("fitness " + fitnessMap.get(chromosome));
			return costMap.get(chromosome);
		}else{
			double costValue = fitness.computeActualCost(chromosome);
			costMap.put(chromosome, costValue);
			return costValue;
		}
	}
	
}
