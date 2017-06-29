package Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/***
 * Reads the pajek file for GUI
 * @author Oron
 *
 */
public class ReadPajekFile 
{
	/***
	 * Read the pajek file 
	 * @param fileName - name/path of the pajek file 
	 * @return pajek file content
	 */
		public static String read(String fileName)
		{
			
			String content = "";
			
			try {
				content = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
				System.out.println(content);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			return content;
			
		}
}
