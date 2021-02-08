package com.example.chatone.home.fragments.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.News.Web;
import com.example.chatone.R;
import com.example.chatone.pojos.OptionsData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by SONUsantosh on 22-12-2018.
 */

public class Papers extends RecyclerView.Adapter<Papers.Viewholder>  {

    Context context;
    ArrayList<OptionsData> al;

    public Papers(Context context, ArrayList<OptionsData> al)
    {
        this.context=context;
        this.al=al;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.papers, null);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {
        Picasso.with(context).load(al.get(position).getImage()).resize(100,100).centerCrop().into(holder.image);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context.getApplicationContext(), Web.class);
                i.putExtra("URL",al.get(position).getUrl());
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
        CardView cv;

        public Viewholder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.option);
            cv = (CardView) itemView.findViewById(R.id.cv);

        }
    }
}
