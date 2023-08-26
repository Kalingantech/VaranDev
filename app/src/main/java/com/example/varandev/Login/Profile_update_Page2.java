package com.example.varandev.Login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.varandev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Profile_update_Page2 extends AppCompatActivity {

    //professional info
    private TextView education_ref, employment_ref, occupation_ref, salary_ref, dosham_list;
    private String education, education_details, employment, occupation, employment_details, salary;

    //Religious & family info
    private TextView nakshatram_ref, rasi_ref, dosham_ref;
    private String nakshatram, rasi;

    private EditText education_details_ref, employment_details_ref;
    private Button profile_next_btn;
    private ProgressBar horizontal_progress_bar;
    private FirebaseAuth fauth;
    private FirebaseUser mcurrentuser;
    private FirebaseFirestore db;
    private String uid;
    private TextView multi_select_spinner_txt;

    private Spinner education_spinner, employment_spinner, occupation_spinner, salary_spinner, nakshatram_spinner, rasi_spinner;
    private String selected_education, selected_employment, selected_occupation, selected_salary, selected_nakshatram, selected_rasi;
    private ArrayAdapter<CharSequence> education_adapter, employment_adapter, occupation_adapter, salary_adapter, nakshatram_adapter, rasi_adapter;
    List<String> dosham;

    boolean[] selectdosham;
    ArrayList<Integer> doshamlist = new ArrayList<>();
    String[] doshamarray = {"no dosham", "chevva dosham", "rahu-kethu dosham", "kalathra dosham", "kalasharpam dosham", "naga dosham"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update_page2);


        education_details_ref = findViewById(R.id.xml_education_details);
        employment_details_ref = findViewById(R.id.xml_employment_details);
        profile_next_btn = findViewById(R.id.xml_profile_save);
        profile_next_btn.setText("Save");

        //spinner
        education_spinner = findViewById(R.id.xml_education);
        employment_spinner = findViewById(R.id.xml_employment);
        occupation_spinner = findViewById(R.id.xml_occupation);
        salary_spinner = findViewById(R.id.xml_salary);
        nakshatram_spinner = findViewById(R.id.xml_nakshatram);
        rasi_spinner = findViewById(R.id.xml_rasi);
        multi_select_spinner_txt = findViewById(R.id.xml_multi_select_dosham);
        horizontal_progress_bar = findViewById(R.id.xml_p_create_progressbar);
        horizontal_progress_bar.setVisibility(View.INVISIBLE);

        fauth = FirebaseAuth.getInstance();
        mcurrentuser = fauth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        uid = fauth.getCurrentUser().getUid();
        horizontal_progress_bar.setVisibility(View.INVISIBLE);


        //This is will check if user has created the proile already
        DocumentReference documentReference = db.collection("users").document(uid);
        documentReference.get(Source.CACHE).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    //personal info

                    //professional info
                    education = task.getResult().getString("education");
                    education_details = task.getResult().getString("education_details");
                    employment = task.getResult().getString("employment");
                    occupation = task.getResult().getString("occupation");
                    employment_details = task.getResult().getString("employment_details");
                    salary = task.getResult().getString("salary");
                    //Religious & family info
                    nakshatram = task.getResult().getString("nakshatram");
                    rasi = task.getResult().getString("rasi");
                    //dosam - try to get multi selectable spinner
                    //dosham = task.getResult().getString("dosham");
                    dosham = (List<String>) task.getResult().get("dosham");

                }
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                education_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Education_array, R.layout.spinner_layout);
                education_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                education_spinner.setAdapter(education_adapter);
                for (int i = 0; i < education_spinner.getCount(); i++) {
                    if (education_spinner.getItemAtPosition(i).equals(education)) {
                        education_spinner.setSelection(i);
                        break;
                    }
                }

                employment_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Employment_array, R.layout.spinner_layout);
                employment_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                employment_spinner.setAdapter(employment_adapter);
                for (int i = 0; i < employment_spinner.getCount(); i++) {
                    if (employment_spinner.getItemAtPosition(i).equals(employment)) {
                        employment_spinner.setSelection(i);
                        break;
                    }
                }

                occupation_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Occupation_array, R.layout.spinner_layout);
                occupation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                occupation_spinner.setAdapter(occupation_adapter);
                for (int i = 0; i < occupation_spinner.getCount(); i++) {
                    if (occupation_spinner.getItemAtPosition(i).equals(occupation)) {
                        occupation_spinner.setSelection(i);
                        break;
                    }
                }


                salary_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Salary_array, R.layout.spinner_layout);
                salary_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                salary_spinner.setAdapter(salary_adapter);
                for (int i = 0; i < salary_spinner.getCount(); i++) {
                    if (salary_spinner.getItemAtPosition(i).equals(salary)) {
                        salary_spinner.setSelection(i);
                        break;
                    }
                }

                nakshatram_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Nakshatram_array, R.layout.spinner_layout);
                nakshatram_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                nakshatram_spinner.setAdapter(nakshatram_adapter);
                for (int i = 0; i < nakshatram_spinner.getCount(); i++) {
                    if (nakshatram_spinner.getItemAtPosition(i).toString().contains(nakshatram)) {
                        nakshatram_spinner.setSelection(i);
                        break;
                    }
                }

                rasi_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Rasi_array, R.layout.spinner_layout);
                rasi_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                rasi_spinner.setAdapter(rasi_adapter);
                for (int i = 0; i < rasi_spinner.getCount(); i++) {
                    if (rasi_spinner.getItemAtPosition(i).toString().contains(rasi)) {
                        rasi_spinner.setSelection(i);
                        break;
                    }
                }

                education_details_ref.setText(education_details);
                employment_details_ref.setText(employment_details);

                String delim = ",";
                String dosham_strings = String.join(delim, dosham);
                multi_select_spinner_txt.setText(dosham_strings);
            }
        });


        /*---------------------education_spinner--------------------------------*/
        education_adapter = ArrayAdapter.createFromResource(this, R.array.Education_array, R.layout.spinner_layout);
        education_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        education_spinner.setAdapter(education_adapter);
        education_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_education = education_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*-------------------------spinner creator----------------------------*/

        /*---------------------employment mstatus--------------------------------*/
        employment_adapter = ArrayAdapter.createFromResource(this, R.array.Employment_array, R.layout.spinner_layout);
        employment_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employment_spinner.setAdapter(employment_adapter);
        employment_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_employment = employment_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*-------------------------employment mstatus----------------------------*/


        /*---------------------occupation_spinner--------------------------------*/
        occupation_adapter = ArrayAdapter.createFromResource(this, R.array.Occupation_array, R.layout.spinner_layout);
        occupation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        occupation_spinner.setAdapter(occupation_adapter);
        occupation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_occupation = occupation_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*-------------------------occupation_spinner----------------------------*/

        /*---------------------salary_spinner--------------------------------*/
        salary_adapter = ArrayAdapter.createFromResource(this, R.array.Salary_array, R.layout.spinner_layout);
        salary_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        salary_spinner.setAdapter(salary_adapter);
        salary_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_salary = salary_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*-------------------------salary_spinner----------------------------*/

        /*---------------------nakshtram_spinner--------------------------------*/
        nakshatram_adapter = ArrayAdapter.createFromResource(this, R.array.Nakshatram_array, R.layout.spinner_layout);
        nakshatram_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nakshatram_spinner.setAdapter(nakshatram_adapter);
        nakshatram_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selected_nakshtram_temp = nakshatram_spinner.getSelectedItem().toString();
                //Below code will split based "-" in array மேஷம்-Mesham
                String[] selected_nakshtram_array1 = selected_nakshtram_temp.split("\\s*-\\s*");
                //Below code will get the first string when ordering alphabarically, means english comes first
                selected_nakshatram = Collections.min(Arrays.asList(selected_nakshtram_array1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*-------------------------nakshtram_spinner----------------------------*/

        /*---------------------rasi_spinner--------------------------------*/
        rasi_adapter = ArrayAdapter.createFromResource(this, R.array.Rasi_array, R.layout.spinner_layout);
        rasi_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rasi_spinner.setAdapter(rasi_adapter);
        rasi_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_rasi_temp = rasi_spinner.getSelectedItem().toString();
                //Below code will split based "-" in array மேஷம்-Mesham
                String[] selected_rasi_array1 = selected_rasi_temp.split("\\s*-\\s*");
                //Below code will get the first string when ordering alphabarically, means english comes first
                selected_rasi = Collections.min(Arrays.asList(selected_rasi_array1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*-------------------------rasi_spinner----------------------------*/

        /*---------------------dosham_spinner--------------------------------*/
        selectdosham = new boolean[doshamarray.length];
        multi_select_spinner_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile_update_Page2.this);

                builder.setTitle("Select your dosham");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(doshamarray, selectdosham, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            doshamlist.add(i);
                            Collections.sort(doshamlist);
                        } else {
                            doshamlist.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < doshamlist.size(); j++) {
                            stringBuilder.append(doshamarray[doshamlist.get(j)]);
                            if (j != doshamlist.size() - 1) {
                                stringBuilder.append(",");
                            }
                        }
                        multi_select_spinner_txt.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < selectdosham.length; j++) {
                            selectdosham[j] = false;
                            doshamlist.clear();
                            multi_select_spinner_txt.setText("");
                        }
                    }
                });
                builder.show();

            }


        });

        /*-------------------------dosham_spinner----------------------------*/


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

        education_details_ref.setFilters(Textfilters);
        employment_details_ref.setFilters(Textfilters);

        /*---This code will stop users from typing numbers---*/




        profile_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (selected_education.equals("Select your Education")) {
                    Toast.makeText(Profile_update_Page2.this, "Select your Education", Toast.LENGTH_LONG).show();
                } else if (selected_employment.equals("Select Employment type")) {
                    Toast.makeText(Profile_update_Page2.this, "Select your Employment type", Toast.LENGTH_LONG).show();
                } else if (selected_occupation.equals("Select your Occupation type")) {
                    Toast.makeText(Profile_update_Page2.this, "Select your Occupation type", Toast.LENGTH_LONG).show();
                } else if (selected_salary.equals("Select Salary range")) {
                    Toast.makeText(Profile_update_Page2.this, "Select your Salary range", Toast.LENGTH_LONG).show();
                } else if (selected_nakshatram.equals("Select your Nakshatram")) {
                    Toast.makeText(Profile_update_Page2.this, "Select your Nakshatram", Toast.LENGTH_LONG).show();
                } else if (selected_rasi.equals("Select your rasi")) {
                    Toast.makeText(Profile_update_Page2.this, "Select your rasi", Toast.LENGTH_LONG).show();
                } else {

                    //below line will convert string1,string2 to array
                    String doshaminput = multi_select_spinner_txt.getText().toString();
                    String[] doshamlist = doshaminput.split("\\s*,\\s*");
                    List<String> selected_dosham = Arrays.asList(doshamlist);

                    //start uploading the data to firestore
                    Date date = new Date();
                    String education_details = education_details_ref.getText().toString();
                    String employment_details = employment_details_ref.getText().toString();

                    horizontal_progress_bar.setVisibility(View.VISIBLE);

                    DocumentReference user_doc = db.collection("users").document(uid);
                    Map<String, Object> user_details = new HashMap<>();
                    user_details.put("education", selected_education);
                    user_details.put("education_details", education_details);
                    user_details.put("employment", selected_employment);
                    user_details.put("occupation", selected_occupation);
                    user_details.put("employment_details", employment_details);
                    user_details.put("salary", selected_salary);
                    user_details.put("nakshatram", selected_nakshatram);
                    user_details.put("rasi", selected_rasi);
                    user_details.put("dosham", selected_dosham);

                    user_details.put("login", date);

                    user_doc.update(user_details).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Profile_update_Page2.this, "Profile  upload success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Profile_update_Page2.this, My_profile_Activity.class));
                            finish();
                            horizontal_progress_bar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(Profile_update_Page2.this, "Profile upload failed", Toast.LENGTH_SHORT).show();
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