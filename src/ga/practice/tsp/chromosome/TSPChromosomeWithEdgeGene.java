package ga.practice.tsp.chromosome;

import ga.core.Chromosome;
import ga.core.GeneGenerator;
import ga.core.Node;
import ga.core.Population;
import ga.practice.tsp.gene.EdgeGene;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * A TSP chromosome with Edges as genes
 * Copyright 2013 Academic Free License 3.0
 */
public class TSPChromosomeWithEdgeGene extends Chromosome<EdgeGene> implements Serializable{

	private static final long serialVersionUID = 5099034235023333158L;
	public TSPChromosomeWithEdgeGene(EdgeGene[] genes) {
		super(genes);
		// TODO Auto-generated constructor stub
	}
	public TSPChromosomeWithEdgeGene(EdgeGene[] genes,String purpose) {
		super(genes,purpose);
		// TODO Auto-generated constructor stub
	}	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder chromosome = new StringBuilder();
		if(isCostDirty){
			getActualCost();
		}
		for (EdgeGene path : genes) {
			chromosome.append(path.toString());
		}
		chromosome.append(" " + cost);
		chromosome.append("\n");
		return chromosome.toString();			
	}
	
	@Override
	public void mutateGene(int i) {
		// TODO Auto-generated method stub
		Node temp;
		if(i==0){// mutating the way to start with the way to end
			temp = genes[0].getNodeTo();
			for(int j = 0; j < genes.length; j ++){
				if(j<genes.length-2)
					genes[j].setNodeTo(genes[j+1].getNodeTo());
				if(j==genes.length-2)
					genes[j].setNodeTo(temp);
				if(j>0 && j < genes.length-1)
					genes[j].setNodeFrom(genes[j+1].getNodeFrom());
				if(j==genes.length-1)
					genes[j].setNodeFrom(temp);
			}
		}else if(i==genes.length-1){// mutating back to front
			temp = genes[genes.length-1].getNodeFrom();
			for(int j = genes.length-1; j >= 0; j --){
				if(j>1)
					genes[j].setNodeFrom(genes[j-1].getNodeFrom());
				if(j==1)
					genes[j].setNodeFrom(temp);
				if(j<=genes.length-2 && j > 0)
					genes[j].setNodeTo(genes[j-1].getNodeTo());
				if(j==0)
					genes[j].setNodeTo(temp);
			}
		}else{
			EdgeGene path = genes[i];
			EdgeGene prevPath = genes[i-1];
			EdgeGene nextPath = genes[i+1];
			EdgeGene mutatedPath = new EdgeGene();
			prevPath.setNodeTo(path.getNodeTo());
			nextPath.setNodeFrom(path.getNodeFrom());
			mutatedPath.setNodeFrom(path.getNodeTo());
			mutatedPath.setNodeTo(path.getNodeFrom());
			genes[i] = mutatedPath;
		}
	}
	// assuming j > i and having a min distance of 3 nodes between them
	// also assuming that it does not affect the last and start genes 
	// so that the terminal nodes are not flipped
	public void mutateGene(int i,int j) {
		// TODO Auto-generated method stub
		//EdgeGene[] copyGene = genes.clone();
		EdgeGene path1 = genes[i-1];
		EdgeGene nexTToPath1 = genes[i];
		EdgeGene path2 = genes[j-1];
		EdgeGene nextTToPath2 = genes[j];
		EdgeGene prevToPath2 = genes[j-2];
		Node node1 = genes[i-1].getNodeTo();
		Node node2 = genes[j-1].getNodeTo();
		
		path1.setNodeTo(node2);
		nexTToPath1.setNodeFrom(node2);
		path2.setNodeTo(node1);
		nextTToPath2.setNodeFrom(node1);
		
		if(j-i==4){
			mutateGene(i+2);
		}else{
			EdgeGene nexTToNextToPath1 = genes[i+1];
			Node temp = nexTToPath1.getNodeTo();
			nexTToPath1.setNodeTo(path2.getNodeFrom());
			nexTToNextToPath1.setNodeFrom(nexTToPath1.getNodeTo());
			path2.setNodeFrom(temp);
			prevToPath2.setNodeTo(temp);
			
		}
		
		
	}	
	@Override
	public boolean isValid(){
		for (int i = 0 ; i < genes.length ; i ++){
			if(genes[i].getNodeFrom().getCodeName().startsWith("D") && genes[i].getNodeTo().getCodeName().startsWith("D")){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public double getActualCost() {
		// TODO Auto-generated method stub
		if(isCostDirty){
			cost = 0;
			for (int i = 0 ; i < this.getGenes().length ; i ++){
				cost+= Node.getEuclidianDistance(this.getGenes()[i].getNodeFrom() , this.getGenes()[i].getNodeTo());
			}
			isCostDirty = false;
		}
		return cost;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public Chromosome flip() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void optimize() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void chop() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public double getFitness() {
		// TODO Auto-generated method stub
		if(isFitnessDirty){
			fitness = 0;
			for (int i = 0 ; i < this.getGenes().length ; i ++){
				if(this.getGenes()[i].getNodeFrom().getCodeName().startsWith("D") && this.getGenes()[i].getNodeTo().getCodeName().startsWith("D")){
					fitness += Double.MAX_VALUE;
				}else{
					fitness += Node.getEuclidianDistance(this.getGenes()[i].getNodeFrom(), this.getGenes()[i].getNodeTo());
				}
			}
			isFitnessDirty = false;
		}
		return fitness;
	}

	

	public static TSPChromosomeWithEdgeGene generateRandomChromosome(final GeneGenerator<EdgeGene> geneGenerator,int noOfNodes, Node startNode, Node endNode) {

		EdgeGene[] genes = new EdgeGene[noOfNodes + 1];
		genes[0] = new EdgeGene();
		
		genes[0].setNodeFrom(startNode);
		for (int j = 0; j < noOfNodes; j++) {
			genes[j+1] = geneGenerator.getNewRandomGene();
			genes[j].setNodeTo(genes[j+1].getNodeFrom());
		}
		genes[noOfNodes].setNodeTo(endNode);
		
		return new TSPChromosomeWithEdgeGene(genes);
	}

	public static Population<TSPChromosomeWithEdgeGene> generatePopulationOfRandomPathChromosomes(
			final GeneGenerator<EdgeGene> geneGenerator,
			final int noOfChromosomes, final int noOfInternalNodes, final Node startNode , final Node endNode) {
		
		List<TSPChromosomeWithEdgeGene> chromosomes = new ArrayList<TSPChromosomeWithEdgeGene>();
		for (int i = 0; i < noOfChromosomes; i++) {
			chromosomes.add(TSPChromosomeWithEdgeGene.generateRandomChromosome(geneGenerator, noOfInternalNodes, startNode, endNode)) ;
		}
		return new Population<TSPChromosomeWithEdgeGene>(chromosomes);
	}
	
}
