package com.shometeam.ao.sservergui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import AI.Layer;
import ArduinoTelnet.Arduino;
import SensorDB.SensorDataBase;
import connectors.DropboxConnector;
import connectors.LogUtil;
import connectors.WeatherConnector;

public class MainActivity extends AppCompatActivity {

    private LogUtil mLogger;
    private Arduino mArduino;
    private SensorDataBase mDataBase;
    private DropboxConnector mDBoxConnector;
    private Layer mNeuralNetwork;
    private WeatherConnector mWeatherConnector;
    private TextView mTextLog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogger = new LogUtil();

        mLogger.addMessage(LogUtil.TAG.DATA_BASE,"Creating DB");
        try {
            mDataBase = new SensorDataBase(this);
            mDataBase.setRoomNumber(1);
            mLogger.addMessage(LogUtil.TAG.DATA_BASE, "DB was successfully created");
        } catch (Exception ex) {
            ex.printStackTrace();
            mLogger.addMessage(LogUtil.TAG.DATA_BASE, "Error, unable to create DB");
        }

        mLogger.addMessage(LogUtil.TAG.ARDUINO, "Creating Arduino class");
        mArduino = new Arduino("chernatsky.la.net.ua",23, mDataBase);
        mLogger.addMessage(LogUtil.TAG.ARDUINO, "Starting connection to Arduino with lifecycle=2000ms");
        mArduino.start(2000);
        mLogger.addMessage(LogUtil.TAG.ARDUINO, "Connection was established");

        //начнем коннект к Dropbox
        mLogger.addMessage(LogUtil.TAG.DROPBOX, "Connecting to Dropbox");
        mDBoxConnector = new DropboxConnector();
        mLogger.addMessage(LogUtil.TAG.DROPBOX, "Starting authentification");
        mDBoxConnector.startAuthentification(this);

        mLogger.addMessage(LogUtil.TAG.WEATHER_SERVICE, "Connecting to open weather map");
        mWeatherConnector = new WeatherConnector("Kiev");
        mLogger.addMessage(LogUtil.TAG.WEATHER_SERVICE,"Connection was established");

        mLogger.addMessage(LogUtil.TAG.NEURAL_NETWORK,"Creating neural network");
        mNeuralNetwork = new Layer("Temperature","Energy usage",0,1000,50,10000,50,500);
        mLogger.addMessage(LogUtil.TAG.NEURAL_NETWORK, "Neural network was successfully created");

        setUpGUI();


        final Context appContext = this.getApplicationContext();

        new Thread(){
            @Override
            public void run() {
                super.run();
                while(!isInterrupted()){
                    mLogger.addMessage(LogUtil.TAG.NEURAL_NETWORK,"Adding new values to network");
                    try {
                        float[] tempValues;
                        mLogger.addMessage(LogUtil.TAG.WEATHER_SERVICE,"Getting weather information");
                        tempValues = mWeatherConnector.getWeatherForecastForWeek(WeatherConnector.MEASURE_UNIT.CELSIUS);
                        for(float currentTemp: tempValues){
                            mNeuralNetwork.addValueToGrid(currentTemp, 1000);
                        }

                        mLogger.addMessage(LogUtil.TAG.DROPBOX, "Saving DB to Dropbox");
                        String[] names = {"database.db"};
                        mDBoxConnector.upload(appContext, names, mDataBase.getBaseFile());

                        mLogger.addMessage(LogUtil.TAG.WEATHER_SERVICE, "Successfully");
                        mLogger.addMessage(LogUtil.TAG.NEURAL_NETWORK,"Successfully");
                        mLogger.addMessage(LogUtil.TAG.DROPBOX,"Successfully");
                    } catch (IOException | JSONException e) {
                        mLogger.addMessage(LogUtil.TAG.WEATHER_SERVICE,"Unsuccessfully");
                        mLogger.addMessage(LogUtil.TAG.NEURAL_NETWORK,"Unsuccessfully");
                        mLogger.addMessage(LogUtil.TAG.DROPBOX,"Unsuccessfully");
                        e.printStackTrace();
                    }
                    try {
                        sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void setUpGUI(){
        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mLogger.getArrayOfTags());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.main_spinner);
        spinner.setAdapter(adapter);

        mTextLog = (TextView) findViewById(R.id.main_log_text);

        // выделяем элемент
        spinner.setSelection(2);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                mTextLog.setText("");
                mTextLog.append("\n");
                for(String message: mLogger.getMessagesByTag(LogUtil.TAG.values()[position])){
                    mTextLog.append(message);
                }
                Toast.makeText(getBaseContext(), LogUtil.TAG.values()[position].toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mArduino.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDBoxConnector.finishAuthentification(this);
        mLogger.addMessage(LogUtil.TAG.DROPBOX, "Connection was established");
    }
}