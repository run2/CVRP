/**
 * 
 */
package ga.practice.onemax.evaluate;

import ga.core.evaluate.FitnessAndCost;
import ga.practice.onemax.chromosome.BinaryStringChromosome;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Copyright 2013 Academic Free License 3.0
 */
public class OneMaxFitness implements FitnessAndCost<BinaryStringChromosome> {
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	@Override
	public double computeFitness(BinaryStringChromosome chromosome) {
		// TODO Auto-generated method stub
		double result = 0;
		for (Boolean gene : chromosome.getGenes())
			if(gene)result++;
		
		//logger.info("Returning fitness " + result + " for chromosome " + chromosome.toString());
		return result;
	}

	@Override
	public double computeActualCost(BinaryStringChromosome chromosome) {
		// TODO Auto-generated method stub
		double result = 0;
		for (Boolean gene : chromosome.getGenes())
			if(!gene)result++;
		
		return result;
	}
	
}
