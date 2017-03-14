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

public class WorldFactory {
    
    public static class Options {
        public List<RoomPieceTemplate> templates = null;
        public int size = 10;
        public Random rand = new Random();
        public Integer maxWidth = null;
        public Integer maxHeight = null;
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
        
        List<Door> doors = new ArrayList<Door>();
        for (RoomPiece rp : w.pieces) {
            doors.addAll(rp.getDoors());
        }
        
        doorLoop:
        while (opts.rand.nextDouble() > 1/(double)opts.size) {
            Door targetDoor = choose(doors, opts.rand);
            doors.remove(targetDoor); // each door gets one try
            
            if (targetDoor == null) {
                break;
            }
            
            RoomPieceTemplate template = choose(opts.templates, opts.rand);
            RoomPiece piece = new RoomPiece(template);
            
            int rotOffset = opts.rand.nextInt(4);
            
            for (int i = 0; i < 4; i++) {
                piece.setPosition(new Point(0, 0));
                piece.setRotation(i + rotOffset);
                List<Door> pieceDoors = piece.getDoors();
                
                pieceDoors = pieceDoors.stream()
                        .filter(d -> targetDoor.isCompatible(d))
                        .collect(Collectors.toList());
                
                Collections.shuffle(pieceDoors, opts.rand);
                for (Door d : pieceDoors) {
                    piece.setPosition(new Point(0, 0)); // kinda jank
                    alignDoors(targetDoor, d, piece);
                    
                    if (!w.collides(piece)) {
                        // it fits!
                        System.out.println(piece);
                        w.add(piece);
                        for (Door dToAdd : piece.getDoors()) {
                            if (!dToAdd.equals(d)) {    // TODO Door has no equals method
                                doors.add(dToAdd);
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
             
                        }
                        continue doorLoop;
                    }
                }
            }
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
        int dx = door2.left.x - door1.right.x;
        int dy = door2.left.y - door1.right.y;
        
        rp.shiftPosition(PointUtils.subtract(door1.left, door2.right));
    }
}
