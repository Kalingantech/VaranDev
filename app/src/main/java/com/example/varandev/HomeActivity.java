package com.example.varandev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.varandev.Backups.shared_pref;
import com.example.varandev.Login.Signin_Activity;
import com.example.varandev.Login.Profile_Create_Page1;
import com.example.varandev.Login.My_profile_Activity;
import com.example.varandev.Mail.MailActivity;
import com.example.varandev.Match.MatchActivity;
import com.example.varandev.Search.SearchActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView messageView;
    Button chg_language;
    Context context;
    Resources resources;

    private CircleImageView profilepic_ref;
    private TextView name_ref;
    private FirebaseAuth newauth;
    private FirebaseUser mcurrentuser;
    private Button logoutbtn, viewprofilebtn, pic_play_btn, shared_pref_btn, show_string_btn, delete_profile_btn;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private String current_language;

    public String my_name;
    public String my_url;
    public String my_id;
    public String my_uid;
    public String other_gender;
    public Date last_login;
    private Button create_prof_btn, chg_lang_show;
    private ImageButton drawer_nav_btn;
    private EditText edit_text;

    boolean[] selectdosham;
    ArrayList<Integer> doshamlist = new ArrayList<>();
    String[] doshamarray = {"No Dosham", "chevva dosham", "rahu-kethu dosham", "kalasharpam dosham", "Naga dosham"};
    List<String> doclist = new ArrayList<String>();

    ListenerRegistration listenerRegistration;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    //drawbacks of share prefs
//This causes an issue when your app evolves. For example, your User class may today have age and weight and name, but in the future, you may want to add gender, address, and title. This will be problematic to handle as you need to write extra code to handle the old version, otherwise, your app will crash.

    //old SH1 key :C6:A9:3A:4D:D8:BB:9D:BE:D3:17:A5:CD:49:D9:C2:3D:17:0D:7D:7E


    //reduce read count:update login date on spashscreen. based on "created date", download only newl created/updated docs only.
    //use snapshot listener to get update deletes instantly
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load language (make sure this method is called first before intiating the setContentView(textvie,buttons etc.)
        //loadLocale();
        LanguageManager languageManager = new LanguageManager(HomeActivity.this);
        languageManager.updateResource(languageManager.getLanguage());
        current_language = languageManager.getLanguage();

        setContentView(R.layout.activity_home);

        //dev to test tracker
        /*1.reject_btn change it to gone instead of invisible and change wiehgt to 1
          2.Language manager testing to be done*/

        profilepic_ref = findViewById(R.id.xml_profile_pic);
        name_ref = findViewById(R.id.xml_profile_name);
        viewprofilebtn = findViewById(R.id.xml_view_profile_btn);
        show_string_btn = findViewById(R.id.xml_show_string);
        edit_text = findViewById(R.id.xml_edit_text);

        newauth = FirebaseAuth.getInstance();
        mcurrentuser = newauth.getCurrentUser();
        logoutbtn = findViewById(R.id.logout_btn);
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        chg_language = findViewById(R.id.xml_chg_lang);
        messageView = findViewById(R.id.xml_messageView);
        chg_lang_show = findViewById(R.id.xml_chg_lang_show);


        create_prof_btn = findViewById(R.id.xml_create_prof);
        pic_play_btn = findViewById(R.id.xml_pic_play);
        shared_pref_btn = findViewById(R.id.xml_shared_pref);
        delete_profile_btn = findViewById(R.id.xml_delete_profile);
        drawerLayout = findViewById(R.id.drawer_nav_layout);
        navigationView = findViewById(R.id.nav_view);
        drawer_nav_btn = findViewById(R.id.xml_drawer);


        my_uid = mcurrentuser.getUid();
        //this is required for proper signout of google account on method "logoutbtn"
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);
        //this is required for proper signout of google account on method "logoutbtn"

        //Toast.makeText(getApplicationContext(), date.toString(), Toast.LENGTH_LONG).show();

        //load side navigation viewer
        load_drawer_nav();


        //check network connection
        checkconnection();

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
        /*---This code will stop users from typing numbers---*/


        chg_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (languageManager.getLanguage().equals("en")) {
                    LanguageManager languageManager = new LanguageManager(HomeActivity.this);
                    languageManager.updateResource("ta");
                    recreate();
                } else if (languageManager.getLanguage().equals("ta")) {
                    LanguageManager languageManager = new LanguageManager(HomeActivity.this);
                    languageManager.updateResource("en");
                    recreate();
                }

            }
        });

        chg_lang_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_text.setFilters(Textfilters);
                Toast.makeText(getApplicationContext(),edit_text.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        shared_pref_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), shared_pref.class));
            }
        });


        //in prod add this listerner to all 3 activities
       /*db.collection("users")
                //.whereEqualTo("gender",other_gender)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.w("snapshotlistener", "Listen failed.", error);
                    return;
                }
                for(DocumentChange dc:value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        Toast.makeText(getApplicationContext(),"doc_added",Toast.LENGTH_SHORT).show();
                    }
                }
                for(DocumentChange dc:value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.MODIFIED){
                        Toast.makeText(getApplicationContext(),"doc_modified",Toast.LENGTH_SHORT).show();
                    }
                }
                //if a user profile is deleted, then
                for(DocumentChange dc:value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.REMOVED){
                        Toast.makeText(getApplicationContext(),"doc_removed",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });*/


        create_prof_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] email_array = {
                        "girl_dcd@gmail.com",
                        "girl_dce@gmail.com",
                        "girl_dcf@gmail.com",
                        "girl_dcg@gmail.com",
                        "girl_dda@gmail.com",
                        "girl_ddb@gmail.com",
                        "girl_ddc@gmail.com",
                        "girl_ddd@gmail.com",
                        "girl_dde@gmail.com",
                        "girl_ddf@gmail.com",
                        "girl_ddg@gmail.com",
                        "girl_dea@gmail.com",
                        "girl_deb@gmail.com",
                        "girl_dec@gmail.com",
                        "girl_ded@gmail.com",
                        "girl_dee@gmail.com",
                        "girl_def@gmail.com",
                        "girl_deg@gmail.com",
                        "girl_dfa@gmail.com",
                        "girl_dfb@gmail.com",
                };
                for (String emailid : email_array) {
                    String password = "sathish";
                    newauth.createUserWithEmailAndPassword(emailid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String newuserid = task.getResult().getUser().getUid();
                                Toast.makeText(HomeActivity.this, "email ID" + emailid + "User uid" + newuserid, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "new user detail " + newuserid);

                                /*--------customid-----------*/


                                final DocumentReference customid_reff = db.collection("userid").document("id_generator");
                                db.runTransaction(new Transaction.Function<Void>() {
                                    @Override
                                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                        DocumentSnapshot snapshot = transaction.get(customid_reff);
                                        long new_customno = snapshot.getLong("customno") + 1;
                                        transaction.update(customid_reff, "customno", new_customno);
                                        transaction.update(customid_reff, "uid", newuserid);
                                        String customid = "M" + new_customno;

                                        DocumentReference user_doc = db.collection("users").document(newuserid);
                                        Map<String, Object> add_customID = new HashMap<>();
                                        add_customID.put("customid", customid);
                                        add_customID.put("uid", newuserid);
                                        user_doc.set(add_customID);

                                        return null;
                                    }

                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //after profile creation success


                                        /*--------customid-----------*/

                                        String profilename = emailid;
                                        String age = "25";
                                        String phoneno = "66698547";
                                        String[] doshamarray = {"no dosham"};
                                        int num = 1;
                                        List<String> selected_dosham = Arrays.asList(doshamarray);


                                        DocumentReference user_doc = db.collection("users").document(newuserid);
                                        Map<String, Object> user_details = new HashMap<>();

                                        user_details.put("pic1", "null");
                                        user_details.put("pic2", "null");
                                        user_details.put("horoscope", "null");
                                        user_details.put("photo_hide", num);

                                        user_details.put("creator", "self");
                                        user_details.put("gender", "Female");
                                        user_details.put("other_gender", "Male");
                                        user_details.put("name", profilename);
                                        user_details.put("age", age);
                                        user_details.put("mstatus", "unmarried");
                                        user_details.put("height", "150");
                                        user_details.put("weight", "40");
                                        user_details.put("body_type", null);
                                        user_details.put("phoneno", phoneno);

                                        user_details.put("country", null);
                                        user_details.put("state", null);
                                        user_details.put("city", null);
                                        user_details.put("native", null);
                                        user_details.put("citizenship", null);

                                        //below details updated in profile create page 2
                                        user_details.put("education", null);
                                        user_details.put("education_details", null);
                                        user_details.put("employment", "Defence");
                                        user_details.put("occupation", null);
                                        user_details.put("employment_details", null);
                                        user_details.put("salary", null);

                                        user_details.put("nakshatram", null);
                                        user_details.put("rasi", null);
                                        user_details.put("dosham", selected_dosham);

                                        //below details updated in profile create page 3
                                        user_details.put("family_type", null);
                                        user_details.put("family_status", null);
                                        user_details.put("father_occupation", null);
                                        user_details.put("mother_occupation", null);
                                        user_details.put("brothers", null);
                                        user_details.put("brothers_married", null);
                                        user_details.put("sisters", null);
                                        user_details.put("sisters_married", null);
                                        user_details.put("eating_habits", null);
                                        user_details.put("drinking_habits", null);
                                        user_details.put("smoking_habits", null);

                                        //below details updated in profile create page 4
                                        user_details.put("preferred_mstatus", null);
                                        user_details.put("preferred_min_age", null);
                                        user_details.put("preferred_max_age", null);
                                        user_details.put("preferred_min_height", null);
                                        user_details.put("preferred_max_height", null);
                                        user_details.put("preferred_employment", null);
                                        user_details.put("preferred_salary", null);
                                        user_details.put("preferred_expectation", null);


                                        user_doc.update(user_details).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(HomeActivity.this, "Profile  create success", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                        //after profile creation success
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Transaction failure.", e);
                                            }
                                        });


                            } else {
                                Toast.makeText(HomeActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


        viewprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, My_profile_Activity.class));
            }
        });

        pic_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), Auth_play.class));

                //below code is update all doc in a collection

                //HPHr7alcYOZqKXwQgn29dfZataL2
                //db.collection("users").document("JrChsW39MngaGD5mYAFrfT6YPNP2").delete();
                db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String doc_id = documentSnapshot.getId();

                            DocumentReference user_doc = db.collection("users").document(doc_id);
                            Map<String, Object> user_details = new HashMap<>();
                            user_details.put("user_native", "user_native");
                            user_details.put("native", FieldValue.delete());
                            user_doc.update(user_details);

                        }

                    }
                });

                /*db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            String doc_id = documentSnapshot.getId();
                            Log.d("snap",doc_id);
                            DocumentReference user_doc = db.collection("users").document(doc_id);
                            Map<String, Object> user_details = new HashMap<>();
                            user_details.put("timestamp", FieldValue.delete());
                            user_doc.update(user_details);

                        }

                    }
                });*/


            }
        });


        delete_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("shortlist").document(my_uid).collection("my_shortlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String my_short_list = documentSnapshot.getId();
                            db.collection("shortlist").document(my_short_list).collection("shortlisted_me").document(my_uid).delete();
                            db.collection("shortlist").document(my_uid).collection("my_shortlist").document(my_short_list).delete();
                        }

                    }
                });

                db.collection("shortlist").document(my_uid).collection("shortlisted_me").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String shortlisted_me_list = documentSnapshot.getId();
                            db.collection("shortlist").document(shortlisted_me_list).collection("my_shortlist").document(my_uid).delete();
                            db.collection("shortlist").document(my_uid).collection("shortlisted_me").document(shortlisted_me_list).delete();
                        }

                    }
                });

                db.collection("requests").document(my_uid).collection("sent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String sent_list = documentSnapshot.getId();
                            db.collection("requests").document(sent_list).collection("received").document(my_uid).delete();
                            db.collection("requests").document(my_uid).collection("sent").document(sent_list).delete();
                        }

                    }
                });

                db.collection("requests").document(my_uid).collection("received").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String rec_list = documentSnapshot.getId();
                            db.collection("requests").document(rec_list).collection("sent").document(my_uid).delete();
                            db.collection("requests").document(my_uid).collection("received").document(rec_list).delete();
                        }

                    }
                });

                StorageReference profilepic1 = storageReference.child("users").child(my_uid).child("profilepic1.jpg");
                StorageReference profilepic2 = storageReference.child("users").child(my_uid).child("profilepic2.jpg");
                StorageReference horoscope = storageReference.child("users").child(my_uid).child("horoscope.jpg");

                profilepic1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("pic_delete", "deleted");
                    }
                });
                profilepic2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("pic_delete", "deleted");
                    }
                });
                ;
                horoscope.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("pic_delete", "deleted");
                    }
                });


                db.collection("users").document(my_uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        newauth.signOut();
                        //logout  google account and send back to signin.
                        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                finish();
                                startActivity(new Intent(HomeActivity.this, Signin_Activity.class));
                            }
                        });
                        sendusertologin();
                    }
                });


            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //logout  firebase account
                newauth.signOut();
                //logout  google account and send back to signin.
                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        finish();
                        startActivity(new Intent(HomeActivity.this, Signin_Activity.class));
                    }
                });
                sendusertologin();
            }
        });

        show_string_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });







/*

        selectdosham = new boolean[doshamarray.length];
        multi_select_spinner_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                builder.setTitle("savu");
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

        */
        /*---------------------spinner height--------------------------------*//*

        height_adapter = ArrayAdapter.createFromResource(this, R.array.Nakshatram_array, R.layout.spinner_layout);
        height_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        height_spinner.setAdapter(height_adapter);
        height_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_height_temp = height_spinner.getSelectedItem().toString();
                //Below code will split "210 cm/6ft 11in" based on space
                String[] selected_height_array1 = selected_height_temp.split("\\s*-\\s*");
                //Below code will get the first string "210" from  "210 cm/6ft 11in" when ordering alphabarically
                String selected_height = Collections.min(Arrays.asList(selected_height_array1));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

*/


        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_btm);
        /*----Start of bottom nav*/
        bottomNavigationView.setSelectedItemId(R.id.Home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Home:
                        break;
                    case R.id.Profile:
                        startActivity(new Intent(getApplicationContext(), MatchActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.Mail:
                        startActivity(new Intent(getApplicationContext(), MailActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.Search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });
        /*----End of bottom nav*/

    }


    private void load_drawer_nav() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        drawer_nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
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
        }
    }


    /*----To check login status start---*/
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Home Lifecycle", "onStart");
        //check if this is new user
        if (mcurrentuser == null) {
            sendusertologin();
        } else {
            String uid = mcurrentuser.getUid();
            //This is will check if user has created the proile already
            DocumentReference documentReference = db.collection("users").document(uid);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists()) {
                        my_url = task.getResult().getString("pic1");
                        my_name = task.getResult().getString("name");
                        my_id = task.getResult().getString("customid");
                        my_uid = task.getResult().getString("uid");
                        other_gender = task.getResult().getString("other_gender");
                        last_login = task.getResult().getDate("login");
                        //Picasso.get().load(url).into(profilepic_ref);
                        //Glide helps us to provide a default pic if there is no pic uploaded to firestore
                        Glide.with(getApplicationContext())
                                .load(my_url)
                                .thumbnail(0.5f)
                                .error(R.drawable.profile_pic)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(profilepic_ref);

                        name_ref.setText(my_name);

                    } else {
                        startActivity(new Intent(HomeActivity.this, Profile_Create_Page1.class));
                        finish();
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    Query query_download = db.collection("users");
                    query_download.whereEqualTo("gender", String.valueOf(other_gender))
                            .get(Source.CACHE)
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        boolean isEmpty = task.getResult().isEmpty();

                                        if (isEmpty) {
                                            db.collection("users")
                                                    .whereEqualTo("gender", String.valueOf(other_gender))
                                                    .get(Source.SERVER).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    Toast.makeText(getApplicationContext(), "downloading all", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        } else {
                                            db.collection("users")
                                                    .whereEqualTo("gender", String.valueOf(other_gender))
                                                    .whereGreaterThan("created", last_login)
                                                    .get(Source.SERVER);
                                            Toast.makeText(getApplicationContext(), "downloading recent docs", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            });
                }
            });
        }

    }

    public static InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String blockCharacterSet = "O1234567890";
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };


    private void sendusertologin() {
        Intent loginintent = new Intent(HomeActivity.this, Signin_Activity.class);
        loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginintent);
        finish();
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d("Home Lifecycle", "onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Home Lifecycle", "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Home Lifecycle", "onDestroy");
    }

    @Override
    public void onBackPressed() {
        //this is if the nav drawer is open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //this is if the nav drawer is closed
            super.onBackPressed();
            finishAffinity();
            finish();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rate_draw_nav:
                Toast.makeText(getApplicationContext(), "rate", Toast.LENGTH_SHORT).show();
                break;             /*--because we are already in home--*/

            case R.id.share_draw_nav:
               /* Intent sharingintent = new Intent(Intent.ACTION_SEND);;
                sharingintent.setType("text/plain");
                String shareBody="https://play.google.com/store/apps/details?id=com.kalingantech.tnpsctopper";
                sharingintent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(sharingintent,"Share using"));*/
                Toast.makeText(getApplicationContext(), "rate", Toast.LENGTH_SHORT).show();
                break;

            case R.id.other_app:
                //startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/developer?id=Kalingan+Tech")));
                Toast.makeText(getApplicationContext(), "rate", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}