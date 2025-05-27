package com.example.testapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.MainActivity;
import com.example.testapplication.R;
import com.example.testapplication.activity.AddEmployeeActivity;
import com.example.testapplication.activity.EmployeeListActivity;
import com.example.testapplication.model.Employee;
import com.example.testapplication.service.ApiService;
import com.example.testapplication.util.ApiClient;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private Context context;
    private List<Employee> employeeList;
    private ApiService apiService;

    public EmployeeAdapter(Context context, List<Employee> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
        this.apiService = ApiClient.getApiService();
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {

        Employee employee = employeeList.get(position);
        holder.nameText.setText(employee.getName());
        holder.emailText.setText(employee.getEmail());
        holder.textDesignation.setText(employee.getDesignation());

        holder.updateButton.setOnClickListener(v -> {
            Log.d("Update", "Update clicked for" + employee.getName());
            Intent intent = new Intent(context, AddEmployeeActivity.class);
            intent.putExtra("employee", new Gson().toJson(employee));
            context.startActivity(intent);


        });
        holder.deleteButton.setOnClickListener(v -> {
            Log.d("Delete", "Update clicked for" + employee.getName());
      new AlertDialog.Builder(context)
              .setTitle("Delete")
              .setMessage("Are you sure, You want to delete"+employee.getName())
              .setPositiveButton("Yes",
                      (dialog, which) -> apiService.deleteEmployee(employee.getId())
                              .enqueue(new Callback<Void>() {
                                  @Override
                                  public void onResponse(Call<Void> call, Response<Void> response) {
                                      if (response.isSuccessful()) {
                                          int adapterPosition = holder.getAdapterPosition();
                                          if (adapterPosition != RecyclerView.NO_POSITION){
                                              employeeList.remove(adapterPosition);
                                              notifyItemRemoved(adapterPosition);
                                              notifyItemChanged(adapterPosition, employeeList.size());
                                          }
                                          Toast.makeText(context,"Employee deleted successfully", Toast.LENGTH_SHORT).show();

                                      } else {
                                          Toast.makeText(context,"Failed to delete employee" , Toast.LENGTH_SHORT).show();
                                      }

                                  }

                                  @Override
                                  public void onFailure(Call<Void> call, Throwable t) {
                                      Toast.makeText(context,"Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                                  }
                              }))
              .setNegativeButton("Cancel",null)
              .show();

        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, emailText, textDesignation;
        Button updateButton, deleteButton;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.nameText);
            emailText = itemView.findViewById(R.id.emailText);
            textDesignation = itemView.findViewById(R.id.textDesignation);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
