package com.example.chatone.home.fragments.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.chat.Chat;
import com.example.chatone.pojos.MessageData;
import com.example.chatone.profile.UserImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SONUsantosh on 26-12-2018.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Viewholder> {

    Context context;
    ArrayList<MessageData> al;

    public ChatListAdapter(Context context, ArrayList<MessageData> al) {
        this.al = al;
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listdesign, null);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {

        if (!al.get(position).getImage().equals("default"))
            Picasso.with(context).load(al.get(position).getImage()).into(holder.profile);

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UserImage.class);
                i.putExtra("IMAGE", al.get(position).getImage());
                context.startActivity(i);
            }
        });

        if (al.get(position).getType().equals("true"))
            holder.online.setVisibility(View.VISIBLE);
        else
            holder.online.setVisibility(View.GONE);

        holder.msg.setText(al.get(position).getMessage());
        holder.time.setText(al.get(position).getTime());
        holder.name.setText(al.get(position).getName());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("UID", al.get(position).getId());
                intent.putExtra("NAME", al.get(position).getName());
                intent.putExtra("PROFILE", al.get(position).getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        CircleImageView profile, online;
        TextView name, msg, time;
        CardView cv;

        public Viewholder(View itemView) {
            super(itemView);
            profile = (CircleImageView) itemView.findViewById(R.id.profile);
            online = (CircleImageView) itemView.findViewById(R.id.online);
            cv = (CardView) itemView.findViewById(R.id.cv);
            msg = (TextView) itemView.findViewById(R.id.msg);
            time = (TextView) itemView.findViewById(R.id.time);
            name = (TextView) itemView.findViewById(R.id.name);

        }
    }
}
