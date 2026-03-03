package cityrescue.model;

import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;

/**
 * Stores Incident object including Incident details: ID, type, severity,
 * x-coord, y-coord, status, assignedUnit
 */
public class Incident {

    private final int incidentId;
    private final IncidentType type;
    private int severity;
    private final int x;
    private final int y;
    private IncidentStatus status;
    private int assignedUnitId;

    /**
     * Creates new instance of an Incident
     *
     * @param incidentId ID number of Incident
     * @param type       type of incident
     * @param severity   level of incident severity
     * @param x          x-Coordinate of incident
     * @param y          y - coordinate of Incident
     */
    public Incident(int incidentId, IncidentType type, int severity, int x, int y) {
        this.incidentId = incidentId;
        this.type = type;
        this.severity = severity;
        this.x = x;
        this.y = y;
        this.status = IncidentStatus.REPORTED;
        this.assignedUnitId = -1;
    }

    /**
     * @return the Incident ID number
     */
    public int getIncidentId() {
        return incidentId;
    }

    /**
     * @return the incident type
     */
    public IncidentType getType() {
        return type;
    }

    /**
     * @return the incident severity level
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * @return the incident x-coord
     */
    public int getX() {
        return x;
    }

    /**
     * @return the incident y-coord
     */
    public int getY() {
        return y;
    }

    /**
     * @return the incident current status
     */
    public IncidentStatus getStatus() {
        return status;
    }

    /**
     * @return the assigned unit ID number for the incident
     */
    public int getAssignedUnitId() {
        return assignedUnitId;
    }

    /**
     * Sets new severity level
     * 
     * @param severity new severity level
     */
    public void setSeverity(int severity) {
        this.severity = severity;
    }

    /**
     * Sets new incident status
     * 
     * @param status new incident status
     */
    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    /**
     * Sets new assigned unit
     * 
     * @param unitId new assigned unit ID number
     */
    public void setAssignedUnitId(int unitId) {
        this.assignedUnitId = unitId;
    }
}