package com.example.varandev.Login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.varandev.HomeActivity;
import com.example.varandev.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class Profile_Create_Page5 extends AppCompatActivity {

    private CircleImageView profilepic_ref1, profilepic_ref2, horoscope_ref;
    private Button profile_save_btn;
    private ProgressBar progressBar_pic1,progressBar_pic2,progressBar_horoscope;
    private ProgressBar horizontal_progress_bar;
    private FirebaseAuth fauth;
    private FirebaseUser mcurrentuser;
    private Uri profileimageuri1, profileimageuri2, horoscope_imageuri;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private String uid;
    public String gender,other_gender,customid;
    private Uri downloaduri1, downloaduri2, horoscope_uri;
    private Switch photo_hide_toggleButton;

    private static int pic_no=0;
    private int photo_hide = 1;
    private static final int PICK_IMAGE1 = 1;
    private static final int PICK_IMAGE2 = 2;
    private static final int PICK_IMAGE3 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_create_page5);

        profilepic_ref1 = findViewById(R.id.xml_profile_pic1);
        profilepic_ref2 = findViewById(R.id.xml_profile_pic2);
        horoscope_ref = findViewById(R.id.xml_horoscope_pic);

        profile_save_btn = findViewById(R.id.xml_profile_save);
        photo_hide_toggleButton = findViewById(R.id.xml_photo_hide_option);



        progressBar_pic1 = findViewById(R.id.xml_progressbar_pic1);
        progressBar_pic2 = findViewById(R.id.xml_progressbar_pic2);
        progressBar_horoscope = findViewById(R.id.xml_progressbar_horoscope);
        horizontal_progress_bar = findViewById(R.id.xml_p_create_progressbar);


        fauth = FirebaseAuth.getInstance();
        mcurrentuser = fauth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        uid = fauth.getCurrentUser().getUid();


        progressBar_pic1.setVisibility(View.INVISIBLE);
        progressBar_pic2.setVisibility(View.INVISIBLE);
        progressBar_horoscope.setVisibility(View.INVISIBLE);
        profilepic_ref2.setVisibility(View.INVISIBLE);
        horizontal_progress_bar.setVisibility(View.INVISIBLE);

        /*------------------------------------------------*/
        /*------------------------------------------------*/

        profilepic_ref1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pic_no =1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE1);
                progressBar_pic1.setVisibility(View.VISIBLE);
                //profilepic_ref2.setVisibility(View.VISIBLE);
            }
        });
        profilepic_ref2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pic_no =2;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE2);
                progressBar_pic2.setVisibility(View.VISIBLE);

            }
        });
        horoscope_ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pic_no =3;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE3);
                progressBar_horoscope.setVisibility(View.VISIBLE);

            }
        });

        photo_hide_toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    // The toggle is enabled
                    photo_hide = 25;
                    Toast.makeText(Profile_Create_Page5.this, "Photo will be hidden", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    photo_hide = 1;
                    Toast.makeText(Profile_Create_Page5.this, "Not hidden", Toast.LENGTH_SHORT).show();
                }
            }
        });



        profile_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    horizontal_progress_bar.setVisibility(View.VISIBLE);

                    DocumentReference user_doc = db.collection("users").document(uid);
                    //Profile_model profile = new Profile_model(uid,customid,gender, String.valueOf(downloaduri), profilename, age, phoneno);
                    Map<String, Object> user_details = new HashMap<>();
                    user_details.put("pic1", String.valueOf(downloaduri1));
                    user_details.put("pic2", String.valueOf(downloaduri2));
                    user_details.put("horoscope", String.valueOf(horoscope_uri));
                    user_details.put("photo_hide", photo_hide);


                    user_doc.update(user_details).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Profile_Create_Page5.this, "Profile  upload success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Profile_Create_Page5.this, HomeActivity.class));
                            finish();
                            horizontal_progress_bar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(Profile_Create_Page5.this, "Profile upload failed", Toast.LENGTH_SHORT).show();
                            horizontal_progress_bar.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "" + e.toString());
                        }
                    });
                    //start uploading the data to firestore
            }
        });

    }


    //Below code will allow user to select photo from local storage with crop option
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK || data != null || data.getData() != null) {

                if (requestCode == PICK_IMAGE1  && pic_no ==1) {
                    resizeimage1(data.getData());
                }
                if (requestCode == PICK_IMAGE2  && pic_no ==2) {
                    resizeimage2(data.getData());
                }

                if (requestCode == PICK_IMAGE3  && pic_no ==3) {

                    Uri horoscope_ImageUri = data.getData();
                    long lengthbmp;

                    Bitmap bmp = null;
                    try {
                        bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), horoscope_ImageUri);
                        //to check size
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                        byte[] imageInByte = stream.toByteArray();
                        lengthbmp = imageInByte.length;

                        //Log.d("size", String.valueOf(lengthbmp));
                        if(lengthbmp < 3000000){
                            //Log.d("size", "lengthbmp is less than 3MB");

                            Uri horoscope_imageuri = get_horoscope_Uri(this,bmp);
                            //horoscope_ref.setImageURI(horoscope_imageuri);
                            upload_horoscope(horoscope_imageuri);
                        }
                        if(lengthbmp > 3000000){
                            //Log.d("size", "lengthbmp is larger than 3MB");
                            Toast.makeText(Profile_Create_Page5.this, "File is too large, choose other image", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                }

                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && pic_no ==4 ) {
                    //get result from crop activity method "resizeimage"
                    CropImage.ActivityResult result1 =CropImage.getActivityResult(data);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result1.getUri());
                    //go to method mark to write watermark on image
                    Bitmap newbitmap = mark(bitmap);
                    //go to getImageUri method to get the uri from bitmap
                    Uri profileimageuri1 = getImageUri(this,newbitmap);
                    //profilepic_ref1.setImageURI(profileimageuri1);
                    //upload the uri to firestore..
                    uploadpicfstore1(profileimageuri1);
                }
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && pic_no ==5 ) {
                    //get result from crop activity method "resizeimage"
                    CropImage.ActivityResult result1 =CropImage.getActivityResult(data);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result1.getUri());
                    //go to method mark to write watermark on image
                    Bitmap newbitmap = mark(bitmap);
                    //go to getImageUri method to get the uri from bitmap
                    Uri profileimageuri2 = getImageUri(this,newbitmap);
                    //profilepic_ref2.setImageURI(profileimageuri2);
                    //upload the uri to firestore..
                    uploadpicfstore2(profileimageuri2);
                }
            }

        } catch (Exception e) {
            Toast.makeText(Profile_Create_Page5.this, "Error with picture" + e, Toast.LENGTH_SHORT).show();
        }
    }


    private void resizeimage1(Uri data) {
        pic_no =4;
        CropImage.activity(data)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1,1)
                .setMaxCropResultSize(2048,2048)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setOutputCompressQuality(50)
                .start(this);
    }

    private void resizeimage2(Uri data) {
        pic_no =5;
        CropImage.activity(data)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1,1)
                .setMaxCropResultSize(2048,2048)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setOutputCompressQuality(50)
                .start(this);
    }

    public static Bitmap mark(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        //start paining on the image
        Paint paint = new Paint();
        paint.setTextSize(h/10);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        // this step will draw(write on the selected image and send result backup)
        canvas.drawText("முத்துராஜா வரன்", w/3, (float) (w/1.3), paint);
        return result;
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        //this method convert bitmap to uri
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "profilepic" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }
    private Uri get_horoscope_Uri(Context context, Bitmap inImage) {
        //this method convert bitmap to uri
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "profilepic" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    private void uploadpicfstore1(Uri profileimageuri1) {

        StorageReference uploadpicref = storageReference.child("users").child(uid).child("profilepic1.jpg");
        uploadpicref.putFile(profileimageuri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile_Create_Page5.this, "Profile pic upload success", Toast.LENGTH_SHORT).show();
                uploadpicref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    //load(uri) is brought from below line,
                    public void onSuccess(Uri uri) {
                        // Picasso.get().load(uri).into(profilepic_ref);
                        downloaduri1 = uri;
                        String pic1 = String.valueOf(downloaduri1);
                        Glide.with(getApplicationContext())
                                .load(pic1)
                                .error(R.drawable.add_photo)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(profilepic_ref1);
                        progressBar_pic1.setVisibility(View.INVISIBLE);
                        profilepic_ref2.setVisibility(View.VISIBLE);
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile_Create_Page5.this, "Profile pic upload failed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void uploadpicfstore2(Uri profileimageuri2) {

        StorageReference uploadpicref = storageReference.child("users").child(uid).child("profilepic2.jpg");
        uploadpicref.putFile(profileimageuri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile_Create_Page5.this, "Profile pic upload success", Toast.LENGTH_SHORT).show();
                uploadpicref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    //load(uri) is brought from below line,
                    public void onSuccess(Uri uri) {
                        // Picasso.get().load(uri).into(profilepic_ref);
                        downloaduri2 = uri;
                        String pic2 = String.valueOf(downloaduri2);

                        Glide.with(getApplicationContext())
                                .load(pic2)
                                .error(R.drawable.add_photo)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(profilepic_ref2);
                        progressBar_pic2.setVisibility(View.INVISIBLE);

                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile_Create_Page5.this, "Profile pic upload failed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void upload_horoscope(Uri horoscope_imageuri) {

        StorageReference uploadpicref = storageReference.child("users").child(uid).child("horoscope.jpg");
        uploadpicref.putFile(horoscope_imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile_Create_Page5.this, "Profile pic upload success", Toast.LENGTH_SHORT).show();
                uploadpicref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    //load(uri) is brought from below line,
                    public void onSuccess(Uri uri) {
                        // Picasso.get().load(uri).into(profilepic_ref);
                        horoscope_uri = uri;
                        String horoscope = String.valueOf(horoscope_uri);

                        Glide.with(getApplicationContext())
                                .load(horoscope)
                                .error(R.drawable.add_photo)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(horoscope_ref);
                        progressBar_horoscope.setVisibility(View.INVISIBLE);
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile_Create_Page5.this, "Profile pic upload failed", Toast.LENGTH_SHORT).show();

            }
        });
    }



}