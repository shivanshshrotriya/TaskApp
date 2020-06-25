package com.example.assignment;

import android.content.Context;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment.model.TaskModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ProductViewHolder> {


    private Context mCtx;
    private List<TaskModel> addressCarModelList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Tasks");

    FirebaseStorage storage;
    StorageReference storageReference;

    public Adapter(Context mCtx, List<TaskModel> addressCarModelList) {
        this.mCtx = mCtx;
        this.addressCarModelList = addressCarModelList;

    }

    @NonNull
    @Override
    public Adapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.task_view,viewGroup,false);
        Adapter.ProductViewHolder holder = new Adapter.ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Adapter.ProductViewHolder holder, final int i) {
        final TaskModel od = addressCarModelList.get(i);

        holder.title.setText(od.getTitle());
        holder.description.setText(od.getDescription());

        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(od.getPath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
               // Toast.makeText(mCtx, ""+uri.toString(), Toast.LENGTH_SHORT).show();
                Glide.with(mCtx).load(uri).into(holder.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Toast.makeText(mCtx, ""+od.getPath(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                if(postSnapshot.getKey().equals(od.getKey()))
                                {
                                    myRef.child(postSnapshot.getKey()).child("status").setValue(1);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
            }
        });

        if(od.getStatus()==1)
        {
            holder.title.setTypeface(null, Typeface.ITALIC);
            holder.title.setTypeface(null, Typeface.BOLD);
            holder.description.setTypeface(null, Typeface.ITALIC);
            holder.description.setTypeface(null, Typeface.BOLD);
            holder.task_view.setBackground(ContextCompat.getDrawable(mCtx, R.drawable.greyout));
            holder.checkBox.setChecked(true);
            holder.checkBox.setEnabled(false);
            int paddingDp = 10;
            float density = mCtx.getResources().getDisplayMetrics().density;
            int paddingPixel = (int)(paddingDp * density);
            holder.task_view.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);
        }

    }

    @Override
    public int getItemCount() {
        return addressCarModelList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView title,description;
        LinearLayout task_view;
        CheckBox checkBox;
        ImageView imageView;
        public ProductViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_tv);
            description = itemView.findViewById(R.id.description_tv);
            task_view = itemView.findViewById(R.id.task_view_ll);
            checkBox = itemView.findViewById(R.id.check);
            imageView = itemView.findViewById(R.id.imageview);
        }
    }

    public void updateList(List<TaskModel> list){
        addressCarModelList = list;
        notifyDataSetChanged();
    }

}