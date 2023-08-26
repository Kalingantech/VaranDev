package com.example.varandev.Backups;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varandev.Login.Profile_Create_Page1;
import com.example.varandev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class shared_pref extends AppCompatActivity {

    private EditText editText1;
    private TextView textView1,text_view2;
    private Button check_btn;
    private int deleted_user_counter;
    private FirebaseAuth newauth;
    private FirebaseUser mcurrentuser;
    private FirebaseFirestore db;

    //public static final Long SHARED_PREFS = "shared_prefs";
    //
    private long sharepref_out1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_pref);
        textView1 = findViewById(R.id.text_view1);
        text_view2=findViewById(R.id.text_view2);
        check_btn =findViewById(R.id.xml_check_btn);
        newauth = FirebaseAuth.getInstance();
        mcurrentuser = newauth.getCurrentUser();
        db = FirebaseFirestore.getInstance();




        //if there is no such preference saves it will take default valuw as give i.e"1" here
        Date defaultdate = new Date();
        SharedPreferences sharedPreferences = getSharedPreferences("timestamp",MODE_PRIVATE);
        Date newdate = new Date(sharedPreferences.getLong("timestamp",defaultdate.getTime()));
        textView1.setText(String.valueOf(newdate));


       DocumentReference documentReference = db.collection("user_deleted").document("user_deleted");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    Date timstamp = task.getResult().getDate("timestamp");
                    text_view2.setText(timstamp.toString());
                }
            }
        });


        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Long timestamp = System.currentTimeMillis();
                    SharedPreferences sharedPreferences = getSharedPreferences("timestamp",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("timestamp", timestamp);
                    editor.apply();



                DocumentReference documentReference = db.collection("user_deleted").document("user_deleted");
                Map<String, Object> timestamp1 = new HashMap<>();
                timestamp1.put("timestamp",new Timestamp(new Date()));
                documentReference.set(timestamp1);

            }
        });



        //loaddata();
        //updatedata();

    }

    /*private void savedata() {
        //shared preferences will not be deleted if device rebooted
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT1,textView1.getText().toString());
        editor.apply(); //must
    }

    private void loaddata(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        sharepref_out1 = sharedPreferences.getString(TEXT1,"1");
        //2nd parameter "" means default value if there no such shared pref.
        //But this default value will be taken only during first time installation & if app is unstalled this share prefs will be created and re-install will take this default value
        //but during app update saved shared pref will remain as it is, means set default will not be taken.
        //shared pref are for app level not user level, means on same device when logged in from different user saved value before will remain same.

    }
    private void updatedata(){
        //textView1.setText(sharepref_out1);
    }*/
}