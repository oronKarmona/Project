package TestingData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/***
 * reading the resistences file and parsing it into list of entries
 * @author Oron
 *
 */
public class ReadResistencesFile 
{		
		/**
		 * file to be parsed
		 */
		private static File Rfile ; 
		/***
		 * parsing the file into ArrayList<TestingEntry>
		 * @param file_name
		 * @return ArrayList<TestingEntry>
		 * @throws FileNotFoundException 
		 */
		public static ArrayList<TestingEntry> ParseFile(String file_name) throws FileNotFoundException
		{
			ArrayList<TestingEntry> entries = new ArrayList<TestingEntry>();
			Scanner input , line ; 
			String input_line;
			String[] word_inLine; 
			
			Rfile = new File(file_name);
			
			input = new Scanner (Rfile);
			
			while(input.hasNextLine())
			{
				input_line = input.nextLine();
				word_inLine = input_line.split(" ");
			
				entries.add(new TestingEntry(Integer.parseInt(word_inLine[0]), Integer.parseInt(word_inLine[1]), Double.parseDouble(word_inLine[2])));
				
				
			}
			
			
			
			
			return entries;
			
		}
}
