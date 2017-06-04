package Threads;

import java.util.ArrayList;

import DB.ElasticSearchService;
import PCN.Neighbors;
import PCN.Node;
import ParallelBFS.NodeBFS;
import ParallelBFS.ParallelBFS;

public class ParallelBFSThread extends Thread
{
	private ElasticSearchService es ,  neighborsReaderClient; 
	private NodeBFS current ; 
	private NodeBFS toAdd;
	
	public ParallelBFSThread(ElasticSearchService es )
	{
		this.es = es ; 
		this.neighborsReaderClient = new ElasticSearchService("pcn","data");
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
		if(!add_to_visited())
			return;
		current.getNeighbors().getNeighbors().addAll(this.get_unrecorded_neighbors());
		for(Node node : current.getNeighbors().getNeighbors())
		{
			 toAdd = new NodeBFS(getNode(node.getProteinIndex(),node.getFragmentIndex()),current.getDistance() + 1);
			 add_to_queue();
		}
	}
	
	private ArrayList<Neighbors> get_unrecorded_neighbors()
	{
		ArrayList<Neighbors> neighbors = neighborsReaderClient.SearchForNeighborsInPCN(current.getNeighbors().getProteinIndex(), current.getNeighbors().getFragmentIndex());
		return neighbors;
	}
	private void add_to_queue()
	{
		ParallelBFS.add_to_queue(current , toAdd);
	}
	private boolean  add_to_visited()
	{
		return ParallelBFS.add_to_visited(current);
	}
	
	
	private Neighbors getNode(long protein , int index)
	{
		Neighbors neighbors = es.SearchPCNDB(protein, index);
		if(neighbors == null)
			return null;
		
		return neighbors;
	}

}
