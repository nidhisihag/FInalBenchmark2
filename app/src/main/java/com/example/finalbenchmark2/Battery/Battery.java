package com.example.finalbenchmark2.Battery;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalbenchmark2.R;
import com.example.finalbenchmark2.WriteToFile;

import java.text.DecimalFormat;

public class Battery extends AppCompatActivity {
    private Context mContext;
    private TextView TempTextView;
    public Long previousTimeStamp = System.currentTimeMillis();
    public int prevBatLevel=-1;
    public Long disChargeTimePerUnit=50000L;
    public  Long AvgdisChargeTimePerUnit=500000L;
    public Long prevDisTimePerUnit=50000L;
    public int Batlev[] = new int[100];
    public Long timeToGet15 = 0L;
    // Initialize a new BroadcastReceiver instance
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent){

            Toast.makeText(mContext,"Received",Toast.LENGTH_SHORT).show();
            Long currentTimeStamp = System.currentTimeMillis();
            final BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
            int batLevel1 = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            float temperature = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
            double volt = voltage * 0.001;
            DecimalFormat newFormat = new DecimalFormat("#.#");
            double oneDecimalVolt = Double.valueOf(newFormat.format(volt));
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            String chargeState="";
            if(isCharging){
                chargeState = "Charging";

            }
            else {if(prevBatLevel !=-1){
                if(prevBatLevel != batLevel1){
                    int dischargePer = prevBatLevel - batLevel1;
                    Long tToDischarex=currentTimeStamp-previousTimeStamp;
                    disChargeTimePerUnit = tToDischarex / dischargePer;
                    AvgdisChargeTimePerUnit = (disChargeTimePerUnit+prevDisTimePerUnit)/2;
                    prevDisTimePerUnit = disChargeTimePerUnit;
                    previousTimeStamp = currentTimeStamp;

                    prevBatLevel = batLevel1;
                }
            }
            else {
                prevBatLevel = batLevel1;
            }
                chargeState = "discharging";
            }
            if(batLevel1 > 15) {
                timeToGet15 = (AvgdisChargeTimePerUnit * (batLevel1 - 15))/1000;

            }

            Log.i("previous batt",Integer.toString(prevBatLevel));
            Log.i("current batt level",Integer.toString(batLevel1));
           /* Long avgCurrent = null, currentNow = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                avgCurrent = bm.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);
                currentNow = bm.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
            }
            Log.d("batbat", "BATTERY_PROPERTY_CURRENT_AVERAGE = " + avgCurrent + "mAh");
            Log.d("bbaatbbaat", "BATTERY_PROPERTY_CURRENT_NOW =  " + currentNow + "mAh");
            */
            String output = "Battery Temperature\n" + Float.toString(temperature) +" "+ (char) 0x00B0 +"C\n" + "BAttery Voltage" + "\nmillivolts : " + Integer.toString(voltage) +"\nBattery Level: "+ Integer.toString(batLevel1)+"%\n"+"Status : "+chargeState + "\ndischarging speed: "+"1% per "+Long.toString(AvgdisChargeTimePerUnit)+" milliseconds\n"+"time to hit 15%: "+Long.toString(timeToGet15)+" seconds";


            // Display the temperature in TextView
            TempTextView.setText( output );
            WriteToFile.write(output,"batteryLog.txt");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request window feature action bar
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);

        // Get the application context
        mContext = getApplicationContext();

        // Initialize a new IntentFilter instance
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        // Register the broadcast receiver
        mContext.registerReceiver(mBroadcastReceiver,iFilter);

        // Get the widgets reference from XML layout
        TempTextView = (TextView) findViewById(R.id.BatteryText);


    }
}
