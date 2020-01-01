package com.example.customermodule.Activities;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.example.customermodule.Fragments.MyOldOrder;
import com.example.customermodule.Fragments.MyOrders;
import com.example.customermodule.Fragments.MarketInfoFragment;
import com.example.customermodule.Fragments.ViewSelectedMarketProducts;
import com.example.customermodule.R;
import com.example.customermodule.Utils.ChangeFragment;

public class MarketsOptionsActivity extends AppCompatActivity {

ChangeFragment changeFragment;
    Bundle extras;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment.changeWithParameter(new MarketInfoFragment(),extras);

                    return true;
                case R.id.navigation_dashboard:
                    changeFragment.changeWithParameter(new ViewSelectedMarketProducts(),extras);
                    return true;
                case R.id.navigation_notifications:
                    changeFragment.changeWithParameter(new MyOrders(),extras);
                    return true;
                    case R.id.navigation_oldOrder:
                    changeFragment.changeWithParameter(new MyOldOrder(),extras);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markets_options);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        define();
        extras = getIntent().getExtras();
        changeFragment.changeWithParameter(new MarketInfoFragment(),extras);




    }

    public void define(){

        changeFragment=new ChangeFragment(MarketsOptionsActivity.this);

    }


}
