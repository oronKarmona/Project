package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.media.j3d.Background;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import org.apache.logging.log4j.core.Layout;

import DB.ElasticSearchService;

/***
 * ClusterSearch panel
 * Recive cluster index from the user and searching in the clusters DB the
 * right cluster. Display the cluster neighbors and open in pajek tool
 * @author ליטף
 *
 */
public class ClusterSearch  extends JPanel{
	
	
	private JLabel clusterIndex;
	private JTextField clusterIndextextField;
	private JButton searchButton;
	private JLabel errorLabel;
	private JTextArea neighborsString;
	
	private String searchedFile;
	private JButton Helpbutton;
	public ClusterSearch(String name) {

		this.setName(name);
		initPanel();		
		setActions();
		//elasticSearchService = new ElasticSearchService("proteins", "known_structure");
	}

	private void initPanel() {


		setLayout(new GridBagLayout());


	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.HORIZONTAL ;	
	    constraints.insets = new Insets(5, 10, 5, 5);
	    /*
	     * protien id: __________ search
	     */
	    constraints.gridx = 0;
	    constraints.gridy = 0;
		clusterIndex = new JLabel();
		clusterIndex.setFont(new Font(clusterIndex.getFont().getName(), Font.BOLD, 14));
		add(clusterIndex,constraints);
		
	    constraints.gridx = 1;
	    constraints.gridy = 0;
	    clusterIndextextField = new JTextField(20);
	    clusterIndextextField.setToolTipText("insert cluster index");
		add(clusterIndextextField,constraints);
		
	    constraints.gridx = 2;
	    constraints.gridy = 0;
		searchButton = new JButton("Search");
		add(searchButton,constraints);

		
		/*
		 * error labal
		 */
		constraints.gridwidth = 5;
	    constraints.gridx = 0;
	    constraints.gridy = 1;
	    errorLabel = new JLabel("");
		errorLabel.setFont(new Font(errorLabel.getFont().getName(), Font.BOLD, 12));
		errorLabel.setForeground(Color.RED);
		add(errorLabel,constraints);
		
	   
		/*
		 * neighbors string
		 */
	
	    constraints.gridx = 0;
	    constraints.gridy = 4;
	    
	    neighborsString = new JTextArea(5,5);
	    neighborsString.setWrapStyleWord(true);
	    neighborsString.setLineWrap(true);

	    neighborsString.setEditable(false);
		
	    Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder title = BorderFactory.createTitledBorder(blackline, "Neighbors");
		title.setTitleJustification(TitledBorder.LEFT);

		neighborsString.setBorder(title);
	    add(neighborsString,constraints);

	    
	    /*
	     * Help
	     */
	    constraints.gridx = 2;
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
        helpString.add("Find cluster from DB.");
        helpString.add("Displaying it's neighbors list");
        helpString.add("and view it's structure in pajek");
        
        Helpbutton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				helpString.show(Helpbutton, Helpbutton.getWidth()/2, Helpbutton.getHeight()/2);
				
			}
		});
        
        add(Helpbutton, constraints);

	}

	
	private void setActions(){
		searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

			String id = clusterIndextextField.getText();
			if(id.isEmpty()){
				
				errorLabel.setText("*insert cluster index");
				errorLabel.setVisible(true);

				return;
			}
			
			id = String.format(clusterIndextextField.getText()+".net");
			if(!checkIfFileExists(id)){    	
				errorLabel.setText("*No cluster was found");
				errorLabel.setVisible(true);
			}
			else{
				errorLabel.setVisible(false);
				try {
					Desktop.getDesktop().open(new File(searchedFile));
//					Map<String, Object> map =elasticSearchService.getProtein(proteinIDtextField.getText());
//					if(map == null){
//						indexErrorLabel.setVisible(false);
//						errorLabel.setVisible(false);
//					}
//					else{
//						proteinName.setText(map.get("name").toString());
//						proteinString.setText(map.get("aminoAcids").toString());
//					}
					repaint();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			}
		});
		
	}
	
	private boolean checkIfFileExists(String ProteinPath) {
		
		File file = new File(ProteinPath);
		 if(!file.exists()){
	    	return false;
	    		
			 }
		 
		 searchedFile = ProteinPath;
		 return true;
		
	}
	
	@Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);

        clusterIndex.setText("Cluster ID:");

    }
}
