package com.example.chatone.videofragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatone.R;
import com.example.chatone.search.adapter.VideoAdapter;
import com.example.chatone.search.adapter.VideoData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sonu on 03-01-2019.
 */

public class CurrentAffairs extends Fragment {

    RecyclerView recv, recv2, recv3, recv4, recv5;
    private ArrayList<VideoData> al1 = new ArrayList<>(), al2 = new ArrayList<>();
    TextView text1, text2, text3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.videofragment, null);
        recv = (RecyclerView) v.findViewById(R.id.recv);
        recv2 = (RecyclerView) v.findViewById(R.id.recv1);
        recv3 = (RecyclerView) v.findViewById(R.id.recv2);
        recv4 = (RecyclerView) v.findViewById(R.id.recv3);
        recv5 = (RecyclerView) v.findViewById(R.id.recv4);
        text1 = (TextView) v.findViewById(R.id.text1);
        text2 = (TextView) v.findViewById(R.id.text2);
        text3 = (TextView) v.findViewById(R.id.text3);

        recv2.setVisibility(View.GONE);
        recv4.setVisibility(View.GONE);
        recv3.setVisibility(View.GONE);
        text2.setVisibility(View.GONE);
        text3.setVisibility(View.GONE);
        text1.setText("watch list");

        recv.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        recv5.setVisibility(View.VISIBLE);
        recv5.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        call1();
        call2();


        return v;
    }

    private void call1() {
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=current affairs and aib episodes&maxResults=30&key=AIzaSyB7ug8gYDb0mbrd7pob8m7sUdAZESeahcI";

        RequestQueue req = Volley.newRequestQueue(getActivity());
        StringRequest strq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    al1.clear();
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
                            d.setT("1");

                            al1.add(d);
                        }
                    }
                    VideoAdapter ad = new VideoAdapter(getActivity(), al1);
                    recv.setAdapter(ad);


                } catch (JSONException e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        req.add(strq);

    }

    private void call2() {
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=tsp&maxResults=40&key=AIzaSyB7ug8gYDb0mbrd7pob8m7sUdAZESeahcI";

        RequestQueue req = Volley.newRequestQueue(getActivity());
        StringRequest strq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    al2.clear();
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
                            d.setT("2");

                            al2.add(d);
                        }
                    }
                    VideoAdapter ad = new VideoAdapter(getActivity(), al2);
                    recv5.setAdapter(ad);


                } catch (JSONException e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        req.add(strq);
    }

}
