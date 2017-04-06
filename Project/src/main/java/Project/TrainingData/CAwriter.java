package Project.TrainingData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import ProGAL.geom3d.Point;

public class CAwriter {

	
	
	public static void write(String path, List<Point> temp) throws IOException
	{
		File file = new File("C:\\pdbstyle-2.06\\"+path);
		@SuppressWarnings("unchecked")
		List<String> lines = FileUtils.readLines(file);
		Point p = null;
		String[] arr = null;
		 BufferedWriter out = new BufferedWriter(new FileWriter("test.txt"));
		 DecimalFormat df = new DecimalFormat("##.###");
		 char[] ch;
		for(String line : lines)
		{
			
			if(line.contains("CA"))
			{
				 ch = line.toCharArray();
				 if(temp.size() == 0 )
					 break;
				 p = temp.remove(0);
				for(int i = 0 ; i < 7 ; i ++)
					ch[31+i] = ' ';
				
				for(int i = 0 ; i < 7 ; i ++)
					ch[39+i] = ' ';
				
				for(int i = 0 ; i < 7 ; i ++)
					ch[47+i] = ' ';
				
				String x = df.format(p.x());
				String y = df.format(p.y());
				String z = df.format(p.z());
				
				for(int i = 0 ; i < x.length() ; i++)
				{
					ch[37-i] = x.toCharArray()[x.length() - 1 - i];
				
				}
				
				for(int i = 0 ; i < y.length() ; i++)
				{
					ch[45-i] = y.toCharArray()[y.length() - 1 - i];
				
				}
				
				for(int i = 0 ; i < z.length() ; i++)
				{
					ch[53-i] = z.toCharArray()[z.length() - 1 - i];
				
				}
				out.write(new String(ch)+"\n");
				//System.out.println(new String(ch));	
			}
		}
			out.write("END");
		   out.close();
		System.out.println(lines.get(lines.size()-1));
		
		
		
	}
	
	/* System.out.println(s);
				arr = s.split("\\s+");
				if(temp.size() == 0)
					break;
				p = temp.remove(0);
				arr[6] = Double.toString(p.x());
				arr[7] = Double.toString(p.y());
				arr[8] = Double.toString(p.z());
				try {
					
					out.write(df.format(p.x())+"\t"+df.format(p.y())+"\t"+df.format(p.z())+"\n");		 
				}
				catch (IOException e)
				{
				    System.out.println("Exception ");

				}
				System.out.println(arr);*/
	

}
