package GUI;


import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ProteinSearch extends JPanel{


	private JLabel proteinID;
	private JLabel proteinIndex;
	private JTextField proteinIDtextField;
	private JButton searchButton;
	private JButton openProtein;

	JLabel errorLabel;
	private final String DBFile = "C:\\pdbstyle-2.06\\";
	String searchedFile;
	
	public ProteinSearch() {
	
		
		initPanel();		
		
	}


	private void initPanel() {
setLayout(new GridBagLayout());
		
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.HORIZONTAL;	  
		
	    constraints.gridx = 0;
	    constraints.gridy = 0;
		proteinID = new JLabel();
		add(proteinID,constraints);
		
	    constraints.gridx = 1;
	    constraints.gridy = 0;

		proteinIDtextField = new JTextField(20);
		add(proteinIDtextField,constraints);
		
		
	    constraints.gridwidth = 2; 
	    constraints.gridx = 0;
	    constraints.gridy =1;
		searchButton = new JButton("Search");
		add(searchButton,constraints);

		
	    constraints.gridx = 0;
	    constraints.gridy =3;
	    JLabel space = new JLabel("	");
		add(space,constraints);

	    
	    constraints.gridx = 0;
	    constraints.gridy =3;
	    errorLabel = new JLabel("");
		errorLabel.setFont(new Font(errorLabel.getFont().getName(), Font.BOLD, 15));
		errorLabel.setForeground(Color.RED);
		add(errorLabel,constraints);
	
		
	    constraints.gridx = 0;
	    constraints.gridy = 5;
		add(space,constraints);
	
	    constraints.gridx = 0;
	    constraints.gridy = 6;
		openProtein = new JButton("open");
		openProtein.setEnabled(false);
		openProtein.addActionListener(new ActionListener() {
			
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileChooser fc = new FileChooser(searchedFile);
			}
		});

		add(openProtein,constraints);

		
		searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

//			if(proteinIDtextField.equals("")){
//			}
			
			String id = proteinIDtextField.getText();
			id = id.substring(id.length()- 5, id.length()- 3);
			id = String.format(id +"\\"+proteinIDtextField.getText()+".ent");
			if(!checkIfFileExists(id)){    	
				errorLabel.setText("No protein was found");
				errorLabel.setVisible(true);
			}
			
			else{
				errorLabel.setVisible(false);
				//openProtein.setEnabled(true);
				try {
					Desktop.getDesktop().open(new File(searchedFile));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


			}
			}
		});
		
	}

	JFileChooser jfc;


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

        proteinID.setText("Protein ID");

    }
	
	public class FileChooser {

		public FileChooser(String path) {

			JFileChooser jfc = new JFileChooser(path);

			int returnValue = jfc.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jfc.getSelectedFile();
				//System.out.println(selectedFile.getAbsolutePath());
			}

		}

	}
}
