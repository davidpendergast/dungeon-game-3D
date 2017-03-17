package threedee;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

/**
 * 100% Stolen from http://www.instructables.com/id/Making-a-Basic-3D-Engine-in-Java/
 *
 */
public class Game extends JFrame implements Runnable {
    private static final long serialVersionUID = 1L;

    private Thread thread;
    private boolean running;
    private BufferedImage image;
    
    public int[] pixels;
    
    private Camera camera;
    private InputState state;
    public Screen screen;
    public Dungeon dungeon;
    
    public Game() {
        thread = new Thread(this);
        image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        camera = new Camera(4.5, 4.5, 1, 0, 0, -.66);
        state = new InputState();
        screen = new Screen(640,480);
        Dungeon dungeon = new Dungeon();
        
        setSize(640, 480);
        setResizable(false);
        setTitle("Stay on the Path");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        setVisible(true);
        start();
                
    }
    
    private synchronized void start() {
        running = true;
        thread.start();
    }
    
    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        requestFocus();
        
        while (running) {
            long now = System.nanoTime();
            double dt = ((now - lastTime) / ns);
            delta = delta + dt;
            lastTime = now;
            while (delta >= 1) {
                camera.update(dt, state);
                delta--;
            }
            screen.update(camera, dungeon, pixels);
            render();
        }
    }
}
