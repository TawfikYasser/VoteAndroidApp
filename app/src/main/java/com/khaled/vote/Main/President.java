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
import com.google.firebase.database.FirebaseDatabase;
import com.khaled.vote.AppClasses.PresidentAdapter;
import com.khaled.vote.R;
import com.khaled.vote.AppClasses.User;

public class President extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private Toolbar mToolbarP;
    private RecyclerView mPRecycler;
    private PresidentAdapter presidentAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_president);
        mToolbarP =findViewById(R.id.toolbar_president);
        setSupportActionBar(mToolbarP);
        forceRTLIfSupported();
        mToolbarP.setTitleTextAppearance(President.this,R.style.TXTBoldTextAppearance);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("المُرشحين لمنصب الرئيس");
        }
        mPRecycler =findViewById(R.id.president_recycler);
        swipeRefreshLayout = findViewById(R.id.refreshp);
        swipeRefreshLayout.setOnRefreshListener(this);


        mPRecycler.setLayoutManager(new LinearLayoutManager(this));
        final FirebaseRecyclerOptions<User> Pusers =
                new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("UsersP"),User.class)
                .build();
        presidentAdapter = new PresidentAdapter(Pusers,this);
        mPRecycler.setAdapter(presidentAdapter);

    }
    //This function to stop get data.
    @Override
    public void onStop() {
        super.onStop();
        presidentAdapter.stopListening();
    }
    //ENd of onStop
    // This function to start get data.
    @Override
    public void onStart() {
        super.onStart();
        presidentAdapter.startListening();
    }
    //END of onStart
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
        presidentAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
    //END of refreshList

}
