package com.example.chatone.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.VideoView.Play;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sonu on 02-01-2019.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.Viewholder> {


    Context context;
    ArrayList<VideoData> al;

    public VideoAdapter(Context context, ArrayList<VideoData> al) {
        this.context = context;
        this.al = al;
    }


    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.videodesign, null);
        return new Viewholder(v);
    }


    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {

        if (al.get(position).getT().equals("2")) {
            holder.cv2.setVisibility(View.VISIBLE);
            Picasso.with(context).load(al.get(position).getThumb()).resize(160, 80).centerCrop().into(holder.option2);
            holder.text.setText(al.get(position).description);

        }

        if (al.get(position).getT().equals("1")) {
            holder.cv.setVisibility(View.VISIBLE);
            Picasso.with(context).load(al.get(position).getThumb()).resize(250, 140).centerCrop().into(holder.option);

        }
        if (al.get(position).getT().equals("3")) {
            holder.cv3.setVisibility(View.VISIBLE);
            Picasso.with(context).load(al.get(position).getThumb()).resize(135, 170).centerCrop().into(holder.option3);

        }


        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, Play.class);
                i.putExtra("ID", al.get(position).getId());
                i.putExtra("CHANNEL", al.get(position).getChannel());
                i.putExtra("DESCRIPTION", al.get(position).getDescription());
                i.putExtra("TITLE", al.get(position).getTitle());
                i.putExtra("PUBLISHED", al.get(position).getPublishat());
                context.startActivity(i);


            }
        });


        holder.cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, Play.class);
                i.putExtra("ID", al.get(position).getId());
                i.putExtra("CHANNEL", al.get(position).getChannel());
                i.putExtra("DESCRIPTION", al.get(position).getDescription());
                i.putExtra("TITLE", al.get(position).getTitle());
                i.putExtra("PUBLISHED", al.get(position).getPublishat());
                context.startActivity(i);


            }
        });

        holder.cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, Play.class);
                i.putExtra("ID", al.get(position).getId());
                i.putExtra("CHANNEL", al.get(position).getChannel());
                i.putExtra("DESCRIPTION", al.get(position).getDescription());
                i.putExtra("TITLE", al.get(position).getTitle());
                i.putExtra("PUBLISHED", al.get(position).getPublishat());
                context.startActivity(i);


            }
        });

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        CardView cv, cv3;
        LinearLayout cv2;
        ImageView option, option2, option3;
        TextView text;


        public Viewholder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv);
            cv2 = (LinearLayout) itemView.findViewById(R.id.cv2);
            cv3 = (CardView) itemView.findViewById(R.id.cv3);
            option = (ImageView) itemView.findViewById(R.id.option);
            option2 = (ImageView) itemView.findViewById(R.id.option2);
            option3 = (ImageView) itemView.findViewById(R.id.option3);
            text = (TextView) itemView.findViewById(R.id.text);

        }
    }


}
