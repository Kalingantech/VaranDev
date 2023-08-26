package com.example.varandev.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.varandev.HomeActivity;
import com.example.varandev.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Splash_Activity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth newauth;
    private FirebaseUser mcurrentuser;
    private TextView splashtxt;
    private String my_uid;
    public static final int APP_UPDATE = 22;
    AppUpdateManager appUpdateManager;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        db = FirebaseFirestore.getInstance();

        newauth = FirebaseAuth.getInstance();
        mcurrentuser = newauth.getCurrentUser();
        splashtxt = findViewById(R.id.splashtext);
        handler = new Handler();


        //remove action bar
        //getSupportActionBar().hide();

        //remote top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        checkconnection();

    }

    private void checkconnection() {

        ConnectivityManager conmanager = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkinfo = conmanager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isConnected() || !networkinfo.isAvailable()) {

            //internet is inactive
            Dialog dialog = new Dialog((this));
            dialog.setContentView(R.layout.internet_prompt);

            //set outside touch
            dialog.setCanceledOnTouchOutside(false);
            //set size
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //set animation
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

            Button tryagain = dialog.findViewById(R.id.tryagain);
            tryagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //call recreate method
                    recreate();
                }
            });

            dialog.show();
        } else
        // if interenet is connected
        {
            //checkforupdates();
            loadData();

        }
    }

    /*private void checkforupdates() {
        appUpdateManager = AppUpdateManagerFactory.create(this);

// Returns an intent object that you use to check for an update.
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, Splash_Activity.this, APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

    private void loadData() {

        //when internet is active
        //to start handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mcurrentuser == null) {
                    Intent loginintent = new Intent(Splash_Activity.this, Signin_Activity.class);
                    loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    /*------------------Disable grabage collection------------------------*/
                    // The default cache size threshold is 100 MB. Configure "setCacheSizeBytes"
                    // for a different threshold (minimum 1 MB) or set to "CACHE_SIZE_UNLIMITED"
                    // to disable clean-up.
                    //this method should be called before any other firestore menthods.

                    /*FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                            .build();
                    db.setFirestoreSettings(settings);
                    */

                    /*------------------Disable grabage collection------------------------*/


                    startActivity(loginintent);
                    finish();
                } else {
                    //come here only for existing user
                    //ensure to delete a user from firestore if not for list of users who created and deleted account wwith mail id.
                    // Means if he deleted the account and ill have my_uid but will not have user doc and below "update will fail.
                    /*----------------below code will ensure to update last login of the user----------*/
                    my_uid = mcurrentuser.getUid();
                    Date last_login = new Date();
                    db.collection("users").document(my_uid).update("login", last_login).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("login", "updated last login");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.d("login", e.getMessage());
                        }
                    });
                    /*----------------below code will ensure to update last login of the user----------*/

                    Intent h_intent = new Intent(Splash_Activity.this, HomeActivity.class);
                    startActivity(h_intent);
                    finish();
                }
            }
        }, 3000);

    }

    @Override
    protected void onResume() {
        super.onResume();

        /*appUpdateManager = AppUpdateManagerFactory.create(this);

// Returns an intent object that you use to check for an update.
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, Splash_Activity.this, APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });*/

    }
}