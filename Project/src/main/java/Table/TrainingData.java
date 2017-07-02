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
import GUI.MyProgressBar;
import Main.App;
import Protein.Protein;
import Threads.BuildTrainningDataTheard;
import Threads.ElasticSearchWriteThread;

/***
 * Manages the training data calculation
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
	private static MyProgressBar pb ;
	/***
	 * Number of threads assigned to the calculation
	 */
	private int threadNum;
	/***
	 * Single thread for writing to the elastic Search (second implementation approach)
	 */
	ElasticSearchWriteThread btt = null;
	/***
	 * Last protein read 
	 */
	private static int LastRead = 0 ;
	/***
	 * barrier for asynchrounos calculation with threads
	 */
	private static int barrier = 0 ;
	/**
	 * Boolean attribute for signing the first iterations of the threads 
	 */
	private static boolean firstTime = true;
	/***
	 * elastic type defined by the user 
	 */
	private String elasticType;
	/***
	 * hamming distance defined by the user 
	 * default value is 60
	 */
	private double hammingThreshold = 60;
	
	public double getHammingThreshold() {
		return hammingThreshold;
	}
	public void setHammingThreshold(double hammingThreshold) {
		this.hammingThreshold = hammingThreshold;
	}

	/***
	 * ElasticSearch client 
	 */
	public static ElasticSearchService elasticSearchService;
	/***
	 * Constructor and initialization of the training data calculation 
	 * @param proteinsDB - the known structural proteins 
	 * @param elasticType - elastic search type  to write to
	 * @param hammingThreshold - hamming distance threshold from the user
	 */
	public TrainingData(ArrayList<Protein> proteinsDB, String elasticType, double hammingThreshold){
		
		TrainingData = new ArrayList<>();
		m_proteinsDB = proteinsDB;
		threadNum = Runtime.getRuntime().availableProcessors();
		this.hammingThreshold = hammingThreshold;
		initDB(elasticType);
		initTraningData();
	}
	/***
	 * Initialization of the elaticSearch client 
	 * @param elasticType - elastic search type  to write to
	 */
	private void initDB(String elasticType) {

		elasticSearchService = new ElasticSearchService("trainingdata" , elasticType);
	}

	/***
	 * Initialization of threads 
	 */
	private void initTraningData() {
		long startTime = System.currentTimeMillis();
		long endTime ; 
//		pb = new ProgressBar("Training Data Calculation");

		ArrayList<BuildTrainningDataTheard> TheardList = new ArrayList<>();
				
	//	pb.addThreadData(0,m_proteinsDB.size(),-1);
		for(int i = 0 ; i < threadNum ; i++)
		{
			TheardList.add(new BuildTrainningDataTheard(m_proteinsDB, i,elasticSearchService,hammingThreshold));
		
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
	public  static synchronized void UpdateProgress(int progress , int Threadindex)	
	{
		pb.setData(progress , Threadindex);
	}
	/***
	 * reset progress bar
	 * @param ThreadIndex
	 */
	public static synchronized void ResetProgress(int ThreadIndex)
	{
		pb.resetData(ThreadIndex);
	}
	
	/***
	 * Date format for progress change 
	 */
	static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	/***
	 * Current data 
	 */
	static LocalDateTime now = LocalDateTime.now();
	/**
	 * Date formatter for user info
	 */
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	/**
	 * Gets the next index of the proteins to be calculated 
	 * @return next protein index 
	 */
	public static synchronized int IndexForThread()
	{

		
		if(LastRead >= m_proteinsDB.size() - 1 ) // if the last proteins has been calculated
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
	

	
	/**
	 * For using a writing thread , adding to write queue
	 * @param entry - entry to be written 
	 */
	public static synchronized void addToWriteQue(TrainingDataEntry entry )
	{
		synchronized(TrainingData)
		{
			
				TrainingData.notify();
		
		}
			
		TrainingData.add(entry);
	}
	
	/***
	 * Gets the first entry from the queue to the writing thread 
	 * @return TrainingDataEntry
	 */
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
	
	/**
	 * Update barrier for asynchronus use
	 */
	public static synchronized  void updateBarrier()
	{
		barrier++;
	}
}
