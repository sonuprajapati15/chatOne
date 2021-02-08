package com.example.chatone.account;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.pojos.HeadingData;
import com.example.chatone.search.adapter.Friends;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Account extends AppCompatActivity {

    ImageView back;
    TextView title;
    RecyclerView recv;
    ArrayList<HeadingData> a = new ArrayList<>();
    DatabaseReference reference, reference1;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        recv = (RecyclerView) findViewById(R.id.recv);

        recv.setLayoutManager(new GridLayoutManager(this, 1));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        title.setText(getIntent().getStringExtra("TITLE"));
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("USERS");

        if (getIntent().getStringExtra("ID").equals("1")) {
            reference1 = FirebaseDatabase.getInstance().getReference().child(getIntent().getStringExtra("TITLE"));
            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user.getUid())) {
                        a.clear();
                        for (DataSnapshot data : dataSnapshot.child(user.getUid()).getChildren())
                            call(data.getKey());
                    } else
                        Toast.makeText(Account.this, "Request responds  0", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        } else if (getIntent().getStringExtra("ID").equals("2")) {
            reference1 = FirebaseDatabase.getInstance().getReference().child("POST").child(getIntent().getStringExtra("URL1"))
                    .child(getIntent().getStringExtra("URL2")).child(getIntent().getStringExtra("URL3"));

            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("LIKE")) {
                        a.clear();
                        for (DataSnapshot data : dataSnapshot.child("LIKE").getChildren())
                            call(data.getKey());
                    } else
                        Toast.makeText(Account.this, "Request responds  0", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });


        }


    }

    private void call(String key) {

        reference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HeadingData data = new HeadingData();
                data.setId(dataSnapshot.child("HEYID").getValue().toString());
                data.setTitle(dataSnapshot.child("NAME").getValue().toString());
                data.setAuthor(dataSnapshot.child("ONLINE").getValue().toString());
                if (!dataSnapshot.child("THUMB").getValue().toString().equals("default"))
                    data.setImage(dataSnapshot.child("THUMB").getValue().toString());
                else
                    data.setImage("default");

                a.add(data);
                Friends fr = new Friends(Account.this, a);
                recv.setAdapter(fr);
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
