package com.example.varandev.Backups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.CombinedLoadStates;
import androidx.paging.LoadState;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.varandev.Match.Match_Tab_Layout.Recycler_Paging_card_Adapter;
import com.example.varandev.Match.Match_Tab_Layout.Recycler_card_Adapter;
import com.example.varandev.Match.Match_Tab_Layout.Recycler_item_model;
import com.example.varandev.Match.Userprofile_Activity;
import com.example.varandev.R;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class All_profiles_Fragment_bkp extends Fragment {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Recycler_Paging_card_Adapter R_adaper;
    private String my_url, my_name, my_id, other_gender;
    private String uid;
    private FirebaseAuth fauth;
    private FirebaseUser mcurrentuser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fauth = FirebaseAuth.getInstance();
        mcurrentuser = fauth.getCurrentUser();
        uid = fauth.getCurrentUser().getUid();
        CollectionReference collectionReference = db.collection("users");

        // Inflate the layout for this fragment
        Source fromCache = Source.CACHE;
        Log.d("First_Frag_Lifecycle", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_all_profiles, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.xml_recyclervview);


        //This is will check if user has created the proile already
        DocumentReference documentReference = db.collection("users").document(uid);
        documentReference.get(fromCache).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    my_url = task.getResult().getString("pic1");
                    my_name = task.getResult().getString("username");
                    my_id = task.getResult().getString("customid");
                    other_gender = task.getResult().getString("other_gender");

                }
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                // The "base query" is a query with no startAt/endAt/limit clauses that the adapter can use
                // to form smaller queries for each page. It should work only include where() and orderBy() clauses

                //we are using FirestoreUI which means recycler adapter provided by Firestore. Not normal recylcer adapter.
                Query query = collectionReference
                        //.whereGreaterThan("timestamp", FieldValue.serverTimestamp())
                        .whereEqualTo("gender",String.valueOf(other_gender));

                PagingConfig config = new PagingConfig(/* page size */ 5, /* prefetchDistance */ 5,/* enablePlaceHolders */ false);

                FirestorePagingOptions<Recycler_item_model> r_options = new FirestorePagingOptions.Builder<Recycler_item_model>()
                        .setLifecycleOwner(getViewLifecycleOwner())
                        .setQuery(query, config, Recycler_item_model.class)
                        //.setQuery(query,config , Recycler_item_model.class)
                        .build();


                R_adaper = new Recycler_Paging_card_Adapter(r_options);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(R_adaper);


                //R_adaper.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.ALLOW);

                //R_adaper.startListening();


                R_adaper.addLoadStateListener(new Function1<CombinedLoadStates, Unit>() {
                    @Override
                    public Unit invoke(CombinedLoadStates combinedLoadStates) {
                        LoadState refresh = combinedLoadStates.getRefresh();
                        LoadState append = combinedLoadStates.getAppend();

                        if (refresh instanceof LoadState.Error || append instanceof LoadState.Error) {
                            Log.d("R_cycle_loader", "Load failed");
                        }

                        if (refresh instanceof LoadState.Loading) {
                            // The initial Load has begun
                            Log.d("R_cycle_loader", "Load started");
                        }

                        if (append instanceof LoadState.Loading) {
                            // The adapter has started to load an additional page
                            Log.d("R_cycle_loader", "Additional page loaded");
                        }

                        if (append instanceof LoadState.NotLoading) {
                            LoadState.NotLoading notLoading = (LoadState.NotLoading) append;
                            if (notLoading.getEndOfPaginationReached()) {
                                // The adapter has finished loading all of the data set
                                Log.d("R_cycle_loader", "Load completed");
                                return null;
                            }

                            if (refresh instanceof LoadState.NotLoading) {
                                // The previous load (either initial or additional) completed
                                // ...
                                return null;
                            }
                        }

                        return null;
                    }
                });

                R_adaper.setonItemclicklisterner(new Recycler_Paging_card_Adapter.single_item_click_interface() {
                    @Override
                    public void onitemclick(DocumentSnapshot documentSnapshot, int position) {
                        Recycler_item_model item_model = documentSnapshot.toObject(Recycler_item_model.class);
                        Log.d("R_cycle_loader", "position");
                        String others_userid = documentSnapshot.getId();
                        Intent intent = new Intent(getContext(), Userprofile_Activity.class);
                        intent.putExtra("others_userid", others_userid);
                        startActivity(intent);
                    }
                });





            }
        });




        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("First_Frag_Lifecycle", "onStart");


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("First_Frag_Lifecycle", "onPause");
        //this line will save the scroll position after coming back
        R_adaper.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("First_Frag_Lifecycle", "onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("First_Frag_Lifecycle", "onResume");
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("First_Frag_Lifecycle", "onSaveInstanceState");
        //this line will save the scroll position after coming back
        R_adaper.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
    }


    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("First_Frag_Lifecycle", "onDestroy");
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        Log.d("First_Frag_Lifecycle", "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("First_Frag_Lifecycle", "onDetach");
    }

}