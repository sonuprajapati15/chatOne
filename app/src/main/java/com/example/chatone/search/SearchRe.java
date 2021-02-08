package com.example.chatone.search;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatone.News.adapter.Hadline;
import com.example.chatone.R;
import com.example.chatone.pojos.HeadingData;
import com.example.chatone.search.adapter.Friends;
import com.example.chatone.search.adapter.VideoData;
import com.example.chatone.search.adapter.Videoadap;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchRe extends AppCompatActivity {

    ImageView searchbtn, back;
    EditText search;
    TabLayout tab;
    ArrayList<HeadingData> al = new ArrayList<>();
    ArrayList<HeadingData> al1 = new ArrayList<>();
    RecyclerView recv1, recv2, recv3;
    static String keyword = "";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ArrayList<VideoData> al3 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchbtn = (ImageView) findViewById(R.id.searchbtn);
        back = (ImageView) findViewById(R.id.back);
        search = (EditText) findViewById(R.id.search);
        tab = (TabLayout) findViewById(R.id.tab);
        recv1 = (RecyclerView) findViewById(R.id.recv1);
        recv2 = (RecyclerView) findViewById(R.id.recv2);
        recv3 = (RecyclerView) findViewById(R.id.recv3);

        recv2.setVisibility(View.GONE);
        recv3.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        search.setText(getIntent().getStringExtra("URL"));

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = search.getText().toString().trim();
                call1();
                call2();
                call3();
            }
        });


        recv1.setLayoutManager(new GridLayoutManager(this, 1));
        recv2.setLayoutManager(new GridLayoutManager(this, 1));
        recv3.setLayoutManager(new GridLayoutManager(this, 1));


        tab.addTab(tab.newTab().setText("News"));
        tab.addTab(tab.newTab().setText("People"));
        tab.addTab(tab.newTab().setText("Video"));

        keyword = getIntent().getStringExtra("URL");
        recv1.setVisibility(View.VISIBLE);
        recv2.setVisibility(View.GONE);
        recv3.setVisibility(View.GONE);
        call1();

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        recv2.setVisibility(View.GONE);
                        recv3.setVisibility(View.GONE);
                        recv1.setVisibility(View.VISIBLE);
                        call1();
                        break;
                    case 1:
                        recv1.setVisibility(View.GONE);
                        recv3.setVisibility(View.GONE);
                        recv2.setVisibility(View.VISIBLE);
                        call2();
                        break;
                    case 2:
                        recv1.setVisibility(View.GONE);
                        recv2.setVisibility(View.GONE);
                        recv3.setVisibility(View.VISIBLE);
                        call3();
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

        String url = "https://newsapi.org/v2/everything?q=" + keyword + "&apiKey=0479426f99a44f6e8fd23bc57f4ea7b9";

        RequestQueue req = Volley.newRequestQueue(this);
        StringRequest strq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    al.clear();
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
                            al.add(d);
                        }
                        Hadline ad1 = new Hadline(SearchRe.this, al);
                        recv1.setAdapter(ad1);
                    } else {
                        Snackbar.make(findViewById(R.id.cord), "Error In Fetching Data", Snackbar.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();

                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(findViewById(R.id.cord), volleyError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
        req.add(strq);

    }

    private void call2() {

        DatabaseReference refrence = FirebaseDatabase.getInstance().getReference().child("USERS");
        al1.clear();
        refrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if ((data.child("NAME").getValue().toString().toLowerCase().contains(keyword.toLowerCase()) || keyword.toLowerCase().contains(data.child("NAME").getValue().toString().toLowerCase()) || keyword.toLowerCase().contains(data.child("HOMETOWN").getValue().toString().toLowerCase()) || data.child("HOMETOWN").getValue().toString().toLowerCase().contains(keyword.toLowerCase()) || data.child("EMAIL").getValue().toString().equals(keyword)) && !data.child("HEYID").getValue().toString().equals(user.getUid())) {
                        HeadingData d = new HeadingData();
                        if (data.hasChild("THUMB"))
                            d.setImage(data.child("THUMB").getValue().toString());
                        else
                            d.setImage("default");
                        d.setTitle(data.child("NAME").getValue().toString());
                        d.setId(data.child("HEYID").getValue().toString());
                        d.setAuthor(data.child("ONLINE").getValue().toString());
                        al1.add(d);
                    }
                }

                if (al1.size() == 0)
                    Snackbar.make(findViewById(R.id.cord), "No Result Found", Snackbar.LENGTH_LONG).show();

                else {
                    Friends fr = new Friends(getApplicationContext(), al1);
                    recv2.setAdapter(fr);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void call3() {

        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + keyword + "&maxResults=40&key=AIzaSyB7ug8gYDb0mbrd7pob8m7sUdAZESeahcI";

        RequestQueue req = Volley.newRequestQueue(this);
        StringRequest strq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    al3.clear();
                    JSONObject job = new JSONObject(s);
                    JSONArray jar = job.getJSONArray("items");

                    for (int i = 0; i < jar.length(); i++) {
                        JSONObject job1 = jar.getJSONObject(i);
                        if (job1.getJSONObject("id").getString("kind").equals("youtube#video")) {
                            VideoData d = new VideoData();

                            d.setId(job1.getJSONObject("id").getString("videoId"));
                            JSONObject snippet = job1.getJSONObject("snippet");
                            d.setChannel(snippet.getString("channelTitle"));
                            d.setPublishat(snippet.getString("publishedAt"));
                            d.setTitle(snippet.getString("title"));
                            d.setDescription(snippet.getString("description"));

                            JSONObject thumb = snippet.getJSONObject("thumbnails").getJSONObject("high");
                            d.setThumb(thumb.getString("url"));

                            al3.add(d);
                        }
                    }
                    Videoadap ad = new Videoadap(SearchRe.this, al3);
                    recv3.setAdapter(ad);


                } catch (JSONException e) {
                    Toast.makeText(SearchRe.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SearchRe.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        req.add(strq);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}