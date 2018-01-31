package com.example.android.sunshine.sync;

import android.content.Context;
import android.content.Intent;

// COMPLETED Create a class called SunshineSyncUtils
public class SunshineSyncUtils {
    //  COMPLETED Create a public static void method called startImmediateSync
    //  COMPLETED Within that method, start the SunshineSyncIntentService
    public static void startImmediateSync (Context context) {
        Intent syncService = new Intent(context, SunshineSyncIntentService.class);
        context.startService(syncService);
    }
}