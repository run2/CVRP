package ga.practice.onemax.chromosome;
import ga.core.Chromosome;
import ga.core.GeneGenerator;
import ga.core.Population;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Copyright 2013 Academic Free License 3.0
 */

public class BinaryStringChromosome extends Chromosome<Boolean> implements Serializable {
	private static final long serialVersionUID = 3548874457421674253L;
	int noOfOnes = 0;
	public BinaryStringChromosome(Boolean[] input) {
		super(input);
	}
	
	public BinaryStringChromosome(BinaryStringChromosome c) {
		super(c);
	}	
	
	@Override
	public void chop() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Chromosome flip() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void optimize() {
		// TODO Auto-generated method stub
		
	}

	public void mutateGene(int i) {
		this.setGeneAt(i, !this.getGeneAt(i));
	}	

	@Override
	public String toString() {
		StringBuilder chromosome = new StringBuilder();
		noOfOnes = 0;
		for (boolean bit : genes) {
			noOfOnes+=(bit ? 1 : 0);
			chromosome.append(bit ? 1 : 0);
		}
		chromosome.append(" " + noOfOnes) ;
		chromosome.append("\n");
		return chromosome.toString();
	}

	private static BinaryStringChromosome generateRandomChromosome(final GeneGenerator<Boolean> geneGenerator,
			final int geneLength) {

		Boolean[] genes = new Boolean[geneLength];
		for (int j = 0; j < genes.length; j++) {
			genes[j] = geneGenerator.getNewRandomGene();
		}
		return new BinaryStringChromosome(genes);	
	}

	public static Population<BinaryStringChromosome> generatePopulationOfRandomBinaryStringChromosomes(
			final GeneGenerator<Boolean> geneGenerator, final int individualChromosomeLength,
			final int noOfChromosomes) {
		
		List<BinaryStringChromosome> chromosomes = new ArrayList<BinaryStringChromosome>();
		for (int i = 0; i < noOfChromosomes; i++) {
			chromosomes.add(BinaryStringChromosome.generateRandomChromosome(geneGenerator, individualChromosomeLength));
		}
		return new Population<BinaryStringChromosome>(chromosomes);
	}
	@Override
	public void dispose(){
		//do nothing
	}

	@Override
	public double getActualCost() {
		// TODO Auto-generated method stub
		noOfOnes = 0;
		for (boolean bit : genes) {
			noOfOnes+=(bit ? 1 : 0);
		}
		return 1 - noOfOnes;
	}

	@Override
	protected void mutateGene(int i, int j) throws Exception {
		// TODO Auto-generated method stub
		mutateGene(i);
		mutateGene(j);
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public double getFitness() {
		// TODO Auto-generated method stub
		if(isFitnessDirty){
			noOfOnes = 0;
			for (boolean bit : genes) {
				noOfOnes+=(bit ? 1 : 0);
			}
			isFitnessDirty = false;
		}
		return noOfOnes;
	}
	
}