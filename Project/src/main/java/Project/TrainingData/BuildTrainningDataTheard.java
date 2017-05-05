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

	private ElasticSearchService m_elasticSearchService;
	private int m_Threadindex; // to be used for progress bar
	
	public BuildTrainningDataTheard(ArrayList<Protein> proteinsDB, int Threadindex, ElasticSearchService elasticSearchService)
	{
		try{
			m_hammingCalculation = new HammingCalculation(100-threshold);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		m_elasticSearchService = elasticSearchService;
		m_trainingData = new ArrayList<>();
		m_proteinsDB = proteinsDB;
		m_Threadindex = Threadindex;
		
	}


	@Override 
	public void run() {
		
		int i ; 
		while( ( i = TrainingData.IndexForThread() ) != -1)
		{
			TrainingData.ResetProgress(m_Threadindex);
			initTable(m_proteinsDB.get(i));
		}
		TrainingData.updateBarrier();
		
	}
	
	private void initTable(Protein proteinToCompareTo)
	{
		int index = m_proteinsDB.indexOf(proteinToCompareTo);
		Protein protein, big, small;
		
		
		for(int p = index; p< m_proteinsDB.size(); p++)
		{
	    protein= m_proteinsDB.get(p);
		
	   
	    
	    if(protein.getAminoAcids().length() > proteinToCompareTo.getAminoAcids().length()){
	    	big = protein;
	    	small = proteinToCompareTo;
	    }
	    else{
	    	big = proteinToCompareTo;
	    	small = protein;
	    }
	    
	    for(int i=0	;	i<big.getAminoAcids().length()-20	;	i++)
		{			
			for(int j=0	;j<small.getAminoAcids().length()-20	;	j++)
			{	
			if(m_hammingCalculation.Calculate(big.GetFragments(i), small.GetFragments(j)))
			{
			TrainingDataEntry dataEntry = new TrainingDataEntry(big.getProteinIndex(), 
																small.getProteinIndex(),
																i,
																j);
			
			dataEntry.setRMSDResult(RMSDCalculation.Calculate(big.getFragmentCoordinates(i),small.getFragmentCoordinates(j)));
			TrainingData.addToWriteQue(dataEntry);
			//m_elasticSearchService.add(dataEntry);
			//m_trainingData.add(dataEntry);
			}
//			else{
//				System.out.println(String.format("protein: %s, protein: %s match is below the threshold", 
//						 protein.GetFragments(i),proteinToCompareTo.GetFragments(j)));
//			}
			
		}
		}
	    TrainingData.UpdateProgress(p, m_Threadindex);
		}
		
		
		
	}
	
	public ArrayList<TrainingDataEntry> GetTrainingData() {
		return m_trainingData;
	}

}
