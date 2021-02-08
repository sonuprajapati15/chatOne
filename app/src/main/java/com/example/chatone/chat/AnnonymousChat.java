package com.example.chatone.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.chat.adapter.AnnoyAdap;
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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnnonymousChat extends AppCompatActivity {

    ImageView back, send;
    TextView name;
    CircleImageView profile;
    EditText box;
    RecyclerView chatting;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference refrence1, refrence2;
    String uid, id, id1;
    ArrayList<MessageData> al = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonymous_chat);
        back = (ImageView) findViewById(R.id.back);
        send = (ImageView) findViewById(R.id.send);
        name = (TextView) findViewById(R.id.name);
        profile = (CircleImageView) findViewById(R.id.profile);
        box = (EditText) findViewById(R.id.box);
        chatting = (RecyclerView) findViewById(R.id.chatting);


        refrence1 = FirebaseDatabase.getInstance().getReference().child("ANNOY");
        refrence2 = FirebaseDatabase.getInstance().getReference().child("LASTMESSAGE").child("ANNOY");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        chatting.setLayoutManager(new GridLayoutManager(this, 1));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (box.getText().toString().length() < 4)
                    Snackbar.make(findViewById(R.id.cord), "text length is small", Snackbar.LENGTH_LONG).show();

                else
                    sending(box.getText().toString());
            }
        });

    }

    private void sending(final String text) {

        box.setText("");
        final String date = DateFormat.getDateTimeInstance().format(new Date());
        final String time = DateFormat.getTimeInstance().format(new Date());

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("MESSAGE", text);
        hm.put("TIME", time);
        hm.put("TYPE", "SEND");

        refrence1.child(user.getUid()).child(id).child(uid).child(date).updateChildren(hm)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        HashMap<String, Object> hm1 = new HashMap<>();
                        hm1.put("MESSAGE", text);
                        hm1.put("TIME", time);
                        hm1.put("TYPE", "RECIVE");
                        refrence1.child(uid).child(id1).child(user.getUid()).child(date).updateChildren(hm1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        final HashMap<String, Object> hm2 = new HashMap<>();
                                        hm2.put("MESSAGE", text);
                                        hm2.put("TIME", time);
                                        refrence2.child(user.getUid()).child(uid).updateChildren(hm2)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        refrence2.child(uid).child(user.getUid()).updateChildren(hm2)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(R.id.cord), "Unkown error found", Snackbar.LENGTH_LONG).show();

                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        uid = getIntent().getStringExtra("UID");
        id = getIntent().getStringExtra("ID");
        id1 = getIntent().getStringExtra("ID1");
        name.setText(getIntent().getStringExtra("NAME"));
        if (!getIntent().getStringExtra("PROFILE").equals("default"))
            Picasso.with(this).load(getIntent().getStringExtra("PROFILE")).into(profile);


        refrence1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                al.clear();

                if (dataSnapshot.hasChild(user.getUid())
                        && dataSnapshot.child(user.getUid()).hasChild(id)
                        && dataSnapshot.child(user.getUid()).child(id).hasChild(uid)) {

                    for (DataSnapshot data : dataSnapshot.child(user.getUid()).child(id).child(uid).getChildren()) {
                        MessageData d = new MessageData();

                        d.setMessage(data.child("MESSAGE").getValue().toString());
                        d.setType(data.child("TYPE").getValue().toString());
                        d.setTime(data.child("TIME").getValue().toString());

                        al.add(d);
                        AnnoyAdap ad = new AnnoyAdap(AnnonymousChat.this, al);
                        chatting.setAdapter(ad);

                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
