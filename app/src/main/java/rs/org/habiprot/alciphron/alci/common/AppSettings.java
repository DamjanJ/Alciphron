package rs.org.habiprot.alciphron.alci.common;

import android.content.Context;
import android.content.SharedPreferences;

import rs.org.habiprot.alciphron.alci.BuildConfig;


/**
 * Created by Core Station on 10/29/2016.
 */

public class AppSettings {

    private final ZctLogger mLogger = new ZctLogger(AppSettings.class.getSimpleName(), BuildConfig.DEBUG);

    private Context mContext;
    private SharedPreferences mSettingsPreference;

    public AppSettings(SharedPreferences settingsPreference, Context context) {
        this.mSettingsPreference = settingsPreference;
        this.mContext = context;
        mLogger.d("Object created");
    }


}
