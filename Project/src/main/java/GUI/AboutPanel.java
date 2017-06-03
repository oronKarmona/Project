package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.JTextArea;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.SwingConstants;

public class AboutPanel extends JPanel{

	String aboutText = String.format("Protein annotation is one of the most important goals pursued by bioinformatics, it is highly important in medicine (for example, in drug design) and biotechnology (for example, in the design of novel enzymes)."+
	"Correct detection of relatedness between protein sequences is an important stage for protein annotation. Usually, this detection is carried out by direct comparison of the sequences – sequence alignment."+
	"Our project's purpose is to improve a new approach for the relatedness detection, which is based on the protein connectivity network (PCN)."+
	"Protein connectivity network (PCN) is a similarity graph. In this graph, each node represents a protein fragment and edges represent relatedness between a pair of corresponded proteins fragments. Connectivity trough the PCN reflects the probability of corresponded protein molecules to be similar. Currently, electrical and flow network models are used for quantitative estimation of this connectivity."+
	"We are going to optimize calculation of resistances of the PCN edges, as well as apply introduction of the fake edges into the system. A user-friendly tool will be created for parameters tuning and power estimation for the new algorithm");
	JTextArea area ;
	 JLabel image1;
	 JLabel image2;

	 JLabel aboutProjectText;
	 JLabel aboutUsText;
	 Image scaledImage1;
	 Image scaledImage2;

	 ImageIcon imageIcon;
	public AboutPanel(String name) {
		
		this.setName(name);
		
		//******************* set panels **************************//
	    JPanel upperPanel = new JPanel();
	    JPanel lowerPanel = new JPanel();
	    
	    upperPanel.setLayout(new GridBagLayout());
	    lowerPanel.setLayout(new GridBagLayout());
	    
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.VERTICAL;	  
	    
	    add(upperPanel, "North");
	    add(lowerPanel, "South");

	    
	    
		//******************* Upper Panel **************************//

	    
	    area = new JTextArea(10,55);
	    area.setLineWrap(true);
	    area.setWrapStyleWord(true);
	    area.setBorder(new EtchedBorder());
	    area.setEditable(false);

	    aboutProjectText= new JLabel("About The Project",SwingConstants.CENTER);	
	    aboutProjectText.setFont(new Font(aboutProjectText.getFont().getName(), Font.BOLD, 20));

	    constraints.gridx = 0;
	    constraints.gridy = 0;
	    upperPanel.add(aboutProjectText,constraints);

	    constraints.gridwidth = 1; 
	    constraints.gridx = 0;
	    constraints.gridy = 1;
	    upperPanel.add(area,constraints);
	    
	    //***************************** Lower Panel ************************//
        Image image;
		try {
	        File imagefile = new File("litaf.jpg");
			image = ImageIO.read(imagefile);
			scaledImage1 = image.getScaledInstance(150,150,Image.SCALE_SMOOTH);

			imagefile = new File("oron.jpg");
			image = ImageIO.read(imagefile);
			scaledImage2 = image.getScaledInstance(150,150,Image.SCALE_SMOOTH);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	
		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder title1 = BorderFactory.createTitledBorder(blackline, "Litaf Kupfer");
		title1.setTitleJustification(TitledBorder.CENTER);
	
		TitledBorder title2 = BorderFactory.createTitledBorder(blackline, "Oron Karmona");
		title2.setTitleJustification(TitledBorder.CENTER);
		
	    image1 = new JLabel();
	    imageIcon = new ImageIcon(scaledImage1);
	    image1.setIcon(imageIcon);
	    image1.setBorder(title1);
	     		
	    image2 = new JLabel();
	    imageIcon = new ImageIcon(scaledImage2);
	    image2.setIcon(imageIcon);
	    image2.setBorder(title2);

	    aboutUsText= new JLabel("About Us",SwingConstants.CENTER);	
	    aboutUsText.setFont(new Font(aboutUsText.getFont().getName(), Font.BOLD, 20));

	    constraints.gridx = 0;
	    constraints.gridy = 0;
	    constraints.gridwidth = 2; 

	    lowerPanel.add(aboutUsText, constraints);
	    constraints.gridwidth = 1; 

	    constraints.gridx = 0;
	    constraints.gridy = 1;
	    lowerPanel.add(image1,constraints);
	    
	    constraints.gridx = 1;
	    constraints.gridy = 1;
	    lowerPanel.add(image2,constraints);
	    


	    
	}
	
	@Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);

        area.setText(aboutText);
          }

	
	
	
	
}
