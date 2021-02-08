package com.example.chatone.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.pojos.HeadingData;
import com.example.chatone.profile.UserProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SONUsantosh on 25-12-2018.
 */

public class Friends extends RecyclerView.Adapter<Friends.Viewholder> {

    Context context;
    ArrayList<HeadingData> al;

    public Friends(Context context, ArrayList<HeadingData> al) {
        this.context = context;
        this.al = al;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.friends, null);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {

        holder.id.setText(al.get(position).getId());
        holder.name.setText(al.get(position).getTitle());

        if (!al.get(position).getImage().equals("default"))
            Picasso.with(context).load(al.get(position).getImage()).into(holder.image);

        if (al.get(position).getAuthor().equals("true"))
            holder.online.setVisibility(View.VISIBLE);
        else
            holder.online.setVisibility(View.GONE);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserProfile.class);
                i.putExtra("UID", al.get(position).getId());
                i.putExtra("PROFILE", al.get(position).getImage());
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        CircleImageView image, online;
        TextView name, id;
        CardView cv;

        public Viewholder(View itemView) {
            super(itemView);
            image = (CircleImageView) itemView.findViewById(R.id.image);
            online = (CircleImageView) itemView.findViewById(R.id.online);
            name = (TextView) itemView.findViewById(R.id.name);
            id = (TextView) itemView.findViewById(R.id.id);
            cv = (CardView) itemView.findViewById(R.id.cv);

        }
    }
}
