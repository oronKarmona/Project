package TestingData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DBbypass 
{
	public static Map<Integer , String> DBparser(String fileName) throws FileNotFoundException
	{
		File file = new File(fileName);
		Scanner input = new Scanner(file);
		Map<Integer , String> map = new HashMap<Integer, String>();
		input.nextLine(); // *Vertices counter 
		while(input.hasNext())
		{
			String line = input.nextLine();
			try{
			int index = Integer.parseInt(line.split(" ")[0]);
			String protein_string = (line.split(" ")[1]).replace("\"", "");
			map.put(index, protein_string);
			}catch(Exception e)
			{
				break;
			}
			
			
			
		}
		return map;
		
	}

}
