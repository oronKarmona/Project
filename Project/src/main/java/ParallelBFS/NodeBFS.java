package ParallelBFS;

import PCN.NodePCN;
/***
 * BFS node 
 * @author Oron
 *
 */
public class NodeBFS{
	/***
	 * distance from root
	 */
	private int distance;
	/***
	 * the vertex
	 */
	private NodePCN vertex ;
	
	/***
	 * constructor
	 * @param n - the node of the pcn 
	 * @param distance - distance from root
	 */
	public NodeBFS(NodePCN n, int distance ){
	
		vertex = new NodePCN();
		vertex = n;
		this.distance = distance;
	}
	
	
	/***
	 * copy constructor
	 * @param current
	 */
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
