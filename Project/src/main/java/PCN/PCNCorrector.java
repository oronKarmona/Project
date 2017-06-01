package PCN;

import DB.ElasticSearchService;

public class PCNCorrector 
{
	private ElasticSearchService es = new ElasticSearchService("pcn","data");
	private Neighbors neighbor ; 
	private int start_index  = 0 , final_index ; 
	
	public PCNCorrector()
	{
		final_index = (int) (es.getCountOfDocInType() - 1) ;
	}
	
	
	public void startCorrection()
	{
		for(int i = start_index ; i < final_index ; i ++ )
		{
			neighbor = es.getNeighbors(i);
			for(Node n  : neighbor.getNeighbors())
				neighbor = es.SearchPCNDB(n.getProteinIndex(), n.getFragmentIndex());
			
			System.out.println("");
			
		}
		
	}

}
