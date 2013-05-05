package ga.cvrp.reproduce;

import ga.core.Operator;
import ga.core.Population;
import ga.cvrp.chromosomes.CVRPChromosomeWithNodeGene;

import java.util.Iterator;
import java.util.Random;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Chooses a parent to mutate based on probability and 
 * then chooses a gene in the parent randomly
 * Copyright 2013 Academic Free License 3.0
 */
public class OneChangeMutationWithNodeGene implements Operator<CVRPChromosomeWithNodeGene> {
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	public double probability;

	public OneChangeMutationWithNodeGene(double probability) {
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
				//CommonGA.logger.info("Mutating chromosome " + chromosome.toString());
				int nodeToMutate = 0;
				while(nodeToMutate==0) // cannot mutate the first and last two genes
					nodeToMutate = random.nextInt(chromosome.getSize() - 2);
				//chromosome.mutateGene(nodeToMutate);
				//chromosome.optimize();
				//chromosome.chop();
				//CommonGA.logger.info("Mutated chromosome to " + chromosome.toString() + " at "  + nodeToMutate );
			}
			output.addChromosome(chromosome);
		}
		return output;
	}

}