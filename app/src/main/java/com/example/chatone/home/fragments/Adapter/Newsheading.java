package com.example.chatone.home.fragments.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.News.Web;
import com.example.chatone.R;
import com.example.chatone.pojos.HeadingData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SONUsantosh on 22-12-2018.
 */

public class Newsheading extends RecyclerView.Adapter<Newsheading.Viewholder> {
    Context context;
    ArrayList<HeadingData> al;

    public Newsheading(FragmentActivity activity, ArrayList<HeadingData> l) {
        al = l;
        context = activity;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.newsheading, null);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {

        holder.title.setText(al.get(position).getTitle());
        holder.date.setText(al.get(position).getDate());
        if (!al.get(position).getAuthor().equals("null"))
            holder.author.setText(al.get(position).getAuthor());
        else
            holder.author.setText("");

        holder.description.setText(al.get(position).getDescription());

        if (!al.get(position).getImage().equals("null"))
            if (!al.get(position).getImage().equals(""))
                Picasso.with(context).load(al.get(position).getImage()).resize(130, 90).centerCrop().into(holder.image);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(), Web.class);
                i.putExtra("URL", al.get(position).getUrl());
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
        TextView title, description, date, author;
        CardView cv;

        public Viewholder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
            author = (TextView) itemView.findViewById(R.id.author);
        }
    }
}
