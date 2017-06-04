package Helpers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DB.ElasticSearchService;
import PCN.Neighbors;
import PCN.Node;

public class PajekFormat 
{
		private ElasticSearchService es ; 
		private ArrayList<Neighbors> graph;
		private long number_of_edges = 0 , number_of_vertex;
		private String pajekFile = "" , fileName;
		private String edgesPart = "";
		private Map<String,Integer> integerRepresentation ;
		public PajekFormat(String cluster_es_index , String cluster_es_type)
		{
			fileName = cluster_es_index+cluster_es_type+".net";
			this.es = new ElasticSearchService(cluster_es_index, cluster_es_type);
			graph = new ArrayList<Neighbors>();
			integerRepresentation = new HashMap<String,Integer>();
			this.retreive_graph();
		}
		
		
		
		private void retreive_graph()
		{
			number_of_vertex = es.getCountOfDocInType() ;
			
			for(int i = 0 ; i < number_of_vertex ; i++)
			{
				graph.add(es.getNeighbors(i));
				integerRepresentation.put(this.node_toString(graph.get(graph.size() - 1)), i + 1) ;
			}
			
			pajekFile = create_head_section_title(true); // true for vertex
			number_of_edges = this.count_number_of_edges();
			pajekFile += create_head_section_title(false);
			pajekFile += edgesPart;
			System.out.println(pajekFile);
			this.saveToFile();
				
		}
		
		
		private long count_number_of_edges()
		{
			long count  = 0 ;
			int numberVertex;
			for(Neighbors node : graph)
			{
				pajekFile += addVertexLine(node);
				edgesPart += integerRepresentation.get(this.node_toString(node));
				for(Node n : node.getNeighbors())
				{
					try{
					numberVertex= integerRepresentation.get(this.node_toString(n));
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
		private String addVertexLine(Neighbors node)
		{
			String node_as_string = node_toString(node);
			String line = integerRepresentation.get(node_as_string)+"";
			line += " " +'"' +node_as_string  + '"' + '\n';

			
			return line;
		}
		private String node_toString(Neighbors node)
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
