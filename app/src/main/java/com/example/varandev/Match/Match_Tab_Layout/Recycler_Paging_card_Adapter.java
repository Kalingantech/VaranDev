package com.example.varandev.Match.Match_Tab_Layout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.varandev.R;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class Recycler_Paging_card_Adapter extends FirestorePagingAdapter<Recycler_item_model, Recycler_Paging_card_Adapter.itemviewholder> {


    private single_item_click_interface listener;

    public Recycler_Paging_card_Adapter(@NonNull @NotNull FirestorePagingOptions<Recycler_item_model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull Recycler_Paging_card_Adapter.itemviewholder holder, int position, @NonNull @NotNull Recycler_item_model model) {

        holder.customid_ref.setText(model.getCustomid());
        holder.name_ref.setText(model.getname());
        holder.age_ref.setText(model.getAge());

        String imageurl = model.getPic1();
        //int blur_id = model.getBlur_id();

        Picasso.get().load(imageurl)
                .error(R.drawable.profile_pic)
                .placeholder(R.drawable.profile_pic)
                .into(holder.profilepic_ref);

//Log.d("R_cycle_loader","Loaded data number" +getItemCount());
    }


    @NonNull
    @NotNull
    @Override
    public itemviewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View rview = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_model, parent, false);
        return new itemviewholder(rview);
    }




    //onholder start

    public class itemviewholder extends RecyclerView.ViewHolder {

        private CircleImageView profilepic_ref;
        private TextView customid_ref, name_ref, age_ref;

        public itemviewholder(@NonNull @NotNull View itemView) {
            super(itemView);

            customid_ref =itemView.findViewById(R.id.xml_rcycle_customid);
            profilepic_ref = itemView.findViewById(R.id.xml_profile_pic);
            name_ref = itemView.findViewById(R.id.xml_rcycle_name);
            age_ref = itemView.findViewById(R.id.xml_rcycle_age);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapter_position = getAdapterPosition();
                    if(adapter_position != RecyclerView.NO_POSITION && listener != null){
                        //listener.onitemclick(getSnapshots().getSnapshot(adapter_position), adapter_position);
                        listener.onitemclick(getItem(getAdapterPosition()),adapter_position);
                    }
                }
            });
        }
    }

    //outside view holder method
    public interface single_item_click_interface {
        void onitemclick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setonItemclicklisterner(single_item_click_interface listener) {
        this.listener = listener;
    }

}
