package Calculation;

import java.util.List;

import DB.ElasticSearchService;
import PCN.Node;
import PCN.NodePCN;
import ProGAL.geom3d.Point;

public class WeightFunctionCalculation {

	private ElasticSearchService m_cluterClient;
	private double m_miu;

	public WeightFunctionCalculation(int clusterNum, double miu){
		
		m_miu = miu;
		m_cluterClient = new ElasticSearchService("cluster", clusterNum+"");
		Calculate();

	}
	public void Calculate(){
		
		NodePCN node = new NodePCN();
		
		int count =(int) (m_cluterClient.getCountOfDocInType() -2); 
		double weight = 0;
		int sum = 0;
		for(int i=0;i<count;i++){
			
			node = m_cluterClient.getVertexAt(i);
			
			for (Node neighbor : node.getNeighbors()) {
				sum = neighbor.getMeanRmsd()
			}
			
		}

	
	}
	
	
}
