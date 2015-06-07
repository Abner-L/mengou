package com.baasplus.mengousms.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.baasplus.mengousms.Global;
import com.baasplus.mengousms.R;
import com.baasplus.mengousms.constants.Setting;


public class SettingsActivity extends PreferenceActivity {

    private static SharedPreferences settingSP;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Setting.PREFERENCE_FILE_NAME);
        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
        settingSP = getSharedPreferences(Setting.PREFERENCE_FILE_NAME,MODE_PRIVATE);

        setupPreferencesScreen();
    }

    private void setupPreferencesScreen() {
        addPreferencesFromResource(R.xml.pref_number_set);
        addPreferencesFromResource(R.xml.pref_key_words);

        bindPreferenceSummaryToValue(findPreference(Setting.KEY_SMS_KEYWORDS));
    }

    private  static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            Global.refreshKeyWords(stringValue);
            return true;
        }
    };


    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,settingSP.getString(preference.getKey(), ""));
    }

}
