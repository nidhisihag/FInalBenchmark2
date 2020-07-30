package com.example.finalbenchmark2.Processor;

import android.app.Activity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalbenchmark2.R;

import eu.chainfire.libsuperuser.Shell;

public class ProcessorTest extends Activity {
    private MultiSelectionSpinner spinner1;
    private Button btnSubmit;
    private Button installBtn;
    public static long tm,it;
    public static boolean flag;
    TextView t;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processor);

        addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
    }
    public void addItemsOnSpinner2() {

        //String listOfDefApp="com.android.chrome,com.google.android.gm,com.android.youtube,com.android.vending,com.android.contacts,com.google.android.videos,com.google.android.apps.docs,com.google.android.apps.maps";

        List<String> list=new ArrayList<String>();
        list=getInstalledApps(this);
        spinner1 = findViewById(R.id.spinner1);
        spinner1.setItems(list);
        spinner1.setSelection(list);
    }


    public void addListenerOnSpinnerItemSelection() {
        spinner1 = findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner1 = findViewById(R.id.spinner1);

        btnSubmit = findViewById(R.id.btnSubmit);
        installBtn=findViewById(R.id.btnInstall);
        installBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //time
                t=findViewById(R.id.btnInstall1);
                t.setVisibility(View.VISIBLE);

                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {

                        Date date= new Date();
                        tm = date.getTime();
                        it=tm;
                        it=System.currentTimeMillis();
                        Log.i("Now the time is:",Long.toString(it));
                        flag = true;

                        Shell.SU.run("pm install -g /storage/emulated/0/com.ubercab.apk");
                        Log.i("installedtime",Long.toString(tm));

                        Shell.SU.run("monkey -p com.ubercab 1");

                        while (flag)
                        {
                            try{
                                Thread.sleep(1000);
                            }catch (Exception e){}
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        t.setText("Installed time is "+String.valueOf(tm-it));
                        Shell.SU.run("pm uninstall com.ubercab");
                    }
                }.execute();



                //time


            }
        });
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String selecltedList = spinner1.getSelectedItemsAsString();
                Toast.makeText(ProcessorTest.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : " + selecltedList,
                        Toast.LENGTH_SHORT).show();
                String[] commaSeparatedArr = selecltedList.split("\\s*,\\s*");
                List<String> result = new ArrayList<String>(Arrays.asList(commaSeparatedArr));
                int l = result.size();

                for (int j = 0; j < l; j++) {
                    Shell.SU.run("monkey -p " + result.get(j) + " 1");
                }

            }
        });
    }
    public List<String> getInstalledApps(Context context)
    {
        final PackageManager pm = context.getPackageManager();
        List<String> list = new ArrayList<String>();
        // list.add("Installed Apps");
        for(ApplicationInfo info : pm.getInstalledApplications(0))
        {
            if (!TextUtils.isEmpty(info.loadLabel(pm)))
            {

                list.add(info.packageName);

            }
        }
        return list;
    }

}
//