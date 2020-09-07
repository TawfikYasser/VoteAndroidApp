package com.khaled.vote.Other;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khaled.vote.Auth.Login;
import com.khaled.vote.Main.MainActivity;
import com.khaled.vote.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private CircleImageView mSplashImg;
    private TextView mTxt1, mClubTxt;
    private Animation mTopAnim, mBottomAnim;
    private boolean connected = false;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference().child("VoteData").child("clubName");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String clubN = dataSnapshot.getValue(String.class);
                if(clubN.equals("")){
                    mClubTxt.setText("لا توجد انتخابات حالياً");

                }else{
                    mClubTxt.setText(clubN);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSplashImg = findViewById(R.id.vote_img);
        mTxt1 = findViewById(R.id.splash_txt1);
        mClubTxt = findViewById(R.id.club_name_txt);
        mTopAnim = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.splash_top_anim);
        mBottomAnim = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.splash_bottom_anim);
        mSplashImg.setAnimation(mTopAnim);
        mTxt1.setAnimation(mBottomAnim);
        mClubTxt.setAnimation(mBottomAnim);

        if (isConnected()) {
            if (user != null) {
                //user signed in
                splashFun();
            } else {
                // user logged out
                Intent loginIntent = new Intent(SplashScreen.this, Login.class);
                //Log.d("TAG", " not signed");
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
            }
        } else {
            Intent gotonowifi = new Intent(SplashScreen.this, NoConnection.class);
            gotonowifi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            gotonowifi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(gotonowifi);
            finish();
        }

    }

    boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        return connected;
    }

    void splashFun() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toHome = new Intent(SplashScreen.this, MainActivity.class);
                //Log.d("TAG", "signed");
                toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toHome);

                finish();

            }
        }, 3000);
    }
}
