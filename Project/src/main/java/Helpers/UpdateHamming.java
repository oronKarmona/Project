package Helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.index.get.GetField;

import DB.ElasticSearchService;
import Project.TrainingData.Protein;
import Threads.UpdateHammingThread;
/***
 * update hamming distance for giver database - internal usage only
 * used for checking the correctness of the data in the training data
 * @author Oron
 *
 */
public class UpdateHamming 
{
	private ArrayList<Protein> proteinsdb ; 
	private Map<String, Object> map ;  
	private  ElasticSearchService es ;
	private Map<Integer , Integer> indexMap;
	private static int indexToUpdate = -1 ;
	private ArrayList<UpdateHammingThread> threads;
	private static long count_of_docs ; 
	
	/***
	 * constructor
	 * @param proteinsdb
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public UpdateHamming(ArrayList<Protein> proteinsdb) throws IOException, InterruptedException, ExecutionException
	{
		es = new ElasticSearchService("proteins" , "trainingdata");
		threads = new ArrayList<UpdateHammingThread>();
		indexMap = new HashMap<Integer,Integer>();
		int ctr = 0 ;
		this.proteinsdb = proteinsdb;
		count_of_docs = es.getCountOfDocInType() ;
		for(int i = 0 ; i< proteinsdb.size() ; i++)
		{
			indexMap.put(proteinsdb.get(i).getProteinIndex(), i);
		}
		int threadNum = Runtime.getRuntime().availableProcessors();
		for(int i = 0 ; i <threadNum ; i++ )
			threads.add(new UpdateHammingThread(proteinsdb, indexMap));

	
		
		for(UpdateHammingThread t : threads)
			t.start();
		
		for(UpdateHammingThread t : threads)
			t.join();
		
		for(UpdateHammingThread t : threads)
			ctr += t.getCtr();
		
		System.out.println("Final " + ctr);
	
	}
	
	public static synchronized int getNextIndex()
	{
		if(indexToUpdate == count_of_docs )
			return -1 ;
		
		indexToUpdate++;
		
		if(indexToUpdate % 100 == 0)
			System.out.println(indexToUpdate);
		
		return indexToUpdate;
	}
	
}
