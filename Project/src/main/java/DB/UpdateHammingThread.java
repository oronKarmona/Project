package DB;

import java.util.ArrayList;
import java.util.Map;

import Project.TrainingData.Protein;

public class UpdateHammingThread extends Thread
{
	private ArrayList<Protein> proteinsdb ; 
	private Map<String, Object> map ;  
	private ElasticSearchService es ;
	private Map<Integer , Integer> indexMap;
	private int firstP , secondP , firstF, secondF , hamming,i,typeSize;
	private String  fragmentA = "", fragmentB = "";
	private int ctr = 0 ;
	
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
		
		while((index = UpdateHamming.getNext()) != -1)
		{
			UpdateDoc(index);
		}
	}
	
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
