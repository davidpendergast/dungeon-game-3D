package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow {
    
    private JFrame frame;
    private JPanel panel;
    private Image offscreenImage;
    
    private Consumer<Image> drawer = null;
    
    @SuppressWarnings("serial")
    public GameWindow(int width, int height) {
        frame = new JFrame("Dungeon Generator");
        frame.setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        
        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (offscreenImage != null) {
                    g.drawImage(offscreenImage, 0, 0, null);
                }
            }
        };
        panel.setPreferredSize(new Dimension(width, height));
        panel.setVisible(true);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        
        offscreenImage = panel.createVolatileImage(width, height);
    }
    
    public void setDrawer(Consumer<Image> drawer) {
        this.drawer = drawer;
    }
    
    public void draw() {
        if (drawer != null) {
            drawer.accept(offscreenImage);
        }
        panel.repaint();
    }
    
    public void addMouseListener(MouseListener l) {
        frame.addMouseListener(l);
    }
    
    public void addMouseMotionListener(MouseMotionListener l) {
        frame.addMouseMotionListener(l);
    }

}
