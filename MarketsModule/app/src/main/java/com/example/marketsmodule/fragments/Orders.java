package com.example.marketsmodule.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ForwardingListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marketsmodule.Activities.MainActivity;
import com.example.marketsmodule.Adapters.OrdersCustomerAdapter;
import com.example.marketsmodule.R;
import com.example.marketsmodule.models.Customer;
import com.example.marketsmodule.models.Order;
import com.example.marketsmodule.utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Orders extends Fragment {

    View view;
    RecyclerView rv_orderProduct;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    DatabaseReference firebaseReference1;
    OrdersCustomerAdapter ordersCustomerAdapter;
    TextView tv_allOrder;
    ChangeFragment changeFragment;

    List<Order> orderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orders, container, false);

        define();
        getOrdersCustomer();


        return view;
    }

    public void define() {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference();
        firebaseReference1 = firebaseDatabase.getReference();
        orderList = new ArrayList<>();
        rv_orderProduct = view.findViewById(R.id.rv_orderProduct);


        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(), 1);
        rv_orderProduct.setLayoutManager(mng);
        ordersCustomerAdapter = new OrdersCustomerAdapter(orderList, getActivity(), getContext());
        rv_orderProduct.setAdapter(ordersCustomerAdapter);
        changeFragment=new ChangeFragment(getContext());
        tv_allOrder=view.findViewById(R.id.tv_allOrder);
        tv_allOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment.change(new OldOrders());
            }
        });


    }

    public void getOrdersCustomer() {

        firebaseReference.child("Orders").child(auth.getUid()).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String customerKey = dataSnapshot1.getKey();

                    for (DataSnapshot postOrders : dataSnapshot.child(customerKey).getChildren()) {
                        String situation = postOrders.getValue(Order.class).getSituation();
                        Log.i("xxxx",postOrders.getValue(Order.class).toString());
                        if (situation.equals("0")) {
                            orderList.add(postOrders.getValue(Order.class));
                        }
                    }
                }

                ordersCustomerAdapter.notifyDataSetChanged();
                rv_orderProduct.setAdapter(ordersCustomerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
