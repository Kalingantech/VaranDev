package com.example.varandev.Match;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.varandev.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class Userprofile_photo_adapter_bkp extends PagerAdapter {

    private Context context;
    private String[] imageUrls;
    private LayoutInflater layoutInflater;
    private int blur_id;

    public Userprofile_photo_adapter_bkp(Context context, String[] imageUrls, int blur_id) {
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
        /*ImageView imageView = new ImageView(context);
        Picasso.get()
                .load(imageUrls[position])
                .placeholder(R.drawable.profile_pic)
                .error(R.drawable.profile_pic)
                .into(imageView);
        container.addView(imageView);*/

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootview = layoutInflater.inflate(R.layout.activity_userprofile_photo_model,container,false);
        ImageView imageView = rootview.findViewById(R.id.xml_user_photo);
        TextView texview = rootview.findViewById(R.id.xml_user_photo_txt);
        Picasso.get()
                .load(imageUrls[position])
                .placeholder(R.drawable.profile_pic)
                .transform(new BlurTransformation(context,blur_id,1))
                .error(R.drawable.profile_pic)
                .into(imageView);
        texview.setText(String.valueOf(position+1)+ "/" +imageUrls.length);
        container.addView(rootview);
        return rootview;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }
}
