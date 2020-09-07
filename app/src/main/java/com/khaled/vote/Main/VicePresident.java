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
import com.khaled.vote.AppClasses.User;
import com.khaled.vote.AppClasses.VPresidentAdapter;

public class VicePresident extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar mToolbarVP;
    private RecyclerView mPRecycler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private VPresidentAdapter vPresidentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vice_president);
        mToolbarVP =findViewById(R.id.toolbar_vpres);
        setSupportActionBar(mToolbarVP);
        forceRTLIfSupported();
        mToolbarVP.setTitleTextAppearance(VicePresident.this,R.style.TXTBoldTextAppearance);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("المُترشحين لمنصب النائب");
        }
        mPRecycler =findViewById(R.id.vpresident_recycler);
        swipeRefreshLayout = findViewById(R.id.v_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);


        mPRecycler.setLayoutManager(new LinearLayoutManager(this));
        final FirebaseRecyclerOptions<User> Pusers =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("UsersVP"),User.class)
                        .build();
        vPresidentAdapter = new VPresidentAdapter(Pusers,this);
        mPRecycler.setAdapter(vPresidentAdapter);

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
        vPresidentAdapter.stopListening();
    }
    //ENd of onStop
    // This function to start get data.
    @Override
    public void onStart() {
        super.onStart();
        vPresidentAdapter.startListening();
    }
    //END of onStart

    // This function to handle refresh ball.
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshList();
    }
    //END of onRefresh

    // This function to refresh the adapter.
    void refreshList(){
        vPresidentAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
    //END of refreshList

}
