package PCN;

import java.util.ArrayList;

import Threads.WritePCNToDBThread;
import DB.ElasticSearchService;
import GUI.ClustersPanel;
/***
 * Write PCN to db in multithreading
 * @author Oron
 *
 */
public class WritePCNtoDB 
{
	/**
	 * client
	 */
	private ElasticSearchService es ; 
	/**
	 * file name
	 */
	private static String pcn_file_name ; 
	/**
	 * number of files to read 
	 */
	private static int numberOfFiles ; 
	/**
	 * start index 
	 */
	private static int currentFileNumber = -1;
	/**
	 * number of threads is correspond to the number of cpus in the machine
	 */
	private int NumberOfThreads = Runtime.getRuntime().availableProcessors();
	/**
	 * thread pool
	 */
	private ArrayList<WritePCNToDBThread> threads ; 
	/**
	 * constructor
	 * @param pcn_file_name
	 * @param numberOfFiles
	 * @param index
	 * @param type
	 * @param startFromLast - if the user wants to continue writing from last document
	 */
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
	/**
	 * flush bulk
	 */
	public void flushBulk()
	{
		es.bulkProcessor.flush();
	}
	/**
	 * initialize threads
	 */
	private void CreateThreads()
	{
		for(int i = 0 ; i < NumberOfThreads ; i ++)
			threads.add(new WritePCNToDBThread(pcn_file_name, es));
	}
	/**
	 * start threads 
	 * @throws InterruptedException
	 */
	private void startThreads() throws InterruptedException
	{
		for(WritePCNToDBThread t : threads)
			t.start();
		
		for(WritePCNToDBThread t : threads)
			t.join();
		
	}
	/**
	 * next index to read 
	 * @return
	 */
	public static synchronized int getNextFileNumber()
	{
		if ( currentFileNumber == numberOfFiles - 1)
			return -1 ; 
		currentFileNumber++;
		ClustersPanel.updateProgress(currentFileNumber);
		System.out.println("Current File: " + pcn_file_name+currentFileNumber );
		return currentFileNumber; 
	}
}
