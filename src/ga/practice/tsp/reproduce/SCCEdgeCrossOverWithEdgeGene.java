package ga.practice.tsp.reproduce;

import ga.core.Chromosome;
import ga.core.CommonGA;
import ga.core.Node;
import ga.core.Operator;
import ga.core.Population;
import ga.practice.tsp.chromosome.TSPChromosomeWithEdgeGene;
import ga.practice.tsp.gene.EdgeGene;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Sequential constructive crossover operator (SCX)
 * http://www.cscjournals.org/csc/manuscript/Journals/IJBB/volume3/Issue6/IJBB-41.pdf
 * Follows the following algorithm
 * 1. Chooses two parents (i and i+1 ) from set of couples and checks if they will mate based on probability.
 *    If the do not mate then they are carried over into the child population
 *    Once a parent is carried over to the child population it is not allowed to be carried
 *    over any more
 * 2. Once chosen to mate, two children are created using SCX cross over
 * Copyright 2013 Academic Free License 3.0
 */
public class SCCEdgeCrossOverWithEdgeGene implements Operator<TSPChromosomeWithEdgeGene>{
	//private static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	//private static prevStartNode;
	double reproduceProbability;
	boolean randomStartPoint;
	Node firstChildStartAt = null;
	public SCCEdgeCrossOverWithEdgeGene(double probability, boolean randomStartPoint){
		this.reproduceProbability = probability;
		this.randomStartPoint = randomStartPoint;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Population<TSPChromosomeWithEdgeGene> operate(
			Population<TSPChromosomeWithEdgeGene> population) {
		// TODO Auto-generated method stub
		Iterator<TSPChromosomeWithEdgeGene> it = population.getAllChromosomes().iterator();
		Population<TSPChromosomeWithEdgeGene> output = new Population<TSPChromosomeWithEdgeGene>();
		Random random = new Random(System.nanoTime());

		while(it.hasNext()){
			Set<Chromosome> allowedIntoNewPopulation = new HashSet<Chromosome>();
			TSPChromosomeWithEdgeGene parent1 = it.next();
			TSPChromosomeWithEdgeGene parent2 = it.next();
			if (random.nextDouble() < reproduceProbability|| (allowedIntoNewPopulation.contains(parent1)||allowedIntoNewPopulation.contains(parent2) )) {
				firstChildStartAt = null;
				TSPChromosomeWithEdgeGene child1 = doSCX(parent1,parent2,random);
				CommonGA.logger.info("Child 1" + child1.toString());
				output.addChromosome(child1);

				TSPChromosomeWithEdgeGene child2 = doSCX(parent2,parent1,random);
				CommonGA.logger.info("Child 2" +child2.toString());
				output.addChromosome(child2);

			}else{
				CommonGA.logger.info("Couple retained in next generation ");
				
				allowedIntoNewPopulation.add(parent1);
				allowedIntoNewPopulation.add(parent2);
				output.addChromosome(parent1);
				output.addChromosome(parent2);
				
			}
		}
		
		return output;
	}
	public Node getNextLegitimateNodeIn(Node[] parentNodesInSequence,Node[] nodesInChild,int lastNodeInChild, int currentNodeInParent){
		//nextPossibleGene = currentParent.getGenes()[geneIndexInCurrentParent+1];
		// check to see if the nodeTo is already in child
		int parentnodeCounter;
		int childnodeCounter;
		Node returnNode = null;
		Node temp = null;
		for(parentnodeCounter = currentNodeInParent + 1 ; parentnodeCounter < parentNodesInSequence.length ; parentnodeCounter ++){
			temp = parentNodesInSequence[parentnodeCounter];
			for(childnodeCounter= 1; childnodeCounter <= lastNodeInChild ; childnodeCounter ++ ){
				//if(temp.equals(nodesInChild[childnodeCounter]) || (temp.getName().startsWith("D") && nodesInChild[lastNodeInChild].getName().startsWith("D") )){
				if(temp.equals(nodesInChild[childnodeCounter])){
					temp = null ; // invalid
					break;
				}
			}
			if(temp!=null){
				returnNode = temp;
				break;
			}
		}
		
		if(temp==null){//look from start
			for(parentnodeCounter = 0 ; parentnodeCounter < parentNodesInSequence.length ; parentnodeCounter ++){
				temp = parentNodesInSequence[parentnodeCounter];
				for(childnodeCounter= 1; childnodeCounter <= lastNodeInChild ; childnodeCounter ++ ){
					if(temp.equals(nodesInChild[childnodeCounter])){
						temp = null ; // invalid
						break;
					}
				}
				if(temp!=null){
					returnNode = temp;
					break;
				}
			}
			
		}
		return returnNode;
	}
	public Node[] splitEdgeGenesIntoNodes(TSPChromosomeWithEdgeGene parent){
		Node[] parentNodes = new Node[parent.getSize()-1];
		int nodeCounter = 0;
		for(int geneCounter = 1 ; geneCounter < parent.getSize() ; geneCounter++){
			parentNodes[nodeCounter] = parent.getGeneAt(geneCounter).getNodeFrom();
			nodeCounter++;
		}
		return parentNodes;
	}
	public TSPChromosomeWithEdgeGene recostructGeneFromEdgeNodes(Node[] childNodes){
		EdgeGene[] childGenes = new EdgeGene[childNodes.length -1] ;
		int nodeCounter = 0;
		int geneCounter = 0;
		for(nodeCounter = 0 ; nodeCounter < childNodes.length - 1 ; nodeCounter++ , geneCounter++){
			childGenes[geneCounter] = new EdgeGene();
			childGenes[geneCounter].setNodeFrom(childNodes[nodeCounter]);
			childGenes[geneCounter].setNodeTo(childNodes[nodeCounter+1]);
		}
		return new TSPChromosomeWithEdgeGene(childGenes);
	}	
	public int getPositionOfNodeIn(Node node, Node[] nodeSequence){
		for(int nodeCounter=0; nodeCounter < nodeSequence.length ; nodeCounter++ ){
			if(node.equals(nodeSequence[nodeCounter]))
				return nodeCounter; // plus one to accommodate for the start node
		}
		return -1;
	}
	public TSPChromosomeWithEdgeGene doSCX(TSPChromosomeWithEdgeGene parent1,TSPChromosomeWithEdgeGene parent2,Random random){
		// First get the genes between crossOverPoint1 and crossOverPoint2 of Parent1 into the child gene
		Node[] nodesChosenForChild = new Node[parent2.getSize()+1]; // as many nodes for child
		Node[] parent1Nodes = splitEdgeGenesIntoNodes(parent1);
		Node[] parent2Nodes = splitEdgeGenesIntoNodes(parent2);
		
		nodesChosenForChild[0] = parent1.getGeneAt(0).getNodeFrom();// start Node
		Node startAt = null;
		int startIndex;
		int noOfRetries = 0;
		do{
			if(randomStartPoint){
				if( firstChildStartAt!= null  ){
					do{
						startIndex = random.nextInt(parent1Nodes.length);
						startAt = parent1Nodes[startIndex];
						noOfRetries++;
					}while(startAt.equals(firstChildStartAt) && noOfRetries <3);
				}else{
					startIndex = random.nextInt(parent1Nodes.length);
					startAt = parent1Nodes[startIndex];
					firstChildStartAt = startAt;
				}
				nodesChosenForChild[1] = parent1Nodes[startIndex]; // Node after start node
			}else{
				nodesChosenForChild[1] = parent1Nodes[0]; // Node after start node in chromosome	
			}
		}while(nodesChosenForChild[1].getCodeName().startsWith("D"));
		
		int nodeIndexInCurrentParent = 1;
		int lastNodeInChild = 1;
		int nodeIndexInSecondParent = 0;
		
		Node nextlegitimateNodeFromCurrentParent = null;
		Node nextlegitimateNodeFromSecondParent = null;
		
		EdgeGene compareToGene = null;
		EdgeGene possibleNextGene = null;
		
		Node[] currentParent = parent1Nodes;
		Node[] secondParent = parent2Nodes;
		int depoCount = 1;
		while(lastNodeInChild<=nodesChosenForChild.length-3){
			nodeIndexInCurrentParent = getPositionOfNodeIn(nodesChosenForChild[lastNodeInChild],currentParent);
			nodeIndexInSecondParent = getPositionOfNodeIn(nodesChosenForChild[lastNodeInChild],secondParent);
			nextlegitimateNodeFromCurrentParent = getNextLegitimateNodeIn(currentParent,nodesChosenForChild,lastNodeInChild,nodeIndexInCurrentParent);
			nextlegitimateNodeFromSecondParent = getNextLegitimateNodeIn(secondParent,nodesChosenForChild,lastNodeInChild,nodeIndexInSecondParent);
			if(null!=nextlegitimateNodeFromCurrentParent)
				possibleNextGene = new EdgeGene(nodesChosenForChild[lastNodeInChild],nextlegitimateNodeFromCurrentParent);
			if(null!=nextlegitimateNodeFromSecondParent)
				compareToGene = new EdgeGene(nodesChosenForChild[lastNodeInChild],nextlegitimateNodeFromSecondParent);
			

			if(( possibleNextGene!=null && compareToGene!=null && Node.getFitnessDistance(nodesChosenForChild[lastNodeInChild],nextlegitimateNodeFromCurrentParent,depoCount)<Node.getFitnessDistance(nodesChosenForChild[lastNodeInChild],nextlegitimateNodeFromSecondParent,depoCount))|| compareToGene==null ){
				lastNodeInChild++;
				nodesChosenForChild[lastNodeInChild] = nextlegitimateNodeFromCurrentParent;
			}else{
				lastNodeInChild++;
				nodesChosenForChild[lastNodeInChild] = nextlegitimateNodeFromSecondParent;
			}
			
			if(nodesChosenForChild[lastNodeInChild].getCodeName().startsWith("D"))
				depoCount++;
					
		}
		lastNodeInChild++;
		nodesChosenForChild[lastNodeInChild]=parent1.getGeneAt(parent1.getGenes().length-1).getNodeTo();// last node
		
		return  recostructGeneFromEdgeNodes(nodesChosenForChild);
	}
}
