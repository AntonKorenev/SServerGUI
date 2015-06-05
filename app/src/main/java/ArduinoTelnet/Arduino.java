package ArduinoTelnet;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import SensorDB.SensorDataBase;

/**
 * Arduino class
 * Using for managing connection parameters and measurements
 *
 * @author Artem Chernatsky
 * @version 1.0.0
 */

public class Arduino {
    /** Connection to arduino */
    private Connector connect;

    /** List for measurements */
    private List<Sensors> measurementList;

    /** For updating requests from arduino and adding information to database in seperate thread*/
    private Checker checker;

    private SensorDataBase dataBase;

    /**
     * Constructor with custom address and port
     *
     * @param hostname is the address of arduino
     * @param hostport is the port number of arduino
     */
    public Arduino (String hostname, int hostport) {
        measurementList = Collections.synchronizedList(new LinkedList<Sensors>());
        connect = new Connector(hostname, hostport);
        dataBase = null;
    }
    public Arduino (String hostname, int hostport, SensorDataBase dataBase) {
        measurementList = Collections.synchronizedList(new LinkedList<Sensors>());
        connect = new Connector(hostname, hostport);
        this.dataBase = dataBase;
    }
    /**
     * Method for starting separate thread for updating requests from arduino and adding information to database
     *
     * @param delay is pause between requests
     */
    public void start(int delay) {
        try {
            checker.interrupt();
        } catch(Exception ex) {}
        checker = new Checker (connect, measurementList, delay, dataBase);
        checker.start();
    }

    /**
     * Method for stoping separate thread
     */
    public void stop() {
        try {
            checker.interrupt();
        } catch (Exception ex) {}
    }

    /**
     * Method for getting String layout of last sensor in the list
     *
     * @return String layout of the last element in the list
     */
    public Sensors getLast() {
        if(measurementList.size() != 0)
            return measurementList.get(measurementList.size() - 1);
        else
            return null;
    }
}