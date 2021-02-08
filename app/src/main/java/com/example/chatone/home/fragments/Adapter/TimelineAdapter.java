package com.example.chatone.home.fragments.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.account.Account;
import com.example.chatone.account.Comments;
import com.example.chatone.pojos.Timelinedata;
import com.example.chatone.profile.UserImage;
import com.example.chatone.profile.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SONUsantosh on 28-12-2018.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.Viewholder> {

    Context context;
    ArrayList<Timelinedata> al;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("POST");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public TimelineAdapter(Context context, ArrayList<Timelinedata> al) {
        this.context = context;
        this.al = al;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.timelinedesign, null);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, final int position) {

        holder.comnt.setText(al.get(position).getComent());

        if (!al.get(position).getImage().equals("default")) {
            holder.image.setVisibility(View.VISIBLE);
            Picasso.with(context).load(al.get(position).getImage()).resize(710, 565).centerCrop().into(holder.image);
        }

        if (!al.get(position).getProfile().equals("default"))
            Picasso.with(context).load(al.get(position).getProfile()).into((holder.profile));


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UserImage.class);
                i.putExtra("IMAGE", al.get(position).getImage());
                context.startActivity(i);

            }
        });

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UserProfile.class);
                i.putExtra("UID", al.get(position).getData1());
                i.putExtra("PROFILE", al.get(position).getProfile());
                context.startActivity(i);
            }
        });

        holder.text.setText(al.get(position).getText());
        holder.time.setText(al.get(position).getTime());
        holder.name.setText(al.get(position).getName());
        holder.comnt.setText(al.get(position).getComent());
        holder.likes.setText(al.get(position).getLikes());

        if (al.get(position).getId().equals("2"))
            holder.like.setImageResource(R.drawable.like2);
        else
            holder.like.setImageResource(R.drawable.like1);


        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Account.class);
                i.putExtra("ID", "2");
                i.putExtra("TITLE", "LIKES");
                i.putExtra("URL1", al.get(position).getData());
                i.putExtra("URL2", al.get(position).getData1());
                i.putExtra("URL3", al.get(position).getSender());

                context.startActivity(i);
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (al.get(position).getId().equals("1")) {
                    ref.child(al.get(position).getData()).child(al.get(position).getData1()).child(al.get(position).getSender()).child("LIKE").child(user.getUid()).setValue("like")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    ;
                                    holder.like.setImageResource(R.drawable.like2);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "error occors", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                if (al.get(position).getId().equals("2")) {
                    ref.child(al.get(position).getData()).child(al.get(position).getData1()).child(al.get(position).getSender()).child("LIKE").child(user.getUid()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    holder.like.setImageResource(R.drawable.like1);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "error occors", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });

        holder.cmntimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Comments.class);
                i.putExtra("URL1", al.get(position).getData());
                i.putExtra("URL2", al.get(position).getData1());
                i.putExtra("URL3", al.get(position).getSender());

                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return al.size();
    }


    class Viewholder extends RecyclerView.ViewHolder {

        ImageView image, like, cmntimg;
        TextView name, time, text, comnt, likes;
        CircleImageView profile;

        public Viewholder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            cmntimg = (ImageView) itemView.findViewById(R.id.cmntimg);
            like = (ImageView) itemView.findViewById(R.id.like);
            profile = (CircleImageView) itemView.findViewById(R.id.circleImageView);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            text = (TextView) itemView.findViewById(R.id.text);
            comnt = (TextView) itemView.findViewById(R.id.cmnt);
            likes = (TextView) itemView.findViewById(R.id.likes);

        }
    }


}
