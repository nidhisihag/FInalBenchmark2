package com.example.finalbenchmark2.Camera;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.hardware.Camera.Parameters;
import java.util.Locale;
import android.os.Build.VERSION;
import java.text.DecimalFormat;
import java.util.List;
import android.hardware.Camera.Size;
import java.util.List;

public class CameraInfoGenerator {
    public static String getCameraTitle(Camera.CameraInfo cameraInfo) {

        Log.i("camersFAcing",Integer.toString(cameraInfo.facing));
        if (cameraInfo.facing == 1) {
            return "Front CameraActivity";
        }
        return "Back CameraActivity";
    }
    public static String getInfo(){
        int i=0;
        String oo="";
        android.hardware.Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
        while(i< android.hardware.Camera.getNumberOfCameras()){




            android.hardware.Camera.getCameraInfo(i,cameraInfo);
            Log.i("title",CameraInfoGenerator.getCameraTitle(cameraInfo));
            android.hardware.Camera camera = CameraUtil.initializeCamera(i);
            Parameters parameters = camera.getParameters();
            String s= Integer.toString(i);
            if(camera!=null){
                String out = CameraInfoGenerator.getCameraMegapixels(camera);
                Log.i("Megapixel",out + ' '+Integer.toString(i));
                oo+="\nMegapixel: "+out+" "+s+"\n";
                oo+=CameraInfoGenerator.addCameraSummary(camera,i);
                String orientation = new StringBuilder(String.valueOf(cameraInfo.orientation)).append(" (*)").toString();
                Log.i("Orientation",orientation+' '+Integer.toString(i));
                oo+="\nOrientation: "+orientation+" "+s+"\n";
                java.util.List<android.util.Size> list=CameraUtil.getSupportedPictureSizes(parameters);
                Log.i("Supported Picture List",list.toString()+' '+Integer.toString(i));
                oo+="\nSupported PictureSize: "+list.toString()+" "+s+"\n";
                Boolean b = CameraUtil.isZoomSupported(camera);
                Log.i("Zoom Supported",b.toString()+' '+Integer.toString(i));
                oo+="\nZoom Suported: "+b.toString()+" "+s+"\n";
                java.util.List<android.util.Size> list1 = CameraUtil.getSupportedPreviewSizes(parameters);
                Log.i("Supported Preview List",list1.toString()+' '+Integer.toString(i));
                oo+=addCameraParameters(camera,i);


            }
            i++;
            camera.release();//most important line
        }
        return oo;

    }
    public static String getCameraMegapixels(Camera camera) {
        if (camera != null) {
            try {
                List<Camera.Size> sizes = camera.getParameters().getSupportedPictureSizes();
                DecimalFormat df = new DecimalFormat("#.#");
                int maxPixels = 0;
                for (Camera.Size size : sizes) {
                    if (size.width * size.height > maxPixels) {
                        maxPixels = size.width * size.height;
                    }
                }
                return df.format(((double) maxPixels) / 1000000.0d);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return "Unknown";
    }
    public static String addCameraSummary(Camera camera,int i){
        Parameters params = camera.getParameters();
        String Summary="";
        String s= ' '+Integer.toString(i);
        List<Size> supportedPictureSizes = params.getSupportedPictureSizes();
        if (supportedPictureSizes != null && supportedPictureSizes.size() > 0) {
            Size maxSize = (Size) supportedPictureSizes.get(0);
            int maxSizePixels = maxSize.width * maxSize.height;
            for (int j = 1; j < supportedPictureSizes.size(); j++) {
                Size size = (Size) supportedPictureSizes.get(j);
                int pixels = size.width * size.height;
                if (pixels > maxSizePixels) {
                    maxSizePixels = pixels;
                    maxSize = size;
                }
            }
            String text = maxSize.width + "x" + maxSize.height;
            if (maxSize.width * 3 == maxSize.height * 4) {
                text = new StringBuilder(String.valueOf(text)).append(" (4:3)").toString()+' '+Integer.toString(i);

            } else {
                if (maxSize.width * 9 == maxSize.height * 16) {
                    text = new StringBuilder(String.valueOf(text)).append(" (16:9)").toString()+' '+Integer.toString(i);
                }
            }
            Log.i("Max Picture Size",text);
            Summary+="\nMax Picture Size: "+text+"\n";
        }
        String maxZoom = "Not supported";
        if (VERSION.SDK_INT >= 8) {
            try {
                if (params.isZoomSupported()) {
                    int maxZoomIndex = params.getMaxZoom();
                    List<Integer> ratios = params.getZoomRatios();
                    Log.i("Zoom Ratios",ratios.toString()+' '+Integer.toString(i));
                    Summary+="\nZoom Ratio: "+ratios.toString()+' '+Integer.toString(i);
                    if (ratios != null && ratios.size() > maxZoomIndex) {
                        int ratio = ((Integer) ratios.get(maxZoomIndex)).intValue();
                        if (ratio % 100 == 0) {
                            maxZoom = (ratio / 100) + "x";

                        } else {
                            maxZoom = String.format(Locale.getDefault(), "%.2fx", new Object[]{Float.valueOf(((float) ratio) * 0.01f)});

                        }
                    }
                    Log.i("zoom",maxZoom + s);
                    Summary+="\nzoom: "+maxZoom+' '+s+"\n";
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        String autofocus = "Not supported";
        if (VERSION.SDK_INT >= 8) {
            List<String> focusModes = params.getSupportedFocusModes();
            Log.i("Focus Modes",focusModes.toString()+' '+Integer.toString(i));
            Summary+="\nFocus Mode: "+focusModes.toString()+' '+Integer.toString(i)+'\n';
            if (focusModes != null && focusModes.size() > 0) {
                if (focusModes.contains("continuous-picture")) {
                    autofocus = "Supported (Continuous)";

                } else if (focusModes.contains("auto")) {
                    autofocus = "Supported";

                }
            }
            Log.i("focus",autofocus + s);
            Summary+="\nfocus: "+autofocus+' '+s+'\n';
        }
        return Summary;
    }
    public static String addCameraParameters(Camera camera,int i) {
        String s= ' '+Integer.toString(i);
        String output="";
        if (camera != null) {
            try {
                String parameterString = camera.getParameters().flatten();
                int parameterStart = 0;
                while (parameterStart != -1) {
                    String parameterValues;
                    int nameEnd = parameterString.indexOf(61, parameterStart);
                    String parameterName = parameterString.substring(parameterStart, nameEnd);
                    int valuesEnd = parameterString.indexOf(59, parameterStart);
                    if (valuesEnd != -1) {
                        parameterValues = parameterString.substring(nameEnd + 1, valuesEnd);
                        parameterStart = valuesEnd + 1;
                    } else {
                        parameterValues = parameterString.substring(nameEnd + 1);
                        parameterStart = -1;
                    }
                    parameterValues = parameterValues.replaceAll(",", ", ");

                    String lowerParam = parameterName.toLowerCase(Locale.ENGLISH);
                    if (lowerParam.contains("picture")) {
                        Log.i("picture",parameterValues +" "+parameterName +s);
                        output+="\npicture: "+parameterName+" "+parameterValues+s+"\n";
                    } else if (lowerParam.contains("video")) {
                        Log.i("video",parameterValues +" "+parameterName +s);
                        output+="\nvideo: "+parameterName+" "+parameterValues+s+"\n";
                    } else if (lowerParam.contains("preview")) {
                        Log.i("preview",parameterValues +" " +parameterName +s);
                        output+="\npreview: "+parameterName+" "+parameterValues+s+"\n";
                    } else if (lowerParam.contains("zoom")) {
                        Log.i("zoom",parameterValues +" "+parameterName +s);
                        output+="\nzoom: "+parameterName+" "+parameterValues+s+"\n";
                    } else if (lowerParam.contains("focus")) {
                        Log.i("focus",parameterValues +" "+parameterName +s);
                        output+="\nfocus: "+parameterName+" "+parameterValues+s+"\n";
                    } else if (lowerParam.contains("flash")) {
                        Log.i("flash",parameterValues +" "+parameterName +s);
                        output+="\nflash: "+parameterName+" "+parameterValues+s+"\n";
                    } else if (lowerParam.contains("whitebalance")) {
                        Log.i("whitebalance",parameterValues +" "+parameterName +s);
                        output+="\nwhitebalance: "+parameterName+" "+parameterValues+s+"\n";
                    } else if (lowerParam.contains("exposure-compensation")) {
                        Log.i("exposure-Compensation",parameterValues +" "+parameterName + s);
                        output+="\nexposure-Compensation: "+parameterName+" "+parameterValues+s+"\n";
                    } else if (lowerParam.contains("scene")) {
                        Log.i("scene",parameterValues +" "+parameterName +s);
                        output+="\nscene: "+parameterName+" "+parameterValues+s+"\n";
                    } else {
                        Log.i("misc",parameterValues +" "+parameterName +s);
                        output+="\nmisc: "+parameterName+" "+parameterValues+s+"\n";
                    }
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return output;
    }
}