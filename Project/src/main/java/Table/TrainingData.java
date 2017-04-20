package Table;

import java.util.ArrayList;
import Project.TrainingData.BuildTrainningDataTheard;
import Project.TrainingData.Protein;

public class TrainingData {
	
	public static ArrayList<Protein> m_proteinsDB;
	public static ArrayList<TrainingDataEntry> TrainingData;
	
	private final int theardNum = 4;
	public TrainingData(ArrayList<Protein> proteinsDB){
		
		TrainingData = new ArrayList<>();
		m_proteinsDB = proteinsDB;
		initTraningData();
	}


	private void initTraningData() {
		
		ArrayList<BuildTrainningDataTheard> TheardList = new ArrayList<>();
		int threadDiv = Math.round(m_proteinsDB.size()/4);
		int start, end;
		for(int i = 0; i< theardNum; i++){
			
			start= i*threadDiv;
			end =  threadDiv + start;
			
			if(i == theardNum -1){
				end = m_proteinsDB.size() ;
			}
			TheardList.add(new BuildTrainningDataTheard(m_proteinsDB,start,end));	
		}
		
		for (BuildTrainningDataTheard buildTrainningDataTheard : TheardList) {
			buildTrainningDataTheard.start();			
			}
		try{
			for (BuildTrainningDataTheard buildTrainningDataTheard : TheardList) {
				buildTrainningDataTheard.join();
				}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		for (BuildTrainningDataTheard buildTrainningDataTheard : TheardList) {
			TrainingData.addAll(buildTrainningDataTheard.GetTrainingData());			
		}
		
		System.out.println(TrainingData.size());
		
				
		
	}
}
