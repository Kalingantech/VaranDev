package com.example.varandev.Match.Match_Tab_Layout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.varandev.Match.Horoscope_Activity;
import com.example.varandev.Match.MatchActivity;
import com.example.varandev.Match.Userprofile_Activity;
import com.example.varandev.R;
import com.google.common.util.concurrent.Service;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Recycler_List_Adapter extends RecyclerView.Adapter<Recycler_List_Adapter.myviewholder> {

    Context context;
    ArrayList<Recycler_item_paging_model> datalist;

    public Recycler_List_Adapter(ArrayList<Recycler_item_paging_model> datalist,Context context) {
        this.datalist = datalist;
        this.context = context;
    }

    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rview = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_model, parent, false);
        return new myviewholder(rview);

    }

    @Override
    public void onBindViewHolder( Recycler_List_Adapter.myviewholder holder, int position) {

        holder.customid_ref.setText(datalist.get(position).getCustomid());
        holder.name_ref.setText(datalist.get(position).getName());
        holder.age_ref.setText(datalist.get(position).getAge());
        String uid = datalist.get(position).getUid();
        String imageurl = datalist.get(position).getPic1();
        //int blur_id = model.getBlur_id();

/*        Picasso.get().load(imageurl)
                .error(R.drawable.profile_pic)
                .placeholder(R.drawable.profile_pic)
                .into(holder.profilepic_ref);*/

        Glide.with(context)
                .load(imageurl)
                .centerCrop()
                .placeholder(R.drawable.profile_pic)
                .into(holder.profilepic_ref);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Userprofile_Activity.class);
                intent.putExtra("others_userid", uid);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        private CircleImageView profilepic_ref;
        private TextView customid_ref, name_ref, age_ref;

        public myviewholder(View itemView) {
            super(itemView);
            customid_ref =itemView.findViewById(R.id.xml_rcycle_customid);
            profilepic_ref = itemView.findViewById(R.id.xml_profile_pic);
            name_ref = itemView.findViewById(R.id.xml_rcycle_name);
            age_ref = itemView.findViewById(R.id.xml_rcycle_age);


        }

        /*@Override
        implements View.OnClickListener
        itemView.setOnClickListener(this);
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), Userprofile_Activity.class);
            intent.putExtra("others_userid", uid);
            view.getContext().startActivity(intent);
        }*/
    }



}
