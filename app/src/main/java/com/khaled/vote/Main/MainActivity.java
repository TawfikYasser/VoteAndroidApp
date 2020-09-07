package com.khaled.vote.Main;
/*
* @Author Khalid Said
* @Since 18/5/2020
* Copy Rights For Khalid Said
* */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khaled.vote.Other.AdminControl;
import com.khaled.vote.AppClasses.ApplicationData;
import com.khaled.vote.Auth.Login;
import com.khaled.vote.Other.Notifications;
import com.khaled.vote.R;
import com.khaled.vote.AppClasses.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    private ApplicationData mAppData;
    private Toolbar mToolbar;
    private TextView mUserName;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference, mDataBase;
    private MaterialCardView mMembers, mPresident, mVPresident, mReports;
    private CircleImageView mMainPhoto;
    private boolean voteState = false, PresStatus = false, ViceStatus = false;
    private String voteStatusDatabase = "",repState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("UsersData");
        mDataBase = FirebaseDatabase.getInstance().getReference("VoteData");
        mToolbar = findViewById(R.id.toolbar);
        mAppData = new ApplicationData(MainActivity.this);
        setSupportActionBar(mToolbar);
        forceRTLIfSupported();
        mToolbar.setTitleTextAppearance(MainActivity.this, R.style.TXTBoldTextAppearance);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("الصفحة الرئيسية");
        }

        mUserName = findViewById(R.id.home_username);
        mMembers = findViewById(R.id.members_card);
        mPresident = findViewById(R.id.pres_card);
        mVPresident = findViewById(R.id.vpres_card);
        mReports = findViewById(R.id.reports_card);
        mMainPhoto = findViewById(R.id.home_user_img);
        mUserName.setText(mAuth.getCurrentUser().getDisplayName());
        Glide.with(this)
                .load(mAuth.getCurrentUser().getPhotoUrl())
                .centerCrop()
                .into(mMainPhoto);

        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Ds : dataSnapshot.getChildren()) {

                    if (Ds.getKey().equals("voteStatus")) {
                        voteStatusDatabase = Ds.getValue().toString();
                    }
                    if(Ds.getKey().equals("Res")){
                        repState = Ds.getValue().toString();
                    }
                        /*Vote vote= Ds.getValue(Vote.class);
                        voteStatusDatabase = vote.getCoteS();*/
                    Log.d("EEE", "onDataChange: " + voteStatusDatabase);

                    if (voteStatusDatabase.equals("Enabled")) {
                        mAppData.saveVoteStatus(true);
                    } else {
                        mAppData.saveVoteStatus(false);
                    }

                    if(repState.equals("True")){
                        mReports.setEnabled(true);
                    }else{
                        mReports.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Ds : dataSnapshot.getChildren()) {
                    User userData = Ds.getValue(User.class);
                    String cu = mAuth.getCurrentUser().getUid();
                    if(userData.getUserId().equals(cu)){
                        if(userData.getUserType().equals("President")){
                            Log.d("TTT", "Type: "+userData.getUserType());
                            mPresident.setEnabled(false);
                            mVPresident.setEnabled(false);
                        }else if(userData.getUserType().equals("VPresident")){
                            Log.d("TTT", "Type: "+userData.getUserType());
                            mPresident.setEnabled(false);
                            mVPresident.setEnabled(false);                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        handleCards();

        mMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Members.class));
            }
        });
        mPresident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, President.class));
            }
        });
        mVPresident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VicePresident.class));
            }
        });
        mReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Reports.class));
            }
        });



    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported() {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuItem item = menu.findItem(R.id.control_menu);
        String AdminEmail = mAuth.getCurrentUser().getEmail();
        if (AdminEmail.equals("khalidsaid200@gmail.com")) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.control_menu:
                startActivity(new Intent(MainActivity.this, AdminControl.class));
                return true;
            case R.id.notification_menu:
                startActivity(new Intent(MainActivity.this, Notifications.class));
                return true;
            case R.id.logout_menu:
                logOut();
                return true;
            default:
                return false;
        }
    }

    // This function to display a dialog when user click on logout.
    private void logOut() {
        Dialog();
    }
    //END of

    // This function to display logout dialog to ask user for logging out.
    void Dialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("هل انت متأكد انك تريد الخروج؟");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "نعم",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(mAuth.getCurrentUser().getEmail().equals("khalidsaid200@gmail.com")){
                            mAppData.saveSignLogin(true);
                        }
                        dialog.cancel();
                        mAuth.signOut();

                        sendToLogin();
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "لا",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    //END of Dialog


    // This function have intent to go to Login Activity when user click on logout.
    void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    //END of sendToLogin


    @Override
    protected void onStart() {
        super.onStart();
        voteState = mAppData.getVoteState();
        PresStatus = mAppData.getPresState();
        ViceStatus = mAppData.getViceState();
        if (PresStatus) {
            mPresident.setEnabled(false);
            if (ViceStatus) {
                mVPresident.setEnabled(false);
            } else {
                mVPresident.setEnabled(true);
            }
        } else if (ViceStatus) {
            mVPresident.setEnabled(false);
            if (PresStatus) {
                mPresident.setEnabled(false);
            } else {
                mPresident.setEnabled(true);
            }
        } else if (voteState) {
            mPresident.setEnabled(true);
            mVPresident.setEnabled(true);
        } else {
            mPresident.setEnabled(false);
            mVPresident.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        voteState = mAppData.getVoteState();
        PresStatus = mAppData.getPresState();
        ViceStatus = mAppData.getViceState();
        if (PresStatus) {
            mPresident.setEnabled(false);
            if (ViceStatus) {
                mVPresident.setEnabled(false);
            } else {
                mVPresident.setEnabled(true);
            }
        } else if (ViceStatus) {
            mVPresident.setEnabled(false);
            if (PresStatus) {
                mPresident.setEnabled(false);
            } else {
                mPresident.setEnabled(true);
            }
        } else if (voteState) {
            mPresident.setEnabled(true);
            mVPresident.setEnabled(true);
        } else {
            mPresident.setEnabled(false);
            mVPresident.setEnabled(false);
        }
    }

    private void handleCards(){
        voteState = mAppData.getVoteState();
        PresStatus = mAppData.getPresState();
        ViceStatus = mAppData.getViceState();
        if (PresStatus) {
            mPresident.setEnabled(false);
            if (ViceStatus) {
                mVPresident.setEnabled(false);
            } else {
                mVPresident.setEnabled(true);
            }
        } else if (ViceStatus) {
            mVPresident.setEnabled(false);
            if (PresStatus) {
                mPresident.setEnabled(false);
            } else {
                mPresident.setEnabled(true);
            }
        } else if (voteState) {
            mPresident.setEnabled(true);
            mVPresident.setEnabled(true);
        } else {
            mPresident.setEnabled(false);
            mVPresident.setEnabled(false);
        }

    }

}
