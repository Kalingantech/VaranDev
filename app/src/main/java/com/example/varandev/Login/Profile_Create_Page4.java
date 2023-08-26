package com.example.varandev.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.varandev.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Profile_Create_Page4 extends AppCompatActivity {

    private EditText expectation_details_ref;
    private Button profile_next_btn;
    private ProgressBar horizontal_progress_bar;
    private FirebaseAuth fauth;
    private FirebaseUser mcurrentuser;
    private FirebaseFirestore db;
    private String uid;
    private Spinner mstatus_spinner,age_min_spinner,age_max_spinner,height_min_spinner,height_max_spinner,employment_spinner,salary_spinner;
    private String mstatus_selected,age_min_selected,age_max_selected,height_min_selected,height_max_selected,employment_selected,salary_selected;
    private ArrayAdapter<CharSequence> mstatus_adapter,age_min_adapter,age_max_adapter,height_min_adapter,height_max_adapter,employment_adapter,salary_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_create_page4);

        profile_next_btn = findViewById(R.id.xml_profile_next);

        //spinner
        mstatus_spinner = findViewById(R.id.xml_pref_mstatus);
        age_min_spinner = findViewById(R.id.xml_pref_min_age);
        age_max_spinner = findViewById(R.id.xml_pref_max_age);
        height_min_spinner = findViewById(R.id.xml_pref_min_height);
        height_max_spinner = findViewById(R.id.xml_pref_max_height);
        employment_spinner = findViewById(R.id.xml_pref_employment);
        salary_spinner = findViewById(R.id.xml_pref_salary);
        expectation_details_ref = findViewById(R.id.xml_expectation_details);

        horizontal_progress_bar = findViewById(R.id.xml_p_create_progressbar);
        horizontal_progress_bar.setVisibility(View.INVISIBLE);

        fauth = FirebaseAuth.getInstance();
        mcurrentuser = fauth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        uid = fauth.getCurrentUser().getUid();


        horizontal_progress_bar.setVisibility(View.INVISIBLE);


        /*---This code will stop users from typing numbers, attach it to edit texts---*/
        InputFilter[] Textfilters = new InputFilter[1];
        Textfilters[0] = new InputFilter(){
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {

                    String numbers = "01234567890";

                    char[] avoidChars = numbers.toCharArray();

                    for (int index = start; index < end; index++) {
                        if (new String(avoidChars).contains(String.valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }

        };

        expectation_details_ref.setFilters(Textfilters);

        /*---This code will stop users from typing numbers---*/

        /*---------------------mstatus_adapter--------------------------------*/
        mstatus_adapter = ArrayAdapter.createFromResource(this, R.array.mstatus_array, R.layout.spinner_layout);
        mstatus_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mstatus_spinner.setAdapter(mstatus_adapter);
        mstatus_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mstatus_selected = mstatus_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /*-------------------------mstatus_adapter----------------------------*/

        /*---------------------age_min_adapter--------------------------------*/
        age_min_adapter = ArrayAdapter.createFromResource(this, R.array.age_array, R.layout.spinner_layout);
        age_min_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        age_min_spinner.setAdapter(age_min_adapter);
        age_min_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                age_min_selected = age_min_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /*-------------------------age_min_adapter----------------------------*/


        /*---------------------age_max_adapter-------------------------------*/
        age_max_adapter = ArrayAdapter.createFromResource(this, R.array.age_array, R.layout.spinner_layout);
        age_max_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        age_max_spinner.setAdapter(age_max_adapter);
        age_max_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                age_max_selected = age_max_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /*-------------------------age_max_adapter---------------------------*/

        /*---------------------height_min_adapter--------------------------------*/
        height_min_adapter = ArrayAdapter.createFromResource(this, R.array.height_array, R.layout.spinner_layout);
        height_min_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        height_min_spinner.setAdapter(height_min_adapter);
        height_min_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_min_height_temp = height_min_spinner.getSelectedItem().toString();
                //Below code will split "210 cm/6ft 11in" based on space
                String[] selected_min_height_array1 = selected_min_height_temp.split("\\s* \\s*");
                //Below code will get the first string "210" from  "210 cm/6ft 11in"
                height_min_selected = Collections.min(Arrays.asList(selected_min_height_array1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /*-------------------------height_min_adapter----------------------------*/

        /*---------------------height_max_adapter--------------------------------*/
        height_max_adapter = ArrayAdapter.createFromResource(this, R.array.height_array, R.layout.spinner_layout);
        height_max_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        height_max_spinner.setAdapter(height_max_adapter);
        height_max_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_max_height_temp = height_max_spinner.getSelectedItem().toString();
                //Below code will split "210 cm/6ft 11in" based on space
                String[] selected_max_height_array1 = selected_max_height_temp.split("\\s* \\s*");
                //Below code will get the first string "210" from  "210 cm/6ft 11in"
                height_max_selected = Collections.min(Arrays.asList(selected_max_height_array1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /*-------------------------height_max_adapter----------------------------*/

        /*---------------------employment_adapter--------------------------------*/
        employment_adapter = ArrayAdapter.createFromResource(this, R.array.Employment_array, R.layout.spinner_layout);
        employment_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employment_spinner.setAdapter(employment_adapter);
        employment_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                employment_selected = employment_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /*-------------------------employment_adapter----------------------------*/

        /*---------------------salary_adapter--------------------------------*/
        salary_adapter = ArrayAdapter.createFromResource(this, R.array.Salary_array, R.layout.spinner_layout);
        salary_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salary_spinner.setAdapter(salary_adapter);
        salary_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                salary_selected = salary_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /*-------------------------salary_adapter----------------------------*/



        profile_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mstatus_adapter.equals("Select marriage status")) {
                    Toast.makeText(Profile_Create_Page4.this, "Select expected marriage status", Toast.LENGTH_LONG).show();
                } else if (age_min_adapter.equals("Select age")) {
                    Toast.makeText(Profile_Create_Page4.this, "Select minimum age", Toast.LENGTH_LONG).show();
                } else if (age_max_adapter.equals("Select age")) {
                    Toast.makeText(Profile_Create_Page4.this, "Select maximum age", Toast.LENGTH_LONG).show();
                } else if (height_min_adapter.equals("Select height")) {
                    Toast.makeText(Profile_Create_Page4.this, "Select minimum height", Toast.LENGTH_LONG).show();
                } else if (height_max_adapter.equals("Select height")) {
                    Toast.makeText(Profile_Create_Page4.this, "Select minimum height", Toast.LENGTH_LONG).show();
                } else if (employment_adapter.equals("Select Employment type")) {
                    Toast.makeText(Profile_Create_Page4.this, "Select expected Job type", Toast.LENGTH_LONG).show();
                } else if (salary_adapter.equals("Select Salary range")) {
                    Toast.makeText(Profile_Create_Page4.this, "Select your expected salary range", Toast.LENGTH_LONG).show();
                }else {

                    String expectation_details = expectation_details_ref.getText().toString();
                    horizontal_progress_bar.setVisibility(View.VISIBLE);


                    DocumentReference user_doc = db.collection("users").document(uid);
                    Map<String, Object> user_details = new HashMap<>();
                    user_details.put("preferred_mstatus", mstatus_selected);
                    user_details.put("preferred_min_age", age_min_selected);
                    user_details.put("preferred_max_age", age_max_selected);
                    user_details.put("preferred_min_height", height_min_selected);
                    user_details.put("preferred_max_height", height_max_selected);
                    user_details.put("preferred_employment", employment_selected);
                    user_details.put("preferred_salary", salary_selected);
                    user_details.put("preferred_expectation", expectation_details);

                    user_doc.update(user_details).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Profile_Create_Page4.this, "Profile  upload success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Profile_Create_Page4.this, Profile_Create_Page5.class));
                            finish();
                            horizontal_progress_bar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(Profile_Create_Page4.this, "Profile upload failed", Toast.LENGTH_SHORT).show();
                            horizontal_progress_bar.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "" + e.toString());
                        }
                    });
                    //start uploading the data to firestore
                }
            }
        });

    }
}