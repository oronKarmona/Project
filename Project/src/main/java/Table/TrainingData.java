package Table;

import java.util.ArrayList;

import GUI.ProgressBar;
import Project.TrainingData.BuildTrainningDataTheard;
import Project.TrainingData.Protein;

public class TrainingData {
	
	public static ArrayList<Protein> m_proteinsDB;
	public static ArrayList<TrainingDataEntry> TrainingData;
	private static ProgressBar pb ;
	private final int theardNum = 4;
	public TrainingData(ArrayList<Protein> proteinsDB){
		
		TrainingData = new ArrayList<>();
		m_proteinsDB = proteinsDB;
		initTraningData();
	}

	
	private void initTraningData() {
		long startTime = System.currentTimeMillis();
		long endTime ; 
		pb = new ProgressBar("Training Data Calculation");
		
		ArrayList<BuildTrainningDataTheard> TheardList = new ArrayList<>();
		
		int threadDiv = Math.round(m_proteinsDB.size()/theardNum);
		int start, end;
		
		for(int i = 0; i< theardNum; i++)
		{
			
			start= i*threadDiv;
			end =  threadDiv + start;
			
			if(i == theardNum -1) // last thread takes the reminder 
			{
				end = m_proteinsDB.size() ;
			}
			
			pb.addThreadData(start, end - 2, i);
			
			TheardList.add(new BuildTrainningDataTheard(m_proteinsDB,start,end,i));	
		}
		System.out.println("Starting Training data calculation");
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
		System.out.println("0 "+TheardList.get(0).isAlive());
		System.out.println("1 "+TheardList.get(1).isAlive());
		System.out.println("2 "+TheardList.get(2).isAlive());
		System.out.println("2 "+TheardList.get(3).isAlive());
		for (BuildTrainningDataTheard buildTrainningDataTheard : TheardList) {
			TrainingData.addAll(buildTrainningDataTheard.GetTrainingData());			
		}
		
		System.out.println(TrainingData.size());
		
		endTime = System.currentTimeMillis();
		long totalTime = (endTime  - startTime ) /(1000 * 60);
		System.out.println("Total calculation time: " + totalTime + " minutes");
		
	}
	/***
	 * Updating the progress bar for specific thread
	 * @param progress - the progress that updated
	 * @param Threadindex - index of the thread to be updated
	 */
	public  static synchronized void UpdateProgress(int progress , int Threadindex)	
	{
		pb.setData(progress , Threadindex);
	}
}
