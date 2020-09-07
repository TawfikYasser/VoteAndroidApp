package com.khaled.vote.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.khaled.vote.R;

public class NoConnection extends AppCompatActivity {
    private Button mRetry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);
        mRetry=findViewById(R.id.retry_btn);
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent gotosplash = new Intent(NoConnection.this,SplashScreen.class);
                gotosplash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                gotosplash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(gotosplash);
            }
        });
    }
}
