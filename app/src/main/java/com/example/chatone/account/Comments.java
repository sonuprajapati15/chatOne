package com.example.chatone.account;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.pojos.MessageData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Comments extends AppCompatActivity {

    RecyclerView recv;
    TextView send;
    EditText box;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref, refrence;
    String profile, name;

    ArrayList<MessageData> al = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        recv = (RecyclerView) findViewById(R.id.recv);
        box = (EditText) findViewById(R.id.box);
        send = (TextView) findViewById(R.id.send);

        recv.setLayoutManager(new GridLayoutManager(this, 1));

        ref = FirebaseDatabase.getInstance().getReference().child("POST").child(getIntent().getStringExtra("URL1")).child(getIntent().getStringExtra("URL2"))
                .child(getIntent().getStringExtra("URL3"));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (box.getText().toString().trim().length() < 1)
                    Snackbar.make(findViewById(R.id.cord), "comment box is empty", Snackbar.LENGTH_LONG).show();

                else
                    comment();
            }
        });


    }

    private void comment() {

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("TEXT", box.getText().toString().trim());
        hm.put("TIME", DateFormat.getTimeInstance().format(new Date()));
        hm.put("ID", user.getUid());

        ref.child("COMMENT").child(DateFormat.getDateTimeInstance().format(new Date())).updateChildren(hm)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        box.setText("");
                        Toast.makeText(Comments.this, "Comment Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(R.id.cord), "failed to upload comment", Snackbar.LENGTH_LONG).show();
                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();

        refrence = FirebaseDatabase.getInstance().getReference().child("USERS");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                al.clear();
                if (dataSnapshot.hasChild("COMMENT")) {
                    for (final DataSnapshot data : dataSnapshot.child("COMMENT").getChildren()) {
                        refrence.child(data.child("ID").getValue().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                MessageData d = new MessageData();
                                d.setProfile(dataSnapshot.child("THUMB").getValue().toString());
                                d.setName(dataSnapshot.child("NAME").getValue().toString());
                                d.setMessage(data.child("TEXT").getValue().toString());
                                d.setTime(data.child("TIME").getValue().toString());

                                al.add(d);

                                CommentAdap ad = new CommentAdap(Comments.this, al);
                                recv.setAdapter(ad);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(R.id.cord), "unkown error find", Snackbar.LENGTH_LONG).show();
            }
        });


    }
}
