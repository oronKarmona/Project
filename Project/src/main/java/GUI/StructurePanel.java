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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import org.elasticsearch.client.transport.NoNodeAvailableException;

import Main.SystemOperations;

/***
 * StructurePanel Panel
 * Create the structure DB for the training DB.
 * Insert the DB type (name in elsticsearch) .
 * @author Litaf
 *
 */
public class StructurePanel extends JPanel{

	private JLabel DBName;
	private JButton Helpbutton;
	private JButton runButton;
	private JTextField DBNametextField;
	private static JProgressBar m_progressBar;	
	private Image image;
	private JLabel title;
	private static JLabel status;
	private static String msg = "";
	
	public StructurePanel(String name) {

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
	    constraints.insets = new Insets(5, 10, 5, 5);
	    
	    
		title = new JLabel();
		title.setFont(new Font(title.getFont().getName(), Font.BOLD, 20));
		add(title);

	    
	    /*
	     * DB Name: __________ 
	     */
	    constraints.gridx = 0;
	    constraints.gridy = 1;
	    DBName = new JLabel();
	    DBName.setFont(new Font(DBName.getFont().getName(), Font.BOLD, 14));
		add(DBName,constraints);
		
	    constraints.gridx = 1;
	    constraints.gridy = 1;
	    DBNametextField = new JTextField(20);
	    DBNametextField.setToolTipText("insert cluster DB name");
		add(DBNametextField,constraints);

		
		/*
		 * Run
		 */
	    constraints.gridx = 0;
	    constraints.gridy = 2;
	    runButton = new JButton("Run");
	    runButton.setForeground(Color.WHITE);
	    runButton.setBackground(Color.black);
	    runButton.setBorder(new LineBorder(Color.BLACK));
	    runButton.setFont(new Font(runButton.getFont().getName(), Font.BOLD, 16));
	    runButton.setPreferredSize(new Dimension(60,35));
		add(runButton,constraints);
		
		runButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				try{
					if(DBNametextField.getText().isEmpty())
					{
						return;
					}
					
					SystemOperations.BuildProteinStructuralData(DBNametextField.getText().toLowerCase(),m_progressBar);

				}
				catch (NoNodeAvailableException e2){
	                JOptionPane.showMessageDialog(null, "Server is down", "Error",JOptionPane.ERROR_MESSAGE);
				}
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
        helpString.add("Create DB of known structure proteins");
        helpString.add("DB name must be in lower case");


        Helpbutton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				helpString.show(Helpbutton, Helpbutton.getWidth()/2, Helpbutton.getHeight()/2);
				
			}
		});
        
        add(Helpbutton, constraints);
        
        /*
         * status labal
         */
        
		 constraints.gridx = 0;
		 constraints.gridy = 3;
		 status = new JLabel("STATUS");
		 status.setForeground(Color.RED);
		  add(status,constraints);

		/*
		 * progress bar
		 */
        m_progressBar = new JProgressBar(0 , 100);
        m_progressBar.setValue(0);
        m_progressBar.setStringPainted(true);
        m_progressBar.setBorder(new LineBorder(Color.black));
        m_progressBar.setPreferredSize(new Dimension(300,20));
		 constraints.gridx = 1;
		 constraints.gridy = 4;
		 constraints.fill = GridBagConstraints.HORIZONTAL;
		  add(m_progressBar,constraints);
		
	}

	@Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);

        title.setText("Create Structual DB");
        DBName.setText("DB Name:");
        status.setText(msg);
        Helpbutton.setPreferredSize(new Dimension(50, 50));
        G.drawImage(image, 0, 0, null);

    }
	public static void setParameters(int min , int max)
	{
		m_progressBar.setMinimum(min);
		m_progressBar.setMaximum(max);
	}
	public static void updateProgress(int data)
	{
		int check = m_progressBar.getValue();
        m_progressBar.setValue(data);
        m_progressBar.update(m_progressBar.getGraphics());
        m_progressBar.repaint();
	}
	
	public static void updateStatus(String msg)
	{
		status.setText(msg);
		status.update(status.getGraphics());
		StructurePanel.setMSG(msg);
		
		
	}
	
	public static void setMSG(String newMsg)
	{
		msg = newMsg;
		status.repaint();
	}
}
