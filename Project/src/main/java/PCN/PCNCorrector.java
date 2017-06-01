package PCN;

import java.util.ArrayList;

import DB.ElasticSearchService;

public class PCNCorrector 
{
	private ElasticSearchService es = new ElasticSearchService("pcn","data");
	
	private int start_index  = 0 , final_index ; 
	
	public PCNCorrector()
	{
		final_index = (int) (es.getCountOfDocInType() - 1) ;
		this.startCorrection();
	}
	
	
	public void startCorrection()
	{
		Neighbors main_node , child_node; 
		ArrayList<Node> child_neighbors  ;
		
		for(int i = start_index ; i < final_index ; i ++ )
		{
			main_node = es.getNeighbors(i);
			
			for(Node node  : main_node.getNeighbors())
			{
				child_node = es.SearchPCNDB(node.getProteinIndex(), node.getFragmentIndex());
				child_neighbors = child_node.getNeighbors();
			}
			
			
		}
		
	}

}
