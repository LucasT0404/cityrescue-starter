package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;
import cityrescue.model.*;

public class CityRescueImpl implements CityRescue {

    private static final int MAX_STATIONS = 20;
    private static final int MAX_UNITS = 50;
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

    /**
     * Starts the simulation and eliminates existing data
     *
     * @param width  width of the map
     * @param height height of the map
     * @throws InvalidGridException in case width and/or height are negative numbers
     */
    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        if (width <= 0 || height <= 0) {
            throw new InvalidGridException("Width and height must be positive.");
        }
        map = new CityMap(width, height);
        currentTick = 0;

        stations = new Station[MAX_STATIONS];
        stationCount = 0;
        nextStationId = 1;

        units = new Unit[MAX_UNITS];
        unitCount = 0;
        nextUnitId = 1;

        incidents = new Incident[MAX_INCIDENTS];
        incidentCount = 0;
        nextIncidentId = 1;
    }

    /**
     * Finds and returns a list with the width and height of the grid.
     *
     * @return integer list with grid[width, height]
     */
    @Override
    public int[] getGridSize() {
        return new int[] { map.getWidth(), map.getHeight() };
    }

    /**
     * Adds an obstacle to the grid
     *
     * @param x x-Coordinate in the grid
     * @param y y-Coordinate in the grid.
     * @throws InvalidLocationException in case of the (x, y) coords being
     *                                  out-of-bouds.
     */
    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {
        if (!map.inBounds(x, y)) {
            throw new InvalidLocationException("Location (" + x + "," + y + ") is out of bounds.");
        }
        map.setBlocked(x, y);
    }

    /**
     * Removes an obstacle from the grid
     *
     * @param x x-Coordinate of obstacle on grid
     * @param y y-Coordinate of obstacle on grid
     * @throws InvalidLocationException in case of (x, y) coords being out-of-bounds
     */
    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        if (!map.inBounds(x, y)) {
            throw new InvalidLocationException("Location (" + x + "," + y + ") is out of bounds.");
        }
        map.clearBlocked(x, y);
    }

    /**
     * Adds a station to the grid
     *
     * @param name string to name the station
     * @param x    x-Coordinate on grid
     * @param y    y-Coordinate on grid.
     * @return returns the created Station ID
     * @throws InvalidNameException     in case of a name for the station not
     *                                  provided
     * @throws InvalidLocationException in case of (x, y) coords being out-of-bounds
     */
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

    /**
     * Remove station from the map
     *
     * @param stationId number of Station ID
     * @throws IDNotRecognisedException in case of the StationID being non-existent.
     * @throws IllegalStateException    in case of the Station still having
     *                                  remaining units
     */
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

    /**
     * Sets a unit capacity for a station
     *
     * @param stationID number of Station ID.
     * @param maxUnits  amount of units assigned to the station
     * @throws IDNotRecognisedException in case of StationId being non-existent
     * @throws InvalidCapacityException in case of capacity being a negative number
     *                                  or less than current unit count
     */
    @Override
    public void setStationCapacity(int stationId, int maxUnits)
            throws IDNotRecognisedException, InvalidCapacityException {
        int idx = findStationIndex(stationId);
        if (maxUnits <= 0) {
            throw new InvalidCapacityException("Capacity must be > 0.");
        }
        if (maxUnits < stations[idx].getUnitCount()) {
            throw new InvalidCapacityException("Capacity less than current unit count.");
        }
        stations[idx].setMaxUnits(maxUnits);
    }

    /**
     * Finds stationID
     *
     * @return an integer list with stationId's
     */
    @Override
    public int[] getStationIds() {
        int[] ids = new int[stationCount];
        for (int i = 0; i < stationCount; i++) {
            ids[i] = stations[i].getStationId();
        }
        sortAscending(ids);
        return ids;
    }

    /**
     * Adds a unit to a station
     *
     * @param stationId number of StationID
     * @param type      takes type of unit added to the station
     * @return Id of the unit added
     * @throws IDNotRecognisedException in case of station ID being non-existent
     * @throws InvalidUnitException     in case of unit type being null
     * @throws IllegalStateException    in case of maximum number of units at
     *                                  station reached.
     */
    @Override
    public int addUnit(int stationId, UnitType type)
            throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
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

    /**
     * Permanently removes unit
     *
     * @param unitId number for unit ID
     * @throws IDNotRecognisedException in case of non-existent unit ID
     * @throws IllegalStateException    in case of unit being active (EN_ROUTE or
     *                                  AT_SCENE)
     */
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

    /**
     * Gets ID for all existing units
     *
     * @return list of unit ID's
     */
    @Override
    public int[] getUnitIds() {
        int[] ids = new int[unitCount];
        for (int i = 0; i < unitCount; i++) {
            ids[i] = units[i].getUnitId();
        }
        sortAscending(ids);
        return ids;
    }

    /**
     * Creates instance of an incident and IncidentID
     *
     * @param type     takes type of incident
     * @param severity severity level number
     * @param x        x-Coordinate in the map
     * @param y        y-Coordinate in the map
     * @return number of IncidentID
     * @throws InvalidSeverityException in case of severity argument not being
     *                                  between 1-5
     * @throws InvalidLocationException in case of (x, y) coords being out-of-bounds
     */
    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y)
            throws InvalidSeverityException, InvalidLocationException {
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

    /**
     * Gets ID's for all incidents on grid
     *
     * @return list of all existing IncidentID's
     */
    @Override
    public int[] getIncidentIds() {
        int[] ids = new int[incidentCount];
        for (int i = 0; i < incidentCount; i++) {
            ids[i] = incidents[i].getIncidentId();
        }
        sortAscending(ids);
        return ids;
    }

    /**
     * Transfers unit from one station to another
     *
     * @param unitId       Id number of unit being transferred
     * @param newStationId Id number of new station for the selected unit.
     * @throws IDNotRecognisedException in case of either ID being non-existent
     * @throws IllegalStateException    in case of unit being active or station
     *                                  being at capacity
     */
    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        int idx = findUnitIndex(unitId);
        Unit u = units[idx];
        int sIdx = findStationIndex(newStationId);
        Station s = stations[sIdx];
        if (u.getStatus() != UnitStatus.IDLE) {
            throw new IllegalStateException("Unit must be Idle");
        }
        if (!s.hasCapacity()) {
            throw new IllegalStateException("Station is at capacity");
        }
        int oldIdx = findStationIndex(u.getHomeStationId());
        stations[oldIdx].decrementUnitCount();
        u.setHomeStationId(newStationId);
        u.setX(s.getX());
        u.setY(s.getY());
        s.incrementUnitCount();
    }

    /**
     * Sets selected unit out of service
     *
     * @param unitId       ID number of selected unit
     * @param outOfService boolean state of unit
     * @throws IDNotRecognisedException in case of unit ID being non-existent
     * @throws IllegalStateException    in case of unit not being in the required
     *                                  state (IDLE to disable, OUT_OF_SERVICE to
     *                                  enable)
     */
    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService)
            throws IDNotRecognisedException, IllegalStateException {
        int idx = findUnitIndex(unitId);
        Unit u = units[idx];
        if (outOfService) {
            if (u.getStatus() != UnitStatus.IDLE) {
                throw new IllegalStateException("Unit must be Idle");
            }
            u.setStatus(UnitStatus.OUT_OF_SERVICE);
        } else {
            if (u.getStatus() != UnitStatus.OUT_OF_SERVICE) {
                throw new IllegalStateException("Unit must be Out of Service");

            }
            u.setStatus(UnitStatus.IDLE);
        }
    }

    /**
     * @param unitID what unit the the method is viewing
     * @return unit string matching format
     * @throws IDNotRecognisedException in case unitID doesn't exist
     */
    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        int idx = findUnitIndex(unitId);
        Unit u = units[idx];
        String result = "U#" + u.getUnitId()
                + " TYPE=" + u.getUnitType()
                + " HOME=" + u.getHomeStationId()
                + " LOC=(" + u.getX() + "," + u.getY() + ")"
                + " STATUS=" + u.getStatus()
                + " INCIDENT=" + (u.getAssignedIncidentId() == -1 ? "-" : u.getAssignedIncidentId());
        if (u.getStatus() == UnitStatus.AT_SCENE) {
            result += " WORK=" + u.getWorkTicksRemaining();
        }
        return result;
    }

    /**
     * Cancels an incident instance
     *
     * @param incidentId Id number of incident
     * @throws IDNotRecognisedException in case of incident ID being non-existent
     * @throws IllegalStateException    in case of incident status not being
     *                                  DISPATCHED or REPORTED
     */
    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        int idx = findIncidentIndex(incidentId);
        Incident i = incidents[idx];
        if (i.getStatus() != IncidentStatus.REPORTED && i.getStatus() != IncidentStatus.DISPATCHED) {
            throw new IllegalStateException("Cannot cancel incident");
        }
        if (i.getStatus() == IncidentStatus.DISPATCHED) {
            int uIdx = findUnitIndex(i.getAssignedUnitId());
            Unit u = units[uIdx];
            u.setStatus(UnitStatus.IDLE);
            u.setAssignedIncidentId(-1);
        }
        i.setStatus(IncidentStatus.CANCELLED);
        i.setAssignedUnitId(-1);
    }

    /**
     * Escalates incident severity level
     *
     * @param incidentId  ID number of selected incident
     * @param newSeverity new severity level for the incident
     * @throws IDNotRecognisedException in case of non-existent IncidentID
     * @throws InvalidSeverityException in case of severity level not being 1-5
     * @throws IllegalStateException    in case of incident being RESOLVED or
     *                                  CANCELLED
     */
    @Override
    public void escalateIncident(int incidentId, int newSeverity)
            throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        int idx = findIncidentIndex(incidentId);
        Incident i = incidents[idx];
        if (newSeverity < 1 || newSeverity > 5) {
            throw new InvalidSeverityException("Severity must be 1-5");
        }
        if (i.getStatus() == IncidentStatus.RESOLVED || i.getStatus() == IncidentStatus.CANCELLED) {
            throw new IllegalStateException("Incident must not be Resolved or Cancelled");
        }
        i.setSeverity(newSeverity);
    }

    /**
     * Print incident details
     *
     * @param incidentId ID number of selected incident
     * @return string containing Incident: ID number, type, severity level,
     *         location, status, and assigned unit.
     * @throws IDNotRecognisedException in case of non-existent incidentID
     */
    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        int idx = findIncidentIndex(incidentId);
        Incident i = incidents[idx];
        String result = "I#" + i.getIncidentId()
                + " TYPE=" + i.getType()
                + " SEV=" + i.getSeverity()
                + " LOC=(" + i.getX() + "," + i.getY() + ")"
                + " STATUS=" + i.getStatus()
                + " UNIT=" + (i.getAssignedUnitId() == -1 ? "-" : i.getAssignedUnitId());
        return result;
    }

    /**
     * Dispatches units to assigned incidents and sets units and incidents to
     * EN_ROUTE and DISPATCHED statuses
     */
    @Override
    public void dispatch() {
        for (int i = 0; i < incidentCount; i++) {
            if (incidents[i].getStatus() != IncidentStatus.REPORTED) {
                continue;
            }
            Unit bestUnit = null;
            for (int j = 0; j < unitCount; j++) {
                if (units[j].getStatus() == UnitStatus.IDLE && units[j].canHandle(incidents[i].getType())) {
                    if (bestUnit == null) {
                        bestUnit = units[j];
                    } else {
                        int newDistance = units[j].manhattanDistance(incidents[i].getX(), incidents[i].getY());
                        int bestDistance = bestUnit.manhattanDistance(incidents[i].getX(), incidents[i].getY());
                        if (newDistance < bestDistance) {
                            bestUnit = units[j];
                        } else if (newDistance == bestDistance) {
                            if (units[j].getUnitId() < bestUnit.getUnitId()) {
                                bestUnit = units[j];
                            } else if (units[j].getUnitId() == bestUnit.getUnitId()) {
                                if (units[j].getHomeStationId() < bestUnit.getHomeStationId()) {
                                    bestUnit = units[j];
                                }
                            }
                        }
                    }
                }
            }
            if (bestUnit != null) {
                bestUnit.setStatus(UnitStatus.EN_ROUTE);
                bestUnit.setAssignedIncidentId(incidents[i].getIncidentId());
                incidents[i].setStatus(IncidentStatus.DISPATCHED);
                incidents[i].setAssignedUnitId((bestUnit.getUnitId()));
            }
        }
    }

    /**
     * Advances simulation by one tick
     * Starts movement for EN_ROUTE units toward their assigned incidents
     * Checks for unit arrivals and starts AT_SCENE status
     * Decreases work ticks remaining for IN_PROGRESS incidents
     * Sets completed incidents to RESOLVED
     */
    @Override
    public void tick() {
        currentTick++;

        for (int i = 0; i < unitCount; i++) {
            if (units[i].getStatus() == UnitStatus.EN_ROUTE) {
                int xCoord = 0;
                int yCoord = 0;
                for (int k = 0; k < incidentCount; k++) {
                    if (incidents[k].getIncidentId() == units[i].getAssignedIncidentId()) {
                        xCoord = incidents[k].getX();
                        yCoord = incidents[k].getY();
                        break;
                    }
                }
                int unitX = units[i].getX();
                int unitY = units[i].getY();
                int[] xDirection = { 0, 1, 0, -1 };
                int[] yDirection = { -1, 0, 1, 0 };
                boolean moved = false;
                int distance = units[i].manhattanDistance(xCoord, yCoord);
                for (int j = 0; j < xDirection.length; j++) {
                    int newX = unitX + xDirection[j];
                    int newY = unitY + yDirection[j];
                    if (map.isLegalMove(newX, newY)) {
                        int newDistance = Math.abs(newX - xCoord) + Math.abs(newY - yCoord);
                        if (newDistance < distance) {
                            moved = true;
                            units[i].setX(newX);
                            units[i].setY(newY);
                            break;
                        }
                    }
                }
                if (!moved) {
                    for (int k = 0; k < yDirection.length; k++) {
                        int newX = unitX + xDirection[k];
                        int newY = unitY + yDirection[k];
                        if (map.isLegalMove(newX, newY)) {
                            units[i].setX(newX);
                            units[i].setY(newY);
                            break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < unitCount; i++) {
            if (units[i].getStatus() == UnitStatus.EN_ROUTE) {
                int xUnit = units[i].getX();
                int yUnit = units[i].getY();
                int xIncident = 0;
                int yIncident = 0;
                Incident inc = null;
                for (int l = 0; l < incidentCount; l++) {
                    if (incidents[l].getIncidentId() == units[i].getAssignedIncidentId()) {
                        inc = incidents[l];
                        xIncident = incidents[l].getX();
                        yIncident = incidents[l].getY();
                        break;
                    }
                }
                if (xUnit == xIncident && yUnit == yIncident) {
                    units[i].setStatus(UnitStatus.AT_SCENE);
                    units[i].setWorkTicksRemaining(units[i].getTicksToResolve(0));
                    inc.setStatus(IncidentStatus.IN_PROGRESS);
                }
            }
        }
        for (int i = 0; i < unitCount; i++) {
            if (units[i].getStatus() == UnitStatus.AT_SCENE) {
                units[i].decrementWorkTicks();
            }
        }
        for (int j = 0; j < incidentCount; j++) {
            if (incidents[j].getStatus() == IncidentStatus.IN_PROGRESS) {
                Unit u = null;
                for (int k = 0; k < unitCount; k++) {
                    if (units[k].getUnitId() == incidents[j].getAssignedUnitId()) {
                        u = units[k];
                        break;
                    }
                }
                if (u != null && u.getWorkTicksRemaining() == 0) {
                    incidents[j].setStatus(IncidentStatus.RESOLVED);
                    incidents[j].setAssignedUnitId(-1);
                    u.setStatus(UnitStatus.IDLE);
                    u.setAssignedIncidentId(-1);
                }
            }
        }
    }

    /**
     * Creates formatted status of simulation including
     * tick/station/unit/incident/obstacle counts, and details on all existing
     * incidents/units
     *
     * @return string of simulation status with all details mentioned above
     */
    @Override
    public String getStatus() {
        String result = "TICK=" + currentTick + "\n";
        result += "STATIONS=" + stationCount + " UNITS=" + unitCount + " INCIDENTS=" + incidentCount + " OBSTACLES="
                + map.countObstacles();
        result += "\nINCIDENTS";
        for (int i = 0; i < incidentCount; i++) {
            try {
                result += "\n" + viewIncident(incidents[i].getIncidentId());
            } catch (IDNotRecognisedException e) {
            }
        }
        result += "\nUNITS";
        for (int i = 0; i < unitCount; i++) {
            try {
                result += "\n" + viewUnit(units[i].getUnitId());
            } catch (IDNotRecognisedException e) {
            }
        }
        return result;
    }

    int findStationIndex(int stationId) throws IDNotRecognisedException {
        for (int i = 0; i < stationCount; i++) {
            if (stations[i].getStationId() == stationId)
                return i;
        }
        throw new IDNotRecognisedException("No station with ID " + stationId);
    }

    int findUnitIndex(int unitId) throws IDNotRecognisedException {
        for (int i = 0; i < unitCount; i++) {
            if (units[i].getUnitId() == unitId)
                return i;
        }
        throw new IDNotRecognisedException("No unit with ID " + unitId);
    }

    int findIncidentIndex(int incidentId) throws IDNotRecognisedException {
        for (int i = 0; i < incidentCount; i++) {
            if (incidents[i].getIncidentId() == incidentId)
                return i;
        }
        throw new IDNotRecognisedException("No incident with ID " + incidentId);
    }

    Station[] getStationsArray() {
        return stations;
    }

    int getStationCount() {
        return stationCount;
    }

    Unit[] getUnitsArray() {
        return units;
    }

    int getUnitCount() {
        return unitCount;
    }

    Incident[] getIncidentsArray() {
        return incidents;
    }

    int getIncidentCountField() {
        return incidentCount;
    }

    int getCurrentTick() {
        return currentTick;
    }

    void setCurrentTick(int t) {
        currentTick = t;
    }

    CityMap getMap() {
        return map;
    }

    private Unit createUnit(int id, UnitType type, int homeStationId, int x, int y) {
        switch (type) {
            case AMBULANCE:
                return new Ambulance(id, homeStationId, x, y);
            case FIRE_ENGINE:
                return new FireEngine(id, homeStationId, x, y);
            case POLICE_CAR:
                return new PoliceCar(id, homeStationId, x, y);
            default:
                throw new IllegalArgumentException("Unknown UnitType: " + type);
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