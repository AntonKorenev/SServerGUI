package SensorDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by artem on 25.04.2015.
 */
public class SensorDataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "SensorDatabase.db";
    public static final String DATABASE_PATH = "/data/data/com.shometeam.ao.sservergui/databases/";
    private static final int DATABASE_VERSION = 1;
    public  String[] TABLE_NAMES = {"base","outdoor","room"};
    private static int ROOM_NUMBER = 1;
    public String [] BASE_COLUMNS;
    public String [] OUTDOOR_COLUMNS;
    public String [] ROOMS_COLUMNS;
    public SensorDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        OUTDOOR_COLUMNS = new String [5];
        OUTDOOR_COLUMNS[0] = "_id";
        OUTDOOR_COLUMNS[1] = "TEMPERATURE";
        OUTDOOR_COLUMNS[2] = "HUMIDITY";
        OUTDOOR_COLUMNS[3] = "ILLUMINATION";
        OUTDOOR_COLUMNS[4] = "PRESSURE";
        ROOMS_COLUMNS = new String [4];
        ROOMS_COLUMNS[0] = "_id";
        ROOMS_COLUMNS[1] = "TEMPERATURE";
        ROOMS_COLUMNS[2] = "HUMIDITY";
        ROOMS_COLUMNS[3] = "ILLUMINATION";
    }
    public void create(SQLiteDatabase db) {
        String sql;
        sql = "create table " + TABLE_NAMES[1] + " (" + OUTDOOR_COLUMNS[0] + " integer primary key autoincrement,";
        sql += OUTDOOR_COLUMNS[1] + " real default null,";
        sql += OUTDOOR_COLUMNS[2] + " real default null,";
        sql += OUTDOOR_COLUMNS[3] + " real default null,";
        sql += OUTDOOR_COLUMNS[4] + " real default null);";
        db.execSQL(sql);
        sql = "create table " + TABLE_NAMES[2] + 1 + " (" + ROOMS_COLUMNS[0] + " integer primary key autoincrement,";
        sql += ROOMS_COLUMNS[1] + " real default null,";
        sql += ROOMS_COLUMNS[2] + " real default null,";
        sql += ROOMS_COLUMNS[3] + " real default null);";
        db.execSQL(sql);
        sql = "create table " + TABLE_NAMES[0] + " (_id integer primary key autoincrement, DATE date NOT NULL,TIME time NOT NULL, key_outdoor integer, key_room1 integer);";
        db.execSQL(sql);
    }

    public void setROOM_NUMBER(int roomNumber) {
        ROOM_NUMBER = roomNumber;
        BASE_COLUMNS = new String [4 + ROOM_NUMBER];
        BASE_COLUMNS [0] = "_id";
        BASE_COLUMNS [1] = "DATE";
        BASE_COLUMNS [2] = "TIME";
        BASE_COLUMNS [3] = "key_outdoor";
        for(int i = 1; i <= ROOM_NUMBER; i++)
            BASE_COLUMNS [3 + i] = "key_room" + i;
    }
    public int getROOM_NUMBER(){
        return ROOM_NUMBER;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(int i = 0; i < ROOM_NUMBER; i++)
            db.execSQL("drop table if exists room"+(i+1));
        db.execSQL("drop table if exists outdoor");
        db.execSQL("drop table if exists base");
        onCreate(db);
    }

}
