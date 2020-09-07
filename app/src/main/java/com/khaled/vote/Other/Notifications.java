package com.khaled.vote.Other;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khaled.vote.R;

public class Notifications extends AppCompatActivity {

    private Toolbar mToolbarN;
    private TextView mTxtNoti;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        mToolbarN =findViewById(R.id.toolbar_noti);
        setSupportActionBar(mToolbarN);
        forceRTLIfSupported();
        mToolbarN.setTitleTextAppearance(Notifications.this,R.style.TXTBoldTextAppearance);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("الإشعارات");
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Notification").child("NContent");
        mTxtNoti =findViewById(R.id.notification_txt);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    String noti = userSnapshot.child("NContent").getValue(String.class);
                    if(noti !=null){
                        mTxtNoti.setText(noti);
                    }

                    //Log.d("TAG", "onCreate: "+email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
