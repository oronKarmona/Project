package ParallelBFS;

import PCN.Neighbors;

public class NodeBFS{
	
	private int distance;
	private Neighbors neighbors ;
	
	
	public NodeBFS(Neighbors n, int distance ){
	
		neighbors = new Neighbors();
		neighbors = n;
		this.distance = distance;
	}
	
	

	public NodeBFS(NodeBFS current) {

		this(current.getNeighbors() , current.getDistance());
	}



	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public Neighbors getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(Neighbors neighbors) {
		this.neighbors = neighbors;
	}
	

}
