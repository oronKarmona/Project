package Project.TrainingData;

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
	public static void WriteObject(ArrayList<Protein> proteinsDB)
	{
		int division = 20 ; 
		for(int i = 0 ; i < division ; i ++ )
		{
			int fromIndex = (proteinsDB.size()/division)*i;
			int toIndex = (proteinsDB.size()/division)*i + (proteinsDB.size()/division);
			if(i == division - 1 ) // write to the end of the list in the last iteration
				toIndex = proteinsDB.size();
			
			try (Writer writer = new FileWriter("Output"+i+".json")) 
			{
			    Gson gson = new GsonBuilder().create();
			    App.animate("Writing JSON files",i, division);
			    gson.toJson(proteinsDB.subList(fromIndex,toIndex ), writer);
			    
			  
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/***
	 * reading json file from specific path
	 * @return data from the file as ArrayList<Protein>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Protein> ReadJsonFile()
	{
		 JSONParser parser = new JSONParser();
		 
	        try {
	        	
	        	
	            Object obj = parser.parse(new FileReader("Output0.json"));
	 
	            JSONArray jsonArray = (JSONArray) obj;
	            
	            ArrayList<Protein> proteinsDB =  new Gson().fromJson(jsonArray.toJSONString(),new TypeToken<ArrayList<Protein>>(){}.getType());
	            return proteinsDB;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	        return null;
	}
	

}
