package GUI;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/***
 * This class will be used to present progress of threads calculation 
 * @author Oron
 *
 */
@SuppressWarnings("serial")
public class ProgressBar extends JFrame{
	/***
	 * ArrayList the holds all the progress bars used in this class
	 */
	private ArrayList<JProgressBar> pbThreads = null;
	/***
	 * ArrayList the holds all the Labels used in this class
	 */
	private ArrayList<JLabel> labels = null;
	/***
	 * This progress bar indicates the overall progress of the calculation 
	 */
	private JProgressBar TotalProgress = null;
	/***
	 * Indicates the progress in numbers
	 */
	private String total_proteins , current_proteins ; 
	/***
	 * Showing the progress in numbers 
	 */
	private JLabel NumericProgress = null;
	
	/***
	 * Constructor 
	 * @param msg - message to be displayed at top of the window
	 */
	public ProgressBar(String msg) {
		super(msg);
		getContentPane().setLayout(new FlowLayout());
		pbThreads = new ArrayList<JProgressBar>();
		labels = new ArrayList<JLabel>();
		NumericProgress = new JLabel("");
		
		this.setSize(300, 500);
		
		
	
	}
	/***
	 * Setting the numeric progress for the Numeric progress JLabel
	 * @param progress
	 */
	public void setNumericProgress(int progress)
	{
		if(progress < 0)
		{
			System.out.println("Progress cannot be negative!");
			return ;
		}
		
		NumericProgress.setText(String.format("%d / " + total_proteins, progress));
			
	}
	/***
	 * Adding new thread progress to this jframe
	 * @param min - minimum value
	 * @param max - maximum value
	 * @param index - index of the thread
	 */
	public void addThreadData(int min, int max , int index)
	{
		this.setVisible(false);
		if(index == - 1) // for the total progress bar
		{

			TotalProgress = new JProgressBar(min , max);
			getContentPane().add(new JLabel("Overall Progress"));
			total_proteins = Integer.toString(max);
			getContentPane().add(NumericProgress);
			TotalProgress.setStringPainted(true);
			getContentPane().add(TotalProgress);
			
		}
		else 
		{
			pbThreads.add(new JProgressBar(min,max));
			labels.add(new JLabel(String.format("Thread number %d",index)));
			pbThreads.get(pbThreads.size() - 1).setValue(0);
			pbThreads.get(pbThreads.size() - 1).setStringPainted(true);
			getContentPane().add(pbThreads.get(pbThreads.size() - 1));
			getContentPane().add(labels.get(labels.size() - 1));
			
		}
		this.setVisible(true);
		
	}
	
	/***
	 * Changing progress var of specific thread
	 * @param data - new value 
	 * @param index - index of the progress bar
	 */
	public  void  setData(int data,int index)
	{
//		if(pbThreads.size() < index || pbThreads.isEmpty())
//		{
//			System.out.println("Index value cannot exceed the size of the Progress bar ArrayList size");
//			return ;
//		}
		if(index == -1 )
			TotalProgress.setValue(data);
//		else
//			pbThreads.get(index).setValue(data);
	}
	
	/***
	 * Resets the data to 0 for specific progress bar
	 * @param index - progress bar to be changed
	 */
	public void resetData(int index)
	{
		if(pbThreads.size() < index || pbThreads.isEmpty())
		{
			System.out.println("Index value cannot exceed the size of the Progress bar ArrayList size");
			return ;
		}
			
		pbThreads.get(index).setValue(0);
	}
	

}
