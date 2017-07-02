package GUI;


import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;

import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;

import DB.ElasticSearchService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;
 
/***
 * ProteinSearch Panel
 * search proteins information in the DB
 * Seacrh by protein index or protein Id (by astral id)
 * If the server in down - an error dialog will apear
 * If the searching parameters are incorrect- an error label will apear
 * If the protein has a known structure- it's strucutre will open in RasMol
 * (Must download the pdb~style DB before) 
 * @author ליטף
 *
 */
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
	private JButton Helpbutton;
	private Image image;

	public ProteinSearch(String name) {

		this.setName(name);
		initBackground();
		initPanel();		
		setActions();
		elasticSearchService = new ElasticSearchService("proteins", "known_structure");
	}

	   private void initBackground() {

		   image=null;
	       try{
	            image = ImageIO.read(Main.class.getResource("/resources/panelbackground.jpg"));
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
	     * protien id: __________ search
	     */
	    constraints.gridx = 0;
	    constraints.gridy = 1;
		proteinID = new JLabel();
		proteinID.setFont(new Font(proteinID.getFont().getName(), Font.BOLD, 14));
		add(proteinID,constraints);
		
	    constraints.gridx = 1;
	    constraints.gridy = 1;
		proteinIDtextField = new JTextField(20);
		proteinIDtextField.setToolTipText("insert astral id");
		add(proteinIDtextField,constraints);
		
	    constraints.gridx = 2;
	    constraints.gridy = 1;
		searchButton = new JButton("Search");
		searchButton.setForeground(Color.WHITE);
		searchButton.setBackground(Color.black);
		searchButton.setBorder(new LineBorder(Color.BLACK));
		searchButton.setFont(new Font(searchButton.getFont().getName(), Font.BOLD, 16));
		searchButton.setPreferredSize(new Dimension(60, 35));
		add(searchButton,constraints);

		
		/*
		 * error labal
		 */
		constraints.gridwidth = 5;
	    constraints.gridx = 0;
	    constraints.gridy = 2;
	    errorLabel = new JLabel("");
		errorLabel.setFont(new Font(errorLabel.getFont().getName(), Font.BOLD, 12));
		errorLabel.setForeground(Color.RED);
		add(errorLabel,constraints);
		
	    /*
	     * protien index: __________ search
	     */
		constraints.gridwidth = 1;

	    constraints.gridx = 0;
	    constraints.gridy = 3;
	    proteinIndex = new JLabel();
	    proteinIndex.setFont(new Font(proteinIndex.getFont().getName(), Font.BOLD, 14));
		add(proteinIndex,constraints);
		
	    constraints.gridx = 1;
	    constraints.gridy = 3;
		proteinIndextextField = new JTextField(10);
		proteinIndextextField.setToolTipText("insert astral id");
		add(proteinIndextextField,constraints);
		
	    constraints.gridx = 2;
	    constraints.gridy = 3;
		searchIndexButton = new JButton("Search");
		searchIndexButton.setForeground(Color.WHITE);
		searchIndexButton.setBackground(Color.black);
		searchIndexButton.setBorder(new LineBorder(Color.BLACK));
		searchIndexButton.setFont(new Font(searchIndexButton.getFont().getName(), Font.BOLD, 16));
		searchIndexButton.setPreferredSize(new Dimension(60, 35));
		add(searchIndexButton,constraints);

		
		
		
		/*
		 * error labal
		 */
		
		constraints.gridwidth = 5;
	    constraints.gridx = 0;
	    constraints.gridy = 4;
	    indexErrorLabel = new JLabel("");
	    indexErrorLabel.setFont(new Font(errorLabel.getFont().getName(), Font.BOLD, 12));
	    indexErrorLabel.setForeground(Color.RED);
		add(indexErrorLabel,constraints);

		
		/*
		 * name string
		 */
		constraints.gridx = 0;
	    constraints.gridy = 5;
	    
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
	    constraints.gridy = 6;
	    
	    proteinString = new JTextArea(5,5);
	    proteinString.setWrapStyleWord(true);
	    proteinString.setLineWrap(true);

	    proteinString.setEditable(false);
		
	    title = BorderFactory.createTitledBorder(blackline, "Sequence");
		title.setTitleJustification(TitledBorder.LEFT);

		proteinString.setBorder(title);
	    add(proteinString,constraints);


	    /*
	     * Help
	     */
	    constraints.gridx = 3;
	    constraints.gridy = 0;
	    Helpbutton = new JButton();
	    BufferedImage buttonIcon = null;
		try {
			buttonIcon = ImageIO.read(Main.class.getResource("/resources/HelpButton.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
			Helpbutton.setText("?");
		}
	    Helpbutton = new JButton(new ImageIcon(buttonIcon));
	    Helpbutton.setBorder(BorderFactory.createEmptyBorder());
	    Helpbutton.setContentAreaFilled(false);

	    
        final JPopupMenu helpString = new JPopupMenu("Menu");
        helpString.add("Find protein from DB.");
        helpString.add("Displaying it's name, aminoAcid sequence");
        helpString.add("and view it's 3D structure");

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

			} 
			catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				
			catch (NoNodeAvailableException e2){
                JOptionPane.showMessageDialog(null, "Server is down", "Error",JOptionPane.ERROR_MESSAGE);
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
						
						/*
						 * find structure in pdbstyle
						 */
						String astralId  = map.get("astralID").toString();
						String id = astralId.substring(astralId.length()- 5, astralId.length()- 3);
						id = String.format(id +"\\"+astralId+".ent");
						if(checkIfFileExists(id))
							Desktop.getDesktop().open(new File(searchedFile));
					}
					
					repaint();
					
				}
				catch (NoNodeAvailableException e){
	                JOptionPane.showMessageDialog(null, "Server is down", "Error",JOptionPane.ERROR_MESSAGE);
				}
				
				
				catch (Exception e )
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
	errorLabel.setVisible(false);
	indexErrorLabel.setVisible(false);
	
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
        Helpbutton.setPreferredSize(new Dimension(50, 50));
        G.drawImage(image, 0, 0, null);

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
