package Threads;

import Table.TrainingData;
import Table.TrainingDataEntry;
import DB.ElasticSearchService;

/***
 * This class is used to write to the elastic search db
 * @author Oron
 *
 */
public class ElasticSearchWriteThread extends Thread 
{
	private ElasticSearchService es = null;
	private long ctr = 0 ;
	public  ElasticSearchWriteThread(ElasticSearchService es)
	{
		
		this.es = es ;
	}

	@Override
	public void run()
	{
		TrainingDataEntry p ;
		while( (p = TrainingData.getEntry()) != null )
		{
			es.add(p); 
			if(ctr%100 == 0)
				System.out.println(String.format("Saved to db %d / %d",ctr,TrainingData.TrainingData.size()));
			ctr++;
		}
	}
}