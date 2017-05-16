package DB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.index.get.GetField;

import Project.TrainingData.Protein;

public class UpdateHamming 
{
	private ArrayList<Protein> proteinsdb ; 
	private Map<String, Object> map ;  
	ElasticSearchService es ;
	private Map<Integer , Integer> indexMap;
	public UpdateHamming(ArrayList<Protein> proteinsdb) throws IOException, InterruptedException, ExecutionException
	{
		es = new ElasticSearchService();
		indexMap = new HashMap<Integer,Integer>();
		int ctr = 0 ;
		this.proteinsdb = proteinsdb;
		
		for(int i = 0 ; i< proteinsdb.size() ; i++)
		{
			indexMap.put(proteinsdb.get(i).getProteinIndex(), i);
		}
			
		int firstP , secondP , firstF, secondF , hamming;
		String  fragmentA = "", fragmentB = "";
		for(int i= 1 ; i <= 205933669; i++ )
		{
			try{
			map = es.get(i);
					
					if(map != null)
					{
							firstP = (Integer)map.get("firstProteinIndex");
							firstF = (Integer)map.get("firstFragmentIndex");
							
							secondP = (Integer)map.get("secondProteinIndex");
							secondF = (Integer)map.get("secondFragmentIndex");
							
							fragmentA = proteinsdb.get(indexMap.get(firstP)).GetFragments(firstF);
							fragmentB = proteinsdb.get(indexMap.get(secondP)).GetFragments(secondF);
//								for(Protein p : proteinsdb)
//								{
//										if(p.getProteinIndex() == firstP)
//										{
//											fragmentA = p.GetFragments(firstF);
//										}
//											
//										 if(p.getProteinIndex() == secondP)
//										{
//											fragmentB = p.GetFragments(secondF);
//										}
//								}
						
							
							hamming = this.hamming(fragmentA , fragmentB);
							
							es.updateDocument(hamming , i );
							
							if(i%100 == 0)
								System.out.println(i);
					}
					
					else
						ctr ++ ;
			}
			catch(Exception e)
			{
				System.out.println(String.format("index: %d",i));
			}
		}
		System.out.println(ctr);
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
		   // System.out.println(hammingDistance);
		    return hammingDistance;
	}
}
