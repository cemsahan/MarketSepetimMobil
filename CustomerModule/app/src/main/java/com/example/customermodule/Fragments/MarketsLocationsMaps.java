package com.example.customermodule.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customermodule.Activities.MainActivity;
import com.example.customermodule.Activities.MarketsOptionsActivity;
import com.example.customermodule.Models.Markets;
import com.example.customermodule.Models.Users;
import com.example.customermodule.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MarketsLocationsMaps extends Fragment {


    View view;
    MapView mMapView;
    GoogleMap googleMap;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    List<Markets> marketsList;
    Users currentUser;
    Double latLngX;
    Double latLngY;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_markets_locations_maps, container, false);
        mMapView = view.findViewById(R.id.map_marketsLocation);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        define();
        getInstanceMarkets();

        return view;
    }

    public void define() {

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        context=this.getContext();
        firebaseReference = firebaseDatabase.getReference();
        marketsList = new ArrayList<>();


    }

    public void getLocationMe() {


        firebaseReference.child("Customers").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(Users.class);

                String[] locationArray = currentUser.getLocation().split(",");
                latLngX = Double.parseDouble(locationArray[0]);
                latLngY = Double.parseDouble(locationArray[1]);
                mapDefine();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void getInstanceMarkets() {




        firebaseReference.child("Marketler").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Markets market=postSnapshot.getValue(Markets.class);
                    market.setKey(postSnapshot.getKey());
                    marketsList.add(market);

                }
                getLocationMe();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                final MarkerOptions myMarkerOptions = new MarkerOptions().position(sydney).draggable(true).title("Konumum");
                myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.locationicon));

                googleMap.addMarker(myMarkerOptions);
                CameraPosition cameraPositionFirst = new CameraPosition.Builder().target(sydney).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionFirst));

                for (Markets markets : marketsList) {
                    String[] locationArray = markets.getLocation().split(",");

                    double mX = Double.parseDouble(locationArray[0]);
                    double mY = Double.parseDouble(locationArray[1]);

                    if (((latLngX-0.027)<mX && mX<(latLngX+0.027))&&(latLngY-0.027)<mY && mY<(0.027+latLngY)) {
                        LatLng marketLocation = new LatLng(mX, mY);

                        googleMap.addMarker(new MarkerOptions().position(marketLocation).
                                icon(BitmapDescriptorFactory.fromBitmap(
                                        createCustomMarker(context, markets.getMarketName()))).snippet(markets.getKey())).setTitle(markets.getMarketName());

                    }
                }
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        if(!marker.getTitle().equals("Konumum")) {
                            String marketKey = marker.getSnippet();
                            Intent intent = new Intent(getContext(), MarketsOptionsActivity.class);
                            intent.putExtra("marketKey", marketKey);
                            intent.putExtra("userlatlngX", latLngX);
                            intent.putExtra("userlatlngY", latLngY);
                            startActivity(intent);
                        }
                        return false;
                    }
                });


            }
        });
    }


    public static Bitmap createCustomMarker(Context context, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        TextView txt_name = (TextView) marker.findViewById(R.id.name);
        txt_name.setText(_name);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);
        return bitmap;
    }


}
