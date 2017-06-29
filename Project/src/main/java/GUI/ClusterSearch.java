package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
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
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
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
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import org.apache.logging.log4j.core.Layout;

import DB.ElasticSearchService;

/***
 * ClusterSearch panel
 * Recive cluster index from the user and searching in the clusters DB the
 * right cluster. Display the cluster neighbors and open in pajek tool
 * @author LITAF
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
	private Image image;
	
	public ClusterSearch(String name) {

		this.setName(name);
		initBackground();
		initPanel();		
		setActions();
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
	    constraints.fill = GridBagConstraints.HORIZONTAL ;	
	    constraints.insets = new Insets(5, 10, 5, 5);
	    /*
	     * protien id: __________ search
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
		 * neighbors string
		 */
	
	    constraints.gridx = 0;
	    constraints.gridy = 5;
	    
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
        Helpbutton.setPreferredSize(new Dimension(50, 50));
        G.drawImage(image, 0, 0, null);

    }
}
