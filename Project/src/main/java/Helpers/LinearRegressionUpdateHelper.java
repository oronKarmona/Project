package Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Calculation.HammingCalculation;
import Calculation.MeanRMSD;
import DB.ElasticSearchService;
import Jama.Matrix;
import PCN.NodePCN;
import Project.TrainingData.Protein;
import PCN.Node;

public class LinearRegressionUpdateHelper {

	
	private ElasticSearchService readClustersClient;
	private double[] m_beta;
	private int m_numOfIndex , m_power;
	private String m_elastic_search_index;
	private HammingCalculation m_humming;
	ArrayList<Protein> m_uknownStructurePDB, m_knownStructrePDB;
	private MeanRMSD m_meanRMSD;
	private static Map<Integer , Protein> protein_map  = new HashMap<Integer , Protein>();

	public LinearRegressionUpdateHelper(String elastic_search_index, int numOfIndex, int power,
			int threshold,ArrayList<Protein> uknownStructurePDB,ArrayList<Protein> knownStructrePDB)
	{
		
		m_uknownStructurePDB = uknownStructurePDB;
		m_knownStructrePDB=knownStructrePDB;
		setProteinsMap();
		
		m_power = power;
		m_numOfIndex = numOfIndex;
		
		m_meanRMSD= new MeanRMSD();
		m_elastic_search_index= elastic_search_index;
		try {
			m_humming = new HammingCalculation(threshold);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		readRegressionFile();
		update();
	}
	
	private void readRegressionFile() {

		m_beta = JSONhelper.readCoefficientsRegression("regression_coefficients");

	}

	private void update(){
		
		NodePCN node;
		int x,y ;
		Protein current_protein, son_protein;
		String contextNode, contextNeighbor;
		ArrayList<Double> meanRmsd = new ArrayList<Double>(); 
		Double[] meanRmsdArray;
		
		for(int i=0 ; i < m_numOfIndex; i++){
		 
			readClustersClient = new ElasticSearchService(m_elastic_search_index,i+"");
			
			for(int j = 0; j < readClustersClient.getCountOfDocInType() ; j++)
			{
			node = readClustersClient.getVertexAt(j);
			
			//calc x and y hamming
			for (Node neighbor : node.getNeighbors()) {
				
				
				current_protein=  protein_map.get((int ) node.getProteinIndex());
				son_protein =  protein_map.get((int)neighbor.getProteinIndex());
			
				m_humming.Calculate(current_protein.GetFragments(node.getFragmentIndex()), 
									son_protein.GetFragments(neighbor.getFragmentIndex()));
				x= m_humming.getHammingDistance();
				
				
				m_humming.setHammingDistance(0); // init humming
				contextNode=current_protein.GetContext(node.getFragmentIndex());
				contextNeighbor = son_protein.GetContext(neighbor.getFragmentIndex());
				if(contextNode == null ||contextNeighbor == null)
				{
					y= 40;
				}
				else{
					m_humming.Calculate(contextNode,contextNeighbor);
					y = m_humming.getHammingDistance();
				}
				m_meanRMSD.setMeanRMSD(x, y, m_power, m_beta);
				meanRmsd.add(m_meanRMSD.getMeanRmsd());	
				
				}//root of cluster
			//update to DB
			
			meanRmsdArray = meanRmsd.toArray(new Double[meanRmsd.size()]);
			readClustersClient.updateDocument(meanRmsdArray, j,"mean_rmsd");
			}
		}
		}
	
	
	private void setProteinsMap()
	{
		for(Protein p : m_uknownStructurePDB)
			protein_map.put(p.getProteinIndex(),p);
		for(int i = 0 ; i < m_knownStructrePDB.size() ; i++)
			protein_map.put(i + 320572, m_knownStructrePDB.get(i)); //320572

	}
	
}
