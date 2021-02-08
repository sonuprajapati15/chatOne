package com.example.chatone.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.home.Upload;
import com.example.chatone.home.fragments.Adapter.TimelineAdapter;
import com.example.chatone.pojos.Timelinedata;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by SONUsantosh on 20-12-2018.
 *
 */

public class Timeline extends Fragment {

    CardView cv;
    RecyclerView recv;
    ImageView tt;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref,post,friends;

     ArrayList<Timelinedata> al=new ArrayList<>();
     ArrayList<String> uid=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.timeline,null);
        cv=(CardView)v.findViewById(R.id.cv);
        recv=(RecyclerView)v.findViewById(R.id.recv);
        tt=(ImageView)v.findViewById(R.id.tt);

         ref= FirebaseDatabase.getInstance().getReference().child("USERS");
         post= FirebaseDatabase.getInstance().getReference().child("POST");
         friends= FirebaseDatabase.getInstance().getReference().child("FRIENDS");
         recv.setLayoutManager(new GridLayoutManager(getActivity(),1));



        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(), Upload.class));
             }
        });

        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Upload.class));
            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


        friends.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user.getUid()))
                {
                    uid.clear();
                     for(DataSnapshot data:dataSnapshot.child(user.getUid()).getChildren())
                        uid.add(data.getKey());

                    if(uid.size()!=0)
                       call();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void call() {

         post.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                al.clear();

                for(final DataSnapshot data:dataSnapshot.getChildren()) {

                    for (final DataSnapshot data1 : data.getChildren()) {
                        if (uid.contains(data1.getKey())) {

                            for (final DataSnapshot data2 : data1.getChildren())
                            {

                                ref.child(data1.getKey()).addValueEventListener(new ValueEventListener() {
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

                                        if (data2.hasChild("COMMENT"))
                                            d.setComent(data2.child("COMMENT").getChildrenCount() + " comments");
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

            }
        });

    }

}
