package Threads;

import java.util.Map;

import Calculation.AverageRMSD;
import DB.ElasticSearchService;
import Helpers.MeanRMSDHelper;
/***
 * Calculation of the direct average RMSD 
 * @author Oron
 *
 */
public class MeanRMSDThread extends Thread
{
	/***
	 * ElasticSearch client
	 */
	private ElasticSearchService es ;
	/***
	 * Index and type of the elasticSearch
	 */
	private String index , type ; 
	/***
	 * Counting the number of entries for each hamming distance value
	 */
	private int[] arrayOfCounters ;
	/***
	 * Total results of the RMSD for each hamming distance value
	 */
	private double[] arrayOfRMSD ; 
	/***
	 * Average results 
	 */
	private double[] arrayOfAVGresults ;
	/***
	 * Returned object from the elasticseach db
	 */
	private Map<String, Object> map ;  
	/***
	 * Hamming distance result 
	 */
	private int HammingDistance ;
	/***
	 * rmsd result
	 */
	private double RMSD ; 
	/***
	 * size of the arrays according to the hamming distance optional values [defined by the user]
	 */
	private int size ;
	/***
	 * Constructor
	 * @param index - index of the elasticSearch
	 * @param type - type of the ElasticSearch
	 * @param size - size of the arrays according to the hamming distance optional values [defined by the user]
	 */
	public  MeanRMSDThread(String index , String type , int size) 
	{
		this.size = size;
		this.index = index ; this.type = type;
		es = new ElasticSearchService(this.index, this.type);
		arrayOfCounters = new int[size] ;
		arrayOfRMSD = new double[size];
		arrayOfAVGresults = new double[size];
		
		for(int i = 0 ; i < size ; i++ )
		{
			arrayOfCounters[i] = 0 ; 
			arrayOfRMSD[i] = 0 ;
			arrayOfAVGresults[i] = 0 ;
		}
	}
	
	
	@Override
	public void run()
	{
		int id_of_document;
		while(( id_of_document = AverageRMSD.getNextIndex() )!= -1 )
			getData_UpdateArray(id_of_document);
		
		this.calculateAVGarray();
	}
	/***
	 * Retreives the data of the specified document and setting the necessary data to the arrays 
	 * @param id_of_document
	 */
	private void getData_UpdateArray(int id_of_document)
	{
		try{
			map = es.get(id_of_document);
			
			this.HammingDistance = (Integer)map.get("HammingDistance");
			this.RMSD = (Double)map.get("RMSDResult");
			
			this.addToArrays();
			
		}catch(Exception e)
		{
			System.out.println("problem " + id_of_document);
		}
	}
	/***
	 * Calculating the average result from the arrays
	 */
	private void calculateAVGarray()
	{
		for(int i = 0 ; i < size ; i ++)
		{
			arrayOfAVGresults[i] = (arrayOfRMSD[i] / arrayOfCounters[i]);
		}
	}
	/***
	 * Adding data to arrays
	 */
	private void addToArrays()
	{
		arrayOfRMSD[this.HammingDistance] += this.RMSD ; 
		arrayOfCounters[this.HammingDistance] ++;
	}
	public double[] getArrayOfHammingValues()
	{
		return this.arrayOfAVGresults;
	}
	
	public int[] getCountArray()
	{
		return this.arrayOfCounters;
	}
	
	public double[] getRmsdValuesArray()
	{
		return this.arrayOfRMSD;
	}
}
