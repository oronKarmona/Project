package Threads;

import java.util.ArrayList;
import java.util.Map;

import DB.ElasticSearchService;
import Helpers.UpdateHamming;
import Protein.Protein;
/***
 * Thread for updating the hamming distance of the training data [internal use only]
 * @author Oron
 *
 */
public class UpdateHammingThread extends Thread
{
	/***
	 * known structural proteins 
	 */
	private ArrayList<Protein> proteinsdb ; 
	/**
	 * Returned object from the elasticsearch db
	 */
	private Map<String, Object> map ;  
	/***
	 * ElasticSearch client
	 */
	private ElasticSearchService es ;
	/***
	 * proteins indices as index map 
	 */
	private Map<Integer , Integer> indexMap;
	/***
	 * details of the training data entry
	 */
	private int firstP , secondP , firstF, secondF , hamming,i,typeSize;
	/***
	 * String of the fragments for hamming calculation 
	 */
	private String  fragmentA = "", fragmentB = "";
	/***
	 * counter for progress check
	 */
	private int ctr = 0 ;
	/***
	 * Constructor
	 * @param proteinsdb - known structural proteins 
	 * @param indexMap - proteins indices as index map 
	 */
	public UpdateHammingThread(ArrayList<Protein> proteinsdb ,Map<Integer , Integer> indexMap ) 
	{
		this.indexMap = indexMap;
		this.proteinsdb = proteinsdb ; 
		this.typeSize = typeSize;
		es = new ElasticSearchService("proteins","trainingdata");
		
	}
	@Override
	public void run()
	{
		int index ;
		
		while((index = UpdateHamming.getNextIndex()) != -1)
		{
			UpdateDoc(index);
		}
	}
	/***
	 * Updating the document of the given index 
	 * @param index - index to update 
	 */
	private void UpdateDoc(int index)
	{
			try{
			map = es.get(index);
					
					if(map != null)
					{
							firstP = (Integer)map.get("firstProteinIndex");
							firstF = (Integer)map.get("firstFragmentIndex");
							
							secondP = (Integer)map.get("secondProteinIndex");
							secondF = (Integer)map.get("secondFragmentIndex");
							
							fragmentA = proteinsdb.get(indexMap.get(firstP)).GetFragments(firstF);
							fragmentB = proteinsdb.get(indexMap.get(secondP)).GetFragments(secondF);

						
							
							hamming = this.hamming(fragmentA , fragmentB);
							
							es.updateDocument(hamming , index );
							System.out.println( index);
					}
					
					else
					{
						ctr ++ ;
						System.out.println("problem " + index);
					}
			}
			catch(Exception e)
			{
				System.out.println("problem " + index);
			}
	
	}
	/***
	 * Hamming distance calculation 
	 * @param protein1
	 * @param protein2
	 * @return
	 */
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
	
	public int getCtr()
	{
		return ctr;
	}
}
