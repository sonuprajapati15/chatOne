package com.example.chatone.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.account.Account;
import com.example.chatone.chat.AnnonymousChat;
import com.example.chatone.chat.Chat;
import com.example.chatone.home.fragments.Adapter.TimelineAdapter;
import com.example.chatone.pojos.Timelinedata;
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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    ImageView option, back;
    CircleImageView profile;
    TextView name, name1, gender, status, dob, city, friends, description, hide, heyid;
    LinearLayout friend, msg;
    Button accept, reject, msgs, send, cancel;
    RecyclerView recv;
    RelativeLayout req;
    ArrayList<Timelinedata> al = new ArrayList<>();
    FirebaseUser user;
    DatabaseReference reference, reference2, reference3, reference4, reference5;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        option = (ImageView) findViewById(R.id.option);
        back = (ImageView) findViewById(R.id.back);
        profile = (CircleImageView) findViewById(R.id.profile);
        name = (TextView) findViewById(R.id.name);
        name1 = (TextView) findViewById(R.id.name1);
        gender = (TextView) findViewById(R.id.gender);
        status = (TextView) findViewById(R.id.status);
        city = (TextView) findViewById(R.id.city);
        dob = (TextView) findViewById(R.id.dob);
        friends = (TextView) findViewById(R.id.friends);
        description = (TextView) findViewById(R.id.description);
        hide = (TextView) findViewById(R.id.hide);
        heyid = (TextView) findViewById(R.id.heyid);
        friend = (LinearLayout) findViewById(R.id.friend);
        req = (RelativeLayout) findViewById(R.id.req);
        msg = (LinearLayout) findViewById(R.id.msg);
        recv = (RecyclerView) findViewById(R.id.recv);
        accept = (Button) findViewById(R.id.accept);
        reject = (Button) findViewById(R.id.reject);
        msgs = (Button) findViewById(R.id.msgs);
        send = (Button) findViewById(R.id.send);
        cancel = (Button) findViewById(R.id.cancel);
        recv.setLayoutManager(new GridLayoutManager(this, 1));


        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = getIntent().getStringExtra("UID");
        if (!getIntent().getStringExtra("PROFILE").equals("default"))
            Picasso.with(UserProfile.this).load(getIntent().getStringExtra("PROFILE")).into(profile);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserProfile.this, UserImage.class);
                i.putExtra("IMAGE", getIntent().getStringExtra("PROFILE"));
                startActivity(i);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p = new PopupMenu(UserProfile.this, v);
                MenuInflater inflater = p.getMenuInflater();
                inflater.inflate(R.menu.menu_user_profile, p.getMenu());

                if (msg.getVisibility() == View.VISIBLE)
                    p.getMenu().findItem(R.id.unfriend).setVisible(true);
                else
                    p.getMenu().findItem(R.id.unfriend).setVisible(false);

                p.show();
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.unfriend:
                                reference2.child(uid).child(user.getUid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                reference2.child(user.getUid()).child(uid).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                req.setVisibility(View.VISIBLE);
                                                                cancel.setVisibility(View.GONE);
                                                                send.setVisibility(View.VISIBLE);
                                                                friend.setVisibility(View.GONE);
                                                                msg.setVisibility(View.GONE);
                                                                Toast.makeText(UserProfile.this, "unfrined", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                            }
                                        });
                                return true;

                            case R.id.send:
                                Intent intent = new Intent(getApplicationContext(), AnnonymousChat.class);
                                intent.putExtra("UID", uid);
                                intent.putExtra("ID", "SEND");
                                intent.putExtra("ID1", "RECIVE");
                                intent.putExtra("NAME", name.getText().toString());
                                intent.putExtra("PROFILE", getIntent().getStringExtra("PROFILE"));
                                startActivity(intent);
                                return true;

                            default:
                                return false;
                        }

                    }
                });
            }
        });


        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Account.class);
                intent.putExtra("TITLE", "FRIENDS");
                intent.putExtra("ID", "1");
                startActivity(intent);

            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("USERS");
        reference2 = FirebaseDatabase.getInstance().getReference().child("FRIENDS");
        reference3 = FirebaseDatabase.getInstance().getReference().child("POST");
        reference4 = FirebaseDatabase.getInstance().getReference().child("REQUESTS");
        reference5 = FirebaseDatabase.getInstance().getReference().child("REQUESTSEND");

        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                name.setText(data.child("NAME").getValue().toString());
                name1.setText("About " + data.child("NAME").getValue().toString());
                heyid.setText(data.child("HEYID").getValue().toString());
                gender.setText(data.child("GENDER").getValue().toString());
                status.setText(data.child("STATUS").getValue().toString());
                description.setText(data.child("DESCRIPTION").getValue().toString());
                dob.setText(data.child("DOB").getValue().toString());
                city.setText(data.child("HOMETOWN").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)) {
                    reference2.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            friends.setText("" + String.valueOf(dataSnapshot.getChildrenCount()));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    });
                } else
                    friends.setText("0");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                al.clear();
                for (final DataSnapshot data : dataSnapshot.getChildren()) {

                    for (final DataSnapshot data1 : data.getChildren()) {
                        if (data1.getKey().equals(uid)) {

                            hide.setVisibility(View.GONE);

                            for (final DataSnapshot data2 : data1.getChildren()) {

                                reference.child(uid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Timelinedata d = new Timelinedata();
                                        d.setData(data.getKey());
                                        d.setData1(data1.getKey());
                                        d.setSender(data2.getKey());
                                        d.setProfile(dataSnapshot.child("THUMB").getValue().toString());
                                        d.setName(dataSnapshot.child("NAME").getValue().toString());
                                        d.setImage(data2.child("IMAGE").getValue().toString());
                                        d.setText(data2.child("TEXT").getValue().toString());
                                        d.setTime(data2.child("TIME").getValue().toString());
                                        d.setId("1");

                                        if (data2.hasChild("LIKE")) {
                                            d.setLikes(data2.child("LIKE").getChildrenCount() + " Likes");
                                            if (data2.child("LIKE").hasChild(uid))
                                                d.setId("2");
                                            else
                                                d.setId("1");

                                        } else {
                                            d.setId("1");
                                            d.setLikes("0 Likes");
                                        }

                                        if (data.hasChild("COMMENT"))
                                            d.setComent(data.child("REPLY").getChildrenCount() + " comments");
                                        else
                                            d.setComent("0 comments");

                                        al.add(d);

                                        TimelineAdapter td = new TimelineAdapter(UserProfile.this, al);
                                        recv.setAdapter(td);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserProfile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference4.child(uid).child(user.getUid()).setValue("send")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reference5.child(user.getUid()).child(uid).setValue("send")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                req.setVisibility(View.VISIBLE);
                                                cancel.setVisibility(View.VISIBLE);
                                                send.setVisibility(View.GONE);
                                                friend.setVisibility(View.GONE);
                                                msg.setVisibility(View.GONE);
                                                Toast.makeText(UserProfile.this, "request send", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference5.child(user.getUid()).child(uid).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reference4.child(uid).child(user.getUid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    req.setVisibility(View.VISIBLE);
                                                    send.setVisibility(View.VISIBLE);
                                                    cancel.setVisibility(View.GONE);
                                                    friend.setVisibility(View.GONE);
                                                    msg.setVisibility(View.GONE);
                                                    Toast.makeText(UserProfile.this, "done", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference2.child(uid).child(user.getUid()).setValue("FRIEND")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    reference2.child(user.getUid()).child(uid).setValue("FRIEND")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isComplete())
                                                        reference5.child(user.getUid()).child(uid).removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isComplete())
                                                                            reference4.child(uid).child(user.getUid()).removeValue()
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isComplete()) {
                                                                                                msg.setVisibility(View.VISIBLE);
                                                                                                friend.setVisibility(View.GONE);
                                                                                                req.setVisibility(View.GONE);
                                                                                            }
                                                                                        }
                                                                                    });
                                                                    }
                                                                });
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                ;
                            }
                        });


            }
        });


        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference5.child(user.getUid()).child(uid).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reference4.child(uid).child(user.getUid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    req.setVisibility(View.VISIBLE);
                                                    send.setVisibility(View.VISIBLE);
                                                    cancel.setVisibility(View.GONE);
                                                    friend.setVisibility(View.GONE);
                                                    msg.setVisibility(View.GONE);
                                                    Toast.makeText(UserProfile.this, "done", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });

        msgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Chat.class);
                intent.putExtra("UID", uid);
                intent.putExtra("NAME", name.getText().toString());
                intent.putExtra("PROFILE", getIntent().getStringExtra("PROFILE"));
                startActivity(intent);
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user.getUid())) {
                    reference2.child(user.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(uid)) {
                                        req.setVisibility(View.GONE);
                                        msg.setVisibility(View.VISIBLE);
                                        friend.setVisibility(View.GONE);
                                    } else
                                        request();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });
                } else
                    request();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });


    }

    private void request() {

        reference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user.getUid())) {
                    reference4.child(user.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(uid)) {
                                        req.setVisibility(View.GONE);
                                        msg.setVisibility(View.GONE);
                                        friend.setVisibility(View.VISIBLE);
                                    } else
                                        requestsend();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });

                } else
                    requestsend();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void requestsend() {
        reference5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user.getUid())) {
                    reference5.child(user.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(uid)) {
                                        req.setVisibility(View.VISIBLE);
                                        cancel.setVisibility(View.VISIBLE);
                                        send.setVisibility(View.GONE);
                                        msg.setVisibility(View.GONE);
                                        friend.setVisibility(View.GONE);
                                    } else {
                                        req.setVisibility(View.VISIBLE);
                                        send.setVisibility(View.VISIBLE);
                                        cancel.setVisibility(View.GONE);
                                        msg.setVisibility(View.GONE);
                                        friend.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });
                } else {
                    req.setVisibility(View.VISIBLE);
                    send.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.GONE);
                    msg.setVisibility(View.GONE);
                    friend.setVisibility(View.GONE);
                }
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
