package com.hanaone.tpwr.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

public class ImageUtils {
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    try{
	    	BitmapFactory.decodeResource(res, resId, options);
	    } catch (OutOfMemoryError e1){
	    	System.gc();
		    try{
		    	BitmapFactory.decodeResource(res, resId, options);
		    } catch (OutOfMemoryError e2){
		    	return decodeSampledBitmapFromResource(res, resId, reqWidth/2, reqHeight/2);
		    }	    	
	    }	    
	    

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    options.inPreferredConfig = Config.RGB_565;
	    options.inDither = true;	    
	    Bitmap rs = null;
	    try{
	    	rs = BitmapFactory.decodeResource(res, resId, options);
	    } catch (OutOfMemoryError e1){
	    	System.gc();
		    try{
		    	rs = BitmapFactory.decodeResource(res, resId, options);
		    } catch (OutOfMemoryError e2){
		    	return decodeSampledBitmapFromResource(res, resId, reqWidth/2, reqHeight/2);
		    }	    	
	    }
	    return rs;	    
	}
	public static Bitmap decodeSampledBitmapFromFile(String path,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    try{
	    	BitmapFactory.decodeFile(path, options);
	    } catch (OutOfMemoryError e1){
	    	System.gc();
		    try{
		    	BitmapFactory.decodeFile(path, options);
		    } catch (OutOfMemoryError e2){
		    	return decodeSampledBitmapFromFile(path, reqWidth/2, reqHeight/2);
		    }	    	
	    }	    
	    	    
	    

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    options.inPreferredConfig = Config.RGB_565;
	    options.inDither = true;
	    Bitmap rs = null;
	    try{
	    	rs = BitmapFactory.decodeFile(path, options);
	    } catch (OutOfMemoryError e1){
	    	System.gc();
		    try{
		    	rs = BitmapFactory.decodeFile(path, options);
		    } catch (OutOfMemoryError e2){
		    	return decodeSampledBitmapFromFile(path, reqWidth/2, reqHeight/2);
		    }	    	
	    }
	    return rs;
	}	
	public static float convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
	    return dp;
	}	
}
