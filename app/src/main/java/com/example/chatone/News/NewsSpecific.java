package com.example.chatone.News;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;


public class NewsSpecific extends AppCompatActivity {

    RecyclerView recv;
    ImageView back, option;
    public static ImageView google, facebook, whatsapp, twitter;
    TextView title;
    ArrayList<HeadingData> al = new ArrayList<>();
    public static BottomSheetBehavior behavior;
    Button cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        recv = (RecyclerView) findViewById(R.id.recv);
        back = (ImageView) findViewById(R.id.back);
        option = (ImageView) findViewById(R.id.more);
        google = (ImageView) findViewById(R.id.google);
        facebook = (ImageView) findViewById(R.id.face);
        twitter = (ImageView) findViewById(R.id.tweet);
        whatsapp = (ImageView) findViewById(R.id.whatsapp);
        title = (TextView) findViewById(R.id.title);
        cancel = (Button) findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(STATE_COLLAPSED);
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

                startActivity(new Intent(getApplicationContext(), Downloads.class));
            }
        });


        recv.setLayoutManager(new GridLayoutManager(this, 1));

        View BottomSheet = findViewById(R.id.design);
        behavior = BottomSheetBehavior.from(BottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        RequestQueue req = Volley.newRequestQueue(this);
        title.setText(getIntent().getStringExtra("TITLE"));

        String url = getIntent().getStringExtra("URL");
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
                        Hadline ad1 = new Hadline(NewsSpecific.this, al);
                        recv.setAdapter(ad1);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
