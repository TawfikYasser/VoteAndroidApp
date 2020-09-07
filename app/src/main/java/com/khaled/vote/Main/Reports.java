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
import com.khaled.vote.R;
import com.khaled.vote.AppClasses.RepClass;
import com.khaled.vote.AppClasses.ReportsAdapter;

public class Reports extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private Toolbar mToolbarR;
    private RecyclerView mReportsRecycler;
    private SwipeRefreshLayout mSwipeRef;
    private ReportsAdapter mReportsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        mToolbarR =findViewById(R.id.toolbar_reports);
        setSupportActionBar(mToolbarR);
        forceRTLIfSupported();
        mToolbarR.setTitleTextAppearance(Reports.this,R.style.TXTBoldTextAppearance);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("نتائج التصويت");
        }
        mReportsRecycler =findViewById(R.id.reports_recycler);
        mSwipeRef = findViewById(R.id.reports_ref);
        mSwipeRef.setOnRefreshListener(this);


        mReportsRecycler.setLayoutManager(new LinearLayoutManager(this));
        final FirebaseRecyclerOptions<RepClass> Voted =
                new FirebaseRecyclerOptions.Builder<RepClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("VotedNames"),RepClass.class)
                        .build();
        mReportsAdapter = new ReportsAdapter(Voted,this);
        mReportsRecycler.setAdapter(mReportsAdapter);

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

    //This function to stop get data.
    @Override
    public void onStop() {
        super.onStop();
        mReportsAdapter.stopListening();
    }
    //ENd of onStop
    // This function to start get data.
    @Override
    public void onStart() {
        super.onStart();
        mReportsAdapter.startListening();
    }
    //END of onStart

    // This function to handle refresh ball.
    @Override
    public void onRefresh() {
        mSwipeRef.setRefreshing(true);
        refreshList();
    }
    //END of onRefresh

    // This function to refresh the adapter.
    void refreshList(){
        mReportsAdapter.notifyDataSetChanged();
        mSwipeRef.setRefreshing(false);
    }
    //END of refreshList

}
