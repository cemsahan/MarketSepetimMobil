package com.example.customermodule.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customermodule.Adapters.OrderProductAdapter;
import com.example.customermodule.Adapters.ProductsAdapter;
import com.example.customermodule.Models.OrderList;
import com.example.customermodule.Models.Products;
import com.example.customermodule.Models.Users;
import com.example.customermodule.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MyOrders extends Fragment {

    RecyclerView rv_orderProduct;
    OrderProductAdapter orderProductAdapter;
    TextView tv_totalPrice;
    View view;
    Button btn_finishOrder;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    String marketKey;
    Button btn_orderClear;
    Users currentUser;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        define();
        showMyOrder();


        return view;
    }

    public void define() {
        marketKey = getArguments().getString("marketKey");
        rv_orderProduct = view.findViewById(R.id.rv_orderProduct);
        tv_totalPrice = view.findViewById(R.id.tv_totalPrice);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference();
        myInfo();
        btn_orderClear = view.findViewById(R.id.btn_orderClear);
        btn_orderClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!OrderList.orderList.isEmpty()) {

                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
                    dlgAlert.setMessage("Sipariş listenizi temizlemek üzeresiniz");
                    dlgAlert.setTitle("Kontrol");
                    dlgAlert.setPositiveButton("İptal", null);
                    dlgAlert.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            OrderList.orderList.clear();
                            showMyOrder();
                        }
                    });
                    dlgAlert.create().show();

                }
            }
        });

        btn_finishOrder = view.findViewById(R.id.btn_finishOrder);

        btn_finishOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!OrderList.orderList.isEmpty()) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
                    final EditText input = new EditText(getContext());
                    input.setText("");
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    dlgAlert.setView(input);
                    dlgAlert.setMessage("Satıcıya mesajınız(opsiyoneldir)");
                    dlgAlert.setTitle("Sipariş Tamamla");
                    dlgAlert.setPositiveButton("İptal", null);
                    dlgAlert.setNegativeButton("Siparişi bitir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            orderAddToFirebase(input.getText().toString());
                            OrderList.orderList.clear();
                            showMyOrder();
                        }
                    });
                    dlgAlert.create().show();


                } else
                    Toast.makeText(getContext(), "Sipariş listeniz boş", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void showMyOrder() {
        double totalPrice = 0;

        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(), 1);
        rv_orderProduct.setLayoutManager(mng);
        orderProductAdapter = new OrderProductAdapter(OrderList.orderList, getActivity(), getContext(), tv_totalPrice);
        rv_orderProduct.setAdapter(orderProductAdapter);

        for (Products product : OrderList.orderList) {
            int rowPrice = 0;
            rowPrice = product.getOrderQuantity() * Integer.parseInt(product.getProductSalePrice());
            totalPrice += rowPrice;
        }
        tv_totalPrice.setText("Toplam fiyat : " + totalPrice + " TL");
    }
    public void myInfo(){
        firebaseReference.child("Customers").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void orderAddToFirebase(String statement) {


        String orderKey=firebaseReference.child("Orders").child(marketKey).child(auth.getUid()).push().getKey();


        Map mapOrder = new HashMap();
        mapOrder.put("orders", OrderList.orderList);
        mapOrder.put("orderKey", orderKey);
        mapOrder.put("statement", statement);
        mapOrder.put("situation", "0");
        mapOrder.put("customerKey", auth.getUid());
        mapOrder.put("date", getDateTime());
        mapOrder.put("customerInfo",currentUser);


        firebaseReference.child("Orders").child(marketKey).child(auth.getUid()).child(orderKey)
                .setValue(mapOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Siparişiniz satıcıya iletildi", Toast.LENGTH_LONG).show();
            }

        });
    }

    public String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }


}





















