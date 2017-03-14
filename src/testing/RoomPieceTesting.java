package testing;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import building.Direction;
import building.RoomPiece;
import building.RoomPiece.Door;
import building.RoomPieceTemplate;

public class RoomPieceTesting {
    
    /**
     * Looks like:
     * 
     *      X X X X X
     * d0   = - - - X
     *      = - - - =   d1
     *      X X - - =
     *        X - - X
     *        X = = X
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
