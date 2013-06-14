package ga.cvrp;

import ga.core.Chromosome;
import ga.core.CommonGA;
import ga.core.GeneGenerator;
import ga.core.Operator;
import ga.core.Population;
import ga.core.RunGA;
import ga.core.evaluate.FitnessAndCost;
import ga.core.evaluate.FitnessAndCostHistoryWrapper;
import ga.core.terminate.Iterations;
import ga.cvrp.chromosomes.CVRPChromosomeWithNodeGene;
import ga.cvrp.evaluate.RouteDistanceFitnessForNodeGene;
import ga.cvrp.gene.RandomNodeGeneGenerator;
import ga.input.Input;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * GA to run cvrp 
 * Copyright 2013 Academic Free License 3.0
 */
public class FruityBunCVRP extends CommonGA{
	private Population<CVRPChromosomeWithNodeGene> population;
	private static int totalRunsPerConfiguration = 1;
	private static int totalConfigs = 1;
	private static boolean plot = false;
	@SuppressWarnings("rawtypes")
	GeneGenerator pathGeneGenerator = null;
	public FruityBunCVRP()throws Exception{
		
	}
	@SuppressWarnings("unchecked")
	public void getInitialPopulation(){
		pathGeneGenerator = new RandomNodeGeneGenerator(Input.nodes);
		population = CVRPChromosomeWithNodeGene.generatePopulationOfRandomPathChromosomes(pathGeneGenerator, runningPopulationSize,Input.nodes.size() ,Input.startDepo, Input.endDepo);
	}
	public void setInitialPopulationFromTSP(ArrayBlockingQueue<Chromosome> chromosomes){
		List newChromosomes = new ArrayList<CVRPChromosomeWithNodeGene>(chromosomes.size()); 
		chromosomes.drainTo(newChromosomes);
		for(int i = 0 ; i < newChromosomes.size() ; i ++){
			newChromosomes.set(i, CVRPChromosomeWithNodeGene.convertToNodeWithDemanChromosome((CVRPChromosomeWithNodeGene)newChromosomes.get(i)));
		}
		//pathGeneGenerator = new RandomNodeGeneGenerator(Input.nodes);
		population = new Population(newChromosomes);
		pathGeneGenerator = new RandomNodeGeneGenerator(Input.nodes);
	}	
	public static void main(String args[]){
		FruityBunCVRP cvrp = null;
		try{
			Input.initializeFruityBunCVRP();
			cvrp = new FruityBunCVRP();
			if(plot){
				//String [] headers = {"Iteration", "Cost",  "Time"};
				//String [] dataTypes = {"0", "0.000",  "hh:mm:ss"};
				//cvrp.plotit = new PlotIt(headers,dataTypes,"CVRP.xls");
				//cvrp.plotit.initialize();
			}
			
			int configCounter = 1;
			while(configCounter<=totalConfigs){
				cvrp.readProperties("cvrpnodes-real");
				//cvrp.readProperties(args[0]);
				cvrp.setUpLogging();
				int run = 1;
				while(run <= totalRunsPerConfiguration){

					//if(plot)
						//cvrp.plotit.setRun(run);
					
					cvrp.getInitialPopulation();
					cvrp.doGA();
					run++;
					
					//System.out.println("Completed config " + configCounter + " run " + run);
				}
				//if(totalRunsPerConfiguration>1 && plot)
					//cvrp.plotit.logImprovement();
				configCounter++;
			}
			//if(plot)
				//cvrp.plotit.close();
			
		}catch(Exception e){
			//if(plot)
				//cvrp.plotit.close();
			e.printStackTrace();
			logger.severe(e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doGA() throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
		
		RunGA<CVRPChromosomeWithNodeGene> ga = new RunGA<CVRPChromosomeWithNodeGene>(population,runningPopulationSize,runningPopulationSize,resetOnIterations,optimizeOnReset,skipSelectionOnReset,pathGeneGenerator);
		//ga.setPlotit(plotit);
		FitnessAndCost<CVRPChromosomeWithNodeGene> evaluator = new RouteDistanceFitnessForNodeGene();
		FitnessAndCostHistoryWrapper<CVRPChromosomeWithNodeGene> fitnesssHistoryWrapper = new FitnessAndCostHistoryWrapper<CVRPChromosomeWithNodeGene>(evaluator);
		ga.setTermination(new Iterations<CVRPChromosomeWithNodeGene>(maxIterations));
		ga.setFitnessEvaluator(fitnesssHistoryWrapper);
		
		
		Class<CVRPChromosomeWithNodeGene> selectorClass = (Class<CVRPChromosomeWithNodeGene>) Class.forName(selectorClassName);
		Constructor selectorConstructor =  selectorClass.getConstructor(new Class[]{FitnessAndCost.class});
		Operator<CVRPChromosomeWithNodeGene> selectorInstance = (Operator<CVRPChromosomeWithNodeGene>) selectorConstructor.newInstance(fitnesssHistoryWrapper);
		ga.setSelector(selectorInstance);
		
		Class<CVRPChromosomeWithNodeGene> crossOverClass = (Class<CVRPChromosomeWithNodeGene>) Class.forName(crossoverClassName);
		Constructor crossOverClassConstructor =  crossOverClass.getConstructor(new Class[]{ double.class});
		Operator<CVRPChromosomeWithNodeGene> crossOverInstance = (Operator<CVRPChromosomeWithNodeGene>) crossOverClassConstructor.newInstance(new Double(probabilityToCrossOver));
		ga.addReproductionOperators(crossOverInstance);

		Class<CVRPChromosomeWithNodeGene> mutationClass = (Class<CVRPChromosomeWithNodeGene>) Class.forName(mutationClassName);
		Constructor mutationClassConstructor =  mutationClass.getConstructor(new Class[]{ double.class});
		Operator<CVRPChromosomeWithNodeGene> mutationInstance = (Operator<CVRPChromosomeWithNodeGene>) mutationClassConstructor.newInstance(probabilityToMutate);
		ga.addReproductionOperators(mutationInstance);
		
		long startTime = System.nanoTime();
		ga.run();
		long endTime = System.nanoTime();
		long duration = endTime - startTime;		

		logger.info("Best solution at iteration " + RunGA.bestChromosomeAtIteration );
		logger.info("Best solutions " + RunGA.bestChromosomes.toString());
		logger.info("Duration in seconds " + duration);
		
		int bestSize = RunGA.bestChromosomes.size();
		for(int i = 0 ; i < bestSize - 1 ; i ++){
			RunGA.bestChromosomes.poll();
		}
		Chromosome c = RunGA.bestChromosomes.poll();
		double bestCost = c.getActualCost();

		//for(int j = 0 ; j < c.getSize() ; j ++){// loop through nodes
			//if(((Node)c.getGeneAt(j)).getCodeName().startsWith("D")){
				//System.out.println();
			//}
			//System.out.println(((Node)c.getGeneAt(j)).getxCoordinate() + "\t" + ((Node)c.getGeneAt(j)).getyCoordinate());
		//}
		
		//System.out.println("login ******");
		System.out.println("cost " + bestCost);
		System.out.println("duration " + duration);
		if(c instanceof CVRPChromosomeWithNodeGene)
			System.out.println(((CVRPChromosomeWithNodeGene)c).printSubTours());
		
		ga.cleanUp();
		population.dispose();
	}
	
	/*
	@SuppressWarnings({ "rawtypes"})
	public void doGA(){
		
		RunGA<CVRPChromosomeWithNodeGene> ga = new RunGA<CVRPChromosomeWithNodeGene>(population,runningPopulationSize,runningPopulationSize,50,1);
		FitnessAndCost<CVRPChromosomeWithNodeGene> evaluator = new RouteDistanceFitnessForNodeGene();
		FitnessAndCostHistoryWrapper<CVRPChromosomeWithNodeGene> fitnesssHistoryWrapper = new FitnessAndCostHistoryWrapper<CVRPChromosomeWithNodeGene>(evaluator);
		ga.setTermination(new Iterations<CVRPChromosomeWithNodeGene>(maxIterations));
		//ga.setTermination(new OptimalFitness<>(0.00125156, fitnesssHistoryWrapper) );
		ga.setFitnessEvaluator(fitnesssHistoryWrapper);
		ga.setSelector(new SUSSelector<CVRPChromosomeWithNodeGene>(fitnesssHistoryWrapper));
		ga.addReproductionOperators(new SCXEdgeCrossOverWithNodeGenesAndDemand(probabilityToCrossOver));
		ga.addReproductionOperators(new ShuffleSubTours(probabilityToMutate));
		ga.run();
		//logger.info("Final population" + population.toString());
		logger.info("Best solution at iteration " + RunGA.bestChromosomeAtIteration );
		logger.info("Best solutions " + RunGA.bestChromosomes.toString());
		int bestSize = RunGA.bestChromosomes.size();
		for(int i = 0 ; i < bestSize - 2 ; i ++){
			RunGA.bestChromosomes.poll();
		}
		Chromosome c = RunGA.bestChromosomes.poll();
		//File bestSolution = new File("best-solution.txt");
		for(int j = 0 ; j < c.getSize() ; j ++){// loop through nodes
			if(((Node)c.getGeneAt(j)).getCodeName().startsWith("D")){
				System.out.println();
			}
			//System.out.println(((Node)c.getGeneAt(j)).getxCoordinate() + "\t" + ((Node)c.getGeneAt(j)).getyCoordinate());
		}
	}
	*/
	/*
	public static void test(){
		//DoublePathFlipMutation dpfm = new DoublePathFlipMutation(1);
		//dpfm.operate(population);
		//SCXPathCrossOver scx = new SCXPathCrossOver(1);
		//scx.operate(population);
		//PathFlipMutation dpfm = new PathFlipMutation(1);
		//dpfm.operate(population);
		
		Node[] genes = new Node[14];
		genes[0] = Input.startDepo;
		genes[1] = Input.nodes.get(0);
		genes[2] = Input.nodes.get(1);
		genes[3] = Input.nodes.get(2);
		genes[4] = Input.nodes.get(3);
		genes[5] = Input.nodes.get(4);
		genes[6] = Input.nodes.get(5);
		genes[7] = Input.nodes.get(6);
		genes[8] = Input.nodes.get(7);
		genes[9] = Input.nodes.get(8);
		genes[10] = Input.nodes.get(9);
		genes[11] = Input.nodes.get(10);
		genes[12] = Input.nodes.get(11);
		genes[13] =	Input.endDepo;
		
		CVRPChromosomeWithNodeGene c = new CVRPChromosomeWithNodeGene(genes);
		System.out.println(c.toString());
		

	}
*/
}
