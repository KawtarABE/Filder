package com.example.filder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filder.databinding.RowAlertBinding;
import com.example.filder.databinding.RowFieldBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterAlert extends RecyclerView.Adapter<AdapterAlert.HolderAlert> {
    private Context context;
    private ArrayList<ModelAlert> alertsArrayList;
    private FirebaseAuth firebaseAuth;
    private RowAlertBinding binding;
    private AdapterAlert.HolderAlert holder;
    private String email;


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
        String context_1 = model.getContext();
        String sender = model.getSender();
        String field = model.getField();
        String date = model.getDate();
        String text = model.getText();

        holder.alert_date.setText(""+date);
        holder.alert_context.setText(context_1);
        holder.alert_message.setText(text);
        holder.alert_sender.setText("Reporter: "+sender);
        holder.alert_field.setText("Field N°: "+field);


        holder.singleAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.singleAlert.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Alert");
                builder.setMessage("Are you sure you want to delete this alert?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String alertId = alertsArrayList.get(holder.getAdapterPosition()).getId();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference itemRef = database.getReference("Alerts").child(alertId);
                        itemRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context,"Delete successfuly..",Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertsArrayList.remove(holder.getAdapterPosition());
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
        TextView alert_sender;
        CardView singleAlert;
        TextView alert_field;

        public HolderAlert(@NonNull View itemView) {
            super(itemView);
            alert_date = binding.alertDate;
            alert_message = binding.alertMessage;
            alert_context = binding.alertContext;
            singleAlert =binding.singleAlert;
            alert_sender = binding.alertSender;
            alert_field = binding.alertField;
        }
    }
}
