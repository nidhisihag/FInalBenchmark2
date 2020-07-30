package com.example.finalbenchmark2.Camera;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalbenchmark2.R;
import com.example.finalbenchmark2.Status;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestCamera extends AppCompatActivity {
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;

   static  String imagepath="";
    Uri imageUri = null;
    static String res="";
    static TextView imageDetails      = null;
    static TextView response=null;
    public  static ImageView showImg  = null;
    TestCamera CameraActivity = null;
     Button photo;
    Button upload;
    //ImageView tick=(ImageView) findViewById(R.id.tick);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        CameraActivity = this;

        imageDetails = (TextView) findViewById(R.id.imageDetails);

        showImg = (ImageView) findViewById(R.id.showImg);

        photo = (Button) findViewById(R.id.photo);
         upload = (Button) findViewById(R.id.upload);



        photo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /*************************** Camera Intent Start ************************/

                // Define the file-name to save photo taken by Camera activity

                String fileName = "Camera_Example.jpg";

                // Create parameters for Intent with filename

                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.TITLE, fileName);

                values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");

                // imageUri is the current activity attribute, define and save it for later usage

                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                /**** EXTERNAL_CONTENT_URI : style URI for the "primary" external storage volume. ****/


                // Standard Intent action that can be sent to have the camera
                // application capture an image and return it.

                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                startActivityForResult( intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


                /*************************** Camera Intent End ************************/


            }

        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadFile(imagepath);
                response=findViewById(R.id.Uploadresponse);

            }
        });

    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if ( resultCode == RESULT_OK) {

                /*********** Load Captured Image And Data Start ****************/

                String imageId = convertImageUriToFile( imageUri,CameraActivity);


                //  Create and excecute AsyncTask to load capture image

                new LoadImagesFromSDCard().execute(""+imageId);

                /*********** Load Captured Image And Data End ****************/


            } else if ( resultCode == RESULT_CANCELED) {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /************ Convert Image Uri path to physical path **************/

    public static String convertImageUriToFile ( Uri imageUri, Activity activity )  {

        Cursor cursor = null;
        int imageID = 0;

        try {

            /*********** Which columns values want to get *******/
            String [] proj={
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Thumbnails._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };

            cursor = activity.managedQuery(

                    imageUri,         //  Get data for specific image URI
                    proj,             //  Which columns to return
                    null,             //  WHERE clause; which rows to return (all rows)
                    null,             //  WHERE clause selection arguments (none)
                    null              //  Order-by clause (ascending by name)

            );

            //  Get Query Data

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            //int orientation_ColumnIndex = cursor.
            //    getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

            int size = cursor.getCount();

            /*******  If size is 0, there are no images on the SD Card. *****/

            if (size == 0) {


                imageDetails.setText("No Image");
            }
            else
            {

                int thumbID = 0;
                if (cursor.moveToFirst()) {

                    /**************** Captured image details ************/

                    /*****  Used to show image on view in LoadImagesFromSDCard class ******/
                    imageID     = cursor.getInt(columnIndex);

                    thumbID     = cursor.getInt(columnIndexThumb);

                    String Path = cursor.getString(file_ColumnIndex);

                    //String orientation =  cursor.getString(orientation_ColumnIndex);

                    String CapturedImageDetails = " CapturedImageDetails : \n\n"
                            +" ImageID :"+imageID+"\n"
                            +" ThumbID :"+thumbID+"\n"
                            +" Path :"+Path+"\n";
                    imagepath=Path;

                    // Show Captured Image detail on activity
                    imageDetails.setText( CapturedImageDetails );

                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Return Captured Image ImageID ( By this ImageID Image will load from sdcard )

        return ""+imageID;
    }


    /**
     * Async task for loading the images from the SD card.
     *
     * @author Android Example
     *
     */

    // Class with extends AsyncTask class

    public class LoadImagesFromSDCard  extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(TestCamera.this);

        Bitmap mBitmap;

        protected void onPreExecute() {
            /****** NOTE: You can call UI Element here. *****/

            // Progress Dialog
            Dialog.setMessage(" Loading image from Sdcard..");
            Dialog.show();
        }


        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            Uri uri = null;


            try {

                /**  Uri.withAppendedPath Method Description
                 * Parameters
                 *    baseUri  Uri to append path segment to
                 *    pathSegment  encoded path segment to append
                 * Returns
                 *    a new Uri based on baseUri with the given segment appended to the path
                 */

                uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + urls[0]);

                /**************  Decode an input stream into a bitmap. *********/
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                if (bitmap != null) {

                    /********* Creates a new bitmap, scaled from an existing bitmap. ***********/

                    newBitmap = Bitmap.createScaledBitmap(bitmap, 170, 170, true);

                    bitmap.recycle();

                    if (newBitmap != null) {

                        mBitmap = newBitmap;
                    }
                }
            } catch (IOException e) {
                // Error fetching image, try to recover

                /********* Cancel execution of this task. **********/
                cancel(true);
            }

            return null;
        }


        protected void onPostExecute(Void unused) {

            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if(mBitmap != null)
            {
                // Set Image to ImageView

                showImg.setImageBitmap(mBitmap);
                upload.setVisibility(View.VISIBLE);
            }

        }

    }

    public void UploadFile(final String imagePath){


        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                try
                {
                    FileInputStream fstrm = new FileInputStream(imagePath);

                    // Set your server page url (and the file title/description)
                    ImageUpload hfu = new ImageUpload("http://101.53.139.11:7891/uploader", "my file title","my file description");

                    res= hfu.Send_Now(fstrm);

                }catch (Exception e){e.printStackTrace();}
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                response.setText(res);
                if(res.contains("Score"))
                {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TestCamera.this);
                    preferences.edit().putBoolean("camera_score",true).apply();
                }



            }
        }.execute();



    }
}