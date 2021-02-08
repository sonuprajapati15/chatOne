package com.example.chatone.geo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.pojos.MessageData;
import com.example.chatone.profile.UserProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sonu on 05-01-2019.
 */

public class Locationadapter extends RecyclerView.Adapter<Locationadapter.Viewholder> {

    Context context;
    ArrayList<MessageData> al;

    public Locationadapter(Context context, ArrayList<MessageData> al) {
        this.al = al;
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.locationdesign, null);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {

        if (!al.get(position).getImage().equals("default"))
            Picasso.with(context).load(al.get(position).getImage()).into(holder.profile);

        if (al.get(position).getUid().equals("true"))
            holder.online.setVisibility(View.VISIBLE);
        else
            holder.online.setVisibility(View.GONE);

        holder.time.setText(al.get(position).getTime());
        holder.name.setText(al.get(position).getName());
        holder.city.setText(al.get(position).getMessage());

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

        CircleImageView profile, online;
        TextView name, city, time;
        CardView cv;

        public Viewholder(View itemView) {
            super(itemView);
            profile = (CircleImageView) itemView.findViewById(R.id.image);
            online = (CircleImageView) itemView.findViewById(R.id.online);
            cv = (CardView) itemView.findViewById(R.id.cv);
            city = (TextView) itemView.findViewById(R.id.city);
            time = (TextView) itemView.findViewById(R.id.time);
            name = (TextView) itemView.findViewById(R.id.name);

        }
    }

}
