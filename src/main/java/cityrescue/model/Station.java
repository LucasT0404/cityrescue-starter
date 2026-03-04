package cityrescue.model;

/**
 * Stores station object with its coordinates, names, and ID's
 */
public class Station {

    private final int stationId;
    private final String name;
    private final int x;
    private final int y;
    private int maxUnits;
    private int unitCount;

    /**
     * Creates new instance of a station
     *
     * @param stationId creates ID number for station
     * @param name      sets name for station
     * @param x         determines x-Coordinate on grid for station
     * @param y         determines y-Coordinate on grid for station
     * @param maxUnits  sets unit capacity for station
     */
    public Station(int stationId, String name, int x, int y, int maxUnits) {
        this.stationId = stationId;
        this.name = name;
        this.x = x;
        this.y = y;
        this.maxUnits = maxUnits;
        this.unitCount = 0;
    }

    /**
     * @return StationID number
     */
    public int getStationId() {
        return stationId;
    }

    /**
     * @return Station name
     */
    public String getName() {
        return name;
    }

    /**
     * @return x-Coordinate for station
     */
    public int getX() {
        return x;
    }

    /**
     * @return y coordinate for station
     */
    public int getY() {
        return y;
    }

    /**
     * @return unit capacity for station
     */
    public int getMaxUnits() {
        return maxUnits;
    }

    /**
     * @return current unit count for station
     */
    public int getUnitCount() {
        return unitCount;
    }

    /**
     * Sets unit capacity for station
     *
     * @param maxUnits new unit cap
     */
    public void setMaxUnits(int maxUnits) {
        this.maxUnits = maxUnits;
    }

    /**
     * @return true if station is not capacity, false if it is at capacity
     */
    public boolean hasCapacity() {
        return unitCount < maxUnits;
    }

    /**
     * Increases unit count at station
     */
    public void incrementUnitCount() {
        unitCount++;
    }

    /**
     * Decreases unit count at station
     */
    public void decrementUnitCount() {
        unitCount--;
    }
}