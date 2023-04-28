package com.example.filder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filder.databinding.RowAlertBinding;
import com.example.filder.databinding.RowFieldBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AdapterAlert extends RecyclerView.Adapter<AdapterAlert.HolderAlert> {


    private Context context;
    private ArrayList<ModelAlert> alertsArrayList;
    private FirebaseAuth firebaseAuth;
    private RowAlertBinding binding;
    private AdapterAlert.HolderAlert holder;

    public AdapterAlert(Context context, ArrayList<ModelAlert> alertsArrayList) {
        this.context = context;
        this.alertsArrayList = alertsArrayList;
    }

    @NonNull
    @Override
    public HolderAlert onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = RowAlertBinding.inflate(LayoutInflater.from(context), parent, false);

        return new AdapterAlert.HolderAlert(binding.getRoot());

    }

    @Override
    public void onBindViewHolder(@NonNull HolderAlert holder, int position) {
        ModelAlert model = alertsArrayList.get(position);
        String id = model.getId();
        String context = model.getContext();
        String sender = model.getSender();
        String field = model.getField();
        String date = model.getDate();
        String text = model.getText();


        holder.alert_date.setText(""+date);
        holder.alert_context.setText(context);
        holder.alert_message.setText(text);


        holder.singleAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return alertsArrayList.size();
    }

    public void filterList(ArrayList<ModelAlert> filteredAlerts) {
        alertsArrayList = filteredAlerts;
        notifyDataSetChanged();
    }

    class HolderAlert extends RecyclerView.ViewHolder {
        TextView alert_date;
        TextView alert_message;
        TextView alert_context;
        CardView singleAlert;

        public HolderAlert(@NonNull View itemView) {
            super(itemView);
            alert_date = binding.alertDate;
            alert_message = binding.alertMessage;
            alert_context = binding.alertContext;
            singleAlert =binding.singleAlert;
        }
    }
}
