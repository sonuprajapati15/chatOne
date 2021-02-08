package com.example.chatone.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.chatone.R;
import com.example.chatone.constants.FirebaseConstants;
import com.example.chatone.enums.RequestCode;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.HashMap;
import java.util.regex.Pattern;

import retrofit2.Response;

public class LoginAndSignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextInputEditText emailBox, passBox;
    private TextView reset, welcome, register, down;
    private Button login;
    private String email_pattern = "[A-Za-z0-9._-]+@[a-z]+\\\\.+[a-z]+";
    private ProgressDialog dialog;
    private LinearLayout ll2;
    private FirebaseAuth mAuth;
    private CardView google, tweet, mobilebtn, emailbtn;
    private DatabaseReference dbReference;
    TwitterLoginButton twitter;
    private static int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        Twitter.initialize(getApplicationContext());
        down = (TextView) findViewById(R.id.down);
        google = (CardView) findViewById(R.id.google);
        tweet = (CardView) findViewById(R.id.tweet);
        reset = (TextView) findViewById(R.id.reset);
        welcome = (TextView) findViewById(R.id.welcome);
        login = (Button) findViewById(R.id.login);
        mobilebtn = (CardView) findViewById(R.id.mobilesign);
        emailbtn = (CardView) findViewById(R.id.emailsign);
        register = (TextView) findViewById(R.id.register);
        emailBox = (TextInputEditText) findViewById(R.id.email);
        passBox = (TextInputEditText) findViewById(R.id.pass);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        twitter = (TwitterLoginButton) findViewById(R.id.tweetbtn);


        loadStartupData();

        mobilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Mobile.class));
            }
        });

        emailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Email.class));

            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getGoogleClient().getSignInIntent(),
                        RequestCode.GOOGLE_SIGN_IN.getCodeId());
            }
        });

        tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        c = 1;
                        twitter.performClick();
                    }
                });
            }
        });

        twitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                handleTwitterSession(result.data, result.response);
            }

            @Override
            public void failure(TwitterException exception) {
                Snackbar.make(findViewById(R.id.cord), exception.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll2.setVisibility(View.VISIBLE);
                ll2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translateup));
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translatedown));
                ll2.setVisibility(View.GONE);
            }
        });

////////////////////////////////email login/////////////////////////////////////////////////////////////////
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Pattern pattern = Pattern.compile(email_pattern);
                String emailId = emailBox.getText().toString().trim();
                String password = passBox.getText().toString().trim();
                if (emailId.isEmpty())
                    Snackbar.make(findViewById(R.id.cord), "username empty", Snackbar.LENGTH_LONG).show();
                else if (pattern.matcher(emailId).matches())
                    Snackbar.make(findViewById(R.id.cord), "email id is not valid.", Snackbar.LENGTH_LONG).show();
                else if (password.isEmpty() || password.length() < 8)
                    Snackbar.make(findViewById(R.id.cord), "password must be of 8 character", Snackbar.LENGTH_LONG).show();
                else {
                    dialog.setMessage("validating !!!!");
                    dialog.show();
                    loginWithEmailAndPassword(emailId, password);
                }
            }
        });
    }

    private GoogleSignInClient getGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        return GoogleSignIn.getClient(this, gso);
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        dialog.setMessage("Creating Account");
        dialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                HashMap<String, Object> hm = new HashMap<String, Object>();
                                hm.put(FirebaseConstants.NAME, acct.getDisplayName());
                                hm.put(FirebaseConstants.THUMB_IAMGE, acct.getPhotoUrl().toString());
                                hm.put(FirebaseConstants.EMAIL, acct.getEmail());
                                hm.put(FirebaseConstants.ID, mAuth.getCurrentUser().getUid());
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                dbReference.child(currentUser.getUid()).updateChildren(hm)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    dialog.dismiss();
                                                    SharedPreferences sp = getSharedPreferences("chatOne", Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sp.edit();
                                                    editor.putBoolean("LOGIN", true);
                                                    editor.commit();
                                                    Intent i = new Intent(LoginAndSignUpActivity.this, Details.class);
                                                    i.putExtra("ID", "1");
                                                    dialog.dismiss();
                                                    startActivity(i);
                                                    finish();
                                                } else {
                                                    dialog.dismiss();
                                                    Snackbar.make(findViewById(R.id.cord), "details not uploading", Snackbar.LENGTH_LONG).show();

                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });

    }

    private void handleTwitterSession(final TwitterSession session, Response response) {
        dialog.setMessage("Checking validation");
        dialog.show();
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        HashMap<String, Object> hm = new HashMap<String, Object>();
                        hm.put(FirebaseConstants.NAME, session.getUserName());
                        hm.put(FirebaseConstants.TWITTER_ID, session.getUserId());
                        hm.put(FirebaseConstants.ID, mAuth.getCurrentUser().getUid());

                        dbReference.child(mAuth.getCurrentUser().getUid()).updateChildren(hm)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
                                        Intent i = new Intent(getApplicationContext(), Details.class);
                                        i.putExtra("ID", "1");
                                        startActivity(i);
                                        finish();
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });

    }

    private void loginWithEmailAndPassword(final String emailId, String password) {
        mAuth.signInWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkIfEmailVerified(mAuth.getCurrentUser());
                        } else {
                            dialog.dismiss();
                            Snackbar.make(findViewById(R.id.cord), "we are facing issues while login with email." +
                                    " try after some time !!! ", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void checkIfEmailVerified(FirebaseUser currentUser) {
        if (currentUser.isEmailVerified()) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put(FirebaseConstants.ID, mAuth.getCurrentUser().getUid());
            hm.put(FirebaseConstants.EMAIL, emailBox.getText().toString());

            dbReference.child(currentUser.getUid()).setValue(hm)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Intent i = new Intent(LoginAndSignUpActivity.this, Details.class);
                                i.putExtra("ID", "1");
                                startActivity(i);
                                finish();
                                return;
                            }
                            dialog.dismiss();
                            Snackbar.make(findViewById(R.id.cord), "We are facing issue while verification of Email id. ", Snackbar.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Snackbar.make(findViewById(R.id.cord), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            dialog.dismiss();
            Snackbar.make(findViewById(R.id.cord), "Please check your mail and verify your identity .", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RequestCode.GOOGLE_SIGN_IN.getCodeId()) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Animation anim3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translateleft);
        welcome.startAnimation(anim3);
        passBox.startAnimation(anim3);
        emailBox.startAnimation(anim3);
        login.startAnimation(anim3);
        register.startAnimation(anim3);
    }

    private void loadStartupData() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);
        dialog.setIcon(R.mipmap.icon1);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.FIREBASE_USERS_PATH);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        dialog.dismiss();
        Snackbar.make(findViewById(R.id.cord), connectionResult.getErrorMessage(), Snackbar.LENGTH_LONG).show();

    }
}


