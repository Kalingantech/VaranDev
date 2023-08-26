package com.example.varandev.Login;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.varandev.HomeActivity;
import com.example.varandev.Match.Userprofile_Activity;
import com.example.varandev.Match.Userprofile_photo_adapter;
import com.example.varandev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class My_profile_Activity extends AppCompatActivity {

    //personal info
    private TextView customid_ref,creator_ref,name_ref, age_ref,mstatus_ref,city_ref,state_ref,country_ref,native_ref,height_ref,body_type_ref,phoneno_ref;
    private String customid,creator,name, age,mstatus,city,state, country,user_native,height,body_type,phoneno;

    //professional info
    private TextView education_ref,education_details_ref,employment_ref,occupation_ref,employment_details_ref,salary_ref;
    private String education,education_details,employment,occupation,employment_details,salary;

    //Religious & family info
    private TextView nakshatram_ref,rasi_ref,dosham_ref,family_status_ref,father_name_ref,father_occupation_ref,mother_name_ref,mother_occupation_ref,brothers_ref,brothers_married_ref,sisters_ref,sisters_married_ref;
    private String nakshatram,rasi,dosham,family_status,father_name,father_occupation,mother_name,mother_occupation,brothers,brothers_married,sisters,sisters_married;

    //Habits & partner preferences info
    private TextView eating_habits_ref,drinking_habits_ref,smoking_habits_ref,pref_mstatus_ref,pref_age_min_ref,pref_age_max_ref,pref_height_min_ref,pref_height_max_ref,pref_employment_ref,pref_salary_ref,pref_expectation_ref;
    private String eating_habits,drinking_habits,smoking_habits,pref_mstatus,pref_age_min,pref_age_max,pref_height_min,pref_height_max,pref_employment,pref_salary,pref_expectation;

    //other declarations
    private FirebaseAuth fauth;
    private String my_uid,shortlist, shortlisted, shortlist_buttonText, pic1, pic2, horoscope, my_id, my_url, my_name, my_age;
    private Uri profileimageuri;
    private FirebaseFirestore db;
    private Button photo_edit_btn;
    private ImageButton personal_edit_btn,profession_edit_btn,family_edit_btn,pref_edit_btn;
    public int blur_id, new_blur_id,phoneno_hide_status;
    private String[] imageUrls;
    ViewPager viewPager;
    Userprofile_photo_adapter userprofile_photo_adapter;
    private Uri downloaduri;
    private FirebaseUser mcurrentuser;
    private StorageReference storageReference;
private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        fauth = FirebaseAuth.getInstance();
        mcurrentuser = fauth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        String my_uid = mcurrentuser.getUid();

        //edit buttons:
        imageView = findViewById(R.id.xml_profile_singleimagae);
        photo_edit_btn = findViewById(R.id.xml_edit_photo);
        personal_edit_btn =findViewById(R.id.xml_edit_personal);
        profession_edit_btn =findViewById(R.id.xml_edit_professional);
        family_edit_btn =findViewById(R.id.xml_edit_family);
        pref_edit_btn =findViewById(R.id.xml_edit_preferences);



        //personal info
        customid_ref =findViewById(R.id.xml_profile_id);
        creator_ref =findViewById(R.id.xml_profile_creator);
        name_ref = findViewById(R.id.xml_profile_name);
        age_ref = findViewById(R.id.xml_profile_age);
        mstatus_ref = findViewById(R.id.xml_profile_mstatus);
        city_ref = findViewById(R.id.xml_profile_city);
        state_ref = findViewById(R.id.xml_profile_state);
        country_ref = findViewById(R.id.xml_profile_country);
        native_ref = findViewById(R.id.xml_profile_native);
        height_ref = findViewById(R.id.xml_profile_height);
        body_type_ref = findViewById(R.id.xml_profile_body_type);
        phoneno_ref = findViewById(R.id.xml_profile_phoneno);

        //professional info
        education_ref = findViewById(R.id.xml_profile_education);
        education_details_ref = findViewById(R.id.xml_profile_education_details);
        employment_ref = findViewById(R.id.xml_profile_employment);
        occupation_ref = findViewById(R.id.xml_profile_occupation);
        employment_details_ref = findViewById(R.id.xml_profile_employment_details);
        salary_ref = findViewById(R.id.xml_profile_salary);
        //Religious & family info
        nakshatram_ref = findViewById(R.id.xml_profile_nakshtram);
        rasi_ref = findViewById(R.id.xml_profile_rasi);
        dosham_ref = findViewById(R.id.xml_profile_dosham);


        family_status_ref = findViewById(R.id.xml_profile_family_status);
        father_name_ref =findViewById(R.id.xml_profile_father_name);
        father_occupation_ref = findViewById(R.id.xml_profile_father_ocupation);
        mother_name_ref =findViewById(R.id.xml_profile_mother_name);
        mother_occupation_ref = findViewById(R.id.xml_profile_mother_ocupation);
        brothers_ref = findViewById(R.id.xml_profile_brothers);
        brothers_married_ref = findViewById(R.id.xml_profile_brothers_married);
        sisters_ref = findViewById(R.id.xml_profile_sisters);
        sisters_married_ref = findViewById(R.id.xml_profile_sisters_married);
        //Habits & partner preferences info
        eating_habits_ref = findViewById(R.id.xml_profile_eating_habits);
        drinking_habits_ref = findViewById(R.id.xml_profile_drinking_habits);
        smoking_habits_ref = findViewById(R.id.xml_profile_smoking_habits);

        pref_mstatus_ref = findViewById(R.id.xml_profile_pref_mstatus);
        pref_age_min_ref = findViewById(R.id.xml_profile_pref_age_min);
        pref_age_max_ref = findViewById(R.id.xml_profile_pref_age_max);
        pref_height_min_ref = findViewById(R.id.xml_profile_pref_height_min);
        pref_height_max_ref = findViewById(R.id.xml_profile_pref_height_max);
        pref_employment_ref = findViewById(R.id.xml_profile_pref_employment);
        pref_salary_ref = findViewById(R.id.xml_profile_pref_salary);
        pref_expectation_ref = findViewById(R.id.xml_profile_pref_expectation);

        /*-------------------download otherUser profile details from cache-----------------------*/
        DocumentReference documentReference = db.collection("users").document(my_uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();

                    //Photo info
                    pic1 = task.getResult().getString("pic1");
                    pic2 = task.getResult().getString("pic2");
                    blur_id = task.getResult().getLong("photo_hide").intValue();
                    horoscope = task.getResult().getString("horoscope");

                    //personal info
                    customid = task.getResult().getString("customid");
                    creator = task.getResult().getString("creator");
                    name = task.getResult().getString("name");
                    age = task.getResult().getString("age");
                    mstatus = task.getResult().getString("mstatus");
                    city = task.getResult().getString("city");
                    state = task.getResult().getString("state");
                    country = task.getResult().getString("country");
                    user_native = task.getResult().getString("user_native");
                    height = task.getResult().getString("height");
                    body_type = task.getResult().getString("body_type");
                    phoneno = task.getResult().getString("phoneno");
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
                    List<String> dosham = (List<String>)task.getResult().get("dosham");
                    family_status = task.getResult().getString("family_status");
                    father_name = task.getResult().getString("father_name");
                    father_occupation = task.getResult().getString("father_occupation");
                    mother_name = task.getResult().getString("mother_name");
                    mother_occupation = task.getResult().getString("mother_occupation");
                    brothers = task.getResult().getString("brothers");
                    brothers_married = task.getResult().getString("brothers_married");
                    sisters = task.getResult().getString("sisters");
                    sisters_married = task.getResult().getString("sisters_married");

                    //Habits
                    eating_habits = task.getResult().getString("eating_habits");
                    drinking_habits = task.getResult().getString("drinking_habits");
                    smoking_habits = task.getResult().getString("smoking_habits");

                    //partner preferences info
                    pref_mstatus = task.getResult().getString("preferred_mstatus");
                    pref_age_min = task.getResult().getString("preferred_min_age");
                    pref_age_max = task.getResult().getString("preferred_max_age");
                    pref_height_min = task.getResult().getString("preferred_min_height");
                    pref_height_max = task.getResult().getString("preferred_max_height");
                    pref_employment = task.getResult().getString("preferred_employment");
                    pref_salary = task.getResult().getString("preferred_salary");
                    pref_expectation = task.getResult().getString("preferred_expectation");

                    loadphoto(pic1,pic2);


                    Glide.with(My_profile_Activity.this)
                            .load(pic1)
                            .centerCrop()
                            .placeholder(R.drawable.profile_pic)
                            .into(imageView);

                    //personal info
                    customid_ref.setText(customid);
                    creator_ref.setText(creator);
                    name_ref.setText(name);
                    age_ref.setText(age);
                    mstatus_ref.setText(mstatus);
                    city_ref.setText(city);
                    state_ref.setText(state);
                    country_ref.setText(country);
                    native_ref.setText(user_native);
                    height_ref.setText(height);
                    body_type_ref.setText(body_type);
                    phoneno_ref.setText(phoneno);

                    //professional info
                    education_ref.setText(education);
                    education_details_ref.setText(education_details);
                    employment_ref.setText(employment);
                    occupation_ref.setText(occupation);
                    employment_details_ref.setText(employment_details);
                    salary_ref.setText(salary);

                    //Religious & family info
                    nakshatram_ref.setText(nakshatram);
                    rasi_ref.setText(rasi);

                    String delim = ",";
                    String dosham_strings = String.join(delim, dosham);
                    dosham_ref.setText(dosham_strings);

                    family_status_ref.setText(family_status);
                    father_name_ref.setText(father_name);
                    father_occupation_ref.setText(father_occupation);
                    mother_name_ref.setText(mother_name);
                    mother_occupation_ref.setText(mother_occupation);
                    brothers_ref.setText(brothers);
                    brothers_married_ref.setText(brothers_married);
                    sisters_ref.setText(sisters);
                    sisters_married_ref.setText(sisters_married);
                    //Habits & partner preferences info
                    eating_habits_ref.setText(eating_habits);
                    drinking_habits_ref.setText(drinking_habits);
                    smoking_habits_ref.setText(smoking_habits);

                    pref_mstatus_ref.setText(pref_mstatus);
                    pref_age_min_ref.setText(pref_age_min);
                    pref_age_max_ref.setText(pref_age_max);
                    pref_height_min_ref.setText(pref_height_min);
                    pref_height_max_ref.setText(pref_height_max);
                    pref_employment_ref.setText(pref_employment);
                    pref_salary_ref.setText(pref_salary);
                    pref_expectation_ref.setText(pref_expectation);
                }
            }
        });
        /*-------------------End of download otherUser profile details from cache-----------------------*/


        personal_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(My_profile_Activity.this, Profile_update_Page1.class));
                finish();
            }
        });
        profession_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(My_profile_Activity.this, Profile_update_Page2.class));
                finish();
            }
        });
        family_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(My_profile_Activity.this, Profile_update_Page3.class));
                finish();
            }
        });
        pref_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(My_profile_Activity.this, Profile_update_Page4.class));
                finish();
            }
        });

        photo_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(My_profile_Activity.this, Profile_update_Page5.class));
                finish();
            }
        });




    }

    public void loadphoto(String pic1,String pic2) {

        int blur_id = 1;

        if (!pic2.equals("null")){
            //means pic2 is null
            imageUrls = new String[]{String.valueOf(pic1), String.valueOf(pic2)};
        }else if (!pic1.equals("null")){
            imageUrls = new String[]{String.valueOf(pic1)};
        }else if (pic1.equals("null")){
            imageUrls = new String[]{String.valueOf(pic1)};
        }


        Glide.with(My_profile_Activity.this)
                .load(pic1)
                .centerCrop()
                .placeholder(R.drawable.profile_pic)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //load_horoscope(horoscope);
                //Dialog dialog = new Dialog(Userprofile_Activity.this);Theme_Wallpaper_NoTitleBar_Fullscreen
                //Theme_DeviceDefault_Light_NoActionBar_Fullscreen this is full screen with white background
                //Theme_Wallpaper_NoTitleBar_Fullscreen full screen with transparent background
                //Theme_DeviceDefault_NoActionBar_Overscan not bad
                //Dont use Theme_DeviceDefault_NoActionBar_TranslucentDecor
                //Theme_Material_NoActionBar_Fullscreen - dark bcakground
                Dialog dialog = new Dialog(My_profile_Activity.this, android.R.style.Theme_Material_NoActionBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.view_profile_pic);
                dialog.setCancelable(false);

                ViewPager viewPager = (ViewPager) dialog.findViewById(R.id.xml_profile_pic_pager_new);
                Userprofile_photo_adapter adapter = new Userprofile_photo_adapter(getApplicationContext(), imageUrls, blur_id);
                viewPager.setAdapter(adapter);

                Button horoscope_close = (Button) dialog.findViewById(R.id.horoscope_close);
                horoscope_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });


    }

}