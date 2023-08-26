package com.example.varandev.Backups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.varandev.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Pic_play_bkp extends AppCompatActivity {

    private CircleImageView profilepic_ref1;
    private Button profile_save_btn;
    private FirebaseAuth fauth;
    private FirebaseUser mcurrentuser;
    private Uri profileimageuri1;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private String uid;
    private static final int PICK_IMAGE55 = 1;
    private static final int PICK_IMAGE56 = 1;
    private Uri downloaduri1;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_play);


        fauth = FirebaseAuth.getInstance();
        mcurrentuser = fauth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        uid = fauth.getCurrentUser().getUid();




        profilepic_ref1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE55);

            }
        });
        profilepic_ref1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE56);

            }
        });

    }


    //Below code will allow user to select photo from local storage with crop option
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK || data != null || data.getData() != null) {

                if (requestCode == PICK_IMAGE55) {
                    //get here once image is picked and send it to "resizeimage" method
                    resizeimage(data.getData());

                }
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
                    //get result from crop activity method "resizeimage"
                    CropImage.ActivityResult result =CropImage.getActivityResult(data);

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                    //go to method mark to write watermark on image
                    Bitmap newbitmap = mark(bitmap);

                    profilepic_ref1.setImageBitmap(newbitmap);
                    imageView.setImageBitmap(newbitmap);
                    //go to getImageUri method to get the uri from bitmap
                    Uri newuri = getImageUri(this,newbitmap);
                    //upload the uri to firestore..
                    uploadpicfstore1(newuri);

                }

            }

        } catch (Exception e) {
            Toast.makeText(Pic_play_bkp.this, "Error with picture" + e, Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap mark(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        //start paining on the image
        Paint paint = new Paint();
        paint.setTextSize(h/15);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        // this step will draw(write on the selected image and send result backup)
        canvas.drawText("Muthuraja Varan", w/3, (float) (w/1.3), paint);
        return result;
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        //this method convert bitmap to uri
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "profilepic", null);
        return Uri.parse(path);
    }

    private void resizeimage(Uri data) {
        CropImage.activity(data)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1,1)
                .setMaxCropResultSize(2048,2048)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setOutputCompressQuality(50)
                .start(this);
    }

    private void uploadpicfstore1(Uri newuri) {

        StorageReference uploadpicref = storageReference.child("users/" + uid + "/profilepic1.jpg");
        uploadpicref.putFile(newuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Pic_play_bkp.this, "Profile pic upload success", Toast.LENGTH_SHORT).show();
                uploadpicref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    //load(uri) is brought from below line,
                    public void onSuccess(Uri uri) {
                        // Picasso.get().load(uri).into(profilepic_ref);
                        downloaduri1 = uri;

                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Pic_play_bkp.this, "Profile pic upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


}