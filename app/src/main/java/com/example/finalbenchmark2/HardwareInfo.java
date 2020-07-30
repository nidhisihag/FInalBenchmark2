package com.example.finalbenchmark2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
import com.example.finalbenchmark2.Camera.CameraInfoGenerator;
import com.example.finalbenchmark2.Sensor.SensorInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HardwareInfo extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String cameraInfo="";
    String sensorInfo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.listView);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Camera Info");
        listDataHeader.add("Sensor Info");
        Activity activity = HardwareInfo.this;
        cameraInfo= CameraInfoGenerator.getInfo();
        sensorInfo= SensorInfo.getInfo(activity);

        // Adding child data
        List<String> CameraInfo = new ArrayList<String>();
        CameraInfo.add(cameraInfo);

        List<String> SensorInfo = new ArrayList<String>();
        SensorInfo.add(sensorInfo);


        listDataChild.put(listDataHeader.get(0), CameraInfo); // Header, Child data
        listDataChild.put(listDataHeader.get(1), SensorInfo);

    }
}

        //dispInfo=findViewById(R.id.displayText);
        //dispInfo.setMovementMethod(new ScrollingMovementMethod());

        //hardwareInfo="***cameraInfo***\n\n"+cameraInfo+"\n***SensorInfo***\n\n"+sensorInfo;
        //dispInfo.setText(hardwareInfo);
