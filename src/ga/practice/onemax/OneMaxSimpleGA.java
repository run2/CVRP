package ga.practice.onemax;

import ga.core.CommonGA;
import ga.core.GeneGenerator;
import ga.core.Population;
import ga.core.RunGA;
import ga.core.evaluate.FitnessAndCost;
import ga.core.evaluate.FitnessAndCostHistoryWrapper;
import ga.core.select.SUSSelector;
import ga.core.terminate.OptimalFitness;
import ga.practice.onemax.chromosome.BinaryStringChromosome;
import ga.practice.onemax.evaluate.OneMaxFitness;
import ga.practice.onemax.gene.BooleanGeneGenerator;
import ga.practice.onemax.reproduce.BinaryFlipMutation;
import ga.practice.onemax.reproduce.TwoPointBinaryCrossOver;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Copyright 2013 Academic Free License 3.0
 */

public class OneMaxSimpleGA extends CommonGA{

	//private static final Logger logger = Logger.getLogger(OneMaxSimpleGA.class.getCanonicalName());

	// default values
	@SuppressWarnings("rawtypes")
	GeneGenerator bGeneGenerator = null;
	private Population<BinaryStringChromosome> population;
	public OneMaxSimpleGA()throws Exception{

		CommonGA.logger.info("Starting OneMaxSimpleGA");
		CommonGA.logger.info("Reading properties");
		readProperties("properties/onemax.properties");
	}
	
	public Population<BinaryStringChromosome> getFinalPopulation(){
		return population;
	}
	@SuppressWarnings("unchecked")
	public void doGA(){
		RunGA<BinaryStringChromosome> ga = new RunGA<BinaryStringChromosome>(population, runningPopulationSize,runningPopulationSize,-1,0,1,bGeneGenerator);
		//ga.setTermination(new Iterations<BinaryStringChromosome>(maxIterations));
		FitnessAndCost<BinaryStringChromosome> evaluator = new OneMaxFitness();
		FitnessAndCostHistoryWrapper<BinaryStringChromosome> cachedFitness = new FitnessAndCostHistoryWrapper<BinaryStringChromosome>(evaluator);
		ga.setTermination(new OptimalFitness<BinaryStringChromosome>(optimailFitnessValue, cachedFitness));
		ga.setFitnessEvaluator(cachedFitness);
		ga.setSelector(new SUSSelector<BinaryStringChromosome>(cachedFitness));
		ga.addReproductionOperators(new TwoPointBinaryCrossOver(probabilityToCrossOver));
		ga.addReproductionOperators(new BinaryFlipMutation(probabilityToMutate));
		ga.run();
	}
	@SuppressWarnings("unchecked")
	public void getInitialPopulation(){
		bGeneGenerator = new BooleanGeneGenerator();
		population = BinaryStringChromosome.generatePopulationOfRandomBinaryStringChromosomes(bGeneGenerator, individualChromosomeLength, runningPopulationSize);
	}

	
	public static void main(String args[]){
		try{
			OneMaxSimpleGA onemax = new OneMaxSimpleGA();
			onemax.setUpLogging();
			onemax.getInitialPopulation();
			onemax.doGA();
			
		}catch(Exception e){
			e.printStackTrace();
			logger.severe(e.getMessage());
		}
	}

}
