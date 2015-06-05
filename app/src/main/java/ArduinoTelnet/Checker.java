package ArduinoTelnet;

import java.util.List;

import SensorDB.SensorDataBase;

/**
 * Checker class
 * Using for requesting arduino in separate thread
 *
 * @author Artem Chernatsky
 * @version 1.0.0
 */

public class Checker extends Thread {
    private int delay;
    private List<Sensors> measurementList;
    private Connector connector;
    SensorDataBase dataBase;
    private int roomsAmount;
    Checker (Connector connect, List<Sensors> list, int delay, SensorDataBase dataBase) {
        this.delay = delay;
        measurementList = list;
        connector = connect;
        this.dataBase = dataBase;
    }
    @Override
    public void run() {
        Sensors sensor;
        String line;
        line = connector.sendCommand("get_rooms_amount");
        String [] arr = line.split (" ");
        //roomsAmount = Integer.parseInt(arr[1]);
        //System.out.println(line);
        roomsAmount = 1;
        while( ! isInterrupted()) {
            Float humidity = null;
            Float temperature = null;
            Float illumination = null;
            Float pressure = null;
            sensor = new Sensors(roomsAmount);
            line = connector.sendCommand("sensor_request outdoor");
            int sensorsAmount;
            int sensorsParameters[];
            int shift = 0;
            arr = line.split (" ");
            sensorsAmount = Integer.parseInt(arr[0]);
            sensorsParameters = new int [sensorsAmount];
            for(int i = 2; i <= sensorsAmount * 2; i += 2)
                sensorsParameters[i / 2 - 1] = Integer.parseInt(arr[i]);
            for(int i = 0; i < sensorsAmount; i++) {
                float value = 0;
                for (int j = 0; j < sensorsParameters[i]; j++)
                    value += Double.parseDouble(arr[2 * sensorsAmount + 1 + shift + j]);
                value /= sensorsParameters[i];
                switch(arr[2 * i + 1]) {
                    case "TEMPERATURE":
                        temperature = value;
                        break;
                    case "HUMIDITY":
                        humidity = value;
                        break;
                    case "ILLUMINATION":
                        illumination = value;
                        break;
                    case "PRESSURE":
                        pressure = value;
                        break;
                }
                shift += sensorsParameters[i];
            }
            sensor.addMeasurements(temperature, humidity, illumination, pressure);
            for(int i = 1; i <= roomsAmount; i++) {
                humidity = null;
                temperature = null;
                illumination = null;
                line = connector.sendCommand("sensor_request room " + i);
                shift = 0;
                arr = line.split (" ");
                sensorsAmount = Integer.parseInt(arr[0]);
                sensorsParameters = new int [sensorsAmount];
                for(int j = 2; j <= sensorsAmount * 2; j += 2)
                    sensorsParameters[j / 2 - 1] = Integer.parseInt(arr[j]);
                for(int j = 0; j < sensorsAmount; j++) {
                    float value = 0;
                    for (int k = 0; k < sensorsParameters[j]; k++)
                        value += Double.parseDouble(arr[2 * sensorsAmount + 1 + shift + k]);
                    value /= sensorsParameters[j];
                    switch(arr[2 * j + 1]) {
                        case "TEMPERATURE":
                            temperature = value;
                            break;
                        case "HUMIDITY":
                            humidity = value;
                            break;
                        case "ILLUMINATION":
                            illumination = value;
                            break;
                    }
                    shift += sensorsParameters[j];
                }
                sensor.addRoomByIndex(i - 1, new Room(temperature, humidity, illumination));
            }
            if(measurementList.size() == 30) {
                Sensors s = new Sensors();
                s = s.getAverage(measurementList);
                if(dataBase != null)
                    dataBase.addData(s);
                measurementList.clear();
            }
            measurementList.add(sensor);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

}