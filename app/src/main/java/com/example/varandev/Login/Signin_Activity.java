package com.example.varandev.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.varandev.HomeActivity;
import com.example.varandev.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.jetbrains.annotations.NotNull;

public class Signin_Activity extends AppCompatActivity {

    private Button signup_btn, signin_btn;
    private ProgressBar mloginprogress;
    private FirebaseAuth fauth;
    private FirebaseUser fcurrentuser;
    private EditText email_txt, password_txt;
    private CheckBox show_pswd_checkbox;
    private FirebaseFirestore db;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView google_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup_btn = findViewById(R.id.xml_Register);
        email_txt = findViewById(R.id.xml_mailID);
        password_txt = findViewById(R.id.xml_password);
        signin_btn = findViewById(R.id.xml_signin);
        mloginprogress = findViewById(R.id.xml_login_progressbar);
        show_pswd_checkbox = findViewById(R.id.xml_showpassword);
        fauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        google_btn = findViewById(R.id.xml_google_signin);



        //this method to enable show pssword
        show_pswd_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    password_txt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password_txt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signin_Activity.this, Signup_Activity.class));
            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = email_txt.getText().toString().trim();
                String password = password_txt.getText().toString().trim();
                mloginprogress.setVisibility(View.VISIBLE);

                if (!emailID.isEmpty() && !password.isEmpty()) {
                    fauth.signInWithEmailAndPassword(emailID, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Signin_Activity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Signin_Activity.this, HomeActivity.class));
                                finish();
                                mloginprogress.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(Signin_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                mloginprogress.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                } else {
                    Toast.makeText(Signin_Activity.this, "Please enter Email & Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Google signin starts here.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        //dont worry about below because if fauth is working fine, flow from splash will be taken to home.
      /*  GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            navigate_next_Activity();
        }*/

        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });

    }

    private void signin() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Try later", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        fauth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    startActivity(new Intent(Signin_Activity.this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener(this, e -> Toast.makeText(Signin_Activity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }

    private void navigate_next_Activity() {
        Intent intent = new Intent(Signin_Activity.this, Profile_Create_Page1.class);
        startActivity(intent);
        finish();
    }
}