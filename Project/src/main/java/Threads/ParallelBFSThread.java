package Threads;

import java.util.ArrayList;

import DB.ElasticSearchService;
import PCN.Vertex;
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

		add_to_visited();
		
		current.getVertex().getNeighbors().addAll(this.get_unrecorded_neighbors(current));
		
		ParallelBFS.writeToDB(current);
		
		for(Node node : current.getVertex().getNeighbors())
		{
			 toAdd = new NodeBFS(getNode(node.getProteinIndex(),node.getFragmentIndex()),current.getDistance() + 1);
			 
			 if(toAdd.getVertex() == null)
			 {
				 toAdd = new NodeBFS(new Vertex(node.getProteinIndex(),node.getFragmentIndex()) , current.getDistance() + 1);
				 toAdd.getVertex().getNeighbors().addAll(this.get_unrecorded_neighbors(toAdd));
			 }
			 
			 add_to_queue();
		}
	}
	
	private ArrayList<Vertex> get_unrecorded_neighbors(NodeBFS node)
	{
		ArrayList<Vertex> neighbors = neighborsReaderClient.SearchForNeighborsInPCN(node.getVertex().getProteinIndex(), node.getVertex().getFragmentIndex());
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
	
	
	private Vertex getNode(long protein , int index)
	{
		Vertex neighbors = es.SearchPCNDB(protein, index);
		if(neighbors == null)
			return null;
		
		return neighbors;
	}

}
