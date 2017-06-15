package ParallelBFS;

import PCN.NodePCN;

public class NodeBFS{
	
	private int distance;
	private NodePCN vertex ;
	
	
	public NodeBFS(NodePCN n, int distance ){
	
		vertex = new NodePCN();
		vertex = n;
		this.distance = distance;
	}
	
	

	public NodeBFS(NodeBFS current) {

		this(current.getVertex() , current.getDistance());
	}



	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public NodePCN getVertex() {
		return vertex;
	}
	public void setNeighbors(NodePCN neighbors) {
		this.vertex = neighbors;
	}
	

}
