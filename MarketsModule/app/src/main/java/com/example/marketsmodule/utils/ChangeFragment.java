package com.example.marketsmodule.utils;


import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.marketsmodule.R;

public class ChangeFragment {
    private Context context;


    public ChangeFragment(Context context) {
        this.context = context;
    }

    public void change(Fragment fragment){


        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentlayout,fragment,"fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();


    }

    public void changeWithParameter(Fragment fragment,Bundle bundle){

        fragment.setArguments(bundle);

        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentlayout,fragment,"fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();


    }




}
