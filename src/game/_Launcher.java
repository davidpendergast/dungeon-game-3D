package game;

import java.awt.Color;
import java.awt.Graphics;

import building.DebugWorldDrawer;
import building.RoomPiece;
import building.World;
import building.WorldDrawer;
import io.TemplateLoader;

public class _Launcher {
    public static void main(String[] args) {
        TemplateLoader.init();
        World w = new World();
        w.add(new RoomPiece(TemplateLoader.templates.get(4)));
        WorldDrawer drawer = new DebugWorldDrawer(w);
        
        GameWindow window = new GameWindow(640, 480);
        window.setDrawer(drawer::draw);
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            window.draw();
        }
    }
}
 