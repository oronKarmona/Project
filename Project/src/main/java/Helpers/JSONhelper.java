package Helpers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import GUI.ClustersPanel;
import GUI.StructurePanel;
import GUI.TestingPanel;
import GUI.TrainingDataPanel;
import Jama.Matrix;
import Main.App;
import Main.SystemOperations.panelName;
import Protein.Protein;

/***
 * This class contains static methods for reading and writing JSON objects from/to file
 * @author Oron
 *
 */
public class JSONhelper 
{
	/***
	 * Writing the input Object to Json file\files
	 * @param proteinsDB - object to be written
	 */
	public static void WriteObject(ArrayList<Protein> proteinsDB,int division,String name)
	{
		
		for(int i = 0 ; i < division ; i ++ )
		{
			int fromIndex = (proteinsDB.size()/division)*i;
			int toIndex = (proteinsDB.size()/division)*i + (proteinsDB.size()/division);
			if(i == division - 1 ) // write to the end of the list in the last iteration
				toIndex = proteinsDB.size();
			
			try (Writer writer = new FileWriter(name+i+".json")) 
			{
			    Gson gson = new GsonBuilder().create();
			    GeneralMethods.animate("Writing JSON files",i, division);
			    gson.toJson(proteinsDB.subList(fromIndex,toIndex ), writer);
			    
			  
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/***
	 * reading json file from specific path
	 * @param name 
	 * @return data from the file as ArrayList<Protein>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Protein> ReadJsonFile(String FileName,int amount, panelName name)
	{
		 JSONParser parser = new JSONParser();
		 ArrayList<Protein> proteinsDB  = new ArrayList<Protein>();
		 switch(name)
		 {
		 		case Structure:
		 				StructurePanel.setParameters(0, amount);
		 				break;
		 		case Training:
		 			TrainingDataPanel.setParameters(0, amount);
		 			break;
		 		case Cluster:
		 			ClustersPanel.setParameters(0, amount);
		 			break;
		 		case Testing:
		 			TestingPanel.setParameters(0, amount);
		 			break;
		 			
		 }
	        try {
	        	
	        	
	        	for(int i = 0 ; i < amount ; i ++)
	        	{
		            System.out.println("Read " + (1+i) + " from " + amount);
		            
		            
		            switch(name)
		   		 {
		   		 		case Structure:
		   		 				StructurePanel.updateProgress(i+1);
		   		 				break;
		   		 		case Training:
		   		 				TrainingDataPanel.updateProgress(i+1);
		   		 				break;
		   		 		case Cluster:
					 			ClustersPanel.updateProgress(i+1);
					 			break;
		   		 		case Testing:
			   		 			TestingPanel.updateProgress(i+1);
			   		 			break;
		   		 }
		            
					Object obj = parser.parse(new FileReader(FileName+i+".json"));
		            JSONArray jsonArray = (JSONArray) obj;
		            
		             proteinsDB.addAll((ArrayList<Protein> )new Gson().fromJson(jsonArray.toJSONString(),new TypeToken<ArrayList<Protein>>(){}.getType()))  ;
	        	}
	            return proteinsDB;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	        return null;
	}
	/***
	 * write mean rmsd values to json file
	 * @param values
	 * @param file_name
	 */
	public static void WriteMeanRMSD(double[] values , String file_name)
	{
		
			try (Writer writer = new FileWriter(file_name+".json")) 
			{
			    Gson gson = new GsonBuilder().create();
			    gson.toJson(values, writer);
			    
			  
			} catch (IOException e)
			{
				e.printStackTrace();
			}
	}
	
	/***
	 * write coefficients as a json object to a file 
	 * @param a
	 * @param file_name
	 */
	public static void writeCoefficientsRegression(Matrix a, String file_name)
	{
		try (Writer writer = new FileWriter(file_name+".json")) 
		{
		    Gson gson = new GsonBuilder().create();
		    gson.toJson(a, writer);
		    
		  
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/***
	 * read json object of coefficients from a file
	 * @param file_name
	 * @return
	 */
	public static double[] readCoefficientsRegression(String file_name)
	{
		JSONParser parser = new JSONParser();
		 Matrix matrixBeta = null;
		 double[] matrixToArray;
	        try {
	        	Object obj = parser.parse(new FileReader(file_name+".json"));
		        JSONObject jsonobject = (JSONObject) obj;
		        matrixBeta = new Gson().fromJson(jsonobject.toJSONString(),new TypeToken<Matrix>(){}.getType())  ;
	        
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        matrixToArray= new double[matrixBeta.getRowDimension()];
	        for (int i= 0; i<matrixBeta.getRowDimension();i++ ) {
	        	matrixToArray[i] = matrixBeta.get(i, 0);
			}
	     return matrixToArray; 
	       
	}
	
	

}
