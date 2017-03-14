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
        Point camera = cameraXY;
        int w = canvas.getWidth(null);
        int h = canvas.getHeight(null);
        Graphics g = canvas.getGraphics();
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, w, h);
        
        int minX = camera.x / cellSize - 1;
        int minY = camera.y / cellSize - 1;
        int maxX = minX + w / cellSize + 1;
        int maxY = minY + h / cellSize + 1;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                g.setColor(world.getCellType(x, y).color);
                g.fillRect(x*cellSize - camera.x, y*cellSize - camera.y, cellSize, cellSize);
            }
        }
    }

    @Override
    public void move(int xPix, int yPix) {
        cameraXY = new Point(cameraXY.x - xPix, cameraXY.y - yPix);
    }

}
