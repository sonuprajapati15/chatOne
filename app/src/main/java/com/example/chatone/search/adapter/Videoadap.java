package com.example.chatone.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.VideoView.Play;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SONUsantosh on 28-12-2018.
 */

public class Videoadap extends RecyclerView.Adapter<Videoadap.Viewholder> {


    Context context;
    ArrayList<VideoData> al;

    public Videoadap(Context context, ArrayList<VideoData> al)
    {
        this.context=context;
        this.al=al;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.videoadap,null);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {
        Picasso.with(context).load(al.get(position).getThumb()).resize(400,195).centerCrop().into(holder.thumb);
        holder.time.setText(al.get(position).getPublishat());
        holder.title.setText(al.get(position).getTitle());
        holder.description.setText(al.get(position).getDescription());
        holder.channel.setText(al.get(position).getChannel());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, Play.class);
                i.putExtra("ID",al.get(position).getId());
                i.putExtra("CHANNEL",al.get(position).getChannel());
                i.putExtra("DESCRIPTION",al.get(position).getDescription());
                i.putExtra("TITLE",al.get(position).getTitle());
                i.putExtra("PUBLISHED",al.get(position).getPublishat());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView thumb;
        TextView channel,title,description,time;


        public Viewholder(View itemView) {
            super(itemView);

            cv=(CardView)itemView.findViewById(R.id.cv);
            thumb=(ImageView)itemView.findViewById(R.id.thumb);
            channel=(TextView)itemView.findViewById(R.id.channel);
            title=(TextView)itemView.findViewById(R.id.title);
            description=(TextView)itemView.findViewById(R.id.description);
            time=(TextView)itemView.findViewById(R.id.time);

        }
    }
}
