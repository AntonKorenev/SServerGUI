package SensorDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ArduinoTelnet.Room;
import ArduinoTelnet.Sensors;


public class SensorDataBase {
    private SensorDataBaseHelper sensorDatabaseHelper;
    private SQLiteDatabase sensorDatabase;
    private Context context;
    public SensorDataBase (Context context) {
        this.context = context;
        sensorDatabaseHelper = new SensorDataBaseHelper(context);
        sensorDatabase = sensorDatabaseHelper.getWritableDatabase();
    }
    private List<Sensors> getData(String filter) {
        List<Sensors> list = Collections.synchronizedList(new LinkedList<Sensors>());
        Cursor cursor;
        Sensors sensor;
        String sql = "";
        sql += "select outdoor.TEMPERATURE, outdoor.HUMIDITY, outdoor.ILLUMINATION, outdoor.PRESSURE";
        for(int i = 1; i <= sensorDatabaseHelper.getROOM_NUMBER(); i++ ) {
            sql += ", room" + i + ".TEMPERATURE";
            sql += ", room" + i + ".HUMIDITY";
            sql += ", room" + i + ".ILLUMINATION";
        }
        sql += " from base left join outdoor on base.key_outdoor = outdoor._id";
        for(int i = 1; i <= sensorDatabaseHelper.getROOM_NUMBER(); i++ )
            sql += " left join room" + i + " on key_room" + i + " = room" + i + "._id";
        sql += " " + filter;
        cursor = sensorDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                sensor = new Sensors(sensorDatabaseHelper.getROOM_NUMBER());
                sensor.addMeasurements(cursor.getFloat(0), cursor.getFloat(1), cursor.getFloat(2), cursor.getFloat(3));
                for (int i = 1; i <= sensorDatabaseHelper.getROOM_NUMBER(); i++ )
                    sensor.addRoomByIndex(i - 1, new Room(cursor.getFloat(3 + i),cursor.getFloat(4 + i),cursor.getFloat(5 + i)));
                list.add(sensor);
            } while (cursor.moveToNext());
        }
        return list;
    }
    public void addData(Sensors sensor) {
        Cursor cursor;
        Room room;
        int outdoorIndex = 0, roomIndex[];
        String sql = "";
        roomIndex = new int [sensorDatabaseHelper.getROOM_NUMBER()];
        sql = "insert into outdoor (TEMPERATURE,HUMIDITY,ILLUMINATION,PRESSURE) values (";
        sql += sensor.getTemperature() + ", " + sensor.getHumidity() + " ," + sensor.getIllumination() + ", " + sensor.getPressure() +")";
        sensorDatabase.execSQL(sql);
        sql = "select _id from outdoor where _id = (select max (_id) from outdoor)";
        cursor = sensorDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst())
            outdoorIndex = cursor.getInt(0);
        for(int i = 0; i < sensorDatabaseHelper.getROOM_NUMBER(); i++)
        {
            room = sensor.getRoomByIndex(i);
            sql = "insert into room" + (i + 1) + "(TEMPERATURE,HUMIDITY,ILLUMINATION) values (";
            sql += room.getTemperature() + ", " + room.getHumidity() + " ," + room.getIllumination()  +")";
            sensorDatabase.execSQL(sql);
            sql = "select _id from room" + (i + 1) + " where _id = (select max(_id) from room" + (i + 1) + ")";
            cursor = sensorDatabase.rawQuery(sql, null);
            if (cursor.moveToFirst())
                roomIndex[i] = cursor.getInt(0);
        }
        sql = "insert into base (DATE, TIME, key_outdoor";
        for(int i = 1; i <= sensorDatabaseHelper.getROOM_NUMBER(); i++)
            sql += ",key_room" + i;
        sql += ") values (";
        sql += "date('now','localtime'), time('now','localtime'), " + outdoorIndex;
        for(int i = 0; i < sensorDatabaseHelper.getROOM_NUMBER(); i++)
            sql += ", " + roomIndex[i];
        sql += ")";
        sensorDatabase.execSQL(sql);
        cursor.close();
    }
    public void setRoomNumber(int number) {
        sensorDatabaseHelper.setROOM_NUMBER(number);
    }
    public void  create () {
        sensorDatabaseHelper.create(sensorDatabase);
    }
    public List<Sensors> getData () {
        return getData("");
    }
    public Sensors getLast() {
        return getData("where base._id = (select max(_id) from base)").get(0);
    }
    public List<Sensors> getDataByDate (String date) {
        return getData("where base.DATE = " + "'" + date + "'");
    }
    public List<Sensors> getDataByDateInRange (String dateBegin, String dateEnd) {
        return getData("where base.DATE between " + dateBegin + " and " + dateEnd);
    }
    public List<Sensors> getDataByTimeInRange (String timeBegin, String timeEnd) {
        return getData("where base.TIME between " + timeBegin + " and " + timeEnd);
    }
    public List<Sensors> getDataByDateAndTimeInRange (String date, String timeBegin, String timeEnd) {
        return getData("where base.DATE = " + "'" +  date + "' and base.TIME between " + timeBegin + " and " + timeEnd);
    }
    public List<Sensors> getDataByDateInRangeAndTimeInRange (String dateBegin, String dateEnd, String timeBegin, String timeEnd) {
        return getData("where base.DATE between " + dateBegin + " and " + dateEnd + " and base.TIME between " + timeBegin + " and " + timeEnd);
    }
    public File getBaseFile () {
        return new File(sensorDatabaseHelper.DATABASE_PATH + sensorDatabaseHelper.DATABASE_NAME);
    }
    public void drop() {
        for(int i = 0; i < sensorDatabaseHelper.getROOM_NUMBER(); i++)
            sensorDatabase.execSQL("drop table if exists room"+(i+1));
        sensorDatabase.execSQL("drop table if exists outdoor");
        sensorDatabase.execSQL("drop table if exists base");
    }
}
