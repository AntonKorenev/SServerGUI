/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectors;

import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.OpenWeatherMap;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by ao on 15.05.15.
 */
public class WeatherConnector {
    public static enum MEASURE_UNIT{CELSIUS, FAHRENHEIT}
    
    String mCityName;

    public WeatherConnector(String cityName){
        mCityName = cityName;
    }

    public float[] getWeatherForecastForDay(int dayNumber) throws IOException, JSONException { //in new Thread only 0-today
        float[] temperature = new float[4];
        OpenWeatherMap owm = new OpenWeatherMap("");
        DailyForecast today = owm.dailyForecastByCityName(mCityName, (byte)dayNumber);
        temperature[0]= today.getForecastInstance(dayNumber).getTemperatureInstance().getMorningTemperature();
        temperature[1]= today.getForecastInstance(dayNumber).getTemperatureInstance().getDayTemperature();
        temperature[2]= today.getForecastInstance(dayNumber).getTemperatureInstance().getEveningTemperature();
        temperature[3]= today.getForecastInstance(dayNumber).getTemperatureInstance().getNightTemperature();
        return temperature;
    }

    public float[] getWeatherForecastForWeek(MEASURE_UNIT unit) throws IOException, JSONException { //in new Thread only
        float[] temperature = new float[28];
        OpenWeatherMap owm = new OpenWeatherMap("");
        DailyForecast today = owm.dailyForecastByCityName(mCityName, (byte)7);
        for(int i=0; i<7; i++){
            temperature[i*4]= today.getForecastInstance(i).getTemperatureInstance().getMorningTemperature();
            temperature[i*4+1]= today.getForecastInstance(i).getTemperatureInstance().getDayTemperature();
            temperature[i*4+2]= today.getForecastInstance(i).getTemperatureInstance().getEveningTemperature();
            temperature[i*4+3]= today.getForecastInstance(i).getTemperatureInstance().getNightTemperature();
            System.out.println("Temp "+"Day"+String.valueOf(i+1));
            if(unit.equals(WeatherConnector.MEASURE_UNIT.FAHRENHEIT)){
                System.out.println("Temp\t"+String.valueOf(temperature[i*4]));
                System.out.println("Temp\t"+String.valueOf(temperature[i*4+1]));
                System.out.println("Temp\t"+String.valueOf(temperature[i*4+2]));
                System.out.println("Temp\t"+String.valueOf(temperature[i*4+3]));
            } else{
                System.out.println("Temp\t"+String.valueOf(convertToCelsius(temperature[i*4])) );
                System.out.println("Temp\t"+String.valueOf(convertToCelsius(temperature[i*4+1])) );
                System.out.println("Temp\t"+String.valueOf(convertToCelsius(temperature[i*4+2])) );
                System.out.println("Temp\t"+String.valueOf(convertToCelsius(temperature[i*4+3])) );
            }

        }
        return temperature;
    }
    
    public float convertToCelsius(float fahrenheitValue){
        return 5*(fahrenheitValue - 32)/9;
    }

}