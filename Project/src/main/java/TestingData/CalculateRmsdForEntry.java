package TestingData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DB.ElasticSearchService;
import PCN.NodePCN;
/***
 * calculate rmsd for each test entry
 * @author Oron
 *
 */
public class CalculateRmsdForEntry 
{
		/***
		 * elasticsearch client
		 */
		private ElasticSearchService es ;
		/**
		 * count of proteins in cluster
		 */
		private int number_of_vertex = 0 ;
		/***
		 * from indices to proteins
		 */
		private Map<Integer , String> proteinMap ; 
		/***
		 * will hold the cluster
		 */
		private ArrayList<NodePCN> graph;
		ArrayList<TestingEntry> entries ;
			/**
			 * constructor
			 * @param entries - test entries
			 * @param cluster_index
			 * @param cluster_type
			 */
			public CalculateRmsdForEntry(ArrayList<TestingEntry> entries , String cluster_index , String cluster_type)
			{
				this.entries = entries ; 
				graph  = new ArrayList<NodePCN>();
				es = new ElasticSearchService(cluster_index, cluster_type);
				proteinMap = new HashMap<Integer, String>();
				number_of_vertex = (int)es.getCountOfDocInType() - 2 ;
				this.createProteinMap();
				this.updateEntries();
			}
			
			
			/***
			 * From index to protein index and fragment
			 */
			private void updateEntries()
			{
				String data ; 
				
				for(TestingEntry e : entries)
				{
					data = new String (proteinMap.get(e.getP1()));
					e.setProtein1_index(Integer.parseInt(data.split("_")[0]));
					e.setProtein1_fragment(Integer.parseInt(data.split("_")[1]));
					
					data = new String (proteinMap.get(e.getP2()));
					e.setProtein2_index(Integer.parseInt(data.split("_")[0]));
					e.setProtein2_fragment(Integer.parseInt(data.split("_")[1]));
					
				}
			}
			
			/**
			 * creates map of proteins 
			 */
			private void createProteinMap()
			{
				for(int i = 0 ; i < number_of_vertex ; i++)
				{
					graph.add(es.getVertexAt(i));
					proteinMap.put( i + 1 , this.node_toString(graph.get(graph.size() - 1))) ;
				}
			}
			
			
			private String node_toString(NodePCN node)
			{
				return node.getProteinIndex()+"_"+node.getFragmentIndex();
			}
}
