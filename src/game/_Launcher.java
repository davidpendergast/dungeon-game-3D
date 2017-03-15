package game;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;
import java.util.stream.Collectors;

import building.DebugWorldDrawer;
import building.World;
import building.WorldDrawer;
import building.WorldFactory;
import building.WorldFactory.Options;
import io.TemplateLoader;

public class _Launcher {
    public static void main(String[] args) {
        TemplateLoader.init();
        Options opts = new Options();
        opts.size = 400;
        int rSeed = (int)(Math.random()*100);
        System.out.println("rSeed = "+rSeed);
        opts.rand = new Random(rSeed);
        opts.templates = TemplateLoader.templates.stream() // wat r u doin
                .filter(x -> !x.isConnector()).collect(Collectors.toList());
        opts.connectors = TemplateLoader.templates.stream()
                .filter(x -> x.isConnector()).collect(Collectors.toList());
        opts.maxWidth = 100;
        opts.maxHeight = 60;
        World w = new World();
        WorldDrawer drawer = new DebugWorldDrawer();
        drawer.setWorld(w);
                
        GameWindow window = new GameWindow((opts.maxWidth+3)*8, (opts.maxHeight+5)*8);
        
        final Point[] lastDrag = {null};
        window.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //System.out.println("DRAGGED ("+e.getX()+","+e.getY()+")");
                Point dragon = lastDrag[0];
                if (dragon != null) {
                    drawer.move(e.getX()-dragon.x, e.getY()-dragon.y);
                }
                lastDrag[0] = new Point(e.getX(), e.getY());
            }
        });
        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                lastDrag[0] = null;
                
            }
        });
        window.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Pressed: "+e.getKeyChar());
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_G:
                        GlobalSettings.showGraph = !GlobalSettings.showGraph;
                        break;
                    case KeyEvent.VK_D:
                        GlobalSettings.showDungeon = !GlobalSettings.showDungeon;
                        break;
                    case KeyEvent.VK_ENTER:
                        World w = new World();
                        drawer.setWorld(w);
                        generateInNewThread(opts, w);
                }
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });
        
        window.setDrawer(drawer::draw);
        //startDrawing(window);
        
        generateInNewThread(opts, w);
       
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            window.draw();
        }
    }
    
    public static void generateInNewThread(Options opts, World w) {
        new Thread(() -> {
            WorldFactory.generate(opts, w);
            System.out.println("Done creating world!");
        }).start();
    }
    
//    public static void startDrawing(GameWindow window) {
//        new Thread(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                window.draw();
//            }
//        }).start();
//    }
}
 