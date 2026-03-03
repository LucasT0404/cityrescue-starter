package cityrescue.model;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

/**
 * Contains FireEngine class as a subclass of Unit and contains constructor for
 * new FireEngine object. Handles FIRE type incidents
 */
public class FireEngine extends Unit {
    /**
     * Creates new FireEngine object
     *
     * @param unitId        assigns unitID number to FireEngine
     * @param homeStationId assigns stationID to FireEngine
     * @param x             initial x-Coord of FireEngine
     * @param y             initial y-Coord of FireEngine
     */
    public FireEngine(int unitId, int homeStationId, int x, int y) {
        super(unitId, UnitType.FIRE_ENGINE, homeStationId, x, y);
    }

    /**
     * @return true if incident type is FIRE, false otherwise
     */
    @Override
    public boolean canHandle(IncidentType type) {
        return type == IncidentType.FIRE;
    }

    /**
     * @return 4 work ticks to resolve incident
     */
    @Override
    public int getTicksToResolve(int severity) {
        return 4;
    }
}