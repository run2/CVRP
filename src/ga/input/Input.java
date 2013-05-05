package ga.input;

import ga.core.Node;

import java.util.List;

public class Input {
	public static List<Node> nodes;
	public static Node startDepo;
	public static Node endDepo;
	public static double totalDemand = 0;
	public static double maxCapacityPerTruck = 0 ;
	public static int totalNoOfDeposOrNoOfVehicles = 0;
	public static double optimumMinimalCost = 0;
	public static void initializeFruityBunTSP(){
		startDepo = FruityBunNodes.startDepo;
		endDepo = FruityBunNodes.endDepo;
		nodes = FruityBunNodes.nodes;
		totalNoOfDeposOrNoOfVehicles = FruityBunNodes.noOfDepos;
		optimumMinimalCost = 600;
	}
	public static void initializeFruityBunCVRP(){
		startDepo = FruityBunNodesWithDemand.startDepo;
		endDepo = FruityBunNodesWithDemand.endDepo;
		nodes = FruityBunNodesWithDemand.nodes;
		totalDemand = FruityBunNodesWithDemand.totalDemand;
		maxCapacityPerTruck = FruityBunNodesWithDemand.maxCapacityPerTruck;
		totalNoOfDeposOrNoOfVehicles = FruityBunNodesWithDemand.noOfDepos;
		optimumMinimalCost = 600;
	}	
	public static void initializeCircleTSP(){
		startDepo = NodesInCircle.startDepo;
		endDepo = NodesInCircle.endDepo;
		nodes = NodesInCircle.nodes;
		totalNoOfDeposOrNoOfVehicles = NodesInCircle.noOfDepos;
		optimumMinimalCost = 600;
	}		

	public static void initializeCircleCVRP(){
		startDepo = NodesInCircleWithDemand.startDepo;
		endDepo = NodesInCircleWithDemand.endDepo;
		nodes = NodesInCircleWithDemand.nodes;
		totalDemand = NodesInCircleWithDemand.totalDemand;
		maxCapacityPerTruck = NodesInCircleWithDemand.maxCapacityPerTruck;
		totalNoOfDeposOrNoOfVehicles = NodesInCircleWithDemand.noOfDepos;
		optimumMinimalCost = 600;
	}
	
	/*static{
		startDepo = FruityBunNodes.startDepo;
		endDepo = FruityBunNodes.endDepo;
		nodes = FruityBunNodes.nodes;
		totalDemand = FruityBunNodes.totalDemand;
		maxCapacityPerTruck = FruityBunNodes.maxCapacityPerTruck;
		totalNoOfDeposOrNoOfVehicles = FruityBunNodes.noOfDepos;
	}*/
}
