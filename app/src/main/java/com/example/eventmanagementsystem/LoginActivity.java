package com.example.eventmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    ConnectionClass connectionClass;
    Connection con;
    ResultSet rs;
    EditText emailEditText, passwordEditText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        emailEditText = findViewById(R.id.emailInput);
        passwordEditText = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        connectionClass = new ConnectionClass();

        // Set the listener for the login button
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(v);

            }
        });

        // Set the listener for the register link
        TextView registerLink = findViewById(R.id.registerLink);
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    public void loginUser(View v) {
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

//        Log.d("Login", "Email: " + email + ", Password: " + password);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                // Establish connection with the database
                con = connectionClass.CONN();

                if (con != null) {
                    Log.d("Database", "Connected to the database");

                    String query = "SELECT * FROM users WHERE email = ?";
                    PreparedStatement stmt = con.prepareStatement(query);
                    stmt.setString(1, email);
                    rs = stmt.executeQuery();
                    // If a matching user is found
                    if (rs.next()) {
                        // Get the hashed password from the database

                        String storedHashedPassword = rs.getString("password");
                        storedHashedPassword = storedHashedPassword.replace("$2b$", "$2a$");

                        if (storedHashedPassword.startsWith("$2a$")) {
                            if (BCrypt.checkpw(password, storedHashedPassword)) {

                                int userID = 0;
                                int isSupplier , isHost;
                                isSupplier = rs.getInt("isSupplier");
                                isHost = rs.getInt("isHost");
                                userID = rs.getInt("id");
                                int finalUserID = userID;

                                runOnUiThread(() -> {
                                    if(isSupplier == 1) {
                                        Toast.makeText(LoginActivity.this, "Login Successful; You are a Supplier", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, SupplierActivity.class);
                                        intent.putExtra("userID", finalUserID);
                                        startActivity(intent);
                                    } else if (isHost == 1) {
                                        Toast.makeText(LoginActivity.this, "Login Successful; You are a Host", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, HostActivity.class);
                                        intent.putExtra("userID", finalUserID);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Login Successful; You are a normal User", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                });
                        }
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    // Connection failed
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Error in connection with MYSQL server", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } finally {
                // Clean up resources
                try {
                    if (rs != null) rs.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}