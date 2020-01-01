package com.example.customermodule.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.customermodule.Fragments.MarketsLocationsMaps;
import com.example.customermodule.Fragments.UserSetting;
import com.example.customermodule.R;
import com.example.customermodule.Utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    ChangeFragment changeFragment;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Define();
        UserCheck();


    }



    public void Define(){

        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        changeFragment=new ChangeFragment(MainActivity.this);


    }
    public void UserCheck(){


        if(user==null){

            Intent intent=new Intent( MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();


        }
        else{

            changeFragment.change(new MarketsLocationsMaps());

        }


    }

    public void signOut(){

        AlertDialog.Builder dlgAlert=new AlertDialog.Builder(this);
        dlgAlert.setMessage("Çıkış yapmak üzeresiniz");
        dlgAlert.setTitle("Uyarı!");
        dlgAlert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                auth.signOut();
                Intent intent =new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        dlgAlert.setNegativeButton("İptal",null);
        dlgAlert.create().show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.action_settings) {
            changeFragment.change(new UserSetting());
            return true;
        }
        if(id==R.id.action_out){
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
