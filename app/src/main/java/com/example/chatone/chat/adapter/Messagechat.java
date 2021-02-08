package com.example.chatone.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.pojos.MessageData;
import com.example.chatone.profile.UserImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SONUsantosh on 26-12-2018.
 */

public class Messagechat extends RecyclerView.Adapter<Messagechat.Viewholder> {

    Context context;
    ArrayList<MessageData> al;

    public Messagechat(Context context, ArrayList<MessageData> al)
    {
        this.context=context;
        this.al=al;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.message, null);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {

        if(al.get(position).getType().equals("send"))
        {
            holder.send.setVisibility(View.VISIBLE);
            if(al.get(position).getSeen().equals("2"))
                holder.seen.setVisibility(View.VISIBLE);
            holder.smessage.setText(al.get(position).getMessage());
            holder.stime.setText(al.get(position).getTime());

            if(!al.get(position).getReply().equals("default")) {
                holder.simage.setVisibility(View.VISIBLE);
                Picasso.with(context).load(al.get(position).getReply()).resize(220,190).centerCrop().into(holder.simage);
            }



            if(!al.get(position).getImage().equals("default"))
                Picasso.with(context).load(al.get(position).getImage()).into(holder.sprofile);

        }

        else
        {
            holder.recive.setVisibility(View.VISIBLE);
            holder.rmessage.setText(al.get(position).getMessage());
            holder.rtime.setText(al.get(position).getTime());
            if(!al.get(position).getImage().equals("default"))
                Picasso.with(context).load(al.get(position).getImage()).into(holder.rprofile);

            if(!al.get(position).getReply().equals("default")) {
                holder.rimage.setVisibility(View.VISIBLE);
                Picasso.with(context).load(al.get(position).getReply()).resize(220,190).centerCrop().into(holder.rimage);
            }


        }



        holder.simage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, UserImage.class);
                i.putExtra("IMAGE",al.get(position).getReply());
                context.startActivity(i);
            }
        });

        holder.rimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, UserImage.class);
                i.putExtra("IMAGE",al.get(position).getReply());
                context.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        LinearLayout send,recive;
        ImageView simage,rimage,seen;
        TextView smessage,rmessage,stime,rtime;
         CircleImageView sprofile,rprofile;

        public Viewholder(View itemView) {
            super(itemView);
            simage = (ImageView) itemView.findViewById(R.id.simage);
            rimage = (ImageView) itemView.findViewById(R.id.rimage);
            sprofile = (CircleImageView) itemView.findViewById(R.id.sprofile);
            rprofile = (CircleImageView) itemView.findViewById(R.id.rprofile);
            smessage = (TextView) itemView.findViewById(R.id.smessage);
            rmessage = (TextView) itemView.findViewById(R.id.rmessage);
            stime = (TextView) itemView.findViewById(R.id.stime);
            rtime = (TextView) itemView.findViewById(R.id.rtime);
            seen = (ImageView) itemView.findViewById(R.id.seen);
            send=(LinearLayout) itemView.findViewById(R.id.send);
            recive=(LinearLayout) itemView.findViewById(R.id.recive);

        }
    }
}
