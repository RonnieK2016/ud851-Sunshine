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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // COMPLETED Create a field to store the weather display TextView
    TextView myWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // COMPLETED Use findViewById to get a reference to the weather display TextView
        myWeatherData = (TextView) findViewById(R.id.tv_weather_data);

        // COMPLETED Create an array of Strings that contain fake weather data
        String[] weatherData = new String[]{
                "Atlanta, Georgia            22",
                "Austin, Texas               27",
                "Baltimore, Maryland         18",
                "Birmingham, Alabama         23",
                "Boston, Massachusetts       15",
                "Buffalo, New York           14",
                "Charlotte, North Carolina   22",
                "Chicago, Illinois           15",
                "Cincinnati, Ohio            18",
                "Cleveland, Ohio             15",
                "Columbus, Ohio              17",
                "Dallas, Texas               25",
                "Denver, Colorado            18",
                "Detroit, Michigan           15",
                "Hartford, Connecticut       16",
                "Houston, Texas              27",
                "Indianapolis, Indiana       17"
        };

        // COMPLETED Append each String from the fake weather data array to the TextView
        for (String city : weatherData) {
            myWeatherData.append(city + "\n\n");
        }
    }
}