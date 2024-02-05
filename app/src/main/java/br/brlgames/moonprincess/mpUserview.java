package br.brlgames.moonprincess;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class mpUserview extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CODE = 230923;
    private WebView webView;
    private Button btnAccept;
    private TextView textvdeclined;
    private CardView moonPrincessCardView;
    private AlertDialog.Builder userAlert;
    private SharedPreferences mpPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp_userview);


        getWindow().setFlags(1024, 1024);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        mpPreferences = getSharedPreferences(MPConfig.appCode, MODE_PRIVATE);
        btnAccept = findViewById(R.id.btnaccept);
        textvdeclined = findViewById(R.id.tvdecline);
        webView = findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(MPConfig.policyURL);


        ImageView logoImageView = findViewById(R.id.imageView2);
        logoImageView.setImageResource(R.drawable.logo);

        btnAccept.setOnClickListener(v -> {
            userAlert = new AlertDialog.Builder(mpUserview.this);
            userAlert.setTitle("User Data Consent");
            userAlert.setMessage("We may collect your information based on your activities during the usage of the app to provide better user experience");

            userAlert.setPositiveButton("Agree", (dialog, which) -> {

                mpPreferences.edit().putBoolean("PermitSendData", true).apply();
                mpPreferences.edit().putBoolean("DoneUserConsent", true).apply();
                dialog.dismiss();
                if (!checkpermission()) {
                    requestpermissions();
                }
                openGame();
            });

            userAlert.setNegativeButton("Disagree", (dialog, which) -> {

                mpPreferences.edit().putBoolean("PermitSendData", false).apply();
                mpPreferences.edit().putBoolean("DoneUserConsent", false).apply();
                dialog.dismiss();
            });

            userAlert.setOnDismissListener(dialog -> {
                if(!checkpermission()){

                    requestpermissions();
                }
                openGame();
            });

            userAlert.show();

        });

        textvdeclined.setOnClickListener(v -> {
            showExitConfirmation();

        });

    }


    private void showExitConfirmation() {
        AlertDialog.Builder exitAlert = new AlertDialog.Builder(mpUserview.this);
        exitAlert.setTitle("Exit Confirmation");
        exitAlert.setMessage("Are you sure you want to exit the app?");

        exitAlert.setPositiveButton("Yes", (dialog, which) -> {
            finishAffinity(); // Exit the app
        });

        exitAlert.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss(); // Dismiss the confirmation dialog
        });

        exitAlert.show();
    }


    //check user permission
    private boolean checkpermission(){
        //loc perm
        int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        int mediaPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mediaPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            mediaPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return locationPermission == PackageManager.PERMISSION_GRANTED
                && cameraPermission == PackageManager.PERMISSION_GRANTED
                && mediaPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestpermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_MEDIA_IMAGES }, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE }, PERMISSION_REQUEST_CODE);
        }
    }

    public void OnRequestPermissionsResult(int requestCode, @NonNull
    String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);


        if (requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED)

            {
                mpPreferences.edit().putBoolean("grantLocation", true);
                mpPreferences.edit().putBoolean("grantCamera", true);
                mpPreferences.edit().putBoolean("grantMedia", true);
            } else {
                mpPreferences.edit().putBoolean("grantLocation", false);
                mpPreferences.edit().putBoolean("grantCamera", false);
                mpPreferences.edit().putBoolean("grantMedia", false);
            }
            openGame();
        }

    }
    private void openGame(){
        Intent gameIntent = null;
        gameIntent = new Intent(this, MPContent.class);
        gameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(gameIntent);
        finish();

    }

}