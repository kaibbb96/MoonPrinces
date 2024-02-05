package br.brlgames.moonprincess;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class MPLoading extends AppCompatActivity {

    private FirebaseRemoteConfig remoteConfiguration;
    private SharedPreferences mpPreferences;

    private MPConfig MoonPrincess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mploading);


        LottieAnimationView animationView = findViewById(R.id.lottieAnimationView);
        animationView.setAnimation(R.raw.json);
        animationView.loop(true);
        animationView.playAnimation();

        getWindow().setFlags(1024, 1024);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        MoonPrincess = MPConfig.getInstance();

        remoteConfiguration = FirebaseRemoteConfig.getInstance();
        mpPreferences = getSharedPreferences(MPConfig.appCode, MODE_PRIVATE);

        remoteConfiguration.setDefaultsAsync(R.xml.mpdefault);
        remoteConfiguration.fetchAndActivate().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {

                Log.d("FirebaseRemoteConfig:", "Loading config successful");

            }
            else {
                Log.d("FirebaseRemoteConfig:", "Loading config not successful");
            }
            setNewValue();
        });

    }
    private void setNewValue() {
        MoonPrincess.apiURL = remoteConfiguration.getString("apiURL");

        RequestQueue apiRequest = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appid", MPConfig.appCode);
        }catch (JSONException e){
            e.printStackTrace();

        }
        String endPoint = MPConfig.apiURL + "?appid=" + MPConfig.appCode;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(endPoint,
                jsonObject, response -> {
            try {
                MoonPrincess.gameURL = response.getString("gameURL");
                MoonPrincess.policyURL = response.getString("policyURL");

                if(!mpPreferences.getBoolean("doneUserContent", false))
                {

                    new Handler().postDelayed(() -> {
                        Intent i = new Intent(getApplicationContext(), mpUserview.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    }, 3000);
                }
                else
                {
                    new Handler().postDelayed(() -> {

                        Intent i = new Intent(getApplicationContext(), MPContent.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    }, 3000);
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> {
            Log.d("API:Error", error.getMessage());
        });

        apiRequest.add(jsonObjectRequest);
    }

}