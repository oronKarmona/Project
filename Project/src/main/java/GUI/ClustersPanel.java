package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class ClustersPanel extends JPanel{

	private JLabel DBName;
	private JButton Helpbutton;
	private JButton runButton;
	private JTextField DBNametextField;
	private JLabel numberOfClusters;
	private JTextField numberOfClusterstextField;
	private JProgressBar m_progressBar;	
	Timer timer;

	public ClustersPanel(String name) {

		this.setName(name);
		initPanel();		
	}

	private void initPanel() {
		

		setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.HORIZONTAL;	  
	    constraints.insets = new Insets(5, 10, 5, 5);
	    
	    

	    
	    /*
	     * DB Name: __________ 
	     */
	    constraints.gridx = 0;
	    constraints.gridy = 1;
	    DBName = new JLabel();
	    DBName.setFont(new Font(DBName.getFont().getName(), Font.BOLD, 14));
		add(DBName,constraints);
		
	    constraints.gridx = 1;
	    constraints.gridy = 1;
	    DBNametextField = new JTextField(20);
	    DBNametextField.setToolTipText("insert cluster DB name");
		add(DBNametextField,constraints);
		
	    /*
	     * Number of clusters: __________ 
	     */
	    constraints.gridx = 0;
	    constraints.gridy = 2;
	    numberOfClusters = new JLabel();
	    numberOfClusters.setFont(new Font(numberOfClusters.getFont().getName(), Font.BOLD, 14));
		add(numberOfClusters,constraints);
		
	    constraints.gridx = 1;
	    constraints.gridy = 2;
	    numberOfClusterstextField = new JTextField(20);
		add(numberOfClusterstextField,constraints);
		
		
		/*
		 * Run
		 */
	    constraints.gridx = 0;
	    constraints.gridy = 3;
	    runButton = new JButton("Run");
		add(runButton,constraints);
	 
		
		runButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO run clusters
		        timer = new Timer(1000, setProgress);
		        timer.start();
				
			}
		});

	    /*
	     * Help
	     */
	    constraints.gridx = 3;
	    constraints.gridy = 0;
	    Helpbutton = new JButton();
	    try {
		   ImageIcon help = new ImageIcon("help.png");
		   Image image = help.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		   Helpbutton.setIcon(new ImageIcon(image));
	    } catch (Exception ex) {
	      System.out.println(ex);
	    }

	    
        final JPopupMenu helpString = new JPopupMenu("Menu");
        helpString.add("Create as many clusters (small networks)");
        helpString.add("in the BFS depth (change in setting window)");
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
        constraints.weightx = 2;
		 constraints.gridx = 1;
		 constraints.gridy = 4;
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
	
	@Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);

        DBName.setText("DB Name:");
        numberOfClusters.setText("Number of clusters: ");
        Helpbutton.setPreferredSize(new Dimension(40, 40));

    }
	
	
}
