package com.example.chatone.register;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatone.R;
import com.example.chatone.constants.FirebaseConstants;
import com.example.chatone.enums.RequestCode;
import com.example.chatone.home.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Details extends AppCompatActivity {

    RadioGroup grp, grp1;
    TextInputEditText name, dob, city, desc;
    ImageView imgbtn, cal;
    CircleImageView profile;
    DatePicker datePicker;
    static String gender = "", status = "", hometown;
    RelativeLayout ll;
    LinearLayout calander;
    Button submit;
    ProgressDialog dialog;
    DatabaseReference ref;
    FirebaseUser user;
    TextView gend;
    Uri uri;
    byte[] bytes;
    String prof;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        grp = (RadioGroup) findViewById(R.id.rgp);
        grp1 = (RadioGroup) findViewById(R.id.rgp2);
        name = (TextInputEditText) findViewById(R.id.name);
        dob = (TextInputEditText) findViewById(R.id.dob);
        city = (TextInputEditText) findViewById(R.id.city);
        desc = (TextInputEditText) findViewById(R.id.desc);
        imgbtn = (ImageView) findViewById(R.id.imageView4);
        cal = (ImageView) findViewById(R.id.cal);
        profile = (CircleImageView) findViewById(R.id.imageView3);
        datePicker = (DatePicker) findViewById(R.id.date);
        ll = (RelativeLayout) findViewById(R.id.cord);
        calander = (LinearLayout) findViewById(R.id.calander);
        submit = (Button) findViewById(R.id.submit);
        gend = (TextView) findViewById(R.id.gender);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.setIcon(R.mipmap.icon1);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select image"), RequestCode.CAMERA_ACTION.getCodeId());
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select image"), RequestCode.CAMERA_ACTION.getCodeId());
            }
        });


        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.GONE);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference().child("USERS");
        user = FirebaseAuth.getInstance().getCurrentUser();

        grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        gender = "Male";
                        break;
                    case R.id.female:
                        gender = "Fe-Male";
                        break;
                    case R.id.other:
                        gender = "Other";
                        break;
                }
            }
        });

        grp1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.single:
                        status = "Single";
                        break;
                    case R.id.married:
                        status = "Relationship";
                        break;
                    case R.id.complicate:
                        status = "Complicated";
                        break;
                }
            }
        });


        calander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hometown = city.getText().toString().trim();

                if (name.getText().toString().length() < 3)
                    name.setError("Name fied is too short");
                else if (getIntent().getStringExtra("ID").equals("1"))
                    if (gender.length() < 3)
                        Snackbar.make(findViewById(R.id.cord), "select gender", Snackbar.LENGTH_LONG).show();

                if (status.length() < 3)
                    Snackbar.make(findViewById(R.id.cord), "select Status", Snackbar.LENGTH_LONG).show();
                else if (desc.getText().toString().length() < 6)
                    desc.setError("Select birth date");
                else {
                    dialog.setMessage("Details submitting");
                    dialog.show();
                    submitt();
                }


            }
        });


    }


    private String getExt(Uri uri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.CAMERA_ACTION.getCodeId() && resultCode == RESULT_OK) {

            CropImage.activity(data.getData())
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 200)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();

                Picasso.with(this).load(uri).resize(520, 530).centerCrop().into(profile);

                File file = new File(uri.getPath());
                Bitmap thumb_bit = null;
                try {
                    thumb_bit = new Compressor(this)
                            .setMaxHeight(300)
                            .setMaxWidth(500)
                            .setQuality(50)
                            .compressToBitmap(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bit.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                bytes = baos.toByteArray();


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getApplicationContext(), result.getError().toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("NAME"))
                    name.setText(dataSnapshot.child("NAME").getValue().toString());
                if (dataSnapshot.hasChild("THUMB")) {
                    prof = dataSnapshot.child("THUMB").getValue().toString();
                    Picasso.with(Details.this).load(prof).into(profile);
                } else
                    prof = "default";

                if (dataSnapshot.hasChild("DOB"))
                    dob.setText(dataSnapshot.child("DOB").getValue().toString());

                if (dataSnapshot.hasChild("HOMETOWN")) {
                    city.setText(dataSnapshot.child("HOMETOWN").getValue().toString());
                    hometown = city.getText().toString();
                }

                if (dataSnapshot.hasChild("GENDER"))
                    gender = dataSnapshot.child("GENDER").getValue().toString();

                if (dataSnapshot.hasChild("DESCRIPTION"))
                    desc.setText(dataSnapshot.child("DESCRIPTION").getValue().toString());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void submitt() {

        StorageReference storage = FirebaseStorage.getInstance().getReference().child(FirebaseConstants.FIREBASE_USERS_PATH);
        if (uri != null) {
            String imageAdd = user.getUid() + "." + getExt(uri);
            UploadTask upload = storage.child(imageAdd).putBytes(bytes);
            upload.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    updateUserData(imageAdd);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            updateUserData(null);
        }
    }

    private void updateUserData(String imageAdd) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(FirebaseConstants.NAME, name.getText().toString().trim());
        hm.put(FirebaseConstants.GENDER, gender);
        hm.put(FirebaseConstants.STATUS, status);
        hm.put(FirebaseConstants.ONLINE, false);
        hm.put(FirebaseConstants.DOB, dob.getText().toString().trim());
        if(imageAdd != null) {
            hm.put(FirebaseConstants.THUMB_IAMGE, imageAdd);
        }
        hm.put(FirebaseConstants.HOMETOWN, hometown);
        hm.put(FirebaseConstants.ID, user.getUid());
        hm.put(FirebaseConstants.DESC, desc.getText().toString().trim());

        ref.child(user.getUid()).updateChildren(hm)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SharedPreferences sp = getSharedPreferences("chatOne", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("login", true);
                        editor.commit();
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finish();
                    }
                });
    }

    private void call() {
        datePicker.setVisibility(View.VISIBLE);
        Calendar calander = Calendar.getInstance();
        datePicker.init(calander.get(Calendar.YEAR), calander.get(Calendar.MONTH), calander.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date;
                date = "" + datePicker.getYear();
                date = datePicker.getMonth() + " / " + date;
                date = datePicker.getDayOfMonth() + " / " + date;
                dob.setText(date);

            }
        });

        datePicker.setVisibility(View.GONE);
    }
}
