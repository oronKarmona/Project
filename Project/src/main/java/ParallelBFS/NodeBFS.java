package ParallelBFS;

import PCN.Vertex;

public class NodeBFS{
	
	private int distance;
	private Vertex neighbors ;
	
	
	public NodeBFS(Vertex n, int distance ){
	
		neighbors = new Vertex();
		neighbors = n;
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
	public Vertex getVertex() {
		return neighbors;
	}
	public void setNeighbors(Vertex neighbors) {
		this.neighbors = neighbors;
	}
	

}
