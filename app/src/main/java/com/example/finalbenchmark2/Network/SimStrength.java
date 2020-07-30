package com.example.finalbenchmark2.Network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalbenchmark2.R;

import java.util.List;

public class SimStrength extends AppCompatActivity {
    Button simStrength;
    Button callInfo;
    Button stateChange;
    TextView txtSignal;
    String s = "";
    TelephonyManager tManager;
    Context context;
    public static boolean ringing = false;
    public static boolean idle=false;
    public static boolean bflag=false;
    public static boolean bend=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        txtSignal = (TextView) findViewById(R.id.printStrength);
        simStrength = findViewById(R.id.signalStrength);
        stateChange=findViewById(R.id.statechange);
        callInfo = findViewById(R.id.callinfo);
        stateChange.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                tManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!bflag)
                        stateChange.performClick();
                    }
                },100);
                /*Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:9650443369"));
                startActivity(callIntent);*/

                tManager.listen(new CustomPhoneStateListener(),
                        PhoneStateListener.LISTEN_CALL_STATE|PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);


            }
        });

        simStrength.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                s = getSignalInfo();
                txtSignal.setText("Signal Strength:" + s);
                txtSignal.setVisibility(View.VISIBLE);


            }
        });
        callInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                s = getSignalInfo();
                txtSignal.setText("Signal Strength:" + s);
                txtSignal.setVisibility(View.VISIBLE);


            }
        });

    }


    public String getSignalInfo() {
        String strength = "";
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();   //This will give info of all sims present inside your mobile
            if (cellInfos != null) {
                for (int i = 0; i < cellInfos.size(); i++) {
                    Log.i("total", Integer.toString(cellInfos.size()));
                    if (cellInfos.get(i).isRegistered()) {
                        if (cellInfos.get(i) instanceof CellInfoWcdma) {
                            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) telephonyManager.getAllCellInfo().get(0);
                            CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                            strength += "cdma " + String.valueOf(cellSignalStrengthWcdma.getDbm()) + " dB";
                        } else if (cellInfos.get(i) instanceof CellInfoGsm) {
                            CellInfoGsm cellInfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
                            CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                            strength += " GSM" + String.valueOf(cellSignalStrengthGsm.getDbm()) + " dB";
                        } else if (cellInfos.get(i) instanceof CellInfoLte) {
                            CellInfoLte cellInfoLte = (CellInfoLte) telephonyManager.getAllCellInfo().get(0);
                            CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                            strength += " LTE" + String.valueOf(cellSignalStrengthLte.getDbm()) + " dB";
                        }
                    }
                    Log.i("sim strength", strength);
                }
                return strength;
            }


        }

        return strength;
    }
}