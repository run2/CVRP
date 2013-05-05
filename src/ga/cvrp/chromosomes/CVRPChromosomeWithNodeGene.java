package ga.cvrp.chromosomes;

import ga.core.Chromosome;
import ga.core.CommonGA;
import ga.core.GeneGenerator;
import ga.core.Node;
import ga.core.Population;
import ga.core.util.MapSort;
import ga.cvrp.gene.NodeWithDemand;
import ga.input.FruityBunNodesWithDemand;
import ga.input.Input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * A CVRP chromosome with Nodes as genes
 * Copyright 2013 Academic Free License 3.0
 */
public class CVRPChromosomeWithNodeGene extends Chromosome<Node> implements Serializable{

	private static final long serialVersionUID = 5099034235023333158L;
	public CVRPChromosomeWithNodeGene(Node[] genes) {
		super(genes);
		// TODO Auto-generated constructor stub
	}
	public CVRPChromosomeWithNodeGene(Node[] genes,String purpose) {
		super(genes,purpose);
		// TODO Auto-generated constructor stub
	}	
	public static CVRPChromosomeWithNodeGene convertToNodeWithDemanChromosome(CVRPChromosomeWithNodeGene chromosome){
		NodeWithDemand[] genes = new NodeWithDemand[chromosome.getGenes().length];
		for(int i = 0 ; i < chromosome.getGenes().length ; i ++){
			int demandId = Integer.valueOf(chromosome.genes[i].getName()) ;
			genes[i] = new NodeWithDemand(chromosome.genes[i].getName(),chromosome.genes[i].getxCoordinate(), chromosome.genes[i].getyCoordinate(),FruityBunNodesWithDemand.demand[demandId]);
			if(genes[i].getName().startsWith("1"))
				genes[i].setCodeName(chromosome.genes[i].getCodeName());
		}
		return new CVRPChromosomeWithNodeGene(genes);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder chromosome = new StringBuilder();
		Node prevNode = null;
		if(isCostDirty){
			getActualCost();
		}
		for (Node node : genes) {
			if(null!=prevNode){
				chromosome.append("->" + node.getName().toString() );
				prevNode = node;
			}else{
				chromosome.append(node.getName().toString());
				prevNode = node;
			}
		}
		chromosome.append(" " + cost);
		chromosome.append("\n");
		return chromosome.toString();			
	}

	@Override
	public void mutateGene(int i) {
		// TODO Auto-generated method stub

		Node temp = genes[i];
		genes[i] = genes[i+1];
		genes[i+1]  = temp;
		while(isZeroFitness()){
			chop();
		}
		isCostDirty = true;
		isFitnessDirty = true;
	}
	// assuming j < i
	public void mutateGene(int i,int j) {
		// TODO Auto-generated method stub
		//EdgeGene[] copyGene = genes.clone();
		Node temp = genes[i];
		genes[i] = genes[j];
		genes[j]  = temp;

		swapGene(i+1, j-1);
		while(isZeroFitness()){
			chop();
		}
		isCostDirty = true;
		isFitnessDirty = true;
	}
	public void chop(){
		int countDepos = 0;
		for(int i = 1 ; i < genes.length -1 ; i ++){
			if(genes[i].getCodeName().startsWith("D")){
					countDepos++;
			}
		}
		Node[] depos = new Node[countDepos];
		for(int i = 1, j= 0 ; i < genes.length -1 ; i ++){
			if(genes[i].getCodeName().startsWith("D")){
				depos[j] = genes[i];
				j ++;
			}
		}

		// distribute the depos
		Node[] newgenes = new Node[genes.length] ;
		newgenes[0] = genes[0];
		newgenes[genes.length-1] = genes[genes.length-1];
		int gap = (genes.length-2)/(countDepos+1);
		int depoCounter = 0;
		
		for(int j = 1 ; j < genes.length -1 ; j ++){
			if(j%gap==0){
				newgenes[j] = depos[depoCounter];
				depoCounter++;
				if(depoCounter==countDepos)
					break;
			}
		}
		int k = 0;
		for(int j = 1 , i = 1 ; j < genes.length -1 ; j ++){
			if(newgenes[j]==null){
				for(k = i ; k < genes.length -1 ; k ++){
					i++;
					if(!genes[k].getCodeName().startsWith("D"))
						break;
				}
				newgenes[j] = genes[k];
			}
		}
		
		genes = newgenes;
	}
	private boolean isZeroFitness(){
		for(int i = 0 ; i < genes.length -1 ; i ++){
			if(genes[i].getCodeName().startsWith("D") && genes[i+1].getCodeName().startsWith("D")){
				CommonGA.logger.info("Gene which will get zero fitness");
				return true;
			}
		}
		return false;
	}
	private void swapGene(int i,int j) {
		// TODO Auto-generated method stub
		Node temp = genes[j];
		genes[j] = genes[i];
		genes[i]  = temp;
	}	
	@Override
	public boolean isValid(){
		//if(! (genes[0] instanceof NodeWithDemand)) return true;
		double runningDemand = 0;
		for (int i = 0 ; i < genes.length ; i ++){
			if(i < genes.length -1 && genes[i].getCodeName().startsWith("D") && genes[i+1].getCodeName().startsWith("D")){
				return false;
			}
			// modify this later with interface
			if(genes[i] instanceof NodeWithDemand){
				if(((Node)genes[i]).getCodeName().startsWith("D")){
					runningDemand = 0;
				}else{
					runningDemand+= ((NodeWithDemand)genes[i]).getDemand();
					if(runningDemand> Input.maxCapacityPerTruck){
						return false;
					}
				}
				
			}
		}
		return true;
	}	
	@Override
	public double getActualCost() {
		// TODO Auto-generated method stub
		if(isCostDirty){
			cost = 0;
			for (int i = 0 ; i < this.getGenes().length - 1; i ++){
				cost+= Node.getEuclidianDistance((Node)this.getGenes()[i], (Node)this.getGenes()[i+1]);
			}
			isCostDirty = false;
		}
		return cost;
	}


	@Override
	public double getFitness() {
		// TODO Auto-generated method stub
		if(isFitnessDirty){
			fitness = 0;
			for (int i = 0 ; i < this.getGenes().length -1 ; i ++){
				if(((Node)this.getGenes()[i]).getCodeName().startsWith("D") && ((Node)this.getGenes()[i+1]).getCodeName().startsWith("D")){
					fitness += Double.MAX_VALUE;
				}else{
					fitness += Node.getEuclidianDistance((Node)this.getGenes()[i], (Node)this.getGenes()[i+1]);
				}
			}
			isFitnessDirty = false;
		}
		return fitness;
	}

	
	@Override
	public void optimize() {
		// TODO Auto-generated method stub
		// get all the different routes
		isCostDirty = true;
		isFitnessDirty = true;
		List<List<Node>> routes = new ArrayList<List<Node>>();
		List<Integer> offSets = new ArrayList<Integer>();
		for(int i = 0 ; i < genes.length - 1; i++){
			if(genes[i].getCodeName().startsWith("D")){
				// new route
				if(routes.size()>0){
					routes.get(routes.size()-1).add(genes[genes.length-1].clone());
				}
				offSets.add(i);
				routes.add(new ArrayList<Node>());
				routes.get(routes.size()-1).add(genes[0].clone());
			}else{
				routes.get(routes.size()-1).add(genes[i]);
			}
			
		}
		if(routes.size()>0){
			routes.get(routes.size()-1).add(genes[genes.length-1].clone());
		}
		
		for(int i = 0 ; i < routes.size() ; i ++){
			optimizeRoute(routes.get(i),offSets.get(i));
		}
	}
	/**
	 * @param nodes
	 * @param offset
	 * Similar to sweep algorithm
	 * http://www.ijest.info/docs/IJEST11-03-10-091.pdf
	 */
	@SuppressWarnings("rawtypes")
	private void getBestRoute(Node[] nodes,int offset){
		double xSum = 0;
		double ySum = 0;
		for(int j = 0; j < nodes.length -1 ; j ++){
			xSum+=nodes[j].getxCoordinate();
			ySum+=nodes[j].getyCoordinate();
		}
		double centroidX = xSum/nodes.length-1;
		double centroidY = ySum/nodes.length-1;
		Map<Node,Double> nodeTheta = new  HashMap<Node,Double>();
		double theta = 0; 
		for(int p = 0 ; p < nodes.length -1 ; p ++){
			theta = Math.atan2((nodes[p].getyCoordinate() - centroidY),(nodes[p].getxCoordinate() - centroidX)) * 180/Math.PI;
			nodeTheta.put(nodes[p], theta);
		}
		nodeTheta = MapSort.sortByValue(nodeTheta);	
		Node[] optimizedNodes = new Node[nodes.length];
		int k = 0;
		for(Map.Entry<Node, Double> entry : nodeTheta.entrySet()) {
			if(k>0 || entry.getKey().getCodeName().equals("D")){
				// start here
				optimizedNodes[k] = entry.getKey();
				k++;
			}
	    }
		for(Map.Entry<Node, Double> entry : nodeTheta.entrySet()) {
			if(entry.getKey().getCodeName().equals("D")){
				break;
			}
			// start here
			optimizedNodes[k] = entry.getKey();
			k++;
	    }
		optimizedNodes[k] =  nodes[nodes.length-1].clone();
		Chromosome newChromosome = new CVRPChromosomeWithNodeGene (optimizedNodes,"S");
		Chromosome oldChromosome = new CVRPChromosomeWithNodeGene (nodes,"S");
		if(newChromosome.getActualCost() < oldChromosome.getActualCost()){
			// modify chromosome
			int j = 1; 
			for(int i = offset + 1 ; i < offset + optimizedNodes.length - 1 ; i++){
				genes[i] = optimizedNodes[j];
				j++;
			}
		}
		//optimizedNodes[k] = optimizedNodes[0].clone();
		
	}
	private void optimizeRoute(List<Node> route,int offset){
		if(route.size() <=4 ) return;
		else{
			Node[] nodes = new Node[route.size()];
			route.toArray(nodes);
			getBestRoute(nodes ,offset);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Chromosome flip() {
		// TODO Auto-generated method stub
		Node[] newGenes = new Node[genes.length];
		for (int i = 1 , j = genes.length - 2; i < genes.length - 1; i ++ , j--){
			newGenes[j] = genes[i];
		}
		newGenes[0] = genes[0];
		newGenes[genes.length-1] = genes[genes.length-1];
		return new CVRPChromosomeWithNodeGene(newGenes);
	}
	public static CVRPChromosomeWithNodeGene generateRandomChromosome(final GeneGenerator<Node> geneGenerator,int noOfNodes, Node startNode, Node endNode) {

		Node[] genes = new Node[noOfNodes + 2];
		genes[0] = startNode;
		for (int j = 1; j < noOfNodes + 1 ; j++) {
			genes[j] = geneGenerator.getNewRandomGene();
		}
		genes[noOfNodes + 1] = endNode;
		
		return new CVRPChromosomeWithNodeGene(genes);
	}

	public static Population<CVRPChromosomeWithNodeGene> generatePopulationOfRandomPathChromosomes(
			final GeneGenerator<Node> geneGenerator,
			final int noOfChromosomes, final int noOfInternalNodes, final Node startNode , final Node endNode) {
		
		List<CVRPChromosomeWithNodeGene> chromosomes = new ArrayList<CVRPChromosomeWithNodeGene>();
		for (int i = 0; i < noOfChromosomes; i++) {
			CVRPChromosomeWithNodeGene c = CVRPChromosomeWithNodeGene.generateRandomChromosome(geneGenerator, noOfInternalNodes, startNode, endNode);
			c.chop();
			chromosomes.add(c) ;
		}
		return new Population<CVRPChromosomeWithNodeGene>(chromosomes);
	}

	public CVRPChromosomeWithNodeGene shuffleTour(){
		isCostDirty = true;
		isFitnessDirty = true;
		List<Node> internalGenes = new ArrayList<Node>();
		List<Node> internalDepos = new ArrayList<Node>();
		int noOfDepos = 0;
		for(int i = 1 ; i < genes.length - 1; i++){
			if(!genes[i].getCodeName().startsWith("D") ){
				internalGenes.add(genes[i]);
			}else{
				internalDepos.add(genes[i]);
				noOfDepos++;
			}
		}		
		Collections.shuffle(internalGenes);
		Node[] newGenes = new Node[genes.length];
		newGenes[0] = genes[0];
		newGenes[genes.length-1] = genes[genes.length-1];
		int interval = internalGenes.size()/noOfDepos;
		for(int i = 0 , j = 1 , k = 0; i < internalGenes.size() ; i++){
			if(i%interval==0 && i > 1 && k < internalDepos.size()){
				newGenes[j] = internalDepos.get(k);
				k++;
				j++;
				newGenes[j] = internalGenes.get(i);
			}else{
				newGenes[j] = internalGenes.get(i);
			}
			j++;
		}
		return new CVRPChromosomeWithNodeGene(newGenes);
	}
	public CVRPChromosomeWithNodeGene shuffleSubTours(){
		isCostDirty = true;
		isFitnessDirty = true;
		Node[] newGenes = new Node[genes.length];
		List<List<Node>> routes = new ArrayList<List<Node>>();
		for(int i = 0 ; i < genes.length ; i++){
			if(genes[i].getCodeName().startsWith("D") ){
				// new route
				newGenes[i] = genes[i].clone();
				if(i<genes.length-1)
					routes.add(new ArrayList<Node>());
			}else{
				routes.get(routes.size()-1).add(genes[i]);
			}
			
		}
		
		for(int i = 0 ; i < routes.size() ; i ++){
			Collections.shuffle(routes.get(i)); 
		}
		try{
			int routeCounter = 0;
			int j = 0;
			for(int i = 0 ; i < genes.length - 1; i++){
				if(newGenes[i]==null){
					newGenes[i] = routes.get(routeCounter).get(j);
					j++;
					if(j==routes.get(routeCounter).size()){
						routeCounter++;
						j = 0;
					}				
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new CVRPChromosomeWithNodeGene(newGenes);
		
	}

	public String printSubTours(){
		StringBuffer retString = new StringBuffer();
		List<List<Node>> routes = new ArrayList<List<Node>>();
		for(int i = 0 ; i < genes.length ; i++){
			if(genes[i].getCodeName().startsWith("D") && i!=genes.length-1){
				routes.add(new ArrayList<Node>());
			}else{
				routes.get(routes.size()-1).add(genes[i]);
			}
			
		}
		
		for(int i = 0 ; i < routes.size(); i++){
			List<Node> nodes = routes.get(i);
			retString.append("1->");
			for(int j = 0 ; j < nodes.size() ; j++){
				retString.append(nodes.get(j).getName()+"->");
			}
			retString.append("1");
			retString.append("\n");
		}
		return retString.toString();
		
	}	
}
