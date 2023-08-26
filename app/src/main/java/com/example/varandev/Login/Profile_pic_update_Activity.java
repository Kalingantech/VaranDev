package com.example.varandev.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.varandev.HomeActivity;
import com.example.varandev.Match.MatchActivity;
import com.example.varandev.Match.Userprofile_Activity;
import com.example.varandev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class Profile_pic_update_Activity extends AppCompatActivity {


    private CircleImageView profilepic_ref1,profilepic_ref2,profilepic_ref3;
    private Button profile_pic_save_btn;
    private ProgressBar horizontal_progress_bar;
    private FirebaseAuth fauth;
    private FirebaseUser mcurrentuser;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private Uri profileimageuri1,profileimageuri2,profileimageuri3;
    private String uid,url1,url2,url3,newuri1,newuri2,newuri3;
    private Uri downloaduri1,downloaduri2,downloaduri3;
    private static final int PICK_IMAGE4 = 4;
    private static final int PICK_IMAGE5 = 5;
    private static final int PICK_IMAGE6 = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pic_update);
        /*-----------------------------------------------*/
        profilepic_ref1 = findViewById(R.id.xml_profile_pic4);
        profilepic_ref2 = findViewById(R.id.xml_profile_pic5);
        profilepic_ref3 = findViewById(R.id.xml_profile_pic6);

        profile_pic_save_btn = findViewById(R.id.xml_save_pic);
        horizontal_progress_bar=findViewById(R.id.xml_p_create_progressbar);
        /*-----------------------------------------------*/
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        fauth = FirebaseAuth.getInstance();
        mcurrentuser = fauth.getCurrentUser();
        uid=fauth.getCurrentUser().getUid();


        profilepic_ref1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE4);

            }
        });
        profilepic_ref2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE5);

            }
        });
        profilepic_ref3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE6);

            }
        });


        String uid = mcurrentuser.getUid();
        //This is will check if user has created the proile already
        //When this activity start exisinting url will be added to url1,url2 & url3. And when image is changed/new image added url* will be updated.

        DocumentReference documentReference = db.collection("users").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    url1 = task.getResult().getString("pic1");
                    url2 = task.getResult().getString("pic2");
                    url3 = task.getResult().getString("pic3");
                    //Picasso.get().load(url).into(profilepic_ref);
                    //Glide helps us to provide a default pic if there is no pic uploaded to firestore
                    Picasso.get().load(url1).placeholder(R.drawable.profile_pic).into(profilepic_ref1);
                    Picasso.get().load(url2).placeholder(R.drawable.profile_pic).into(profilepic_ref2);
                    Picasso.get().load(url3).placeholder(R.drawable.profile_pic).into(profilepic_ref3);
                }
            }
        });


        profile_pic_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //again upload the pic start
                DocumentReference updatedoc_ref = db.collection("users").document(uid);
                Map<String, Object> user_details = new HashMap<>();
                user_details.put("pic1", String.valueOf(url1));
                user_details.put("pic2", String.valueOf(url2));
                user_details.put("pic3", String.valueOf(url3));

                updatedoc_ref.update(user_details).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Profile_pic_update_Activity.this,"successful",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Profile_pic_update_Activity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

                //again upload the pic end
            }
        });

    }





    //Below code will allow user to select photo from local storage with crop option
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if(resultCode == RESULT_OK || data != null || data.getData() != null){

                if (requestCode == PICK_IMAGE4 ) {
                    Toast.makeText(Profile_pic_update_Activity.this, "Profile pic 4 picked", Toast.LENGTH_SHORT).show();
                    Uri profileimageuri1 = data.getData();
                    profilepic_ref1.setImageURI(profileimageuri1);
                    //Picasso.get().load(profileimageuri1).into(profilepic_ref1);
                    uploadpicfstore1(profileimageuri1);
                    //progressBar.setVisibility(View.VISIBLE);

                }
                if (requestCode == PICK_IMAGE5) {
                    Toast.makeText(Profile_pic_update_Activity.this, "Profile pic 5 picked", Toast.LENGTH_SHORT).show();
                    Uri profileimageuri2 = data.getData();
                    profilepic_ref2.setImageURI(profileimageuri2);

                    //Picasso.get().load(profileimageuri2).into(profilepic_ref2);
                    uploadpicfstore2(profileimageuri2);
                    //progressBar.setVisibility(View.VISIBLE);

                }
                if (requestCode == PICK_IMAGE6) {
                    Toast.makeText(Profile_pic_update_Activity.this, "Profile pic 6 picked", Toast.LENGTH_SHORT).show();
                    Uri profileimageuri3 = data.getData();
                    //Picasso.get().load(profileimageuri3).into(profilepic_ref3);
                    profilepic_ref3.setImageURI(profileimageuri3);
                    uploadpicfstore3(profileimageuri3);
                    //progressBar.setVisibility(View.VISIBLE);

                }
            }

        } catch (Exception e) {
            Toast.makeText(Profile_pic_update_Activity.this, "Error with picture" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadpicfstore1(Uri profileimageuri1) {

        StorageReference uploadpicref = storageReference.child("users/" + uid + "/profilepic1.jpg");
        uploadpicref.putFile(profileimageuri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile_pic_update_Activity.this, "Profile pic upload success", Toast.LENGTH_SHORT).show();
                uploadpicref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    //load(uri) is brought from below line,
                    public void onSuccess(Uri uri) {
                        // Picasso.get().load(uri).into(profilepic_ref);
                        downloaduri1 = uri;
                        url1= String.valueOf(downloaduri1);
                        //below line will add the uri link to downloaduri string which  is used to upload to firestore DB
                        //progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile_pic_update_Activity.this, "Profile pic upload failed", Toast.LENGTH_SHORT).show();
                //progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void uploadpicfstore2(Uri profileimageuri2) {

        StorageReference uploadpicref = storageReference.child("users/" + uid + "/profilepic2.jpg");
        uploadpicref.putFile(profileimageuri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile_pic_update_Activity.this, "Profile pic upload success", Toast.LENGTH_SHORT).show();
                uploadpicref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    //load(uri) is brought from below line,
                    public void onSuccess(Uri uri) {
                        // Picasso.get().load(uri).into(profilepic_ref);
                        downloaduri2 = uri;
                        url2= String.valueOf(downloaduri2);
                        //below line will add the uri link to downloaduri string which  is used to upload to firestore DB
                        //progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile_pic_update_Activity.this, "Profile pic upload failed", Toast.LENGTH_SHORT).show();
                //progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void uploadpicfstore3(Uri profileimageuri3) {

        StorageReference uploadpicref = storageReference.child("users/" + uid + "/profilepic3.jpg");
        uploadpicref.putFile(profileimageuri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile_pic_update_Activity.this, "Profile pic upload success", Toast.LENGTH_SHORT).show();
                uploadpicref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    //load(uri) is brought from below line,
                    public void onSuccess(Uri uri) {
                        // Picasso.get().load(uri).into(profilepic_ref);
                        downloaduri3 = uri;
                        url3= String.valueOf(downloaduri3);
                        //below line will add the uri link to downloaduri string which  is used to upload to firestore DB
                        //progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile_pic_update_Activity.this, "Profile pic upload failed", Toast.LENGTH_SHORT).show();
                //progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}