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
                    
                    if (!w.collides(piece) && !outOfBounds(piece, opts)) {
                        // it fits!
                        System.out.println(piece);
                        w.add(piece);
                        
                        piece.addNieghbor(targetDoor.roomPiece, d.id);
                        targetDoor.roomPiece.addNieghbor(piece, targetDoor.id);

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
             
                        }
                        continue doorLoop;
                    }
                }
                
            }
            // couldn't make it fit
            targetDoor.roomPiece.addNieghbor(null, targetDoor.id);
        }
        
        return w;
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
    
    private static boolean outOfBounds(RoomPiece rp, Options opts) {
        Integer width = opts.maxWidth;
        if (width != null) {
            if (rp.pos.x < 0 || rp.pos.x + rp.width() > width) {
                return true;
            }
        }
        
        Integer height = opts.maxHeight;
        if (height != null) {
            if (rp.pos.y < 0 || rp.pos.y + rp.height() > height) {
                return true;
            }
        }
        
        return false;
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
