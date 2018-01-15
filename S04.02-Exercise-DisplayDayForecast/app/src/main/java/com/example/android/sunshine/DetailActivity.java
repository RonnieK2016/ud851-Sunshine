package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView mWeatherDetail;

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mWeatherDetail = (TextView) findViewById(R.id.tv_weather_detail);
        // COMPLETED Display the weather forecast that was passed from MainActivity
        Intent receivedIntent = getIntent();
        if (receivedIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mWeatherDetail.setText(receivedIntent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }
}