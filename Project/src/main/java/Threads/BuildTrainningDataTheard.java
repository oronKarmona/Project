package Threads;

import java.util.ArrayList;

import Calculation.HammingCalculation;
import Calculation.RMSDCalculation;
import DB.ElasticSearchService;
import Protein.Protein;
import Table.TrainingData;
import Table.TrainingDataEntry;
/***
 * Thread class for building making the training data claculation 
 * @author Oron
 *
 */
public class BuildTrainningDataTheard extends Thread{
	/***
	 * known structure proteins 
	 */
	private static ArrayList<Protein> m_proteinsDB;
	/***
	 * Training data entries
	 */
	private ArrayList<TrainingDataEntry> m_trainingData;
	/***
	 * Hamming distance object for calculating the hamming distance due to a threshold
	 */
	private HammingCalculation m_hammingCalculation;
	/***
	 * Default hamming distance in case of wrong hamming distance set by the user 
	 */
	private static final double threshold = 60;
	/**
	 * current time for testing purpose
	 */
	private long current_time ;
	/***
	 * writing data to the elasticsearch db
	 */
	private ElasticSearchService m_elasticSearchService;
	/***
	 * to be used for progress bar
	 */
	private int m_Threadindex; 
	/***
	 * indices for the for loops
	 */
	private int i=0	,j = 0;
	/***
	 * fragment count of the compared proteins
	 */
	private int p1_fragment_count,p2_fragment_count;
	/***
	 * Second protein of the comparison
	 */
	private Protein protein;
	/***
	 * indices of the compared proteins
	 */
	private int index , p;
	/***
	 * Data entry of the training data
	 */
	private TrainingDataEntry dataEntry  = new TrainingDataEntry();
	/***
	 * rmsd calculation result
	 */
	private RMSDCalculation rmsd;
	/***
	 * Constructor
	 * @param proteinsDB - database of the known structural proteins
	 * @param Threadindex - thread index 
	 * @param elasticSearchService - elasticSearch client
	 * @param hammingThreshold - hamming distance threshold set by the user
	 */
	public BuildTrainningDataTheard(ArrayList<Protein> proteinsDB, int Threadindex, ElasticSearchService elasticSearchService , double hammingThreshold)
	{
		try{
			m_hammingCalculation = new HammingCalculation(hammingThreshold);
		}
		catch(Exception e){
			e.printStackTrace();
			try {
				System.out.println("Setting Threshold to 60% for recovering from user error");
				m_hammingCalculation = new HammingCalculation(threshold);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		m_elasticSearchService = elasticSearchService;
		m_trainingData = new ArrayList<>();
		m_proteinsDB = proteinsDB;
		m_Threadindex = Threadindex;
		rmsd = new RMSDCalculation();
	}


	@Override 
	public void run() {
		
		int protein_get ; 
		current_time = System.currentTimeMillis(); 
		while( ( protein_get = TrainingData.IndexForThread() ) != -1)
		{
//			TrainingData.ResetProgress(m_Threadindex);
			initTable(m_proteinsDB.get(protein_get));
		}
		
	}
	/***
	 * Making the calculation of the training data table
	 * @param proteinToCompareTo - first protein of the comparison 
	 */
	private void initTable(Protein proteinToCompareTo)
	{//1
		 index = m_proteinsDB.indexOf(proteinToCompareTo); 
				 p1_fragment_count = proteinToCompareTo.getFragment_count();
		
		
		for( p = index; p< m_proteinsDB.size(); p++)
		{
		    protein= m_proteinsDB.get(p);
		    p2_fragment_count = protein.getFragment_count();
		    
		    for( i=0	;	i< p1_fragment_count	;	i++)
			{			
				for( j=0	; j<p2_fragment_count	;	j++)
				{	
					if(index == p && i == j ) // don't check the same fragment of the same protein
						break;
					if(m_hammingCalculation.Calculate(proteinToCompareTo.GetFragments(i), protein.GetFragments(j)))
					{
						 dataEntry.Set(proteinToCompareTo.getProteinIndex(), 
								protein.getProteinIndex(),i,j);
						
						dataEntry.setRMSDResult(rmsd.Calculate(proteinToCompareTo.getFragmentCoordinates(i),protein.getFragmentCoordinates(j)));
						dataEntry.setHammingDistance(m_hammingCalculation.getHammingDistance());
						//TrainingData.addToWriteQue(dataEntry);
						//m_elasticSearchService.add(dataEntry);
						m_elasticSearchService.addToBulk(dataEntry);
						//m_trainingData.add(dataEntry);
					
					}

				}//j
			}
		    //i
//		    if((System.currentTimeMillis() - current_time)/(1000*60) > 0.5 ) // if the time interval is at least half minute
//		    {
//		    	current_time = System.currentTimeMillis();
//		    	TrainingData.UpdateProgress(p, m_Threadindex);
//		    }
		}//p
		
		
		
	}//1
	
	public ArrayList<TrainingDataEntry> GetTrainingData() {
		return m_trainingData;
	}
  
}
