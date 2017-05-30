package BFS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import DB.ElasticSearchService;
import PCN.Neighbors;
import PCN.Node;

public class BFS {
	
	private ElasticSearchService elasticSearchService;
	private Queue<NodeBFS> queue = new LinkedList<NodeBFS>();
	//private ArrayList<Neighbors> visited = new ArrayList<Neighbors>();
	private Map<String , Boolean> visited = new HashMap <String , Boolean>();
	private NodeBFS current ; 
	private int factor  ;
	
	public BFS(int factor){
		 elasticSearchService = new ElasticSearchService("proteins","pcn_db");
		 this.factor = factor;
	}

	public void run(){
	
		
		 queue.add(new NodeBFS(this.getRoot(1),0));
		 current = queue.peek();
		 // Save cluster ? 
		 while(!(queue.isEmpty()) && current.getDistance() <= factor ) 
		 {
			 current = queue.poll();
			// visited.add(new Neighbors(current.getNeighbors()));
			   visited.put(this.getString(current.getNeighbors()),true);
			   
			 for(Node n : current.getNeighbors().getNeighbors())
			 {
				 NodeBFS toAdd = new NodeBFS(getNode(n.getProtein(),n.getIndex()),current.getDistance() + 1);
				 
				 if(toAdd.getNeighbors() != null &&
				   !visited.containsKey(this.getString(toAdd.getNeighbors())))
					 queue.add(new NodeBFS(toAdd));

			 }
			 
			 
			 
		 }
		
		
	//	System.out.println(neighbors);
		
		
		
	}
	
	private String getString(Neighbors n )
	{
		return n.getProtein()+" "+n.getIndex();
	}
	/***
	 * Get the root node 
	 * @return root node
	 */
	private Neighbors getRoot(int index)
	{
		Neighbors neighbors = elasticSearchService.getNeighbors(index);
		neighbors.setNeighbors(fromMapToNeighbors(neighbors));
		
		return neighbors;
	}
	
	
	@SuppressWarnings("unchecked")
	private ArrayList<Node> fromMapToNeighbors(Neighbors neighbors)
	{
		Map<String, Object> nmap;
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		for(int i = 0 ; i < neighbors.getNeighbors().size() ; i++)
		{
			nmap = (Map<String, Object>) neighbors.getNeighbors().get(i);
			if(nmap == null)
				return null;
			nodes.add(new Node( (Integer)nmap.get("m_protein"),(Integer)nmap.get("m_index")));
		}
		
		return nodes;
	}
	
	
	private Neighbors getNode(long protein , int index)
	{
		Neighbors neighbors = elasticSearchService.SearchPCNDB(protein, index);
		if(neighbors == null)
			return null;
		neighbors.setNeighbors(fromMapToNeighbors(neighbors));
		
		return neighbors;
	}
	
	
}
