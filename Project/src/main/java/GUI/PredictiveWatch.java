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

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Helpers.ResistanceRMSD;
import Helpers.ResistanceRMSDParser;

public class PredictiveWatch extends JPanel{

	
	private JLabel clusterIndex;
	private JTextField clusterIndextextField;
	private JButton searchButton;
	private JLabel errorLabel;
	private JLabel resultsLabel;
	private JTable resultsTable;
	private JButton Helpbutton;
	private String[][] data; 
	public PredictiveWatch(String name) {

		this.setName(name);
		initPanel();		
		//setActions();
		//elasticSearchService = new ElasticSearchService("proteins", "known_structure");
	}
	
	private void initPanel() {

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.HORIZONTAL;	  
	    constraints.insets = new Insets(5, 10, 5, 10);
	
	    /*
	     * Help
	     */
	    constraints.gridx = 4;
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
        helpString.add("insert cluster index to see the");
        helpString.add("predictive abilty results");
 
        Helpbutton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				helpString.show(Helpbutton, Helpbutton.getWidth()/2, Helpbutton.getHeight()/2);
				
			}
		});
        
        add(Helpbutton, constraints);


	    /*
	     * Cluster Index: __________ search
	     */
	    constraints.gridx = 0;
	    constraints.gridy = 1;
		clusterIndex = new JLabel();
		clusterIndex.setFont(new Font(clusterIndex.getFont().getName(), Font.BOLD, 14));
		add(clusterIndex,constraints);
		
	    constraints.gridx = 1;
	    constraints.gridy = 1;
	    clusterIndextextField = new JTextField(20);
	    clusterIndextextField.setToolTipText("insert cluster index");
		add(clusterIndextextField,constraints);
		
	    constraints.gridx = 2;
	    constraints.gridy = 1;
		searchButton = new JButton("Search");
		add(searchButton,constraints);

		
		searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				int i = 0;
				ResistanceRMSDParser.readFile("data.txt");
				data = new String[ResistanceRMSDParser.getData().size()][6];
				for (ResistanceRMSD r : ResistanceRMSDParser.getData()) {
					
					data[i][0] = r.getProtein1();
					data[i][1] = r.getFragment1();
					data[i][2] = r.getProtein2();
					data[i][3] = r.getFragment2();
					data[i][4] = r.getR();
					data[i][5] = r.getRmsd();
					i++;
					
				}
				
				
			    DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();

			    for (String[] strings : data) {
			    	model.addRow(strings);
				}
		           
			}

		});
		
		/*
		 * error labal
		 */
		constraints.gridwidth = 3;
	    constraints.gridx = 0;
	    constraints.gridy = 2;
	    errorLabel = new JLabel("");
		errorLabel.setFont(new Font(errorLabel.getFont().getName(), Font.BOLD, 12));
		errorLabel.setForeground(Color.RED);
		add(errorLabel,constraints);
		
	   /*
	    * Results:
	    */
		constraints.gridwidth = 1;
	    constraints.gridx = 0;
	    constraints.gridy = 3;
		resultsLabel = new JLabel();
		resultsLabel.setFont(new Font(resultsLabel.getFont().getName(), Font.BOLD, 14));
		add(resultsLabel,constraints);
		
		/*
		 * table
		 */
		
	    constraints.gridx = 0;
	    constraints.gridy = 4;
	    constraints.gridwidth=3;
	    
		String[] columnNames = {"First Protein", "Fragment",
                "Second Protein","Fragment",
                "Resistance",
                "RMSD"};
		
	    DefaultTableModel model = new DefaultTableModel(columnNames,0);
	    resultsTable = new JTable(model);
	    JScrollPane scrollPane = new JScrollPane(resultsTable);
	    scrollPane.setPreferredSize(new Dimension(600, 300));
	    add(scrollPane,constraints);


	}
	@Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);

        clusterIndex.setText("Cluster Index: ");
        resultsLabel.setText("Results: ");
        Helpbutton.setPreferredSize(new Dimension(40, 40));

    }
	
	
}
