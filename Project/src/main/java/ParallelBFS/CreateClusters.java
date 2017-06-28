package ParallelBFS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import Calculation.CharacterOccurrence;
import Calculation.HammingCalculation;
import DB.ElasticSearchService;
import Helpers.PajekFormatHelper;
import PCN.CreateClusterManager;
import PCN.NodePCN;
import PCN.Node;
import Protein.Protein;
import Threads.ParallelBFSThread;
/***
 * creating clusters by using the bfs algorithm until certaion distance
 * @author Oron
 *
 */
public class CreateClusters  extends Thread
{
	/***
	 * client for read
	 */
	private ElasticSearchService readPcnClient;
	/***
	 * client for write 
	 */
	private static ElasticSearchService writeClusterClient;
	/***
	 * queue of nodes to be discovered
	 */
	private static ArrayList<NodeBFS> queue = new ArrayList<NodeBFS>();
	/***
	 * visited nodes
	 */
	private static Map<String , Boolean> visited = new HashMap <String , Boolean>();
	/***
	 * marked nodes that are about to be visited
	 */
	private static Map<String , Boolean> marked_to_be_visited = new HashMap <String , Boolean>();
	/***
	 * current node in the bfs 
	 */
	private static NodeBFS current ; 
	/***
	 * hamming object
	 */
	private static HammingCalculation hamming;
	/***
	 * distance factor
	 */
	private static int distance_threshold  ;
	/***
	 * proteins pdb of known and unknown
	 */
	private ArrayList<Protein> uknownStructurePDB , knownStructrePDB;
	/***
	 * protein map contatins the lists above for easiear access
	 */
	private static Map<Integer , Protein> protein_map  = new HashMap<Integer , Protein>();
	/***
	 * occurance threshold
	 */
	private static double OThreshold;
	private String cluster_index ; 

	/***
	 * contructor
	 * @param distance_factor
	 * @param uknownStructurePDB
	 * @param knownStructrePDB
	 * @param OccurenceThreshold
	 * @param elastic_search_index - of the pcn fro read 
	 * @param elastic_search_type - of the pcn  for read 
	 * @param cluster_index - for write 
	 * @param cluster_type - for write 
	 * @param threshold - for similarity 
	 */
	public CreateClusters(int distance_factor, ArrayList<Protein> uknownStructurePDB, ArrayList<Protein> knownStructrePDB , int OccurenceThreshold , 
							String elastic_search_index , String elastic_search_type, String cluster_index,  double threshold){
		
		 readPcnClient = new ElasticSearchService(elastic_search_index,elastic_search_type);
		 distance_threshold = distance_factor;
		 this.uknownStructurePDB = uknownStructurePDB;
		 this.knownStructrePDB = knownStructrePDB;
		 this.cluster_index = cluster_index ; 
		 OThreshold = OccurenceThreshold;
		 this.setProteinsMap();
		 try {
			hamming = new HammingCalculation(threshold);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void run()
	{
		int index ;
		
		while( (index = CreateClusterManager.getNextIndex()) != -1)
		{
			this.setClusterStart(index);
			this.startBFS(index);
			PajekFormatHelper pf = new PajekFormatHelper("cluster", index+"");
		}
	}
	/***
	 * Starting the bfs run from certain index of root
	 * @param root_index
	 */
	public void startBFS(int root_index)
	{
		NodeBFS childNode  ;
		ArrayList<Node> neighbors;
		queue.add(new NodeBFS(this.getRoot(root_index),0));
		current = queue.get(0); // get the root without removing from queue
		 
					while(!queue.isEmpty() && queue.get(0).getDistance() < distance_threshold )
					{
							 current = queue.remove(0);
							 add_to_visited(current);
							 System.out.println();
							 current.getVertex().setNeighbors(correctNeighbors(current));
						     writeToDB(current);
						     
							 current.getVertex().getNeighbors().addAll(return_unrecoreded_neighbors(current));
						     
						     neighbors = current.getVertex().getNeighbors();
						     
								 for(Node node : neighbors)
								 {
									 childNode = new NodeBFS(getNode(node.getProteinIndex(),node.getFragmentIndex()),current.getDistance() + 1);
											 
											 if(childNode.getVertex() == null)
											 {
												 if(node.getProteinIndex() == 275852)
													 System.out.println();
												 childNode = new NodeBFS(new NodePCN(node.getProteinIndex(),node.getFragmentIndex()) , current.getDistance() + 1);
												 childNode.getVertex().getNeighbors().addAll(return_unrecoreded_neighbors(childNode));
											 }
									 
									 add_to_queue(current, childNode);
					
								 }
					}
					
					 readPcnClient.clientClose();
					 writeClusterClient.clientClose();	
			}
	/***
	 * cluster type for elastiSearch
	 * the cluster type will also be the root index of the cluster
	 * @param cluster_type
	 */
	public void setClusterStart(int cluster_type)
	{
		writeClusterClient = new ElasticSearchService(this.cluster_index,cluster_type+"");
	}

	/***
	 * correcting the neighbors of certain node 
	 * @param node
	 * @return corrected neighbors list
	 */
	private ArrayList<Node> correctNeighbors(NodeBFS node)
	{
		 ArrayList<Node> neighbors = node.getVertex().getNeighbors();
		 ArrayList<Node> nodes_toRemove = new ArrayList<Node>();
		 
		 for(Node n : neighbors)
		 {
			 try{
			 if(!check_conditions(node, new NodeBFS(new NodePCN(n.getProteinIndex(),n.getFragmentIndex()),0)))
				 nodes_toRemove.add(n);
			 }catch(Exception e )
			 {
				 nodes_toRemove.add(n);
			 }
		 }
		 
		 for(Node n : nodes_toRemove)
			 neighbors.remove(n);
		 
		 return neighbors;
	}
	/***
	 * checking conditions of similarity between 2 nodes
	 * @param father
	 * @param child
	 * @return true for similar (not good) , false otherwise
	 */
	private synchronized static boolean check_conditions(NodeBFS father , NodeBFS child)
	{
		if(child != null && child.getVertex() != null  && 
					check_repeates(child) &&
					!check_complete_correspondence(father, child) )
			return true;
		
		return false;
	}
	
	/***
	 * return all neighbors not listed in this node list 
	 * @param node
	 * @return
	 */
	private ArrayList<NodePCN>  return_unrecoreded_neighbors(NodeBFS node)
	{	
		
		return readPcnClient.SearchForNeighborsInPCN(node.getVertex().getProteinIndex(), 
									node.getVertex().getFragmentIndex());
	}
	/***
	 * for asynchrounos use
	 */
	public void flushBulk()
	{
		writeClusterClient.bulkProcessor.flush();
	}
	/***
	 * add to visited map after visit
	 * @param node
	 * @return
	 */
	public static synchronized boolean add_to_visited(NodeBFS node)
	{
		
		visited.put(getString(node.getVertex()),true);
		
		//System.out.println(node.getDistance());
		return true;
	}
	/***
	 * write node to db under new cluster
	 * @param node
	 */
	public static synchronized void writeToDB(NodeBFS node)
	{
		//writeClusterClient.addToBulk(node.getVertex());
		writeClusterClient.add(node.getVertex());
	}
	/***
	 * adding to the waiting queue after checking conditions 
	 * @param father
	 * @param child
	 */
	public static synchronized void add_to_queue(NodeBFS father ,NodeBFS child )
	{
		try{
		 if(check_conditions(father , child) && !check_marked(child.getVertex()) && !check_exist(child.getVertex()) )
		 {
			 		marked_to_be_visited.put(getString(child.getVertex()),true);
			 		queue.add(child);
		 }}catch(Exception e)
		{
			 
		}
	}
	/***
	 * check if the node is about to be visited (marked)
	 * @param node
	 * @return
	 */
	private static boolean check_marked(NodePCN node)
	{
		boolean result ; 
		try{
			result = marked_to_be_visited.get(getString(node) );
			
			return result;
		}catch (NullPointerException e)
		{
			return false;
		}
		

	}
	/***
	 * checking if the node is already visited
	 * @param node
	 * @return
	 */
	private static boolean check_exist(NodePCN node)
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
	/***
	 * get next node from queue
	 * @return
	 */
	public static synchronized NodeBFS get_from_queue()
	{
		if(queue.isEmpty() || (queue.get(0).getDistance()> distance_threshold) )
			return null;
	
		
			return queue.remove(0);
	}
	private static String getString(NodePCN n )
	{
		return n.getProteinIndex()+"_"+n.getFragmentIndex();
	}
	
	/***
	 * Get the root node 
	 * @return root node
	 */
	private NodePCN getRoot(int index)
	{
		NodePCN neighbors = readPcnClient.getVertexAt(index);		
		return neighbors;
	}

	/***
	 * return node from the PCN
	 * @param protein
	 * @param index
	 * @return
	 */
	private NodePCN getNode(long protein , int index)
	{
		NodePCN neighbors = readPcnClient.SearchPCNDB(protein, index);
		if(neighbors == null)
			return null;
		
		return neighbors;
	}
	/**
	 * checking the repeats in a bfs node amino acid chain under the constraint of the predefined OThreshold
	 * @param bfs_node
	 * @return true for good occurance 
	 */
	private static boolean check_repeates(NodeBFS bfs_node )
	{
		NodePCN node = bfs_node.getVertex();
		int protein_index = (int)node.getProteinIndex();
		int fragment_index = node.getFragmentIndex();
		
		//protein_index = protein_index_corrector(protein_index);
		
		Protein node_protein = protein_map.get(protein_index);
		CharacterOccurrence co = new CharacterOccurrence(OThreshold);
		boolean occurence_check = co.Calculate(node_protein.GetFragments(fragment_index));
		
		return occurence_check;
	}
	
	/***
	 * checking complete correspondence between father and son
	 * @param current_node
	 * @param child_node
	 * @return
	 */
	private static boolean check_complete_correspondence(NodeBFS current_node , NodeBFS child_node)
	{
		NodePCN father_node = current_node.getVertex();
		int father_protein_index = (int)father_node.getProteinIndex();
		
		NodePCN son_node = child_node.getVertex();
		int son_protein_index = (int)son_node.getProteinIndex();
		
	
		Protein current_protein =  protein_map.get(father_protein_index);
		Protein son_protein =  protein_map.get(son_protein_index);
		
		boolean result ;
		result = current_protein.GetFragments(current_node.getVertex().getFragmentIndex()).equals(son_protein.GetFragments(child_node.getVertex().getFragmentIndex()));
//		hamming.Calculate(current_protein.GetFragments(father_node.getFragmentIndex()), son_protein.GetFragments(son_node.getFragmentIndex()));
//		
//		result = hamming.checkSimilarity();
//		hamming.setHammingDistance(0); // initialise again
		return  result;
	}
	

	/***
	 * from lists to protein map
	 */
	private void setProteinsMap()
	{
		for(Protein p : uknownStructurePDB)
			protein_map.put(p.getProteinIndex(),p);
//		for(int i = 0 ; i < knownStructrePDB.size() ; i++)
//			protein_map.put(i + 320572, knownStructrePDB.get(i)); //320572
		for(Protein p : knownStructrePDB)
			protein_map.put(p.getProteinIndex() + 320572, p);

	}
	

}
