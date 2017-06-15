package PCN;

import java.util.ArrayList;

public class NodePCN extends Node {

	public ArrayList<Node> neighbors;
	
	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(ArrayList<Node> neighbors) {
		this.neighbors = neighbors;
	}
	public NodePCN(){
		neighbors = new ArrayList<Node>();

	}
	public NodePCN(long protein,int index){
		super(protein, index);
		neighbors = new ArrayList<Node>();
	}
	public NodePCN(NodePCN node) {
		this(node.getProteinIndex() , node.getFragmentIndex());
		neighbors = node.getNeighbors();
	}
	

}
