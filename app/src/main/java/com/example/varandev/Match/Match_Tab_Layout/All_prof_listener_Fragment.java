package com.example.varandev.Match.Match_Tab_Layout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.varandev.R;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class All_prof_listener_Fragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String my_url, my_name, my_id, other_gender;
    private String my_uid;
    private FirebaseAuth fauth;
    private FirebaseUser mcurrentuser;
    private Recycler_List_Adapter list_adapter;
    ArrayList<Recycler_item_paging_model> dataList;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        fauth = FirebaseAuth.getInstance();
        mcurrentuser = fauth.getCurrentUser();
        my_uid = fauth.getCurrentUser().getUid();
        CollectionReference collectionReference = db.collection("users");
        Source fromCache = Source.CACHE;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_prof_listener, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.xml_recyclervview4);
        dataList = new ArrayList<>();
        list_adapter = new Recycler_List_Adapter(dataList, getActivity());


        progressDialog= new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();

       recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //recyclerView.setAdapter(list_adapter);
        String uid = mcurrentuser.getUid();

        collectionReference
                .whereEqualTo("gender",String.valueOf(other_gender))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                if(error != null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.e("fstore error",error.getMessage());
                    return;
                }

                for(DocumentChange dc: value.getDocumentChanges()){

                    if(dc.getType() == DocumentChange.Type.ADDED){
                        Recycler_item_paging_model object = dc.getDocument().toObject(Recycler_item_paging_model.class);
                        dataList.add(object);
                    }
                }

                for(DocumentChange dc: value.getDocumentChanges()){

                    if(dc.getType() == DocumentChange.Type.MODIFIED){
                        Recycler_item_paging_model object = dc.getDocument().toObject(Recycler_item_paging_model.class);
                        dataList.add(object);
                    }
                }

                list_adapter.notifyDataSetChanged();
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });

                /*.whereEqualTo("gender", String.valueOf(other_gender))
                .get(Source.CACHE)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list) {
                            Recycler_item_paging_model object = documentSnapshot.toObject(Recycler_item_paging_model.class);
                            dataList.add(object);
                        }

                        list_adapter.notifyDataSetChanged();
                    }
                });

*/
        return view;
    }
}