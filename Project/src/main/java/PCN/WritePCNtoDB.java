package PCN;

import java.util.ArrayList;

import Threads.WritePCNToDBThread;
import DB.ElasticSearchService;

public class WritePCNtoDB 
{
	private ElasticSearchService es ; 
	private static String pcn_file_name ; 
	private static int numberOfFiles ; 
	private static int currentFileNumber = -1;
	private int NumberOfThreads = Runtime.getRuntime().availableProcessors();
	private ArrayList<WritePCNToDBThread> threads ; 
	
	public WritePCNtoDB(String pcn_file_name , int  numberOfFiles , String index , String type,
			boolean startFromLast)
	{
		this.pcn_file_name = pcn_file_name;
		this.numberOfFiles = numberOfFiles;
		threads = new ArrayList<WritePCNToDBThread>();
		es = new ElasticSearchService(index, type);
		if(startFromLast)
			es.setID(es.getCountOfDocInType() - 1);
		this.CreateThreads();
		
		try {
			
			this.startThreads();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void flushBulk()
	{
		es.bulkProcessor.flush();
	}
	private void CreateThreads()
	{
		for(int i = 0 ; i < NumberOfThreads ; i ++)
			threads.add(new WritePCNToDBThread(pcn_file_name, es));
	}
	
	private void startThreads() throws InterruptedException
	{
		for(WritePCNToDBThread t : threads)
			t.start();
		
		for(WritePCNToDBThread t : threads)
			t.join();
		
	}
	
	public static synchronized int getNextFileNumber()
	{
		if ( currentFileNumber == numberOfFiles - 1)
			return -1 ; 
		currentFileNumber++;
		System.out.println("Current File: " + pcn_file_name+currentFileNumber );
		return currentFileNumber; 
	}
}
