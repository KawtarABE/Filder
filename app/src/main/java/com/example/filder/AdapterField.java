package com.example.filder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filder.databinding.RowFieldBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterField extends RecyclerView.Adapter<AdapterField.HolderField> {
    private Context context;
    private ArrayList<ModelField> fieldsArrayList;
    private FirebaseAuth firebaseAuth;
    private RowFieldBinding binding;
    private AdapterField.HolderField holder;

    public AdapterField(Context context, ArrayList<ModelField> fieldsArrayList) {
        this.context = context;
        this.fieldsArrayList = fieldsArrayList;
    }

    @NonNull
    @Override
    public AdapterField.HolderField onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowFieldBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderField(binding.getRoot());
    }

    public void onBindViewHolder(@NonNull AdapterField.HolderField holder, int position) {
        ModelField model = fieldsArrayList.get(position);
        String id = model.getId();
        String number = model.getNumero();
        String location = model.getLocation();
        String surface = model.getSurface();
        String owner = model.getOwner();
        String Temperature = model.getTemperature();
        String UV = model.getUV();
        String humidity = model.getHumidity();

        holder.field_number.setText("Field N° "+number);
        holder.localisation.setText(location);

        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, detail_field.class);
                intent.putExtra("id", fieldsArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("location", fieldsArrayList.get(holder.getAdapterPosition()).getLocation());
                intent.putExtra("surface", fieldsArrayList.get(holder.getAdapterPosition()).getSurface());
                intent.putExtra("numero", fieldsArrayList.get(holder.getAdapterPosition()).getNumero());
                intent.putExtra("owner", fieldsArrayList.get(holder.getAdapterPosition()).getOwner());
                intent.putExtra("humidity", fieldsArrayList.get(holder.getAdapterPosition()).getHumidity());
                intent.putExtra("temperature", fieldsArrayList.get(holder.getAdapterPosition()).getTemperature());
                intent.putExtra("UV", fieldsArrayList.get(holder.getAdapterPosition()).getUV());
                context.startActivity(intent);
            }
        });

        holder.singleField.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete field");
                builder.setMessage("Are you sure you want to delete this field?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String fieldId = fieldsArrayList.get(holder.getAdapterPosition()).getId();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference itemRef = database.getReference("Fields").child(fieldId);
                        itemRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context,"Delete successfuly..",Toast.LENGTH_SHORT).show();
                            }
                        });
                        fieldsArrayList.remove(holder.getAdapterPosition());
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
        return fieldsArrayList.size();
    }

    public void filterList(ArrayList<ModelField> filteredContacts) {
        fieldsArrayList = filteredContacts;
        notifyDataSetChanged();
    }

    class HolderField extends RecyclerView.ViewHolder {
        TextView field_number;
        TextView localisation;
        TextView detail;
        CardView singleField;

        public HolderField(@NonNull View itemView) {
            super(itemView);
            field_number =binding.fieldNumber;
            localisation = binding.localisation;
            detail = binding.detail;
            singleField =binding.singleField;
        }
    }
}

