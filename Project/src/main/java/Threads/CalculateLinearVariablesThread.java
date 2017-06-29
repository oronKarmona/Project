package Threads;

import java.util.Map;

import Calculation.HammingCalculation;
import Calculation.LinearSystemSolution;
import DB.ElasticSearchService;
import Helpers.LinearTableValues;
import Protein.Protein;
/***
 * Calculate the linear regression x , y values for each document in the training data index 
 * @author Oron
 *
 */
public class CalculateLinearVariablesThread extends Thread{

	/***
	 * ElasticSearch for training data index in the elasticSearch
	 */
	private ElasticSearchService trainingDataClient ;
	/***
	 * ElasticSearch for proteins data index in the elasticSearch
	 */
	private ElasticSearchService proteinsDataClient ;
	/***
	 * ElasticSearch for sacing results in  linear data index in the elasticSearch (optional)
	 */
	private ElasticSearchService linearDataClient ;
	/***
	 * Maps for easy access to records of the elasticsearch
	 */
	private Map<String,Object> trainingDataRecord , proteinAsMap;
	/***
	 * Hamming distance calculation object
	 */
	private HammingCalculation m_hammingCalculation;
	/***
	 * details of the training data the needs to be considered for the linear values
	 */
	private int firstProteinIndex , secondProteinIndex ,fragmentHammingDistance , contextHammingDistance; 
	/***
	 * Proteins of the comparison 
	 */
	private Protein firstProtein , secondProtein;
	/***
	 * Fragments of the compared proteins
	 */
	private int firstProteinFragment , secondProteinFragment;
	/***
	 * RMSD result of the compared proteins
	 */
	private double rmsd ; 
	/***
	 * context of the proteins fragments (10 amino acids for each side of the fragment [ 10 + fragment + 10 ] ) 
	 */
	private String firstContext = null, secondContext = null;
	
	/***
	 * Constructor
	 * @param trainingDataClient
	 * @param proteinsDataClient
	 * @param linearDataClient
	 */
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
	
	/***
	 * calculation function 
	 * @param index - index to be manipulated in the training data 
	 */
	private void calculateVariables(long index)
	{
		trainingDataRecord = trainingDataClient.get((int)index);
		try{
		firstProteinIndex = (int) trainingDataRecord.get("firstProteinIndex");
		}catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		secondProteinIndex = (int) trainingDataRecord.get("secondProteinIndex");
		firstProteinFragment  = (int) trainingDataRecord.get("firstFragmentIndex");
		secondProteinFragment = (int) trainingDataRecord.get("secondFragmentIndex");
		
		fragmentHammingDistance = (int)trainingDataRecord.get("HammingDistance");
		rmsd = (double)trainingDataRecord.get("RMSDResult");
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
		 
		// linearDataClient.addToBulk(new LinearTableValues((int)fragmentHammingDistance, (int)contextHammingDistance));
		LinearSystemSolution.save_to_file(new LinearTableValues((int)fragmentHammingDistance, (int)contextHammingDistance , (double) rmsd, 4));
	}
	
	
	/***
	 * Gets protein according to its index 
	 * @param index - protein index 
	 * @return protein with the provided index 
	 */
	private Protein getProteinFromDB(int index )
	{
		proteinAsMap = proteinsDataClient.getProtein(index);
		
		String aminoAcid = (String)proteinAsMap.get("aminoAcids");
		Protein p =  new Protein();
		p.setAminoAcids(aminoAcid);
		
		return p ; 
	}
}
