package ParallelBFS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import Calculation.CharacterOccurrence;
import DB.ElasticSearchService;
import PCN.Neighbors;
import PCN.Node;
import Project.TrainingData.Protein;
import Threads.ParallelBFSThread;

public class ParallelBFS 
{
	private ElasticSearchService elasticSearchService;
	private static Queue<NodeBFS> queue = new LinkedList<NodeBFS>();
	private static Map<String , Boolean> visited = new HashMap <String , Boolean>();
	private static NodeBFS current ; 
	private static int distance_threshold  ;
	private ArrayList<Protein> uknownStructurePDB , knownStructrePDB;
	private static Map<Integer , Protein> protein_map  = new HashMap<Integer , Protein>();
	private static double OThreshold;
	private static int barrier ,Amount_of_threads ; 
	private ArrayList<ParallelBFSThread> threads ; 
	
	public ParallelBFS(int distance_factor, ArrayList<Protein> uknownStructurePDB, ArrayList<Protein> knownStructrePDB , int OccurenceThreshold , 
							String elastic_search_index , String elastic_search_type){
		 elasticSearchService = new ElasticSearchService(elastic_search_index,elastic_search_type);
		 distance_threshold = distance_factor;
		 this.uknownStructurePDB = uknownStructurePDB;
		 this.knownStructrePDB = knownStructrePDB;
		 OThreshold = OccurenceThreshold;
		 this.setProteinsMap();
		 Amount_of_threads = Runtime.getRuntime().availableProcessors();
		 barrier = Amount_of_threads;
		 createThreads();
	}
	
	private void createThreads()
	{
		for(int i = 0 ; i < Amount_of_threads ; i ++)
			threads.add( new ParallelBFSThread(elasticSearchService));
	}
	
	private void startThreads()
	{
		for(ParallelBFSThread t : threads)
			t.start();
		
		for(ParallelBFSThread t : threads)
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	public void InitiateBFS(int root_index){
		
		
		 queue.add(new NodeBFS(this.getRoot(root_index),0));
		 current = queue.peek();
		 // Save cluster ? 
		 current = queue.poll();
	     visited.put(getString(current.getNeighbors()),true);
			   
			 for(Node n : current.getNeighbors().getNeighbors())
			 {
				 NodeBFS toAdd = new NodeBFS(getNode(n.getProteinIndex(),n.getFragmentIndex()),current.getDistance() + 1);
				 
				 if(toAdd.getNeighbors() != null  &&
				   !visited.containsKey(getString(toAdd.getNeighbors()))&& 
					check_repeates(toAdd) &&
					check_complete_correspondence(current, toAdd)
					)
					 	queue.add(new NodeBFS(toAdd));

			 }
			 
			 startThreads();
			 
			 
			 
		
	}
	
	public static synchronized void add_to_visited(NodeBFS node)
	{
		visited.put(getString(node.getNeighbors()),true);
	}
	
	public static synchronized void add_to_queue(NodeBFS node )
	{
		 if(node.getNeighbors() != null  &&
				   !visited.containsKey(getString(node.getNeighbors()))&& 
					check_repeates(node) &&
					check_complete_correspondence(current, node)
					)
			 		queue.add(node);
	}
	
	public static synchronized NodeBFS get_from_queue()
	{
		if(queue.isEmpty() && barrier == 0 )
			return null;
		else if(queue.peek().getDistance() >= distance_threshold)
			return null;
		else
			return queue.poll();
	}
	private static String getString(Neighbors n )
	{
		return n.getProteinIndex()+" "+n.getFragmentIndex();
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
	
	private static boolean check_repeates(NodeBFS bfs_node )
	{
		Neighbors node = bfs_node.getNeighbors();
		int protein_index = (int)node.getProteinIndex();
		int fragment_index = node.getFragmentIndex();
		
		protein_index = protein_index_corrector(protein_index);
		
		Protein node_protein = protein_map.get(protein_index);
		CharacterOccurrence co = new CharacterOccurrence(OThreshold);
		boolean occurence_check = co.Calculate(node_protein.GetFragments(fragment_index));
		
		return occurence_check;
	}
	
	
	private static boolean check_complete_correspondence(NodeBFS current_node , NodeBFS child_node)
	{
		Neighbors father_node = current_node.getNeighbors();
		int father_protein_index = (int)father_node.getProteinIndex();
		
		Neighbors son_node = child_node.getNeighbors();
		int son_protein_index = (int)son_node.getProteinIndex();
		
		father_protein_index = protein_index_corrector(father_protein_index);
		son_protein_index = protein_index_corrector(son_protein_index);
	
		Protein current_protein =  protein_map.get(father_protein_index);
		Protein son_protein =  protein_map.get(son_protein_index);

		boolean result = current_protein.getAminoAcids().equals(son_protein.getAminoAcids());
		
		
		return  result;
	}
	
	private static int protein_index_corrector(int protein_index)
	{
		if(protein_index > 320571)
			protein_index -= 320572;
		
		return protein_index;
	}
	
	
	private void setProteinsMap()
	{
		for(Protein p : uknownStructurePDB)
			protein_map.put(p.getProteinIndex(),p);
		
		for(Protein p : knownStructrePDB)
			protein_map.put(p.getProteinIndex(), p);
	}
	
	public static synchronized void update_barrier()
	{
		barrier--;
	}
}
