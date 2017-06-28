package Helpers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DB.ElasticSearchService;
import PCN.NodePCN;
import PCN.Node;
/***
 * Converting a cluster from the databzse into a pajek file format
 * @author Oron
 *
 */
public class PajekFormatHelper 
{
	/***
	 * Elastic search client
	 */
		private ElasticSearchService es ; 
		/***
		 * will hold the cluster
		 */
		private ArrayList<NodePCN> graph;
		/***
		 * number of edges and vertices
		 */
		private long number_of_edges = 0 , number_of_vertex;
		/***
		 * pjek file - string representation of the file 
		 * fileName - name of the output file 
		 */
		private String pajekFile = "" , fileName;
		/**
		 * part of the edges details for easier file building 
		 */
		private String edgesPart = "";
		/***
		 * Vertex map that convert the node to increasing indices from 1 
		 */
		private Map<String,Integer> vertexMap ;
		/***
		 * Constructor
		 * @param cluster_es_index
		 * @param cluster_es_type
		 */
		public PajekFormatHelper(String cluster_es_index , String cluster_es_type)
		{
			fileName = cluster_es_index+cluster_es_type+".net";
			this.es = new ElasticSearchService(cluster_es_index, cluster_es_type);
			graph = new ArrayList<NodePCN>();
			vertexMap = new HashMap<String,Integer>();
			this.retreive_graph();
			this.es.clientClose();
		}
		
		
		/***
		 * gets the graph from the database
		 * manipulates all data from the graph 
		 */
		private void retreive_graph()
		{
			number_of_vertex = es.getCountOfDocInType() - 2 ;
			
			for(int i = 0 ; i < number_of_vertex ; i++)
			{
				graph.add(es.getVertexAt(i));
				vertexMap.put(this.node_toString(graph.get(graph.size() - 1)), i + 1) ;
			}
			
			pajekFile = create_head_section_title(true); // true for vertex
			number_of_edges = this.create_file();
			pajekFile += create_head_section_title(false);
			pajekFile += edgesPart;
			//System.out.println(pajekFile);
			this.saveToFile();
				
		}
		
		/***
		 * creates the file under the class attribute pajekFile 
		 * @return number of edges
		 */
		private long create_file()
		{
			long count  = 0 ;
			int numberVertex;
			
			
			int i = 0 ;
			for(NodePCN node : graph)
			{
				
				pajekFile += addVertexLine(node);
				
				edgesPart += vertexMap.get(this.node_toString(node));
				System.out.println(i);
				i++;
				for(Node n : node.getNeighbors())
				{
					try{
					numberVertex= vertexMap.get(this.node_toString(n));
					edgesPart+=" ";
					edgesPart+=numberVertex;
					}catch(NullPointerException e)
					{
						
					}
				}
				edgesPart += "\n";
				count += node.getNeighbors().size() ;
				
			}
			return count;
		}
		/***
		 * save the result file to an output file 
		 */
		private void saveToFile()
		{
			System.out.println("Print to file");
			try(  PrintWriter out = new PrintWriter("cluster//"+this.fileName)  ){
			    out.println( pajekFile );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("No folder cluster found");
				e.printStackTrace();
			}
		}
		/***
		 * vertex line for the vertices part at top of the file 
		 * @param node
		 * @return
		 */
		private String addVertexLine(NodePCN node)
		{
			String node_as_string = node_toString(node);
			String line = vertexMap.get(node_as_string)+"";
			line += " " +'"' +node_as_string  + '"' + '\n';

			return line;
		}
		
		private String node_toString(NodePCN node)
		{
			return node.getProteinIndex()+"_"+node.getFragmentIndex();
		}
		private String node_toString(Node node)
		{
			return node.getProteinIndex()+"_"+node.getFragmentIndex();
		}
		private String create_head_section_title(boolean vertex_or_edge)
		{
			if(vertex_or_edge)
				return "*Vertices "+number_of_vertex+"\n";
			else
				return "*Arcslist "+number_of_edges+"\n";
		}
}
