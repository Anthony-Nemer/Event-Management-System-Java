package com.example.eventmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    // UI elements
    EditText fullNameEditText, emailEditText, passwordEditText, confirmPasswordEditText, phoneEditText, employeePinEditText;
    RadioGroup userRoleRadioGroup;
    Button registerButton;
    Spinner supplierServiceSpinner;
    ConnectionClass connectionClass;

    // Service mapping
    private HashMap<String, String> serviceMap = new HashMap<>();
    private ArrayList<String> serviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI elements
        fullNameEditText = findViewById(R.id.fullname);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        phoneEditText = findViewById(R.id.number);
        userRoleRadioGroup = findViewById(R.id.radioGrpUserRole);
        registerButton = findViewById(R.id.registerButton);
        supplierServiceSpinner = findViewById(R.id.supplierServiceSpinner);
        employeePinEditText = findViewById(R.id.employeePin);
        connectionClass = new ConnectionClass();

        // Initially hide employee PIN and supplier service spinner
        employeePinEditText.setVisibility(View.GONE);
        supplierServiceSpinner.setVisibility(View.GONE);

        // Listen for role selection changes
        userRoleRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.host) {
                employeePinEditText.setVisibility(View.VISIBLE);
                supplierServiceSpinner.setVisibility(View.GONE);
            } else if (checkedId == R.id.supplier) {
                supplierServiceSpinner.setVisibility(View.VISIBLE);
                employeePinEditText.setVisibility(View.GONE);
            } else {
                supplierServiceSpinner.setVisibility(View.GONE);
                employeePinEditText.setVisibility(View.GONE);
            }
        });

        // Fetch services for supplier dropdown
        fetchServices();

        // Set listener for register button
        registerButton.setOnClickListener(v -> {
            String fullName = fullNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                int selectedRoleId = userRoleRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedRoleButton = findViewById(selectedRoleId);
                String userRole = selectedRoleButton.getText().toString();
                registerUser(fullName, email, password, phone, userRole);
            }
        });
    }

    private void fetchServices() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Connection con = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                con = connectionClass.CONN();
                if (con != null) {
                    String query = "SELECT id, cuisine FROM cuisines";
                    stmt = con.prepareStatement(query);
                    rs = stmt.executeQuery();

                    serviceList.clear();
                    serviceMap.clear();

                    while (rs.next()) {
                        String id = rs.getString("id");
                        String name = rs.getString("cuisine");

                        serviceMap.put(name, id); // Map service_name -> id
                        serviceList.add(name); // Add service_name to list
                    }

                    runOnUiThread(() -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                RegisterActivity.this,
                                android.R.layout.simple_spinner_item,
                                serviceList
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        supplierServiceSpinner.setAdapter(adapter);
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void registerUser(String fullName, String email, String password, String phone, String userRole) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Connection con = null;
            PreparedStatement stmt = null;

            try {
                con = connectionClass.CONN();
                if (con != null) {
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                    int ifHost = 0;
                    int ifSupplier = 0;
                    String serviceId = null;
                    String employeePin = null;

                    if (userRole.equals("Host")) {
                        ifHost = 1;
                        employeePin = employeePinEditText.getText().toString().trim();
                    } else if (userRole.equals("Supplier")) {
                        ifSupplier = 1;
                        if (supplierServiceSpinner.getSelectedItem() != null) {
                            String selectedServiceName = supplierServiceSpinner.getSelectedItem().toString();
                            serviceId = serviceMap.get(selectedServiceName); // Get corresponding ID
                        }
                    }

                    String query = "INSERT INTO users (fullname, email, password, mobilenumber, isSupplier, isHost, cuisineId, employee_pin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    stmt = con.prepareStatement(query);
                    stmt.setString(1, fullName);
                    stmt.setString(2, email);
                    stmt.setString(3, hashedPassword);
                    stmt.setString(4, phone);
                    stmt.setInt(5, ifSupplier);
                    stmt.setInt(6, ifHost);
                    stmt.setString(7, serviceId);
                    stmt.setString(8, employeePin);

                    int result = stmt.executeUpdate();

                    if (result > 0) {
                        runOnUiThread(() -> {
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Error in connection with MYSQL server", Toast.LENGTH_SHORT).show());
                }
            } catch (SQLException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
