package Helpers;

import java.util.ArrayList;

import DB.ElasticSearchService;
import PCN.Neighbors;

public class PajekFormat 
{
		private ElasticSearchService es ; 
		private ArrayList<Neighbors> graph;
		
		
		public PajekFormat(String cluster_es_index , String cluster_es_type)
		{
			this.es = new ElasticSearchService(cluster_es_index, cluster_es_type);
			graph = new ArrayList<Neighbors>();
			this.retreive_graph();
		}
		
		
		
		private void retreive_graph()
		{
			long number_of_vertex = es.getCountOfDocInType() ;
			
			for(int i = 0 ; i < number_of_vertex ; i++)
			{
				graph.add(es.getNeighbors(i));
			}
			System.out.println();
				
		}
}
