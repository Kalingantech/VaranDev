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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.varandev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class Signup_Activity extends AppCompatActivity {



    private Button signup_btn;
    private ProgressBar registerprogress;
    private FirebaseAuth fauth;
    private FirebaseUser fcurrentuser;
    private EditText email_txt, password_txt,confirm_password_txt;
    private CheckBox show_pswd_checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_btn = findViewById(R.id.xml_signup);
        email_txt = findViewById(R.id.xml_mailID);
        password_txt = findViewById(R.id.xml_password);
        confirm_password_txt =findViewById(R.id.xml_confirm_password);
        fauth = FirebaseAuth.getInstance();
        registerprogress = findViewById(R.id.xml_register_progressbar);
        show_pswd_checkbox = findViewById(R.id.xml_showpassword);

        //this method to enable show pssword
        show_pswd_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    password_txt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirm_password_txt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    password_txt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirm_password_txt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = email_txt.getText().toString().trim();
                String password = password_txt.getText().toString().trim();
                String confirm_password = confirm_password_txt.getText().toString().trim();
                registerprogress.setVisibility(View.VISIBLE);

                if(password.equals(confirm_password)){

                    if(!emailID.isEmpty() && !password.isEmpty() ){
                        fauth.createUserWithEmailAndPassword(emailID,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    //Login after signup
                                    fauth.signInWithEmailAndPassword(emailID,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Signup_Activity.this,"Login successful",Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(Signup_Activity.this,Profile_Create_Page1.class);
                                                intent.putExtra("emailID", emailID);
                                                startActivity(intent);
                                                finish();
                                                registerprogress.setVisibility(View.INVISIBLE);
                                            }else{
                                                Toast.makeText(Signup_Activity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    //login after signup completed

                                }else{
                                    Toast.makeText(Signup_Activity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    registerprogress.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                    }else {
                        Toast.makeText(Signup_Activity.this,"Please enter Email & Password",Toast.LENGTH_SHORT).show();
                        registerprogress.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    Toast.makeText(Signup_Activity.this,"Password & confirm password not Matching",Toast.LENGTH_SHORT).show();
                    registerprogress.setVisibility(View.INVISIBLE);
                }



            }
        });
    }
}