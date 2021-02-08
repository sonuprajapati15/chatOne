package com.example.chatone.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatone.R;
import com.example.chatone.search.SearchRe;
import com.example.chatone.search.adapter.VideoData;
import com.example.chatone.videofragments.Comedy;
import com.example.chatone.videofragments.Gameplay;
import com.example.chatone.videofragments.Kapil;
import com.example.chatone.videofragments.Movies;
import com.example.chatone.videofragments.Music;
import com.example.chatone.videofragments.Newsfr;
import com.example.chatone.videofragments.Status;
import com.example.chatone.videofragments.TSP;
import com.example.chatone.videofragments.TVF;
import com.example.chatone.videofragments.Trending;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * Created by SONUsantosh on 20-12-2018.
 */

public class Video extends Fragment {

    ArrayList<VideoData> newslist = new ArrayList<VideoData>();
    TabLayout tab;
    ImageView clear, search;
    EditText text;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.video, null);

        tab = (TabLayout) v.findViewById(R.id.tab);
        search = (ImageView) v.findViewById(R.id.search);
        text = (EditText) v.findViewById(R.id.text);
        clear = (ImageView) v.findViewById(R.id.clear);


        tab.addTab(tab.newTab().setText("Trending"));
        tab.addTab(tab.newTab().setText("News"));
        tab.addTab(tab.newTab().setText("Movies"));
        tab.addTab(tab.newTab().setText("Music"));
        tab.addTab(tab.newTab().setText("Comedy"));
        tab.addTab(tab.newTab().setText("Kapil Sharma"));
        tab.addTab(tab.newTab().setText("TVF"));
        tab.addTab(tab.newTab().setText("TSP"));
        tab.addTab(tab.newTab().setText("GAMEPLAY"));
        tab.addTab(tab.newTab().setText("Status"));


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), SearchRe.class);
                intent.putExtra("URL", text.getText().toString().trim());
                startActivity(intent);
            }
        });


        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video, new Trending()).addToBackStack("trending").commit();
                        break;

                    case 1:
                        int newsfr = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video, new Newsfr()).addToBackStack("newsfr").commit();
                        break;
                    case 2:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video, new Movies()).addToBackStack("movies").commit();
                        break;
                    case 3:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video, new Music()).addToBackStack("music").commit();
                        break;
                    case 4:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video, new Comedy()).addToBackStack("comedy").commit();
                        break;

                    case 5:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video, new Kapil()).addToBackStack("kapil").commit();

                        break;
                    case 6:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video, new TVF()).addToBackStack("tvf").commit();

                        break;
                    case 7:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video, new TSP()).addToBackStack("tsp").commit();

                        break;
                    case 8:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video, new Gameplay()).addToBackStack("game").commit();

                        break;

                    case 9:
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video, new Status()).addToBackStack("status").commit();

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


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.video, new Trending()).addToBackStack("trending").commit();

    }

}
