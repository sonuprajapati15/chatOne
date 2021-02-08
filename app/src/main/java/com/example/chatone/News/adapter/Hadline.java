package com.example.chatone.News.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.News.NewsSpecific;
import com.example.chatone.News.Web;
import com.example.chatone.R;
import com.example.chatone.pojos.HeadingData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by SONUsantosh on 21-12-2018.
 */

public class Hadline extends RecyclerView.Adapter<Hadline.Viewholder> {

    Context context;
    ArrayList<HeadingData> al;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SAVE").child("NEWS");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ;

    public Hadline(Context context, ArrayList<HeadingData> l) {
        this.context = context;
        al = l;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.hadline, null);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, final int position) {

        holder.title.setText(al.get(position).getTitle());

        holder.date.setText(al.get(position).getDate());
        holder.author.setText(al.get(position).getAuthor());
        holder.description.setText(al.get(position).getDescription());

        if (!al.get(position).getImage().equals("null"))
            if (!al.get(position).getImage().equals(""))
                Picasso.with(context).load(al.get(position).getImage()).resize(1205, 750).centerCrop().into(holder.image);

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("IMAGE", al.get(position).getImage());
                hm.put("AUTHOR", al.get(position).getAuthor());
                hm.put("DATE", al.get(position).getDate());
                hm.put("DETAIL", al.get(position).getDescription());
                hm.put("URL", al.get(position).getUrl());
                hm.put("TITLE", al.get(position).getTitle());

                ref.child(user.getUid()).child(DateFormat.getDateTimeInstance().format(new Date())).updateChildren(hm)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                holder.save.setImageResource(R.drawable.ic_bookmark_black_24dp);
                                holder.save.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bounce));
                                Toast.makeText(context, "bookmark added", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "some error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewsSpecific.behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                NewsSpecific.google.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, al.get(position).getUrl());
                            intent.setPackage("com.google.android.apps.plus");
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(context, "install official google plus", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                NewsSpecific.facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, al.get(position).getUrl());
                            intent.setPackage("com.facebook.katana");
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(context, "install facebook", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                NewsSpecific.twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, al.get(position).getUrl());
                            intent.setPackage("advanced.twitter.android");
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(context, "install Official twitter", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                NewsSpecific.whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_TEXT, al.get(position).getUrl());
                            intent.setPackage("com.whatsapp");
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(context, "install whatsapp", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context.getApplicationContext(), Web.class);
                i.putExtra("URL", al.get(position).getUrl());
                context.startActivity(i);
            }
        });

        holder.read.setOnClickListener(new View.OnClickListener() {
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

        ImageView image, save, share;
        TextView title, description, date, author;
        CardView cv;
        TextView read;

        public Viewholder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            share = (ImageView) itemView.findViewById(R.id.share);
            save = (ImageView) itemView.findViewById(R.id.download);
            cv = (CardView) itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
            author = (TextView) itemView.findViewById(R.id.author);
            read = (TextView) itemView.findViewById(R.id.read);
        }
    }
}
