package com.example.varandev.Backups;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.denzcoskun.imageslider.ImageSlider;
import com.example.varandev.Match.Userprofile_photo_adapter;
import com.example.varandev.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import ro.alexmamo.firestore_document.FirestoreDocument;

public class Userprofile_Activity_bkp extends AppCompatActivity {

    private String userid;
    //private ImageView profilepic_ref;
    private ImageSlider profilepic_ref;
    //personal info
    private TextView customid_ref,creator_ref,name_ref, age_ref,mstatus_ref,city_ref,state_ref,country_ref,native_ref,citizenship_ref,height_ref,weight_ref,body_type_ref,phoneno_ref;
    private String customid,creator,name, age,mstatus,city,state, country,user_native,citizenship,height,weight,body_type,phoneno;

    //professional info
    private TextView education_ref,education_details_ref,employment_ref,occupation_ref,employment_details_ref,salary_ref;
    private String education,education_details,employment,occupation,employment_details,salary;

    //Religious & family info
    private TextView nakshatram_ref,rasi_ref,dosham_ref,family_type_ref,family_status_ref,father_occupation_ref,mother_occupation_ref,brothers_ref,brothers_married_ref,sisters_ref,sisters_married_ref;
    private String nakshatram,rasi,dosham,family_type,family_status,father_occupation,mother_occupation,brothers,brothers_married,sisters,sisters_married;

    //Habits & partner preferences info
    private TextView eating_habits_ref,drinking_habits_ref,smoking_habits_ref,pref_mstatus_ref,pref_age_min_ref,pref_age_max_ref,pref_height_min_ref,pref_height_max_ref,pref_employment_ref,pref_salary_ref;
    private String eating_habits,drinking_habits,smoking_habits,pref_mstatus,pref_age_min,pref_age_max,pref_height_min,pref_height_max,pref_employment,pref_salary;

    //other declarations
    private FirebaseAuth fauth;
    private String my_uid,shortlist, shortlisted, shortlist_buttonText, pic1, pic2, horoscope, my_id, my_url, my_name, my_age;
    private Uri profileimageuri;
    private FirebaseFirestore db;
    private Button shortlist_unshortlist_btn, show_cancel_interest_btn,reject_btn,view_contact_btn,view_horoscope_btn;
    public int blur_id, new_blur_id,phoneno_hide_status;
    private String[] imageUrls;
    ViewPager viewPager;
    Userprofile_photo_adapter userprofile_photo_adapter;
    private Uri downloaduri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        /*------------------------------------------*/
        //get user id from recycler view
        String others_uid = getIntent().getStringExtra("others_userid");
        /*------------------------------------------*/

        profilepic_ref = findViewById(R.id.xml_profile_pic);

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
                view_contact_btn = findViewById(R.id.xml_view_contact);
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
                view_horoscope_btn = findViewById(R.id.xml_view_horoscope);


                family_status_ref = findViewById(R.id.xml_profile_family_status);
                father_occupation_ref = findViewById(R.id.xml_profile_father_ocupation);
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



        //other initiations
        reject_btn = findViewById(R.id.xml_reject);
        fauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        my_uid = fauth.getCurrentUser().getUid();
        shortlist_unshortlist_btn = findViewById(R.id.xml_prof_shortlist);
        show_cancel_interest_btn = findViewById(R.id.xml_show_cancel_interest);


        //do not play with interests button on using local cache. Use local cache for shortlists & user profile.
        Source fromCache = Source.CACHE;
        FirestoreDocument firestoreDocument = FirestoreDocument.getInstance();

        //set reject button to hidden and disabled first
        reject_btn.setVisibility(View.INVISIBLE);

        /*-------------------check if i have sent any request to that user already-----------------------*/
        DocumentReference chec_sent_Reference = db.collection("requests").document(my_uid).collection("sent").document(others_uid);
        chec_sent_Reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {

                    //if this document iss enabled whihc means already interest sent.

                    String show_interests = task.getResult().getString("show_cancel_interest");
                    new_blur_id = task.getResult().getLong("photo_hide_req").intValue();

                    //if other user has already accepted my request (no action just loading it)
                    if (show_interests.equals("Accepted")) {

                        show_cancel_interest_btn.setVisibility(View.VISIBLE);
                        show_cancel_interest_btn.setEnabled(true);
                        show_cancel_interest_btn.setText("Accepted");
                        //new_blur_id =1;

                    }

                    //if i have sent request to this user already. if "cancel Interest" is pressed it will be handled in other below method.
                    if (show_interests.equals("sent")) {
                        shortlist_unshortlist_btn.setVisibility(View.VISIBLE);
                        shortlist_unshortlist_btn.setEnabled(true);
                        show_cancel_interest_btn.setText("cancel Interest");

                    }

                    //if user send request and cancelled it , then it will be updated in DB as cancelled.
                    if (show_interests.equals("cancelled")) {
                        show_cancel_interest_btn.setText("show Interest");
                    }


                    //if other user has already rejected my request (no action just loading it)
                    if (show_interests.equals("Rejected")) {
                        show_cancel_interest_btn.setVisibility(View.VISIBLE);
                        show_cancel_interest_btn.setEnabled(true);
                        show_cancel_interest_btn.setText("Rejected");
                    }

                }
            }
        });
        /*-------------------End of check if i have sent any request to that user already-----------------------*/


        //
        /*-------------------check if i have shortlisted that user already-----------------------*/
        DocumentReference chec_shrtlist_Reference = db.collection("shortlist").document(my_uid).collection("my_shortlist").document(others_uid);
        chec_shrtlist_Reference.get(fromCache).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {

                    //checking only the shortlist status
                    /*----check if i have shortlisted other user profile---*/

                    String shortlist_bool = task.getResult().getBoolean("my_shortlist").toString().trim();

                    if (shortlist_bool.equals("true")) {
                        shortlist_unshortlist_btn.setText("shortlisted");
                    }
                    if (shortlist_bool.equals("false")) {
                        shortlist_unshortlist_btn.setText("shortlist");
                    }
                    /*----check if  have shortlisted other user profile---*/

                }
            }
        });
        /*-------------------End of "check if i have shortlisted that user already"-----------------------*/


        /*-------------------download otherUser profile details from cache-----------------------*/
        DocumentReference documentReference = db.collection("users").document(others_uid);
        documentReference.get(fromCache).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    //Log.d(TAG, "Cached data: " + document.getData());
                    //below 2 lines is to calculate the documment size
                    int documentSize = firestoreDocument.getSize(document);
                    //Toast.makeText(Userprofile_Activity.this, "documentSize" + documentSize, Toast.LENGTH_SHORT).show();

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
                    citizenship = task.getResult().getString("citizenship");
                    height = task.getResult().getString("height");
                    weight = task.getResult().getString("weight");
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
                    dosham = task.getResult().getString("dosham");
                    family_type = task.getResult().getString("family_type");
                    family_status = task.getResult().getString("family_status");
                    father_occupation = task.getResult().getString("father_occupation");
                    mother_occupation = task.getResult().getString("mother_occupation");
                    brothers = task.getResult().getString("brothers");
                    brothers_married = task.getResult().getString("brothers_married");
                    sisters = task.getResult().getString("sisters");
                    sisters_married = task.getResult().getString("sisters_married");

                    //Habits & partner preferences info
                    eating_habits = task.getResult().getString("eating_habits");
                    drinking_habits = task.getResult().getString("drinking_habits");
                    smoking_habits = task.getResult().getString("smoking_habits");
                    pref_mstatus = task.getResult().getString("pref_mstatus");
                    pref_age_min = task.getResult().getString("pref_age_min");
                    pref_age_max = task.getResult().getString("pref_age_max");
                    pref_height_min = task.getResult().getString("pref_height_min");
                    pref_height_max = task.getResult().getString("pref_height_max");
                    pref_employment = task.getResult().getString("pref_employment");
                    pref_salary = task.getResult().getString("pref_salary");

                    loadphoto(pic1,pic2,blur_id);
                    load_phoneno(phoneno);

                    /*Picasso.get().load(pic1)
                            .placeholder(R.drawable.profile_pic)
                            .error(R.drawable.profile_pic)
                            //below code will create blur imageview
                            .transform(new BlurTransformation(getApplicationContext(), 1, 1))
                            .into(profilepic_ref);*/

                    //Glide helps us to provide a default pic if there is no pic uploaded to firestore
                    /*Glide.with(Userprofile_Activity.this)
                            .load(url)
                            .placeholder(R.drawable.profile_pic)
                            .into(profilepic_ref);*/

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
                    citizenship_ref.setText(citizenship);
                    height_ref.setText(height);
                    weight_ref.setText(weight);
                    body_type_ref.setText(body_type);
                    //do not load phone number "phoneno_ref.setText(phoneno);"

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
                    dosham_ref.setText(dosham);
                    family_type_ref.setText(family_type);
                    family_status_ref.setText(family_status);
                    father_occupation_ref.setText(father_occupation);
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

                }
            }
        });
        /*-------------------End of download otherUser profile details from cache-----------------------*/


        /*-------------------download my_own profile details from cache-----------------------*/
        DocumentReference my_documentReference = db.collection("users").document(my_uid);
        my_documentReference.get(fromCache).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    //Log.d(TAG, "Cached data: " + document.getData());
                    //below 2 lines is to calculate the documment size
                    int documentSize = firestoreDocument.getSize(document);
                    //Toast.makeText(Userprofile_Activity.this, "documentSize" + documentSize, Toast.LENGTH_SHORT).show();

                    my_url = task.getResult().getString("pic1");
                    my_id = task.getResult().getString("customid");
                    my_name = task.getResult().getString("name");
                    my_age = task.getResult().getString("age");
                }
            }
        });
        /*------------------- End of download my_own profile details from cache-----------------------*/


        /*-------------------check if other user have sent any request to me already-----------------------*/
        DocumentReference chec_recieve_Reference = db.collection("requests").document(my_uid).collection("received").document(others_uid);
        chec_recieve_Reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {

                    //if this document iss enabled whihc means already interest received.

                    String show_interests = task.getResult().getString("show_cancel_interest");
                    new_blur_id = task.getResult().getLong("photo_hide_req").intValue();

                    //if i have received a request, yet to take action.
                    if (show_interests.equals("received")) {
                        show_cancel_interest_btn.setText("Accept");
                        reject_btn.setVisibility(View.VISIBLE);
                        reject_btn.setText("Reject");

                        //if request is accepted(Remember "show_cancel_interest_btn" is Accept button).
                        show_cancel_interest_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                show_cancel_interest_btn.setText("Accepted");
                                reject_btn.setVisibility(View.INVISIBLE);
                                //update it in DB
                                DocumentReference accept_interest_Reference1 = db.collection("requests").document(my_uid).collection("received").document(others_uid);
                                Map<String, Object> sent_map = new HashMap<>();
                                sent_map.put("show_cancel_interest", "Accepted");
                                sent_map.put("photo_hide_req", 1);
                                sent_map.put("timestamp", FieldValue.serverTimestamp());
                                accept_interest_Reference1.update(sent_map);

                                DocumentReference accept_interest_Reference2 = db.collection("requests").document(others_uid).collection("sent").document(my_uid);
                                Map<String, Object> rec_map = new HashMap<>();
                                rec_map.put("show_cancel_interest", "Accepted");
                                rec_map.put("photo_hide_req", 1);
                                rec_map.put("timestamp", FieldValue.serverTimestamp());
                                accept_interest_Reference2.update(rec_map);
                            }
                        });


                        //if request is Rejected (Remember "shortlist_unshortlist_btn" is reject button).
                        reject_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reject_btn.setVisibility(View.INVISIBLE);
                                show_cancel_interest_btn.setText("Rejected");
                                //update it in DB
                                DocumentReference reject_interest_Reference1 = db.collection("requests").document(my_uid).collection("received").document(others_uid);
                                Map<String, Object> sent_map = new HashMap<>();
                                sent_map.put("show_cancel_interest", "Rejected");
                                sent_map.put("photo_hide_req", 25);
                                sent_map.put("timestamp", FieldValue.serverTimestamp());
                                reject_interest_Reference1.update(sent_map);

                                DocumentReference reject_interest_Reference2 = db.collection("requests").document(others_uid).collection("sent").document(my_uid);
                                Map<String, Object> rec_map = new HashMap<>();
                                rec_map.put("show_cancel_interest", "Rejected");
                                rec_map.put("photo_hide_req", 25);
                                rec_map.put("timestamp", FieldValue.serverTimestamp());
                                reject_interest_Reference2.update(rec_map);

                            }
                        });


                    }

                    //if i have already accepted other users request.(no action just loading it)
                    if (show_interests.equals("Accepted")) {
                        show_cancel_interest_btn.setVisibility(View.VISIBLE);
                        show_cancel_interest_btn.setEnabled(true);
                        show_cancel_interest_btn.setText("Accepted");
                        //By changin below id value, we are allowing "Accepted" users can view photo
                        //new_blur_id = 1;

                    }

                    //if i have already Rejected other users request.(no action just loading it)
                    if (show_interests.equals("Rejected")) {
                        show_cancel_interest_btn.setVisibility(View.VISIBLE);
                        show_cancel_interest_btn.setEnabled(true);
                        show_cancel_interest_btn.setText("Rejected");
                        //new_blur_id = 25;
                    }

                }
            }
        });

        /*-------------------End of check if other user have sent any request to me already-----------------------*/

        //first time i am giving interest to other user
        show_cancel_interest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String interest_buttonText = show_cancel_interest_btn.getText().toString();

                if (interest_buttonText.equals("show Interest")) {

                    DocumentReference sent_Reference = db.collection("requests").document(my_uid).collection("sent").document(others_uid);
                    Map<String, Object> sent_map = new HashMap<>();
                    sent_map.put("show_cancel_interest", "sent");
                    sent_map.put("pic1", pic1);
                    sent_map.put("name", name);
                    sent_map.put("age", age);
                    sent_map.put("customid", customid);
                    sent_map.put("photo_hide_req", 25);
                    sent_map.put("timestamp", FieldValue.serverTimestamp());

                    sent_Reference.set(sent_map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            show_cancel_interest_btn.setText("cancel Interest");
                        }
                    });

                    DocumentReference recieve_Reference = db.collection("requests").document(others_uid).collection("received").document(my_uid);
                    Map<String, Object> rec_map = new HashMap<>();
                    rec_map.put("show_cancel_interest", "received");
                    rec_map.put("pic1", my_url);
                    rec_map.put("name", my_name);
                    rec_map.put("age", my_age);
                    rec_map.put("customid", my_id);
                    rec_map.put("photo_hide_req", 25);
                    rec_map.put("timestamp", FieldValue.serverTimestamp());
                    recieve_Reference.set(rec_map, SetOptions.merge());

                } else if (interest_buttonText.equals("cancel Interest")) {

                    //dont just delete this document, it will create null point reference when starting this page.
                    DocumentReference sent_Reference = db.collection("requests").document(my_uid).collection("sent").document(others_uid);
                    Map<String, Object> sent_map = new HashMap<>();
                    sent_map.put("show_cancel_interest", "cancelled");
                    sent_map.put("pic1", pic1);
                    sent_map.put("name", name);
                    sent_map.put("age", age);
                    sent_map.put("customid", customid);
                    sent_map.put("photo_hide_req", 25);
                    sent_map.put("timestamp", FieldValue.serverTimestamp());
                    sent_Reference.set(sent_map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            show_cancel_interest_btn.setText("show Interest");
                        }
                    });

                    DocumentReference recieve_Reference = db.collection("requests").document(others_uid).collection("received").document(my_uid);
                    Map<String, Object> rec_map = new HashMap<>();
                    rec_map.put("show_cancel_interest", "cancelled");
                    rec_map.put("pic1", my_url);
                    rec_map.put("name", my_name);
                    rec_map.put("customid", my_id);
                    rec_map.put("age", my_age);
                    rec_map.put("photo_hide_req", 25);
                    rec_map.put("timestamp", FieldValue.serverTimestamp());
                    recieve_Reference.set(rec_map, SetOptions.merge());
                }
            }
        });

        //first time i am using shortlist button for this user
        shortlist_unshortlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shortlist_buttonText = shortlist_unshortlist_btn.getText().toString();
                if (shortlist_buttonText.equals("shortlist")) {
                    shortlist_unshortlist_btn.setText("shortlisted");
                    //use update instead of set, to avoid all fields getting updated.
                    DocumentReference sent_Reference = db.collection("shortlist").document(my_uid).collection("my_shortlist").document(others_uid);
                    Map<String, Object> sent_map = new HashMap<>();
                    sent_map.put("my_shortlist", true);
                    sent_map.put("pic1", pic1);
                    sent_map.put("name", name);
                    sent_map.put("age", age);
                    sent_map.put("customid", customid);
                    sent_map.put("timestamp", FieldValue.serverTimestamp());
                    sent_Reference.set(sent_map, SetOptions.merge());

                    DocumentReference recieve_Reference = db.collection("shortlist").document(others_uid).collection("shortlisted_me").document(my_uid);
                    Map<String, Object> rec_map = new HashMap<>();
                    rec_map.put("shortlisted_me", true);
                    rec_map.put("pic1", my_url);
                    rec_map.put("name", my_name);
                    rec_map.put("customid", my_id);
                    rec_map.put("age", my_age);
                    rec_map.put("timestamp", FieldValue.serverTimestamp());
                    recieve_Reference.set(rec_map, SetOptions.merge());



                } else if (shortlist_buttonText.equals("shortlisted")) {
                    //use update instead of set, to avoid all fields getting updated.
                    shortlist_unshortlist_btn.setText("shortlist");
                    DocumentReference sent_Reference = db.collection("shortlist").document(my_uid).collection("my_shortlist").document(others_uid);
                    Map<String, Object> sent_map = new HashMap<>();
                    sent_map.put("my_shortlist", false);
                    sent_map.put("pic1", pic1);
                    sent_map.put("name", name);
                    sent_map.put("age", age);
                    sent_map.put("customid", customid);
                    sent_map.put("timestamp", FieldValue.serverTimestamp());
                    sent_Reference.set(sent_map, SetOptions.merge());

                    DocumentReference receive_Reference = db.collection("shortlist").document(others_uid).collection("shortlisted_me").document(my_uid);
                    Map<String, Object> rec_map = new HashMap<>();
                    rec_map.put("shortlisted_me", false);
                    rec_map.put("pic1", my_url);
                    rec_map.put("name", my_name);
                    rec_map.put("customid", my_id);
                    rec_map.put("age", my_age);
                    rec_map.put("timestamp", FieldValue.serverTimestamp());
                    receive_Reference.set(rec_map, SetOptions.merge());
                }
            }
        });

//view_horoscope_btn goes to next activity

        view_horoscope_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                load_horoscope(horoscope);





            }
        });


    }

    private void load_horoscope(String horoscope) {

        Dialog dialog = new Dialog(Userprofile_Activity_bkp.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_horoscope);
        PhotoView photoView = (PhotoView) dialog.findViewById(R.id.xml_view_horoscope_pop);
        Button horoscope_close = (Button) dialog.findViewById(R.id.horoscope_close);

        Glide.with(getApplicationContext())
                .load(horoscope)
                //.thumbnail(0.5f)
                .error(R.drawable.profile_pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);

        horoscope_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void load_phoneno(String phoneno) {

        //default setting
        phoneno_ref.setText("+91-XXXXXXXXXX");

        view_contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*------number & photo were inconsistent, this check resolves it------------*/
                String acceptence_check = show_cancel_interest_btn.getText().toString();

                if (acceptence_check.equals("Accepted")) {
                    phoneno_ref.setText(phoneno);

                }else if (acceptence_check.equals("show Interest")){
                    phoneno_ref.setText("+91-XXXXXXXXXX");
                    Toast.makeText(Userprofile_Activity_bkp.this,"send interest to view number", Toast.LENGTH_SHORT).show();

                }
                else if (acceptence_check.equals("cancel Interest")){
                    phoneno_ref.setText("+91-XXXXXXXXXX");
                    Toast.makeText(Userprofile_Activity_bkp.this,"Interest not accepted yet", Toast.LENGTH_SHORT).show();

                }else if (acceptence_check.equals("Rejected")){
                    phoneno_ref.setText("+91-XXXXXXXXXX");
                    Toast.makeText(Userprofile_Activity_bkp.this,"your Interest rejected by user", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    public int loadphoto(String pic1,String pic2,int blur_id) {


        Log.d("acceptence_check", String.valueOf(blur_id));
        Log.d("acceptence_check", String.valueOf(new_blur_id));

        /*

        *//*------number & photo were inconsistent, this check resolves it------------*//*


        if (acceptence_check.equals("Accepted")) {
            new_blur_id =1;
            Log.d("acceptence_check", "Accepted");
        }
        if (acceptence_check.equals("Rejected") || acceptence_check.equals("show Interest") || acceptence_check.equals("Accept") || acceptence_check.equals("cancel Interest")  ) {
            new_blur_id =25;
            Log.d("acceptence_check", "one of the above");
        }

*/
        /*------number & photo were inconsistent, this check resolves it------------*/

        //if accptted new_blur_id was set to 1 and will be able view the photo, if not it depends on user setting.
        if (blur_id == 1)  {

            if(!pic2.equals("null")) {
                imageUrls = new String[]{String.valueOf(pic1),String.valueOf(pic2)};
                viewPager = findViewById(R.id.xml_profile_pic_pager);
                Userprofile_photo_adapter adapter = new Userprofile_photo_adapter(getApplicationContext(), imageUrls, blur_id);
                viewPager.setAdapter(adapter);
                Log.d("loadphoto", "pic 2 available");
            } else if (pic2.equals("null") && !pic1.equals("null")){
                imageUrls = new String[]{String.valueOf(pic1)};
                viewPager = findViewById(R.id.xml_profile_pic_pager);
                Userprofile_photo_adapter adapter = new Userprofile_photo_adapter(getApplicationContext(), imageUrls, blur_id);
                viewPager.setAdapter(adapter);
                Log.d("loadphoto", "all pics are null");}
            else if (pic2.equals("null") && pic1.equals("null")){
                imageUrls = new String[]{String.valueOf(pic1)};
                viewPager = findViewById(R.id.xml_profile_pic_pager);
                Userprofile_photo_adapter adapter = new Userprofile_photo_adapter(getApplicationContext(), imageUrls, blur_id);
                viewPager.setAdapter(adapter);
                Log.d("loadphoto", "pic 1 not null");}

        } else if (blur_id == 25){
            //Means if user hass hidden his photo in general
            //if request is approved
            if(new_blur_id ==1){

                blur_id =1;
                if(!pic2.equals("null")) {
                    imageUrls = new String[]{String.valueOf(pic1),String.valueOf(pic2)};
                    viewPager = findViewById(R.id.xml_profile_pic_pager);
                    Userprofile_photo_adapter adapter = new Userprofile_photo_adapter(getApplicationContext(), imageUrls, blur_id);
                    viewPager.setAdapter(adapter);
                    Log.d("loadphoto", "pic 2 available");
                } else if (pic2.equals("null") && !pic1.equals("null")){
                    imageUrls = new String[]{String.valueOf(pic1)};
                    viewPager = findViewById(R.id.xml_profile_pic_pager);
                    Userprofile_photo_adapter adapter = new Userprofile_photo_adapter(getApplicationContext(), imageUrls, blur_id);
                    viewPager.setAdapter(adapter);
                    Log.d("loadphoto", "all pics are null");}
                else if (pic2.equals("null") && pic1.equals("null")){
                    imageUrls = new String[]{String.valueOf(pic1)};
                    viewPager = findViewById(R.id.xml_profile_pic_pager);
                    Userprofile_photo_adapter adapter = new Userprofile_photo_adapter(getApplicationContext(), imageUrls, blur_id);
                    viewPager.setAdapter(adapter);
                    Log.d("loadphoto", "pic 1 not null");}

            }

            //if new_blur_id is not defined anywhere it take 0 as value, so keep as it is (or) if new_blur_id == 25, then request pending/rejected
            if((new_blur_id == 0) || (new_blur_id == 25)){

                 if(!pic2.equals("null")) {
                    imageUrls = new String[]{String.valueOf(pic1),String.valueOf(pic2)};
                    viewPager = findViewById(R.id.xml_profile_pic_pager);
                    Userprofile_photo_adapter adapter = new Userprofile_photo_adapter(getApplicationContext(), imageUrls, blur_id);
                    viewPager.setAdapter(adapter);
                    Log.d("loadphoto", "pic 2 available");
                } else if (pic2.equals("null") && !pic1.equals("null")){
                    imageUrls = new String[]{String.valueOf(pic1)};
                    viewPager = findViewById(R.id.xml_profile_pic_pager);
                    Userprofile_photo_adapter adapter = new Userprofile_photo_adapter(getApplicationContext(), imageUrls, blur_id);
                    viewPager.setAdapter(adapter);
                    Log.d("loadphoto", "all pics are null");}
                else if (pic2.equals("null") && pic1.equals("null")){
                    imageUrls = new String[]{String.valueOf(pic1)};
                    viewPager = findViewById(R.id.xml_profile_pic_pager);
                    Userprofile_photo_adapter adapter = new Userprofile_photo_adapter(getApplicationContext(), imageUrls, blur_id);
                    viewPager.setAdapter(adapter);
                    Log.d("loadphoto", "pic 1 not null");}
            }
        }
            //return a int
        return 1;
}

}