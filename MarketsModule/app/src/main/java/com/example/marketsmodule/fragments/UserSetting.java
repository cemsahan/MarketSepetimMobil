package com.example.marketsmodule.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.marketsmodule.R;
import com.example.marketsmodule.models.Markets;
import com.example.marketsmodule.models.Products;
import com.example.marketsmodule.utils.ChangeFragment;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class UserSetting extends Fragment {

    MapView mMapView;
    GoogleMap googleMap;
    View view;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    Button btn_uptInformation;
    EditText et_inputAbout;
    EditText et_marketName;
    Markets myMarket;
    Double latLngX;
    Double latLngY;
    Button btn_openGoogleMaps;
    ImageView iv_marketProfileImage;
    Uri filePath;
    String imageUri = null;
    TextView et_inputPhoneNo;

    StorageReference storageReference;
    FirebaseStorage firebaseStorage;


    ScrollView userSetting_scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_setting, container, false);


        Define(savedInstanceState);
        getInstanceMarkets();
        return view;
    }

    public void getInstanceMarkets() {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference("Marketler").child(auth.getUid());

        firebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myMarket = dataSnapshot.getValue(Markets.class);

                if (getArguments() != null) {
                    latLngX = getArguments().getDouble("latLngX");
                    latLngY = getArguments().getDouble("latLngY");
                } else {
                    String[] locationArray = myMarket.getLocation().split(",");
                    latLngX = Double.parseDouble(locationArray[0]);
                    latLngY = Double.parseDouble(locationArray[1]);
                }

                et_inputAbout.setText(myMarket.getAboutMe() + "");
                et_marketName.setText(myMarket.getMarketName() + "");
                et_inputPhoneNo.setText(myMarket.getMarketPhoneNo());
                imageUri = myMarket.getMarketImg();
                if (!imageUri.equals("null")) {
                    iv_marketProfileImage.setBackgroundResource(android.R.color.transparent);
                    Picasso.get().load(imageUri).into(iv_marketProfileImage);
                }
                mapDefine();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void Define(Bundle savedInstanceState) {
        userSetting_scrollView = view.findViewById(R.id.userSetting_scrollView);
        et_inputPhoneNo=view.findViewById(R.id.et_inputPhoneNo);
        auth = FirebaseAuth.getInstance();
        mMapView = view.findViewById(R.id.map_userSetting);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        btn_uptInformation = view.findViewById(R.id.btn_uptInformation);
        et_inputAbout = view.findViewById(R.id.et_inputAbout);
        et_marketName = view.findViewById(R.id.et_marketName);
        iv_marketProfileImage = view.findViewById(R.id.iv_marketProfileImage);
        iv_marketProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalery();
            }
        });

        btn_openGoogleMaps = view.findViewById(R.id.btn_openGoogleMaps);


        btn_openGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putDouble("latLngX", latLngX);
                bundle.putDouble("latLngY", latLngY);
                ChangeFragment changeFragment = new ChangeFragment(getContext());
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

    }

    public void uptFirebase() {

        final String aboutMe = et_inputAbout.getText().toString();
        final String marketName = et_marketName.getText().toString();
        final String location = "" + latLngX + "," + latLngY;
        final String phoneNo = et_inputPhoneNo.getText().toString();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();

        if (filePath != null) {
            final StorageReference reference = storageReference.child("marketImages").child(auth.getUid() + ".jpg");
            reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUri = uri.toString();

                            Markets uptMarket = new Markets(aboutMe, location, imageUri, marketName,phoneNo);
                            firebaseReference = firebaseDatabase.getReference().child("Marketler").child(auth.getUid());
                            firebaseReference.setValue(uptMarket);


                        }
                    });
                }
            });
        } else {
            Markets uptMarket = new Markets(aboutMe, location, imageUri, marketName,phoneNo);
            firebaseReference = firebaseDatabase.getReference().child("Marketler").child(auth.getUid());
            firebaseReference.setValue(uptMarket);
        }


    }

    public void openGalery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && data != null) {
            filePath = data.getData();
            iv_marketProfileImage.setBackgroundResource(android.R.color.transparent);
            Picasso.get().load(filePath).into(iv_marketProfileImage);
        }
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
                myMarkerOptions.position(sydney).draggable(true).title(myMarket.getMarketName());


                googleMap.addMarker(myMarkerOptions);
                CameraPosition cameraPositionFirst = new CameraPosition.Builder().target(sydney).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionFirst));

            }
        });
    }
}