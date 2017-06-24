package TestingData;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class WriteDataToFile 
{
		private ArrayList<TestingEntry> data;
		
		
		public static void WriteDataToFile(ArrayList<TestingEntry> data,String fileName)
		{
			System.out.println("Starting to write data to file...");
			
			String output = "";
			
			for(TestingEntry t : data)
			{
				output+= t.getAstralId1()+" ";
				output+= t.getProtein1_fragment()+" ";
				output+= t.getAstralId2()+" ";
				output+= t.getProtein2_fragment()+" ";
				output+= t.getResistence()+" ";
				output+= t.getRmsd()+"\n";
				
			}
			
			try(  PrintWriter out = new PrintWriter(fileName)  ){
			    out.println( output );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Finished");
		}
}
