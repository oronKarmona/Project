package Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DB.ElasticSearchService;
import PCN.Neighbors;

public class PajekFormat 
{
		private ElasticSearchService es ; 
		private ArrayList<Neighbors> graph;
		private long number_of_edges = 0 , number_of_vertex;
		private String pajekFile = "";
		private Map<String,Integer> integerRepresentation ;
		public PajekFormat(String cluster_es_index , String cluster_es_type)
		{
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
				if(i == 17)
					System.out.println();
				integerRepresentation.put(this.node_toString(graph.get(graph.size() - 1)), i + 1) ;
			}
			
			pajekFile = create_head_section_title(true); // true for vertex
			number_of_edges = this.count_number_of_edges();
			
				
		}
		
		
		private long count_number_of_edges()
		{
			long count  = 0 ; 
			for(Neighbors node : graph)
			{
				pajekFile += addVertexLine(node);
				count += node.getNeighbors().size() ;
			}
			System.out.println(pajekFile);
			return count;
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
		private String create_head_section_title(boolean vertex_or_edge)
		{
			if(vertex_or_edge)
				return "*Vertices "+number_of_vertex+"\n";
			else
				return "*Arcslist "+number_of_edges+"\n";
		}
}
