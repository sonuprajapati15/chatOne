package com.example.chatone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.chatone.home.Home;
import com.example.chatone.register.AuthenticationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashStart extends AppCompatActivity {

    private FirebaseUser user;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.setIcon(R.mipmap.icon1);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(" Authenticating User. ");
        dialog.show();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, AuthenticationActivity.class));
            return;
        }

        SharedPreferences sp = getSharedPreferences("chatOne", Context.MODE_PRIVATE);
        boolean ch = sp.getBoolean("LOGIN", false);

        if (!ch)
            startActivity(new Intent(getApplicationContext(), AuthenticationActivity.class));
        else
            startActivity(new Intent(getApplicationContext(), Home.class));

        dialog.dismiss();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}