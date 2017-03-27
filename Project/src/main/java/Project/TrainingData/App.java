package Project.TrainingData;

import java.io.IOException;

public class App 
{
    public static void main( String[] args )
    {
    	Protein p = null;
    	
        
    	try {
			 p = FileParser.ReadAstralDB();
			} 
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
							  }
    	
    	System.out.println(p.getfolderIndex());
    	
    	FileParser.ReadStructureDateFile(p.getfolderIndex()+"\\"+p.getFileName());
    	
    	
    	
    	
    }
}
