package com.example.marketsmodule.Activities;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marketsmodule.R;
import com.example.marketsmodule.fragments.AddProduct;
import com.example.marketsmodule.fragments.EditViewProduct;
import com.example.marketsmodule.fragments.OldOrders;
import com.example.marketsmodule.fragments.Orders;
import com.example.marketsmodule.fragments.UserSetting;
import com.example.marketsmodule.utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseUser user;
    ChangeFragment changeFragment;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment.change(new EditViewProduct());
                    return true;
                case R.id.navigation_dashboard:
                    changeFragment.change(new AddProduct());
                    return true;
                case R.id.navigation_notifications:
                    changeFragment.change(new UserSetting());
                    return true;
                case R.id.navigation_exit:
                    SignOut();
                    return true;
                case R.id.navigation_myOrder:
                    changeFragment.change(new Orders());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Define();
        UserCheck();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void Define() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        changeFragment = new ChangeFragment(MainActivity.this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {

            fragment.onActivityResult(requestCode, resultCode, data);

        }
    }

    public void UserCheck() {


        if (user == null) {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();


        } else {

            changeFragment.change(new EditViewProduct());

        }


    }

    public void SignOut() {

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Çıkış yapmak üzeresiniz");
        dlgAlert.setTitle("Uyarı!");
        dlgAlert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                auth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        dlgAlert.setNegativeButton("İptal", null);
        dlgAlert.create().show();


    }
}




















