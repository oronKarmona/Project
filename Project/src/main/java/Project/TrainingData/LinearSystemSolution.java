package Project.TrainingData;

import java.util.ArrayList;

import Threads.CalculateLinearVariablesThread;
import DB.ElasticSearchService;

public class LinearSystemSolution {
	private static long sizeofTrainingData;
	private int numberOfThreads; 
	private static long index = -1; 
	private ElasticSearchService trainingDataClient ,proteinsDataClient , linearDataClient;
	private ArrayList<CalculateLinearVariablesThread> threads ; 
	
	public LinearSystemSolution()
	{
		trainingDataClient = new ElasticSearchService("project" , "trainingdata");
		proteinsDataClient = new ElasticSearchService("proteins", "known_structure");
		linearDataClient = new ElasticSearchService("linear_reg","xy_values");
		
		sizeofTrainingData = trainingDataClient.getCountOfDocInType();
		
		threads = new ArrayList<CalculateLinearVariablesThread>();
		numberOfThreads = Runtime.getRuntime().availableProcessors();
		
		createThreads();
		startThreads();
		
		this.closeClients();
	}
	
	private void closeClients()
	{
		proteinsDataClient.clientClose();
		trainingDataClient.clientClose();
		linearDataClient.clientClose();
	}
	private void createThreads()
	{
		for(int i = 0 ; i < numberOfThreads ; i++)
			threads.add(new CalculateLinearVariablesThread(trainingDataClient , proteinsDataClient , linearDataClient));
	}
	private void startThreads()
	{
		
		
		for(CalculateLinearVariablesThread t : threads)
			t.start();
		
		for(CalculateLinearVariablesThread t : threads)
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	public static synchronized long getNextIndex()
	{
		index++;
		
		if(index == sizeofTrainingData)
			return -1 ; 
		
		return index;
		
	}
	
	
}
