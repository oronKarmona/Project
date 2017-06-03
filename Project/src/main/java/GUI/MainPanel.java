package GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class MainPanel extends JPanel{
	
    private Image m_image;

    public MainPanel(Image image) {
    	m_image = image;	
    }
 
	@Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);

        Image scaledImage = m_image.getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);

        G.drawImage(scaledImage,  0, 0, this.getWidth(), this.getHeight(), null);
    }
}
