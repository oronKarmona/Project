package Calculation;

import java.util.ArrayList;

public class MeanRMSD
{
	private int x , y , power;
	private ArrayList<Double> power_of_x  , power_of_y , total ; 
	private double[] beta;
	
	public MeanRMSD( int x,int y , int power, double[] beta)
	{
		this.x = x ;
		this.y = y; 
		this.power = power;
	}
	
	public void calculate_pows()
	{
		for(int i = 0 ; i <= power ; i++)
		{
			power_of_x.add(Math.pow(x,i));
			power_of_y.add(Math.pow(y,i));
		}
		
		
		for(int i = 1 ; i <= power ; i++ )
		{
			for(int j = 1 ; j <= power ; j++)
			{
				for(int k = 2 ; k<= power ; k++)
				{
					if((i + j ) == k)
					{
						total.add(power_of_x.get(i) *  power_of_y.get(j));
						
					}
				}
			}
		}
	}

}
