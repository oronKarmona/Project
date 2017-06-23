package GUI;


import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;

import org.elasticsearch.client.transport.TransportClient;

import DB.ElasticSearchService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ProteinSearch extends JPanel{


	private JLabel proteinID;
	private JLabel proteinIndex;
	private JTextField proteinIDtextField;
	private JButton searchButton;
	private JButton searchIndexButton;
	private JTextArea proteinString;
	private JTextArea proteinName;
	
	private JLabel indexErrorLabel;
	private JLabel errorLabel;
	private final String DBFile = "C:\\pdbstyle-2.06\\";
	private String searchedFile;
	private JTextField proteinIndextextField;
	private ElasticSearchService elasticSearchService;
	public ProteinSearch(String name) {

		this.setName(name);
		initPanel();		
		setActions();
		elasticSearchService = new ElasticSearchService("proteins", "known_structure");
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
		proteinID = new JLabel();
		proteinID.setFont(new Font(proteinID.getFont().getName(), Font.BOLD, 14));
		add(proteinID,constraints);
		
	    constraints.gridx = 1;
	    constraints.gridy = 0;
		proteinIDtextField = new JTextField(20);
		proteinIDtextField.setToolTipText("insert astral id");
		add(proteinIDtextField,constraints);
		
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
	     * protien index: __________ search
	     */
		constraints.gridwidth = 1;

	    constraints.gridx = 0;
	    constraints.gridy = 2;
	    proteinIndex = new JLabel();
	    proteinIndex.setFont(new Font(proteinIndex.getFont().getName(), Font.BOLD, 14));
		add(proteinIndex,constraints);
		
	    constraints.gridx = 1;
	    constraints.gridy = 2;
		proteinIndextextField = new JTextField(10);
		proteinIndextextField.setToolTipText("insert astral id");
		add(proteinIndextextField,constraints);
		
	    constraints.gridx = 2;
	    constraints.gridy = 2;
		searchIndexButton = new JButton("Search");
		add(searchIndexButton,constraints);

		
		
		
		/*
		 * error labal
		 */
		
		constraints.gridwidth = 5;
	    constraints.gridx = 0;
	    constraints.gridy = 3;
	    indexErrorLabel = new JLabel("");
	    indexErrorLabel.setFont(new Font(errorLabel.getFont().getName(), Font.BOLD, 12));
	    indexErrorLabel.setForeground(Color.RED);
		add(indexErrorLabel,constraints);

		
		/*
		 * name string
		 */
		constraints.gridx = 0;
	    constraints.gridy = 4;
	    
	    proteinName = new JTextArea(1,3);
	    proteinName.setEditable(false);
		
	    Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder title = BorderFactory.createTitledBorder(blackline, "Name");
		title.setTitleJustification(TitledBorder.LEFT);
		proteinName.setBorder(title);
	    add(proteinName,constraints);
	
		/*
		 * protein string
		 */
	
	    constraints.gridx = 0;
	    constraints.gridy = 5;
	    
	    proteinString = new JTextArea(5,5);
	    proteinString.setWrapStyleWord(true);
	    proteinString.setLineWrap(true);

	    proteinString.setEditable(false);
		
	    title = BorderFactory.createTitledBorder(blackline, "Sequence");
		title.setTitleJustification(TitledBorder.LEFT);

		proteinString.setBorder(title);
	    add(proteinString,constraints);
//		openProtein = new JButton("open");
//		openProtein.setEnabled(false);
//		openProtein.addActionListener(new ActionListener() {
//			
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				FileChooser fc = new FileChooser(searchedFile);
//			}
//		});

//		add(openProtein,constraints);

	    
		

	}
	
private void setActions(){
	searchButton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {

//		if(proteinIDtextField.equals("")){
//		}
		cleanResults();
		String id = proteinIDtextField.getText();
		if(id.isEmpty() || id.length() < 7){
			
			errorLabel.setText("*Protein is in too short");
			errorLabel.setVisible(true);
			indexErrorLabel.setVisible(false);

			return;
		}

		id = id.substring(id.length()- 5, id.length()- 3);
		id = String.format(id +"\\"+proteinIDtextField.getText()+".ent");
		if(!checkIfFileExists(id)){    	
			errorLabel.setText("*No protein was found");
			errorLabel.setVisible(true);
			indexErrorLabel.setVisible(false);

		}
		
		else{
			errorLabel.setVisible(false);
			indexErrorLabel.setVisible(false);

			//openProtein.setEnabled(true);
			
			try {
				Desktop.getDesktop().open(new File(searchedFile));
				Map<String, Object> map =elasticSearchService.getProtein(proteinIDtextField.getText());
				if(map == null){
					indexErrorLabel.setVisible(false);
					errorLabel.setVisible(false);
				}
				else{
					proteinName.setText(map.get("name").toString());
					proteinString.setText(map.get("aminoAcids").toString());
				}
				repaint();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (Throwable e2) {

				errorLabel.setText("*ElasticSearch server is not running!");
				int optionPane = JOptionPane.showOptionDialog(null, "Hello World", "The title", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

				if(optionPane == JOptionPane.OK_OPTION)
				{

				}
				else{
					
				}
				}
		}
		}
	});
	
	searchIndexButton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			cleanResults();
			if(proteinIndextextField.getText().equals("")){
				indexErrorLabel.setText("Index must be a number between 0 - 30874");
				indexErrorLabel.setVisible(true);
				errorLabel.setVisible(false);

			}
			else
			{
				try{
					int protein_id = Integer.parseInt(proteinIndextextField.getText());
					if(protein_id < 0 || protein_id > 30874)
						throw new Exception();
					
					Map<String, Object> map =elasticSearchService.getProtein(Integer.parseInt(proteinIndextextField.getText()));
					if(map == null){
						indexErrorLabel.setVisible(false);
						errorLabel.setVisible(false);
					}
					else{
						proteinName.setText(map.get("name").toString());
						proteinString.setText(map.get("aminoAcids").toString());
					}
					
					repaint();
					
				}catch (Exception e )
				{
					indexErrorLabel.setText("Index must be a number between 0 - 30874");
					indexErrorLabel.setVisible(true);
					errorLabel.setVisible(false);
				}
		
			}
			
		}
	});

}

private void cleanResults() {
	
	
	proteinName.setText("");
	proteinString.setText("");
	repaint();
}
	
	
	//JFileChooser jfc;


	private boolean checkIfFileExists(String ProteinPath) {
		
		File file = new File(DBFile+ProteinPath);
		 if(!file.exists()){
	    	return false;
	    		
			 }
		 
		 searchedFile = DBFile+ProteinPath;
		 return true;
		
	}
	

	@Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);

        proteinID.setText("Protein ID:");
        proteinIndex.setText("Protein Index:");

    }
	
	/***
	 * Update for test purpose
	 * @param txt
	 */
	public void setproteinIDtextField(String txt)
	{
		proteinIDtextField.setText(txt);
	}
	/***
	 * Simulating pressed button 
	 */
	public void pressButton()
	{
		this.searchButton.doClick();
	}
	/***
	 * return error label 
	 * @return text of error label
	 */
	public String getErrorLabel()
	{
		return this.errorLabel.getText();
	}
	
	/***
	 * Update for test purpose
	 * @param txt - text for testing
	 */
	public void setproteinIndextextField(String txt)
	{
		proteinIndextextField.setText(txt);
	}
	/***
	 * Simulating pressed button 
	 */
	public void pressSerchIndexButton()
	{
		this.searchIndexButton.doClick();
	}
	/***
	 * Get the protein data as string for test purpose
 	 * @return the protein data
	 */
	public String getProteinData()
	{
		return proteinName.getText() + " " + proteinString.getText() ; 
	}
	/***
	 * Returns the content of the indexErrorlabel
	 * @return the error msg
	 */
	public String getIndexErrorLabelText()
	{
		return this.indexErrorLabel.getText();
	}
//	public class FileChooser {
//
//		public FileChooser(String path) {
//
//			JFileChooser jfc = new JFileChooser(path);
//
//			int returnValue = jfc.showOpenDialog(null);
//
//			if (returnValue == JFileChooser.APPROVE_OPTION) {
//				File selectedFile = jfc.getSelectedFile();
//				//System.out.println(selectedFile.getAbsolutePath());
//			}
//
//		}
//
//	}
}
