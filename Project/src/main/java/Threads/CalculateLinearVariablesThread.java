package Threads;

import java.util.Map;

import Calculation.HammingCalculation;
import DB.ElasticSearchService;
import Helpers.LinearTableValues;
import Project.TrainingData.LinearSystemSolution;
import Project.TrainingData.Protein;

public class CalculateLinearVariablesThread extends Thread{

	private ElasticSearchService trainingDataClient ;
	private ElasticSearchService proteinsDataClient ;
	private ElasticSearchService linearDataClient ;
	private Map<String,Object> trainingDataRecord , proteinAsMap;
	private HammingCalculation m_hammingCalculation;
	private int firstProteinIndex , secondProteinIndex ,fragmentHammingDistance , contextHammingDistance; 
	private Protein firstProtein , secondProtein;
	private int firstProteinFragment , secondProteinFragment;
	private String firstContext = null, secondContext = null;
	public CalculateLinearVariablesThread( ElasticSearchService trainingDataClient, ElasticSearchService proteinsDataClient, 
											ElasticSearchService linearDataClient) {
		
		this.trainingDataClient = new ElasticSearchService("project" , "trainingdata");
		this.proteinsDataClient = new ElasticSearchService("proteins", "known_structure");
		this.linearDataClient = linearDataClient;
		
		try {
			m_hammingCalculation = new HammingCalculation(60);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void run()
	{
		long index ; 
		
		while((index = LinearSystemSolution.getNextIndex()) != -1)
		{
			calculateVariables(index);
		}
	}
	
	
	private void calculateVariables(long index)
	{
		trainingDataRecord = trainingDataClient.get((int)index);
		
		firstProteinIndex = (int) trainingDataRecord.get("firstProteinIndex");
		secondProteinIndex = (int) trainingDataRecord.get("secondProteinIndex");
		firstProteinFragment  = (int) trainingDataRecord.get("firstFragmentIndex");
		secondProteinFragment = (int) trainingDataRecord.get("secondFragmentIndex");
		
		fragmentHammingDistance = (int)trainingDataRecord.get("HammingDistance");
		
		firstProtein = getProteinFromDB(firstProteinIndex) ; 
		secondProtein = getProteinFromDB(secondProteinIndex);
		
		firstContext = null;
		secondContext = null;
		
		firstContext = firstProtein.GetContext((int)firstProteinFragment);
		secondContext = secondProtein.GetContext((int)secondProteinFragment);
		
		if(firstContext == null || secondContext == null)
			return;
		
		 m_hammingCalculation.Calculate(firstContext, secondContext);
		 contextHammingDistance = m_hammingCalculation.getHammingDistance();
		 
		 linearDataClient.addToBulk(new LinearTableValues((int)fragmentHammingDistance, (int)contextHammingDistance));
		
	}
	
	
	
	private Protein getProteinFromDB(int index )
	{
		proteinAsMap = proteinsDataClient.getProtein(index);
		
		String aminoAcid = (String)proteinAsMap.get("aminoAcids");
		Protein p =  new Protein();
		p.setAminoAcids(aminoAcid);
		
		return p ; 
	}
}
