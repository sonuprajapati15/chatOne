package com.example.chatone.home.fragments.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.News.NewsSpecific;
import com.example.chatone.R;
import com.example.chatone.pojos.OptionsData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SONUsantosh on 21-12-2018.
 */

public class NewsOption extends RecyclerView.Adapter<NewsOption.Viewholder> {

    Context context;
    ArrayList<OptionsData> al;

    public NewsOption(Context context, ArrayList<OptionsData> al) {
        this.context = context;
        this.al = al;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.newsoption, null);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {
        holder.title.setText(al.get(position).getTitle());
        Picasso.with(context).load(al.get(position).getImage()).resize(200, 108).centerCrop().into(holder.image);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(), NewsSpecific.class);
                i.putExtra("URL", al.get(position).getUrl());
                i.putExtra("TITLE", al.get(position).getTitle());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        CardView cv;

        public Viewholder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.option);
            title = (TextView) itemView.findViewById(R.id.title);
            cv = (CardView) itemView.findViewById(R.id.cv);

        }
    }
}
