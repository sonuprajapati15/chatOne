package com.example.chatone.profile;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatone.R;
import com.squareup.picasso.Picasso;

public class UserImage extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_image);
        image = (ImageView) findViewById(R.id.image);

        if (!getIntent().getStringExtra("IMAGE").equals("default"))
            Picasso.with(this).load(getIntent().getStringExtra("IMAGE")).into(image);

    }
}
