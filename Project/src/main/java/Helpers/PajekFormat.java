package Helpers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DB.ElasticSearchService;
import PCN.NodePCN;
import PCN.Node;

public class PajekFormat 
{
		private ElasticSearchService es ; 
		private ArrayList<NodePCN> graph;
		private long number_of_edges = 0 , number_of_vertex;
		private String pajekFile = "" , fileName;
		private String edgesPart = "";
		private Map<String,Integer> vertexMap ;
		public PajekFormat(String cluster_es_index , String cluster_es_type)
		{
			fileName = cluster_es_index+cluster_es_type+".net";
			this.es = new ElasticSearchService(cluster_es_index, cluster_es_type);
			graph = new ArrayList<NodePCN>();
			vertexMap = new HashMap<String,Integer>();
			this.retreive_graph();
		}
		
		
		
		private void retreive_graph()
		{
			number_of_vertex = es.getCountOfDocInType() ;
			
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
		
		
		private long create_file()
		{
			long count  = 0 ;
			int numberVertex;
			
			
			
			for(NodePCN node : graph)
			{
				pajekFile += addVertexLine(node);
				
				edgesPart += vertexMap.get(this.node_toString(node));
				
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
		
		private void saveToFile()
		{
			try(  PrintWriter out = new PrintWriter( this.fileName)  ){
			    out.println( pajekFile );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
