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
 * Stochastic Universal Sampling
 * http://www.geatbx.com/docu/algindex-02.html#P416_20744
 * http://ifigenia.org/w/images/2/2c/IWGN-2009-01-07.pdf
 * Copyright 2013 Academic Free License 3.0
 */
public class SUSSelector<T> extends Selector<T> implements Operator<T> {
	
	private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());

	public SUSSelector(FitnessAndCost<T> fitness){
		super(fitness);
	}
	
	/**
	 * This method has the following algorithm
	 * Say population size is n - choose n/2 parents each time - so loop 2 times to get n/2 couples
	 * 1. Shuffle the members in the population 
	 * 2. Get fitness of members and individual selective pressure
	 * 3. Choose a random number (r) between 0 1/(n/2) and choose the
	 * parents whose selective pressure ranges fall between the
	 * values of r*[1,n/2-1]
	 * 3. Try to avoid mating same parents* 
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
		// To rule out any kind of bias on a previously fitness based sorted population
	    Population<T> output = new Population<T>();
	    
	    int size = population.size();
	    int halfSize = size/2 + size%2;
	    
	    double r;
	    Random random = new Random(System.nanoTime());// Random(System.currentTimeMillis());
	    // remove duplicates from the population
	    
	    while(output.size() < population.size()){
			Collections.shuffle(chromosomes );
			//logger.info("Shuffled chromosomes " + chromosomes.toString());
		    getSelectivePressure(chromosomes);// this shuffling and selective pressure can be improved
		    //logger.info("Selective pressure " + selectivePressureWheel.toString());
	    	r = random.nextDouble()*((double)1/halfSize);
	    	double ptr = 0;
	    	for(int i = 0 ; i <halfSize ; i++){
	    		ptr = r + ((double)1/halfSize)*(i);
	    		output.addChromosome(chromosomes.get(getIndexOfParentFromSelectionPressureDistribution(ptr)));
	    	}
	    }
	    
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
