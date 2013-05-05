package ga.practice.onemax.reproduce;

import ga.core.CommonGA;
import ga.core.Operator;
import ga.core.Population;
import ga.practice.onemax.chromosome.BinaryStringChromosome;

import java.util.Iterator;
import java.util.Random;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Copyright 2013 Academic Free License 3.0
 */

public class BinaryFlipMutation implements Operator<BinaryStringChromosome> {
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	private final double probability;

	public BinaryFlipMutation(double probability) {
		this.probability = probability;
	}
	
	@Override
	public Population<BinaryStringChromosome> operate(
			Population<BinaryStringChromosome> population) {
		// TODO Auto-generated method stub
		Iterator<BinaryStringChromosome> iterator = population
				.getAllChromosomes().iterator();
		Population<BinaryStringChromosome> output = new Population<BinaryStringChromosome>();
		BinaryStringChromosome chromosome;
		Random random = new Random(System.nanoTime());
		while (iterator.hasNext()) {
			chromosome = iterator.next();
			if (random.nextDouble() < probability) {
				CommonGA.logger.info("Mutating chromosome " + chromosome.toString());
				chromosome.mutateGene(random.nextInt(chromosome.getSize() - 1));
				CommonGA.logger.info("Mutated chromosome to " + chromosome.toString());
			}
			output.addChromosome(chromosome);
		}
		return output;
	}

}