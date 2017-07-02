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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import Helpers.ResistanceRMSD;
import Helpers.ResistanceRMSDParser;
import Main.SystemOperations;

/***
 * Gui Class - PredictiveWatch
 * user will insert the cluster that he want to see the predicted results
 * the class will present the resistacne and Rmsd results of the choosen cluster
 * @author Litaf
 *
 */
public class PredictiveWatch extends JPanel{

	
	private JLabel clusterIndex;
	private JTextField clusterIndextextField;
	private JButton searchButton;
	private JLabel errorLabel;
	private JLabel resultsLabel;
	private JTable resultsTable;
	private JButton Helpbutton;
	private String[][] data; 
	private Image image;

	 
	public PredictiveWatch(String name) {

		this.setName(name);
		initBackground();
		initPanel();		
		//setActions();
		//elasticSearchService = new ElasticSearchService("proteins", "known_structure");
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
	    constraints.insets = new Insets(5, 10, 5, 10);
	
	    /*
	     * Help
	     */
	    
	    constraints.weightx = 1;
	    constraints.gridx = 4;
	    constraints.gridy = 0;
	

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
        helpString.add("Insert generated file name (RMSDclusterResistance)");
        helpString.add("with the desire index. The program will calculate the");
        helpString.add("RMSD results and show the predictive abilty results");

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
		searchButton.setForeground(Color.WHITE);
		searchButton.setBackground(Color.black);
		searchButton.setBorder(new LineBorder(Color.BLACK));
		searchButton.setFont(new Font(searchButton.getFont().getName(), Font.BOLD, 16));
		searchButton.setPreferredSize(new Dimension(60, 35));
	    
		searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				int i = 0;
				if(checkIfFileExists(clusterIndextextField.getText()))
				{
					ResistanceRMSDParser.readFile(String.format("RMSD"+clusterIndextextField.getText()));
					data = new String[ResistanceRMSDParser.getData().size()][6];
					for (ResistanceRMSD r : ResistanceRMSDParser.getData()) 
					{	
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
		else
            JOptionPane.showMessageDialog(null, "File doesn't exsits", "Error",JOptionPane.ERROR_MESSAGE);

		
		           
			}

		});
		
		/*
		 * error labal
		 */
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

	    JTableHeader  header = resultsTable.getTableHeader();
	    header.setBackground(new Color(135,206,235));
	}
	@Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);

        clusterIndex.setText("File name ");
        resultsLabel.setText("Results: ");
        Helpbutton.setPreferredSize(new Dimension(50, 50));
        G.drawImage(image, 0, 0, null);

    }
	private boolean checkIfFileExists(String path) {
		
		File file = new File(String.format(path+".txt"));
		 if(!file.exists()){
	    	return false;
	    }
		 return true;
		
	}
	
	
}
