package cityrescue.model;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

/**
 * Stores unit object with details such as Unit: ID, type, home station ID,
 * x-coord, y-coord, status, assigned incident ID, and remaining work ticks.
 */
public abstract class Unit {

    private final int unitId;
    private final UnitType unitType;
    private int homeStationId;
    private int x;
    private int y;
    private UnitStatus status;
    private int assignedIncidentId;
    private int workTicksRemaining;

    /**
     * Creates new instance of a unit
     *
     * @param unitId        creates ID number for the unit
     * @param unitType      determines type of unit
     * @param homeStationId determines home station of the unit
     * @param x             determines the x-coordinate for the unit
     * @param y             determines the y-coordinate for the unit
     */
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

    /**
     * Checks if unit can handle assigned incident type
     *
     * @param type incident type
     * @return true if unit can handle type, false otherwise
     */
    public abstract boolean canHandle(IncidentType type);

    /**
     * Returns necessary work ticks to resolve incident
     *
     * @param severity severity of the assigned incident
     * @return number of work ticks needed
     */
    public abstract int getTicksToResolve(int severity);

    /**
     * @return unit ID number
     */
    public int getUnitId() {
        return unitId;
    }

    /**
     * @return unit type
     */
    public UnitType getUnitType() {
        return unitType;
    }

    /**
     * @return unit home station ID number
     */
    public int getHomeStationId() {
        return homeStationId;
    }

    /**
     * @return x-coordinate on the grid
     */
    public int getX() {
        return x;
    }

    /**
     * @return y-coordinate on the grid
     */
    public int getY() {
        return y;
    }

    /**
     * @return unit status
     */
    public UnitStatus getStatus() {
        return status;
    }

    /**
     * @return assigned incident ID number for the unit
     */
    public int getAssignedIncidentId() {
        return assignedIncidentId;
    }

    /**
     * @return work ticks remaining at the units incident
     */
    public int getWorkTicksRemaining() {
        return workTicksRemaining;
    }

    /**
     * Sets new home station for the unit
     *
     * @param homeStationId ID number for units home station
     */
    public void setHomeStationId(int homeStationId) {
        this.homeStationId = homeStationId;
    }

    /**
     * Sets new x-Coordinate for the unit
     *
     * @param x new x-Coordinate for the unit
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets new y-coordinate for the unit
     *
     * @param y new y-coordinate for the unit
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets new status type on unit
     *
     * @param status new unit Status
     */
    public void setStatus(UnitStatus status) {
        this.status = status;
    }

    /**
     * Assigns incident ID to unit
     *
     * @param id units assigned incident ID
     */
    public void setAssignedIncidentId(int id) {
        this.assignedIncidentId = id;
    }

    /**
     * Sets new amount of work ticks remaining
     *
     * @param ticks amount of work ticks remaining at an incident
     */
    public void setWorkTicksRemaining(int ticks) {
        this.workTicksRemaining = ticks;
    }

    /**
     * Decreases work ticks remaining for unit at incident
     */
    public void decrementWorkTicks() {
        if (workTicksRemaining > 0)
            workTicksRemaining--;
    }

    /**
     * Calculates Manhattan distance between unit and target
     *
     * @param tx unit target x-Coord
     * @param ty unit target y-Coord
     * @return Manhattan distance to target
     */
    public int manhattanDistance(int tx, int ty) {
        return Math.abs(x - tx) + Math.abs(y - ty);
    }
}