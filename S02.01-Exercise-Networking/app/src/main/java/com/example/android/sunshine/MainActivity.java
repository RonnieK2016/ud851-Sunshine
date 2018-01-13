/*
 * Copyright (C) 2016 The Android Open Source Project
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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        // COMPLETED Delete the dummy weather data. You will be getting REAL data from the Internet in this lesson.
        /*
         * This String array contains dummy weather data. Later in the course, we're going to get
         * real weather data. For now, we want to get something on the screen as quickly as
         * possible, so we'll display this dummy data.
         */

        // COMPLETED Delete the for loop that populates the TextView with dummy data
        /*
         * Iterate through the array and append the Strings to the TextView. The reason why we add
         * the "\n\n\n" after the String is to give visual separation between each String in the
         * TextView. Later, we'll learn about a better way to display lists of data.
         */

        // COMPLETED Call loadWeatherData to perform the network request to get the weather
        loadWeatherData();
    }

    // COMPLETED Create a method that will get the user's preferred location and execute your new AsyncTask and call it loadWeatherData
    private void loadWeatherData() {
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        URL weatherDataUrl = NetworkUtils.buildUrl(location);
        new GetWeatherDataAsync().execute(weatherDataUrl);
    }

    // COMPLETED Create a class that extends AsyncTask to perform network requests
    // COMPLETED Override the doInBackground method to perform your network requests
    // COMPLETED Override the onPostExecute method to display the results of the network request
    public class GetWeatherDataAsync extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            URL url = urls[0];
            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !"".equals(s)) {
                try {
                    JSONObject weatherDataJson = new JSONObject(s);
                    JSONObject cityJson = weatherDataJson.getJSONObject("city");
                    mWeatherTextView.append(cityJson.getString("name") + ":\n");
                    JSONArray listJson = weatherDataJson.getJSONArray("list");
                    for (int i = 0; i < listJson.length(); i++) {
                        JSONObject tempData = (JSONObject) listJson.get(i);
                        JSONObject temperature = tempData.getJSONObject("temp");
                        Double dayTemp = temperature.getDouble("day");
                        Long dateMsecs = tempData.getLong("dt");
                        Date date = new Date(dateMsecs);
                        DateFormat df = SimpleDateFormat.getDateInstance();
                        mWeatherTextView.append(df.format(date));
                        mWeatherTextView.append(" : ");
                        mWeatherTextView.append(dayTemp.toString());
                        mWeatherTextView.append("\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}