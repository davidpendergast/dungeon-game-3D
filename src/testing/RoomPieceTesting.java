package testing;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import building.CellType;
import building.Direction;
import building.RoomPiece;
import building.RoomPiece.Door;
import building.RoomPieceTemplate;

public class RoomPieceTesting {
    
    /**
     * Looks like:
     *     rot = NORTH         rot = WEST    
     *                             d1           
     *      X X X X X         X X = = X X 
     * d0   = - - - X         X - - - - = d2
     *      = - - - =   d1    X - - - - =
     *      X X - - =         X - - X X X
     *        X - - X         X = = X
     *        X = = X           d0
     *                     
     *           d2        
     */
    public static final RoomPieceTemplate testTemplate = new RoomPieceTemplate(
            new int[][]{{1, 3, 3, 1, 0, 0},
                        {1, 2, 2, 1, 1, 1},
                        {1, 2, 2, 2, 2, 3},
                        {1, 2, 2, 2, 2, 3},
                        {1, 1, 3, 3, 1, 1}}
            );
    @Test
    public void testNumDoors() {
        RoomPiece rp = new RoomPiece(testTemplate);
        assertEquals(3, rp.numDoors());
    }
    
    @Test
    public void testWidth() {
        RoomPiece rp = new RoomPiece(testTemplate);
        assertEquals(5, rp.width());
        rp.setRotation(Direction.EAST);
        assertEquals(6, rp.width());
        rp.setRotation(Direction.SOUTH);
        assertEquals(5, rp.width());
        rp.setRotation(Direction.WEST);
        assertEquals(6, rp.width());
    }
    
    @Test
    public void testGet() {
        RoomPiece rp = new RoomPiece(testTemplate);
        assertEquals(CellType.EMPTY, rp.get(0, 4));
        assertEquals(CellType.FLOOR, rp.get(3, 1));
        assertEquals(CellType.WALL, rp.get(0, 0));
        assertEquals(CellType.WALL,  rp.get(4, 5));
        assertEquals(CellType.EMPTY, rp.get(0, 4));
    }
    
    @Test
    public void testGetUnderRotation() {
        RoomPiece rp = new RoomPiece(testTemplate);
        rp.setRotation(Direction.WEST);
        assertEquals(CellType.WALL, rp.get(0, 0));
        assertEquals(CellType.WALL, rp.get(0, 4));
        assertEquals(CellType.WALL, rp.get(5, 0));
        assertEquals(CellType.EMPTY, rp.get(5, 4));
        assertEquals(CellType.WALL, rp.get(3, 3));
        
        assertEquals(CellType.DOOR, rp.get(2, 4));
        assertEquals(CellType.DOOR, rp.get(1, 4));
        assertEquals(CellType.DOOR, rp.get(2, 0));
        assertEquals(CellType.DOOR, rp.get(3, 0));
        assertEquals(CellType.DOOR, rp.get(5, 1));
        assertEquals(CellType.DOOR, rp.get(5, 2));
    }
    
    @Test
    public void testDoorPosition() {
        RoomPiece rp = new RoomPiece(testTemplate);
        List<Door> doors = getSortedDoors(rp);
        assertEquals(new Point(0,2), doors.get(0).left());
        assertEquals(new Point(0,1), doors.get(0).right());
        
        assertEquals(new Point(4,2), doors.get(1).left());
        assertEquals(new Point(4,3), doors.get(1).right());
        
        assertEquals(new Point(3,5), doors.get(2).left());
        assertEquals(new Point(2,5), doors.get(2).right());
    }
    
    @Test
    public void testDoorPositionUnderTranslation() {
        RoomPiece rp = new RoomPiece(testTemplate);
        rp.setPosition(new Point(30, 22));
        List<Door> doors = getSortedDoors(rp);
        assertEquals(new Point(30,24), doors.get(0).left());
        assertEquals(new Point(30,23), doors.get(0).right());
        
        assertEquals(new Point(34,24), doors.get(1).left());
        assertEquals(new Point(34,25), doors.get(1).right());
        
        assertEquals(new Point(33,27), doors.get(2).left());
        assertEquals(new Point(32,27), doors.get(2).right());
    }
    
    @Test
    public void testDoorsUnderRotation() {
        RoomPiece rp = new RoomPiece(testTemplate);
        List<Door> doors = getSortedDoors(rp);
        rp.setRotation(Direction.WEST);
        
        Door d0 = doors.get(0);
        Door d1 = doors.get(1);
        Door d2 = doors.get(2);
        
        System.out.println(d0);
        System.out.println(d1);
        System.out.println(d2);
        
        assertEquals(new Point(2,4), d0.left());
        assertEquals(new Point(1,4), d0.right());
        assertEquals(Direction.SOUTH, d0.dir());
        
        assertEquals(new Point(2,0), d1.left());
        assertEquals(new Point(3,0), d1.right());
        assertEquals(Direction.NORTH, d1.dir());
        
        assertEquals(new Point(5,1), d2.left());
        assertEquals(new Point(5,2), d2.right());
        assertEquals(Direction.EAST, d2.dir());
    }
    
    @Test
    public void testDoorDirection() {
        RoomPiece rp = new RoomPiece(testTemplate);
        List<Door> doors = getSortedDoors(rp);
        assertEquals(Direction.WEST, doors.get(0).dir());
        assertEquals(Direction.EAST, doors.get(1).dir());
        assertEquals(Direction.SOUTH, doors.get(2).dir());
    }
    
    private static List<Door> getSortedDoors(RoomPiece rp) {
        List<Door> doors = rp.getDoors();
        Collections.sort(doors, (d1, d2) -> {
            if (d1.left().x == d2.left().x) {
                return d1.left().y - d2.left().y;
            } else {
                return d1.left().x - d2.left().y;
            }
        });
        return doors;
    }

}
