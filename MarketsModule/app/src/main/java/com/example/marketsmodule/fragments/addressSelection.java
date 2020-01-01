package com.example.marketsmodule.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.marketsmodule.R;
import com.example.marketsmodule.models.Markets;
import com.example.marketsmodule.utils.ChangeFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addressSelection extends Fragment {

    View view;
    MapView mMapView;
    GoogleMap googleMap;
    Double latLngX;
    Double latLngY;
    FirebaseAuth auth;
    Button btn_addressSelectionOk;
    ChangeFragment changeFragment;
    Button btn_addressSelectionCancel;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    Markets myMarket;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_address_selection, container, false);
        Define(savedInstanceState);

        return view;

    }

    public void Define(Bundle savedInstanceState) {

        latLngX=getArguments().getDouble("latLngX");
        latLngY=getArguments().getDouble("latLngY");
        auth = FirebaseAuth.getInstance();
        mMapView = view.findViewById(R.id.map_userSetting);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mapDefine();
        changeFragment=new ChangeFragment(getContext());
        btn_addressSelectionOk=view.findViewById(R.id.btn_addressSelectionOk);
        btn_addressSelectionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putDouble("latLngX",latLngX);
                bundle.putDouble("latLngY",latLngY);
                bundle.putString("senderFrgmnt","addressSelection");
                changeFragment.changeWithParameter(new UserSetting(),bundle);
            }
        });
        btn_addressSelectionCancel=view.findViewById(R.id.btn_addressSelectionCancel);
        btn_addressSelectionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment.change(new UserSetting());
            }
        });



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
                LatLng sydney = new LatLng(latLngX, latLngY);
                final MarkerOptions myMarkerOptions = new MarkerOptions();
                myMarkerOptions.position(sydney).draggable(true);


                googleMap.addMarker(myMarkerOptions);
                CameraPosition cameraPositionFirst = new CameraPosition.Builder().target(sydney).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionFirst));

                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        latLngX = cameraPosition.target.latitude;
                        latLngY = cameraPosition.target.longitude;
                        googleMap.clear();
                        myMarkerOptions.position(cameraPosition.target).draggable(true);
                        googleMap.addMarker(myMarkerOptions);
                    }
                });
            }
        });
    }

}
