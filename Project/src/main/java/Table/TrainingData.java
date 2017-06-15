package Table;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import DB.ElasticSearchService;
import GUI.ProgressBar;
import Project.TrainingData.App;
import Project.TrainingData.Protein;
import Threads.BuildTrainningDataTheard;
import Threads.ElasticSearchWriteThread;

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
		threadNum = Runtime.getRuntime().availableProcessors();
		
		initDB();
		initTraningData();
	}

	private void initDB() {

		elasticSearchService = new ElasticSearchService("project" , "trainingdata");
	}

	/***
	 * This method will initiate the Threads to calculate the training data
	 */
	private void initTraningData() {
		long startTime = System.currentTimeMillis();
		long endTime ; 
//		pb = new ProgressBar("Training Data Calculation");

		ArrayList<BuildTrainningDataTheard> TheardList = new ArrayList<>();
				
	//	pb.addThreadData(0,m_proteinsDB.size(),-1);
		for(int i = 0 ; i < threadNum ; i++)
		{
			TheardList.add(new BuildTrainningDataTheard(m_proteinsDB, i,elasticSearchService));
		
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
	

		endTime = System.currentTimeMillis();
		long totalTime = (endTime  - startTime ) /(1000 * 60);
		System.out.println("Total calculation time: " + totalTime + " minutes");


	}
	/***
	 * Updating the progress bar for specific thread
	 * @param progress - the progress that updated
	 * @param Threadindex - index of the thread to be updated
	 */
//	public  static synchronized void UpdateProgress(int progress , int Threadindex)	
//	{
//		pb.setData(progress , Threadindex);
//	}
//	
//	public static synchronized void ResetProgress(int ThreadIndex)
//	{
//		pb.resetData(ThreadIndex);
//	}
	
	static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	static LocalDateTime now = LocalDateTime.now();
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public static synchronized int IndexForThread()
	{

		
		if(LastRead == 10) // if the last proteins has been calculated
			return - 1 ;

		if(!firstTime) 
			LastRead++;
		
		if(LastRead  == 0 )
			firstTime = false;

	
		if(LastRead % 100 == 0)
		{
			now = LocalDateTime.now();
			System.out.println(dtf.format(now)); //2016/11/16 12:08:43
			System.out.println(LastRead);
		}
			
		
		//pb.setNumericProgress(LastRead + 1);
		//pb.setData(LastRead + 1,-1); // total progress bar
		
		
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
