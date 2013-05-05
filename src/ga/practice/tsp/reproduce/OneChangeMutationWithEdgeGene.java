package ga.practice.tsp.reproduce;

import ga.core.CommonGA;
import ga.core.Operator;
import ga.core.Population;
import ga.practice.tsp.chromosome.TSPChromosomeWithEdgeGene;

import java.util.Iterator;
import java.util.Random;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Mutates a single edge by flipping it over
 * Copyright 2013 Academic Free License 3.0
 */
public class OneChangeMutationWithEdgeGene implements Operator<TSPChromosomeWithEdgeGene> {
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	private final double probability;

	public OneChangeMutationWithEdgeGene(double probability) {
		this.probability = probability;
	}
	
	@Override
	public Population<TSPChromosomeWithEdgeGene> operate(
			Population<TSPChromosomeWithEdgeGene> population) {
		// TODO Auto-generated method stub
		Iterator<TSPChromosomeWithEdgeGene> iterator = population
				.getAllChromosomes().iterator();
		Population<TSPChromosomeWithEdgeGene> output = new Population<TSPChromosomeWithEdgeGene>();
		TSPChromosomeWithEdgeGene chromosome;
		Random random = new Random(System.nanoTime());
		while (iterator.hasNext()) {
			chromosome = iterator.next();
			if (random.nextDouble() < probability) {
				CommonGA.logger.info("Mutating chromosome " + chromosome.toString());
				int geneToMutate = 0;
				//while(geneToMutate==0||geneToMutate==population.size()-1) // cannot mutate the first and last gene
				geneToMutate = random.nextInt(chromosome.getSize() - 1);
				chromosome.mutateGene(geneToMutate);
				CommonGA.logger.info("Mutated chromosome to " + chromosome.toString());
			}
			output.addChromosome(chromosome);
		}
		return output;
	}

}