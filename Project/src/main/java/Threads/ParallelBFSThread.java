package Threads;

import java.util.ArrayList;

import DB.ElasticSearchService;
import PCN.NodePCN;
import PCN.Node;
import ParallelBFS.NodeBFS;
import ParallelBFS.CreateClusters;
/***
 * Thread for using BFS algorithm in parallel [optional]
 * @author Oron
 *
 */
public class ParallelBFSThread extends Thread
{
	/***
	 * ElasticSearch client
	 */
	private ElasticSearchService es ,  neighborsReaderClient; 
	/**
	 * Current node of the run
	 */
	private NodeBFS current ; 
	/**
	 * Neighbor node 
	 */
	private NodeBFS toAdd;
	/***
	 * Constructor
	 * @param es - elastic search client
	 */
	public ParallelBFSThread(ElasticSearchService es )
	{
		this.es = es ; 
		this.neighborsReaderClient = new ElasticSearchService("pcn","data");
	}
	
	
	@Override
	public void run()
	{
		while( (current  = CreateClusters.get_from_queue()) != null)
		{
			runBFS();
		}
		
	}
	
	/***
	 * Parallel BFS
	 * Opening the neighbors of the 'current' Node and adding them to a queue for further run from the managament file 
	 */
	private void runBFS()
	{

		add_to_visited();
		current.getVertex().getNeighbors().addAll(this.get_unrecorded_neighbors(current));
		
	  //  current.getVertex().setNeighbors(correctNeighbors(current)); 
	    
		CreateClusters.writeToDB(current);
		
		for(Node node : current.getVertex().getNeighbors())
		{
			 toAdd = new NodeBFS(getNode(node.getProteinIndex(),node.getFragmentIndex()),current.getDistance() + 1);
			 
			 if(toAdd.getVertex() == null)
			 {
				 toAdd = new NodeBFS(new NodePCN(node.getProteinIndex(),node.getFragmentIndex()) , current.getDistance() + 1);
				 toAdd.getVertex().getNeighbors().addAll(this.get_unrecorded_neighbors(toAdd));
			 }
			 
			 add_to_queue();
		}
	}
	
	/***
	 * Correcting the node neighbors [optional]
	 * @param node - node to be corrected 
	 * @return corrected neighbors
	 */
	private ArrayList<Node> correctNeighbors(NodeBFS node)
	{
		 ArrayList<Node> neighbors = node.getVertex().getNeighbors();
		 ArrayList<Node> nodes_toRemove = new ArrayList<Node>();
		 
	/*	 for(Node n : neighbors)
		 {
			 if(!CreateClusters.check_conditions(node, new NodeBFS(new NodePCN(n.getProteinIndex(),n.getFragmentIndex()),0)))
				 nodes_toRemove.add(n);
		 }
	*/	 
		 for(Node n : nodes_toRemove)
			 neighbors.remove(n);
		 
		 return neighbors;
	}

	/***
	 * Retruning all the unrecorded neighbors in this PCN node 
	 * @param node
	 * @return
	 */
	private ArrayList<NodePCN> get_unrecorded_neighbors(NodeBFS node)
	{
		ArrayList<NodePCN> neighbors = neighborsReaderClient.SearchForNeighborsInPCN(node.getVertex().getProteinIndex(), node.getVertex().getFragmentIndex());
		return neighbors;
	}
	/***
	 * Adding to nodes queue
	 */
	private void add_to_queue()
	{
		CreateClusters.add_to_queue(current , toAdd);
	}
	/***
	 * Adding to visited nodes
	 * @return
	 */
	private boolean  add_to_visited()
	{
		return CreateClusters.add_to_visited(current);
	}
	
	/***
	 * Return node from the PCN data base
	 * @param protein
	 * @param index
	 * @return
	 */
	private NodePCN getNode(long protein , int index)
	{
		NodePCN neighbors = es.SearchPCNDB(protein, index);
		if(neighbors == null)
			return null;
		
		return neighbors;
	}

}
