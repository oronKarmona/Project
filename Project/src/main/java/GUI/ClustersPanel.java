package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import org.elasticsearch.client.transport.NoNodeAvailableException;

import Helpers.PCNpdbParser;
import Main.SystemOperations;
import PCN.WritePCNtoDB;
import Threads.WritePCNToDBThread;

/***
 * ClustersPanel Panel
 * Create the clusters DB.
 * Insert the DB type (name in elsticsearch) and the number of cluster you wish to create.
 * @author Litaf
 *
 */
public class ClustersPanel extends JPanel{

	private JLabel DBName;
	private JLabel PCNName;

	private JButton Helpbutton;
	private JButton runButton;
	private JButton runPCNButton;

	private JTextField DBNametextField;
	private JLabel numberOfClusters;
	private JTextField numberOfClusterstextField;
	private JProgressBar m_progressBar;	
	private Timer timer;
	private JLabel title1;
	private JLabel title2;

	public ClustersPanel(String name) {

		this.setName(name);
		initBackground();
		initPanel();		
	}
	private void initBackground() {

		   image=null;
	       try{
	            image = ImageIO.read(new File("panelbackground.jpg"));
	        }
	        catch (IOException e){
	            e.printStackTrace();
	        }
		          
			
		}
	private void initPanel() {
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.HORIZONTAL;	  
	    constraints.insets = new Insets(5, 10, 5, 5);
	    
	    
	    /*
	     * Build PCN 
	     */
	    constraints.gridx = 0;
	    constraints.gridy = 1;

	    title1 = new JLabel();
		title1.setFont(new Font(title1.getFont().getName(), Font.BOLD, 16));
		title1.setForeground(Color.BLUE);
		add(title1,constraints);
		
	    /*
	     * PCN instructions 
	     */
	    constraints.gridx = 0;
	    constraints.gridy = 2;
	    PCNName = new JLabel();
	    PCNName.setFont(new Font(PCNName.getFont().getName(), Font.BOLD, 14));
		add(PCNName,constraints);
		
	    /*
	     * parse pcn
	     */
		  constraints.gridx = 0;
		    constraints.gridy = 3;
		    runPCNButton = new JButton("Run");
		    runPCNButton.setForeground(Color.WHITE);
		    runPCNButton.setBackground(Color.black);
			runPCNButton.setBorder(new LineBorder(Color.BLACK));
			runPCNButton.setFont(new Font(runPCNButton.getFont().getName(), Font.BOLD, 16));
			runPCNButton.setPreferredSize(new Dimension(60, 35));
			add(runPCNButton,constraints);
		 
			
			runPCNButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					try{
					 SystemOperations.WritePCNtoElastic();
		             JOptionPane.showMessageDialog(null, "PCN Built", "Info",JOptionPane.INFORMATION_MESSAGE);

			        }
					catch (NoNodeAvailableException e3){
		                JOptionPane.showMessageDialog(null, "Server is down", "Error",JOptionPane.ERROR_MESSAGE);
					}
					catch (Exception e2){
		                JOptionPane.showMessageDialog(null, "Please make sure you download the PCN\nFile from the internet", "Error",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
	    
	    /*
	     * Build Clusters
	     */
	    constraints.gridx = 0;
	    constraints.gridy = 4;
		title2 = new JLabel();
		title2.setFont(new Font(title2.getFont().getName(), Font.BOLD, 16));
		title2.setForeground(Color.BLUE);
		add(title2,constraints);
		
	    /*
	     * DB Name: __________ 
	     */
	    constraints.gridx = 0;
	    constraints.gridy = 5;
	    DBName = new JLabel();
	    DBName.setFont(new Font(DBName.getFont().getName(), Font.BOLD, 14));
		add(DBName,constraints);
		
	    constraints.gridx = 1;
	    constraints.gridy = 5;
	    DBNametextField = new JTextField(20);
	    DBNametextField.setToolTipText("insert cluster DB name");
		add(DBNametextField,constraints);
		
	    /*
	     * Number of clusters: __________ 
	     */
	    constraints.gridx = 0;
	    constraints.gridy = 6;
	    numberOfClusters = new JLabel();
	    numberOfClusters.setFont(new Font(numberOfClusters.getFont().getName(), Font.BOLD, 14));
		add(numberOfClusters,constraints);
		
	    constraints.gridx = 1;
	    constraints.gridy = 6;
	    numberOfClusterstextField = new JTextField(20);
		add(numberOfClusterstextField,constraints);
		
		
		/*
		 * Run
		 */
	    constraints.gridx = 0;
	    constraints.gridy = 7;
	    runButton = new JButton("Run");
		runButton.setForeground(Color.WHITE);
		runButton.setBackground(Color.black);
		runButton.setBorder(new LineBorder(Color.BLACK));
		runButton.setFont(new Font(runButton.getFont().getName(), Font.BOLD, 16));
		runButton.setPreferredSize(new Dimension(60, 35));
		add(runButton,constraints);
	 
		
		runButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try{
				if(DBNametextField.getText().isEmpty() ||numberOfClusterstextField.getText().isEmpty() )
				{
	                JOptionPane.showMessageDialog(null, "One or two fields are empty", "Error",JOptionPane.ERROR_MESSAGE);

				}
				int num = Integer.parseInt(numberOfClusterstextField.getText());
				//SystemOperations.BuildClusters(DBNametextField.getText().toLowerCase(), Integer.parseInt(numberOfClusters.getText()));
		        timer = new Timer(1000, setProgress);
		        timer.start();
				}

				catch (NoNodeAvailableException e2){
	                JOptionPane.showMessageDialog(null, "Server is down", "Error",JOptionPane.ERROR_MESSAGE);
				}
				catch (NumberFormatException e1){
	                JOptionPane.showMessageDialog(null, "Wrong format, number of clusters have to be an int number", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});

	    /*
	     * Help
	     */
	    constraints.gridx = 3;
	    constraints.gridy = 0;
	    Helpbutton = new JButton();
	    BufferedImage buttonIcon = null;
		try {
			buttonIcon = ImageIO.read(new File("HelpButton.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
			Helpbutton.setText("?");
		}
	    Helpbutton = new JButton(new ImageIcon(buttonIcon));
	    Helpbutton.setBorder(BorderFactory.createEmptyBorder());
	    Helpbutton.setContentAreaFilled(false);

	    
        final JPopupMenu helpString = new JPopupMenu("Menu");
        helpString.add("First, create the PCN DB");
        helpString.add("DB name must be in lower case");
        helpString.add("After creating the DB, you can create as many clusters");
        helpString.add("(small networks) in the BFS depth (change in setting window)");
        helpString.add("and save them in the ElasticSearch DB");
        Helpbutton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				helpString.show(Helpbutton, Helpbutton.getWidth()/2, Helpbutton.getHeight()/2);
				
			}
		});
        
        add(Helpbutton, constraints);
	    
		/*
		 * progress bar
		 */
        m_progressBar = new JProgressBar(0 , 100);
        m_progressBar.setValue(0);
        m_progressBar.setStringPainted(true);
        m_progressBar.setBorder(new LineBorder(Color.black));
        m_progressBar.setPreferredSize(new Dimension(300,20));
		 constraints.gridx = 1;
		 constraints.gridy = 8;
		 constraints.fill = GridBagConstraints.HORIZONTAL;
		  add(m_progressBar,constraints);
		  
        
	}
	ActionListener setProgress = new ActionListener() {
        int counter = 0;
        public void actionPerformed(ActionEvent ae) {
            m_progressBar.setValue(counter);
            counter++;
            if (counter>100) {
                JOptionPane.showMessageDialog(null, "Finished Building Clusters!");
                timer.stop();
            } 
        }
    };
	private Image image;
	
	@Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);

        title1.setText("Create PCN DB");
               
        title2.setText("Create Clusters DB");

        PCNName.setText("Make sure to download Pcn files");
        DBName.setText("DB Name:");
        numberOfClusters.setText("Number of clusters: ");
        Helpbutton.setPreferredSize(new Dimension(50, 50));
        G.drawImage(image, 0, 0, null);

    }
	
	
}
