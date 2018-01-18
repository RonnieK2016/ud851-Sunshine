package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

// Do steps 5 - 11 within SettingsFragment
// COMPLETED Implement OnSharedPreferenceChangeListener from SettingsFragment
// COMPLETED Create SettingsFragment and extend PreferenceFragmentCompat
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    // COMPLETED Override onCreatePreferences and add the preference xml file using addPreferencesFromResource
    // Do step 9 within onCreatePreference
    // COMPLETED Set the preference summary on each preference that isn't a CheckBoxPreference
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_screen);

        PreferenceScreen prefScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = prefScreen.getSharedPreferences();
        for (int i = 0; i < prefScreen.getPreferenceCount(); i++) {
            Preference preference = prefScreen.getPreference(i);
            if (!(preference instanceof CheckBoxPreference)) {
                setPreferenceSummary(preference, sharedPreferences.getString(preference.getKey(), ""));
            }
        }
    }

    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if (index >= 0) {
                listPreference.setSummary(listPreference.getEntries()[index]);
            }
        }
        else {
            preference.setSummary(value);
        }
    }

    @Override
    // COMPLETED Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (!(preference instanceof CheckBoxPreference)) {
            setPreferenceSummary(preference, sharedPreferences.getString(key,""));
        }
    }

    @Override
    // COMPLETED Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    // COMPLETED Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

}
