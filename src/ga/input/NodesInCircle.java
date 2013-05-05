package ga.input;

import ga.core.Node;

import java.util.ArrayList;
import java.util.List;

public class NodesInCircle {
	public static List<Node> nodes = new ArrayList<Node>();
	public static Node startDepo;
	public static Node endDepo;
	public static int noOfDepos ;

	/*
	 * nodes.add(new Node("A",10,20)); nodes.add(new Node("B",5,3));
	 * nodes.add(new Node("C",14,45)); nodes.add(new Node("X",0,6));
	 * nodes.add(new Node("E",23,1)); nodes.add(new Node("F",11,7));
	 * nodes.add(new Node("M",17,20)); nodes.add(new Node("N",51,3));
	 * nodes.add(new Node("O",1,45)); nodes.add(new Node("P",10,6));
	 * nodes.add(new Node("Q",20,1)); nodes.add(new Node("R",11,17));
	 */
	static {
		nodes.add(new Node("A", 300.0, 200.0));
		nodes.add(new Node("B", 286.6, 250));
		nodes.add(new Node("C", 250, 286.6));
		nodes.add(new Node("X", 200, 300));
		nodes.add(new Node("E", 150, 286.6));
		nodes.add(new Node("F", 113.39, 250));
		nodes.add(new Node("M", 100, 200));
		nodes.add(new Node("N", 113.39, 150));
		nodes.add(new Node("O", 150, 113.39));
		nodes.add(new Node("P", 200, 100));
		nodes.add(new Node("Q", 250, 113.39));
		nodes.add(new Node("R", 286.6, 150));
		nodes.add(new Node("D1", 200, 200));
		nodes.add(new Node("D2", 200, 200));

		//nodes.add(new Node("Z1", 200, 200));
		startDepo = new Node("D", 200, 200);
		startDepo.setPos("S");
		endDepo = new Node("D", 200, 200);
		endDepo.setPos("E");

		
	}

}
