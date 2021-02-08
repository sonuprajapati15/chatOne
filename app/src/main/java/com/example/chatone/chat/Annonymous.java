package com.example.chatone.chat;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.chat.adapter.Annoylist;
import com.example.chatone.pojos.MessageData;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Annonymous extends AppCompatActivity {

    RecyclerView send, recive;
    TabLayout tab;

    ArrayList<MessageData> al1 = new ArrayList<>();
    ArrayList<MessageData> al2 = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference, reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonymous);

        tab = (TabLayout) findViewById(R.id.tab);
        send = (RecyclerView) findViewById(R.id.send);
        recive = (RecyclerView) findViewById(R.id.recive);

        send.setLayoutManager(new GridLayoutManager(this, 1));
        recive.setLayoutManager(new GridLayoutManager(this, 1));

        tab.addTab(tab.newTab().setText("Recive"));
        tab.addTab(tab.newTab().setText("Send"));


        reference = FirebaseDatabase.getInstance().getReference().child("USERS");
        reference1 = FirebaseDatabase.getInstance().getReference().child("ANNOY");
        reference2 = FirebaseDatabase.getInstance().getReference().child("LASTMESSAGE").child("ANNOY");


        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        recive.setVisibility(View.VISIBLE);
                        send.setVisibility(View.GONE);
                        call1();
                        break;

                    case 1:
                        send.setVisibility(View.VISIBLE);
                        recive.setVisibility(View.GONE);
                        call2();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void call1() {

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                al1.clear();
                if (dataSnapshot.hasChild(user.getUid()) && dataSnapshot.child(user.getUid()).hasChild("RECIVE")) {
                    for (final DataSnapshot data : dataSnapshot.child(user.getUid()).child("RECIVE").getChildren()) {
                        reference2.child(user.getUid()).child(data.getKey())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot data1) {
                                        MessageData d = new MessageData();
                                        d.setImage("default");
                                        d.setName("UNKONW USER");
                                        d.setMessage(data1.child("MESSAGE").getValue().toString());
                                        d.setTime(data1.child("TIME").getValue().toString());
                                        d.setId(data.getKey());
                                        d.setType("RECIVE");
                                        d.setUid("SEND");
                                        al1.add(d);

                                        Annoylist ad = new Annoylist(Annonymous.this, al1);
                                        recive.setAdapter(ad);
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

            }
        });

    }

    private void call2() {

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                al2.clear();
                if (dataSnapshot.hasChild(user.getUid()) && dataSnapshot.child(user.getUid()).hasChild("SEND")) {
                    for (final DataSnapshot data : dataSnapshot.child(user.getUid()).child("SEND").getChildren()) {
                        reference2.child(user.getUid()).child(data.getKey())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot data1) {

                                        reference.child(data.getKey()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot data2) {

                                                MessageData d = new MessageData();
                                                d.setImage(data2.child("THUMB").getValue().toString());
                                                d.setName(data2.child("NAME").getValue().toString());
                                                d.setMessage(data1.child("MESSAGE").getValue().toString());
                                                d.setTime(data1.child("TIME").getValue().toString());
                                                d.setId(data.getKey());
                                                d.setType("SEND");
                                                d.setUid("RECIVE");
                                                al2.add(d);

                                                Annoylist ad = new Annoylist(getApplicationContext(), al2);
                                                send.setAdapter(ad);

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
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

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recive.setVisibility(View.VISIBLE);
        call1();
    }
}
