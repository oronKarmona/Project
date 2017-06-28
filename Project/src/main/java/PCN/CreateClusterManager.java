package PCN;

import java.util.ArrayList;

import ParallelBFS.CreateClusters;
import Protein.Protein;

public class CreateClusterManager 
{
	private static int number_of_clusters; 
	private ArrayList<CreateClusters> threads ; 
	private int distance_factor;
	private ArrayList<Protein> uknownStructurePDB ,knownStructrePDB;
	private int OccurenceThreshold ;
	private String elastic_search_index, elastic_search_type;
	private double threshold;
	private int number_of_threads ; 
	private static int index = -1 ;
	
	public CreateClusterManager(int number_of_clusters ,
					int distance_factor, ArrayList<Protein> uknownStructurePDB, ArrayList<Protein> knownStructrePDB , int OccurenceThreshold , 
							String elastic_search_index , String elastic_search_type,  double threshold)
							
							{
								this.number_of_clusters = number_of_clusters;
								this.distance_factor = distance_factor;
								this.uknownStructurePDB = uknownStructurePDB;
								this.knownStructrePDB = knownStructrePDB;
								this.OccurenceThreshold = OccurenceThreshold;
								this.elastic_search_index = elastic_search_index;
								this.elastic_search_type = elastic_search_type;
								this.threshold = threshold;
								threads = new ArrayList<CreateClusters>();
								this.number_of_threads = Runtime.getRuntime().availableProcessors();
								
								initialThreads();
								startThreads();
							}
	
	private void initialThreads()
	{
		for(int i = 0 ; i<number_of_threads ; i++)
			threads.add(new CreateClusters(this.distance_factor, uknownStructurePDB, knownStructrePDB, i, elastic_search_index, elastic_search_type, 
					"cluster", this.threshold));
	}
	
	private void startThreads()
	{
		for(CreateClusters t : threads)
		{
			t.start();
		}
		
		for(CreateClusters t : threads)
		{
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static synchronized int getNextIndex()
	{
		if(index >= number_of_clusters)
			return -1;
		else 
			index++;
		
		return index;
	}
	
	
	
	
						
}
