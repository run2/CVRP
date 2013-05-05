package ga.core;


/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * A basic Node with co-ordinates (can be a gene too)
 * Copyright 2013 Academic Free License 3.0
 */

public class Node{
	
	/**
	 * This flag separates a internal node from an end node.
	 * It can be used to treat two nodes as in equal even
	 * if their names are the same but one of them is a
	 * terminal node and other internal. S and E is used
	 * to denote terminal nodes
	 */
	private String pos = "I"; // internal 
	private String codeName;
	private double xCoordinate;
	private double yCoordinate;
	private boolean picked = false;
	private String name = "";
	public Node(String name,double xCoordinate,double yCoordinate){
		this.codeName = name;
		this.name = name;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}
	public void setName(String realName) {
		this.name = realName ;
	}
	public String getName() {
		return name ;
	}	
	public double getxCoordinate() {
		return xCoordinate;
	}
	public void setxCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
	}
	public double getyCoordinate() {
		return yCoordinate;
	}
	public void setyCoordinate(double yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
	public boolean isPicked() {
		return picked;
	}

	public void setPicked(boolean picked) {
		this.picked = picked;
	}

	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String name) {
		this.codeName = name;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Node))
			return false;
		Node compareTo = (Node)obj;

		return (compareTo.getCodeName() + compareTo.getPos()).equals(codeName + pos);
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return (codeName + pos).hashCode();
	}
	public static double getEuclidianDistance(Node nodeFrom, Node nodeTo ){
		return Math.pow((Math.pow(nodeTo.getyCoordinate()-nodeFrom.getyCoordinate(),2) + Math.pow(nodeTo.getxCoordinate()-nodeFrom.getxCoordinate(),2)),0.5);
	}
	public static double getFitnessDistance(Node nodeFrom, Node nodeTo, int noODepos ){
		if(nodeFrom.getCodeName().startsWith("D") && nodeTo.getCodeName().startsWith("D")){
			return Double.MAX_VALUE;
		}else{
			return Math.pow((Math.pow(nodeTo.getyCoordinate()-nodeFrom.getyCoordinate(),2) + Math.pow(nodeTo.getxCoordinate()-nodeFrom.getxCoordinate(),2)),0.5);
		}
	}	
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos =  pos;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return codeName;
	}
	
	public Node clone(){
		// TODO Auto-generated method stub
		Node newNode = new Node(codeName,xCoordinate,yCoordinate);
		newNode.setPos(pos);
		return newNode;
		
	}	
	
}
