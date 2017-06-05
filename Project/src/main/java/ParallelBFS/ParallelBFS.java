package ParallelBFS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import Calculation.CharacterOccurrence;
import DB.ElasticSearchService;
import PCN.Vertex;
import PCN.Node;
import Project.TrainingData.Protein;
import Threads.ParallelBFSThread;

public class ParallelBFS 
{
	private ElasticSearchService readPcnClient;
	private static ElasticSearchService writeClusterClient;
	private static ArrayList<NodeBFS> queue = new ArrayList<NodeBFS>();
	private static Map<String , Boolean> visited = new HashMap <String , Boolean>();
	private static Map<String , Boolean> marked_to_be_visited = new HashMap <String , Boolean>();
	private static NodeBFS current ; 
	private static int distance_threshold  ;
	private ArrayList<Protein> uknownStructurePDB , knownStructrePDB;
	private static Map<Integer , Protein> protein_map  = new HashMap<Integer , Protein>();
	private static double OThreshold;
	private static int barrier ,Amount_of_threads ;
	private static Object lock; 
	private ArrayList<ParallelBFSThread> threads ; 
	
	public ParallelBFS(int distance_factor, ArrayList<Protein> uknownStructurePDB, ArrayList<Protein> knownStructrePDB , int OccurenceThreshold , 
							String elastic_search_index , String elastic_search_type, String cluster_index, String cluster_type){
		
		 readPcnClient = new ElasticSearchService(elastic_search_index,elastic_search_type);
		 writeClusterClient = new ElasticSearchService(cluster_index,cluster_type);
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
		threads = new ArrayList<ParallelBFSThread>();
		for(int i = 0 ; i < Amount_of_threads ; i ++)
			threads.add( new ParallelBFSThread(readPcnClient));
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
		 current = queue.remove(0);
	     add_to_visited(current);
	     writeToDB(current);
	     current.getNeighbors().getNeighbors().addAll(return_unrecoreded_neighbors(current));
	    		 
			 for(Node n : current.getNeighbors().getNeighbors())
			 {
				 NodeBFS toAdd = new NodeBFS(getNode(n.getProteinIndex(),n.getFragmentIndex()),current.getDistance() + 1);
				 add_to_queue(current, toAdd);

			 }
			 
			 startThreads();
			 
			 
			 
		
	}
	
	private ArrayList<Vertex>  return_unrecoreded_neighbors(NodeBFS node)
	{	
		
		return readPcnClient.SearchForNeighborsInPCN(node.getNeighbors().getProteinIndex(), 
									node.getNeighbors().getFragmentIndex());
	}
	public void flushBulk()
	{
		writeClusterClient.bulkProcessor.flush();
	}
	public static synchronized boolean add_to_visited(NodeBFS node)
	{
		
		visited.put(getString(node.getNeighbors()),true);
		
		System.out.println(node.getDistance());
		return true;
	}
	
	public static synchronized void writeToDB(NodeBFS node)
	{
		writeClusterClient.addToBulk(node.getNeighbors());
	}
	
	public static synchronized void add_to_queue(NodeBFS father ,NodeBFS child )
	{
		
		 if(child != null && child.getNeighbors() != null  &&
				   (!check_exist(child.getNeighbors())&& 
					check_repeates(child) &&
					!check_complete_correspondence(father, child) &&
					!check_marked(child.getNeighbors())
					))
		 {
			 	//	marked_to_be_visited.put(getString(child.getNeighbors()),true);
			 		queue.add(child);
		 }
	}
	
	private static boolean check_marked(Vertex node)
	{
//		boolean result ; 
//		try{
//			result = marked_to_be_visited.get(getString(node) );
//			
//			return result;
//		}catch (NullPointerException e)
//		{
//			return false;
//		}
		
		int index = -1 ; 
		
		index = queue.indexOf(node);
		
		if(index == -1 )
			return false;
		
		return true;
	}
	
	private static boolean check_exist(Vertex node)
	{
		boolean result ; 
		try{
			result = visited.get(getString(node) );
			
			return result;
		}catch (NullPointerException e)
		{
			return false;
		}
	}
	public static synchronized NodeBFS get_from_queue()
	{
		if(queue.isEmpty() || (queue.get(0).getDistance()> distance_threshold) )
			return null;
	
		
			return queue.remove(0);
	}
	private static String getString(Vertex n )
	{
		return n.getProteinIndex()+"_"+n.getFragmentIndex();
	}
	
	/***
	 * Get the root node 
	 * @return root node
	 */
	private Vertex getRoot(int index)
	{
		Vertex neighbors = readPcnClient.getVertexAt(index);		
		return neighbors;
	}

	
	private Vertex getNode(long protein , int index)
	{
		Vertex neighbors = readPcnClient.SearchPCNDB(protein, index);
		if(neighbors == null)
			return null;
		
		return neighbors;
	}
	
	private static boolean check_repeates(NodeBFS bfs_node )
	{
		Vertex node = bfs_node.getNeighbors();
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
		Vertex father_node = current_node.getNeighbors();
		int father_protein_index = (int)father_node.getProteinIndex();
		
		Vertex son_node = child_node.getNeighbors();
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
		for(int i = 0 ; i < knownStructrePDB.size() ; i++)
			protein_map.put(i, knownStructrePDB.get(i));
//		for(Protein p : knownStructrePDB)
//			protein_map.put(p.getProteinIndex(), p);
	}
	
	public static synchronized void update_barrier()
	{
		barrier--;
	}
}
