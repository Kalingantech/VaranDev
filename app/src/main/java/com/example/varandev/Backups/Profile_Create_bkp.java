package com.example.varandev.Backups;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.varandev.HomeActivity;
import com.example.varandev.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class Profile_Create_bkp extends AppCompatActivity {

    private EditText profilename_ref, user_phoneno_ref;
    private CircleImageView profilepic_ref1, profilepic_ref2, profilepic_ref3;
    private Button profile_save_btn;
    private ProgressBar progressBar;
    private ProgressBar horizontal_progress_bar;
    private FirebaseAuth fauth;
    private FirebaseUser mcurrentuser;
    private Uri profileimageuri1, profileimageuri2, profileimageuri3;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private String uid;
    public String gender,other_gender,customid;
    private Uri downloaduri1, downloaduri2, downloaduri3;
    private Switch photo_hide_toggleButton;
    private Spinner creator_spinner,mstatus_spinner,age_spinner,height_spinner, country_spinner, state_spinner, city_spinner;
    private String selected_creator,selected_mstatus,selected_age,selected_height, selected_country, selected_state, selected_city;
    private ArrayAdapter<CharSequence> creator_adapter,mstatus_adapter,age_adapter,height_adapter, country_adapter, state_adapter, city_adapter;

    private int photo_hide = 1;
    private static final int PICK_IMAGE1 = 1;
    private static final int PICK_IMAGE2 = 2;
    private static final int PICK_IMAGE3 = 3;

    RadioGroup rgp_gender;
    RadioButton rbtn_gender,rbtn1_gender,rbtn2_gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_create_page1);

        profilepic_ref1 = findViewById(R.id.xml_profile_pic1);
        profilepic_ref2 = findViewById(R.id.xml_profile_pic2);
        profilepic_ref3 = findViewById(R.id.xml_horoscope_pic);

        profilename_ref = findViewById(R.id.xml_profile_name);
        user_phoneno_ref = findViewById(R.id.xml_profile_phoneno);
        profile_save_btn = findViewById(R.id.xml_profile_save);
        photo_hide_toggleButton = findViewById(R.id.xml_photo_hide_option);

        //rgp_gender = findViewById(R.id.xml_rdgrp_gender_check);

        //spinner
        creator_spinner = findViewById(R.id.xml_created_by);
        mstatus_spinner = findViewById(R.id.xml_mstatus);
        age_spinner = findViewById(R.id.xml_profile_age);
        height_spinner = findViewById(R.id.xml_height);
        country_spinner = findViewById(R.id.xml_country);
        state_spinner = findViewById(R.id.xml_state);

        progressBar = findViewById(R.id.xml_progressBar);
        horizontal_progress_bar = findViewById(R.id.xml_p_create_progressbar);


        fauth = FirebaseAuth.getInstance();
        mcurrentuser = fauth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        uid = fauth.getCurrentUser().getUid();


        progressBar.setVisibility(View.INVISIBLE);
        horizontal_progress_bar.setVisibility(View.INVISIBLE);
        profilepic_ref2.setVisibility(View.INVISIBLE);
        profilepic_ref3.setVisibility(View.INVISIBLE);


        /*------------------------------------------------*/
        /*------------------------------------------------*/

        profilepic_ref1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE1);
                profilepic_ref2.setVisibility(View.VISIBLE);

            }
        });
        profilepic_ref2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE2);
                profilepic_ref3.setVisibility(View.VISIBLE);

            }
        });
        profilepic_ref3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE3);

            }
        });

        photo_hide_toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    // The toggle is enabled
                    photo_hide = 25;
                    Toast.makeText(Profile_Create_bkp.this, "Photo will be hidden", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    photo_hide = 1;
                    Toast.makeText(Profile_Create_bkp.this, "Not hidden", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*--------gendercheck-----------*/
        //below code will select the default gender value as Male
        /*rbtn1_gender = findViewById(R.id.xml_rdbtn_male);
        rbtn2_gender = findViewById(R.id.xml_rdbtn_female);
        rbtn1_gender.setChecked(true);
*/

        /*--------gendercheck-----------*/

        /*---------------------spinner creator--------------------------------*/
        creator_adapter = ArrayAdapter.createFromResource(this, R.array.creator_array, R.layout.spinner_layout);
        creator_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creator_spinner.setAdapter(creator_adapter);
        creator_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_creator = creator_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*-------------------------spinner creator----------------------------*/

        /*---------------------spinner mstatus--------------------------------*/
        mstatus_adapter = ArrayAdapter.createFromResource(this, R.array.mstatus_array, R.layout.spinner_layout);
        mstatus_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mstatus_spinner.setAdapter(mstatus_adapter);
        mstatus_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_mstatus = mstatus_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*-------------------------spinner mstatus----------------------------*/


        /*---------------------spinner age--------------------------------*/
        age_adapter = ArrayAdapter.createFromResource(this, R.array.age_array, R.layout.spinner_layout);
        age_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        age_spinner.setAdapter(age_adapter);
        age_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_age = age_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*-------------------------spinner age----------------------------*/


        /*---------------------spinner height--------------------------------*/
        height_adapter = ArrayAdapter.createFromResource(this, R.array.height_array, R.layout.spinner_layout);
        height_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        height_spinner.setAdapter(height_adapter);
        height_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_height = height_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*-------------------------spinner height----------------------------*/


        /*-----------------------Country_state_city_spinner------------------------------*/
        country_adapter = ArrayAdapter.createFromResource(this, R.array.array_country, R.layout.spinner_layout);
        country_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country_spinner.setAdapter(country_adapter);
        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                state_spinner = findViewById(R.id.xml_state);

                selected_country = country_spinner.getSelectedItem().toString();      //Obtain the country

                int adapterViewID = adapterView.getId();
                if (adapterViewID == R.id.xml_country) {
                    switch (selected_country) {
                        case "Select Your country":
                            state_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                    R.array.array_default_country, R.layout.spinner_layout);
                            break;
                        case "India":
                            state_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                    R.array.array_India, R.layout.spinner_layout);
                            break;
                        case "Singapore":
                            state_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                    R.array.array_Singapore, R.layout.spinner_layout);
                            break;
                        case "Sri lanka":
                            state_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                    R.array.array_Srilanka, R.layout.spinner_layout);
                            break;
                        case "Malaysia":
                            state_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                    R.array.array_Malaysia, R.layout.spinner_layout);
                            break;
                        case "other":
                            state_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                    R.array.array_other_country, R.layout.spinner_layout);
                            break;

                        default:
                            break;
                    }
                    state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     // Specify the layout to use when the list of choices appears
                    state_spinner.setAdapter(state_adapter);

                    state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            /*-----------------------------------------------------*/

                            city_spinner = findViewById(R.id.xml_city);

                            selected_state = state_spinner.getSelectedItem().toString();      //Obtain the selected State

                            int adapterViewID = adapterView.getId();
                            if (adapterViewID == R.id.xml_state) {
                                switch (selected_state) {
                                    case "Select Your State":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_default_districts, R.layout.spinner_layout);
                                        break;
                                    case "Andhra Pradesh":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_andhra_pradesh_districts, R.layout.spinner_layout);
                                        break;
                                    case "Arunachal Pradesh":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_arunachal_pradesh_districts, R.layout.spinner_layout);
                                        break;
                                    case "Assam":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_assam_districts, R.layout.spinner_layout);
                                        break;
                                    case "Bihar":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_bihar_districts, R.layout.spinner_layout);
                                        break;
                                    case "Chhattisgarh":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_chhattisgarh_districts, R.layout.spinner_layout);
                                        break;
                                    case "Goa":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_goa_districts, R.layout.spinner_layout);
                                        break;
                                    case "Gujarat":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_gujarat_districts, R.layout.spinner_layout);
                                        break;
                                    case "Haryana":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_haryana_districts, R.layout.spinner_layout);
                                        break;
                                    case "Himachal Pradesh":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_himachal_pradesh_districts, R.layout.spinner_layout);
                                        break;
                                    case "Jharkhand":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_jharkhand_districts, R.layout.spinner_layout);
                                        break;
                                    case "Karnataka":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_karnataka_districts, R.layout.spinner_layout);
                                        break;
                                    case "Kerala":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_kerala_districts, R.layout.spinner_layout);
                                        break;
                                    case "Madhya Pradesh":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_madhya_pradesh_districts, R.layout.spinner_layout);
                                        break;
                                    case "Maharashtra":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_maharashtra_districts, R.layout.spinner_layout);
                                        break;
                                    case "Manipur":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_manipur_districts, R.layout.spinner_layout);
                                        break;
                                    case "Meghalaya":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_meghalaya_districts, R.layout.spinner_layout);
                                        break;
                                    case "Mizoram":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_mizoram_districts, R.layout.spinner_layout);
                                        break;
                                    case "Nagaland":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_nagaland_districts, R.layout.spinner_layout);
                                        break;
                                    case "Odisha":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_odisha_districts, R.layout.spinner_layout);
                                        break;
                                    case "Punjab":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_punjab_districts, R.layout.spinner_layout);
                                        break;
                                    case "Rajasthan":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_rajasthan_districts, R.layout.spinner_layout);
                                        break;
                                    case "Sikkim":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_sikkim_districts, R.layout.spinner_layout);
                                        break;
                                    case "Tamil Nadu":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_tamil_nadu_districts, R.layout.spinner_layout);
                                        break;
                                    case "Telangana":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_telangana_districts, R.layout.spinner_layout);
                                        break;
                                    case "Tripura":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_tripura_districts, R.layout.spinner_layout);
                                        break;
                                    case "Uttar Pradesh":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_uttar_pradesh_districts, R.layout.spinner_layout);
                                        break;
                                    case "Uttarakhand":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_uttarakhand_districts, R.layout.spinner_layout);
                                        break;
                                    case "West Bengal":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_west_bengal_districts, R.layout.spinner_layout);
                                        break;
                                    case "Andaman and Nicobar Islands":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_andaman_nicobar_districts, R.layout.spinner_layout);
                                        break;
                                    case "Chandigarh":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_chandigarh_districts, R.layout.spinner_layout);
                                        break;
                                    case "Dadra and Nagar Haveli":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_dadra_nagar_haveli_districts, R.layout.spinner_layout);
                                        break;
                                    case "Daman and Diu":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_daman_diu_districts, R.layout.spinner_layout);
                                        break;
                                    case "Delhi":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_delhi_districts, R.layout.spinner_layout);
                                        break;
                                    case "Jammu and Kashmir":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_jammu_kashmir_districts, R.layout.spinner_layout);
                                        break;
                                    case "Lakshadweep":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_lakshadweep_districts, R.layout.spinner_layout);
                                        break;
                                    case "Ladakh":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_ladakh_districts, R.layout.spinner_layout);
                                        break;
                                    case "Puducherry":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_puducherry_districts, R.layout.spinner_layout);
                                        break;
                                    case "other":
                                        city_adapter = ArrayAdapter.createFromResource(adapterView.getContext(),
                                                R.array.array_other_city, R.layout.spinner_layout);
                                        break;
                                    default:
                                        break;
                                }
                                city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     // Specify the layout to use when the list of choices appears
                                city_spinner.setAdapter(city_adapter);        //Populate the list of city in respect of the State selected

                                //To obtain the selected city from the spinner
                                city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                        selected_city = city_spinner.getSelectedItem().toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                    }
                                });
                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        profile_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* //below code will get the selected value instead of default gender value
                int genderID = rgp_gender.getCheckedRadioButtonId();
                rbtn_gender = findViewById(genderID);
                rgp_gender.check(rbtn1_gender.getId());
                gender = rbtn_gender.getText().toString();

                if(gender.equals("Male")){
                    other_gender = "Female";
                }else if(gender.equals("Female")){
                    other_gender = "Male";
                }*/
                //below code will get the selected value instead of default gender value


                if (selected_creator.equals("Profile created by")) {
                    Toast.makeText(Profile_Create_bkp.this, "Profile created by", Toast.LENGTH_LONG).show();
                } else if (selected_mstatus.equals("Select marriage status")) {
                    Toast.makeText(Profile_Create_bkp.this, "Please select your marriage status", Toast.LENGTH_LONG).show();
                } else if (selected_age.equals("Select your age")) {
                    Toast.makeText(Profile_Create_bkp.this, "Please select your age", Toast.LENGTH_LONG).show();
                } else if (selected_height.equals("Select your height")) {
                    Toast.makeText(Profile_Create_bkp.this, "Please select your height", Toast.LENGTH_LONG).show();
                }else if (selected_country.equals("Select Your country")) {
                    Toast.makeText(Profile_Create_bkp.this, "Please select your country from the list", Toast.LENGTH_LONG).show();
                } else if (selected_state.equals("Select Your State")) {
                    Toast.makeText(Profile_Create_bkp.this, "Please select your state from the list", Toast.LENGTH_LONG).show();
                } else if (selected_city.equals("Select Your District")) {
                    Toast.makeText(Profile_Create_bkp.this, "Please select your City from the list", Toast.LENGTH_LONG).show();
                } else {


                    //start uploading the data to firestore

                    String name = profilename_ref.getText().toString();
                    //String age = userage_ref.getText().toString();
                    String phoneno = user_phoneno_ref.getText().toString();
                    horizontal_progress_bar.setVisibility(View.VISIBLE);

                    DocumentReference user_doc = db.collection("users").document(uid);
                    //Profile_model profile = new Profile_model(uid,customid,gender, String.valueOf(downloaduri), profilename, age, phoneno);
                    Map<String, Object> user_details = new HashMap<>();
                    user_details.put("pic1", String.valueOf(downloaduri1));
                    user_details.put("pic2", String.valueOf(downloaduri2));
                    user_details.put("pic3", String.valueOf(downloaduri3));
                    user_details.put("photo_hide", photo_hide);
                    //user_details.put("gender", gender);
                    user_details.put("other_gender", other_gender);
                    user_details.put("name", name);
                    user_details.put("customid", customid);
                    user_details.put("uid", uid);
                    user_details.put("phoneno", phoneno);
                    user_details.put("creator", selected_creator);
                    user_details.put("mstatus", selected_mstatus);
                    user_details.put("age", selected_age);
                    user_details.put("height", selected_height);
                    user_details.put("country", selected_country);
                    user_details.put("state", selected_state);
                    user_details.put("city", selected_city);

                    user_doc.set(user_details).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Profile_Create_bkp.this, "Profile  upload success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Profile_Create_bkp.this, HomeActivity.class));
                            finish();
                            horizontal_progress_bar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(Profile_Create_bkp.this, "Profile upload failed", Toast.LENGTH_SHORT).show();
                            horizontal_progress_bar.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "" + e.toString());
                        }
                    });
                    //start uploading the data to firestore
                }
            }
        });

    }


    //Below code will allow user to select photo from local storage with crop option
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK || data != null || data.getData() != null) {

                if (requestCode == PICK_IMAGE1) {

                    Uri profileimageuri1 = data.getData();
                    profilepic_ref1.setImageURI(profileimageuri1);
                    uploadpicfstore1(profileimageuri1);
                    progressBar.setVisibility(View.VISIBLE);

                }
                if (requestCode == PICK_IMAGE2) {

                    Uri profileimageuri2 = data.getData();
                    profilepic_ref2.setImageURI(profileimageuri2);
                    uploadpicfstore2(profileimageuri2);
                    progressBar.setVisibility(View.VISIBLE);

                }
                if (requestCode == PICK_IMAGE3) {

                    Uri profileimageuri3 = data.getData();
                    profilepic_ref3.setImageURI(profileimageuri3);
                    uploadpicfstore3(profileimageuri3);
                    progressBar.setVisibility(View.VISIBLE);

                }
            }

        } catch (Exception e) {
            Toast.makeText(Profile_Create_bkp.this, "Error with picture" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadpicfstore1(Uri profileimageuri1) {

        StorageReference uploadpicref = storageReference.child("users/" + uid + "/profilepic1.jpg");
        uploadpicref.putFile(profileimageuri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile_Create_bkp.this, "Profile pic upload success", Toast.LENGTH_SHORT).show();
                uploadpicref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    //load(uri) is brought from below line,
                    public void onSuccess(Uri uri) {
                        // Picasso.get().load(uri).into(profilepic_ref);
                        downloaduri1 = uri;
                        //below line will add the uri link to downloaduri string which  is used to upload to firestore DB
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile_Create_bkp.this, "Profile pic upload failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void uploadpicfstore2(Uri profileimageuri2) {

        StorageReference uploadpicref = storageReference.child("users/" + uid + "/profilepic2.jpg");
        uploadpicref.putFile(profileimageuri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile_Create_bkp.this, "Profile pic upload success", Toast.LENGTH_SHORT).show();
                uploadpicref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    //load(uri) is brought from below line,
                    public void onSuccess(Uri uri) {
                        // Picasso.get().load(uri).into(profilepic_ref);
                        downloaduri2 = uri;
                        //below line will add the uri link to downloaduri string which  is used to upload to firestore DB
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile_Create_bkp.this, "Profile pic upload failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void uploadpicfstore3(Uri profileimageuri3) {

        StorageReference uploadpicref = storageReference.child("users/" + uid + "/profilepic3.jpg");
        uploadpicref.putFile(profileimageuri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile_Create_bkp.this, "Profile pic upload success", Toast.LENGTH_SHORT).show();
                uploadpicref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    //load(uri) is brought from below line,
                    public void onSuccess(Uri uri) {
                        // Picasso.get().load(uri).into(profilepic_ref);
                        downloaduri3 = uri;
                        //below line will add the uri link to downloaduri string which  is used to upload to firestore DB
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile_Create_bkp.this, "Profile pic upload failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        //this will create new document and update custom ID & uid


        final DocumentReference customid_reff = db.collection("userid").document("id_generator");
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(customid_reff);
                long new_customno = snapshot.getLong("customno") + 1;
                transaction.update(customid_reff, "customno", new_customno);
                transaction.update(customid_reff, "uid", uid);
                customid = "M" + new_customno;

                /*DocumentReference user_doc = db.collection("users").document(uid);
                Map<String, Object> add_customID = new HashMap<>();
                add_customID.put("customid", customid);
                add_customID.put("uid", uid);
                user_doc.set(add_customID);*/

                return null;
            }

        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success & the custom ID is " + customid);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });
        /*--------customid-----------*/

    }
}