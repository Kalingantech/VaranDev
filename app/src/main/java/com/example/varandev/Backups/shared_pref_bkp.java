package com.example.varandev.Backups;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.varandev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class shared_pref_bkp extends AppCompatActivity {

    private EditText editText1;
    private TextView textView1,text_view2;
private Button check_btn;
    private int deleted_user_counter;
    private FirebaseAuth newauth;
    private FirebaseUser mcurrentuser;
    private FirebaseFirestore db;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String TEXT1 = "text1";
    private int sharepref_out1;

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
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        sharepref_out1 = sharedPreferences.getInt(TEXT1,1);
        textView1.setText(Integer.toString(sharepref_out1));

        DocumentReference documentReference = db.collection("user_deleted").document("user_deleted");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    deleted_user_counter = task.getResult().getLong("counter").intValue();
                    text_view2.setText(Integer.toString(deleted_user_counter));
                }
            }
        });

        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharepref_out1 < deleted_user_counter){
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(TEXT1,deleted_user_counter);
                    editor.apply();

                    Toast.makeText(getApplicationContext(),"sharepref_out1 value has been updated",Toast.LENGTH_SHORT).show();
                }else if (sharepref_out1 == deleted_user_counter){
                    Toast.makeText(getApplicationContext(),"sharepref_out1 == deleted_user_counter",Toast.LENGTH_SHORT).show();
                }
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