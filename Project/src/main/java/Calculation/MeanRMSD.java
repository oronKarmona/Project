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
public class MeanRMSD 
{
	private static long documentsCount ;
	private ElasticSearchService es ; 
	private String index , type;
	private ArrayList<MeanRMSDThread> threads ;
	private static int indexToUpdate = -1 ;
	private int HammingDistanceFactor;
	private int numberOfThreads ;
	
	public MeanRMSD(String index , String type , int HammingDistanceFactor) 
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
	
	private void createThreads()
	{
		for(int i = 0 ; i < numberOfThreads ; i++)
			threads.add(new MeanRMSDThread(this.index , this.type,this.HammingDistanceFactor));
	}
	
	private void startThreads() throws InterruptedException
	{
		if(threads == null )
			return ;
		
		for(MeanRMSDThread t : threads)
			t.start();
		
		for(MeanRMSDThread t : threads)
			t.join();
	}
	
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
