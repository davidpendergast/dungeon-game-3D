package building;

public class RoomPieceTemplate {
    private final int[][] grid;
    
    public RoomPieceTemplate(int[][] grid) {
        this.grid = grid;
    }
    
    public int width() {
        return grid.length;
    }
    
    public int height() {
        return grid[0].length;
    }
    
    public CellType get(int x, int y) {
        return CellType.get(grid[x][y]);
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
    
    
}
