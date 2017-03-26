package Project.TrainingData;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       // StructReader.ReadStructureDateFile();
    	try {
			FileParser.ReadAstralDB();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
