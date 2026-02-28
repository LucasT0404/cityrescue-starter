package cityrescue.model;

public class CityMap {

    private int width;
    private int height;
    private boolean[][] blocked;

    public CityMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.blocked = new boolean[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isBlocked(int x, int y) {
        return blocked[x][y];
    }

    public void setBlocked(int x, int y) {
        blocked[x][y] = true;
    }

    public void clearBlocked(int x, int y) {
        blocked[x][y] = false;
    }

    public boolean isLegalMove(int nx, int ny) {
        return inBounds(nx, ny) && !isBlocked(nx, ny);
    }

    public int countObstacles() {
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (blocked[x][y]) count++;
            }
        }
        return count;
    }
}