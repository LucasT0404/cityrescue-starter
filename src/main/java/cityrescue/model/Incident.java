package cityrescue.model;

import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;

public class Incident {

    private final int incidentId;
    private final IncidentType type;
    private int severity;
    private final int x;
    private final int y;
    private IncidentStatus status;
    private int assignedUnitId;

    public Incident(int incidentId, IncidentType type, int severity, int x, int y) {
        this.incidentId = incidentId;
        this.type = type;
        this.severity = severity;
        this.x = x;
        this.y = y;
        this.status = IncidentStatus.REPORTED;
        this.assignedUnitId = -1;
    }

    public int getIncidentId() {
        return incidentId;
    }

    public IncidentType getType() {
        return type;
    }

    public int getSeverity() {
        return severity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public int getAssignedUnitId() {
        return assignedUnitId;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public void setAssignedUnitId(int unitId) {
        this.assignedUnitId = unitId;
    }
}