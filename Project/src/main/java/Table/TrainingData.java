package Table;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import GUI.ProgressBar;
import Project.TrainingData.App;
import Project.TrainingData.BuildTrainningDataTheard;
import Project.TrainingData.Protein;

/***
 * This class purpose is to manage the training data calculation
 * @author Oron
 *
 */
public class TrainingData {
	/***
	 * Protein DB after the filtering 
	 */
	public static ArrayList<Protein> m_proteinsDB;
	/***
	 * Training data details
	 */
	public static ArrayList<TrainingDataEntry> TrainingData;
	/***
	 * progress bar for visualising the progress of each thread
	 */
	private static ProgressBar pb ;
	/***
	 * Number of threads assigned to the calculation
	 */
	private int threadNum;
	/***
	 * Constructor
	 * @param proteinsDB - protein DB after filtering
	 */
	public TrainingData(ArrayList<Protein> proteinsDB){
		
		TrainingData = new ArrayList<>();
		m_proteinsDB = proteinsDB;
		threadNum = Runtime.getRuntime().availableProcessors();
		initTraningData();
	}

	/***
	 * This method will initiate the Threads to calculate the training data
	 */
	private void initTraningData() {
		long startTime = System.currentTimeMillis();
		long endTime ; 
		pb = new ProgressBar("Training Data Calculation");

		ArrayList<BuildTrainningDataTheard> TheardList = new ArrayList<>();
		
		int threadDiv = Math.round(m_proteinsDB.size()/threadNum);
		int start, end;
		
		for(int i = 0; i< threadNum; i++)
		{
			
			start= i*threadDiv;
			end =  threadDiv + start;
			
			if(i == threadNum -1) // last thread takes the reminder 
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
		
//		try (Writer writer = new FileWriter("TrainingData.json")) 
//		{
//		    Gson gson = new GsonBuilder().create();
//		    
//		    gson.toJson(TrainingData, writer);
//		    
//		  
//		} catch (IOException e)
//		{
//			e.printStackTrace();
//		}
		
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
