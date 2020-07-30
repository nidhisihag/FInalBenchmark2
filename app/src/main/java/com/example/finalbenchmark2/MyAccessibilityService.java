package com.example.finalbenchmark2;

import android.accessibilityservice.AccessibilityService;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;
import com.example.finalbenchmark2.Processor.ProcessorTest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyAccessibilityService extends AccessibilityService {


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!TextUtils.isEmpty(event.getPackageName()))
        {
            if(event.getPackageName().toString().contains("uber")){

                ProcessorTest.tm=System.currentTimeMillis();
                ProcessorTest.flag=false;
                Log.i("time to launch uber:",Long.toString(ProcessorTest.tm));

            }
            Log.i(getTime(),event.getPackageName().toString());
           // write(event.getPackageName().toString() + " --> " +getTime(),"screen.txt");
        }

    }

    public String getTime()
    {
        try{
            return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        }catch (Exception e){return  "none";}
    }

    @Override
    public void onInterrupt() {

    }


    public static   void write(String data,String filename)
    {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        try{
            File gpxfile = new File(file, filename);
            FileOutputStream stream = new FileOutputStream(gpxfile,true);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.append(data+"\n");
            writer.close();
            stream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
