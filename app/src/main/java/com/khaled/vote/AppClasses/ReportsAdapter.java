package com.khaled.vote.AppClasses;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.khaled.vote.R;

public class ReportsAdapter extends FirebaseRecyclerAdapter<RepClass,ReportsAdapter.PViewHolder> {
    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ReportsAdapter(@NonNull FirebaseRecyclerOptions<RepClass> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PViewHolder holder, int position, @NonNull RepClass model) {
        holder.mPName.setText(model.getName());
        String accType = model.getType();
        Log.d("TAG", "onBindViewHolder: "+ accType);
        if(accType.equals("President")) {
            holder.mPType.setText("رئيس");
        }else{
            holder.mPType.setText("نائب");
        }
        String uri = model.getImage();
        Glide.with(holder.itemView.getContext()).load(uri).into(holder.mPImage);
        holder.Number.setText(model.getNumber());
    }

    @NonNull
    @Override
    public PViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        return new ReportsAdapter.PViewHolder(view);
    }

    public class PViewHolder extends RecyclerView.ViewHolder {
        TextView mPName, mPType,Number;
        ImageView mPImage;

        public PViewHolder(@NonNull View itemView) {
            super(itemView);
            mPName = itemView.findViewById(R.id.report_name);
            mPType = itemView.findViewById(R.id.report_type);
            mPImage = itemView.findViewById(R.id.report_image);
            Number = itemView.findViewById(R.id.number);
        }
    }
}
