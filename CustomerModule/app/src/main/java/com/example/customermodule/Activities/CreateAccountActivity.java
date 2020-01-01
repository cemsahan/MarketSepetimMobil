package com.example.customermodule.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customermodule.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {


    TextView hesapvarText;
    EditText input_email, input_password;
    Button registerButton;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Define();
        Actions();
    }

    public void Define() {

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        registerButton = findViewById(R.id.registerButton);
        hesapvarText = findViewById(R.id.hesapvarText);

        auth = FirebaseAuth.getInstance();


    }
    public void Actions() {


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = input_email.getText().toString();
                String pass = input_password.getText().toString();

                if (!email.equals("") && pass.length() >= 6) {
                    createAccount(email, pass);
                    input_email.setText("");
                    input_password.setText("");
                } else
                    Toast.makeText(getApplicationContext(), "Lütfen bilgilerini eksiksiz doldurunuz...", Toast.LENGTH_LONG).show();
            }
        });

        hesapvarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();


            }
        });

    }
    public void createAccount(String email, String password) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    reference = firebaseDatabase.getReference().child("Customers").child(auth.getUid());

                    Map map = new HashMap();
                    map.put("userName", "null");
                    map.put("userSurname", "null");
                    map.put("location", "38.77141764932852,35.54501373320818");
                    map.put("userPhoneNo","null");
                    reference.setValue(map);
                    Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else
                {
                    Toast.makeText(getApplicationContext(), "Geçersiz e-mail adresi.", Toast.LENGTH_LONG).show();
                    Log.i("xxxx",task.getException()+"");

                }


            }
        });
    }



}
