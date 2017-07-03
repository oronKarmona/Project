package InnerTesting;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import Protein.Protein;

public class CreateSequenceFile {
	
	private ArrayList<Protein> m_proteinsDB;
	public CreateSequenceFile(ArrayList<Protein> proteinsDB){
		m_proteinsDB = proteinsDB;
		create();
	}
	
	private void create() {
		try{
		    PrintWriter writer = new PrintWriter("proteinDB.txt", "UTF-8");
		    for (Protein protein : m_proteinsDB) {
		    	writer.println(String.format(">%d\n%s", protein.getProteinIndex(), protein.getAminoAcids()));

			}

		   writer.close();
		} catch (IOException e) {
		  
		}
		
	}
	
	

}
