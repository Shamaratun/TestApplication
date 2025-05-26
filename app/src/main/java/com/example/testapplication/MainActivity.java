package com.example.testapplication;

import static androidx.core.content.res.TypedArrayUtils.getText;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.testapplication.activity.AddEmployeeActivity;
import com.example.testapplication.activity.EmployeeListActivity;
import com.example.testapplication.model.Employee;
import com.example.testapplication.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnAddEmployee = findViewById(R.id.btnAddEmployee);
        Button btnListEmployee = findViewById(R.id.btnListEmployee);

        btnAddEmployee.setOnClickListener(v -> navigateToAddEmployeePage());
        btnListEmployee.setOnClickListener(v -> navigateToEmployeeListPage());
    }

    private void navigateToAddEmployeePage() {
        Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
        startActivity(intent);
    }

    private void navigateToEmployeeListPage() {
        Intent intent = new Intent(MainActivity.this, EmployeeListActivity.class);
        startActivity(intent);
    }

}