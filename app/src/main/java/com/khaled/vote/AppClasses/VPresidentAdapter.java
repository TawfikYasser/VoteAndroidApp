package com.khaled.vote.AppClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khaled.vote.Main.MainActivity;
import com.khaled.vote.R;

public class VPresidentAdapter extends FirebaseRecyclerAdapter<User, VPresidentAdapter.VPViewHolder> {

    private Context mContext ;
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
    public VPresidentAdapter(@NonNull FirebaseRecyclerOptions<User> options , Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull final VPViewHolder holder, final int position, @NonNull User model) {
        mData = new ApplicationData(mContext);
        holder.mVPName.setText(model.getUserName());
        holder.mVPType.setText("مُترشح لمنصب النائب");
        String uri = model.getUserImage();
        Glide.with(holder.itemView.getContext()).load(uri).into(holder.mVPImage);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("VotedNames");
        holder.mVvoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setMessage("هل أنت متأكد من التصويت لهذا النائب؟");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "نعم",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                String idv = getRef(position).getKey();
                                reference = FirebaseDatabase.getInstance().getReference("UsersVP");
                                reference.child(idv).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){

                                            String n = String.valueOf( ((numberP++)+1) );
                                            String VName= dataSnapshot.child("userName").getValue().toString();
                                            String VId = dataSnapshot.child("userId").getValue().toString();
                                            String VType = dataSnapshot.child("userType").getValue().toString();
                                            String VImage = dataSnapshot.child("userImage").getValue().toString();

                                            DatabaseReference databaseReference = mDatabaseReference.child(VId);
                                            RepClass aClass = new RepClass(VImage,VName,VType,n);
                                            databaseReference.setValue(aClass);
                                            mData.saveViceStatus(true);

                                            //Toast.makeText(mContext, "الآن قم بغلق وفتح التطبيق مرة أخرى!", Toast.LENGTH_SHORT).show();
                                            Intent goBackV = new Intent(mContext, MainActivity.class);
                                            goBackV.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            goBackV.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            mContext.startActivity(goBackV);
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

    @NonNull
    @Override
    public VPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vpresident_item, parent, false);
        return new VPresidentAdapter.VPViewHolder(view);
    }

    public class VPViewHolder extends RecyclerView.ViewHolder {
        TextView mVPName, mVPType;
        ImageView mVPImage;
        Button mVvoteBtn;
        public VPViewHolder(@NonNull View itemView) {
            super(itemView);
            mVPName = itemView.findViewById(R.id.vpres_card_name);
            mVPType = itemView.findViewById(R.id.vpres_card_type);
            mVPImage = itemView.findViewById(R.id.vpres_image_item);
            mVvoteBtn = itemView.findViewById(R.id.vote_btn_vp);
        }
    }
}
