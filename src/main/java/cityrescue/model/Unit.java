package cityrescue.model;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

public abstract class Unit {

    private final int unitId;
    private final UnitType unitType;
    private int homeStationId;
    private int x;
    private int y;
    private UnitStatus status;
    private int assignedIncidentId;
    private int workTicksRemaining;

    public Unit(int unitId, UnitType unitType, int homeStationId, int x, int y) {
        this.unitId = unitId;
        this.unitType = unitType;
        this.homeStationId = homeStationId;
        this.x = x;
        this.y = y;
        this.status = UnitStatus.IDLE;
        this.assignedIncidentId = -1;
        this.workTicksRemaining = 0;
    }

    public abstract boolean canHandle(IncidentType type);
    public abstract int getTicksToResolve(int severity);

    public int getUnitId()               { return unitId; }
    public UnitType getUnitType()        { return unitType; }
    public int getHomeStationId()        { return homeStationId; }
    public int getX()                    { return x; }
    public int getY()                    { return y; }
    public UnitStatus getStatus()        { return status; }
    public int getAssignedIncidentId()   { return assignedIncidentId; }
    public int getWorkTicksRemaining()   { return workTicksRemaining; }

    public void setHomeStationId(int homeStationId) { this.homeStationId = homeStationId; }
    public void setX(int x)                         { this.x = x; }
    public void setY(int y)                         { this.y = y; }
    public void setStatus(UnitStatus status)         { this.status = status; }
    public void setAssignedIncidentId(int id)        { this.assignedIncidentId = id; }
    public void setWorkTicksRemaining(int ticks)     { this.workTicksRemaining = ticks; }
    public void decrementWorkTicks()                 { if (workTicksRemaining > 0) workTicksRemaining--; }

    public int manhattanDistance(int tx, int ty) {
        return Math.abs(x - tx) + Math.abs(y - ty);
    }
}