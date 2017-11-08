package com.example.nath.take3app.activity.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by nath on 17-Oct-17.
 */

public class ImageManager {
    private static final String TAG = "ImageManager";

    public static Bitmap getBitmap(String imgUrl){
        File imageFile = new File(imgUrl);
        FileInputStream fis = null;
        Bitmap bitmap = null;

        try{
            fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        }catch(FileNotFoundException e){
            Log.e(TAG, "getBitmap: FileNotFoundException: " + e);
        }finally{
            try{
                fis.close();
            }catch(IOException e){
                Log.e(TAG, "getBitmap: IOException: " + e);
            }
        }
        return bitmap;
    }


    /** return byte array from a bitmap
     *  quality is in % 0%<100
     * @param bm
     * @param quality
     * @return
     */
    public static byte[] getBytesFromBitmap(Bitmap bm){

        Bitmap newBM = bm;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

//        newBM = getResizedBitmap(bm, 50);
        int sizeTarget = 15000000;
        if(bm.getByteCount() > sizeTarget){
            Log.d(TAG, "getBytesFromBitmap: bm.getByteCount: " + bm.getByteCount());
            float newPercentage = (float)(sizeTarget) / (float)(bm.getByteCount()) ;
            Log.d(TAG, "getBytesFromBitmap: sizeTarget: " + sizeTarget);
            Log.d(TAG, "getBytesFromBitmap: bm.getByteCount(): " + bm.getByteCount());
            Log.d(TAG, "getBytesFromBitmap: newPercentage: " + newPercentage);

            newBM = getResizedBitmap(bm, newPercentage );
            Log.d(TAG, "getBytesFromBitmap: newBM.getByteSize: " + newBM.getByteCount());
        }


        newBM.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }


    /**
     * reduces the size of the image
     * @param image
     * @param percentage
     * @return
     */
    private static Bitmap getResizedBitmap(Bitmap image, float percentage) {
        Log.d(TAG, "getResizedBitmap: image: " + image);
        Log.d(TAG, "getResizedBitmap: percentage: " + percentage);
        int width = image.getWidth();
        int height = image.getHeight();
        Log.d(TAG, "getResizedBitmap: width: " + width);
        Log.d(TAG, "getResizedBitmap: height: " + height);

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = (int) (width * percentage);
            height = (int) (width / bitmapRatio);
        } else {
            height = (int) (height * percentage);
            width = (int) (height * bitmapRatio);
        }


        Log.d(TAG, "getResizedBitmap: newHeight: " + height);
        Log.d(TAG, "getResizedBitmap: newWidth: " + width);
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


}
