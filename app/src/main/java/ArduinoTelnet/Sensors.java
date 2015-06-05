package ArduinoTelnet;

import java.util.List;

/**
 * Sensors class
 * Using as storage for outdoor and array of rooms measurements
 *
 * @author Artem Chernatsky
 * @version 1.0.0
 */

public class Sensors {
    /** Amount of rooms */
    private int roomsNumber;

    /** Measurement of outdoor humidity */
    private Float humidity;

    /** Measurement of outdoor temperature */
    private Float temperature;

    /** Measurement of outdoor illumination */
    private Float illumination;

    /** Measurement of outdoor pressure */
    private Float pressure;

    /** Array for storing measurements from rooms */
    private Room rooms[];

    /**
     * Constructor with default parameters
     */
    public Sensors() {
        humidity = null;
        temperature = null;
        illumination = null;
        pressure = null;
    }

    /**
     * Constructor with custom parameters
     *
     * @param roomsNumber is amount of rooms
     */
    public Sensors(int roomsNumber) {
        humidity = null;
        temperature = null;
        illumination = null;
        pressure = null;
        this.roomsNumber = roomsNumber;
        rooms = new Room[roomsNumber];
        for(int i =0; i < roomsNumber; i++)
            rooms[i] = new Room();
    }

    /**
     * Method for adding outdoor measurements
     *
     * @param temperature is measurement of outdoor temperature
     * @param humidity is measurement of outdoor humidity
     * @param illumination is measurement of outdoor illumination
     * @param pressure is measurement of outdoor pressure
     */
    public void addMeasurements (Float temperature, Float humidity, Float illumination, Float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.illumination = illumination;
        this.pressure = pressure;
    }

    /**
     * Method converts object to String
     *
     * @return result of conversation to String
     */
    public String toString() {
        String line = "";
        Room room;
        line += "Outdoor:";
        line+=" T=" + temperature;
        line+=" H=" + humidity;
        line+=" L=" + illumination;
        line+=" P=" + pressure;
        line += "\n";
        for(int i = 0; i < roomsNumber; i++) {
            room = rooms[i];
            line += "Room " + (i + 1) + ":";
            line += " T=" + room.getTemperature();
            line += " H=" + room.getHumidity();
            line += " L=" + room.getIllumination();
            line += "\n";
        }
        return line;
    }

    /**
     * Method for adding room's measurement to the array by room index
     *
     * @param index is index of room int the array
     * @param room is object with measurements
     */
    public void addRoomByIndex (int index, Room room) {
        rooms[index] = room;
    }

    /**
     * Method for setting room's humidity
     *
     * @param humidity is measurement of outdoor's humidity
     */
    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    /**
     * Method for setting room's temperature
     *
     * @param temperature is measurement of outdoor's temperature
     */
    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    /**
     * Method for setting room's illumination
     *
     * @param illumination is measurement of outdoor's illumination
     */
    public void setIllumination(Float illumination) {
        this.illumination = illumination;
    }

    /**
     * Method for setting room's illumination
     *
     * @param pressure is measurement of outdoor's pressure
     */
    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    /**
     * Method which returns outdoor humidity measurement
     *
     * @return outdoor humidity measurement
     */

    public Float getHumidity () {
        return humidity;
    }

    /**
     * Method which returns outdoor temperature measurement
     *
     * @return outdoor temperature measurement
     */
    public Float getTemperature () {
        return temperature;
    }

    /**
     * Method which returns outdoor illumination measurement
     *
     * @return outdoor illumination measurement
     */
    public Float getIllumination () {
        return illumination;
    }

    /**
     * Method which returns outdoor pressure measurement
     *
     * @return outdoor pressure measurement
     */
    public Float getPressure () {
        return pressure;
    }

    /**
     * Method which returns amount of rooms
     *
     * @return amount of rooms
     */
    public int getRoomNumber () {
        return roomsNumber;
    }

    /**
     * Method which return room according to it's index
     *
     * @param index is room's number
     * @return room according to it's index
     * @throws ArrayIndexOutOfBoundsException
     */
    public Room getRoomByIndex (int index) throws ArrayIndexOutOfBoundsException {
        return rooms[index];
    }

    /**
     * Method which return average value of list's measurements
     *
     * @param list list with measurements
     * @return average value
     */
    public Sensors getAverage (List<Sensors> list) {
        Sensors sensor = new Sensors(list.get(0).getRoomNumber());
        /*sensor.setTemperature(0.0f);
        sensor.setHumidity(0.0f);
        sensor.setIllumination(0.0f);
        sensor.setPressure(0.0f);
        for(int j = 0; j < sensor.getRoomNumber(); j++) {
            sensor.getRoomByIndex(j).setTemperature(0.0f);
            sensor.getRoomByIndex(j).setHumidity(0.0f);
            sensor.getRoomByIndex(j).setIllumination(0.0f);
        }*/
        for(int i =0 ; i < list.size(); i++) {
            sensor.setTemperature(sensor.getTemperature() == null ? 0.0f : sensor.getTemperature() + (list.get(i).getTemperature() == null ? 0.0f : list.get(i).getTemperature()));
            sensor.setHumidity(sensor.getHumidity() == null ? 0.0f : sensor.getHumidity() + (list.get(i).getHumidity() == null ? 0.0f : list.get(i).getHumidity()));
            sensor.setIllumination(sensor.getIllumination() == null ? 0.0f : sensor.getIllumination() + (list.get(i).getIllumination() == null ? 0.0f : list.get(i).getIllumination()));
            sensor.setPressure(sensor.getPressure() == null ? 0.0f : sensor.getPressure() + (list.get(i).getPressure() == null ? 0.0f : list.get(i).getPressure()));
            for(int j = 0; j < sensor.getRoomNumber(); j++) {
                sensor.getRoomByIndex(j).setTemperature(sensor.getRoomByIndex(j).getTemperature() == null ? 0.0f : sensor.getRoomByIndex(j).getTemperature() + (list.get(i).getRoomByIndex(j).getTemperature() == null ? 0.0f : list.get(i).getRoomByIndex(j).getTemperature()));
                sensor.getRoomByIndex(j).setHumidity(sensor.getRoomByIndex(j).getHumidity() == null ? 0.0f : sensor.getRoomByIndex(j).getHumidity() + (list.get(i).getRoomByIndex(j).getHumidity() == null ? 0.0f : list.get(i).getRoomByIndex(j).getHumidity()));
                sensor.getRoomByIndex(j).setIllumination(sensor.getRoomByIndex(j).getIllumination() == null ? 0.0f : sensor.getRoomByIndex(j).getIllumination() + (list.get(i).getRoomByIndex(j).getIllumination() == null ? 0.0f : list.get(i).getRoomByIndex(j).getIllumination()));
            }
        }
        sensor.setTemperature(sensor.getTemperature() / list.size());
        sensor.setHumidity(sensor.getHumidity() / list.size());
        sensor.setIllumination(sensor.getIllumination() / list.size());
        sensor.setPressure(sensor.getPressure() / list.size());
        for(int j = 0; j < sensor.getRoomNumber(); j++) {
            sensor.getRoomByIndex(j).setTemperature(sensor.getRoomByIndex(j).getTemperature() / list.size());
            sensor.getRoomByIndex(j).setHumidity(sensor.getRoomByIndex(j).getHumidity() / list.size());
            sensor.getRoomByIndex(j).setIllumination(sensor.getRoomByIndex(j).getIllumination() / list.size());
        }
        return sensor;
    }
}
