package ga.cvrp.reproduce;

import ga.core.Operator;
import ga.core.Population;
import ga.cvrp.chromosomes.CVRPChromosomeWithNodeGene;

import java.util.Iterator;
import java.util.Random;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Double edge mutation
 * Only works if the chromosome at least has 9 nodes
 * D A B C X E D
 * Can mutate if A E are chosen to be flipped with a minimum of 3 nodes between them
 * To keep number of edges changed to 2 ( rather than 4) the vertices of the portion in between is also flipped
 * After mutation D E X C B A D
 * Longer chromosomes are OK - shorter - NO
 * http://www.iba.t.u-tokyo.ac.jp/~jaku/pdf/rindoku101109_slide.pdf
 * Copyright 2013 Academic Free License 3.0
 */
public class TwoChangeMutationWithNodeGene implements Operator<CVRPChromosomeWithNodeGene> {
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	private final double probability;

	public TwoChangeMutationWithNodeGene(double probability) {
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
		
		// Does not work with any chromosome less that 9 nodes
		if(population.getAllChromosomes().get(0).getSize() < 7 ) return population;
		
		while (iterator.hasNext()) {
			chromosome = iterator.next();
			if (random.nextDouble() < probability) {
				//CommonGA.logger.info("Mutating chromosome " + chromosome.toString());
				int nodeToMutate1 = 0;
				int nodeToMutate2 = 0;

				while(
						nodeToMutate1==0
						||nodeToMutate2==0
						||nodeToMutate1>chromosome.getSize()-6
						||nodeToMutate2>chromosome.getSize()-2
						||(nodeToMutate2-nodeToMutate1)<4) 
				
				{// cannot mutate the first and last gene
					nodeToMutate1 = random.nextInt(chromosome.getSize() - 1);
					nodeToMutate2 = random.nextInt(chromosome.getSize() - 1);
					if(nodeToMutate1 > nodeToMutate2){
						int temp = nodeToMutate2;
						nodeToMutate2 = nodeToMutate1;
						nodeToMutate1 = temp;
					}
				}
				chromosome.mutateGene(nodeToMutate1,nodeToMutate2);
				//CommonGA.logger.info("Mutated chromosome to " + chromosome.toString() + " at " + nodeToMutate1 + " " + nodeToMutate2);
			}
			output.addChromosome(chromosome);
		}
		return output;
	}
}