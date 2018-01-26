/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class DetailActivity extends AppCompatActivity
//  COMPLETED Implement LoaderManager.LoaderCallbacks<Cursor>
    implements LoaderManager.LoaderCallbacks<Cursor>{

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

//  COMPLETED Create a String array containing the names of the desired data columns from our ContentProvider
    public static final String[] WEATHER_DATA_COLUMNS = new String[]{
        WeatherContract.WeatherEntry.COLUMN_DATE,
        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
        WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
        WeatherContract.WeatherEntry.COLUMN_PRESSURE,
        WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
        WeatherContract.WeatherEntry.COLUMN_DEGREES
    };
//  COMPLETED Create constant int values representing each column name's position above
    public static final int COLUMN_DATE_INDEX = 0;
    public static final int COLUMN_WEATHER_ID_INDEX = 1;
    public static final int COLUMN_MIN_TEMP_INDEX = 2;
    public static final int COLUMN_MAX_TEMP_INDEX = 3;
    public static final int COLUMN_HUMIDITY_INDEX = 4;
    public static final int COLUMN_PRESSURE_INDEX = 5;
    public static final int COLUMN_WIND_SPEED_INDEX = 6;
    public static final int COLUMN_WIND_DIRECTION_INDEX = 7;

//  COMPLETED Create a constant int to identify our loader used in DetailActivity

    private static final int DETAIL_ACTIVITY_ID = 999;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

//  COMPLETED Declare a private Uri field called mUri
    private Uri mUri;

//  COMPLETED Remove the mWeatherDisplay TextView declaration

//  COMPLETED Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mWeatherDate;
    private TextView mWeatherDescription;
    private TextView mWeatherHighTemp;
    private TextView mWeatherLowTemp;
    private TextView mWeatherHumidity;
    private TextView mWeatherWind;
    private TextView mWeatherPressure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      COMPLETED Remove mWeatherDisplay TextView

//      COMPLETED Find each of the TextViews by ID
        mWeatherDate = (TextView) findViewById(R.id.weather_date);
        mWeatherDescription = (TextView) findViewById(R.id.weather_description);
        mWeatherHighTemp = (TextView) findViewById(R.id.weather_high_temp);
        mWeatherLowTemp = (TextView) findViewById(R.id.weather_low_temp);
        mWeatherHumidity = (TextView) findViewById(R.id.weather_humidity);
        mWeatherPressure = (TextView) findViewById(R.id.weather_pressure);
        mWeatherWind = (TextView) findViewById(R.id.weather_wind);

//      COMPLETED Remove the code that checks for extra text

//      COMPLETED Use getData to get a reference to the URI passed with this Activity's Intent
//      COMPLETED Throw a NullPointerException if that URI is null
//      COMPLETED Initialize the loader for DetailActivity
        Intent activityIntent = getIntent();
        if (activityIntent != null) {
            mUri = activityIntent.getData();
            if (mUri == null) {
                throw new NullPointerException("URI is null in DetailActivity");
            }

        }
        getSupportLoaderManager().initLoader(DETAIL_ACTIVITY_ID, null, this);
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     *
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

//  COMPLETED Override onCreateLoader
//  COMPLETED If the loader requested is our detail loader, return the appropriate CursorLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == DETAIL_ACTIVITY_ID) {
            return new CursorLoader(this,
                    mUri,
                    WEATHER_DATA_COLUMNS,
                    null,
                    null,
                    null);
        }

        return null;
    }

    @Override
    //  COMPLETED Override onLoadFinished
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //  COMPLETED Check before doing anything that the Cursor has valid data
        if (data == null || !data.moveToFirst()) {
            return;
        }

        //  COMPLETED Display a readable date string
        String date = SunshineDateUtils.getFriendlyDateString(this,
                data.getLong(COLUMN_DATE_INDEX),
                true);
        mWeatherDate.setText(date);
        //  COMPLETED Display the weather description (using SunshineWeatherUtils)
        int weatherId = data.getInt(COLUMN_WEATHER_ID_INDEX);
        String description = SunshineWeatherUtils.getStringForWeatherCondition(
                this,
                weatherId);
        mWeatherDescription.setText(description);
        //  COMPLETED Display the high temperature
        double highTempDbl = data.getDouble(COLUMN_MAX_TEMP_INDEX);
        String highTemp = SunshineWeatherUtils.formatTemperature(this,
                highTempDbl);
        mWeatherHighTemp.setText(highTemp);
        //  COMPLETED Display the low temperature
        double lowTempDbl = data.getDouble(COLUMN_MIN_TEMP_INDEX);
        String lowTemp = SunshineWeatherUtils.formatTemperature(this,
                lowTempDbl);
        mWeatherLowTemp.setText(lowTemp);
        //  COMPLETED Display the humidity
        String humidity = getString(R.string.format_humidity,
                data.getFloat(COLUMN_HUMIDITY_INDEX));
        mWeatherHumidity.setText(humidity);
        //  COMPLETED Display the wind speed and direction
        String windFormatted = SunshineWeatherUtils.getFormattedWind(this,
                data.getFloat(COLUMN_WIND_SPEED_INDEX),
                data.getFloat(COLUMN_WIND_DIRECTION_INDEX));
        mWeatherWind.setText(windFormatted);
        //  COMPLETED Display the pressure
        String pressure = getString(R.string.format_pressure,
                data.getFloat(COLUMN_PRESSURE_INDEX));
        mWeatherPressure.setText(pressure);
        //  COMPLETED Store a forecast summary in mForecastSummary
        mForecastSummary = new StringBuilder(date).append(" - ").append(description).append(" - ").
                append(SunshineWeatherUtils.formatHighLows(this, highTempDbl, lowTempDbl)).toString();
    }

    @Override
//  COMPLETED Override onLoaderReset, but don't do anything in it yet
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}