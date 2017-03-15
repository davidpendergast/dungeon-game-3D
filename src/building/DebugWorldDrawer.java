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
        int maxX = minX + w / cellSize + 3;
        int maxY = minY + h / cellSize + 3;
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                if (x == world.bounds[0] || x == world.bounds[1] 
                        || y == world.bounds[2] || y == world.bounds[3]) {
                    g.setColor(Color.RED);
                } else {
                    CellType t = world.getSpecialCellType(x, y);
                    RoomPiece rp = world.getPieceAt(x, y);
                    if (rp != null && (rp.flipped && world.getCellType(x, y) == CellType.FLOOR)) {
                        g.setColor(new Color(
                                255 - t.color.getRed(), 
                                255 - t.color.getGreen(), 
                                255 - t.color.getBlue()));
                        
                    } else {
                        g.setColor(t.color);
                    }
                }
                g.fillRect(x*cellSize - camera.x, y*cellSize - camera.y, cellSize, cellSize);
            }
        }
        
        drawGraph(g);
    }
    
    private void drawGraph(Graphics g) {
        Color color = Color.RED;
        g.setColor(color);
        for (RoomPiece piece : world.pieces) {
            Point center = worldToScreen(piece.centerPoint());
            g.fillOval(center.x, center.y, cellSize, cellSize);
            for (RoomPiece n : piece.neighbors.values()) {
                if (n != null) {
                    Point nCenter = worldToScreen(n.centerPoint()); 
                    nCenter = PointUtils.add(nCenter, new Point(cellSize/2, cellSize/2));
                    g.drawLine(center.x, center.y, nCenter.x, nCenter.y); // double draws, i know
                }
            }
        }
    }
    
    public Point worldToScreen(Point p) {
        return new Point(p.x * cellSize - cameraXY.x, p.y * cellSize - cameraXY.y);
    }

    @Override
    public void move(int xPix, int yPix) {
        cameraXY = new Point(cameraXY.x - xPix, cameraXY.y - yPix);
    }

}
