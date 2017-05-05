package Table;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import DB.ElasticSearchService;
import GUI.ProgressBar;
import Project.TrainingData.App;
import Project.TrainingData.BuildTrainningDataTheard;
import Project.TrainingData.ElasticSearchWriteThread;
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
	ElasticSearchWriteThread btt = null;
	private static boolean firstT = true;
	private static int LastRead = 0 ;
	private static int barrier = 0 ;
	private static boolean firstTime = true;
	/***
	 * Constructor
	 * @param proteinsDB - protein DB after filtering
	 */
	
	public static ElasticSearchService elasticSearchService;
	
	public TrainingData(ArrayList<Protein> proteinsDB){
		
		TrainingData = new ArrayList<>();
		m_proteinsDB = proteinsDB;
		threadNum = Runtime.getRuntime().availableProcessors() - 1;
		
		initDB();
		btt = new ElasticSearchWriteThread(elasticSearchService);
		initTraningData();
	}

	private void initDB() {

		elasticSearchService = new ElasticSearchService();
	}

	/***
	 * This method will initiate the Threads to calculate the training data
	 */
	private void initTraningData() {
		long startTime = System.currentTimeMillis();
		long endTime ; 
		pb = new ProgressBar("Training Data Calculation");

		ArrayList<BuildTrainningDataTheard> TheardList = new ArrayList<>();
				
		pb.addThreadData(0,m_proteinsDB.size(),-1);
		for(int i = 0 ; i < threadNum ; i++)
		{
			TheardList.add(new BuildTrainningDataTheard(m_proteinsDB, i,elasticSearchService));
			pb.addThreadData(0,m_proteinsDB.size() - 2 , i);
		}
		System.out.println("Starting Training data calculation");
		for (BuildTrainningDataTheard buildTrainningDataTheard : TheardList) {
			buildTrainningDataTheard.start();			
			}
		btt.start();
		try{
			for (BuildTrainningDataTheard buildTrainningDataTheard : TheardList) {
				buildTrainningDataTheard.join();
				}
			btt.join();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	
//		for (BuildTrainningDataTheard buildTrainningDataTheard : TheardList) {
//			TrainingData.addAll(buildTrainningDataTheard.GetTrainingData());			
//		}
		
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
	
	public static synchronized void ResetProgress(int ThreadIndex)
	{
		pb.resetData(ThreadIndex);
	}
	
	
	public static synchronized int IndexForThread()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
		if(LastRead == m_proteinsDB.size() - 1)
			return - 1 ;
		
		if(!firstTime)
			LastRead++;
		
		if(LastRead  == 0 )
			firstTime = false;

	
		System.out.println(LastRead);
		pb.setNumericProgress(LastRead + 1);
		pb.setData(LastRead + 1,-1); // total progress bar
		
		
		return LastRead; 
		
	}
	
	
	public static synchronized void addToWriteQue(TrainingDataEntry d )
	{
		synchronized(TrainingData)
		{
			
				TrainingData.notify();
		
		}
			
		TrainingData.add(d);
	}
	
	public static TrainingDataEntry getEntry()
	{
		if(TrainingData.isEmpty() &&  (Runtime.getRuntime().availableProcessors() - 1 != barrier))
		{
			synchronized(TrainingData)
			{
				try {
					TrainingData.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		else if(Runtime.getRuntime().availableProcessors() - 1 == barrier && TrainingData.isEmpty())
			return null;
			
		
		return TrainingData.remove(0);
		
	}
	
	public static synchronized  void updateBarrier()
	{
		barrier++;
	}
}
