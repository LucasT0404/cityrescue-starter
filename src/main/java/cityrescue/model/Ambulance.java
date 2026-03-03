package cityrescue.model;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

/**
 * Contains Ambulance class as a subclass of Unit and contains constructor for
 * new Ambulance object. Handles MEDICAL type incidents
 */
public class Ambulance extends Unit {
    /**
     * Creates new Ambulance object
     *
     * @param unitId        assigns unitID number to ambulance
     * @param homeStationId assigns stationID to ambulance
     * @param x             initial x-Coord of ambulance
     * @param y             initial y-Coord of ambulance
     */
    public Ambulance(int unitId, int homeStationId, int x, int y) {
        super(unitId, UnitType.AMBULANCE, homeStationId, x, y);
    }

    /**
     * @return true if incident type is MEDICAL, false otherwise
     */
    @Override
    public boolean canHandle(IncidentType type) {
        return type == IncidentType.MEDICAL;
    }

    /**
     * @return 2 work ticks to resolve incident
     */
    @Override
    public int getTicksToResolve(int severity) {
        return 2;
    }
}