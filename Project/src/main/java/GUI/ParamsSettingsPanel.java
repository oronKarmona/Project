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

public class ParamsSettingsPanel extends JPanel{
	
	   private JTextArea hammingExplanationArea;
	   private String hammingExplanationText = "The number of positions at which the corresponding symbols are different";

	   private JLabel currentLabel;
	   private JLabel currentThreshold;

	   private JLabel statusLabel;
	   
	   public ParamsSettingsPanel(String name) {

			this.setName(name);
				
	   
	   }

		@Override
	    public void paintComponent(Graphics G) {
	        super.paintComponent(G);

	          }

}
