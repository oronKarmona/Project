package PCN;

import java.util.ArrayList;

public class Vertex extends Node {

	public ArrayList<Node> neighbors;
	
	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(ArrayList<Node> neighbors) {
		this.neighbors = neighbors;
	}
	public Vertex(){
		neighbors = new ArrayList<Node>();

	}
	public Vertex(long protein,int index){
		super(protein, index);
		neighbors = new ArrayList<Node>();
	}
	public Vertex(Vertex node) {
		this(node.getProteinIndex() , node.getFragmentIndex());
		neighbors = node.getNeighbors();
	}
	

}
