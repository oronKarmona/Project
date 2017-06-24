package PCN;

import java.util.ArrayList;
/**
 * Node in the PCN , has neighbor list
 * @author Oron
 *
 */
public class NodePCN extends Node {
	/**
	 * Neighbor list
	 */
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
	/**
	 * constructor
	 * @param protein
	 * @param index
	 */
	public NodePCN(long protein,int index){
		super(protein, index);
		neighbors = new ArrayList<Node>();
	}
	/**
	 * copy constructor
	 * @param node
	 */
	public NodePCN(NodePCN node) {
		this(node.getProteinIndex() , node.getFragmentIndex());
		neighbors = node.getNeighbors();
	}
	

}
