package ga.core.select;

import ga.core.CommonGA;
import ga.core.Operator;
import ga.core.Population;
import ga.core.evaluate.FitnessAndCost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Simple routlette wheel selector
 * Copyright 2013 Academic Free License 3.0
 */

public class RouletteWheelSelector<T> extends Selector<T> implements Operator<T> {
	private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	public RouletteWheelSelector(FitnessAndCost<T> fitness){
		super(fitness);
	}
	/**
	 * This method has the following algorithm
	 * 1. Get individual selective pressure of chromosomes based on fitness
	 * 2. Go through the population and pick as many couples as the population size 
	 * to be considered for reproducing. Picking up double the size allows
	 * maintaining the population size over generations assuming each pair of
	 * parents will give two children
	 * 3. Try to avoid mating same parents
	 */
	@Override
	public Population<T> operate(Population<T> population) {
		// TODO Auto-generated method stub
		Set<T> uniqueChromosomes = new HashSet<T>();
		
		for(int i = 0 ; i < population.getAllChromosomes().size() ; i ++){
			uniqueChromosomes.add(population.getAllChromosomes().get(i));
		}
		
		if(uniqueChromosomes.size()<10){
			logger.info("Unique chromosomes reduced to " + uniqueChromosomes.size());
		}
		
		List<T> chromosomes = new ArrayList<T>();
		Iterator<T> it = uniqueChromosomes.iterator();
		while(it.hasNext()){
			chromosomes.add(it.next());
		}
		
	    //List<T> chromosomes = population.getAllChromosomes();
	    getSelectivePressure(chromosomes);
	    //CommonGA.logger.info("Selective pressure " + selectivePressureWheel.toString());
	    Population<T> output = new Population<T>();
	    Random random = new Random(System.nanoTime());
	    
	    int iParent1,iParent2;
	    while(output.size() < population.size()){
	    	iParent1 = getIndexOfParentFromSelectionPressureDistribution(random.nextDouble()) ;
	    	iParent2 = getIndexOfParentFromSelectionPressureDistribution(random.nextDouble()) ;
    		output.addChromosome(chromosomes.get(iParent1));
    		output.addChromosome(chromosomes.get(iParent2));
	    		
	    }
	    
	    // Try to avoid mating the same parents 
	    // Try to avoid mating the same parents 
		int avoidSameParentMatingRetry = 0;
		boolean sameParentsInCouple = true;
		while(sameParentsInCouple && avoidSameParentMatingRetry < 5){
			sameParentsInCouple = false;
			for(int i = 0 ; i < output.size()-1 ; i++,i++){
				if(output.getAllChromosomes().get(i).equals(output.getAllChromosomes().get(i+1))){
					sameParentsInCouple = true;
					break;
				}
			}
			avoidSameParentMatingRetry++;
			if(sameParentsInCouple)
				Collections.shuffle(output.getAllChromosomes());
		}
		//CommonGA.logger.info("Shuffled reproducing couples " + couples.toString());
	    
		return output;
	}
	
}
