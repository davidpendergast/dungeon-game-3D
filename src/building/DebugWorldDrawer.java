package building;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ConcurrentModificationException;

import game.GlobalSettings;

public class DebugWorldDrawer implements WorldDrawer{
    private Point cameraXY = new Point(0,0);
    private int cellSize = 8;
    private World world;
    
    public synchronized void setWorld(World w) {
        this.world = w;
        this.cameraXY = new Point(0,0);
        this.cellSize = 8;
    }
    
    public synchronized void draw(Image canvas) {
        if (canvas == null || world == null) {
            return;
        }
        
        int w = canvas.getWidth(null);
        int h = canvas.getHeight(null);
        Graphics g = canvas.getGraphics();
        
        fillBackground(g, w, h);
        
        if (GlobalSettings.showDungeon) {
            drawWorld(g, w, h);
        }
        
        if (GlobalSettings.showGraph) {
            drawGraph(g);
        }
    }
    
    private synchronized void drawWorld(Graphics g, int w, int h) {
        Point camera = cameraXY;
        int minX = camera.x / cellSize - 1;
        int minY = camera.y / cellSize - 1;
        int maxX = minX + w / cellSize + 3;
        int maxY = minY + h / cellSize + 3;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                if (world.isGenerating && 
                        (x == world.bounds[0] || x == world.bounds[1] 
                        || y == world.bounds[2] || y == world.bounds[3])) {
                    g.setColor(Color.RED);
                } else {
                    CellType t = world.getSpecialCellType(x, y);
                    g.setColor(t.color);
                }
                g.fillRect(x*cellSize - camera.x, y*cellSize - camera.y, cellSize, cellSize);
            }
        }
    }
    
    private synchronized void drawGraph(Graphics g) {
        Color color = Color.RED;
        g.setColor(color);
        for (RoomPiece piece : world.pieces) {
            Point center = worldToScreen(piece.centerPoint());
            center = PointUtils.add(center, new Point(cellSize/2, cellSize/2));
            g.fillOval(center.x-cellSize/2, center.y-cellSize/2, cellSize, cellSize);
            try {
                for (RoomPiece n : piece.neighbors.values()) {
                    if (n != null) {
                        Point nCenter = worldToScreen(n.centerPoint()); 
                        nCenter = PointUtils.add(nCenter, new Point(cellSize/2, cellSize/2));
                        g.drawLine(center.x, center.y, nCenter.x, nCenter.y); // double draws, i know
                        g.drawLine(center.x+1, center.y, nCenter.x+1, nCenter.y);
                        g.drawLine(center.x, center.y+1, nCenter.x, nCenter.y+1);
                        g.drawLine(center.x+2, center.y, nCenter.x+2, nCenter.y);
                        g.drawLine(center.x, center.y+2, nCenter.x, nCenter.y+2);
                        
                    }
                }
            } catch (ConcurrentModificationException e) { // can trigger during WorldFactory.clearWalledOffDoors
                e.printStackTrace();
            }
        }
    }
    
    private synchronized void fillBackground(Graphics g, int w, int h) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, w, h);
    }
    
    private synchronized Point worldToScreen(Point p) {
        return new Point(p.x * cellSize - cameraXY.x, p.y * cellSize - cameraXY.y);
    }

    @Override
    public synchronized void move(int xPix, int yPix) {
        cameraXY = new Point(cameraXY.x - xPix, cameraXY.y - yPix);
    }

}
