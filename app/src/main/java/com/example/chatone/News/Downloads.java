package com.example.chatone.News;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.News.adapter.Downadapter;
import com.example.chatone.R;
import com.example.chatone.pojos.HeadingData;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;


public class Downloads extends AppCompatActivity {

    RecyclerView recv;
    ImageView back;
    public static ImageView google, facebook, whatsapp, twitter;
    ArrayList<HeadingData> al = new ArrayList<>();
    public static BottomSheetBehavior behavior;
    Button cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);
        recv = (RecyclerView) findViewById(R.id.recv);
        back = (ImageView) findViewById(R.id.back);
        google = (ImageView) findViewById(R.id.google);
        facebook = (ImageView) findViewById(R.id.face);
        twitter = (ImageView) findViewById(R.id.tweet);
        whatsapp = (ImageView) findViewById(R.id.whatsapp);

        cancel = (Button) findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(STATE_COLLAPSED);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recv.setLayoutManager(new GridLayoutManager(this, 1));

        View BottomSheet = findViewById(R.id.design);
        behavior = BottomSheetBehavior.from(BottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SAVE").child("NEWS");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                al.clear();
                if (dataSnapshot.hasChild(user.getUid())) {
                    ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                HeadingData d2 = new HeadingData();
                                d2.setImage(data.child("IMAGE").getValue().toString());
                                d2.setAuthor(data.child("AUTHOR").getValue().toString());
                                d2.setDate(data.child("DATE").getValue().toString());
                                d2.setDescription(data.child("DETAIL").getValue().toString());
                                d2.setUrl(data.child("URL").getValue().toString());
                                d2.setTitle(data.child("TITLE").getValue().toString());
                                d2.setId(data.getKey());
                                al.add(d2);
                            }
                            Downadapter ad1 = new Downadapter(Downloads.this, al);
                            recv.setAdapter(ad1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    });

                } else
                    Snackbar.make(findViewById(R.id.cord), "NO Download EXIST", Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
