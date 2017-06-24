package PCN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import DB.ElasticSearchService;
/**
 * Inside use
 * correcting the PCN with several conditions
 * @author Oron
 *
 */
public class PCNCorrector 
{
	/**
	 * client
	 */
	private ElasticSearchService es = new ElasticSearchService("pcn","data");
	/**
	 * running indices
	 */
	private int start_index  = 0 , final_index ; 
	/**
	 * constructor
	 */
	public PCNCorrector()
	{
		final_index = (int) (es.getCountOfDocInType() - 1) ;
		this.startCorrection();
	}
	
	
	public void startCorrection()
	{
		NodePCN main_node , child_node; 
		ArrayList<Node> child_neighbors  ;
		String id_toCorrect ; 
		
		for(int i = start_index ; i < final_index ; i ++ )
		{
			main_node = es.getVertexAt(i);
			
			check_neighbors(main_node);
			
			
		}
		
	}
	
	private void check_neighbors(NodePCN main_node)
	{
		NodePCN  child_node; 
		ArrayList<Node> child_neighbors  ;
		String id_toCorrect ; 
		
		for(Node node  : main_node.getNeighbors())
		{
			child_node = es.SearchPCNDB(node.getProteinIndex(), node.getFragmentIndex());
			id_toCorrect = es.getLastDocID();
			child_neighbors = child_node.getNeighbors();
			
			if(!this.check_if_father_is_Neighbor(child_neighbors, main_node))
			{
				this.UpdateDoc(child_neighbors, main_node, id_toCorrect);
			}
		}
	}
	
	private boolean check_if_father_is_Neighbor(ArrayList<Node> child_neighbors , NodePCN main_node)
	{
		for(Node node : child_neighbors)
			if(node.getFragmentIndex() == main_node.getFragmentIndex() && node.getProteinIndex() == main_node.getProteinIndex())
				return true;
		
		return false;
	}
	
	private void UpdateDoc(ArrayList<Node> child_neighbors ,NodePCN main_node , String id_toCorrect  )
	{
		child_neighbors.add(new Node(main_node.getProteinIndex(),main_node.getFragmentIndex()));
		
		try {
			es.updateDocument(child_neighbors, id_toCorrect);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
