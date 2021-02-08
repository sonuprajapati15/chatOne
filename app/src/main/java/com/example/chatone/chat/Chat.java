package com.example.chatone.chat;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatone.R;
import com.example.chatone.pojos.MessageData;
import com.example.chatone.chat.adapter.Messagechat;
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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Chat extends AppCompatActivity {

    ImageView back, more, send, attach, sendbtn, img;
    CircleImageView profile;
    TextView name;
    EditText box;
    TextInputEditText text;
    RecyclerView recv;
    FirebaseUser user;
    DatabaseReference reference, reference2, reference3, reference4;
    String uid, sendprofile;
    ArrayList<MessageData> al = new ArrayList<>();
    CoordinatorLayout ll;
    byte[] bytes;
    Uri uri;
    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        back = (ImageView) findViewById(R.id.back);
        more = (ImageView) findViewById(R.id.more);
        send = (ImageView) findViewById(R.id.send);
        sendbtn = (ImageView) findViewById(R.id.sendbtn);
        attach = (ImageView) findViewById(R.id.attach);
        img = (ImageView) findViewById(R.id.img);
        profile = (CircleImageView) findViewById(R.id.profile);
        name = (TextView) findViewById(R.id.name);
        box = (EditText) findViewById(R.id.box);
        text = (TextInputEditText) findViewById(R.id.text);
        recv = (RecyclerView) findViewById(R.id.chatting);
        ll = (CoordinatorLayout) findViewById(R.id.ll);

        recv.setLayoutManager(new GridLayoutManager(this, 1));

        name.setText(getIntent().getStringExtra("NAME"));
        if (!getIntent().getStringExtra("PROFILE").equals("default"))
            Picasso.with(this).load(getIntent().getStringExtra("PROFILE")).into(profile);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p = new PopupMenu(Chat.this, v);
                MenuInflater inflater = p.getMenuInflater();
                inflater.inflate(R.menu.chat, p.getMenu());
                p.show();

                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.send:
                                Intent intent = new Intent(getApplicationContext(), AnnonymousChat.class);
                                intent.putExtra("UID", uid);
                                intent.putExtra("ID", "SEND");
                                intent.putExtra("NAME", name.getText().toString());
                                intent.putExtra("PROFILE", getIntent().getStringExtra("PROFILE"));
                                startActivity(intent);
                                return true;

                            default:
                                return false;
                        }
                    }
                });

            }
        });


        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select image"), 123);
            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text.getText().toString().length() < 1)
                    text.setText(" ");

                String msg = text.getText().toString().trim();

                call1(msg);
            }
        });


        uid = getIntent().getStringExtra("UID");
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("USERS");
        reference4 = FirebaseDatabase.getInstance().getReference().child("FRIENDS");
        reference2 = FirebaseDatabase.getInstance().getReference().child("CHATTING");
        reference3 = FirebaseDatabase.getInstance().getReference().child("LASTMESSAGE");


        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sendprofile = dataSnapshot.child("THUMB").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (box.getText().toString().isEmpty())
                    Snackbar.make(findViewById(R.id.cord), "message box is empty", Snackbar.LENGTH_LONG).show();

                else {

                    reference4.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(user.getUid()) && dataSnapshot.child(user.getUid()).hasChild(uid))
                                sending(box.getText().toString());

                            else
                                Snackbar.make(findViewById(R.id.cord), "you are unknown to him/her", Snackbar.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Snackbar.make(findViewById(R.id.cord), "connectivity problem", Snackbar.LENGTH_LONG).show();

                        }
                    });

                }
            }
        });
    }

    private String getExt(Uri uri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void call1(final String msg) {

        final String date = DateFormat.getDateTimeInstance().format(new Date());
        final String time = DateFormat.getTimeInstance().format(new Date());

        storage = FirebaseStorage.getInstance().getReference().child("SEND");
        String image1 = DateFormat.getDateTimeInstance().format(new Date()) + "." + getExt(uri);
        UploadTask upload = storage.child(image1).putBytes(bytes);

        upload.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("MESSAGE", msg);
                hm.put("TYPE", "send");
                hm.put("IMAGE", image1);
                hm.put("TIME", time);
                hm.put("SEEN", "1");
                ll.setVisibility(View.GONE);

                reference2.child(user.getUid()).child(uid).child(date).updateChildren(hm)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task1) {
                                HashMap<String, Object> hm1 = new HashMap<>();
                                hm1.put("MESSAGE", msg);
                                hm1.put("IMAGE", "");
                                hm1.put("TYPE", "recive");
                                hm1.put("TIME", time);
                                reference2.child(uid).child(user.getUid()).child(date).updateChildren(hm1)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                final HashMap<String, Object> hm2 = new HashMap<>();
                                                hm2.put("MESSAGE", "attatchment ");
                                                hm2.put("TIME", time);
                                                reference3.child("CHAT").child(user.getUid()).child(uid).updateChildren(hm2)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                reference3.child("CHAT").child(uid).child(user.getUid()).updateChildren(hm2)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                text.setText("");
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });

    }

    private void sending(final String text) {

        final String date = DateFormat.getDateTimeInstance().format(new Date());
        final String time = DateFormat.getTimeInstance().format(new Date());

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("MESSAGE", text);
        hm.put("TYPE", "send");
        hm.put("IMAGE", "default");
        hm.put("TIME", time);
        hm.put("SEEN", "1");
        box.setText("");

        reference2.child(user.getUid()).child(uid).child(date).updateChildren(hm)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        HashMap<String, Object> hm1 = new HashMap<>();
                        hm1.put("MESSAGE", text);
                        hm1.put("IMAGE", "defalut");
                        hm1.put("TYPE", "recive");
                        hm1.put("TIME", time);
                        reference2.child(uid).child(user.getUid()).child(date).updateChildren(hm1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        final HashMap<String, Object> hm2 = new HashMap<>();
                                        hm2.put("MESSAGE", text);
                                        hm2.put("TIME", time);
                                        reference3.child("CHAT").child(user.getUid()).child(uid).updateChildren(hm2)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        reference3.child("CHAT").child(uid).child(user.getUid()).updateChildren(hm2)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        box.setText("");
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        name.setText(getIntent().getStringExtra("NAME"));

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                al.clear();
                if (dataSnapshot.hasChild(user.getUid()))
                    if (dataSnapshot.child(user.getUid()).hasChild(uid))
                        call();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });


    }

    private void call() {

        reference2.child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot data) {
                        if (data.hasChild(uid)) {
                            for (DataSnapshot dataSnapshot : data.child(uid).getChildren()) {
                                MessageData d = new MessageData();
                                d.setId(dataSnapshot.getKey());
                                d.setUid(uid);
                                d.setMessage(dataSnapshot.child("MESSAGE").getValue().toString());
                                d.setTime(dataSnapshot.child("TIME").getValue().toString());
                                d.setReply(dataSnapshot.child("IMAGE").getValue().toString());

                                if (dataSnapshot.child("TYPE").getValue().toString().equals("send")) {
                                    d.setSeen(dataSnapshot.child("SEEN").getValue().toString());
                                    d.setType(dataSnapshot.child("TYPE").getValue().toString());
                                    d.setImage(sendprofile);

                                } else {
                                    d.setType(dataSnapshot.child("TYPE").getValue().toString());
                                    d.setImage(getIntent().getStringExtra("PROFILE"));
                                    reference2.child(uid).child(user.getUid()).child(dataSnapshot.getKey().toString()).child("SEEN").setValue("2")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                                }
                                            });

                                }

                                al.add(d);
                            }


                            Messagechat msg = new Messagechat(Chat.this, al);
                            recv.setAdapter(msg);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar.make(findViewById(R.id.cord), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
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
                Picasso.with(this).load(uri).resize(520, 290).centerCrop().into(img);
                ll.setVisibility(View.VISIBLE);

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
