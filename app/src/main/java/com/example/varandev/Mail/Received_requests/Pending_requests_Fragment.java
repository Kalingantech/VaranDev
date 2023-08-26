package com.example.varandev.Mail.Received_requests;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.varandev.Mail.Recycler_card_Adapter_requests;
import com.example.varandev.Mail.Recycler_item_model_requests;
import com.example.varandev.Match.Match_Tab_Layout.Recycler_card_Adapter;
import com.example.varandev.Match.Match_Tab_Layout.Recycler_item_model;
import com.example.varandev.Match.Userprofile_Activity;
import com.example.varandev.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class Pending_requests_Fragment extends Fragment {


    private FirebaseAuth fauth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Recycler_card_Adapter R_adaper;
    private String uid;
    private ProgressBar progressBar;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fauth = FirebaseAuth.getInstance();
        uid = fauth.getCurrentUser().getUid();

        CollectionReference collectionReference = db.collection("requests").document(uid).collection("received");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mail_received_pending, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.xml_recyclervview_received_pending);
        progressBar = view.findViewById(R.id.xml_progressbar);
        textView =view.findViewById(R.id.xml_listisempty);
        textView.setVisibility(View.INVISIBLE);


// The "base query" is a query with no startAt/endAt/limit clauses that the adapter can use
// to form smaller queries for each page. It should only include where() and orderBy() clauses
        Query query = collectionReference.whereEqualTo("show_cancel_interest","received");

        FirestoreRecyclerOptions<Recycler_item_model> recycle_options = new FirestoreRecyclerOptions.Builder<Recycler_item_model>()
                .setQuery(query, Recycler_item_model.class)
                .build();


        R_adaper = new Recycler_card_Adapter(recycle_options){

            @Override
            public void onDataChanged() {
                if(getItemCount() == 0 ){
                    textView.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.INVISIBLE);
                super.onDataChanged();
            }

        };;
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(R_adaper);
        R_adaper.startListening();


        R_adaper.setonItemclicklisterner(new Recycler_card_Adapter.single_item_click_interface() {
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


        return view;
    }
}