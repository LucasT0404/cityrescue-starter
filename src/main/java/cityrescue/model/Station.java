package cityrescue.model;

public class Station {

    private final int stationId;
    private final String name;
    private final int x;
    private final int y;
    private int maxUnits;
    private int unitCount;

    public Station(int stationId, String name, int x, int y, int maxUnits) {
        this.stationId = stationId;
        this.name = name;
        this.x = x;
        this.y = y;
        this.maxUnits = maxUnits;
        this.unitCount = 0;
    }

    public int getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMaxUnits() {
        return maxUnits;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public void setMaxUnits(int maxUnits) {
        this.maxUnits = maxUnits;
    }

    public boolean hasCapacity() {
        return unitCount < maxUnits;
    }

    public void incrementUnitCount() {
        unitCount++;
    }

    public void decrementUnitCount() {
        unitCount--;
    }
}