package Helpers;

import java.util.ArrayList;

import DB.ElasticSearchService;
import PCN.Neighbors;

public class PajekFormat 
{
		private ElasticSearchService es ; 
		private ArrayList<Neighbors> graph;
		private long number_of_edges = 0 , number_of_vertex;
		private String pajekFile = "";
		public PajekFormat(String cluster_es_index , String cluster_es_type)
		{
			this.es = new ElasticSearchService(cluster_es_index, cluster_es_type);
			graph = new ArrayList<Neighbors>();
			this.retreive_graph();
		}
		
		
		
		private void retreive_graph()
		{
			number_of_vertex = es.getCountOfDocInType() ;
			
			for(int i = 0 ; i < number_of_vertex ; i++)
				graph.add(es.getNeighbors(i));
			
			number_of_edges = this.count_number_of_edges();
			pajekFile = create_head_section_title(true); // true for vertex
				
		}
		
		
		private long count_number_of_edges()
		{
			long count  = 0 ; 
			for(Neighbors node : graph)
			{
				pajekFile += this.node_toString(node);
				count += node.getNeighbors().size() ;
			}
			
			return count;
		}
		private String node_toString(Neighbors node)
		{
			return node.getProteinIndex()+"_"+node.getProteinIndex()+"\n";
		}
		private String create_head_section_title(boolean vertex_or_edge)
		{
			if(vertex_or_edge)
				return "*Vertices "+number_of_vertex+"\n";
			else
				return "*Arcslist "+number_of_edges+"\n";
		}
}
