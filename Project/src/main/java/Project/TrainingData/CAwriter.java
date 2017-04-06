package Project.TrainingData;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class CAwriter {

	
	
	public static void write(String path) throws IOException
	{
		File file = new File("C:\\pdbstyle-2.06\\"+path);
		@SuppressWarnings("unchecked")
		List<String> lines = FileUtils.readLines(file);

		
		for(String s : lines)
		{
			if(s.contains("CA"))
				System.out.println(s);
		}
		
		System.out.println(lines.get(lines.size()-1));
		
		
		
	}
}
