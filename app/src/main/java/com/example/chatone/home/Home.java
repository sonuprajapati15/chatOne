package com.example.chatone.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.chatone.News.Downloads;
import com.example.chatone.R;
import com.example.chatone.geo.Geolocation;
import com.example.chatone.home.fragments.ChatList;
import com.example.chatone.home.fragments.News;
import com.example.chatone.home.fragments.Profile;
import com.example.chatone.home.fragments.Timeline;
import com.example.chatone.home.fragments.Video;
import com.example.chatone.register.Details;
import com.example.chatone.register.LoginAndSignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Home extends AppCompatActivity {

    FragmentManager fm;
    ImageView more, search1;
    CircleImageView profile;
    FirebaseUser user;
    RelativeLayout cv1;
    DatabaseReference reference;
    FloatingActionButton location;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.timeline:
                    cv1.setVisibility(View.VISIBLE);
                    fm.beginTransaction().replace(R.id.frame, new Timeline()).addToBackStack("time").commit();
                    return true;

                case R.id.news:
                    cv1.setVisibility(View.VISIBLE);
                    fm.beginTransaction().replace(R.id.frame, new News()).addToBackStack("news").commit();
                    return true;

                case R.id.cam:
                    cv1.setVisibility(View.GONE);
                    fm.beginTransaction().replace(R.id.frame, new Video()).addToBackStack("search").commit();
                    return true;

                case R.id.chat:
                    cv1.setVisibility(View.VISIBLE);
                    fm.beginTransaction().replace(R.id.frame, new ChatList()).addToBackStack("chat").commit();
                    return true;

                case R.id.profile:
                    cv1.setVisibility(View.VISIBLE);
                    fm.beginTransaction().replace(R.id.frame, new Profile()).addToBackStack("profile").commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        more = (ImageView) findViewById(R.id.option);
        search1 = (ImageView) findViewById(R.id.search1);
        cv1 = (RelativeLayout) findViewById(R.id.cv1);
        profile = (CircleImageView) findViewById(R.id.imageView3);
        location = (FloatingActionButton) findViewById(R.id.location);


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Geolocation.class));
            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("USERS");


        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv1.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.frame, new Video()).addToBackStack("search").commit();
            }
        });


        fm = getSupportFragmentManager();

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu p = new PopupMenu(Home.this, view);
                MenuInflater inflater = p.getMenuInflater();
                inflater.inflate(R.menu.menu, p.getMenu());
                p.show();

                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.downloads:
                                Intent intent = new Intent(Home.this, Downloads.class);
                                startActivity(intent);
                                return true;

                            case R.id.update:
                                Intent i = new Intent(Home.this, Details.class);
                                i.putExtra("ID", "2");
                                startActivity(i);
                                return true;

                            case R.id.logout:
                                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                                builder.setCancelable(false);
                                builder.setTitle("Warrning");
                                builder.setMessage("Are You Sure !!!" + "\n" + "you want to logout");
                                builder.setPositiveButton("No", null);
                                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        FirebaseAuth.getInstance().signOut();
                                        SharedPreferences sp = getSharedPreferences("hey", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putBoolean("login", false);
                                        editor.commit();
                                        startActivity(new Intent(Home.this, LoginAndSignUpActivity.class));
                                        finish();
                                    }
                                });
                                builder.create().show();
                                return true;


                            default:
                                return false;
                        }
                    }
                });

            }
        });

        fm.beginTransaction().replace(R.id.frame, new Timeline()).addToBackStack("time").commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    @Override
    protected void onStart() {
        super.onStart();

        reference.child(user.getUid()).child("ONLINE").setValue("true")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });

        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                if (data.hasChild("NAME")) {
                    String hey = data.child("NAME").getValue().toString();
                }

                if (data.hasChild("THUMB"))
                    Picasso.with(Home.this).load(data.child("THUMB").getValue().toString()).into(profile);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Home.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        reference.child(user.getUid()).child("ONLINE").setValue("false")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }


    @Override
    public void onBackPressed() {

        if (fm.getBackStackEntryCount() > 0) {

            fm.popBackStack();

        }

        if (fm.getBackStackEntryCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setCancelable(false);
            builder.setTitle("Warrning");
            builder.setMessage("Are You Sure !!!" + "\n" + "you want to Exit");
            builder.setNegativeButton("No", null);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.create().show();

        }

    }
}
