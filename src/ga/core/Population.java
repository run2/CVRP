package ga.core;
import ga.core.evaluate.FitnessAndCost;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * This is the population class of generic chromosomes
 * Copyright 2013 Academic Free License 3.0
 */
public class Population<T> implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5631021004208794918L;
	
	
	/**
	 * Chromosomes should only be initialized within the constructors
	 */
	private final List<T> chromosomes;

	public Population(List<T> chromosomes) {
		this.chromosomes = chromosomes;
	}

	

	/**
	 * This creates an empty population
	 * Ideally addpopulationofchromosomes
	 * should be called after using this constructor
	 */
	public Population() {
		chromosomes = new ArrayList<T>();
	}

	public List<T> getAllChromosomes() {
		return chromosomes;
	}

	public void addChromosome(T chromosome) {
		chromosomes.add(chromosome);
	}

	public void removeChromosome(int position) {
		chromosomes.remove(position);
	}

	public void removeAllChromosomes() {
		chromosomes.clear();
	}
	
	/**
	 * In here caching (FitnessHistoryWrapper) will come into play if the
	 * chromosomes fitness has already been measured
	 * A chromosome is identified by the sequence
	 * of genes in it (and also by its purpose)
	 */
	public void evaluate(FitnessAndCost<T> fitness) {
		for (T chromosome: chromosomes){
			fitness.computeFitness(chromosome);
			fitness.computeActualCost(chromosome);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addPopulationOfChromosomes(Population<T> population, int populationSize) {
		
		Chromosome[] chromosomeArray = new Chromosome[population.getAllChromosomes().size()];
		population.getAllChromosomes().toArray(chromosomeArray);
		Arrays.sort(chromosomeArray);
		for( int j = 0 ; j < populationSize; j++ ){
			chromosomes.add((T)chromosomeArray[j]);
		}
		
	}
	/** 
	 * This method calls the toString method of every chromosome
	 * in the population
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Start Population\n");
		for (T chromosome : chromosomes) {
			sb.append(chromosome.toString());
		}
		sb.append("End Population\n");
		return sb.toString();
	}

	public int size() {
		return chromosomes.size();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * Two populations are equal only when all their
	 * chromosomes are equal - sequence doe not matter
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof Population)) {
			return false;
		}

		Population<T> compareTo = checkTypeOfEveryChromosomeAndReturnPopulation(object);
		return Arrays.equals(chromosomes.toArray() , compareTo.chromosomes.toArray());
	}

	/** {@inheritDoc} */
	public int hashCode() {
		return chromosomes.hashCode();
	}
	/** 
	 * This equals checks every chromosome of the compareTo population
	 * to make sure they are of the same type as this population
	 */
	private Population<T> checkTypeOfEveryChromosomeAndReturnPopulation(Object object) {
		@SuppressWarnings("unchecked")
		Population<T> population = (Population<T>) object;
		for (@SuppressWarnings("unused") T chromosome : population.getAllChromosomes()) {
			// DO nothing - the above loop will check the cast of each member
		}
		return population;
	}
	
	public void dispose(){
		//for (int i = 0 ; i < chromosomes.size() ; i++)
			//((Chromosome)chromosomes.get(i)).dispose();
		chromosomes.clear();
	}

}

