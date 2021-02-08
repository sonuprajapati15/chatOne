package com.example.chatone.geo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.pojos.MessageData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Geolocation extends AppCompatActivity {


    TextView text;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref, reference;
    RecyclerView recv;
    String cityName = "";
    ArrayList<MessageData> a = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        text = (TextView) findViewById(R.id.text);
        recv = (RecyclerView) findViewById(R.id.recv);

        recv.setLayoutManager(new GridLayoutManager(this, 2));


        ref = FirebaseDatabase.getInstance().getReference().child("LOCATION");
        reference = FirebaseDatabase.getInstance().getReference().child("USERS");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, locationListener);

    }

    @Override
    protected void onStart() {
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot data) {
                a.clear();
                if (data.hasChild(user.getUid())) {
                    Location userloc = new Location("point a");
                    userloc.setLatitude(Double.valueOf(data.child(user.getUid()).child("LATTITUDE").getValue().toString()));
                    userloc.setLongitude(Double.valueOf(data.child(user.getUid()).child("LONGITUDE").getValue().toString()));

                    for (final DataSnapshot data1 : data.getChildren()) {
                        if (!data1.getKey().equals(user.getUid())) {
                            Location uid = new Location("point b");
                            uid.setLatitude(Double.valueOf(data1.child("LATTITUDE").getValue().toString()));
                            uid.setLongitude(Double.valueOf(data1.child("LONGITUDE").getValue().toString()));

                            if (userloc.distanceTo(uid) < 2000.00) {

                                reference.child(data1.getKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        MessageData d = new MessageData();
                                        d.setId(data1.getKey());
                                        d.setMessage(data1.child("CITY").getValue().toString());
                                        d.setTime(data1.child("TIME").getValue().toString());
                                        d.setName(dataSnapshot.child("NAME").getValue().toString());
                                        d.setUid(dataSnapshot.child("ONLINE").getValue().toString());
                                        if (!dataSnapshot.child("THUMB").getValue().toString().equals("default"))
                                            d.setImage(dataSnapshot.child("THUMB").getValue().toString());
                                        else
                                            d.setImage("default");

                                        a.add(d);

                                        Locationadapter fr = new Locationadapter(getApplicationContext(), a);
                                        recv.setAdapter(fr);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        }
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    class MyLocation implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            String longitude = String.valueOf(location.getLongitude());
            String latitude = String.valueOf(location.getLatitude());

            /*------- To get city name from coordinates -------- */
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                if (addresses.size() > 0) {
                    cityName = addresses.get(0).getLocality();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            text.setText("your Current loaction is:-" + cityName);

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("CITY", cityName);
            hm.put("LONGITUDE", longitude);
            hm.put("LATTITUDE", latitude);
            hm.put("TIME", DateFormat.getTimeInstance().format(new Date()));
            ref.child(user.getUid()).updateChildren(hm)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }


}

