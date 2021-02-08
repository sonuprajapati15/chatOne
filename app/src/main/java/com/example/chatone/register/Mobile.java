package com.example.chatone.register;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatone.R;
import com.example.chatone.constants.FirebaseConstants;
import com.example.chatone.enums.RequestCode;
import com.example.chatone.permission.DevicePermission;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Mobile extends AppCompatActivity {
    LinearLayout ll, ll1;
    ImageView down;
    TextInputEditText mobile, otpText;
    Button send, next;
    TextView resend;
    LinearLayout nextOption;
    CircleImageView iv;
    ImageView img;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    ProgressDialog dialog;
    static String verificationId = "";
    DatabaseReference ref;
    static int count = 0;

    byte[] bytes;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);

        ll = (LinearLayout) findViewById(R.id.ll);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        nextOption = (LinearLayout) findViewById(R.id.option);
        down = (ImageView) findViewById(R.id.down);
        img = (ImageView) findViewById(R.id.img);
        iv = (CircleImageView) findViewById(R.id.pic);
        mobile = (TextInputEditText) findViewById(R.id.mobile);
        otpText = (TextInputEditText) findViewById(R.id.otp);
        send = (Button) findViewById(R.id.submit1);
        next = (Button) findViewById(R.id.submit2);
        resend = (TextView) findViewById(R.id.resend);

       loadResource();

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginAndSignUpActivity.class));
                finish();
            }
        });


        nextOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Email.class));
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count != 2) {
                    count++;
                    send.performClick();
                } else
                    Snackbar.make(findViewById(R.id.cord), "Please try again later", Snackbar.LENGTH_SHORT).show();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mobile.getText().toString().trim();
                if (phoneNumber.length() < 10 || phoneNumber.length() > 15)
                    mobile.setError("mobile no. length is not valid. ");
                else {
                    dialog.setMessage("otp sending");
                    dialog.show();
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                                    .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(Mobile.this)                 // Activity (for callback binding)
                                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(PhoneAuthCredential credential) {
                                            if(credential.getSmsCode().equals(otpText.getText().toString())){
                                                verify(credential, credential.getSmsCode());
                                            }
                                        }

                                        @Override
                                        public void onVerificationFailed(FirebaseException e) {
                                            dialog.dismiss();
                                            Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            super.onCodeSent(s, forceResendingToken);
                                            verificationId = s;
                                            dialog.dismiss();
                                            Snackbar.make(findViewById(R.id.cord), "otp send to your no." + mobile.getText().toString(), Snackbar.LENGTH_LONG).show();

                                        }
                                    })          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpText.getText().toString().trim();
                if (otp.length() < 5)
                    otpText.setError("must have 6 digit");

                else {
                    dialog.setMessage("verify");
                    dialog.show();
                    verify(null, otp);
                }

            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select image"), RequestCode.CAMERA_ACTION.getCodeId());
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DevicePermission().checkDevicePermission(
                        RequestCode.CAMERA_PERMISSION_CODE.name(),
                        RequestCode.CAMERA_PERMISSION_CODE.getCodeId());
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select image"), RequestCode.CAMERA_ACTION.getCodeId());
            }
        });

    }

    private void loadResource() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.setIcon(R.mipmap.icon1);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.FIREBASE_USERS_PATH);
    }

    private String getExt(Uri uri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void verify(PhoneAuthCredential credential, String code) {

        if(credential == null) {
            credential = PhoneAuthProvider.getCredential(verificationId, code);
        }
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        ref.child(user.getUid()).child(FirebaseConstants.MOBILE_NO).setValue(mobile.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            dialog.dismiss();
                                            startActivity(new Intent(getApplicationContext(), Details.class));
                                            finish();
                                            return;
                                        }
                                        dialog.dismiss();
                                        Snackbar.make(findViewById(R.id.cord), "we are facing issues ." +
                                                " please try after some time!!!", Snackbar.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });

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
                Picasso.with(this).load(uri).resize(520, 530).centerCrop().into(iv);

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


    private void call() {
        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.otp);
        ll.startAnimation(anim1);
        ll.setVisibility(View.INVISIBLE);
        ll1.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        ll1.startAnimation(anim);

    }

}
