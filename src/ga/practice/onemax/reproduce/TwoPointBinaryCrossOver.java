package ga.practice.onemax.reproduce;

import ga.core.Chromosome;
import ga.core.CommonGA;
import ga.core.Operator;
import ga.core.Population;
import ga.practice.onemax.chromosome.BinaryStringChromosome;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Copyright 2013 Academic Free License 3.0
 */
public class TwoPointBinaryCrossOver implements Operator<BinaryStringChromosome> {
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	double reproduceProbability;

	public TwoPointBinaryCrossOver(double probability){
		this.reproduceProbability = probability;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Population<BinaryStringChromosome> operate(
			Population<BinaryStringChromosome> population) {
		// TODO Auto-generated method stub
		Iterator<BinaryStringChromosome> it = population.getAllChromosomes().iterator();
		Population<BinaryStringChromosome> output = new Population<BinaryStringChromosome>();
		Random random = new Random(System.nanoTime());

		while(it.hasNext()){
			Set<Chromosome> allowedIntoNewPopulation = new HashSet<Chromosome>();
			BinaryStringChromosome parent1 = it.next();
			BinaryStringChromosome parent2 = it.next();

			//logger.info("Couples chosen to reproduce are " + parent1.toString() + " and " + parent2.toString());
			
			BinaryStringChromosome child1 ;
			BinaryStringChromosome child2 ;
			

			child1 = new BinaryStringChromosome(parent1);
			child2 = new BinaryStringChromosome(parent2);
			
			// Parents who are already retained are not allowed to have duplicate copies of them
			// in the population
			if (random.nextDouble() < reproduceProbability|| (allowedIntoNewPopulation.contains(parent1)||allowedIntoNewPopulation.contains(parent2) )) {

				int point1 = 0;
				int point2 = 0;
				int temp = 0;
				while(point1==0 || point1==(parent1.getSize()-1) || point2==0 || point2==(parent1.getSize()-1) || (point1==point2)){
					point1 = random.nextInt(parent1.getSize()-1);
					point2 = random.nextInt(parent1.getSize()-1);
					if(point1>point2){
						temp = point2;
						point2 = point1;
						point1 = temp;
					}
				}
				
				child1.setGenes(point1, point2, point1, parent2.getGenes());
				child2.setGenes(point1, point2, point1, parent1.getGenes());

				//logger.info("Children produced are " + child1.toString() + " and " + child2.toString());
				output.addChromosome(child1);
				output.addChromosome(child2);				
			}else{
				CommonGA.logger.info("Couple retained in next generation ");
				
				allowedIntoNewPopulation.add(parent1);
				allowedIntoNewPopulation.add(parent2);
				output.addChromosome(parent1);
				output.addChromosome(parent2);				
			}
			

		}
		return output;
	}

	

}
