package com.example.customermodule.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customermodule.Activities.MarketsOptionsActivity;
import com.example.customermodule.Models.Markets;
import com.example.customermodule.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MarketInfoFragment extends Fragment {
    View view;
    MapView mMapView;
    GoogleMap googleMap;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    EditText et_inputAbout;
    EditText et_marketName;
    Double selectedMarketlatLngX;
    Double selectedMarketlatLngY;
    Double userlatlngX;
    Double userlatlngY;
    ImageView iv_marketProfileImage;
    Markets selectedMarket;
    EditText et_inputPhoneNo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_market_info, container, false);
        Define(savedInstanceState);
        fillSelectedMarketInfo();



        return view;
    }


    public void fillSelectedMarketInfo(){

        final String marketKey = getArguments().getString("marketKey");

        Query query = firebaseReference.child("Marketler").orderByKey().equalTo(marketKey);


       query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    selectedMarket=postSnapshot.getValue(Markets.class);
                    selectedMarket.setKey(marketKey);
                    String[] locationArray=selectedMarket.getLocation().split(",");
                    selectedMarketlatLngX=Double.parseDouble(locationArray[0]);
                    selectedMarketlatLngY=Double.parseDouble(locationArray[1]);
                    et_inputAbout.setText(selectedMarket.getAboutMe());
                    et_marketName.setText(selectedMarket.getMarketName());
                    et_inputPhoneNo.setText(selectedMarket.getMarketPhoneNo());
                    String  imageUri = selectedMarket.getMarketImg();
                    if (!imageUri.equals("null")) {
                        iv_marketProfileImage.setBackgroundResource(android.R.color.transparent);
                        Picasso.get().load(imageUri).into(iv_marketProfileImage);
                    }

                    mapDefine();

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void Define(Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference();
        mMapView = view.findViewById(R.id.map_userSetting);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        et_inputAbout = view.findViewById(R.id.et_inputAbout);
        et_marketName = view.findViewById(R.id.et_marketName);
        et_inputPhoneNo=view.findViewById(R.id.et_inputPhoneNo);
        iv_marketProfileImage = view.findViewById(R.id.iv_marketProfileImage);
        userlatlngX=getArguments().getDouble("userlatlngX");
        userlatlngY=getArguments().getDouble("userlatlngY");

    }

    public void mapDefine() {

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                LatLng selecteMarketLatLng = new LatLng(selectedMarketlatLngX, selectedMarketlatLngY);
                MarkerOptions marketMarkerOptions = new MarkerOptions();
                marketMarkerOptions.position(selecteMarketLatLng).draggable(true).title(selectedMarket.getMarketName()+" Konumu");

                LatLng userLatlng=new LatLng(userlatlngX,userlatlngY);
                MarkerOptions userMarkerOptions=new MarkerOptions();
                userMarkerOptions.position(userLatlng).draggable(true).title("Konumunuz").icon(BitmapDescriptorFactory.fromResource(R.drawable.locationicon));
                googleMap.addMarker(userMarkerOptions).showInfoWindow();

                googleMap.addMarker(marketMarkerOptions).showInfoWindow();
                CameraPosition cameraPositionFirst = new CameraPosition.Builder().target(selecteMarketLatLng).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionFirst));

            }
        });
    }





}
