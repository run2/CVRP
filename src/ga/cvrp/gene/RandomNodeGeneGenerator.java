package ga.cvrp.gene;

import ga.core.GeneGenerator;
import ga.core.Node;

import java.util.List;
import java.util.Random;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Spits out random nodes from a set of nodes
 * Copyright 2013 Academic Free License 3.0
 */
public class RandomNodeGeneGenerator implements GeneGenerator<Node> {
	boolean reInitialize = false;
	Random random = new Random(System.nanoTime()); 
	List<Node> nodes;
	int picked = 0;
	public RandomNodeGeneGenerator(List<Node> nodes){
		this.nodes = nodes;
		for(int i = 0; i < nodes.size(); i ++)
			nodes.get(i).setPicked(false);

	}

	@Override
	public void reInitialize() {
		// TODO Auto-generated method stub
		reInitialize = true;

	}

	/** {@inheritDoc} */
	@Override
	public Node getNewRandomGene() {
		
		int nodeNo;
		if(reInitialize){
			for(int i = 0; i < nodes.size(); i ++)
				nodes.get(i).setPicked(false);
			//this.nodesCopy = new ArrayList<>(nodes);
			reInitialize = false;
			picked = 0;
			random = new Random(System.nanoTime()); 
		}
		Node node = null;
		while(node== null || node.isPicked()){
			nodeNo = random.nextInt(nodes.size());
			node = nodes.get(nodeNo); 
		}
		node.setPicked(true);
		picked++;
		if(picked==nodes.size())
			reInitialize = true;
		return node;
	}	
}