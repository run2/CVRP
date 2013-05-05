package ga.practice.tsp;

import ga.core.CommonGA;
import ga.core.GeneGenerator;
import ga.core.Population;
import ga.core.RunGA;
import ga.core.evaluate.FitnessAndCost;
import ga.core.evaluate.FitnessAndCostHistoryWrapper;
import ga.core.select.SUSSelector;
import ga.core.terminate.Iterations;
import ga.input.Input;
import ga.practice.tsp.chromosome.TSPChromosomeWithEdgeGene;
import ga.practice.tsp.evaluate.RouteDistanceFitnessWithedgeGene;
import ga.practice.tsp.gene.EdgeGene;
import ga.practice.tsp.gene.RandomEdgeGeneGenerator;
import ga.practice.tsp.reproduce.OneChangeMutationWithEdgeGene;
import ga.practice.tsp.reproduce.SCCEdgeCrossOverWithEdgeGene;
import poi.cvrp.PlotIt;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Copyright 2013 Academic Free License 3.0
 */

public class TSPWithEdgeGene extends CommonGA{
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	private Population<TSPChromosomeWithEdgeGene> population;
	private static boolean plot = true;
	@SuppressWarnings("rawtypes")
	GeneGenerator pathGeneGenerator = null;
	public TSPWithEdgeGene()throws Exception{
		//readProperties("tspnodes2");
	}
	@SuppressWarnings("unchecked")
	public void getInitialPopulation(){
		pathGeneGenerator = new RandomEdgeGeneGenerator(Input.nodes);
		population = TSPChromosomeWithEdgeGene.generatePopulationOfRandomPathChromosomes(pathGeneGenerator, runningPopulationSize,Input.nodes.size() ,Input.startDepo, Input.endDepo);
		logger.info(population.toString());
	}
	
	@SuppressWarnings("unchecked")
	public void doGA(){
		
		RunGA<TSPChromosomeWithEdgeGene> ga = new RunGA<TSPChromosomeWithEdgeGene>(population,runningPopulationSize,runningPopulationSize,resetOnIterations,optimizeOnReset,skipSelectionOnReset,pathGeneGenerator);
		ga.setPlotit(plotit);
		
		FitnessAndCost<TSPChromosomeWithEdgeGene> evaluator = new RouteDistanceFitnessWithedgeGene();
		FitnessAndCostHistoryWrapper<TSPChromosomeWithEdgeGene> fitnesssHistoryWrapper = new FitnessAndCostHistoryWrapper<TSPChromosomeWithEdgeGene>(evaluator);
		ga.setTermination(new Iterations<TSPChromosomeWithEdgeGene>(maxIterations));
		//ga.setTermination(new OptimalFitness<>(0.00125156, fitnesssHistoryWrapper) );
		ga.setFitnessEvaluator(fitnesssHistoryWrapper);
		ga.setSelector(new SUSSelector<TSPChromosomeWithEdgeGene>(fitnesssHistoryWrapper));
		ga.addReproductionOperators(new SCCEdgeCrossOverWithEdgeGene(probabilityToCrossOver,true));
		ga.addReproductionOperators(new OneChangeMutationWithEdgeGene(probabilityToMutate));
		ga.run();
		System.out.println("Final population" + population.toString());
		System.out.println("Best chromosome " + RunGA.bestChromosomeAtIteration);
		System.out.println("Best chromosomes " + RunGA.bestChromosomes);
		ga.cleanUp();
		population.dispose();
		
	}
	public static void main(String args[]){
		try{
			Input.initializeFruityBunTSP();
			TSPWithEdgeGene tsp = new TSPWithEdgeGene();
			if(plot){
				String [] headers = {"Iteration", "Cost",  "Time"};
				String [] dataTypes = {"0", "0.000",  "hh:mm:ss"};
				tsp.plotit = new PlotIt(headers,dataTypes,"TSP.xls");
				tsp.plotit.initialize();
			}
			
			tsp.readProperties("tspedges1");
			tsp.setUpLogging();
			
			int run = 1;
			while(run <= 1){

				if(plot)
					tsp.plotit.setRun(run);
				
				tsp.getInitialPopulation();
				tsp.doGA();
				run++;
				
				//System.out.println("Completed config " + configCounter + " run " + run);
			}
			
			//tsp.test();
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
		
		EdgeGene[] genes = new EdgeGene[13];
		genes[0] = new EdgeGene(Input.startDepo, Input.nodes.get(0));
		genes[1] = new EdgeGene(Input.nodes.get(0), Input.nodes.get(1));
		genes[2] = new EdgeGene(Input.nodes.get(1), Input.nodes.get(2));
		genes[3] = new EdgeGene(Input.nodes.get(2), Input.nodes.get(3));
		genes[4] = new EdgeGene(Input.nodes.get(3), Input.nodes.get(4));
		genes[5] = new EdgeGene(Input.nodes.get(4), Input.nodes.get(5));
		genes[6] = new EdgeGene(Input.nodes.get(5), Input.nodes.get(6));
		genes[7] = new EdgeGene(Input.nodes.get(6), Input.nodes.get(7));
		genes[8] = new EdgeGene(Input.nodes.get(7), Input.nodes.get(8));
		genes[9] = new EdgeGene(Input.nodes.get(8), Input.nodes.get(9));
		genes[10] = new EdgeGene(Input.nodes.get(9), Input.nodes.get(10));
		genes[11] = new EdgeGene(Input.nodes.get(10), Input.nodes.get(11));
		genes[12] = new EdgeGene(Input.nodes.get(11), Input.endDepo);
		
		TSPChromosomeWithEdgeGene c = new TSPChromosomeWithEdgeGene(genes);
		System.out.println(c.toString());
		

	}

}
