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
        public List<RoomPieceTemplate> connectors = new ArrayList<RoomPieceTemplate>();
        public int size = 10;
        public Random rand = new Random();
        public Integer maxWidth = 30;
        public Integer maxHeight = 30;
        public int animationDelay = 50;
        
        public double elimIslandsProb = 0.5;
    }
    
    public static World generate(Options opts) {
        World w = new World();
        return generate(opts, w);
    }

    public static World generate(Options opts, World w) {
        w.isGenerating = true;
        setBounds(w, opts);
        
        if (w.pieces.isEmpty()) {
            placeFirstPiece(opts, w);
        }
        
        
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
                    sleep(1 * opts.animationDelay);
                    w.ghostPieces.remove(piece);
                    if (!w.collides(piece) && !w.outOfBounds(piece)) {
                        w.add(piece);

                        sleep(5 * opts.animationDelay);
                        continue doorLoop;
                    }
                }
            }
            // couldn't make it fit, wall off the door
            targetDoor.roomPiece.setWalledOff(targetDoor, true);
        }
        
        placeConnectors(opts, w);
        removeWallIslands(opts, w);
        
        w.isGenerating = false;
        return w;
    }
    
    private static void placeFirstPiece(Options opts, World w) {
        RoomPiece start = new RoomPiece(choose(opts.templates, opts.rand));
        int x = (w.bounds[0] != null && w.bounds[1] != null) ? (w.bounds[1] - w.bounds[0]) / 2 : 0;
        int y = (w.bounds[2] != null && w.bounds[3] != null) ? (w.bounds[3] - w.bounds[2]) / 2 : 0;
        start.setPosition(new Point(x, y));
        start.setRotation(Direction.values()[opts.rand.nextInt(4)]);
        w.add(start);
    }
    
    /**
     * LOOPS
     */
    private static void placeConnectors(Options opts, World w) {
        clearWalledOffDoors(w);
        
        List<Door> unmatchedDoors = w.availableDoors();
        Collections.shuffle(unmatchedDoors, opts.rand);
        
        // lol this is murder
        doorLoop:
        for (Door start : unmatchedDoors) {
            if (start.getNeighbor() != null || start.isWalledOff()) {
                // door is already matched
                continue;
            }
            
            for (RoomPieceTemplate template : opts.connectors) {
                assert template.isConnector();
                List<RoomPiece> orientations = RoomPiece.allOrientations(template);
                for (RoomPiece connector : orientations) {
                    List<Door> connectorDoors = connector.getDoors();
                    for (int i = 0; i < 2; i++) {
                        Door d1 = connectorDoors.get(i);
                        Door d2 = connectorDoors.get((i+1) % 2);
                        if (start.isCompatible(d1)) {
                            alignDoors(start, d1, connector);
                            
                            if (w.doorThatConnectsTo(d2) != null) {
                                // holy crap it connects
                                // i just hope it doesn't collide...
                                if (w.add(connector)) {
                                    continue doorLoop;
                                }
                            }
                        }
                    }
                }
            }
            
            // no connector found...
            start.roomPiece.setWalledOff(start, true);
        }
        
    }
    
    private static void clearWalledOffDoors(World w) {
        for (RoomPiece rp : w.pieces) {
            for (Door d : rp.getDoors()) {
                if (rp.getNeighbor(d) == null) {
                    rp.removeNeighbor(d);
                }
            }
        }
    }
    
    private static void removeWallIslands(Options opts, World w) {
        List<Point> worldPoints = new ArrayList<Point>();
        for (int x = w.bounds[0]; x <= w.bounds[1]; x++) {
            for (int y = w.bounds[2]; y <= w.bounds[3]; y++) {
                worldPoints.add(new Point(x, y));
            }
        }
        for (int i = 0; i < worldPoints.size(); i++) {
            Point p = worldPoints.get(i);
            if (w.getCellType(p) == CellType.WALL) {
                List<Point> wallSect = PointUtils.floodRemove(worldPoints, p, 
                        x -> w.getCellType(x) == CellType.WALL || w.getSpecialCellType(x) == CellType.WALLED_DOOR);
                if (opts.rand.nextDouble() < opts.elimIslandsProb 
                        && wallSect.stream().allMatch(x -> !w.adjacentCellTypes(x).contains(CellType.EMPTY))) {
                    for (Point wall : wallSect) {
                        w.setCellType(wall, CellType.FLOOR);
                        List<Point> adjDoors = PointUtils.floodRemove(worldPoints, 
                                wallSect, x -> w.getSpecialCellType(x) == CellType.LINKED_DOOR);
                        for (Point doorPoint : adjDoors) {
                            w.setCellType(doorPoint, CellType.FLOOR);
                        }
                    }
                    sleep(20);
                }
            }
        }
    }
    
    private static void sleep(long t) {
        if (t <= 0) {
            return;
        } else {
            try {
                Thread.sleep(t);
            } catch (InterruptedException e) { }
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
