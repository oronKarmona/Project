package TestingData;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.search.SearchHit;

import Calculation.RMSDCalculation;
import DB.ElasticSearchService;
import PCN.NodePCN;
import ProGAL.geom3d.Point;
import Protein.Protein;
import Protein.Structure;
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
		private ElasticSearchService readCluster , readProteins;
		/**
		 * count of proteins in cluster
		 */
		private int number_of_vertex = 0 ;
		/***
		 * from indices to proteins
		 */
		private Map<Integer , String> clusterMap ; 
		private Map<Integer , Protein> proteinMap;
		/***
		 * will hold the cluster
		 */
		private ArrayList<NodePCN> graph;
		private ArrayList<Protein> known_proteins;
		private ArrayList<TestingEntry> entries ;
		
		private String cluster_index , cluster_type;
			/**
			 * constructor
			 * @param entries - test entries
			 * @param cluster_index
			 * @param cluster_type
			 * @throws FileNotFoundException 
			 */
			public CalculateRmsdForEntry(String read_file_name, String cluster_index , String cluster_type , ArrayList<Protein> known_proteins) throws FileNotFoundException
			{
				this.entries =  ReadResistencesFile.ParseFile(read_file_name); ; 
				this.known_proteins = known_proteins;
				this.cluster_index = cluster_index;
				this.cluster_type = cluster_type;
				graph  = new ArrayList<NodePCN>();
				readCluster = new ElasticSearchService(cluster_index, cluster_type);
				readProteins = new ElasticSearchService("proteins", "known_structure");
				clusterMap = new HashMap<Integer, String>();
				proteinMap = new HashMap<Integer,Protein>();
				this.createProteinMap();
				this.updateEntries();
				WriteDataToFile.WriteDataToFile(entries, "RMSD"+read_file_name);
			}
			
			
			/***
			 * From index to protein index and fragment
			 */
			private void updateEntries()
			{
				String data ; 
				SearchHit[] result  ; 
				Protein p1 , p2 ;
				double rmsd ;
				RMSDCalculation r = new RMSDCalculation();
				for(TestingEntry e : entries)
				{
					data = new String (clusterMap.get(e.getP1()));
					e.setProtein1_index(Integer.parseInt(data.split("_")[0]));
					e.setProtein1_fragment(Integer.parseInt(data.split("_")[1]));
					
					p1 = getProtein(e.getProtein1_index());
					e.setAstralId1(p1.getAstralID());
					
					data = new String (clusterMap.get(e.getP2()));
					e.setProtein2_index(Integer.parseInt(data.split("_")[0]));
					e.setProtein2_fragment(Integer.parseInt(data.split("_")[1]));
					if(e.getProtein2_index() == 322131)
						System.out.println();
					p2 = getProtein(e.getProtein2_index());
					e.setAstralId2(p2.getAstralID());
					
					
					rmsd = r.Calculate(p1.getFragmentCoordinates(e.getProtein1_fragment()), p2.getFragmentCoordinates(e.getProtein2_fragment()));
					
					e.setRmsd(rmsd);
					
					
				}
			}
			
			private Protein getProtein(int protein_index)
			{
				Protein p =null;

				
				p = proteinMap.get(protein_index);

				return p ;
			}
			

			/**
			 * creates map of proteins 
			 */
			private void createProteinMap()
			{
				System.out.println("creating cluster map");
				try{
					number_of_vertex = (int)readCluster.getCountOfDocInType() - 2 ;
				for(int i = 0 ; i < number_of_vertex ; i++)
				{
					System.out.println(i);
					graph.add(readCluster.getVertexAt(i));
					clusterMap.put( i + 1 , this.node_toString(graph.get(graph.size() - 1))) ;
				}} catch(Exception e){
					try {
						clusterMap = DBbypass.DBparser(cluster_index+cluster_type+".net");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				System.out.println("finished cluster map");
				
				
				System.out.println("Creating protein map");
				for(int i = 0 ; i< known_proteins.size() ; i++)
				{
					System.out.println(i);
					proteinMap.put(i + 320572, known_proteins.get(i));
				}
				System.out.println("finished protein map");
			}
			
			
			private String node_toString(NodePCN node)
			{
				return node.getProteinIndex()+"_"+node.getFragmentIndex();
			}
			
			public ArrayList<TestingEntry> getEntries()
			{
				return entries;
			}
}
