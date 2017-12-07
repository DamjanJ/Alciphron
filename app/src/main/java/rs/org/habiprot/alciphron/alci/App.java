package rs.org.habiprot.alciphron.alci;

import android.app.Application;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.webkit.WebViewClient;

import com.zerocodeteam.network.ZctNetwork;

import rs.org.habiprot.alciphron.alci.common.AppConstants;
import rs.org.habiprot.alciphron.alci.common.AppSettings;
import rs.org.habiprot.alciphron.alci.common.ZctLogger;
import rs.org.habiprot.alciphron.alci.common.ZctPersistData;

/**
 * Created by milorad on 6.12.17..
 */

public class App extends Application {
    private static ZctLogger mLogger = new ZctLogger(App.class.getSimpleName(), BuildConfig.DEBUG);
    private static App sInstance;

    // Used for app settings
    private static AppSettings sAppSettings;

    // Used to call API
    private static ZctNetwork sNetwork;

    private ZctPersistData mSharedPref;

    public static App getInstance() {
        return sInstance;
    }

    public static AppSettings getAppSettings() {
        return sAppSettings;
    }

    public static ZctNetwork getNetwork() {
        return sNetwork;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        sAppSettings = new AppSettings(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()), getApplicationContext());

        // Init network module in order to extend default timeout time
        sNetwork = new ZctNetwork.Builder(this).requestTimeout(5000).enableConsoleDebugging().build();

        mSharedPref = new ZctPersistData(getApplicationContext(), false);
        mLogger.d("Shared preferences created");

    }

    public void setCookie(String cookie) {
        mLogger.d("Set Cookie: " + cookie);
        mSharedPref.storeData(AppConstants.PREF_COOKIE_KEY, cookie);
    }

    public String getCookie() {
        mLogger.d("Getting Cookie");
        return (String) mSharedPref.readData(ZctPersistData.Type.STRING, AppConstants.PREF_COOKIE_KEY);

    }
}