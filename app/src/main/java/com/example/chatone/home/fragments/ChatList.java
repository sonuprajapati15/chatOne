package com.example.chatone.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.chat.Annonymous;
import com.example.chatone.home.fragments.Adapter.ChatListAdapter;
import com.example.chatone.pojos.MessageData;
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
 */

public class ChatList extends Fragment {

    RecyclerView recv;
    ArrayList<MessageData> al = new ArrayList<>();
    ArrayList<String> uid = new ArrayList<>();
    DatabaseReference reference, reference2, reference3;
    FirebaseUser user;
    CardView cv;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chatlist, null);

        recv = (RecyclerView) v.findViewById(R.id.recv);
        cv = (CardView) v.findViewById(R.id.cv);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Annonymous.class);
                getActivity().startActivity(intent);
            }
        });

        recv.setLayoutManager(new GridLayoutManager(getActivity(), 1));


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("USERS");
        reference2 = FirebaseDatabase.getInstance().getReference().child("CHATTING");
        reference3 = FirebaseDatabase.getInstance().getReference().child("LASTMESSAGE").child("CHAT").child(user.getUid());

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user.getUid())) {
                    uid.clear();
                    for (DataSnapshot data : dataSnapshot.child(user.getUid()).getChildren())
                        uid.add(data.getKey());
                    call();
                } else
                    Toast.makeText(getActivity(), "You have no chats add friend", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void call() {
        al.clear();
        for (int i = 0; i < uid.size(); i++) {

            final String id = uid.get(i);
            reference.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {

                    reference3.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot datasnap) {
                            MessageData data = new MessageData();

                            if (datasnap.hasChild("MESSAGE")) {

                                if (!dataSnapshot.child("THUMB").getValue().toString().equals("default"))
                                    data.setImage(dataSnapshot.child("THUMB").getValue().toString());
                                else
                                    data.setImage("default");

                                data.setName(dataSnapshot.child("NAME").getValue().toString());
                                data.setType(dataSnapshot.child("ONLINE").getValue().toString());

                                data.setMessage(datasnap.child("MESSAGE").getValue().toString());
                                data.setTime(datasnap.child("TIME").getValue().toString());
                                data.setId(dataSnapshot.child("HEYID").getValue().toString());

                                al.add(data);

                                ChatListAdapter fr = new ChatListAdapter(getActivity(), al);
                                recv.setAdapter(fr);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }


    }
}
