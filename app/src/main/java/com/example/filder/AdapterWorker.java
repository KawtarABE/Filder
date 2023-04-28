package com.example.filder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filder.databinding.RowWorkerBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterWorker extends RecyclerView.Adapter<AdapterWorker.HolderWorker>{
    private Context context;
    private ArrayList<ModelWorker> WorkerArrayList;
    private FirebaseAuth firebaseAuth;
    private RowWorkerBinding binding;


    public AdapterWorker(Context context, ArrayList<ModelWorker> WorkerArrayList) {
        this.context = context;
        this.WorkerArrayList = WorkerArrayList;
    }


    @NonNull
    @Override
    public AdapterWorker.HolderWorker onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowWorkerBinding.inflate(LayoutInflater.from(context),parent,false);

        return new HolderWorker(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterWorker.HolderWorker holder, int position) {
        ModelWorker model = WorkerArrayList.get(position);
        String Worker_name = model.getName();
        String Worker_email = model.getEmail().replace(",",".");
        String role = model.getRole();
        String field = model.getField();
        String img_uri = model.getImg_uri();

        holder.Worker.setText(Worker_name);
        holder.email.setText(Worker_email);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        StorageReference imageRef = storageRef.child("images/"+img_uri);

        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.imgUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });


        holder.singleWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailWorker.class);
                intent.putExtra("name", WorkerArrayList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("email_Worker", WorkerArrayList.get(holder.getAdapterPosition()).getEmail());
                intent.putExtra("role", WorkerArrayList.get(holder.getAdapterPosition()).getRole());
                intent.putExtra("field", WorkerArrayList.get(holder.getAdapterPosition()).getField());
                intent.putExtra("img_uri",WorkerArrayList.get(holder.getAdapterPosition()).getImg_uri());
                context.startActivity(intent);
            }
        });


        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:" + Worker_email));
                context.startActivity(intent);
            }
        });

        holder.singleWorker.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete field");
                builder.setMessage("Are you sure you want to delete this worker?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String fieldId = WorkerArrayList.get(holder.getAdapterPosition()).getEmail();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference itemRef = database.getReference("Users").child(fieldId);
                        itemRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context,"Delete successfuly..",Toast.LENGTH_SHORT).show();
                            }
                        });
                        WorkerArrayList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return WorkerArrayList.size();
    }

    public void filterList(ArrayList<ModelWorker> filteredWorkers) {
        WorkerArrayList = filteredWorkers;
        notifyDataSetChanged();
    }

    class HolderWorker extends RecyclerView.ViewHolder {
        TextView Worker;
        TextView email;
        CardView singleWorker;
        ImageView imgUser;
        ImageView message;
        public HolderWorker(@NonNull View itemView) {
            super(itemView);
            Worker =binding.Worker;
            email = binding.email;
            singleWorker = binding.singleWorker;
            imgUser = binding.imgUser;
            message = binding.message;
        }
    }
}


