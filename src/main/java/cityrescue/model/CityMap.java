package cityrescue.model;

/**
 * Stores city grid map with all the sizes and obstacles.
 */
public class CityMap {

    private int width;
    private int height;
    private boolean[][] blocked;

    /**
     * Creates new city map with given size parameters
     *
     * @param width  width of map
     * @param height height of map
     */
    public CityMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.blocked = new boolean[width][height];
    }

    /**
     * @return Returns grid width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return Returns grid height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Checks if coords are in-bounds
     *
     * @param x x-Coordinate of grid
     * @param y y-Coordinate of grid
     * @return true if in bounds, false if out-of-bounds
     */
    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Checks if spot on grid has an obstacle
     *
     * @param x x-Coordinate of grid
     * @param y y-Coordinate of grid
     * @return true if blocked, false otherwise
     */
    public boolean isBlocked(int x, int y) {
        return blocked[x][y];
    }

    /**
     * Sets an obstacle on the map
     *
     * @param x x-Coordinate of grid
     * @param y y-Coordinate of grid
     */
    public void setBlocked(int x, int y) {
        blocked[x][y] = true;
    }

    /**
     * Clears obstacle from grid
     *
     * @param x x-Coordinate of grid
     * @param y y-Coordinate of grid
     */
    public void clearBlocked(int x, int y) {
        blocked[x][y] = false;
    }

    /**
     *
     * @param nx new x-Coordinate of grid
     * @param ny new y-Coordinate of grid
     * @return true if move is legal, false otherwise
     */
    public boolean isLegalMove(int nx, int ny) {
        return inBounds(nx, ny) && !isBlocked(nx, ny);
    }

    /**
     * Counts how many obstacles are on grid
     *
     * @return integer representing amount of obstacles on the map
     */
    public int countObstacles() {
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (blocked[x][y])
                    count++;
            }
        }
        return count;
    }
}