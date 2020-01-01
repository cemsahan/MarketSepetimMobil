package com.example.customermodule.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class LoginActivity extends AppCompatActivity {

    private EditText input_email_login,input_password_login;
    private Button loginButton;
    FirebaseAuth auth;
    private TextView notAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Define();
    }

    public void Define(){
        auth=FirebaseAuth.getInstance();
        input_email_login=findViewById(R.id.input_email_login);
        input_password_login=findViewById(R.id.input_password_login);
        notAccountText=findViewById(R.id.notAccountText);
        loginButton=findViewById(R.id.loginbutton);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=input_email_login.getText().toString();
                String pass=input_password_login.getText().toString();
                if (!email.equals("")&&!pass.equals("")) SignIn(email,pass);
                else Toast.makeText(getApplicationContext(),"e-mail ve parola boş girilemez",Toast.LENGTH_LONG).show();
            }
        });
        notAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
    public void SignIn(String email, String parola){

        auth.signInWithEmailAndPassword(email,parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{

                    Toast.makeText(getApplicationContext(),"Email veya parolanız yanlış...",Toast.LENGTH_LONG).show();

                }



            }
        });




    }




}
