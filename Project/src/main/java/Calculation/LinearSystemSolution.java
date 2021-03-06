package Calculation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import Threads.CalculateLinearVariablesThread;
import DB.ElasticSearchService;
import Helpers.LinearTableValues;

public class LinearSystemSolution {
	private static long sizeofTrainingData;
	private int numberOfThreads; 
	private static long index = -1; 
	private ElasticSearchService trainingDataClient ,proteinsDataClient , linearDataClient;
	private ArrayList<CalculateLinearVariablesThread> threads ; 
	private static PrintWriter out;
	private static boolean firstLine = true;
	
	public LinearSystemSolution() throws IOException
	{
		trainingDataClient = new ElasticSearchService("project" , "trainingdata");
		proteinsDataClient = new ElasticSearchService("proteins", "known_structure");
		linearDataClient = new ElasticSearchService("linear_reg","xy_values");
		
		sizeofTrainingData = trainingDataClient.getCountOfDocInType() - 1;
		
		threads = new ArrayList<CalculateLinearVariablesThread>();
		numberOfThreads = Runtime.getRuntime().availableProcessors();
		
		out = new PrintWriter(new BufferedWriter(new FileWriter("xyValues", true)));
		createThreads();
		startThreads();
		out.close();
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
		if(index%1000 == 0)
			System.out.println("index " + index  );
		
		if(index >= sizeofTrainingData)
			return -1 ; 
		
		return index;
		
	}
	
	public static synchronized void save_to_file(LinearTableValues xy)
	{
		if(firstLine)
		{
			firstLine = false;
			out.println(xy.getColumnsNames());
		}
		out.println(xy.toString());
		
	}
	
}
