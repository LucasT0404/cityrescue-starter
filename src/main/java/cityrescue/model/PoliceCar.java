package cityrescue.model;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

/**
 * Contains PoliceCar class as a subclass of Unit and contains constructor for
 * new PoliceCar object. Handles CRIME type incidents
 */
public class PoliceCar extends Unit {
    /**
     * Creates new PoliceCar object
     *
     * @param unitId        assigns unitID number to PoliceCar
     * @param homeStationId assigns stationID to PoliceCar
     * @param x             initial x-Coord of PoliceCar
     * @param y             initial y-Coord of PoliceCar
     */
    public PoliceCar(int unitId, int homeStationId, int x, int y) {
        super(unitId, UnitType.POLICE_CAR, homeStationId, x, y);
    }

    /**
     * @return true if incident type is CRIME, false otherwise
     */
    @Override
    public boolean canHandle(IncidentType type) {
        return type == IncidentType.CRIME;
    }

    /**
     * @return 3 work ticks to resolve incident
     */
    @Override
    public int getTicksToResolve(int severity) {
        return 3;
    }
}