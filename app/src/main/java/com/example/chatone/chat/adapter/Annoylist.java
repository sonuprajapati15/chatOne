package com.example.chatone.chat.adapter;

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
import com.example.chatone.chat.AnnonymousChat;
import com.example.chatone.pojos.MessageData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sonu on 06-01-2019.
 */

public class Annoylist extends RecyclerView.Adapter<Annoylist.Viewholder> {
    Context context;
    ArrayList<MessageData> al;

    public Annoylist(Context context, ArrayList<MessageData> al) {
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

        holder.msg.setText(al.get(position).getMessage());
        holder.time.setText(al.get(position).getTime());
        holder.name.setText(al.get(position).getName());

        if (!al.get(position).getImage().equals("default"))
            Picasso.with(context).load(al.get(position).getImage()).into(holder.profile);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AnnonymousChat.class);
                intent.putExtra("UID", al.get(position).getId());
                intent.putExtra("ID", al.get(position).getType());
                intent.putExtra("ID1", al.get(position).getUid());
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

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name, msg, time;
        CardView cv;

        public Viewholder(View itemView) {
            super(itemView);
            profile = (ImageView) itemView.findViewById(R.id.profile);
            cv = (CardView) itemView.findViewById(R.id.cv);
            msg = (TextView) itemView.findViewById(R.id.msg);
            time = (TextView) itemView.findViewById(R.id.time);
            name = (TextView) itemView.findViewById(R.id.name);

        }
    }
}
