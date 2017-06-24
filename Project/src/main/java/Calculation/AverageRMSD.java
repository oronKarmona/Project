package Calculation;

import java.util.ArrayList;

import DB.ElasticSearchService;
import Helpers.JSONhelper;
import Threads.MeanRMSDThread;
import Threads.UpdateHammingThread;
/***
 * Mean RMSD according to Hamming distance in db
 * @author Oron
 *
 */
public class AverageRMSD 
{
	/***
	 * number of docs in the elastic search index /type
	 */
	private static long documentsCount ;
	/***
	 * elastic search client
	 */
	private ElasticSearchService es ; 
	/**
	 * index and type for the elastic search
	 */
	private String index , type;
	/**
	 * working threads
	 */
	private ArrayList<MeanRMSDThread> threads ;
	/**
	 * index to update in the output , starting for -1 due to the first loop iteration
	 */
	private static int indexToUpdate = -1 ;
	/***
	 * hamming distance factor
	 */
	private int HammingDistanceFactor;
	private int numberOfThreads ;
	
	/**
	 * constructor 
	 * @param index - elastic search index 
	 * @param type - elastic search type 
	 * @param HammingDistanceFactor - factor
	 */
	public AverageRMSD(String index , String type , int HammingDistanceFactor) 
	{
		this.index = index ; this.type = type ; 
		this.HammingDistanceFactor = HammingDistanceFactor;
		es = new ElasticSearchService(this.index,this.type);
		documentsCount = es.getCountOfDocInType() - 1;
		numberOfThreads = Runtime.getRuntime().availableProcessors();
		threads = new ArrayList<MeanRMSDThread>();
		
		this.createThreads();
		
		try {
			this.startThreads();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		double[] meanRmsdValues = this.sum_arrays();
		JSONhelper.WriteMeanRMSD(meanRmsdValues, "MeanRMSD");
	}
	/***
	 * creating the thread 
	 */
	private void createThreads()
	{
		for(int i = 0 ; i < numberOfThreads ; i++)
			threads.add(new MeanRMSDThread(this.index , this.type,this.HammingDistanceFactor));
	}
	/**
	 * starting threads 
	 * @throws InterruptedException
	 */
	private void startThreads() throws InterruptedException
	{
		if(threads == null )
			return ;
		
		for(MeanRMSDThread t : threads)
			t.start();
		
		for(MeanRMSDThread t : threads)
			t.join();
	}
	/**
	 * sums the array of the working threads 
	 * @return avearage rmsd array
	 */
	private double[] sum_arrays()
	{
		if(threads == null)
			return null;
		
		double[] MeanRMSDarray = new double[this.HammingDistanceFactor];
		int[] Counterarray = new int[this.HammingDistanceFactor];
		double[] RMSDarray = new double[this.HammingDistanceFactor];
		
		int[] tempCounters = new int[this.HammingDistanceFactor];
		double[] tempRMSD = new double[this.HammingDistanceFactor];
		
		for(MeanRMSDThread t : threads)
		{
			tempCounters  = t.getCountArray().clone();
			tempRMSD = t.getRmsdValuesArray().clone();
			
			for(int i = 0 ; i < HammingDistanceFactor ; i++)
			{
				Counterarray[i] += tempCounters[i];
				RMSDarray[i] += tempRMSD[i];
			}
		}
		
		for(int i = 0 ; i < HammingDistanceFactor ; i++)
		{
			if(Counterarray[i] != 0)
			{
				MeanRMSDarray[i] = RMSDarray[i] / Counterarray[i] ; 
			}
				
		}
		
		return MeanRMSDarray;
	}
	
	/**
	 * retrun the next index to be manipulated
	 * @return
	 */
	public static synchronized int getNextIndex()
	{
		if(indexToUpdate == documentsCount )
			return -1 ;
		
		indexToUpdate++;
		
		if(indexToUpdate % 100 == 0)
			System.out.println(indexToUpdate);
		
		return indexToUpdate;
	}

}
