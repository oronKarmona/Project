package GUI;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import DB.ElasticSearchService;

public class ClusterSearch  extends JPanel{
	
	
	private JLabel clusterIndex;
	private JTextField clusterIndextextField;
	private JButton searchButton;
	private JLabel errorLabel;
	private JTextArea neighborsString;
	
	private String searchedFile;
	public ClusterSearch(String name) {

		this.setName(name);
		initPanel();		
		setActions();
		//elasticSearchService = new ElasticSearchService("proteins", "known_structure");
	}

	private void initPanel() {

		setLayout(new GridBagLayout());
//		try {
//			Desktop.getDesktop().open(new File("1.net"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.HORIZONTAL;	  
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
