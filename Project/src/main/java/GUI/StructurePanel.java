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

public class StructurePanel extends JPanel{

	private JLabel DBName;
	private JButton Helpbutton;
	private JButton runButton;
	private JTextField DBNametextField;
	private JProgressBar m_progressBar;	
	private Timer timer;
	private Image image;
	private JLabel title;

	public StructurePanel(String name) {

		this.setName(name);		
		initBackground();
		initPanel();		

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
				// TODO run clusters
		        timer = new Timer(1000, setProgress);
		        timer.start();				
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
			buttonIcon = ImageIO.read(new File("HelpButton.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
			Helpbutton.setText("?");
		}
	    Helpbutton = new JButton(new ImageIcon(buttonIcon));
	    Helpbutton.setBorder(BorderFactory.createEmptyBorder());
	    Helpbutton.setContentAreaFilled(false);
	    
        final JPopupMenu helpString = new JPopupMenu("Menu");
        helpString.add("Create DB of known structure proteins");

        Helpbutton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				helpString.show(Helpbutton, Helpbutton.getWidth()/2, Helpbutton.getHeight()/2);
				
			}
		});
        
        add(Helpbutton, constraints);
        
	    
		/*
		 * progress bar
		 */
        m_progressBar = new JProgressBar(0 , 100);
        m_progressBar.setValue(0);
        m_progressBar.setStringPainted(true);
        m_progressBar.setBorder(new LineBorder(Color.black));
        m_progressBar.setPreferredSize(new Dimension(300,20));
		 constraints.gridx = 1;
		 constraints.gridy = 3;
		 constraints.fill = GridBagConstraints.HORIZONTAL;
		  add(m_progressBar,constraints);
		
	}
	
	ActionListener setProgress = new ActionListener() {
        int counter = 0;
        public void actionPerformed(ActionEvent ae) {
            m_progressBar.setValue(counter);
            counter++;
            if (counter>100) {
                JOptionPane.showMessageDialog(null, "Finished Building Clusters!");
                timer.stop();
            } 
        }
    };
	
	@Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);

        title.setText("Create Structual DB");
        DBName.setText("DB Name:");
        Helpbutton.setPreferredSize(new Dimension(50, 50));
        G.drawImage(image, 0, 0, null);

    }
}
