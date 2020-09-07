package com.khaled.vote.Other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khaled.vote.AppClasses.ApplicationData;
import com.khaled.vote.R;

public class AdminControl extends AppCompatActivity {

    private Toolbar mToolbarAdmin;
    private RelativeLayout mVoteLayout;
    private SwitchMaterial mSwitch;
    private TextInputEditText mClubName, mAdContent;
    private Button mSaveAd,SignLogin,SignLogin2,mShow,mHide;
    private ApplicationData mAppData;
    private boolean switchChecked;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference,mDataN,mMembersData;
    private boolean vS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control);
        mToolbarAdmin = findViewById(R.id.toolbar_control);
        setSupportActionBar(mToolbarAdmin);
        forceRTLIfSupported();
        mToolbarAdmin.setTitleTextAppearance(AdminControl.this, R.style.TXTBoldTextAppearance);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("لوحة التحكم");
        }


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("VoteData");
        mMembersData = FirebaseDatabase.getInstance().getReference().child("UsersData");
        mDataN = FirebaseDatabase.getInstance().getReference().child("Notification");
        mAuth =FirebaseAuth.getInstance();
        mAppData = new ApplicationData(AdminControl.this);

        SignLogin = findViewById(R.id.sign_login_state);
        SignLogin2 =findViewById(R.id.sign_login_state2);
        mShow = findViewById(R.id.show_results);
        mHide =findViewById(R.id.hide_results);
        vS = mAppData.getVoteState();
        mClubName = findViewById(R.id.club_name);
        mAdContent = findViewById(R.id.ad_text);
        mSwitch = findViewById(R.id.switch_vote);
        mVoteLayout = findViewById(R.id.start_vote_layout);
        mSaveAd = findViewById(R.id.send_add_btn);

        mSaveAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send the notification to users
                if(!TextUtils.isEmpty(mAdContent.getText().toString())){
                    mDataN.child("NContent").setValue(mAdContent.getText().toString());
                    Toast.makeText(AdminControl.this, "تم إرسال الإشعار للمستخدمين!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AdminControl.this, "يجب كتابة نص أولاً!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        if (vS) {
            mSwitch.setChecked(true);
            switchChecked = true;
            mAppData.saveVoteStatus(true);
            switchChecked = true;
        } else {
            mSwitch.setChecked(false);
            switchChecked = false;
            mAppData.saveVoteStatus(false);
            switchChecked = false;
        }


        mVoteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String club = mClubName.getText().toString();

                if (switchChecked) {
                    mSwitch.setChecked(false);
                    Toast.makeText(AdminControl.this, "تم إيقاف الإنتخابات.", Toast.LENGTH_SHORT).show();
                    mDatabaseReference.child("voteStatus").setValue("Disabled");
                    mDatabaseReference.child("clubName").setValue("");
                    mAppData.saveVoteStatus(false);
                    switchChecked = false;

                    Toast.makeText(AdminControl.this, "قم بغلق وفتح التطبيق مرة أخرى!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!TextUtils.isEmpty(club)) {
                        Toast.makeText(AdminControl.this, "تم بدء الإنتخابات لـ " + club, Toast.LENGTH_SHORT).show();
                        mSwitch.setChecked(true);
                        mAppData.saveVoteStatus(true);
                        //upload all data to firebase
                        mDatabaseReference.child("voteStatus").setValue("Enabled");
                        mDatabaseReference.child("clubName").setValue(club);
                        switchChecked = true;
                        mAppData.savePresStatus(false);
                        mAppData.saveVoteStatus(false);
                        Toast.makeText(AdminControl.this, "قم بغلق وفتح التطبيق مرة أخرى!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminControl.this, "قم بإدخال اسم النادي!", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        SignLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppData.saveSignLogin(false);
                Toast.makeText(AdminControl.this, "تم تفعيل إمكانية التسجيل للمستخدمين!", Toast.LENGTH_SHORT).show();
                mDatabaseReference.child("SL").setValue("True");
            }
        });

        SignLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.child("SL").setValue("False");
                mAppData.saveSignLogin(true);
                Toast.makeText(AdminControl.this, "تم إيقاف إمكانية التسجيل للمستخدمين!", Toast.LENGTH_SHORT).show();

            }
        });

        mShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // mAppData.saveResults(true);
                mDatabaseReference.child("Res").setValue("True");

            }
        });
        mHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mAppData.saveResults(false);
                mDatabaseReference.child("Res").setValue("False");

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported() {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
