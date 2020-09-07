package com.khaled.vote.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khaled.vote.AppClasses.ApplicationData;
import com.khaled.vote.Main.MainActivity;
import com.khaled.vote.R;

public class Login extends AppCompatActivity {

    private TextInputEditText  mEmail, mPassword;
    private Button mLoginBtn,mDontHave;
    private FirebaseAuth mAuth;
    private ProgressBar mLoginProgress;
    private ApplicationData mData;
    private DatabaseReference mDatabase;
    private String VSL = "True";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /// System Data START ************************************************************///
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("VoteData");
        /// System Data END ************************************************************///
        mData = new ApplicationData(Login.this);
        initViews();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Ds : dataSnapshot.getChildren()) {

                    if (Ds.getKey().equals("SL")) {
                        //voteStatusDatabase = Ds.getValue().toString();
                        VSL =  Ds.getValue().toString();;
                    }
                        /*Vote vote= Ds.getValue(Vote.class);
                        voteStatusDatabase = vote.getCoteS();*/
                    //Log.d("EEE", "onDataChange: " + voteStatusDatabase);

                    if(VSL.equals("True") ){
                        mLoginBtn.setEnabled(true);
                        mDontHave.setEnabled(true);
                    }else if(mData.getSaveLogin()){
                        mLoginBtn.setEnabled(true);
                        mDontHave.setEnabled(true);
                    }else{
                        mLoginBtn.setEnabled(false);
                        mDontHave.setEnabled(false);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String LoginEmail = mEmail.getText().toString();
                String LoginPass = mPassword.getText().toString();
                if (!TextUtils.isEmpty(LoginEmail) && !TextUtils.isEmpty(LoginPass)) {
                    mLoginProgress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(LoginEmail,LoginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendToMainActivity();
                            }else{
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(Login.this, "Error: "+errorMessage, Toast.LENGTH_SHORT).show();
                            }
                            mLoginProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                }else{
                    Snackbar.make(findViewById(android.R.id.content),"يجب إدخال جميع البيانات أولاً!",Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
                }

            }
        });

        mDontHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Login.this,Register.class);
                startActivity(mainIntent);
                finish();
            }
        });

    }
    // This function to initialize all definitions.
    private void initViews() {

        mEmail=findViewById(R.id.email_et_login);
        mPassword=findViewById(R.id.password_et_login);
        mLoginBtn=findViewById(R.id.login_btn);
        mDontHave =findViewById(R.id.dont_have);
        mLoginProgress =findViewById(R.id.progressBar);
    }
    //END of initViews


    // This function to detect if user logged in or not when application starts.
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            //user already login
            sendToMainActivity();
        }else{
            //user not login
        }
    }
    //END of onStart


    // This function to go to main activity
    private void sendToMainActivity() {
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
    //END of sendToMainActivity

}
