package cityrescue.model;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

public class FireEngine extends Unit {

    public FireEngine(int unitId, int homeStationId, int x, int y) {
        super(unitId, UnitType.FIRE_ENGINE, homeStationId, x, y);
    }

    @Override
    public boolean canHandle(IncidentType type) {
        return type == IncidentType.FIRE;
    }

    @Override
    public int getTicksToResolve(int severity) {
        return 4;
    }
}