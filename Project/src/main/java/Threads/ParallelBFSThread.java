package Threads;

import DB.ElasticSearchService;
import PCN.Neighbors;
import PCN.Node;
import ParallelBFS.NodeBFS;
import ParallelBFS.ParallelBFS;

public class ParallelBFSThread extends Thread
{
	private ElasticSearchService es , writeClusterClient; 
	private NodeBFS current ; 
	private NodeBFS toAdd;
	
	public ParallelBFSThread(ElasticSearchService es, ElasticSearchService writeClusterClient )
	{
		this.es = es ; 
		this.writeClusterClient = writeClusterClient;
	}
	
	
	@Override
	public void run()
	{
		while( (current  = ParallelBFS.get_from_queue()) != null)
		{
			runBFS();
		}
		
		ParallelBFS.update_barrier();
	}
	
	
	private void runBFS()
	{
		add_to_visited();
		
		for(Node node : current.getNeighbors().getNeighbors())
		{
			 toAdd = new NodeBFS(getNode(node.getProteinIndex(),node.getFragmentIndex()),current.getDistance() + 1);
		}
	}
	
	
	
	private void add_to_visited()
	{
		ParallelBFS.add_to_visited(current);
	}
	
	
	private Neighbors getNode(long protein , int index)
	{
		Neighbors neighbors = es.SearchPCNDB(protein, index);
		if(neighbors == null)
			return null;
		
		return neighbors;
	}

}
