package building;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class DebugWorldDrawer implements WorldDrawer{
    private Point cameraXY = new Point(0,0);
    private int cellSize = 8;
    private World world;
    
    public DebugWorldDrawer(World w) {
        this.world = w;
    }
    
    public void draw(Image canvas) {
        if (canvas == null) {
            return;
        }
        int w = canvas.getWidth(null);
        int h = canvas.getHeight(null);
        Graphics g = canvas.getGraphics();
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, w, h);
        
        int maxX = w / cellSize + 1;
        int maxY = h / cellSize + 1;
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                g.setColor(world.getCellType(x, y).color);
                g.fillRect(x*cellSize, y*cellSize, (x+1)*cellSize, (y+1)*cellSize);
            }
        }
    }

}
