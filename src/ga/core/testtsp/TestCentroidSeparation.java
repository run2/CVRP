package ga.core.testtsp;

import ga.core.Node;
import ga.cvrp.chromosomes.CVRPChromosomeWithNodeGene;

import java.util.ArrayList;
import java.util.List;

public class TestCentroidSeparation {
	private static List<Node> nodes = new ArrayList<Node>();
	static {
		
		 /*nodes.add(new Node("1", 40 ,40)); nodes.add(new Node("2", 11 , 28));
		 nodes.add(new Node("3", 6 ,25)); nodes.add(new Node("4", 7 ,43));
		 nodes.add(new Node("5", 12 ,38)); nodes.add(new Node("6", 22 ,22));
		 nodes.add(new Node("7", 16 ,19)); nodes.add(new Node("8", 15 ,14));
		 nodes.add(new Node("9", 12 ,17)); nodes.add(new Node("10", 26 ,13));
		 nodes.add(new Node("11", 36 ,6)); nodes.add(new Node("12", 15 ,5));
		 nodes.add(new Node("13", 26 ,29)); nodes.add(new Node("14", 43 ,26));
		 nodes.add(new Node("15", 48 ,21)); nodes.add(new Node("16", 45 ,35));
		 nodes.add(new Node("1", 40 ,40));
		 */
		nodes.add(new Node("1", 40, 40));
		nodes.add(new Node("2", 50, 40));
		nodes.add(new Node("3", 51, 42));
		nodes.add(new Node("4", 50, 50));
		nodes.add(new Node("5", 55, 57));
		nodes.add(new Node("6", 62, 48));
		nodes.add(new Node("7", 55, 45));
		nodes.add(new Node("8", 55, 50));
		nodes.add(new Node("9", 62, 57));
		nodes.add(new Node("10", 70, 64));
		nodes.add(new Node("11", 55, 65));
		nodes.add(new Node("12", 50, 70));
		nodes.add(new Node("13", 47, 66));
		nodes.add(new Node("14", 57, 72));
		nodes.add(new Node("15", 30, 60));
		nodes.add(new Node("16", 40, 60));
		nodes.add(new Node("17", 35, 60));
		nodes.add(new Node("18", 26, 59));
		nodes.add(new Node("19", 22, 53));
		nodes.add(new Node("20", 21, 48));
		nodes.add(new Node("21", 21, 45));
		nodes.add(new Node("22", 15, 56));
		nodes.add(new Node("23", 17, 64));
		nodes.add(new Node("24", 10, 70));
		nodes.add(new Node("25", 31, 76));
		nodes.add(new Node("26", 40, 66));
		nodes.add(new Node("27", 41, 46));
		nodes.add(new Node("1", 40, 40));
	}

	public static void main(String args[]) {
		Node[] a = new Node[nodes.size()];
		nodes.toArray(a);
		CVRPChromosomeWithNodeGene node = new CVRPChromosomeWithNodeGene(a);
		System.out.println(node.toString());
		//CVRPChromosomeWithNodeGene node1 = new CVRPChromosomeWithNodeGene(
				//CVRPChromosomeWithNodeGene.getBestRoute(nodes));
		//System.out.println(node1.toString());
	}
}
