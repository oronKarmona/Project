package BFS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.lucene.search.BooleanClause.Occur;

import Calculation.CharacterOccurrence;
import DB.ElasticSearchService;
import PCN.Neighbors;
import PCN.Node;
import Project.TrainingData.Protein;

public class BFS {
	
	private ElasticSearchService elasticSearchService;
	private Queue<NodeBFS> queue = new LinkedList<NodeBFS>();
	//private ArrayList<Neighbors> visited = new ArrayList<Neighbors>();
	private Map<String , Boolean> visited = new HashMap <String , Boolean>();
	private NodeBFS current ; 
	private int factor  ;
	private ArrayList<Protein> uknownStructurePDB , knownStructrePDB;
	private Map<Integer , Protein> protein_map  = new HashMap<Integer , Protein>();
	private double OccurenceThreshold;
	
	public BFS(int factor, ArrayList<Protein> uknownStructurePDB, ArrayList<Protein> knownStructrePDB , int OccurenceThreshold){
		 elasticSearchService = new ElasticSearchService("pcn","data");
		 this.factor = factor;
		 this.uknownStructurePDB = uknownStructurePDB;
		 this.knownStructrePDB = knownStructrePDB;
		 this.OccurenceThreshold = OccurenceThreshold;
		 this.setProteinsMap();
	}
	
	private void setProteinsMap()
	{
		for(Protein p : uknownStructurePDB)
			protein_map.put(p.getProteinIndex(),p);
		
		for(Protein p : knownStructrePDB)
			protein_map.put(p.getProteinIndex(), p);
	}
	public void runBFS(){
	
		
		 queue.add(new NodeBFS(this.getRoot(0),0));
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
				 
				 if(toAdd.getNeighbors() != null  &&
				   !visited.containsKey(this.getString(toAdd.getNeighbors()))&& 
					this.check_repeates(toAdd)
					)
					 	queue.add(new NodeBFS(toAdd));

			 }
			 
			 
			 
		 }
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
		
		return neighbors;
	}
	

	
	
	private Neighbors getNode(long protein , int index)
	{
		Neighbors neighbors = elasticSearchService.SearchPCNDB(protein, index);
		if(neighbors == null)
			return null;
		
		
		return neighbors;
	}
	
	private boolean check_repeates(NodeBFS bfs_node )
	{
		Neighbors node = bfs_node.getNeighbors();
		int protein_index = (int)node.getProtein();
		int fragment_index = node.getIndex();
		
		if(protein_index > 320571)
			protein_index -= 320572;
		
		Protein node_protein = this.protein_map.get(protein_index);
		CharacterOccurrence co = new CharacterOccurrence(this.OccurenceThreshold);
		boolean occurence_check = co.Calculate(node_protein.GetFragments(fragment_index));
		
		return occurence_check;
	}
	
	
}
