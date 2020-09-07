package com.khaled.vote.Main;
/*
 * @Author Khalid Said
 * @Since 18/5/2020
 * Copy Rights For Khalid Said
 * */
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.khaled.vote.R;
import com.khaled.vote.AppClasses.User;
import com.khaled.vote.AppClasses.UserAdapter;

import java.util.ArrayList;

public class Members extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private Toolbar mToolbarM;
    private RecyclerView mMembers;
    private ArrayList<User> mArrList;
    private FirebaseAuth mAuth;
    private UserAdapter userAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        mToolbarM =findViewById(R.id.toolbar_members);
        setSupportActionBar(mToolbarM);
        forceRTLIfSupported();
        mToolbarM.setTitleTextAppearance(Members.this,R.style.TXTBoldTextAppearance);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("الأعضاء");
        }

        /// System Data START ************************************************************///
        mAuth=FirebaseAuth.getInstance();
        mArrList =new ArrayList<>();
        /// System Data START ************************************************************///

        initViews();
        loadAllUsers();
    }
    // This function to initialize all definitions.
    private void initViews() {
        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        mMembers = findViewById(R.id.members_recycler);
    }
    //END of initViews

    // This function to load all users for the database to recycler view.
    private void loadAllUsers(){
        mMembers.setLayoutManager(new LinearLayoutManager(this));
        final FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("UsersData"), User.class)
                        .build();
        userAdapter = new UserAdapter(options);
        mMembers.setAdapter(userAdapter);

    }
    //END of loadAllUsers

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

    // This function to handle refresh ball.
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshList();
    }
    //END of onRefresh

    // This function to refresh the adapter.
    void refreshList(){
        userAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
    //END of refreshList

    // This function to start get data.
    @Override
    public void onStart() {
        super.onStart();
        userAdapter.startListening();
    }
    //END of onStart

    //This function to stop get data.
    @Override
    public void onStop() {
        super.onStop();
        userAdapter.stopListening();
    }
    //ENd of onStop

}
