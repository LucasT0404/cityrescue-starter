package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;
import cityrescue.model.*;

public class CityRescueImpl implements CityRescue {

    private static final int MAX_STATIONS  = 20;
    private static final int MAX_UNITS     = 50;
    private static final int MAX_INCIDENTS = 200;
    private static final int DEFAULT_STATION_CAPACITY = 5;

    private CityMap map;
    private int currentTick;

    private Station[] stations;
    private int stationCount;
    private int nextStationId;

    private Unit[] units;
    private int unitCount;
    private int nextUnitId;

    private Incident[] incidents;
    private int incidentCount;
    private int nextIncidentId;

    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        if (width <= 0 || height <= 0) {
            throw new InvalidGridException("Width and height must be positive.");
        }
        map = new CityMap(width, height);
        currentTick = 0;

        stations      = new Station[MAX_STATIONS];
        stationCount  = 0;
        nextStationId = 1;

        units      = new Unit[MAX_UNITS];
        unitCount  = 0;
        nextUnitId = 1;

        incidents      = new Incident[MAX_INCIDENTS];
        incidentCount  = 0;
        nextIncidentId = 1;
    }

    @Override
    public int[] getGridSize() {
        return new int[]{map.getWidth(), map.getHeight()};
    }

    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {
        if (!map.inBounds(x, y)) {
            throw new InvalidLocationException("Location (" + x + "," + y + ") is out of bounds.");
        }
        map.setBlocked(x, y);
    }

    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        if (!map.inBounds(x, y)) {
            throw new InvalidLocationException("Location (" + x + "," + y + ") is out of bounds.");
        }
        map.clearBlocked(x, y);
    }

    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("Station name must not be blank.");
        }
        if (!map.inBounds(x, y)) {
            throw new InvalidLocationException("Location (" + x + "," + y + ") is out of bounds.");
        }
        if (map.isBlocked(x, y)) {
            throw new InvalidLocationException("Location (" + x + "," + y + ") is blocked.");
        }
        if (stationCount >= MAX_STATIONS) {
            throw new InvalidLocationException("Maximum number of stations reached.");
        }
        Station s = new Station(nextStationId, name, x, y, DEFAULT_STATION_CAPACITY);
        stations[stationCount] = s;
        stationCount++;
        return nextStationId++;
    }

    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        int idx = findStationIndex(stationId);
        if (stations[idx].getUnitCount() > 0) {
            throw new IllegalStateException("Station " + stationId + " still has units.");
        }
        for (int i = idx; i < stationCount - 1; i++) {
            stations[i] = stations[i + 1];
        }
        stations[stationCount - 1] = null;
        stationCount--;
    }

    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        int idx = findStationIndex(stationId);
        if (maxUnits <= 0) {
            throw new InvalidCapacityException("Capacity must be > 0.");
        }
        if (maxUnits < stations[idx].getUnitCount()) {
            throw new InvalidCapacityException("Capacity less than current unit count.");
        }
        stations[idx].setMaxUnits(maxUnits);
    }

    @Override
    public int[] getStationIds() {
        int[] ids = new int[stationCount];
        for (int i = 0; i < stationCount; i++) {
            ids[i] = stations[i].getStationId();
        }
        sortAscending(ids);
        return ids;
    }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        int sIdx = findStationIndex(stationId);
        if (type == null) {
            throw new InvalidUnitException("UnitType must not be null.");
        }
        if (!stations[sIdx].hasCapacity()) {
            throw new IllegalStateException("Station " + stationId + " is at full capacity.");
        }
        if (unitCount >= MAX_UNITS) {
            throw new IllegalStateException("Maximum number of units reached.");
        }
        Unit u = createUnit(nextUnitId, type, stationId, stations[sIdx].getX(), stations[sIdx].getY());
        units[unitCount] = u;
        unitCount++;
        stations[sIdx].incrementUnitCount();
        return nextUnitId++;
    }

    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        int idx = findUnitIndex(unitId);
        Unit u = units[idx];
        if (u.getStatus() == UnitStatus.EN_ROUTE || u.getStatus() == UnitStatus.AT_SCENE) {
            throw new IllegalStateException("Cannot decommission unit " + unitId + ": currently active.");
        }
        int sIdx = findStationIndex(u.getHomeStationId());
        stations[sIdx].decrementUnitCount();
        for (int i = idx; i < unitCount - 1; i++) {
            units[i] = units[i + 1];
        }
        units[unitCount - 1] = null;
        unitCount--;
    }

    @Override
    public int[] getUnitIds() {
        int[] ids = new int[unitCount];
        for (int i = 0; i < unitCount; i++) {
            ids[i] = units[i].getUnitId();
        }
        sortAscending(ids);
        return ids;
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        if (type == null) {
            throw new InvalidLocationException("IncidentType must not be null.");
        }
        if (severity < 1 || severity > 5) {
            throw new InvalidSeverityException("Severity must be 1-5.");
        }
        if (!map.inBounds(x, y)) {
            throw new InvalidLocationException("Location (" + x + "," + y + ") is out of bounds.");
        }
        if (map.isBlocked(x, y)) {
            throw new InvalidLocationException("Location (" + x + "," + y + ") is blocked.");
        }
        if (incidentCount >= MAX_INCIDENTS) {
            throw new InvalidLocationException("Maximum number of incidents reached.");
        }
        Incident inc = new Incident(nextIncidentId, type, severity, x, y);
        incidents[incidentCount] = inc;
        incidentCount++;
        return nextIncidentId++;
    }

    @Override
    public int[] getIncidentIds() {
        int[] ids = new int[incidentCount];
        for (int i = 0; i < incidentCount; i++) {
            ids[i] = incidents[i].getIncidentId();
        }
        sortAscending(ids);
        return ids;
    }

    // ------------------------------------------------------------------ Person B stubs

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void dispatch() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void tick() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public String getStatus() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    // ------------------------------------------------------------------ helpers (package-visible for Person B)

    int findStationIndex(int stationId) throws IDNotRecognisedException {
        for (int i = 0; i < stationCount; i++) {
            if (stations[i].getStationId() == stationId) return i;
        }
        throw new IDNotRecognisedException("No station with ID " + stationId);
    }

    int findUnitIndex(int unitId) throws IDNotRecognisedException {
        for (int i = 0; i < unitCount; i++) {
            if (units[i].getUnitId() == unitId) return i;
        }
        throw new IDNotRecognisedException("No unit with ID " + unitId);
    }

    int findIncidentIndex(int incidentId) throws IDNotRecognisedException {
        for (int i = 0; i < incidentCount; i++) {
            if (incidents[i].getIncidentId() == incidentId) return i;
        }
        throw new IDNotRecognisedException("No incident with ID " + incidentId);
    }

    Station[]  getStationsArray()     { return stations; }
    int        getStationCount()      { return stationCount; }
    Unit[]     getUnitsArray()        { return units; }
    int        getUnitCount()         { return unitCount; }
    Incident[] getIncidentsArray()    { return incidents; }
    int        getIncidentCountField(){ return incidentCount; }
    int        getCurrentTick()       { return currentTick; }
    void       setCurrentTick(int t)  { currentTick = t; }
    CityMap    getMap()               { return map; }

    // ------------------------------------------------------------------ private utilities

    private Unit createUnit(int id, UnitType type, int homeStationId, int x, int y) {
        switch (type) {
            case AMBULANCE:   return new Ambulance(id, homeStationId, x, y);
            case FIRE_ENGINE: return new FireEngine(id, homeStationId, x, y);
            case POLICE_CAR:  return new PoliceCar(id, homeStationId, x, y);
            default: throw new IllegalArgumentException("Unknown UnitType: " + type);
        }
    }

    private void sortAscending(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
}