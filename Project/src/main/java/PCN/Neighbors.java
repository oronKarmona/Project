package PCN;

import java.util.ArrayList;

public class Neighbors extends Node {

	public ArrayList<Node> neighbors;
	
	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(ArrayList<Node> neighbors) {
		this.neighbors = neighbors;
	}
	public Neighbors(){
		neighbors = new ArrayList<Node>();

	}
	public Neighbors(long protein,int index){
		super(protein, index);
		neighbors = new ArrayList<Node>();
	}
	public Neighbors(Neighbors node) {
		this(node.getProteinIndex() , node.getFragmentIndex());
		neighbors = node.getNeighbors();
	}
	

}
