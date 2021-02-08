package com.example.chatone.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.account.Account;
import com.example.chatone.home.fragments.Adapter.TimelineAdapter;
import com.example.chatone.pojos.Timelinedata;
import com.example.chatone.profile.UserImage;
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

/**
 * Created by SONUsantosh on 20-12-2018.
 */

public class Profile extends Fragment {

    CircleImageView profile;
    TextView name, heyid, id, friend, request, sent, name1, gender, status, dob, city, description, hide;
    RecyclerView recv;
    String prof;
    FirebaseUser user;
    DatabaseReference reference, reference2, reference3, reference4, reference5;
    ArrayList<Timelinedata> al = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile, null);

        profile = (CircleImageView) v.findViewById(R.id.profile);
        name = (TextView) v.findViewById(R.id.name);
        name1 = (TextView) v.findViewById(R.id.name1);
        sent = (TextView) v.findViewById(R.id.sent);
        id = (TextView) v.findViewById(R.id.id);
        heyid = (TextView) v.findViewById(R.id.heyid);
        friend = (TextView) v.findViewById(R.id.friend);
        request = (TextView) v.findViewById(R.id.request);
        gender = (TextView) v.findViewById(R.id.gender);
        status = (TextView) v.findViewById(R.id.status);
        dob = (TextView) v.findViewById(R.id.dob);
        city = (TextView) v.findViewById(R.id.city);
        hide = (TextView) v.findViewById(R.id.hide);
        description = (TextView) v.findViewById(R.id.description);
        recv = (RecyclerView) v.findViewById(R.id.recv);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("USERS");
        reference2 = FirebaseDatabase.getInstance().getReference().child("FRIENDS");
        reference3 = FirebaseDatabase.getInstance().getReference().child("POST");
        reference4 = FirebaseDatabase.getInstance().getReference().child("REQUESTS");
        reference5 = FirebaseDatabase.getInstance().getReference().child("REQUESTSEND");

        recv.setLayoutManager(new GridLayoutManager(getActivity(), 1));


        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Account.class);
                intent.putExtra("TITLE", "FRIENDS");
                intent.putExtra("ID", "1");
                getActivity().startActivity(intent);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserImage.class);
                i.putExtra("IMAGE", prof);
                getActivity().startActivity(i);
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), Account.class);
                intent.putExtra("TITLE", "REQUESTS");
                intent.putExtra("ID", "1");
                getActivity().startActivity(intent);
            }
        });

        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Account.class);
                intent.putExtra("TITLE", "REQUESTSEND");
                intent.putExtra("ID", "1");
                getActivity().startActivity(intent);
            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {

                String hey = data.child("NAME").getValue().toString();

                name.setText(hey);
                name1.setText("About " + hey);
                heyid.setText(data.child("HEYID").getValue().toString());
                id.setText(data.child("EMAIL").getValue().toString());
                gender.setText(data.child("GENDER").getValue().toString());
                status.setText(data.child("STATUS").getValue().toString());
                description.setText(data.child("DESCRIPTION").getValue().toString());
                dob.setText(data.child("DOB").getValue().toString());
                city.setText(data.child("HOMETOWN").getValue().toString());

                if (!data.child("THUMB").getValue().toString().equals("default")) {
                    prof = data.child("THUMB").getValue().toString();
                    Picasso.with(getActivity()).load(prof).into(profile);
                } else
                    prof = "default";

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user.getUid())) {
                    reference2.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            friend.setText("" + dataSnapshot.getChildrenCount());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    friend.setText("0");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                al.clear();
                for (final DataSnapshot data : dataSnapshot.getChildren()) {

                    for (final DataSnapshot data1 : data.getChildren()) {
                        if (data1.getKey().equals(user.getUid())) {

                            hide.setVisibility(View.GONE);

                            for (final DataSnapshot data2 : data1.getChildren()) {
                                reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
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
                                            if (data2.child("LIKE").hasChild(user.getUid()))
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

                                        TimelineAdapter td = new TimelineAdapter(getActivity(), al);
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
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        reference4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user.getUid())) {
                    reference4.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            request.setText("" + dataSnapshot.getChildrenCount());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    request.setText("0");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        reference5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user.getUid())) {
                    reference5.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            sent.setText("" + dataSnapshot.getChildrenCount());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    sent.setText("0");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
