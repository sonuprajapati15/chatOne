package com.example.chatone.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Email extends AppCompatActivity {
    ImageView down;
    Button submit;
    TextInputEditText email, pass;
    String email_pattern = "[A-Za-z0-9._-]+@[a-z]+\\\\.+[a-z]+";
    ProgressDialog dialog;
    LinearLayout option;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        down = (ImageView) findViewById(R.id.imageView2);
        submit = (Button) findViewById(R.id.login);
        email = (TextInputEditText) findViewById(R.id.email);
        pass = (TextInputEditText) findViewById(R.id.pass);
        option = (LinearLayout) findViewById(R.id.choose);

        loadStartUpDate();

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Mobile.class));
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginAndSignUpActivity.class));
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Pattern pattern = Pattern.compile(email_pattern);
                String username = email.getText().toString().trim(), password = pass.getText().toString().trim();

                if (username.isEmpty() || username.length() == 0)
                    email.setError("Email mandatory");
                else if (pattern.matcher(username).matches())
                    email.setError("enter valid email");
                else if (password.length() < 7)
                    pass.setError("password must be 8 char");
                else {
                    dialog.show();
                    dialog.setMessage("Uploading data");
                    signup(username, password);
                }
            }
        });


    }

    private void loadStartUpDate() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.setIcon(R.mipmap.icon1);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mAuth = FirebaseAuth.getInstance();
    }

    private void signup(String username, String password) {

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            sendVerificationEmail(mAuth.getCurrentUser());
                        }else {
                            dialog.dismiss();
                            Snackbar.make(findViewById(R.id.cord), "We are facing some Issue in Login." +
                                    " try with some other option !!", Snackbar.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void sendVerificationEmail(FirebaseUser currentUser) {

        currentUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            dialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), LoginAndSignUpActivity.class));
                            Toast.makeText(Email.this, "email verification message send ", Toast.LENGTH_LONG).show();
                            return;
                        }
                        dialog.dismiss();
                        Snackbar.make(findViewById(R.id.cord), "We are facing some Issue in while sending email link." +
                                " try with some other option !!", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
