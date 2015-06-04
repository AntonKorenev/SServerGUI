package com.shometeam.ao.sservergui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LogUtil mLogger;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogger = new LogUtil();
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.NEURAL_NETWORK,"GOT asdasdsasdasdaIT!");
        mLogger.addMessage(LogUtil.TAG.APP,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO, "GOT asdasddxzvcxb dfgb dIT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO, "GOT IT!");
        mLogger.addMessage(LogUtil.TAG.DATA_BASE, "GOT sdasdsadIT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.NEURAL_NETWORK,"GOT asdasdsasdasdaIT!");
        mLogger.addMessage(LogUtil.TAG.APP,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT asdasddxzvcxb dfgb dIT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.DATA_BASE,"GOT sdasdsadIT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.NEURAL_NETWORK,"GOT asdasdsasdasdaIT!");
        mLogger.addMessage(LogUtil.TAG.APP,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT asdasddxzvcxb dfgb dIT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.DATA_BASE,"GOT sdasdsadIT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.NEURAL_NETWORK,"GOT asdasdsasdasdaIT!");
        mLogger.addMessage(LogUtil.TAG.APP,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT asdasddxzvcxb dfgb dIT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.DATA_BASE,"GOT sdasdsadIT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.NEURAL_NETWORK,"GOT asdasdsasdasdaIT!");
        mLogger.addMessage(LogUtil.TAG.APP,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT asdasddxzvcxb dfgb dIT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.DATA_BASE,"GOT sdasdsadIT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.NEURAL_NETWORK,"GOT asdasdsasdasdaIT!");
        mLogger.addMessage(LogUtil.TAG.APP,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT asdasddxzvcxb dfgb dIT!");
        mLogger.addMessage(LogUtil.TAG.ARDUINO,"GOT IT!");
        mLogger.addMessage(LogUtil.TAG.DATA_BASE,"GOT sdasdsadIT!");



        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mLogger.getArrayOfTags());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.main_spinner);
        spinner.setAdapter(adapter);

        final TextView textLog = (TextView) findViewById(R.id.main_log_text);

        // выделяем элемент
        spinner.setSelection(2);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                textLog.setText("");
                textLog.append("\n");
                for(String message: mLogger.getMessagesByTag(LogUtil.TAG.values()[position])){
                    textLog.append(message);
                }
                Toast.makeText(getBaseContext(), LogUtil.TAG.values()[position].toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
}