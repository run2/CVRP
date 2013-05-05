package ga.input;

import ga.core.Node;
import ga.cvrp.gene.NodeWithDemand;

import java.util.ArrayList;
import java.util.List;

public class NodesInCircleWithDemand {
	public static List<Node> nodes = new ArrayList<Node>();
	public static Node startDepo;
	public static Node endDepo;
	public static int noOfDepos ;
	public static double totalDemand;
	public static double maxCapacityPerTruck = 60;

	/*
	 * nodes.add(new Node("A",10,20)); nodes.add(new Node("B",5,3));
	 * nodes.add(new Node("C",14,45)); nodes.add(new Node("X",0,6));
	 * nodes.add(new Node("E",23,1)); nodes.add(new Node("F",11,7));
	 * nodes.add(new Node("M",17,20)); nodes.add(new Node("N",51,3));
	 * nodes.add(new Node("O",1,45)); nodes.add(new Node("P",10,6));
	 * nodes.add(new Node("Q",20,1)); nodes.add(new Node("R",11,17));
	 */
	static {
		nodes.add(new NodeWithDemand("A", 300.0, 200.0,18));
		nodes.add(new NodeWithDemand("B", 286.6, 250,16));
		nodes.add(new NodeWithDemand("C", 250, 286.6,14));
		nodes.add(new NodeWithDemand("X", 200, 300,9));
		nodes.add(new NodeWithDemand("E", 150, 286.6,11));
		nodes.add(new NodeWithDemand("F", 113.39, 250,13));
		nodes.add(new NodeWithDemand("M", 100, 200,5));
		nodes.add(new NodeWithDemand("N", 113.39, 150,8));
		nodes.add(new NodeWithDemand("O", 150, 113.39,7));
		nodes.add(new NodeWithDemand("P", 200, 100,17));
		nodes.add(new NodeWithDemand("Q", 250, 113.39,15));
		nodes.add(new NodeWithDemand("R", 286.6, 150,11));
		nodes.add(new NodeWithDemand("D1", 200, 200,0));
		nodes.add(new NodeWithDemand("D2", 200, 200,0));

		//nodes.add(new Node("Z1", 200, 200));
		startDepo = new NodeWithDemand("D", 200, 200,0);
		startDepo.setPos("S");
		endDepo = new NodeWithDemand("D", 200, 200,0);
		endDepo.setPos("E");

		noOfDepos = 3;		
		totalDemand = 144;
	}
}
