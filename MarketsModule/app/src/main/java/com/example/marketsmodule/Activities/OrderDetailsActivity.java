package com.example.marketsmodule.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marketsmodule.Adapters.OrderDetailsAdapter;
import com.example.marketsmodule.R;
import com.example.marketsmodule.fragments.EditViewProduct;
import com.example.marketsmodule.fragments.UpdateProduct;
import com.example.marketsmodule.models.Order;
import com.example.marketsmodule.models.OrderContent;
import com.example.marketsmodule.models.Products;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {

    Order order;
    Double latLngX;
    Double latLngY;
    MapView mMapView;
    GoogleMap googleMap;
    Bundle savedInstanceState;
    RecyclerView rv_oDetail;
    List<Products> productsOrderList;
    OrderDetailsAdapter orderDetailsAdapter;
    TextView tv_totalPrice;
    Button btn_finishOrder;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        order = OrderContent.order;
        this.savedInstanceState = savedInstanceState;
        define();
        Log.i("xxxx", order.toString());

    }

    public void define() {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference();
        String[] locationArray = order.getCustomerInfo().getLocation().split(",");
        latLngX = Double.parseDouble(locationArray[0]);
        latLngY = Double.parseDouble(locationArray[1]);
        mMapView = findViewById(R.id.map_oDetail);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mapDefine();
        rv_oDetail = findViewById(R.id.rv_oDetail);
        tv_totalPrice = findViewById(R.id.tv_totalPrice);
        productsOrderList = new ArrayList<>();
        productsOrderList = order.getOrders();
        btn_finishOrder = findViewById(R.id.btn_finishOrder);

        RecyclerView.LayoutManager mng = new GridLayoutManager(getApplicationContext(), 1);
        rv_oDetail.setLayoutManager(mng);
        orderDetailsAdapter = new OrderDetailsAdapter(productsOrderList, OrderDetailsActivity.this, getApplicationContext());
        rv_oDetail.setAdapter(orderDetailsAdapter);

        double total = 0;
        for (Products products : productsOrderList) {
            double productPrice = Integer.parseInt(products.getProductSalePrice());
            double quantity = products.getOrderQuantity();
            double subTotal = productPrice * quantity;
            total += subTotal;
        }
        tv_totalPrice.setText("Toplam Fiyat : " + total + " TL");

        btn_finishOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(OrderDetailsActivity.this);
                dlgAlert.setMessage("Alışverişi bitirmek üzeresiniz");
                dlgAlert.setTitle("Kontrol");
                dlgAlert.setPositiveButton("İptal", null);
                dlgAlert.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishOrder();
                    }
                });
                dlgAlert.create().show();

            }
        });


    }

    public void finishOrder() {

        for (final Products p : order.getOrders()) {

            firebaseReference.child("products").child(auth.getUid()).child(p.getProductBarcodeNo())
                    .child("productQuantity").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int productQuantity = Integer.parseInt(dataSnapshot.getValue().toString());
                    int newQuantity = productQuantity - p.getOrderQuantity();
                    Map map = new HashMap();
                    map.put("productQuantity", newQuantity);
                    firebaseReference.child("products").child(auth.getUid()).child(p.getProductBarcodeNo())
                            .child("productQuantity").setValue(newQuantity + "");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        firebaseReference.child("Orders").child(auth.getUid()).child(order.getCustomerKey()).child(order.getOrderKey()).child("situation")
                .setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(OrderDetailsActivity.this, "Alışveriş tamamlandı", Toast.LENGTH_LONG).show();
                finish();
            }

        });


    }


    public void mapDefine() {
        try {
            MapsInitializer.initialize(OrderDetailsActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                LatLng sydney = new LatLng(latLngX, latLngY);
                final MarkerOptions myMarkerOptions = new MarkerOptions();
                myMarkerOptions.position(sydney).draggable(true);


                googleMap.addMarker(myMarkerOptions.title("Müşteri Konumu")).showInfoWindow();
                CameraPosition cameraPositionFirst = new CameraPosition.Builder().target(sydney).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionFirst));

            }
        });
    }


}
