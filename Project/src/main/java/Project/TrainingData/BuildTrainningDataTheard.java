package Project.TrainingData;

import java.util.ArrayList;

import Calculation.HammingCalculation;
import Calculation.RMSDCalculation;
import DB.ElasticSearchService;
import Table.TrainingData;
import Table.TrainingDataEntry;

public class BuildTrainningDataTheard extends Thread{
	
	private static ArrayList<Protein> m_proteinsDB;
	private ArrayList<TrainingDataEntry> m_trainingData;
	private HammingCalculation m_hammingCalculation;
	private static final double threshold = 60;
	private long current_time ;
	private ElasticSearchService m_elasticSearchService;
	private int m_Threadindex; // to be used for progress bar
	private int i=0	,j = 0;
	private int p1_fragment_count,p2_fragment_count;
	private Protein protein;
	private int index , p;
	private TrainingDataEntry dataEntry;
	private RMSDCalculation rmsd;
	
	public BuildTrainningDataTheard(ArrayList<Protein> proteinsDB, int Threadindex, ElasticSearchService elasticSearchService)
	{
		try{
			m_hammingCalculation = new HammingCalculation(threshold);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		m_elasticSearchService = elasticSearchService;
		m_trainingData = new ArrayList<>();
		m_proteinsDB = proteinsDB;
		m_Threadindex = Threadindex;
		rmsd = new RMSDCalculation();
	}


	@Override 
	public void run() {
		
		int i ; 
		current_time = System.currentTimeMillis(); 
		while( ( i = TrainingData.IndexForThread() ) != -1)
		{
//			TrainingData.ResetProgress(m_Threadindex);
			initTable(m_proteinsDB.get(i));
		}
		TrainingData.updateBarrier();
		
	}
	
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
						 dataEntry = new TrainingDataEntry(proteinToCompareTo.getProteinIndex(), 
								protein.getProteinIndex(),i,j);
						
						dataEntry.setRMSDResult(rmsd.Calculate(proteinToCompareTo.getFragmentCoordinates(i),protein.getFragmentCoordinates(j)));
						//TrainingData.addToWriteQue(dataEntry);
						m_elasticSearchService.add(dataEntry);
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
