package com.example.varandev.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class Profile_Update_Activity extends AppCompatActivity {

    private EditText profilename_ref,userage_ref,user_phoneno_ref;
    private CircleImageView profilepic_ref;
    private Button profile_save_btn;
    private ProgressBar horizontal_progress_bar;
    private FirebaseAuth fauth;
    private FirebaseUser mcurrentuser;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private String uid;
    private Uri downloaduri;
    private static final int PICK_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        profilepic_ref = findViewById(R.id.xml_profile_pic);
        profilename_ref = findViewById(R.id.xml_profile_name);
        userage_ref = findViewById(R.id.xml_profile_age);
        user_phoneno_ref = findViewById(R.id.xml_profile_phoneno);
        profile_save_btn = findViewById(R.id.xml_profile_save);
        horizontal_progress_bar=findViewById(R.id.xml_p_create_progressbar);

        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        fauth = FirebaseAuth.getInstance();
        mcurrentuser = fauth.getCurrentUser();
        uid=fauth.getCurrentUser().getUid();

        horizontal_progress_bar.setVisibility(View.INVISIBLE);

        profilepic_ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile_Update_Activity.this, Profile_pic_update_Activity.class));
            }
        });

        profile_save_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateprofile();
            }
        });

    }


    private void updateprofile() {
        String profilename = profilename_ref.getText().toString().trim();
        String age = userage_ref.getText().toString().trim();
        String phoneno = user_phoneno_ref.getText().toString().trim();

        final DocumentReference updatedoc_ref = db.collection("users").document(uid);
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {

                transaction.update(updatedoc_ref, "name", profilename);
                transaction.update(updatedoc_ref, "age", age);
                transaction.update(updatedoc_ref, "phoneno", phoneno);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
                startActivity(new Intent(Profile_Update_Activity.this, HomeActivity.class));
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });
    }

    /*----To check login status start---*/
    @Override
    protected void onStart() {
        super.onStart();

            String uid = mcurrentuser.getUid();
            //This is will check if user has created the proile already
            DocumentReference documentReference = db.collection("users").document(uid);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if(task.getResult().exists()){
                        String url = task.getResult().getString("pic1");
                        String name = task.getResult().getString("name");
                        String age = task.getResult().getString("age");
                        String phoneno = task.getResult().getString("phoneno");

                        //Picasso.get().load(url).into(profilepic_ref);
                        //Glide helps us to provide a default pic if there is no pic uploaded to firestore
                        Glide.with(Profile_Update_Activity.this)
                                .load(url)
                                .placeholder(R.drawable.profile_pic)
                                .into(profilepic_ref);

                        profilename_ref.setText(name);
                        userage_ref.setText(age);
                        user_phoneno_ref.setText(phoneno);

                    }
                }
            });


    }
}