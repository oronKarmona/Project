package DB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.index.get.GetField;

import Project.TrainingData.Protein;

public class UpdateHamming 
{
	private ArrayList<Protein> proteinsdb ; 
	private Map<String, Object> map ;  
	ElasticSearchService es ;
	
	public UpdateHamming(ArrayList<Protein> proteinsdb) throws IOException, InterruptedException, ExecutionException
	{
		es = new ElasticSearchService();
		
		this.proteinsdb = proteinsdb;
		int firstP , secondP , firstF, secondF , hamming;
		for(int i= 1 ; i <= 128188; i++ )
		{
			map = es.get(i);
			firstP = (Integer)map.get("firstProteinIndex");
			firstF = (Integer)map.get("firstFragmentIndex");
			
			secondP = (Integer)map.get("secondProteinIndex");
			secondF = (Integer)map.get("secondFragmentIndex");
			
			hamming = this.hamming(proteinsdb.get(firstP).
					GetFragments(firstF), 
			proteinsdb.get(secondP).
			GetFragments(secondF));
			
			es.updateDocument(hamming , i );

			
		}
	}
	
	
	private int hamming(String protein1 , String protein2)
	{
		
		  int hammingDistance = 0;
		    int stringLength = protein1.length();

		    for (int j = 0; j < stringLength; j++) {
		      if (protein1.charAt(j) != protein2.charAt(j)) {
		        hammingDistance++;
		      }
		    }
		    
		    return hammingDistance;
	}
}
