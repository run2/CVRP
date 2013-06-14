package ga.cvrp.reproduce;

import ga.core.Chromosome;
import ga.core.CommonGA;
import ga.core.Node;
import ga.core.Operator;
import ga.core.Population;
import ga.cvrp.chromosomes.CVRPChromosomeWithNodeGene;
import ga.cvrp.gene.NodeWithDemand;
import ga.input.Input;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;


/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Sequential constructive crossover operator (SCX)
 * http://www.cscjournals.org/csc/manuscript/Journals/IJBB/volume3/Issue6/IJBB-41.pdf
 * Follows the following algorithm
 * 1. Chooses two parents (i and i+1 ) from set of couples, checks if they will mate based on probability.
 *    If the do not mate then they are carried over into the child population
 *    Once a parent is carried over to the child population it is not allowed to be carried
 *    over any more.
 * 2. Once chosen to mate, two children are created using SCX cross over
 * Copyright 2013 Academic Free License 3.0
 */
public class SCCEdgeCrossOverWithNodeGenesAndDemand implements Operator<CVRPChromosomeWithNodeGene>{
	double reproduceProbability;
	//boolean randomStartPoint;
	Node firstChildStartAt = null;
	boolean allowChoosingDepo = true;
	static int loopCounter = 0;
	static int randomStart = 0;
	boolean fourChild;
	public SCCEdgeCrossOverWithNodeGenesAndDemand(double probability){
		this.reproduceProbability = probability;
		if(CommonGA.fourChild==1)
			this.fourChild = true;
		else
			this.fourChild=false;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Population<CVRPChromosomeWithNodeGene> operate(
			Population<CVRPChromosomeWithNodeGene> population) {
		
		Iterator<CVRPChromosomeWithNodeGene> it = population.getAllChromosomes().iterator();
		Population<CVRPChromosomeWithNodeGene> output = new Population<CVRPChromosomeWithNodeGene>();
		Random random = new Random(System.nanoTime());
		SCCEdgeCrossOverWithNodeGenesAndDemand.loopCounter = 0;
		while(it.hasNext()){
			Set<Chromosome> allowedIntoNewPopulation = new HashSet<Chromosome>();
			CVRPChromosomeWithNodeGene parent1 = it.next();
			CVRPChromosomeWithNodeGene parent2 = it.next();
			//double[] cost = new double[4];
			if (random.nextDouble() < reproduceProbability|| (allowedIntoNewPopulation.contains(parent1)||allowedIntoNewPopulation.contains(parent2) )) {
				firstChildStartAt = null;
				CVRPChromosomeWithNodeGene child1 = doSCX(parent1,parent2,random);
				//CommonGA.logger.info("Child 1" + child1.toString());
				output.addChromosome(child1);
				//cost[0] = child1.getActualCost();

				allowChoosingDepo = true;
				CVRPChromosomeWithNodeGene child2 = doSCX(parent2,parent1,random);
				//CommonGA.logger.info("Child 2" +child2.toString());
				output.addChromosome(child2);
				//cost[1] = child2.getActualCost();
		
				if(fourChild){
					firstChildStartAt = null;
					allowChoosingDepo = true;
					CVRPChromosomeWithNodeGene child3 = doSCX((CVRPChromosomeWithNodeGene)parent1.flip(),(CVRPChromosomeWithNodeGene)parent2.flip(),random);
					//CommonGA.logger.info("Child 1" + child1.toString());
					output.addChromosome(child3);
					//cost[2] = child3.getActualCost();
					
					allowChoosingDepo = true;
					CVRPChromosomeWithNodeGene child4 = doSCX((CVRPChromosomeWithNodeGene)parent2.flip(),(CVRPChromosomeWithNodeGene)parent1.flip(),random);
					output.addChromosome(child4);
				}
				//cost[3] = child4.getActualCost();
				
				//Arrays.sort(cost);
				
			}else{
				//CommonGA.logger.info("Couple retained in next generation ");
				
				allowedIntoNewPopulation.add(parent1);
				allowedIntoNewPopulation.add(parent2);
				output.addChromosome(parent1);
				output.addChromosome(parent2);
				
			}
		}
		
		return output;
	}
	
	/**
	 * @param parentNodesInSequence
	 * @param nodesInChild
	 * @param lastNodeInChild
	 * @param currentNodeInParent
	 * @param demand
	 * @return
	 * Chooses the next legitimate node to be carried forward to the child
	 */
	@SuppressWarnings("unused")
	public CVRPChromosomeWithNodeGene doSCX(CVRPChromosomeWithNodeGene parent1,CVRPChromosomeWithNodeGene parent2,Random random){
		// First get the genes between crossOverPoint1 and crossOverPoint2 of Parent1 into the child gene
		Node[] nodesChosenForChild = new Node[parent2.getSize()]; // as many nodes for child
		Node[] parent1Nodes = parent1.getGenes();
		Node[] parent2Nodes = parent2.getGenes();
		double runningDemand = 0;
		double runningTotalDemand = 0;
		nodesChosenForChild[0] = parent1.getGeneAt(0);// start Node
		Node startAt = null;
		int startIndex = 0;
		int noOfRetries = 0;
		if(firstChildStartAt!= null){
			do{
				for(int j = startIndex ; j < parent1Nodes.length -1 ; j ++ ){
					if(parent1Nodes[j].getCodeName().startsWith("D")){
						startIndex = j;
						break;
					}
				}
				startIndex = startIndex + 1;
				startAt = parent1Nodes[startIndex];
			}while(startAt.equals(firstChildStartAt));
		}else{
			if(SCCEdgeCrossOverWithNodeGenesAndDemand.randomStart > 0){
				int startCounter = 0;
				for(int j = 1 ; j < parent1Nodes.length -1 ; j ++ ){
					if(parent1Nodes[j].getCodeName().startsWith("D")){
						startCounter++;
						if(startCounter==SCCEdgeCrossOverWithNodeGenesAndDemand.randomStart){
							startIndex = j ;
							break;
						}
					}
				}
				startIndex = startIndex + 1;
				firstChildStartAt = parent1Nodes[startIndex];
			}else{
				firstChildStartAt = parent1Nodes[1];
				startIndex = 1;
			}
		}
		
		nodesChosenForChild[1] = parent1Nodes[startIndex];

		if(nodesChosenForChild[1] instanceof NodeWithDemand){
			runningDemand = ((NodeWithDemand)nodesChosenForChild[1]).getDemand();
			runningTotalDemand = runningDemand;
		}
		int nodeIndexInCurrentParent = 1;
		int lastNodeInChild = 1;
		int nodeIndexInSecondParent = 0;
		
		Node nextlegitimateNodeFromCurrentParent = null;
		Node nextlegitimateNodeFromSecondParent = null;
		
	
		Node[] currentParent = parent1Nodes;
		Node[] secondParent = parent2Nodes;
		int noOfDeposIncluded = 1;
		while(lastNodeInChild<nodesChosenForChild.length-2){
			if((Input.totalNoOfDeposOrNoOfVehicles-noOfDeposIncluded)*Input.maxCapacityPerTruck < (Input.totalDemand-runningTotalDemand)){
				allowChoosingDepo = false;
			}else{
				allowChoosingDepo = true;
			}

			nodeIndexInCurrentParent = getPositionOfNodeIn(nodesChosenForChild[lastNodeInChild],currentParent);	
			nextlegitimateNodeFromCurrentParent = getNextLegitimateNodeIn(currentParent,nodesChosenForChild,lastNodeInChild,nodeIndexInCurrentParent,runningDemand,parent1.getSize() -2,true);

			if(!nodesChosenForChild[lastNodeInChild].getCodeName().startsWith("D")){
				nodeIndexInSecondParent = getPositionOfNodeIn(nodesChosenForChild[lastNodeInChild],secondParent);
				nextlegitimateNodeFromSecondParent = getNextLegitimateNodeIn(secondParent,nodesChosenForChild,lastNodeInChild,nodeIndexInSecondParent,runningDemand,parent2.getSize() -2,true);
			}else{
				nextlegitimateNodeFromSecondParent = null;
			}

			// Invalid chromosome formation , try again to get a valid combination
			if(nextlegitimateNodeFromCurrentParent==null){
				
				//randomStartPoint = true;
				firstChildStartAt = null;
				SCCEdgeCrossOverWithNodeGenesAndDemand.loopCounter ++;
				SCCEdgeCrossOverWithNodeGenesAndDemand.randomStart ++;
				if(SCCEdgeCrossOverWithNodeGenesAndDemand.loopCounter>Input.totalNoOfDeposOrNoOfVehicles-1){
					// somethings wrong here // get out by cloning parent
					//System.out.println("Cloning and chopping parent");
					//CVRPChromosomeWithNodeGene n =  (CVRPChromosomeWithNodeGene)parent1.clone();
					SCCEdgeCrossOverWithNodeGenesAndDemand.loopCounter = 0;
					SCCEdgeCrossOverWithNodeGenesAndDemand.randomStart = 0;
					try {
						CVRPChromosomeWithNodeGene child =  (CVRPChromosomeWithNodeGene)parent1.clone();
						//child.chop();
						return child;
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
				}else{
					CVRPChromosomeWithNodeGene n =  doSCX(parent1, parent2, random);
					SCCEdgeCrossOverWithNodeGenesAndDemand.loopCounter = 0;
					SCCEdgeCrossOverWithNodeGenesAndDemand.randomStart = 0;
					return n;
				}
				/*
				System.out.println("Cloning parent");
				firstChildStartAt = null;
				try {
					CVRPChromosomeWithNodeGene n =  (CVRPChromosomeWithNodeGene)parent1.clone();
					SCXEdgeCrossOverWithNodeGenesAndDemand.loopCounter = 0;
					return n;
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				*/
			}

			if( (nextlegitimateNodeFromCurrentParent!=null && nextlegitimateNodeFromSecondParent!=null && Node.getFitnessDistance(nodesChosenForChild[lastNodeInChild],nextlegitimateNodeFromCurrentParent,noOfDeposIncluded) <Node.getFitnessDistance(nodesChosenForChild[lastNodeInChild],nextlegitimateNodeFromSecondParent,noOfDeposIncluded) ) || nextlegitimateNodeFromSecondParent==null ){
				lastNodeInChild++;
				nodesChosenForChild[lastNodeInChild] = nextlegitimateNodeFromCurrentParent;
			}else{
				lastNodeInChild++;
				nodesChosenForChild[lastNodeInChild] = nextlegitimateNodeFromSecondParent;
			}
			
			if(nodesChosenForChild[lastNodeInChild] instanceof NodeWithDemand && nodesChosenForChild[lastNodeInChild].getCodeName().startsWith("D")){
				//runningTotalDemand+= runningDemand;
				runningDemand = 0;
				noOfDeposIncluded++;
				// next node should be anything starting after D
			}else if(nodesChosenForChild[lastNodeInChild] instanceof NodeWithDemand && !nodesChosenForChild[lastNodeInChild].getCodeName().startsWith("D")){
				runningDemand += ((NodeWithDemand)nodesChosenForChild[lastNodeInChild]).getDemand();
				runningTotalDemand+= ((NodeWithDemand)nodesChosenForChild[lastNodeInChild]).getDemand();
			}
					
		}
		lastNodeInChild++;
		nodesChosenForChild[lastNodeInChild]=parent1.getGeneAt(parent1.getGenes().length-1);// last node
		
		return  new CVRPChromosomeWithNodeGene(nodesChosenForChild);
	}
	private Node getNextLegitimateNodeIn(Node[] parentNodesInSequence,Node[] nodesInChild,int lastNodeInChild, int currentNodeInParent,double demand, int lookTill, boolean loop){
		int parentnodeCounter;
		int childnodeCounter;
		Node returnNode = null;
		Node temp = null;
		
		for(parentnodeCounter = currentNodeInParent + 1 ; parentnodeCounter <= lookTill ; parentnodeCounter ++){
			temp = parentNodesInSequence[parentnodeCounter];
			if((temp.getCodeName().startsWith("D") && allowChoosingDepo)|| !temp.getCodeName().startsWith("D")){
				if(temp instanceof NodeWithDemand){
					if (((NodeWithDemand) temp).getDemand() + demand >Input.maxCapacityPerTruck){
						// have to choose a D node or try another node
						temp = null ; // invalid
					}
				}
				if(temp!=null){
					for(childnodeCounter= 1; childnodeCounter <= lastNodeInChild ; childnodeCounter ++ ){
						//if(temp.equals(nodesInChild[childnodeCounter]) || (temp.getName().startsWith("D") && nodesInChild[lastNodeInChild].getName().startsWith("D") )){
						if(temp.equals(nodesInChild[childnodeCounter])){
							temp = null ; // invalid
							break;
						}
					}
				}
				if(temp!=null){
					returnNode = temp;
					break;
				}
			}
		}
		if(loop){
			if(temp==null){//look from start
				returnNode = getNextLegitimateNodeIn(parentNodesInSequence, nodesInChild, lastNodeInChild, 0, demand, currentNodeInParent,false);
			}
			
		}
		return returnNode;
	}
	private int getPositionOfNodeIn(Node node, Node[] nodeSequence){
		for(int nodeCounter=0; nodeCounter < nodeSequence.length ; nodeCounter++ ){
			if(node.equals(nodeSequence[nodeCounter]))
				return nodeCounter; // plus one to accommodate for the start node
		}
		return -1;
	}
	
}
