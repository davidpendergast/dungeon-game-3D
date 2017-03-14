package building;

import static building.Direction.EAST;
import static building.Direction.NORTH;
import static building.Direction.SOUTH;
import static building.Direction.WEST;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RoomPieceTemplate {
    private final int[][] grid;
    private final List<DoorTemplate> doors;
    
    public RoomPieceTemplate(int[][] grid) {
        this.grid = grid;
        this.doors = Collections.unmodifiableList(computeDoors());
    }
    
    public int width() {
        return grid.length;
    }
    
    public int height() {
        return grid[0].length;
    }
    
    public CellType get(int x, int y) {
        if (inBounds(x, y)) {
            return CellType.get(grid[x][y]);
        } else {
            return CellType.EMPTY;
        }
    }
    
    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < width() && y < height();
    }
    
    public CellType get(Point p) {
        return get(p.x, p.y);
    }
    
    public List<Point> points() {
        List<Point> result = new ArrayList<Point>();
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (get(x,y) != CellType.EMPTY) {
                    result.add(new Point(x,y));
                }
            }
        }
        return result;
    }
    
    public List<DoorTemplate> getDoors() {
        return doors;
    }
    
    public DoorTemplate getDoor(int i) {
        return doors.get(i);
    }
    
    public int numDoors() {
        return doors.size();
    }
     
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                sb.append(get(x,y).str);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static class DoorTemplate {
        /**
         * Leftmost point of the door you'd see when standing inside the room
         * and looking out.
         */
        final Point left;
        /**
         * Rightmost point of the door you'd see when standing inside the room
         * and looking out.
         */
        final Point right;
        
        public DoorTemplate(Point left, Point right) {
            assert (left.x == right.x || left.y == right.y);
            assert !(left.equals(right));
            this.left = left;
            this.right = right;
        }
        
        public boolean isHorz() {
            return left.y == right.y;
        }
        
        public boolean isVert() {
            return left.x == left.y;
        }
        
        public int width() {
            if (isHorz()) {
                return (int)Math.abs(left.x - right.x);
            } else {
                return (int)Math.abs(left.y - right.y);
            }
        }
        
        /**
         * Direction door opens away from the room.
         */
        public Direction dir() {
            if (isHorz()) {
                return left.x < right.x ? NORTH : SOUTH;
            } else {
                return left.y < right.y ? EAST : WEST;
            }
        }
        
        public boolean isCompatible(DoorTemplate other) {
            return width() == other.width() && dir().isOpposite(other.dir());
        }
        
        public String toString() {
            return "(left:"+left.x+", "+left.y+", right:"+right.x+", "+right.y+", dir:"+dir()+")";
        }
    }
    
    private List<DoorTemplate> computeDoors() {
        Set<Point> doorPoints = points().stream()
                .filter(p -> get(p) == CellType.DOOR)
                .collect(Collectors.toSet());
        List<DoorTemplate> result = new ArrayList<DoorTemplate>();
        while (!doorPoints.isEmpty()) {
            Point seed = doorPoints.iterator().next();
            List<Point> thisDoor = floodRemove(doorPoints, seed, 
                    p -> get(p) == CellType.DOOR);
            
            DoorTemplate door = getDoor(thisDoor);
            if (door != null) {
                result.add(door);
            }
        }
        
        return result;
    }
    
    private DoorTemplate getDoor(Collection<Point> doorPoints) {
        Point[] extrema = PointUtils.getExtremePoints(doorPoints);
        int width = extrema[1].x - extrema[0].x + 1;
        int height = extrema[3].y - extrema[2].y + 1;
        if ((width > 1 && height > 1) || (width == 0 && height == 0)) {
            return null;
        } else {
            if (width > 1) {
                // horizontal door
                Point p1 = extrema[0];
                Point p2 = extrema[1];
                if (get(p1.x, p1.y-1) == CellType.EMPTY) {
                    return new DoorTemplate(p1, p2);
                } else {
                    return new DoorTemplate(p2, p1);
                }
            } else {
                // vertical door
                Point p1 = extrema[2];
                Point p2 = extrema[3];
                if (get(p1.x-1, p1.y) == CellType.EMPTY) {
                    return new DoorTemplate(p1, p2);
                } else {
                    return new DoorTemplate(p2, p1);
                }
            }
        }
    }
    
    /**
     * Removes the filter-satisfying points flood-adjacent to a seed point.
     */
    private static List<Point> floodRemove(Set<Point> points, Point seed, Predicate<Point> filter) {
        List<Point> res = new ArrayList<Point>();
        Queue<Point> q = new LinkedList<Point>();
        q.add(seed);
        boolean seedInPoints = points.contains(seed);
        points.remove(seed);
        Point p;
        while (!q.isEmpty()) {
            p = q.poll();
            res.add(p);
            Point[] neighbors = {new Point(p.x-1, p.y), new Point(p.x+1, p.y),
                    new Point(p.x, p.y-1), new Point(p.x, p.y+1)};
            for (Point n : neighbors) {
                if (points.contains(n) && filter.test(n)) {
                    points.remove(n);
                    q.add(n);
                }
            }
        }
        
        if (!filter.test(seed)) {
            // if seed isn't part of the flood fill, fix stuff
            res.remove(seed);
            if (seedInPoints) {
                points.add(seed);
            }
        }
        
        return res;
    }
}
