package Calculation;

import java.util.ArrayList;
import java.util.List;

import DB.ElasticSearchService;
import PCN.Node;
import PCN.NodePCN;
import ProGAL.geom3d.Point;

public class WeightFunctionCalculation implements IWeightFunctionCalculation {

	private ElasticSearchService m_cluterClient;
	private double m_miu;

	public WeightFunctionCalculation(int clusterNum, double miu){
		
		m_miu = miu;
		m_cluterClient = new ElasticSearchService("cluster", clusterNum+"");
		Calculate();

	}
	public Double[] Calculate(){
		
		NodePCN nodeX, nodeY = new NodePCN();
		
		int count =(int) (m_cluterClient.getCountOfDocInType() -2); 
		double sumX=0,sumY=0;
		double epsilon = 0;
		int j = 0;
		
		for(int i=0;i<count;i++){
			System.out.println("current run index " + i + "until " + count);
			nodeX = m_cluterClient.getVertexAt(i);
			Double[] weight = new Double[nodeX.getNeighbors().size()];
			j = 0 ; sumX = 0 ;
			//mean(Rmsd(xi,n))
			for (Node xiN : nodeX.getNeighbors()) {
				sumX += xiN.getMeanRmsd();
			}

			
			//mean(Rmsd(xj,n))
			for (int index = 0 ; index < nodeX.getNeighbors().size() ; index++) 
			{
				nodeY = m_cluterClient.SearchPCNDB((long)nodeX.getNeighbors().get(index).getProteinIndex()
														, nodeX.getNeighbors().get(index).getFragmentIndex());
				if(nodeY == null)
					nodeY = new NodePCN((long)nodeX.getNeighbors().get(index).getProteinIndex()
														, nodeX.getNeighbors().get(index).getFragmentIndex());
				
				nodeY.setMeanRmsd(nodeX.getNeighbors().get(index).getMeanRmsd());
				
				for (Node xjN : nodeY.getNeighbors()) 
				{
					sumY += xjN.getMeanRmsd();
				}
				epsilon = (sumX+ sumY)/3;
				
				weight[j] = Math.pow(Math.E,(-1*Math.pow(( nodeY.getMeanRmsd()),2))/(m_miu*epsilon));
//				weight[j] = Math.pow(Math.E,(-1*Math.pow(( nodeY.getMeanRmsd()),2)));
				j++;
				
				sumY = 0 ;
			}
			
			
			m_cluterClient.updateDocument(weight, i,"weight");
			
		}
		
		return null;

	
	}
	
	
}
