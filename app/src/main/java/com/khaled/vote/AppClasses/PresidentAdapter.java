package com.khaled.vote.AppClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khaled.vote.Main.MainActivity;
import com.khaled.vote.R;

public class PresidentAdapter extends FirebaseRecyclerAdapter<User, PresidentAdapter.PViewHolder> {

    private Context context;
    private DatabaseReference reference;
    private DatabaseReference mDatabaseReference;
    private int numberP = 0;
    private ApplicationData mData;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PresidentAdapter(@NonNull FirebaseRecyclerOptions<User> options, Context mContext) {
        super(options);
        this.context = mContext;
    }

    @NonNull
    @Override
    public PViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.president_item, parent, false);
        return new PViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull final PViewHolder holder, final int position, @NonNull User model) {
        mData = new ApplicationData(context);
        holder.mPName.setText(model.getUserName());
        holder.mPType.setText("مُترشح لمنصب الرئيس");
        String uri = model.getUserImage();
        Glide.with(holder.itemView.getContext()).load(uri).into(holder.mPImage);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("VotedNames");
        holder.mVoteP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("هل أنت متأكد من التصويت لهذا الرئيس؟");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "نعم",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String idp = getRef(position).getKey();
                                reference = FirebaseDatabase.getInstance().getReference("UsersP");
                                reference.child(idp).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {


                                            String n = String.valueOf( ((numberP++)+1) );
                                            Log.d("TTT", "onClick: " + dataSnapshot.child("userName").getValue());
                                            String PName = dataSnapshot.child("userName").getValue().toString();
                                            String PId = dataSnapshot.child("userId").getValue().toString();
                                            String PType = dataSnapshot.child("userType").getValue().toString();
                                            String PImage = dataSnapshot.child("userImage").getValue().toString();

                                            DatabaseReference databaseReference = mDatabaseReference.child(PId);
                                            RepClass aClass = new RepClass(PImage,PName,PType,n);
                                            databaseReference.setValue(aClass);
                                            mData.savePresStatus(true);
                                            //Toast.makeText(context, "الآن قم بغلق وفتح التطبيق مرة أخرى!", Toast.LENGTH_SHORT).show();
                                            Intent goBackP = new Intent(context, MainActivity.class);
                                            goBackP.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            goBackP.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            context.startActivity(goBackP);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });

                builder1.setNegativeButton(
                        "لا",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();



            }
        });
    }


    public class PViewHolder extends RecyclerView.ViewHolder {
        TextView mPName, mPType;
        ImageView mPImage;
        Button mVoteP;

        public PViewHolder(@NonNull View itemView) {
            super(itemView);
            mPName = itemView.findViewById(R.id.pres_card_name);
            mPType = itemView.findViewById(R.id.pres_card_type);
            mPImage = itemView.findViewById(R.id.pres_image_item);
            mVoteP = itemView.findViewById(R.id.pvote_btn);

        }
    }
    void Dialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("هل أنت متأكد من التصويت لهذا الرئيس؟");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "نعم",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder1.setNegativeButton(
                "لا",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
