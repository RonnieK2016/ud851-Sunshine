package com.example.android.sunshine.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

// COMPLETED Create a new class called SunshineSyncIntentService that extends IntentService
public class SunshineSyncIntentService extends IntentService {

    //  COMPLETED Create a constructor that calls super and passes the name of this class
    public SunshineSyncIntentService() {
        super(SunshineSyncIntentService.class.getName());
    }

    @Override
    //  COMPLETED Override onHandleIntent, and within it, call SunshineSyncTask.syncWeather
    protected void onHandleIntent(@Nullable Intent intent) {
        SunshineSyncTask.syncWeather(this);
    }
}