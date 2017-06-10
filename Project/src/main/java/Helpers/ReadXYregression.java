package Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import Project.TrainingData.Protein;

public class ReadXYregression
{
	private ArrayList<Double> x  , y; 
	
	public ReadXYregression(String file_name)
	{
		x = new ArrayList<Double>();
		y = new ArrayList<Double>();
		this.readFile(file_name);
	}
	
	@SuppressWarnings("resource")
	private void readFile(String file_name)
	{
		System.out.println("Strating to read file...");
		Scanner input;
		String input_line;
		try {
			 input = new Scanner(new File(file_name));
			 while (input.hasNextLine())
				{
	                input_line = input.nextLine();
	                x.add((double)Double.parseDouble(input_line.split(" ")[0]));
	                y.add((double)Double.parseDouble(input_line.split(" ")[1]));	                	
	            }
	            input.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		System.out.println("Finished to read file...");
	}
	
	
	public double[] getX()
	{
		
		return this.fromArrayListtoArray(x);
	}
	
	public double[] getY()
	{
		return this.fromArrayListtoArray(y);

	}
	
	private double[] fromArrayListtoArray(ArrayList<Double> list)
	{
		if(list == null)
			return null;
		
		double[] target = new double[list.size()];
		for(int i = 0 ; i < list.size() ; i++)
			target[i] =  list.get(i);
		
		return target;
	}
	
}
