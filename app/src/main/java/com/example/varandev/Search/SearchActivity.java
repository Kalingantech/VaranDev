package com.example.varandev.Search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.varandev.HomeActivity;
import com.example.varandev.Mail.MailActivity;
import com.example.varandev.Match.MatchActivity;
import com.example.varandev.Match.Match_Tab_Layout.Recycler_List_Adapter;
import com.example.varandev.Match.Match_Tab_Layout.Recycler_card_Adapter;
import com.example.varandev.Match.Match_Tab_Layout.Recycler_item_model;
import com.example.varandev.Match.Match_Tab_Layout.Recycler_item_paging_model;
import com.example.varandev.Match.Userprofile_Activity;
import com.example.varandev.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SearchActivity extends AppCompatActivity {

    private EditText search_ID_text;
    private FirebaseAuth newauth;
    private FirebaseUser mcurrentuser;
    private Button search_ID_btn, search_filter_btn;
    private String search_id, my_uid, custom_id, other_gender;
    private Spinner mstatus_spinner, age_min_spinner, age_max_spinner, dosham_spinner, employment_spinner;
    private String selected_mstatus, selected_age_min, selected_age_max, selected_dosham;
    private ArrayAdapter<CharSequence> mstatus_adapter, age_min_adapter, age_max_adapter, dosham_adapter, employment_adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressBar progressBar;

    private Recycler_List_Adapter list_adapter;
    ArrayList<Recycler_item_paging_model> dataList;
    Date last_login;

    String[] selected_employment;

    private Recycler_card_Adapter New_adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        recyclerView = findViewById(R.id.recycervw_search_result);
        recyclerView.setVisibility(View.GONE);
        progressBar = findViewById(R.id.xml_progressbar);

        mstatus_spinner = findViewById(R.id.xml_search_mstatus);
        age_min_spinner = findViewById(R.id.xml_search_min_age);
        age_max_spinner = findViewById(R.id.xml_search_max_age);
        dosham_spinner = findViewById(R.id.xml_search_dosham);
        employment_spinner = findViewById(R.id.xml_search_employment);
        search_ID_text = findViewById(R.id.xml_search_id);
        search_ID_btn = findViewById(R.id.xml_search_id_btn);
        search_filter_btn = findViewById(R.id.xml_search_filter);


        /*dataList = new ArrayList<>();
        list_adapter = new Recycler_List_Adapter(dataList);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(list_adapter);*/

        CollectionReference collectionReference = db.collection("users");

        dataList = new ArrayList<>();
        list_adapter = new Recycler_List_Adapter(dataList,getApplicationContext());
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recyclerView.setAdapter(list_adapter);


        Source fromCache = Source.CACHE;

        search_ID_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_id = search_ID_text.getText().toString();
                Toast.makeText(SearchActivity.this, "ID" + " " + search_id + " " + "searching", Toast.LENGTH_SHORT).show();

                //Toast.makeText(SearchActivity.this, "ID"+search_id+"not found", Toast.LENGTH_SHORT).show();
                db.collection("users")
                        .whereEqualTo("customid", search_id)
                        .whereEqualTo("gender", other_gender)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    Toast.makeText(SearchActivity.this, "task successful", Toast.LENGTH_SHORT).show();

                                    for (QueryDocumentSnapshot single_doc : queryDocumentSnapshots) {
                                        if (single_doc.exists()) {
                                            Toast.makeText(SearchActivity.this, "ID" + " " + search_id + " " + "found", Toast.LENGTH_SHORT).show();
                                            String others_userid = single_doc.getString("uid");

                                            Intent intent = new Intent(SearchActivity.this, Userprofile_Activity.class);
                                            intent.putExtra("others_userid", others_userid);
                                            startActivity(intent);
                                        }
                                    }
                                } else {
                                    Toast.makeText(SearchActivity.this, "ID" + " " + search_id + " " + " not found", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(SearchActivity.this, "ID" + " " + search_id + " " + "not found", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });


        /*---------------------spinner mstatus--------------------------------*/
        mstatus_adapter = ArrayAdapter.createFromResource(this, R.array.search_mstatus_array, R.layout.spinner_layout);
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


        /*---------------------age_min_spinner--------------------------------*/
        age_min_adapter = ArrayAdapter.createFromResource(this, R.array.age_min_array, R.layout.spinner_layout);
        age_min_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        age_min_spinner.setAdapter(age_min_adapter);
        age_min_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_age_min = age_min_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*-------------------------age_min_spinner---------------------------*/


        /*---------------------age_max_spinner--------------------------------*/
        age_max_adapter = ArrayAdapter.createFromResource(this, R.array.age_max_array, R.layout.spinner_layout);
        age_max_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        age_max_spinner.setAdapter(age_max_adapter);
        age_max_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_age_max = age_max_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        /*-------------------------age_max_spinner---------------------------*/


        /*---------------------dosham_spinner--------------------------------*/

        //"No Dosham","chevva dosham", "rahu-kethu dosham", "kalasharpam dosham","Naga dosham"
        dosham_adapter = ArrayAdapter.createFromResource(this, R.array.Dosham_filter_array, R.layout.spinner_layout);
        dosham_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dosham_spinner.setAdapter(dosham_adapter);
        dosham_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_dosham = dosham_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /*-------------------------dosham_spinner---------------------------*/

        /*---------------------employment_spinner--------------------------------*/

        employment_adapter = ArrayAdapter.createFromResource(this, R.array.Employment_filter_array, R.layout.spinner_layout);
        employment_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employment_spinner.setAdapter(employment_adapter);
        employment_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String employment = employment_spinner.getSelectedItem().toString();

                if (employment.equals("Any")) {
                    selected_employment = new String[]{"Goverment", "Private", "Defence", "Self Employed", "Business", "Not working"};
                } else {
                    selected_employment = new String[]{employment};
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /*-------------------------employment_spinner---------------------------*/


        search_filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                findViewById(R.id.xml_search_ll).setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                collectionReference.whereEqualTo("mstatus", selected_mstatus)
                        .whereEqualTo("gender", other_gender)
                        .whereGreaterThanOrEqualTo("age", selected_age_min)
                        .whereLessThanOrEqualTo("age", selected_age_max)
                        .whereArrayContains("dosham", selected_dosham)
                        .whereIn("employment",Arrays.asList(selected_employment))
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot documentSnapshot:list){
                                    Recycler_item_paging_model object = documentSnapshot.toObject(Recycler_item_paging_model.class);
                                    dataList.add(object);

                                }

                                list_adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                Log.d("logging","downloded docs");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d("logging","failed downloding docs");
                    }
                });


                /*------------Do no delete -----code to search and get data from server*/

               /* Query query = collectionReference
                        .whereEqualTo("mstatus", selected_mstatus)
                        .whereEqualTo("gender", other_gender)
                        .whereGreaterThanOrEqualTo("age", selected_age_min)
                        .whereLessThanOrEqualTo("age", selected_age_max)
                        .whereArrayContains("dosham", selected_dosham)
                        .whereIn("employment",Arrays.asList(selected_employment));

                FirestoreRecyclerOptions<Recycler_item_model> recycle_options = new FirestoreRecyclerOptions.Builder<Recycler_item_model>()
                        .setQuery(query, Recycler_item_model.class)
                        .build();

                New_adapter = new Recycler_card_Adapter(recycle_options) {
                    @Override
                    public void onDataChanged() {
                        progressBar.setVisibility(View.GONE);
                        super.onDataChanged();
                    }


                };
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(New_adapter);
                New_adapter.startListening();

                New_adapter.setonItemclicklisterner(new Recycler_card_Adapter.single_item_click_interface() {
                    @Override
                    public void onitemclick(DocumentSnapshot documentSnapshot, int position) {
                        Recycler_item_model item_model = documentSnapshot.toObject(Recycler_item_model.class);
                        Log.d("R_cycle_loader", "position");
                        String others_userid = documentSnapshot.getId();
                        Intent intent = new Intent(getApplicationContext(), Userprofile_Activity.class);
                        intent.putExtra("others_userid", others_userid);
                        startActivity(intent);
                    }
                });*/

                /*------------Do no delete -----code to search and get data from server*/

               /*Task task1 = db.collection("users")
                        .whereEqualTo("mstatus", selected_mstatus)
                        .whereEqualTo("gender", other_gender)
                        .whereGreaterThanOrEqualTo("age", selected_age_min)
                        .whereLessThanOrEqualTo("age", selected_age_max)
                        .whereArrayContains("dosham",selected_dosham)
                        .get();


                Task<List<QuerySnapshot>> listTask = Tasks.whenAllSuccess(task1);
                listTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
                    @Override
                    public void onSuccess(List<QuerySnapshot> querySnapshots) {

                        for (QuerySnapshot queryDocumentSnapshots : querySnapshots){
                            for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                Recycler_item_model object = documentSnapshot.toObject(Recycler_item_model.class);
                                dataList.add(object);
                            }
                        }
                        list_adapter.notifyDataSetChanged();
                    }
                });*/

            }
        });


        /*----Start of bottom nav*/
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_btm);
        bottomNavigationView.setSelectedItemId(R.id.Search);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
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
                        break;
                }
                return true;
            }
        });
        /*----End of bottom nav*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        newauth = FirebaseAuth.getInstance();
        mcurrentuser = newauth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        String my_uid = mcurrentuser.getUid();

        DocumentReference documentReference = db.collection("users").document(my_uid);
        documentReference.get(Source.CACHE).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    custom_id = task.getResult().getString("customid");
                    other_gender = task.getResult().getString("other_gender");
                }
            }

        });
    }

    @Override
    public void onBackPressed() {

        if (findViewById(R.id.xml_search_ll).getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.GONE);
            //do no delete below code, on backpress dataset/filter output was not changing so clearning the list for fresh fetch
            dataList.clear();
            list_adapter.notifyDataSetChanged();

            findViewById(R.id.xml_search_ll).setVisibility((View.VISIBLE));
        } else if (findViewById(R.id.xml_search_ll).getVisibility() == View.VISIBLE) {
            Intent Premium_intent = new Intent(SearchActivity.this, HomeActivity.class);
            startActivity(Premium_intent);
            overridePendingTransition(0, 0);
            super.onBackPressed();
        }

    }
}