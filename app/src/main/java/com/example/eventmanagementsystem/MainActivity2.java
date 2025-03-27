package com.example.eventmanagementsystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity2 extends AppCompatActivity {

    ConnectionClass connectionClass;
    Connection con;
    ResultSet rs;
    String name, str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        connectionClass=new ConnectionClass();
        connect();

        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);
        TextView registerLink = findViewById(R.id.registerLink);

        // Handle the login button click
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailInput.getText().toString();
//                String password = passwordInput.getText().toString();
//
//                if (email.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(MainActivity2.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//                } else {
//                    // For now, show a success message (replace with actual login logic)
//                    Toast.makeText(MainActivity2.this, "Login Successful!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        // Handle the register link click
//        registerLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to the Register Activity (create a new activity for registration)
//                Intent intent = new Intent(MainActivity2.this, RegisterActivity.class);
//                startActivity(intent);
//            }

    }


    public void connect(){
        ExecutorService executorrService= Executors.newSingleThreadExecutor();
        executorrService.execute(()->{
            try{
                con=connectionClass.CONN();
                if(con==null){
                    str="Error in connection with MYSQL server";
                }else{
                    str="Conected with MYSQL server";
                }
            }catch(Exception e){
                throw new RuntimeException(e);
            }

            runOnUiThread(()->{
                try{
                  Thread.sleep(1000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
            });
        });
    }
}