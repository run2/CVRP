package ga.cvrp.gene;

import ga.core.Node;


/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * Same as Node - but with Demand
 * Copyright 2013 Academic Free License 3.0
 */
public class NodeWithDemand extends Node {
	private double demand = 0;
	public NodeWithDemand(String name, double xCoordinate, double yCoordinate) {
		super(name, xCoordinate, yCoordinate);
		// TODO Auto-generated constructor stub
	}
	public NodeWithDemand(String name, double xCoordinate, double yCoordinate, double demand) {
		super(name, xCoordinate, yCoordinate);
		this.demand = demand;
		// TODO Auto-generated constructor stub
	}	
	public double getDemand(){
		return demand;
	}
			
}	
