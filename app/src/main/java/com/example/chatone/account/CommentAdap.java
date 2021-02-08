package com.example.chatone.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.pojos.MessageData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sonu on 03-01-2019.
 */

public class CommentAdap extends RecyclerView.Adapter<CommentAdap.Viewholder> {

    ArrayList<MessageData> al;
    Context context;

    CommentAdap(Context context, ArrayList<MessageData> al) {
        this.al = al;
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cmntdesign, null);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {

        if (!al.get(position).getProfile().equals("default"))
            Picasso.with(context).load(al.get(position).getProfile()).into(holder.profile);

        holder.name.setText(al.get(position).getName());
        holder.time.setText(al.get(position).getTime());
        holder.text.setText(al.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {

        TextView name, time, text;
        CircleImageView profile;

        public Viewholder(View itemView) {
            super(itemView);
            profile = (CircleImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            text = (TextView) itemView.findViewById(R.id.text);


        }
    }

}
