package ga.core;

import ga.core.evaluate.FitnessAndCost;
import ga.core.terminate.Termination;
import ga.cvrp.chromosomes.CVRPChromosomeWithNodeGene;
import ga.input.Input;
import ga.practice.onemax.chromosome.BinaryStringChromosome;
import ga.practice.tsp.chromosome.TSPChromosomeWithEdgeGene;
import ga.practice.tsp.gene.EdgeGene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import poi.cvrp.PlotIt;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * This is the class which runs the GA
 * It is generic, so the same class can be used
 * for different types of chromosomes and genes
 * Copyright 2013 Academic Free License 3.0
 */

public class RunGA<T> {
	
	final List<Operator<T>> reproductionOperators;
	Operator<T> selector;

	FitnessAndCost<T> fitnessEvaluator;
	Population<T> currentPopulation = new Population<T>();
	Population<T> lastPopulation;
	Termination<T> terminator;

	int populationSize = 0;
	int resetCounter = -1;
	PlotIt plotit = null;
	public static String bestChromosomeAtIteration;
	public static double bestCost = Double.MAX_VALUE;
	@SuppressWarnings({ "rawtypes"})
	public static ArrayBlockingQueue<Chromosome> bestChromosomes ;
	int lastBestIteration = 0;
	int iterations = 0;
	@SuppressWarnings("rawtypes")
	Chromosome lastBestChromosome = null;
	GeneGenerator<T> geneGenerator;
	Random random = new Random(System.nanoTime());
	boolean resetFired = false;
	boolean optimizeOnReset = false;
	boolean skipSelectionOnReset = false;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RunGA(Population<T> initialPopulation, int storeCount, int populationSize, int resetCounter, int optimizeOnReset , int skipSelectionOnReset, GeneGenerator<T> geneGenerator){
		this.lastPopulation = initialPopulation;
		this.geneGenerator = geneGenerator;
		reproductionOperators = new ArrayList<Operator<T>>();
		bestChromosomes = new ArrayBlockingQueue(storeCount);
		this.populationSize = populationSize;
		this.resetCounter = resetCounter;
		//geneGenerator = new RandomNodeGeneGenerator(Input.nodes);
		resetFired = false;
		if(optimizeOnReset==0)
			this.optimizeOnReset = false;
		else
			this.optimizeOnReset = true;

		if(skipSelectionOnReset==0)
			this.skipSelectionOnReset = false;
		else
			this.skipSelectionOnReset = true;
		
		
	}
	public void setPlotit(PlotIt plotit){
		this.plotit	= plotit;
	}
	/**
	 * Stores a running queue of best (valid) chromosomes found so far
	 */
	@SuppressWarnings("rawtypes")
	public void storeBestChromosome(){
		
		//Chromosome bestChromosome;
		Chromosome currentChromosome;
		List<T> chromosomes = lastPopulation.getAllChromosomes();
		//Chromosome c = null;
		for(int i = 0 ; i < chromosomes.size() ; i ++ ){
			
			currentChromosome = (Chromosome)chromosomes.get(i);
			
			if(currentChromosome.getActualCost() < bestCost && currentChromosome.isValid()){
				bestCost = currentChromosome.getActualCost();
				//bestChromosome = currentChromosome;

				// Store this chromosome - max 10 best
				//c = null;
				//lastBestChromosome = null;
				try {
					lastBestChromosome = currentChromosome.clone();
					lastBestChromosome.setPurpose("Store");
					if(bestChromosomes.size()==populationSize)
						bestChromosomes.poll();
					bestChromosomes.put(lastBestChromosome);

					bestChromosomeAtIteration = iterations + ":" +  currentChromosome.toString();
					CommonGA.logger.info("Best solution improved " + bestChromosomeAtIteration);
					if(null!=lastBestChromosome)
						System.out.println(iterations + "\t" + lastBestChromosome.getActualCost());
						//CommonGA.plotlogger.info(iterations + "\t" + c.getActualCost());
					
					lastBestIteration = iterations;
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		if(null!=plotit && iterations > 1){
			try{
				if(null!=lastBestChromosome){
					Object[] data = {new Double(iterations),new Double(lastBestChromosome.getActualCost()),new Date()};
					plotit.plot(data);
				}
			}catch(Exception e){
				CommonGA.logger.info("PLotit failed to plot");
			}
		}

	}
	
	/**
	 * This feeds back at most one fourth of the population size, best chromosomes stored so far, into the running population
	 * Replaces random elements in the current population
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void reset(){
		
		if(iterations - lastBestIteration > resetCounter){// no improvement
			if(lastPopulation.getAllChromosomes().get(0) instanceof CVRPChromosomeWithNodeGene)
				lastPopulation = (Population<T>)CVRPChromosomeWithNodeGene.generatePopulationOfRandomPathChromosomes((GeneGenerator<Node>)geneGenerator, lastPopulation.size()/2,Input.nodes.size() ,Input.startDepo, Input.endDepo);
			else if(lastPopulation.getAllChromosomes().get(0) instanceof TSPChromosomeWithEdgeGene)
				lastPopulation = (Population<T>)TSPChromosomeWithEdgeGene.generatePopulationOfRandomPathChromosomes((GeneGenerator<EdgeGene>)geneGenerator, lastPopulation.size()/2,Input.nodes.size() ,Input.startDepo, Input.endDepo);
			else if(lastPopulation.getAllChromosomes().get(0) instanceof BinaryStringChromosome)
				lastPopulation = (Population<T>)BinaryStringChromosome.generatePopulationOfRandomBinaryStringChromosomes((GeneGenerator<Boolean>)geneGenerator, lastPopulation.size()/2,Input.nodes.size());
				
			
			Iterator<Chromosome> it = bestChromosomes.iterator();
			int count = bestChromosomes.size();
			CommonGA.logger.info("Adding back best chromosomes");
			
			while(it.hasNext()){
				Chromosome c = null;
				if(count - populationSize/2 <= 0){
					try {
						c = ((Chromosome)it.next()).clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					c.setPurpose("GA");
					if(optimizeOnReset)
						c.optimize();
					lastPopulation.getAllChromosomes().add((T)c);
				}else{
					it.next();
				}
				count --;
			}
			Collections.shuffle(lastPopulation.getAllChromosomes());
			lastBestIteration = iterations;
			resetFired = true;
		}
		/*if(iterations - lastBestIteration > resetCounter){
			GeneGenerator<Node> pathGeneGenerator = new RandomNodeGeneGenerator(Input.nodes);
			lastPopulation = (Population<T>)CVRPChromosomeWithNodeGene.generatePopulationOfRandomPathChromosomes(pathGeneGenerator, lastPopulation.size(),Input.nodes.size() ,Input.startDepo, Input.endDepo);
			lastBestIteration = iterations;
		}*/
		
	}
	
	/**
	 * This is the method which runs the GA
	 * 1. Check the termination condition on the last population and terminates the run if reached
	 * 2. Evaluates the fitness of the last population. This makes sure that the fitnesses
	 * and actual costs of all the chromosomes in the last population are stored (if not already present) in the 
	 * fitness and cost history wrapper (cached)
	 * 3. Increment the iteration
	 * 4. Conditionally checks if there is a reset flag ( > -1). If yes then re introduces
	 * some of the best chromosomes into the last population if there has been no improvement
	 * in the best chromosomes over as many as the resetcounter number (stuck into local optima)
	 * Along with the best chromosomes some random population is also introduced. Currently
	 * it is half random and half best chromosomes. The best chromosomes are optimized (condition) before
	 * being reintroduced into the population. The optimization uses a kind of sweep search
	 * algorithm to minimize intersections in sub routes
	 * 5. Stores the best (and valid) chromosomes which have the minimum costs (NOTE - not fitness) in a queue
	 * (FIFO) where the tail will have the (best) chromosome with least cost
	 * 6. Selects the parents from this last population and chooses couples for reproduction operations.
	 * If there has just been a reset this selection operation is overridden as the best chromosomes
	 * will always have a much higher probability compared to the new random population. In that case
	 * (immediately before reset) the half random and half optimized best chromosome population 
	 * goes for cross over/mutation as it is. Additionally in this case the population is shuffled
	 * so that the new population and old population are mixed when forming couples.
	 * 7. Applies cross over and mutation operators (first cross over and then mutation )
	 * Note
	 * 8. Dispose the last population and intialize it as the current population
	 * 
	 * Finally when the termination condition is reached
	 * the bestschromosomes are optimised and a final check is done to 
	 * see if there is any in the last population which is better by called storeChromosomes
	 */
	public void run(){
		lastBestChromosome = null;
		while(!terminator.isTerminationConditionReached(lastPopulation)){//=> 1
			
			lastPopulation.evaluate(fitnessEvaluator);//=> 2
			
			iterations++;//=>3
			
			CommonGA.logger.info("Iterations" + iterations);
			//CommonGA.logger.info("Parent generation " + lastPopulation.toString() + " Iteration " + iterations);

			if(resetCounter > 0 ){// => 4
				reset();// => 4
			}			
			storeBestChromosome(); // => 5
			
			currentPopulation.dispose();// => 6
			if(resetFired == false || (resetFired == true && skipSelectionOnReset == false)){
				currentPopulation = selector.operate(lastPopulation); // => 6
			}else{
				//force mating
				currentPopulation = lastPopulation; // => 6
				resetFired = false;
			}

			for(Operator<T> o : reproductionOperators){// => 7
				currentPopulation = o.operate(currentPopulation); // => 7
			}
			//CommonGA.logger.info("Current population " + currentPopulation.toString() + " Iteration " + iterations);

			lastPopulation.dispose();// => 8
			lastPopulation.addPopulationOfChromosomes(currentPopulation,populationSize);// => 8

			//CommonGA.logger.info("Next generation " + lastPopulation.toString() + " Iteration " + iterations);
		}
		iterations++;
		optimizeBestChromosomes();
		storeBestChromosome();
		
	}
	public void cleanUp(){
		
		currentPopulation.dispose();
		lastPopulation.dispose();
		bestChromosomeAtIteration = "";
		bestCost = Double.MAX_VALUE;
		lastBestIteration = 0;
		iterations = 0;
		random = new Random(System.nanoTime());
		resetFired = false;

		while(bestChromosomes.size()>0)
			bestChromosomes.remove();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void optimizeBestChromosomes(){
		Iterator<Chromosome> it = bestChromosomes.iterator();
		CommonGA.logger.info("Optimizing chromosomes");
		lastPopulation.dispose();
		
		while(it.hasNext()){
			Chromosome c = null;
			c = ((Chromosome)it.next());
			CommonGA.logger.info("Cost before optimizing chromosome" + c.toString() );
			c.optimize();
			CommonGA.logger.info("Cost after optimizing chromosome" + c.toString() );
			lastPopulation.addChromosome((T)c);
		}
	}
	public void setSelector(Operator<T> selector){
		this.selector = selector;
	}
	public void setTermination(Termination<T> terminator){
		this.terminator = terminator;
	}
	public void addReproductionOperators(Operator<T> reproductionOperator){
		this.reproductionOperators.add(reproductionOperator);
	}
	public void setFitnessEvaluator(FitnessAndCost<T> fitnessEvaluator){
		this.fitnessEvaluator = fitnessEvaluator;
	}	
	
}
