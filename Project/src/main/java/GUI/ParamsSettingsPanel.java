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
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import Main.SystemOperations;
 /***
  * ParamsSettingsPanel panel
  * change the parameters in the project (code)
  * changing the BFS depth- control the size of the clusters that the project will built
  * changing the hamming treshold- control the similarty in the training data
  * @author Litaf
  *
  */
public class ParamsSettingsPanel extends JPanel{
	
	   private JTextArea hammingExplanationArea;
	   private String hammingExplanationText = "The number of positions at which the corresponding symbols are different";

	   private JLabel currentLabel;
	   private JLabel currentThreshold;
	   private JLabel statusLabel;
		private JLabel bfsLabel;
		private JLabel valueLabel;
		private JButton changeBfsButton;
		private JButton changeHamingButton;
		private JTextField currentBfs;
		private JLabel errorLabel;
		private JLabel hammingLabel;
		private JTextField currentHamming;
		private JTextField newBfs;
		private JTextField newHamming;
		private JLabel errorHammingLabel;
		private JLabel valueLabel2;
		private JLabel currentLabel2;
		private JButton Helpbutton;
		private Image image;
	   
	   public ParamsSettingsPanel(String name) {

			this.setName(name);
			initBackground();
			initPanel();
	   
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
		    constraints.insets = new Insets(5, 10, 5, 10);

		    /*
		     * constant 
		     */
		    
		    currentLabel = new JLabel();
		    currentLabel.setBackground(new Color(255,255,0));
		    currentLabel.setPreferredSize(new Dimension(75,35));

		    valueLabel = new JLabel();
		    valueLabel.setBackground(new Color(255,255,0));
		    valueLabel.setPreferredSize(new Dimension(75,35));

		    currentLabel2 = new JLabel();
		    currentLabel2.setBackground(new Color(255,255,0));
		    currentLabel2.setPreferredSize(new Dimension(75,35));

		    valueLabel2 = new JLabel();
		    valueLabel2.setBackground(new Color(255,255,0));
		    valueLabel2.setPreferredSize(new Dimension(75,35));

		    changeBfsButton = new JButton("Change");
		    changeBfsButton.setForeground(Color.WHITE);
		    changeBfsButton.setBackground(Color.black);
		    changeBfsButton.setBorder(new LineBorder(Color.BLACK));
			changeBfsButton.setFont(new Font(changeBfsButton.getFont().getName(), Font.BOLD, 16));
			changeBfsButton.setPreferredSize(new Dimension(75, 35));
			

		    changeBfsButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					errorLabel.setText("");
					errorHammingLabel.setText("");
					if(newBfs.getText().isEmpty())
					{
						errorLabel.setText("string is empty");
						errorLabel.setVisible(true);
						return;
					}
					try{
						int num = Integer.parseInt(newBfs.getText());
						if(num < 0){
							
							errorLabel.setText("string is incorrect.please insert a number bigger than 0");
							errorLabel.setVisible(true);
							return;
						}
					}catch(Exception e1){
						errorLabel.setText("string is incorrect.please insert a number bigger than 0");
						errorLabel.setVisible(true);
						e1.printStackTrace();
						return;
					}
					
					SystemOperations.setBfsDepth(Integer.parseInt(newBfs.getText()));
	                JOptionPane.showMessageDialog(null, "Bfs depth was changed!");
	    	        repaint();
				}
			});
		    
		    
		    changeHamingButton = new JButton("Change");

		    changeHamingButton.setForeground(Color.WHITE);
		    changeHamingButton.setBackground(Color.black);
		    changeHamingButton.setBorder(new LineBorder(Color.BLACK));
		    changeHamingButton.setFont(new Font(changeBfsButton.getFont().getName(), Font.BOLD, 16));
		    changeHamingButton.setPreferredSize(new Dimension(75,35));
		    
		    
		    changeHamingButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {

					errorLabel.setText("");
					errorHammingLabel.setText("");
					if(newHamming.getText().isEmpty())
					{
						errorHammingLabel.setText("string is empty");
						errorHammingLabel.setVisible(true);
						return;
					}
					try{
						int num = Integer.parseInt(newHamming.getText());
						if(num < 0 || num >100){
							
							errorHammingLabel.setText("string is incorrect.please insert a number between 1-100");
							errorHammingLabel.setVisible(true);
							return;
						}
					}catch(Exception e1){
						errorHammingLabel.setText("string is incorrect.please insert a number between 1-100");
						errorHammingLabel.setVisible(true);
						e1.printStackTrace();
						return;
					}
					
					SystemOperations.sedHammingDistanceThreshold(Integer.parseInt(newHamming.getText()));
	                JOptionPane.showMessageDialog(null, "Hamming threshold was changed!");

	                repaint();
				}
			});
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
	        helpString.add("Change the parameters in the code");
	        helpString.add("Hamming Treshold: 0 - 100%");
	        helpString.add("BFS Depth > 1");

	        Helpbutton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					helpString.show(Helpbutton, Helpbutton.getWidth()/2, Helpbutton.getHeight()/2);
					
				}
			});
	        
	        add(Helpbutton, constraints);
	        
	        
		    /*
		     * BFS: 
		     */
		    constraints.gridx = 0;
		    constraints.gridy = 1;
			bfsLabel = new JLabel();
			bfsLabel.setFont(new Font(bfsLabel.getFont().getName(), Font.BOLD, 20));
			add(bfsLabel,constraints);
			
			/*
			 * Current		Value
			 */
		    constraints.gridx = 0;
		    constraints.gridy = 2;
		    currentLabel.setOpaque(true);
		    add(currentLabel,constraints);
		    
		    constraints.gridx = 1;
		    constraints.gridy = 2;
		    valueLabel.setOpaque(true);
		    add(valueLabel, constraints);
		    
		    /*
		     * current label		value text 		change button
		     */
		    constraints.gridx = 0;
		    constraints.gridy = 3;
		    
		    currentBfs = new JTextField();
		    currentBfs.setEditable(false);	
		    currentBfs.setPreferredSize(new Dimension(75,35));
		    add(currentBfs, constraints);
		    
		    constraints.gridx = 1;
		    newBfs = new JTextField();
		    newBfs.setPreferredSize(new Dimension(75,35));
		    add(newBfs, constraints);

		    constraints.gridx = 2;
		    add(changeBfsButton, constraints);
		    
		    /*
		     * error label
		     */
		    constraints.gridx = 0;
		    constraints.gridy = 4;
		    
		    errorLabel = new JLabel("");
		    errorLabel.setForeground(Color.RED);
			errorLabel.setFont(new Font(errorLabel.getFont().getName(), Font.BOLD, 12));
			add(errorLabel, constraints);
		    
		    /*
		     * Hamming:
		     */
		    constraints.gridx = 0;
		    constraints.gridy = 5;
		    hammingLabel = new JLabel();
		    hammingLabel.setFont(new Font(hammingLabel.getFont().getName(), Font.BOLD, 20));
			add(hammingLabel,constraints);
			
		    
			/*
			 * Current		Value
			 */
		    constraints.gridx = 0;
		    constraints.gridy = 6;
		    currentLabel2.setOpaque(true);
		    add(currentLabel2,constraints);
		    
		    constraints.gridx = 1;
		    valueLabel2.setOpaque(true);
		    add(valueLabel2, constraints);
		    
		    /*
		     * current label		value text 		change button
		     */
		    constraints.gridx = 0;
		    constraints.gridy = 7;
		    currentHamming = new JTextField();
		    currentHamming.setEditable(false);
		    currentHamming.setPreferredSize(new Dimension(75,35));
		    add(currentHamming, constraints);
		    
		    constraints.gridx = 1;
		    newHamming = new JTextField();
		    newHamming.setPreferredSize(new Dimension(75,35));
		    add(newHamming, constraints);

		    constraints.gridx = 2;
		    add(changeHamingButton, constraints);
		    
		    /*
		     * error label
		     */
		    constraints.gridx = 0;
		    constraints.gridy = 8;
		    
		    errorHammingLabel = new JLabel("");
		    errorHammingLabel.setForeground(Color.RED);
		    errorHammingLabel.setFont(new Font(errorLabel.getFont().getName(), Font.BOLD, 12));
		    add(errorHammingLabel, constraints);

	}

		@Override
	    public void paintComponent(Graphics G) {
	        super.paintComponent(G);
	        currentBfs.setText(SystemOperations.getBfsDepth());
	        currentHamming.setText(SystemOperations.getHammingDistanceThreshold());
	        bfsLabel.setText("BFS");
	        hammingLabel.setText("Hamming Threshold");
	        currentLabel.setText("Current");
	        valueLabel.setText("Value");
	        currentLabel2.setText("Current");
	        valueLabel2.setText("Value");
	        Helpbutton.setPreferredSize(new Dimension(50, 50));

	        G.drawImage(image, 0, 0, null);

		}

}
