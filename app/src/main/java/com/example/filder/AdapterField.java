package com.example.filder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filder.databinding.RowFieldBinding;
import com.google.firebase.auth.FirebaseAuth;

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

        public HolderField(@NonNull View itemView) {
            super(itemView);
            field_number =binding.fieldNumber;
            localisation = binding.localisation;
        }
    }
}

