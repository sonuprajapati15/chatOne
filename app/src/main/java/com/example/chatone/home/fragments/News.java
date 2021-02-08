package com.example.chatone.home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatone.R;
import com.example.chatone.home.fragments.Adapter.NewsOption;
import com.example.chatone.home.fragments.Adapter.Newsheading;
import com.example.chatone.home.fragments.Adapter.Papers;
import com.example.chatone.pojos.HeadingData;
import com.example.chatone.pojos.OptionsData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SONUsantosh on 20-12-2018.
 */

public class News extends Fragment {

    RecyclerView recv1, recv2, recv3;
    private DatabaseReference reference;
    ArrayList<OptionsData> al = new ArrayList<>();
    ArrayList<HeadingData> al2 = new ArrayList<>();
    ArrayList<OptionsData> al3 = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news, null);

        recv1 = (RecyclerView) v.findViewById(R.id.recv1);
        recv2 = (RecyclerView) v.findViewById(R.id.recv2);
        recv3 = (RecyclerView) v.findViewById(R.id.recv3);

        recv1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recv3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recv2.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        /*recv2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/


        return v;
    }


    @Override
    public void onStart() {
        super.onStart();

        reference = FirebaseDatabase.getInstance().getReference().child("NEWSTIME");
        al.clear();

        reference.child("NEWS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    OptionsData op = new OptionsData();
                    op.setImage(data.child("IMAGE").getValue().toString());
                    op.setTitle(data.child("TITLE").getValue().toString());
                    op.setUrl(data.child("URL").getValue().toString());
                    al.add(op);
                }

                NewsOption ad = new NewsOption(getActivity(), al);
                recv1.setAdapter(ad);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        al3.clear();
        reference.child("PAPERS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    OptionsData op = new OptionsData();
                    op.setImage(data.child("IMAGE").getValue().toString());
                    op.setUrl(data.child("URL").getValue().toString());
                    al3.add(op);
                }

                Papers ad3 = new Papers(getActivity(), al3);
                recv3.setAdapter(ad3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue req = Volley.newRequestQueue(getActivity());

        String url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=0479426f99a44f6e8fd23bc57f4ea7b9";

        StringRequest strq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    al2.clear();
                    JSONObject job = new JSONObject(s);
                    if (job.getString("status").equals("ok")) {

                        JSONArray jar = job.getJSONArray("articles");

                        for (int i = 0; i < jar.length(); i++) {
                            JSONObject job1 = jar.getJSONObject(i);
                            HeadingData d = new HeadingData();
                            d.setImage(job1.getString("urlToImage"));
                            d.setAuthor(job1.getString("author"));
                            d.setDate(job1.getString("publishedAt"));
                            d.setDescription(job1.getString("description"));
                            d.setUrl(job1.getString("url"));
                            d.setTitle(job1.getString("title"));
                            al2.add(d);
                        }
                        Newsheading ad1 = new Newsheading(getActivity(), al2);
                        recv2.setAdapter(ad1);
                    } else {
                        Toast.makeText(getActivity(), "Error in Generating result", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        req.add(strq);


    }
}
