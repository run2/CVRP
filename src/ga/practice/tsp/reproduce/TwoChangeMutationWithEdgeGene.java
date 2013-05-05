package ga.practice.tsp.reproduce;

import ga.core.CommonGA;
import ga.core.Operator;
import ga.core.Population;
import ga.practice.tsp.chromosome.TSPChromosomeWithEdgeGene;

import java.util.Iterator;
import java.util.Random;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Double edge mutation
 * Only works if the chromosome at least has 9 nodes
 * D A B C X E D
 * Can mutate if D A  and X E are chosen to be flipped with a minimum of 3 nodes between them
 * To keep number of edges changed to 2 ( rather than 4) the vertices of the portion in between is also flipped
 * After mutation D E X C B A D
 * Longer chromosomes are OK - shorter - NO
 * http://www.iba.t.u-tokyo.ac.jp/~jaku/pdf/rindoku101109_slide.pdf
 * Copyright 2013 Academic Free License 3.0
 */
public class TwoChangeMutationWithEdgeGene implements Operator<TSPChromosomeWithEdgeGene> {
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	private final double probability;

	public TwoChangeMutationWithEdgeGene(double probability) {
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
				int geneToMutate1 = 0;
				int geneToMutate2 = 0;
				while(
						geneToMutate1==0
						||geneToMutate2==0
						||geneToMutate1>=population.size()-5
						||geneToMutate2>=population.size()-1
						||(geneToMutate2-geneToMutate1)<=3) {// cannot mutate the first and last gene
					geneToMutate1 = random.nextInt(chromosome.getSize() - 1);
					geneToMutate2 = random.nextInt(chromosome.getSize() - 1);
					if(geneToMutate1 > geneToMutate2){
						int temp = geneToMutate2;
						geneToMutate2 = geneToMutate1;
						geneToMutate1 = temp;
					}
					
				}
				chromosome.mutateGene(geneToMutate1,geneToMutate2);
				CommonGA.logger.info("Mutated chromosome to " + chromosome.toString());
			}
			output.addChromosome(chromosome);
		}
		return output;
	}
}