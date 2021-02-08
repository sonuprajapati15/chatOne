package com.example.chatone.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatone.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthenticationActivity extends AppCompatActivity {

    Button login;
    CircleImageView img;
    TextView t1, t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        login = (Button) findViewById(R.id.email);
        t1 = (TextView) findViewById(R.id.textView6);
        t2 = (TextView) findViewById(R.id.textView2);
        img = (CircleImageView) findViewById(R.id.img);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginAndSignUpActivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        img.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce));
        t1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translateleft));
        t2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translateleft));
        login.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translateup));

    }
}
