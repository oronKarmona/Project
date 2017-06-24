package TestingData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.search.SearchHit;

import Calculation.RMSDCalculation;
import DB.ElasticSearchService;
import PCN.NodePCN;
import ProGAL.geom3d.Point;
import Project.TrainingData.Protein;
import Project.TrainingData.Structure;
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
			/**
			 * constructor
			 * @param entries - test entries
			 * @param cluster_index
			 * @param cluster_type
			 */
			public CalculateRmsdForEntry(ArrayList<TestingEntry> entries , String cluster_index , String cluster_type , ArrayList<Protein> known_proteins)
			{
				this.entries = entries ; 
				this.known_proteins = known_proteins;
				graph  = new ArrayList<NodePCN>();
				readCluster = new ElasticSearchService(cluster_index, cluster_type);
				readProteins = new ElasticSearchService("proteins", "known_structure");
				clusterMap = new HashMap<Integer, String>();
				proteinMap = new HashMap<Integer,Protein>();
				number_of_vertex = (int)readCluster.getCountOfDocInType() - 2 ;
				this.createProteinMap();
				this.updateEntries();
				System.out.println();
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
					System.out.println();
					
				}
			}
			
			private Protein getProtein(int protein_index)
			{
				Protein p =null;
//				Map<String, Object> map  = readProteins.get(protein_index - 320572);
//				p.setAminoAcids((String)map.get("aminoAcids"));
//				p.setAstralID((String)map.get("astralID"));
//				p.setStructure((ArrayList<Structure>)map.get("structure"));
//				p.setStructure(fromMaptToStructure(p));
				
				p = proteinMap.get(protein_index);
//				for(Protein pr : known_proteins)
//				{
//					if(pr.getProteinIndex() == protein_index - 320572)
//						return pr;
//				}
//				return null ;
				return p ;
			}
			
//			private ArrayList<Structure> fromMaptToStructure(Protein p )
//			{
//				Map<String,Object> smap;
//				Structure temp = new Structure();
//				ArrayList<Structure> structure = new ArrayList<Structure>();
//				for(int i = 0 ; i < p.getStructure().size() ; i++)
//				{
//					smap = (Map<String,Object>) p.getStructure().get(i);
//					Point point = new Point()
//					if(smap == null)
//						return null;
//					Object o = smap.get("p");
//					temp.setAminoAcid((String)smap.get("AminoAcid"));
//					temp.setIndex((Integer)smap.get("atomIndex"));
//					temp.setP((Point) smap.get("p"));
//					temp.setResidueSequenceNumber((Integer)smap.get("residueSequenceNumber"));
//					temp.setType((String)smap.get("type"));
//					
//					structure.add(new Structure(temp));
//					
//				}
//				
//				if(structure.isEmpty())
//					return null;
//				else
//					return structure;
//				
//			}
			/**
			 * creates map of proteins 
			 */
			private void createProteinMap()
			{
				System.out.println("creating cluster map");
				for(int i = 0 ; i < number_of_vertex ; i++)
				{
					System.out.println(i);
					graph.add(readCluster.getVertexAt(i));
					clusterMap.put( i + 1 , this.node_toString(graph.get(graph.size() - 1))) ;
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
