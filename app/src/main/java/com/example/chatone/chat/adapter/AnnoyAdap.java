package com.example.chatone.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.pojos.MessageData;

import java.util.ArrayList;

/**
 * Created by sonu on 06-01-2019.
 */

public class AnnoyAdap extends RecyclerView.Adapter<AnnoyAdap.Viewholder> {


    Context context;
    ArrayList<MessageData> al;


    public AnnoyAdap(Context context, ArrayList<MessageData> al) {
        this.context = context;
        this.al = al;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.annonymousmsg, null);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {

        if (al.get(position).getType().equals("SEND")) {
            holder.send.setVisibility(View.VISIBLE);
            holder.smessage.setText(al.get(position).getMessage());
            holder.stime.setText(al.get(position).getTime());
        } else {
            holder.recive.setVisibility(View.VISIBLE);
            holder.rmessage.setText(al.get(position).getMessage());
            holder.rtime.setText(al.get(position).getTime());
        }


    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        LinearLayout send, recive;
        TextView smessage, rmessage, stime, rtime;


        public Viewholder(View itemView) {
            super(itemView);
            smessage = (TextView) itemView.findViewById(R.id.smessage);
            rmessage = (TextView) itemView.findViewById(R.id.rmessage);
            stime = (TextView) itemView.findViewById(R.id.stime);
            rtime = (TextView) itemView.findViewById(R.id.rtime);
            send = (LinearLayout) itemView.findViewById(R.id.send);
            recive = (LinearLayout) itemView.findViewById(R.id.recive);

        }
    }
}
