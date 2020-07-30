package com.example.finalbenchmark2;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class WriteToFile {
    static  public void write(String ad,String filename)
    {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        try{
            File gpxfile = new File(file, filename);
            FileOutputStream stream = new FileOutputStream(gpxfile,true);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.append(ad+"\n");
            writer.close();
            stream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
