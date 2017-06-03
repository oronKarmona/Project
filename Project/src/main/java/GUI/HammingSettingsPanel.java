package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class HammingSettingsPanel extends JPanel{
	
	   private JTextArea hammingExplanationArea;
	   private String hammingExplanationText = "The number of positions at which the corresponding symbols are different";

	   private JLabel currentLabel;
	   private JLabel currentThreshold;

	   private JLabel statusLabel;
	   
	   public HammingSettingsPanel(String name) {

			this.setName(name);
	   
			//******************* set panels **************************//
		    JPanel upperPanel = new JPanel();
		    JPanel lowerPanel = new JPanel();
		   
		    upperPanel.setLayout(new GridBagLayout());
		    lowerPanel.setLayout(new GridBagLayout());
		    
		    GridBagConstraints constraints = new GridBagConstraints();
		    
		    add(upperPanel, "North");
		    add(lowerPanel, "South");
		    
		    
			//******************* Upper Panel **************************//

		   hammingExplanationArea = new JTextArea(1,37);
		   hammingExplanationArea.setEditable(false);
		   hammingExplanationArea.setLineWrap(true);
		   hammingExplanationArea.setForeground(Color.WHITE);
		   hammingExplanationArea.setBackground(Color.black);
		   
		   hammingExplanationArea.setFont(new Font(hammingExplanationArea.getFont().getName(), Font.PLAIN, 15));
		   
		   
		   constraints.gridx = 0;
		   constraints.gridy = 0;
		   constraints.gridwidth=2;
		   upperPanel.add(hammingExplanationArea, constraints);
	   	
		    //********************* Lower Panel **********************************//

		   constraints.gridx = 0;
		   constraints.gridy = 1;
		   currentLabel = new JLabel("Current Threshold:");
		   constraints.gridwidth=1;
		   lowerPanel.add(currentLabel, constraints);

		   
		   currentThreshold = new JLabel("     40%");
		   constraints.gridx = 1;
		   constraints.gridy = 1;

		   lowerPanel.add(currentThreshold, constraints);
		    
		    	
	   
	   }

		@Override
	    public void paintComponent(Graphics G) {
	        super.paintComponent(G);

	        hammingExplanationArea.setText(hammingExplanationText);
	          }

}
