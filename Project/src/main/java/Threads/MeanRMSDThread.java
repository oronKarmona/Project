package Threads;

import java.util.Map;

import Calculation.AverageRMSD;
import DB.ElasticSearchService;
import Helpers.MeanRMSDHelper;

public class MeanRMSDThread extends Thread
{
	private ElasticSearchService es ;
	private String index , type ; 
	private int[] arrayOfCounters ;
	private double[] arrayOfRMSD ; 
	private double[] arrayOfAVGresults ;
	private Map<String, Object> map ;  
	private int HammingDistance ; 
	private double RMSD ; 
	private int size ;
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
	
	private void calculateAVGarray()
	{
		for(int i = 0 ; i < size ; i ++)
		{
			arrayOfAVGresults[i] = (arrayOfRMSD[i] / arrayOfCounters[i]);
		}
	}
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
