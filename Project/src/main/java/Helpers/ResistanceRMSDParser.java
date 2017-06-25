package Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ResistanceRMSDParser {

	static ArrayList<ResistanceRMSD> m_data = new ArrayList<>();
	
	public static ArrayList<ResistanceRMSD> getData() {
		return m_data;
	}


	public static void readFile(String file_name) {

		String[] result ; 
		System.out.println(String.format("Starting to read file %s", file_name));
		Scanner input = null;
		String input_line;
		try {
			 input = new Scanner(new File(file_name));
			 input.nextLine(); // skip first line 
			 while (input.hasNextLine())
			{
	           result = input.nextLine().split(" ");

	           ResistanceRMSD element = new ResistanceRMSD(result[0],
	        		   result[1], result[2], result[3],result[4], result[5]);
	           m_data.add(element);
			}
				
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			input.close();
		}
	}
	
}