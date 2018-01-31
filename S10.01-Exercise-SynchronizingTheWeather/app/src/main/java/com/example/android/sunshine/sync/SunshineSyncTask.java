package com.example.android.sunshine.sync;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

//  COMPLETED Create a class called SunshineSyncTask
public class SunshineSyncTask {

    private static final String TAG = SunshineSyncTask.class.getName();

    //  COMPLETED Within SunshineSyncTask, create a synchronized public static void method called syncWeather
//      COMPLETED Within syncWeather, fetch new weather data
    public static synchronized void syncWeather(Context context) {
        try {
            //getting url to retrieve data from
            URL weatherDataUrl = NetworkUtils.getUrl(context);

            //getting weather data in JSON format from url
            String weatherDataStr = NetworkUtils.getResponseFromHttpUrl(weatherDataUrl);

            //preprocessing JSON data into content provider applicable format
            ContentValues[] weatherDataValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, weatherDataStr);

            //verify that data is valid
//          COMPLETED If we have valid results, delete the old data and insert the new
            if (weatherDataValues != null && weatherDataValues.length > 0) {
                ContentResolver weatherDataProvider = context.getContentResolver();

                weatherDataProvider.delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);

                weatherDataProvider.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherDataValues);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot synchronize weather data" + e.getMessage());
        }

    }
}