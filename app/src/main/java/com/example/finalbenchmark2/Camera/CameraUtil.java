package com.example.finalbenchmark2.Camera;

import android.hardware.Camera;
import android.os.Build.VERSION;
import android.hardware.Camera.Parameters;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.util.Size;

public class CameraUtil {
    public static Camera initializeCamera(int index) {
        try {
            Camera camera;
            if (VERSION.SDK_INT >= 9) {
                camera = Camera.open(index);
            } else {
                Log.i("else","else");
                camera = Camera.open();

            }
            return camera;
        } catch (Throwable exception) {
            exception.printStackTrace();
            return null;
        }
    }
    public static boolean isZoomSupported(Camera camera) {
        if (camera == null) {
            return false;
        }
        Parameters parameters = camera.getParameters();
        if (parameters == null) {
            return false;
        }
        String zoomString = parameters.get("zoom-supported");
        if (zoomString == null || zoomString.compareTo("true") != 0) {
            return false;
        }
        return true;
    }
    public static int getMaxZoom(Camera camera) {
        if (camera != null) {
            try {
                Parameters parameters = camera.getParameters();
                if (parameters != null) {
                    String zoomString = parameters.get("max-zoom");
                    if (zoomString != null) {
                        return Integer.valueOf(zoomString).intValue();
                    }
                }
            } catch (Exception e) {
            }
        }
        return 1;
    }

    public static List<Size> getSupportedPictureSizes(Parameters parameters) {
        List<Size> list = getSupportedSizes("picture-size-provider_paths", parameters);
        list.add(new Size(parameters.getPictureSize().width, parameters.getPictureSize().height));
        return list;
    }
    public static List<Size> getSupportedPreviewSizes(Parameters parameters) {
        List<Size> list = getSupportedSizes("preview-size-provider_paths", parameters);
        list.add(new Size(parameters.getPreviewSize().width, parameters.getPreviewSize().height));
        return list;
    }
    public static List<Size> getSupportedSizes(String key, Parameters parameters) {
        List<Size> list = new ArrayList();
        try {
            String values = parameters.get(key);
            if (values != null) {
                String[] sizeTokens = values.split(",");
                for (String trim : sizeTokens) {
                    String[] dimensionTokens = trim.trim().split("x");
                    if (dimensionTokens.length == 2) {
                        Size size = new Size(Integer.valueOf(dimensionTokens[0].trim()).intValue(), Integer.valueOf(dimensionTokens[1].trim()).intValue());
                        list.add(size);
                        Log.i("size detected", size.toString());
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return list;
    }

}