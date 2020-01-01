package com.example.customermodule.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import com.example.customermodule.Models.Users;
import com.example.customermodule.R;
import com.example.customermodule.Utils.ChangeFragment;

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

public class UserSetting extends Fragment {

    MapView mMapView;
    GoogleMap googleMap;
    View view;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    Button btn_uptInformation;
    EditText et_userSurname;
    EditText et_userName;
    Double latLngX;
    Double latLngY;
    Button btn_openGoogleMaps;
    Users currentUser;
    Button btn_back;
    ChangeFragment changeFragment ;
    EditText et_inputPhoneNo;

    ScrollView userSetting_scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_setting, container, false);


        Define(savedInstanceState);
        getInstanceUser();
        return view;
    }

    public void getInstanceUser() {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference("Customers").child(auth.getUid());

        firebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(Users.class);

                if (getArguments() != null) {
                    latLngX = getArguments().getDouble("latLngX");
                    latLngY = getArguments().getDouble("latLngY");
                } else {
                    String[] locationArray = currentUser.getLocation().split(",");
                    latLngX = Double.parseDouble(locationArray[0]);
                    latLngY = Double.parseDouble(locationArray[1]);
                }
                et_userName.setText(currentUser.getUserName());
                et_userSurname.setText(currentUser.getUserSurname());
                et_inputPhoneNo.setText(currentUser.getUserPhoneNo());

                mapDefine();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void Define(Bundle savedInstanceState) {
        userSetting_scrollView = view.findViewById(R.id.userSetting_scrollView);

        auth = FirebaseAuth.getInstance();
        mMapView = view.findViewById(R.id.map_userSetting);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        btn_uptInformation = view.findViewById(R.id.btn_uptInformation);
        et_userSurname = view.findViewById(R.id.et_userSurname);
        et_userName = view.findViewById(R.id.et_userName);
        et_inputPhoneNo=view.findViewById(R.id.et_inputPhoneNo);
        changeFragment = new ChangeFragment(getContext());

        btn_openGoogleMaps = view.findViewById(R.id.btn_openGoogleMaps);


        btn_openGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putDouble("latLngX", latLngX);
                bundle.putDouble("latLngY", latLngY);

                changeFragment.changeWithParameter(new addressSelection(), bundle);

            }
        });

        btn_uptInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
                dlgAlert.setMessage("Bilgiler güncellensin mi?");
                dlgAlert.setTitle("Uyarı!");
                dlgAlert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        uptFirebase();


                    }
                });
                dlgAlert.setNegativeButton("İptal", null);
                dlgAlert.create().show();
            }
        });
        btn_back=view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment.change(new MarketsLocationsMaps());
            }
        });

    }

    public void uptFirebase() {

         String surname = et_userSurname.getText().toString();
         String name = et_userName.getText().toString();
         String location = "" + latLngX + "," + latLngY;
        String phonoNo=et_inputPhoneNo.getText().toString();

        Users uptUser = new Users(name,surname,location,phonoNo);
        firebaseReference = firebaseDatabase.getReference().child("Customers").child(auth.getUid());
        firebaseReference.setValue(uptUser);
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
                myMarkerOptions.position(sydney).draggable(true).title("Konumunuz");

                googleMap.addMarker(myMarkerOptions);
                CameraPosition cameraPositionFirst = new CameraPosition.Builder().target(sydney).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionFirst));


            }
        });
    }
}