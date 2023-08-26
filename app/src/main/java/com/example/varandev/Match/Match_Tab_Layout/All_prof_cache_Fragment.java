package com.example.varandev.Match.Match_Tab_Layout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.varandev.HomeActivity;
import com.example.varandev.Login.Splash_Activity;
import com.example.varandev.Match.MatchActivity;
import com.example.varandev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class All_prof_cache_Fragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String my_url, my_name, my_id, other_gender;
    private String my_uid;
    private FirebaseAuth fauth;
    private FirebaseUser mcurrentuser;
    private Recycler_List_Adapter list_adapter;
    ArrayList<Recycler_item_paging_model> dataList;
    Date last_login;
    ProgressBar progressBar;
    CollectionReference collectionReference = db.collection("users");
    Source fromCache = Source.CACHE;
    ListenerRegistration registration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fauth = FirebaseAuth.getInstance();
        mcurrentuser = fauth.getCurrentUser();
        my_uid = fauth.getCurrentUser().getUid();




        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_prof_cache, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.xml_recyclervview4);

        dataList = new ArrayList<>();
        list_adapter = new Recycler_List_Adapter(dataList,getActivity());
        progressBar = view.findViewById(R.id.xml_progressbar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(list_adapter);






        DocumentReference documentReference = db.collection("users").document(my_uid);
        documentReference.get(fromCache).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    my_url = task.getResult().getString("pic1");
                    my_name = task.getResult().getString("name");
                    my_id = task.getResult().getString("customid");
                    my_uid = task.getResult().getString("uid");
                    other_gender=task.getResult().getString("other_gender");
                    last_login = task.getResult().getDate("login");
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                collectionReference
                        .whereEqualTo("gender",String.valueOf(other_gender))
                        .get(Source.CACHE)
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
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isComplete()){
                            Log.d("task","completed");
                        }
                    }
                });


            }
        });





        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}