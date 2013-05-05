package ga.practice.tsp.gene;

import ga.core.Node;
import ga.cvrp.gene.NodeWithDemand;

/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * A gene with two nodes from and to
 * Copyright 2013 Academic Free License 3.0
 */
public class EdgeGene {
	private Node nodeFrom;
	private Node nodeTo;
	public EdgeGene(){
		
	}
	public EdgeGene(Node nodeFrom, Node nodeTo){
		this.nodeFrom = nodeFrom;
		this.nodeTo = nodeTo;
	}
	public Node getNodeTo() {
		return nodeTo;
	}
	public void setNodeFrom(Node node) {
		this.nodeFrom = node;
	}
	public Node getNodeFrom() {
		return nodeFrom;
	}
	public void setNodeTo(Node node) {
		this.nodeTo = node;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof EdgeGene))
			return false;
		EdgeGene compareTo = (EdgeGene)obj;
		return (nodeFrom.equals(compareTo.getNodeFrom()) && nodeTo.equals(compareTo.getNodeTo()));
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return toString().hashCode();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(nodeFrom.getPos().equals("S"))
			return nodeFrom.toString() + "->" + nodeTo.getCodeName();
		else
			return "->" + nodeTo.getCodeName();
	}

	@Override
	public EdgeGene clone() {
		// TODO Auto-generated method stub
		EdgeGene newPath = new EdgeGene((Node)nodeFrom.clone(),(Node)nodeTo.clone());
		return newPath;
	}
	public double getDemandOnDestination(){
		if(nodeTo instanceof NodeWithDemand ){
			return ((NodeWithDemand)nodeTo).getDemand();
		}else
			return -1;
	}
	
}
