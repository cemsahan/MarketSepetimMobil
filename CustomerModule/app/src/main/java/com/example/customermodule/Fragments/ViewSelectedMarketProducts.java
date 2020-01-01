package com.example.customermodule.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.customermodule.Adapters.ProductsAdapter;
import com.example.customermodule.Models.Products;
import com.example.customermodule.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ViewSelectedMarketProducts extends Fragment {
    View view;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    List<Products> productsList;
    RecyclerView rv_viewProduct;
    ProductsAdapter productsAdapter;
    String selectedMarketKey;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_selected_market_products, container, false);
        define();
        getSelectedMarketProducts();

    return view;
    }


    public void define(){

        selectedMarketKey=getArguments().getString("marketKey");
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseReference=firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        productsList=new ArrayList<>();

        rv_viewProduct=view.findViewById(R.id.rv_viewProduct);
        RecyclerView.LayoutManager mng=new GridLayoutManager(getContext(),1);
        rv_viewProduct.setLayoutManager(mng);
        productsAdapter=new ProductsAdapter(productsList,getActivity(),getContext());
        rv_viewProduct.setAdapter(productsAdapter);



    }
    public void getSelectedMarketProducts(){

        firebaseReference.child("products").child(selectedMarketKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                productsList.add(dataSnapshot.getValue(Products.class));
                productsAdapter.notifyDataSetChanged();
                rv_viewProduct.setAdapter(productsAdapter);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                productsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                productsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                productsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                productsAdapter.notifyDataSetChanged();

            }
        });



    }





}
