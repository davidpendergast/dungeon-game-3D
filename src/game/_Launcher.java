package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

import building.DebugWorldDrawer;
import building.RoomPiece;
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
        opts.maxHeight = 60;
        opts.maxWidth = 100;
        World w = new World();
        WorldDrawer drawer = new DebugWorldDrawer(w);
                
        GameWindow window = new GameWindow(850, 640);
        
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
        window.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastDrag[0] = null;
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        });
        window.setDrawer(drawer::draw);
        startDrawing(window);
        
        WorldFactory.generate(opts, w);
        System.out.println("Done creating world!");
    }
    
    public static void startDrawing(GameWindow window) {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                window.draw();
            }
        }).start();
    }
}
 