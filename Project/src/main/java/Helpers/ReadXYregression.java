package Helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import Project.TrainingData.Protein;

public class ReadXYregression
{
	private ArrayList<Double> x  , y , rmsd; 
	private ArrayList<Double> values;
	private int number_of_colomns;
	
	public ReadXYregression(String file_name)
	{
		
		x = new ArrayList<Double>();
		rmsd = new ArrayList<Double>();
		y = new ArrayList<Double>();
		values = new ArrayList<Double>();
		this.readFile(file_name);
	}
	
	@SuppressWarnings("resource")
	private void readFile(String file_name)
	{
		String[] split_result ; 
		int line_number = 0 ;
		System.out.println("Starting to read file...");
		Scanner input;
		String input_line;
		try {
			 input = new Scanner(new File(file_name));
			 input.nextLine(); // skip first line 
			 
			 while (input.hasNextLine())
				{
	                input_line = input.nextLine();
	                line_number++;
	                if(line_number%100 == 0)
	                	System.out.println(line_number);
	                split_result = input_line.split(" ");
	                for(int i = 1 ; i < split_result.length ; i++)
	                {	
	                	if(i == split_result.length - 1 )
	                	{
	                		rmsd.add((double)Double.parseDouble(split_result[i]));
	                		break;
	                	}
	                	
	                	values.add((double)Double.parseDouble(split_result[i]));
	                	
	                }
	                number_of_colomns = split_result.length - 2 ; // minus RMSD
	                

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
	public double[][] getMatrixX()
	{
		double[][] matrix = new double[rmsd.size()][number_of_colomns];
//		for(int i=0;i<x.size();i++){
//			matrix[i][0] = x.get(i);
//			matrix[i][1] = y.get(i);
//		}
		
		for(int i = 0 ; i < values.size() ; i++)
		{
			for(int j = 0 ; j< number_of_colomns ; j++)
				matrix[i][j] = values.get(i*number_of_colomns + j);
		}
		return matrix;
		
		
	}

	public double[] getRMSD()
	{
		return fromArrayListtoArray(rmsd);
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
