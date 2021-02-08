package com.example.chatone.VideoView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatone.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Play extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    TextView title, channel, time, description;
    private YouTubePlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        playerView = (YouTubePlayerView) findViewById(R.id.player);

        playerView.initialize("AIzaSyB7ug8gYDb0mbrd7pob8m7sUdAZESeahcI", this);
        title = (TextView) findViewById(R.id.title);
        channel = (TextView) findViewById(R.id.channel);
        time = (TextView) findViewById(R.id.time);
        description = (TextView) findViewById(R.id.description);


        time.setText(getIntent().getStringExtra("PUBLISHED"));
        title.setText(getIntent().getStringExtra("TITLE"));
        channel.setText(getIntent().getStringExtra("CHANNEL"));
        description.setText(getIntent().getStringExtra("DESCRIPTION"));


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        if (!b) {
            youTubePlayer.cueVideo(getIntent().getStringExtra("ID"));
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Failed to play", Toast.LENGTH_LONG).show();

    }
}
