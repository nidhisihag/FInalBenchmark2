package com.example.finalbenchmark2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.finalbenchmark2.Network.SimStrength;

import eu.chainfire.libsuperuser.Shell;

public class MainActivity extends AppCompatActivity {
    Button startProcess;
    Button showResult;
    Button hardwareInfo;
    Button networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {

                Shell.SU.run("settings put secure enabled_accessibility_services com.example.finalbenchmark2/com.example.finalbenchmark2.MyAccessibilityService");
            }
        }).start();
        startProcess=findViewById(R.id.button);
        showResult=findViewById(R.id.button2);
        hardwareInfo=findViewById(R.id.button3);
        networkInfo=findViewById(R.id.button4);
        startProcess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class

                Intent myIntent = new Intent(MainActivity.this,
                        Status.class);
                startActivity(myIntent);
            }
        });
        hardwareInfo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent myIntent=new Intent(MainActivity.this,HardwareInfo.class);
                startActivity(myIntent);
            }
        });
    showResult.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,Result.class);
            startActivity(intent);
        }
    });
        networkInfo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent myIntent=new Intent(MainActivity.this, SimStrength.class);
                startActivity(myIntent);
            }
        });


    }

}
