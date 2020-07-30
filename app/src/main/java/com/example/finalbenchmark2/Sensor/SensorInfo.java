package com.example.finalbenchmark2.Sensor;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.finalbenchmark2.Status;

import java.util.List;
import java.util.Locale;

public class SensorInfo {

    public static String getInfo(Activity activity){
        List<Sensor> listsensor;
        float value=0;
        SensorManager sensorManager ;
        String displayInfo="SenesorInfo: Display: ";
        String harwareInfo="";
        DisplayMetrics displayMetrics;
        int deviceWidth,deviceHeight,height,width = 0;
        displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        value = activity.getResources().getDisplayMetrics().density;
        sensorManager = (SensorManager)activity.getSystemService(Context.SENSOR_SERVICE);
        listsensor = sensorManager.getSensorList(Sensor.TYPE_ALL);
        Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        float refreshRating = display.getRefreshRate();
        Log.i("refreshRate",Float.toString(refreshRating)); // in Hz
        displayInfo+="\nRefreshrate: "+Float.toString(refreshRating)+" in Hz\n";
        value = activity.getResources().getConfiguration().orientation;
        Log.i("orientation",Float.toString(value)); //value=1:Portrait value=2:Landscape
        displayInfo+="\nOrientation: "+Float.toString(value)+" 1=potrait 2=Landscape\n";

        Point size = new Point();
        display.getSize(size);
        Log.i("widthANd Height",Integer.toString(size.x)+" "+Integer.toString(size.y));
        displayInfo+="\nWidth: "+ Integer.toString(size.x)+"\nHeight: "+Integer.toString(size.y)+"\n";
        double x = Math.pow(displayMetrics.widthPixels / displayMetrics.xdpi, 2);
        double y = Math.pow(displayMetrics.heightPixels / displayMetrics.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Float LCD_Diagonal = (float)screenInches;
        float PPI = (float)Math.sqrt((double)(size.x*size.x + size.y*size.y))/LCD_Diagonal;
        for(int i=0; i<listsensor.size(); i++){
            Log.i("SensorInfoActivity",generateSensorInfo(activity,listsensor.get(i)));
            harwareInfo+="\nSensor Info: "+generateSensorInfo(activity,listsensor.get(i))+"\n";
        }
        Log.i("xxxx", "display_page_screen_ppi: " + String.format(Locale.getDefault(),"%d %s", Math.round(PPI), "ppi"));
        displayInfo+="\ndisplay page screen ppi"+String.format(Locale.getDefault(),"%d %s",Math.round(PPI),"ppi")+"\n";
        harwareInfo=displayInfo+"\nNumber of Sensors: "+Integer.toString(listsensor.size())+"\n"+harwareInfo;

        return harwareInfo;

    }

    public  static String generateSensorInfo(Activity activity, Sensor sensor) {
        int type = sensor.getType();
        float range=0,resolution=0;
        String vendor = sensor.getVendor();
        String delay="";
        if (isResolutionRelevant(type)) {
            range = sensor.getMaximumRange();
            resolution = sensor.getResolution();
        }
        float power = sensor.getPower();
        if (Build.VERSION.SDK_INT >= 9) {
            int minDelay = sensor.getMinDelay();
            if (minDelay > 0) {
                delay = String.valueOf(minDelay);
            } else {
                delay= "On Trigger";
            }
        }

        return "sensorName:"+sensor.getName()+" ,vendor:"+vendor+" ,range:"+Float.toString(range)+" ,delay(us):"+delay+" ,resolution:"+Float.toString(resolution);
    }

    private static boolean isResolutionRelevant(int sensorType) {
        switch (sensorType) {
            case 17:
            case 18:
            case 19:
                return false;
            default:
                return true;
        }
    }
    public static String  getBuildInfo(Context context)
    {
        StringBuilder builder = new StringBuilder("Build details ->\n");
        try{
            builder.append("BOARD : ");
            builder.append(Build.BOARD);
            builder.append("\n");

            builder.append("BOOTLOADER : ");
            builder.append(Build.BOOTLOADER);
            builder.append("\n");

            builder.append("BRAND : ");
            builder.append(Build.BRAND);
            builder.append("\n");

            builder.append("DEVICE : ");
            builder.append(Build.DEVICE);
            builder.append("\n");

            builder.append("DISPLAY : ");
            builder.append(Build.DISPLAY);
            builder.append("\n");

            builder.append("FINGERPRINT : ");
            builder.append(Build.FINGERPRINT);
            builder.append("\n");

            builder.append("HARDWARE : ");
            builder.append(Build.HARDWARE);
            builder.append("\n");

            builder.append("HOST : ");
            builder.append(Build.HOST);
            builder.append("\n");

            builder.append("ID : ");
            builder.append(Build.ID);
            builder.append("\n");

            builder.append("MANUFACTURER : ");
            builder.append(Build.MANUFACTURER);
            builder.append("\n");

            builder.append("MODEL : ");
            builder.append(Build.MODEL);
            builder.append("\n");

            builder.append("PRODUCT : ");
            builder.append(Build.PRODUCT);
            builder.append("\n");

            builder.append("RADIO : ");
            if (!Build.getRadioVersion().equals(""))
                builder.append(Build.getRadioVersion());
            builder.append("\n");


            builder.append("SUPPORTED_32_BIT_ABIS : ");
            for(String a:Build.SUPPORTED_32_BIT_ABIS)
                builder.append(a + " ,");
            builder.append("\n");

            builder.append("SUPPORTED_64_BIT_ABIS : ");
            for(String a:Build.SUPPORTED_64_BIT_ABIS)
                builder.append(a + " ,");
            builder.append("\n");

            builder.append("SUPPORTED_ABIS : ");
            for(String a:Build.SUPPORTED_ABIS)
                builder.append(a + " ,");
            builder.append("\n");

            builder.append("TAGS : ");
            builder.append(Build.TAGS);
            builder.append("\n");

            builder.append("TYPE : ");
            builder.append(Build.TYPE);
            builder.append("\n");

            builder.append("USER : ");
            builder.append(Build.USER);
            builder.append("\n");

            builder.append("CODENAME : ");
            builder.append(Build.VERSION.CODENAME);
            builder.append("\n");

            builder.append("INCREMENTAL : ");
            builder.append(Build.VERSION.INCREMENTAL);
            builder.append("\n");

            builder.append("PREVIEW_SDK_INT : ");
            builder.append(Build.VERSION.PREVIEW_SDK_INT);
            builder.append("\n");

            builder.append("RELEASE : ");
            builder.append(Build.VERSION.RELEASE);
            builder.append("\n");


            builder.append("SDK_INT : ");
            builder.append(Build.VERSION.SDK_INT);
            builder.append("\n");

            builder.append("SECURITY_PATCH : ");
            builder.append(Build.VERSION.SECURITY_PATCH);
            builder.append("\n");

            builder.append("BASE_OS : ");
            builder.append(Build.VERSION.BASE_OS);
            builder.append("\n");

            return builder.toString();
        }catch (Exception e){
            builder.append("\n");
            builder.append(e.toString());
            return builder.toString();}
    }
    public  boolean checkPermission(Context context, String permission)
    {
        int result = ContextCompat.checkSelfPermission(context, permission);
        return  result == PackageManager.PERMISSION_GRANTED;
    }

}
