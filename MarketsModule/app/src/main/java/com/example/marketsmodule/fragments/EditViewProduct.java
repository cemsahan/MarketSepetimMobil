package com.example.marketsmodule.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marketsmodule.Adapters.ProductsAdapter;
import com.example.marketsmodule.R;
import com.example.marketsmodule.models.Products;
import com.example.marketsmodule.utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class EditViewProduct extends Fragment {

    View view;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    List<Products> productsList;
    RecyclerView rv_editViewProduct;
    ProductsAdapter productsAdapter;
    ChangeFragment changeFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_edit_view_product, container, false);
        define();
        getMyProducts();


        return view;

    }

    public void define(){

        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseReference=firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        productsList=new ArrayList<>();
        changeFragment=new ChangeFragment(getContext());

        rv_editViewProduct=view.findViewById(R.id.rv_editViewProduct);
        RecyclerView.LayoutManager mng=new GridLayoutManager(getContext(),1);
        rv_editViewProduct.setLayoutManager(mng);
        productsAdapter=new ProductsAdapter(productsList,getActivity(),getContext());
        rv_editViewProduct.setAdapter(productsAdapter);



    }
    public void getMyProducts(){

        firebaseReference.child("products").child(auth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                productsList.add(dataSnapshot.getValue(Products.class));
                productsAdapter.notifyDataSetChanged();
                rv_editViewProduct.setAdapter(productsAdapter);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                productsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                productsAdapter.notifyDataSetChanged();
                changeFragment.change(new EditViewProduct());

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
