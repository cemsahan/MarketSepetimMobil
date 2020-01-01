package com.example.customermodule.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.customermodule.Adapters.OldOrderAdapter;
import com.example.customermodule.Models.Order;
import com.example.customermodule.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MyOldOrder extends Fragment {
    View view;
    String marketKey;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    OldOrderAdapter oldOrderAdapter;
    List<Order> orderList;
    RecyclerView rv_myOldOlder;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_old_order, container, false);
        define();
        getMyOldOrder();
        return view;
    }

    public void define(){

        marketKey = getArguments().getString("marketKey");
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference();
        orderList = new ArrayList<>();
        rv_myOldOlder = view.findViewById(R.id.rv_myOldOlder);


        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(), 1);
        rv_myOldOlder.setLayoutManager(mng);
        oldOrderAdapter = new OldOrderAdapter(orderList, getActivity(), getContext());
        rv_myOldOlder.setAdapter(oldOrderAdapter);

    }
    public void getMyOldOrder() {

        firebaseReference.child("Orders").child(marketKey).child(auth.getUid()).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String customerKey = dataSnapshot1.getKey();

                        String situation = dataSnapshot1.getValue(Order.class).getSituation();
                            orderList.add(dataSnapshot1.getValue(Order.class));

                }

                oldOrderAdapter.notifyDataSetChanged();
                rv_myOldOlder.setAdapter(oldOrderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }





























}

