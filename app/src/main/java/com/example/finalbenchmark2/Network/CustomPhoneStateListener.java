package com.example.finalbenchmark2.Network;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
public class CustomPhoneStateListener extends PhoneStateListener {
    Context mContext;
    public static String LOG_TAG = "CustomPhoneStateListener";

    public CustomPhoneStateListener() {

    }
    @Override
    public void onDataConnectionStateChanged(int state, int networkType) {
        super.onDataConnectionStateChanged(state, networkType);
        switch (state) {
            case TelephonyManager.DATA_DISCONNECTED:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: DATA_DISCONNECTED");
                break;
            case TelephonyManager.DATA_CONNECTING:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: DATA_CONNECTING");
                break;
            case TelephonyManager.DATA_CONNECTED:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: DATA_CONNECTED");
                break;
            case TelephonyManager.DATA_SUSPENDED:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: DATA_SUSPENDED");
                break;
            default:
                Log.w(LOG_TAG, "onDataConnectionStateChanged: UNKNOWN " + state);
                break;
        }

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_CDMA:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: NETWORK_TYPE_CDMA");
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: NETWORK_TYPE_EDGE");
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: NETWORK_TYPE_EVDO_0");
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: NETWORK_TYPE_GPRS");
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: NETWORK_TYPE_HSDPA");
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: NETWORK_TYPE_HSPA");
                SimStrength.idle=true;
               SimStrength.bend=true;
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: NETWORK_TYPE_IDEN");
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: NETWORK_TYPE_LTE");
                if(SimStrength.idle & SimStrength.ringing){

                   //SimStrength.b=System.currentTimeMillis();
                    Log.i("End time:",Long.toString(System.currentTimeMillis()));
                    SimStrength.bflag=true;
                    //Log.i("time taken to change from 3G to 4G:",Long.toString(SimStrength.b-SimStrength.a));

                }
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: NETWORK_TYPE_UMTS");
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                Log.i(LOG_TAG, "onDataConnectionStateChanged: NETWORK_TYPE_UNKNOWN");
                break;
            default:
                Log.w(LOG_TAG, "onDataConnectionStateChanged: Undefined Network: "
                        + networkType);
                break;
        }
    }
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_IDLE");
                if(SimStrength.ringing & SimStrength.bend){
                    //SimStrength.a=System.currentTimeMillis();
                    Log.i("Call ended:",Long.toString(System.currentTimeMillis()));
                    SimStrength.bend=false;
                }
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_RINGING");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_OFFHOOK");
                SimStrength.ringing=true;
                Log.i("Calling Starts:",Long.toString(System.currentTimeMillis()));
                break;
            default:
                Log.i(LOG_TAG, "UNKNOWN_STATE: " + state);
                break;
        }
    }
}

