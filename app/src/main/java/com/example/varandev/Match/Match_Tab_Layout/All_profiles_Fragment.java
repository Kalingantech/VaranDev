package com.example.varandev.Match.Match_Tab_Layout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.CombinedLoadStates;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.varandev.Match.Match_Tab_Layout.Recycler_card_Adapter;

import com.example.varandev.Match.Userprofile_Activity;
import com.example.varandev.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static android.content.Context.MODE_PRIVATE;

public class All_profiles_Fragment extends Fragment {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String my_url, my_name, my_id, other_gender;
    private String my_uid;
    private FirebaseAuth fauth;
    private FirebaseUser mcurrentuser;
    private Recycler_card_Adapter New_adapter;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fauth = FirebaseAuth.getInstance();
        mcurrentuser = fauth.getCurrentUser();
        my_uid = fauth.getCurrentUser().getUid();
        CollectionReference collectionReference = db.collection("users");

        // Inflate the layout for this fragment
        Source fromCache = Source.CACHE;
        Log.d("First_Frag_Lifecycle", "onCreateView");

        View view = inflater.inflate(R.layout.fragment_all_profiles, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.xml_recyclervview);
        progressBar = view.findViewById(R.id.xml_progressbar);

        //if there is no such preference saves it will take default valuw as give i.e"1" here
        Date defaultdate = new Date();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("timestamp",MODE_PRIVATE);
        Date newdate = new Date(sharedPreferences.getLong("timestamp",defaultdate.getTime()));
        Log.d("sharedpref",String.valueOf(newdate));





        //This is will check if user has created the proile already
        DocumentReference documentReference = db.collection("users").document(my_uid);
        documentReference.get(Source.CACHE).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    my_url = task.getResult().getString("pic1");
                    my_name = task.getResult().getString("username");
                    my_id = task.getResult().getString("customid");
                    other_gender = task.getResult().getString("other_gender");
                    Date newdate1 = task.getResult().getDate("timestamp");
                    Log.d("sharedpref_F",String.valueOf(newdate1));
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
/*

                Date date = new Date();
                //Toast.makeText(getContext(),date.toString(),Toast.LENGTH_LONG).show();
                //we are using FirestoreUI which means recycler adapter provided by Firestore. Not normal recylcer adapter.

                Query query = collectionReference
                        .whereEqualTo("gender",String.valueOf(other_gender)).limit(5);
                        //.whereGreaterThan("timestamp",newdate);


                FirestoreRecyclerOptions<Recycler_item_model> recycle_options = new FirestoreRecyclerOptions.Builder<Recycler_item_model>()
                        .setQuery(query,Recycler_item_model.class)
                        .build();


                New_adapter = new Recycler_card_Adapter(recycle_options){
                    @Override
                    public void onDataChanged() {
                        progressBar.setVisibility(View.INVISIBLE);
                        super.onDataChanged();
                    }
                };
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(New_adapter);
                //New_adapter.startListening();

               New_adapter.setonItemclicklisterner(new Recycler_card_Adapter.single_item_click_interface() {
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

*/

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
        //New_adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
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
        //New_adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
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