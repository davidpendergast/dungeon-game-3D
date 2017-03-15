package building;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import building.RoomPiece.Door;
import building.RoomPieceTemplate.DoorTemplate;

public class WorldFactory {
    
    public static class Options {
        public List<RoomPieceTemplate> templates = null;
        public List<RoomPieceTemplate> connectors = null;
        public int size = 10;
        public Random rand = new Random();
        public Integer maxWidth = 30;
        public Integer maxHeight = 30;
    }
    
    public static World generate(Options opts) {
        World w = new World();
        return generate(opts, w);
    }

    public static World generate(Options opts, World w) {
        if (w.pieces.isEmpty()) {
            RoomPiece start = new RoomPiece(choose(opts.templates, opts.rand));
            start.setPosition(new Point(40,20));
            start.setRotation(Direction.values()[opts.rand.nextInt(4)]);
            w.add(start);
        }
        
        setBounds(w, opts);
        
        List<Door> doors;
        
        doorLoop:
        while (true) {
            doors = w.availableDoors();
            if (doors.isEmpty()) {
                break doorLoop;
            }
            
            Door targetDoor = choose(doors, opts.rand);
            
            RoomPieceTemplate template = choose(opts.templates, opts.rand);
            List<RoomPiece> orientations = RoomPiece.allOrientations(template);
            Collections.shuffle(orientations, opts.rand);
            
            for (RoomPiece piece : orientations) {
                
                List<Door> pieceDoors = piece.getDoors();
                
                pieceDoors = pieceDoors.stream()
                        .filter(d -> targetDoor.isCompatible(d))
                        .collect(Collectors.toList());
                
                Collections.shuffle(pieceDoors, opts.rand);
                for (Door d : pieceDoors) {
                    alignDoors(targetDoor, d, piece);
                    w.ghostPieces.add(piece);
                    sleep(100);
                    w.ghostPieces.remove(piece);
                    if (!w.collides(piece) && !w.outOfBounds(piece)) {
                        w.add(piece);

                        sleep(500);
                        continue doorLoop;
                    }
                }
            }
            // couldn't make it fit, wall off the door
            targetDoor.roomPiece.addNeighbor(null, targetDoor.id);
        }
        
        return w;
    }
    
    private static void placeConnectors(Options opts, World w) {
        clearWalledOffDoors(w);
    }
    
    private static void clearWalledOffDoors(World w) {
        for (RoomPiece rp : w.pieces) {
            for (int id : rp.neighbors.keySet()) {
                if (rp.neighbors.get(id) == null) {
                    rp.neighbors.remove(id);
                }
            }
        }
    }
    
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {

        }
    }
    
    private static <T> T choose(List<T> list, Random rand) {
        if (list.size() == 0) {
            return null;
        } else {
            return list.get(rand.nextInt(list.size()));
        }
    }
    
    private static void alignDoors(Door door1, Door door2, RoomPiece rp) {
        assert door1.isCompatible(door2);
        // we want to shift d2 onto d1
        
        rp.shiftPosition(PointUtils.subtract(door1.left(), door2.right()));
    }
    
    private static void setBounds(World w, Options opts) {
        if (opts.maxWidth == null) {
            w.bounds[0] = null;
            w.bounds[1] = null;
        } else {
            w.bounds[0] = 0;
            w.bounds[1] = opts.maxWidth;
        }
        
        if (opts.maxHeight == null) {
            w.bounds[2] = null;
            w.bounds[3] = null;
        } else {
            w.bounds[2] = 0;
            w.bounds[3] = opts.maxHeight;
        }
    }
}
