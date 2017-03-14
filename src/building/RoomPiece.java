package building;

import static building.Direction.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import building.RoomPieceTemplate.DoorTemplate;

public class RoomPiece {
    
    public RoomPieceTemplate template;
    
    public Point pos;
    public Direction rot;
    public boolean flipped;
    
    public World world;
    
    /**
     * Rooms whose doors are connected to this one.
     * Door id -> RoomPiece
     */
    public Map<Integer, RoomPiece> neighbors;
    
    public RoomPiece(RoomPieceTemplate template) {
        this.template = template;
        this.pos = new Point(0,0);
        this.rot = Direction.NORTH;
        this.flipped = false;
        this.neighbors = new HashMap<Integer, RoomPiece>();
    }
    
    public void addNieghbor(RoomPiece other, int door) {
        if (neighbors.containsKey(door)) {
            //throw new IllegalArgumentException("There's already a neighbor at" + 
            //        "door " + door);
            System.out.println("There's already a neighbor at" + 
                    " door " + door);
        } else {
            neighbors.put(door, other);
        }
    }
    
    public void addToWorld(World w) {
        this.world = w;
    }
    
    public void removeFromWorld() {
        this.world = null;
    }
    
    private void makeSureNotInWorld() {
        if (this.world != null) {
            throw new RuntimeException("Cannot alter RoomPiece after it's"
                    + " been added to a world.");
        }
    }
    
    public void setPosition(Point pos) {
        makeSureNotInWorld();
        this.pos = pos;
    }
    
    public Point getPosition() {
        return pos;
    }
    
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
    
    public boolean getFlipped() {
        return this.flipped;
    }
    
    public void shiftPosition(Point shift) {
        makeSureNotInWorld();
        this.pos = new Point(pos.x + shift.x, pos.y + shift.y);
    }
    
    public void setRotation(Direction rot) {
        makeSureNotInWorld();
        this.rot = rot;
    }
    
    public void setRotation(int r) {
        makeSureNotInWorld();
        this.rot = Direction.values()[((r % 4) + 4) % 4];
    }
    
    public Direction getRotation() {
        return this.rot;
    }
    
    public int width() {
        if (rot ==  NORTH || rot == SOUTH) {
            return template.width();
        } else {
            return template.height();
        }
    }
    
    public int height() {
        if (rot == NORTH || rot == SOUTH) {
            return template.height();
        } else {
            return template.width();
        }
    }
    
    public CellType get(int x, int y) {
        if (!isValid(x,y)) {
            return CellType.EMPTY;
        } else {
            Point t = worldToTemplate(new Point(x,y));
            return template.get(t.x, t.y);
        }
    }
    
    public CellType get(Point p) {
        return get(p.x, p.y);
    }
    
    public CellType getSpecial(int x, int y) {
        CellType templateType = get(x, y);
        if (templateType == CellType.FLOOR) {
            switch(getRotation()) {
                case NORTH: return CellType.N_FLOOR;
                case EAST: return CellType.E_FLOOR;
                case WEST: return CellType.W_FLOOR;
                case SOUTH: return CellType.S_FLOOR;
            }
        } else if (templateType == CellType.DOOR) {
            Door d = getDoor(x, y);
            if (d == null) {
                return CellType.DOOR; // weird threading issue with Drawer...
            }
            if (!neighbors.containsKey(d.id)) {
                return CellType.DOOR;
            } else if (neighbors.get(d.id) == null) {
                return CellType.WALLED_DOOR;
            } else {
                return CellType.LINKED_DOOR;
            }
        } else {
            return templateType;
        }
        return CellType.EMPTY;
    }
    
    public Point centerPoint() {
        return new Point(pos.x + width()/2, pos.y + height()/2);
    }
    
    public CellType getSpecial(Point p) {
        return getSpecial(p.x, p.y);
    }
    
//    public CellType getAbsolute(int absX, int absY) {
//        return get(absX - pos.x, absY - pos.y);
//    }
    
//    public CellType getAbsolute(Point p) {
//        return getAbsolute(p.x, p.y);
//    }
    
    public Point worldToTemplate(Point p) {
        p = new Point(p.x - pos.x, p.y - pos.y);
        if (flipped) {
            p = new Point(p.y, p.x);
        }
        switch(rot) {
            case NORTH:
                return p;
            case EAST:
                return new Point(p.y, width() - p.x - 1);
            case WEST:
                return new Point(height() - p.y - 1, p.x);
            case SOUTH:
                return new Point(width() - p.x - 1, height() - p.y - 1);
            default:
                throw new RuntimeException("strange new direction encountered...");
        }
    }
    
    public Point templateToWorld(Point p) {
        Point res;
        switch(rot) {
            case NORTH:
                res = p;
                break;
            case EAST:
                res = new Point(height() - p.y - 1, p.x);
                break;
            case WEST:
                res = new Point(p.y, width() - p.x - 1);
                break;
            case SOUTH:
                res = new Point(width() - p.x - 1, height() - p.y - 1);
                break;
            default:
                throw new RuntimeException("strange new direction encountered...");
        }
        if (flipped) {
            res = new Point(res.y, res.x);
        }
        
        return new Point(res.x + pos.x, res.y + pos.y);
    }
    
    private boolean isValid(int x, int y) {
        return x >= pos.x && y >= pos.y && x < pos.x + width() && y < pos.y + height();
    } 
    
    public List<Point> points() {
        List<Point> result = new ArrayList<Point>();
        for (int x = pos.x; x < pos.x + width(); x++) {
            for (int y = pos.y; y < pos.y + height(); y++) {
                if (get(x,y) != CellType.EMPTY) {
                    result.add(new Point(x,y));
                }
            }
        }
        return result;
    }
    
//    public List<Point> pointsAbsolute() {
//        return points().stream()
//                .map(p -> new Point(p.x + pos.x, p.y + pos.y))
//                .collect(Collectors.toList());
//    }
    
    public boolean collides(RoomPiece other) {
        for (Point p : points()) {
            CellType otherType = other.get(p);
            if (otherType != CellType.EMPTY && get(p) != otherType) {
                return true;
            }
        }
        return false;
    }
    
    public int numDoors() {
        return template.numDoors();
    }
    
    public Point[] doorPosition(int i) {
        DoorTemplate d = template.getDoor(i);
        Point left = templateToWorld(d.left);
        Point right = templateToWorld(d.right);
        return new Point[] {left, right};
    }
    
    public Direction doorDirection(int i) {
        Point[] position = doorPosition(i);
        Point left = position[0];
        Point right = position[1];
        
        if (left.x == right.x) {
            return (left.y > right.y) ? EAST : WEST;
        } else {
            return (left.x > right.x) ? NORTH : SOUTH;
        }
    }
    
    public List<Door> getDoors() {
        List<Door> res = new ArrayList<Door>();
        for (int i = 0; i < numDoors(); i++) {
            res.add(new Door(this, i));
        }
        return res;
    }
    
    public List<Door> getAvailableDoors() {
        return getDoors().stream()
                .filter(door -> !neighbors.containsKey(door.id))
                .collect(Collectors.toList());
        
    }
    
    public Door getDoor(Point p) {
        for (Door d : getDoors()) {
            if (PointUtils.inBox(p, d.left(), d.right())) {
                return d;
            }
        }
        return null;
    }
    
    public Door getDoor(int x, int y) {
        return getDoor(new Point(x, y));
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
    
    public static class Door {
        RoomPiece roomPiece;
        int id;
        
        public Door(RoomPiece rp, int id) {
            this.roomPiece = rp;
            this.id = id;
        }
        
        public Point left() {
            return roomPiece.doorPosition(id)[0];
        }
        
        public Point right() {
            return roomPiece.doorPosition(id)[1];
        }
        
        public int width() {
            Point left = left();
            Point right = right();
            return Math.max(Math.abs(left.x - right.x), Math.abs(left.y - right.y));
        }
        
        public boolean isCompatible(Door other) {
            return width() == other.width() && dir().isOpposite(other.dir());
        }
        
        public Direction dir() {
            return roomPiece.doorDirection(id);
        }
        
        public boolean equals(Object other) {
            if (other instanceof Door) {
                return Objects.equals(roomPiece, ((Door) other).roomPiece)
                        && id == ((Door) other).id;
            } else {
                return false;
            }
        }
        
        public int hashCode() {
            return Objects.hash(id, roomPiece);
        }
    }
    
    public static List<RoomPiece> allOrientations(RoomPieceTemplate template) {
        List<RoomPiece> res = new ArrayList<RoomPiece>();
        
        //for (boolean flip : new boolean[] {false, true}) {
        for (boolean flip : new boolean[] {false}) {
            for (Direction dir : Direction.values()) {
                RoomPiece rp = new RoomPiece(template);
                rp.setFlipped(flip);
                rp.setRotation(dir);
                res.add(rp);
            }
        }
        assert res.size() == 4;
        return res;
    }
}
