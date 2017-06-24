package Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import Project.TrainingData.Protein;
/***
 * Parser for the pdb given by zachary 
 * @author Oron
 *
 */
public class PCNpdbParser 
{
	private static File pdbFile ;
	
		@SuppressWarnings("resource")
		public static ArrayList<Protein> ParseFile(String FileName) throws FileNotFoundException
		{
			pdbFile = new File(FileName);

			ArrayList<Protein> pcn_pdb = new ArrayList<Protein>();
			Scanner input , line; 
			String input_line , word_inLine;
			Protein temp = new Protein();
			boolean indexOrAA = false ; // false for index 
			input = new Scanner(pdbFile);
			
			while (input.hasNextLine())
			{
                input_line = input.nextLine();
                line = new Scanner(input_line);
                
                if(line.hasNext())
                {
                	 word_inLine = line.next();
                	 
                	if(!word_inLine.equals("DBsize"))
                	{
                		if(!indexOrAA)
                		{
    	                	temp.setProteinIndex(Integer.parseInt(word_inLine));
    	                	indexOrAA = true;
                		}
           	
                		else
	                	{
	                		temp.setAminoAcids(word_inLine);
	                		pcn_pdb.add(new Protein(temp));
	                		indexOrAA = false;
	                	}
                	}
                }
               
                	
            }
            input.close();
			
			return pcn_pdb;
			
		}
}
