package ga.cvrp.reproduce;

import ga.core.Operator;
import ga.core.Population;
import ga.cvrp.chromosomes.CVRPChromosomeWithNodeGene;

import java.util.Iterator;
import java.util.Random;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Copyright 2013 Academic Free License 3.0
 */

public class ShuffleTour implements Operator<CVRPChromosomeWithNodeGene> {
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	public double probability;

	public ShuffleTour(double probability) {
		this.probability = probability;
	}
	
	@Override
	public Population<CVRPChromosomeWithNodeGene> operate(
			Population<CVRPChromosomeWithNodeGene> population) {
		// TODO Auto-generated method stub
		Iterator<CVRPChromosomeWithNodeGene> iterator = population
				.getAllChromosomes().iterator();
		Population<CVRPChromosomeWithNodeGene> output = new Population<CVRPChromosomeWithNodeGene>();
		CVRPChromosomeWithNodeGene chromosome;
		Random random = new Random(System.nanoTime());
		while (iterator.hasNext()) {
			chromosome = iterator.next();
			if (random.nextDouble() < probability) {
				//System.out.println("Mutating");
				//chromosome.optimize();
				if(chromosome instanceof CVRPChromosomeWithNodeGene){
					chromosome = ((CVRPChromosomeWithNodeGene)chromosome).shuffleTour();
				}
				//CommonGA.logger.info("Mutated chromosome to " + chromosome.toString() + " at "  + nodeToMutate );
			}
			output.addChromosome(chromosome);
		}
		return output;
		
	}
}