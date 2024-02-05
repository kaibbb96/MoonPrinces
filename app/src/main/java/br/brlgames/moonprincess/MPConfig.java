package br.brlgames.moonprincess;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MPConfig extends Application {
    private static Application MoonPrinces = new Application();
    public static String gameURL = "";
    public static String policyURL = "";
    public static String appCode = "BRL004GS";
    public static String apiURL = "";
    public static String appFlyerAPI = "";


    @Override
    public void onCreate() {


        super.onCreate();
        MoonPrinces = this;

        FirebaseApp.initializeApp(this);
        initializeConfig();
    }
    private void initializeConfig() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings remoteSetting = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();

        remoteConfig.setConfigSettingsAsync(remoteSetting);
}
    public static synchronized MPConfig getInstance() {
        return  (MPConfig) MoonPrinces;
    }


}