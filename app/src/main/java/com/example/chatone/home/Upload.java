package com.example.chatone.home;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class Upload extends AppCompatActivity {

    ImageView image, imgbtn;
    EditText text;
    Button btn;
    byte[] bytes;
    Uri uri;
    ProgressDialog dialog;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        image = (ImageView) findViewById(R.id.image);
        imgbtn = (ImageView) findViewById(R.id.imgbtn);
        text = (EditText) findViewById(R.id.text);
        btn = (Button) findViewById(R.id.btn);


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
                startActivityForResult(Intent.createChooser(intent, "select image"), 123);

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text.getText().toString().trim().length() < 10)
                    text.setError(" length is too short");

                else {
                    dialog.setMessage("uploading");
                    dialog.show();
                    posting();
                }
            }
        });


    }

    private void posting() {

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("POST");
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("POST").child(user.getUid());

        if (uri != null) {
            String image = DateFormat.getDateTimeInstance().format(new Date()) + "." + getExt(uri);
            UploadTask upload = storage.child(image).putBytes(bytes);

            upload.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    HashMap<String, Object> hm = new HashMap<String, Object>();
                    hm.put("TIME", DateFormat.getTimeInstance().format(new Date()));
                    hm.put("TEXT", text.getText().toString().trim());
                    hm.put("IMAGE", image);

                    reference.child(DateFormat.getDateInstance().format(new Date())).child(user.getUid()).child(DateFormat.getDateTimeInstance().format(new Date())).updateChildren(hm)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    dialog.dismiss();
                                    Toast.makeText(Upload.this, "upload succesfully", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
        } else {
            HashMap<String, Object> hm = new HashMap<String, Object>();
            hm.put("TIME", DateFormat.getTimeInstance().format(new Date()));
            hm.put("TEXT", text.getText().toString().trim());
            hm.put("IMAGE", "default");

            reference.child(DateFormat.getDateInstance().format(new Date())).child(user.getUid()).child(DateFormat.getDateTimeInstance().format(new Date())).updateChildren(hm)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            Toast.makeText(Upload.this, "upload succesfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    });
        }

    }

    private String getExt(Uri uri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {

            CropImage.activity(data.getData())
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 200)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
                Picasso.with(this).load(uri).resize(520, 290).centerCrop().into(image);

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
    public void onBackPressed() {
        super.onBackPressed();
    }
}
