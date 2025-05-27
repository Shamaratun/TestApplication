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


    private EditText nameText, emailText, designationText, multilineAddress,
            doubleSalary, numberAge;
    //,  textPhone
    private TextInputEditText textDate;
    private TextInputLayout dateLayout;
    private Button button, btnListPage;
    private ApiService apiService;

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

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        designationText = findViewById(R.id.designationText);
        multilineAddress = findViewById(R.id.multilineAddress);
        doubleSalary = findViewById(R.id.doubleSalary);
        numberAge = findViewById(R.id.numberAge);
        textDate = findViewById(R.id.textDate);
//        textPhone = findViewById(R.id.textPhone);
        dateLayout=findViewById(R.id.dateLayout);
        button = findViewById(R.id.button);
        btnListPage=findViewById(R.id.btnListPage);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

    dateLayout.setEndIconOnClickListener(v-> showDatePicker());
        button.setOnClickListener(v -> saveEmployee());
        btnListPage.setOnClickListener(v -> navigateToEmployeeListPage());
    }

    private void navigateToEmployeeListPage() {
        Intent intent =new Intent(MainActivity.this,EmployeeListActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(this, (view, year1, month1, day1) -> {
            String dob = String.format(Locale.US, "%4d-%02d-%02d", year1, month1 + 1, day1 + 1);
            textDate.setText(dob);
        },
                year, month, day);
        picker.show();
    }

    private void saveEmployee() {

            String name = nameText.getText().toString().trim();
            String email = emailText.getText().toString().trim();
            String designation = designationText.getText().toString().trim();
            String address = multilineAddress.getText().toString().trim();
            String salary = doubleSalary.getText().toString().trim();
            String age = numberAge.getText().toString().trim();
//            String phoneNo = textPhone.getText().toString().trim();
            String dobString = textDate.getText().toString().trim();


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(dobString, formatter);

            Employee employee = new Employee();
            employee.setName(name);
            employee.setEmail(email);
            employee.setDesignation(designation);
            employee.setAge(Integer.parseInt(age));
            employee.setAddress(address);
        String dateOfBirth;
        employee.setDateOfBirth(dobString);
            employee.setSalary(Double.parseDouble(salary));

            Call<Employee> call = apiService.saveEmployee(employee);
            String string = call.toString();
            System.out.println(string);
            call.enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Employee saved successfully", Toast.LENGTH_SHORT).show();
                        clearForm();
                        Intent intent = new Intent(MainActivity.this, EmployeeListActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to save employee" + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        }
        private void clearForm () {
            nameText.setText("");
            emailText.setText("");
//        textPhone.setText("");
            numberAge.setText("");
            textDate.setText(""); // fixed this
            doubleSalary.setText("");
            multilineAddress.setText("");
            designationText.setText("");
        }
    }
