package io;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.imageio.ImageIO;

import building.CellType;
import building.RoomPiece;
import building.RoomPieceTemplate;

public class TemplateLoader {
    
    public static final String templateFile = "resources/room_templates.png";
    public static List<RoomPieceTemplate> templates = new ArrayList<RoomPieceTemplate>();
    private static boolean initted = false;
    
    private static Map<Integer, CellType> rgbToCell;
    
    public static final int WALL_RGB = Color.BLACK.getRGB();
    public static final int DOOR_RGB = Color.GREEN.getRGB();
    public static final int FLOOR_RGB = Color.WHITE.getRGB();
    
    public static void init() {
        if (initted) {
            return;
        }
        
        rgbToCell = new HashMap<Integer, CellType>();
        rgbToCell.put(Color.BLACK.getRGB(), CellType.WALL);
        rgbToCell.put(Color.WHITE.getRGB(), CellType.FLOOR);
        rgbToCell.put(Color.GREEN.getRGB(), CellType.DOOR);
        
        try {
            System.out.println("Loading templates from "+templateFile+"...");
            BufferedImage img = ImageIO.read(new File(templateFile));
            templates.addAll(parseTemplates(img));
            initted = true;
            
            for (RoomPieceTemplate rpt : templates) {
                System.out.println(rpt);
                System.out.println(new RoomPiece(rpt).getDoors());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static List<RoomPieceTemplate> parseTemplates(BufferedImage img) {
        boolean[][] done = new boolean[img.getWidth()][img.getHeight()];
        List<RoomPieceTemplate> result = new ArrayList<RoomPieceTemplate>();
        
        for (int x = 0; x < done.length; x++) {
            for (int y = 0; y < done[0].length; y++) {
                if (done[x][y]) {
                    continue;
                } else {
                    RoomPieceTemplate rpt = handlePixel(img, done, x, y);
                    if (rpt != null) {
                        result.add(rpt);
                    }
                }
                
            }
        }
        
        return result;
    }
    
    private static RoomPieceTemplate handlePixel(BufferedImage img, 
            boolean[][] done, int x, int y) {
        done[x][y] = true;
        int rgb = img.getRGB(x, y);
        if (!isntEmpty(img, new Point(x, y))) {
            return null;
        } else {
            // use flood fill to find template
            List<Point> pointsInRoom = new ArrayList<Point>();
            Queue<Point> q = new LinkedList<Point>();
            q.add(new Point(x,y));
            Point p;
            
            while (!q.isEmpty()) {
                p = q.poll();
                pointsInRoom.add(p);
                
                Point[] neighbors = {new Point(p.x+1, p.y), new Point(p.x-1, p.y), 
                        new Point(p.x, p.y+1), new Point(p.x, p.y-1)};
                for (Point n : neighbors) {
                    if (isValid(n, img) && !done[n.x][n.y]) {
                        done[n.x][n.y] = true;
                        if (isntEmpty(img, n)) {
                            q.add(n);
                        }
                    }
                }
            }

            return buildTemplate(pointsInRoom, img);
        }
    }
    
    private static RoomPieceTemplate buildTemplate(List<Point> points, BufferedImage img) {
        if (points.size() == 0) {
            return null;
        }
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        
        for (Point p : points) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }
        
        int[][] arr = new int[maxX-minX+1][maxY-minY+1];
        for (Point p : points) {
            CellType type = rgbToCell.get(img.getRGB(p.x, p.y));
            if (type == null) {
                System.out.println("bad point: "+p);
            } else {
                arr[p.x-minX][p.y-minY] = type.id;
            }
        }
        
        return new RoomPieceTemplate(arr);
    }
    
    private static boolean isValid(Point p, BufferedImage img) {
        return !(p.x < 0 || p.y < 0 || p.x >= img.getWidth() || p.y >= img.getHeight());
    }
    
    private static boolean isntEmpty(BufferedImage img, Point p) {
        return rgbToCell.containsKey(img.getRGB(p.x, p.y));
    }
    
}
