package com.khaled.vote.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khaled.vote.AppClasses.ApplicationData;
import com.khaled.vote.AppClasses.User;
import com.khaled.vote.Main.MainActivity;
import com.khaled.vote.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Register extends AppCompatActivity {

    private MaterialButtonToggleGroup mToggleType;
    private TextInputEditText mUsername, mEmail, mPassword, mAge, mIdNumber;
    private Button mCreateAccount,mUserImg,mAlready;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReferencep;
    private DatabaseReference mDatabaseReferencevp;
    private DatabaseReference mDatab;
    private StorageReference mStorageReference;
    private ProgressDialog progressDialog;
    private Uri mainImageUri = null;
    private String Type;
    private ApplicationData mData;
    private String VSL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        setContentView(R.layout.activity_register);

        /// System Data START ************************************************************///
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("UsersData");
        mDatabaseReferencep = FirebaseDatabase.getInstance().getReference().child("UsersP");
        mDatabaseReferencevp = FirebaseDatabase.getInstance().getReference().child("UsersVP");
        mDatab = FirebaseDatabase.getInstance().getReference().child("voteData");
        mStorageReference= FirebaseStorage.getInstance().getReference();
        /// System Data START ************************************************************///
        mData = new ApplicationData(Register.this);
        initViews();


        mDatab.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Ds : dataSnapshot.getChildren()) {

                    if (Ds.getKey().equals("SL")) {
                        VSL = Ds.getValue().toString();
                    }

                    if(VSL.equals("True") || mData.getSaveLogin()){
                        mCreateAccount.setEnabled(true);
                        mAlready.setEnabled(true);
                    }else if(mData.getSaveLogin()){
                        mCreateAccount.setEnabled(true);
                        mAlready.setEnabled(true);
                    }else{
                        mCreateAccount.setEnabled(false);
                        mAlready.setEnabled(false);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mUsername.getText().toString();
                String email = mEmail.getText().toString();

                String password = mPassword.getText().toString();
                String age = mAge.getText().toString();
                String id = mIdNumber.getText().toString();

                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) &&
                   !TextUtils.isEmpty(age) && !TextUtils.isEmpty(id)){
                    progressDialog.setMessage("مرحباً "+username+", من فضلك انتظر ثواني، تتم عملية إنشاء الحساب...");
                    progressDialog.show();
                    createNewAccount(email,password,username);
                }else{
                    Snackbar.make(findViewById(android.R.id.content),"يجب ملء جميع البيانات المطلوبة!",Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.red))
                            .setTextColor(getResources().getColor(R.color.white))
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
                }

            }
        });


        mUserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    //check the permission
                    if(ContextCompat.checkSelfPermission(Register.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        //Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(Register.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else{
                        //Toast.makeText(this, "Permission ok!", Toast.LENGTH_SHORT).show();
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(Register.this);
                    }
                }

            }
        });
        mAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Register.this,Login.class);
                startActivity(mainIntent);
                finish();
            }
        });
        mToggleType.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(isChecked){
                    switch (checkedId){
                        case R.id.button1:
                            Type = "NMember";
                            //Toast.makeText(Register.this, "عضو", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.button2:
                            //Toast.makeText(Register.this, "نائب", Toast.LENGTH_SHORT).show();
                            Type = "VPresident";
                            break;

                        case R.id.button3:
                            //Toast.makeText(Register.this, "رئيس", Toast.LENGTH_SHORT).show();
                            Type = "President";
                            break;
                    }
                }else{
                    if(-1 == group.getCheckedButtonId()){
                        group.check(checkedId);
                    }
                }

            }
        });

    }
    private void initViews() {
        mToggleType = findViewById(R.id.toggle_btn);
        mUsername = findViewById(R.id.username_et_reg);
        mEmail = findViewById(R.id.email_et_reg);
        mPassword = findViewById(R.id.password_et_reg);
        mAge = findViewById(R.id.age_reg);
        mIdNumber = findViewById(R.id.id_num_reg);
        mCreateAccount = findViewById(R.id.create_btn);
        mUserImg =findViewById(R.id.choose_user_img);
        mAlready=findViewById(R.id.already_have);
        progressDialog = new ProgressDialog(this);

    }

    // This function to create new account, (calling updateUserInfo to create the account).
    private void createNewAccount(String email,String password,final String username) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    //after creation we need to update profile with username and img
                    updateUserInfo(username,user);
                }else{
                    String errorMsg = task.getException().getMessage();
                    Toast.makeText(Register.this, "Error: "+errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //END of createNewAccount


    // This function to upload the image and user data to firebase and go to main activity.
    private void updateUserInfo(final String username,final FirebaseUser user) {

        final String cUserEmail= user.getEmail();
        final String cUserId = user.getUid();
        final StorageReference filepath = mStorageReference.child("UserImages").child(mainImageUri.getLastPathSegment());
        filepath.putFile(mainImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest userProfileChangeRequest =new UserProfileChangeRequest.Builder()
                                .setDisplayName(username).setPhotoUri(uri).build();
                        if(Type.equals("President")){
                            DatabaseReference databaseReference =mDatabaseReferencep.child(cUserId);
                            User user1 = new User(username,cUserId,cUserEmail,uri.toString(),mAge.getText().toString(),mIdNumber.getText().toString(),Type);
                            databaseReference.setValue(user1);

                        }else if(Type.equals("VPresident")){
                            DatabaseReference databaseReference =mDatabaseReferencevp.child(cUserId);
                            User user1 = new User(username,cUserId,cUserEmail,uri.toString(),mAge.getText().toString(),mIdNumber.getText().toString(),Type);
                            databaseReference.setValue(user1);
                        }else{

                        }
                        DatabaseReference databaseReference =mDatabaseReference.child(cUserId);
                        User user1 = new User(username,cUserId,cUserEmail,uri.toString(),mAge.getText().toString(),mIdNumber.getText().toString(),Type);
                        databaseReference.setValue(user1);


                        mData.savePresStatus(false);
                        mData.saveViceStatus(false);


                        user.updateProfile(userProfileChangeRequest).addOnCompleteListener( new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //user info updated
                                    sendToMain();
                                    progressDialog.dismiss();
                                }else{
                                    //something wrong
                                    Snackbar.make(findViewById(android.R.id.content),"هناك خطأ ما، حاول مجدداً!",Snackbar.LENGTH_LONG)
                                            .setBackgroundTint(getResources().getColor(R.color.red))
                                            .setTextColor(getResources().getColor(R.color.white))
                                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).show();
                                }
                            }
                        });
                    }
                });
            }

        });
    }
    //END of updateUserInfo



    // This function to choose and crop the image.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //image chosed ok and croped then get it as uri
                mainImageUri = result.getUri();
                //mProfileSetupImg.setImageURI(mainImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    //END of onActivityResult


    // This function to detect if user logged in or not when application starts.
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            //already logged in
            sendToMain();
        }else{
            //not log in
        }

    }
    //END of onStart

    // This function to go to main activity
    void sendToMain(){
        Intent mainIntent = new Intent(Register.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
    //END of sendToMain
}


