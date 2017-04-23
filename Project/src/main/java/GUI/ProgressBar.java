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
	 * Constructor 
	 * @param msg - message to be displayed at top of the window
	 */
	public ProgressBar(String msg) {
		super(msg);
		getContentPane().setLayout(new FlowLayout());
		pbThreads = new ArrayList<JProgressBar>();
		labels = new ArrayList<JLabel>();
		
		
		this.setSize(300, 500);
		
		
	
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
		pbThreads.add(new JProgressBar(min,max));
		labels.add(new JLabel(String.format("Thread number %d",index)));
		pbThreads.get(pbThreads.size() - 1).setValue(0);
		pbThreads.get(pbThreads.size() - 1).setStringPainted(true);
		getContentPane().add(labels.get(labels.size() - 1));
		getContentPane().add(pbThreads.get(pbThreads.size() - 1));
		this.setVisible(true);
		
	}
	
	/***
	 * Changing progress var of specific thread
	 * @param data - new value 
	 * @param index - index of the progress bar
	 */
	public  void  setData(int data,int index)
	{
		if(pbThreads.size() < index || pbThreads.isEmpty())
		{
			System.out.println("Index value cannot exceed the size of the Progress bar ArrayList size");
			return ;
		}
			
		pbThreads.get(index).setValue(data);
	}
	

}
