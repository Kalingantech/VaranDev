package com.example.varandev.Match;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.varandev.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class Userprofile_photo_adapter extends PagerAdapter {

    private Context context;
    private String[] imageUrls;
    private LayoutInflater layoutInflater;
    private int blur_id;

    public Userprofile_photo_adapter(Context context, String[] imageUrls, int blur_id) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.blur_id = blur_id;
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootview = layoutInflater.inflate(R.layout.activity_userprofile_photo_model,container,false);
        PhotoView photoView = rootview.findViewById(R.id.xml_user_photo);
        TextView texview = rootview.findViewById(R.id.xml_user_photo_txt);
        if (blur_id ==25){

            /*Picasso.get()
                    .load(imageUrls[position])
                    .placeholder(R.drawable.profile_pic)
                    .transform(new BlurTransformation(context,blur_id,1))
                    .error(R.drawable.profile_pic)
                    .into(photoView);*/

            Picasso.get()
                    .load(R.drawable.blur_image)
                    .placeholder(R.drawable.blur_image)
                    .error(R.drawable.blur_image)
                    .into(photoView);

            /*Glide.with(context)
                    .load(imageUrls[position])
                    .error(R.drawable.profile_pic)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photoView);*/

        }else if (blur_id ==1){
            Picasso.get()
                    .load(imageUrls[position])
                    .placeholder(R.drawable.profile_pic)
                    .error(R.drawable.profile_pic)
                    .into(photoView);
        }
        rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        texview.setText(String.valueOf(position+1)+ "/" +imageUrls.length);
        container.addView(rootview);
        return rootview;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }
}
