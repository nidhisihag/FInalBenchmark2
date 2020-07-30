package com.example.finalbenchmark2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.finalbenchmark2.Battery.Battery;
import com.example.finalbenchmark2.Camera.CameraInfoGenerator;
import com.example.finalbenchmark2.Camera.TestCamera;
import com.example.finalbenchmark2.Processor.ProcessorTest;
import com.example.finalbenchmark2.Sensor.SensorInfo;

public class Status extends Activity {

    String cameraInfo="",sensorInfo="",hardwareInfo="";
    ImageButton processorTest;
    ImageView cameraTest,tick;
    ImageButton battery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setActivityBackgroundColor(0xC6C3C3);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        cameraTest=findViewById(R.id.startCameraTestimg);
        battery=findViewById(R.id.startbattery);
        tick=findViewById(R.id.tick);
        battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mi=new Intent(Status.this, Battery.class);
                startActivity(mi);
            }
        });
        cameraTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(Status.this,
                        TestCamera.class);
                startActivity(myIntent);

            }
        });
        processorTest=findViewById(R.id.startProcessorTest);
        processorTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Status.this, ProcessorTest.class);
                startActivity(intent);
            }
        });

        Activity activity=Status.this;



        cameraInfo= CameraInfoGenerator.getInfo();
        sensorInfo= SensorInfo.getInfo(activity);

        hardwareInfo="***cameraInfo***\n\n"+cameraInfo+"\n***SensorInfo***\n\n"+sensorInfo;
        WriteToFile.write(hardwareInfo,"hardwareInfo.txt");


}
    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("camera_score",false))
            ((ImageView)findViewById(R.id.tick)).setVisibility(View.VISIBLE);
    }
}
