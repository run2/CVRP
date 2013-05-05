

package ga.cvrp;

import ga.core.Chromosome;
import ga.core.CommonGA;
import ga.core.GeneGenerator;
import ga.core.Node;
import ga.core.Population;
import ga.core.RunGA;
import ga.core.evaluate.FitnessAndCost;
import ga.core.evaluate.FitnessAndCostHistoryWrapper;
import ga.core.select.SUSSelector;
import ga.core.terminate.Iterations;
import ga.cvrp.chromosomes.CVRPChromosomeWithNodeGene;
import ga.cvrp.evaluate.RouteDistanceFitnessForNodeGene;
import ga.cvrp.gene.RandomNodeGeneGenerator;
import ga.cvrp.reproduce.SCCEdgeCrossOverWithNodeGenes;
import ga.cvrp.reproduce.ShuffleTour;
import ga.input.Input;
import poi.cvrp.PlotIt;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * GA to run TSP
 * Copyright 2013 Academic Free License 3.0 
 */
public class FruityBunTSP extends CommonGA{
	private Population<CVRPChromosomeWithNodeGene> population;
	private static boolean plot = false;
	@SuppressWarnings("rawtypes")
	GeneGenerator pathGeneGenerator  = null;
	public FruityBunTSP()throws Exception{
		//readProperties("properties/tspnodes");
	}
	@SuppressWarnings("unchecked")
	public void getInitialPopulation(){
		pathGeneGenerator = new RandomNodeGeneGenerator(Input.nodes);
		population = CVRPChromosomeWithNodeGene.generatePopulationOfRandomPathChromosomes(pathGeneGenerator, runningPopulationSize,Input.nodes.size() ,Input.startDepo, Input.endDepo);
		//logger.info(population.toString());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public void doGA(){
		
		RunGA<CVRPChromosomeWithNodeGene> ga = new RunGA<CVRPChromosomeWithNodeGene>(population,runningPopulationSize,runningPopulationSize,50,1,1,pathGeneGenerator);
		ga.setPlotit(plotit);
		
		FitnessAndCost<CVRPChromosomeWithNodeGene> evaluator = new RouteDistanceFitnessForNodeGene();
		FitnessAndCostHistoryWrapper<CVRPChromosomeWithNodeGene> fitnesssHistoryWrapper = new FitnessAndCostHistoryWrapper<CVRPChromosomeWithNodeGene>(evaluator);
		ga.setTermination(new Iterations<CVRPChromosomeWithNodeGene>(maxIterations));
		//ga.setTermination(new OptimalFitness<>(0.00125156, fitnesssHistoryWrapper) );
		ga.setFitnessEvaluator(fitnesssHistoryWrapper);
		ga.setSelector(new SUSSelector<CVRPChromosomeWithNodeGene>(fitnesssHistoryWrapper));
		ga.addReproductionOperators(new SCCEdgeCrossOverWithNodeGenes(probabilityToCrossOver));
		
		// Will not work with ShuffleSubTours!!!!!!!!!!!!!!!!!!!!!!
		ga.addReproductionOperators(new ShuffleTour(probabilityToMutate));
		ga.run();
		//logger.info("Final population" + population.toString());
		logger.info("Best solution at iteration " + RunGA.bestChromosomeAtIteration );
		logger.info("Best solutions " + RunGA.bestChromosomes.toString());
		
		try {
			FruityBunCVRP cvrp = new FruityBunCVRP();
			Input.initializeFruityBunCVRP();
			cvrp.setInitialPopulationFromTSP(RunGA.bestChromosomes);
			ga.cleanUp();
			population.dispose();
			cvrp.readProperties("cvrpnodes-real");
			cvrp.setUpLogging();
			cvrp.doGA();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*int bestSize = RunGA.bestChromosomes.size();
		for(int i = 0 ; i < bestSize - 2 ; i ++){
			RunGA.bestChromosomes.poll();
		}
		Chromosome c = RunGA.bestChromosomes.poll();
		//File bestSolution = new File("best-solution.txt");
		for(int j = 0 ; j < c.getSize() ; j ++){// loop through nodes
			if(((Node)c.getGeneAt(j)).getCodeName().startsWith("D")){
				System.out.println();
			}
			System.out.println(((Node)c.getGeneAt(j)).getxCoordinate() + "\t" + ((Node)c.getGeneAt(j)).getyCoordinate());
		}
		*/
	}
	public static void main(String args[]){
		try{
			Input.initializeFruityBunTSP();
			FruityBunTSP tsp = new FruityBunTSP();
			if(plot){
				String [] headers = {"Iteration", "Cost",  "Time"};
				String [] dataTypes = {"0", "0.000",  "hh:mm:ss"};
				tsp.plotit = new PlotIt(headers,dataTypes,"TSP.xls");
				tsp.plotit.initialize();
			}
			tsp.readProperties("tspnodes1");
			
			
			tsp.setUpLogging();
			//tsp.test();
			tsp.getInitialPopulation();
			if(plot)
				tsp.plotit.setRun(1);
			
			tsp.doGA();
			if(plot){
				tsp.plotit.logImprovement();
				tsp.plotit.close();
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			logger.severe(e.getMessage());
		}
	}
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
/*
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doGA() throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException{
		
		RunGA<CVRPChromosomeWithNodeGene> ga = new RunGA<CVRPChromosomeWithNodeGene>(population,runningPopulationSize,runningPopulationSize,resetOnIterations,optimizeOnReset);
		FitnessAndCost<CVRPChromosomeWithNodeGene> evaluator = new RouteDistanceFitnessForNodeGene();
		FitnessAndCostHistoryWrapper<CVRPChromosomeWithNodeGene> fitnesssHistoryWrapper = new FitnessAndCostHistoryWrapper<CVRPChromosomeWithNodeGene>(evaluator);
		ga.setTermination(new Iterations<CVRPChromosomeWithNodeGene>(maxIterations));
		//ga.setTermination(new OptimalFitness<>(0.00125156, fitnesssHistoryWrapper) );
		ga.setFitnessEvaluator(fitnesssHistoryWrapper);
		
		
		Class<CVRPChromosomeWithNodeGene> selectorClass = (Class<CVRPChromosomeWithNodeGene>) Class.forName("ga.core.select.SUSSelector");
		Constructor selectorConstructor =  selectorClass.getConstructor(new Class[]{FitnessAndCost.class});
		Operator<CVRPChromosomeWithNodeGene> selectorInstance = (Operator<CVRPChromosomeWithNodeGene>) selectorConstructor.newInstance(fitnesssHistoryWrapper);
		ga.setSelector(selectorInstance);
		//ga.setSelector(new SUSSelector<CVRPChromosomeWithNodeGene>(fitnesssHistoryWrapper));
		
		Class<CVRPChromosomeWithNodeGene> crossOverClass = (Class<CVRPChromosomeWithNodeGene>) Class.forName("ga.cvrp.reproduce.SCXEdgeCrossOverWithNodeGenesAndDemand");
		Constructor crossOverClassConstructor =  crossOverClass.getConstructor(new Class[]{ double.class,boolean.class});
		Operator<CVRPChromosomeWithNodeGene> crossOverInstance = (Operator<CVRPChromosomeWithNodeGene>) crossOverClassConstructor.newInstance(new Double(probabilityToCrossOver),new Boolean(false));
		ga.addReproductionOperators(crossOverInstance);
		//ga.addReproductionOperators(new SCXEdgeCrossOverWithNodeGenesAndDemand(probabilityToCrossOver,false));

		Class<CVRPChromosomeWithNodeGene> mutationClass = (Class<CVRPChromosomeWithNodeGene>) Class.forName("ga.cvrp.reproduce.ShuffleSubTours");
		Constructor mutationClassConstructor =  mutationClass.getConstructor(new Class[]{ double.class});
		Operator<CVRPChromosomeWithNodeGene> mutationInstance = (Operator<CVRPChromosomeWithNodeGene>) mutationClassConstructor.newInstance(probabilityToMutate);
		ga.addReproductionOperators(mutationInstance);
		
		//ga.addReproductionOperators(new ShuffleSubTours(probabilityToMutate));
		ga.run();
		//logger.info("Final population" + population.toString());

		//logger.info("Best solution at iteration " + RunGA.bestChromosomeAtIteration );
		//logger.info("Best solutions " + RunGA.bestChromosomes.toString());
		
		int bestSize = RunGA.bestChromosomes.size();
		for(int i = 0 ; i < bestSize - 1 ; i ++){
			RunGA.bestChromosomes.poll();
		}
		Chromosome c = RunGA.bestChromosomes.poll();
		double bestCost = c.getActualCost();

		System.out.println("login db12196");
		System.out.println("cost " + bestCost);
		if(c instanceof CVRPChromosomeWithNodeGene)
			System.out.println(((CVRPChromosomeWithNodeGene)c).printSubTours());
		
		
	}
 */
}
