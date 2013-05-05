package ga.practice.tsp.gene;

import ga.core.GeneGenerator;
import ga.core.Node;

import java.util.List;
import java.util.Random;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Generates random edges from a set of edges
 * Copyright 2013 Academic Free License 3.0
 */
public class RandomEdgeGeneGenerator implements GeneGenerator<EdgeGene> {
	boolean reInitialize = false;
	Random random = new Random(System.nanoTime()); 
	List<Node> nodes;
	//List<Node> nodesCopy;
	int picked = 0;

	public RandomEdgeGeneGenerator(List<Node> nodes){
		this.nodes = nodes;
	}

	@Override
	public void reInitialize() {
		// TODO Auto-generated method stub
		reInitialize = true;

	}

	/** {@inheritDoc} */
	@Override
	public EdgeGene getNewRandomGene() {
		
		int nodeNo;
		if(reInitialize){
			for(int i = 0; i < nodes.size(); i ++)
				nodes.get(i).setPicked(false);
			//this.nodesCopy = new ArrayList<>(nodes);
			reInitialize = false;
			picked = 0;
			random = new Random(System.nanoTime()); 
		}
		Node nodeFrom = null;
		while(nodeFrom== null || nodeFrom.isPicked()){
			nodeNo = random.nextInt(nodes.size());
			nodeFrom = nodes.get(nodeNo); 
		}
		nodeFrom.setPicked(true);
		picked++;
		if(picked==nodes.size())
			reInitialize = true;
		return new EdgeGene(nodeFrom,null);
	}	
}