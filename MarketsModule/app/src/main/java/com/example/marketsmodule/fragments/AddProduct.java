package com.example.marketsmodule.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.marketsmodule.utils.ChangeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.marketsmodule.R;
import com.example.marketsmodule.models.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

public class AddProduct extends Fragment {

    View view;
    EditText et_productBarcodeNo;
    EditText et_productName;
    EditText et_productUnit;
    EditText et_productCategory;
    EditText et_productQuantity;
    EditText et_productSalePrice;
    EditText et_productComePrice;
    Button btn_barcodeScanner;
    Button btn_productSave;
    CheckBox cb_checkBarcode;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    IntentIntegrator scanIntegrator;
    ChangeFragment changeFragment;
    ImageView iv_productImage;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    Uri filePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_product, container, false);

        define();


        return view;


    }


    public void define() {

        et_productBarcodeNo = view.findViewById(R.id.et_productBarcodeNo);
        et_productName = view.findViewById(R.id.et_productName);
        et_productUnit = view.findViewById(R.id.et_productUnit);
        et_productCategory = view.findViewById(R.id.et_productCategory);
        et_productQuantity = view.findViewById(R.id.et_productQuantity);
        et_productSalePrice = view.findViewById(R.id.et_productSalePrice);
        et_productComePrice = view.findViewById(R.id.et_productComePrice);
        btn_barcodeScanner = view.findViewById(R.id.btn_barcodeScanner);
        btn_productSave = view.findViewById(R.id.btn_productSave);
        cb_checkBarcode = view.findViewById(R.id.cb_checkBarcode);
        iv_productImage = view.findViewById(R.id.iv_productImage);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        changeFragment = new ChangeFragment(getContext());


        cb_checkBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cb_checkBarcode.isChecked()) {
                    et_productBarcodeNo.setText("");
                    btn_barcodeScanner.setEnabled(false);
                    et_productBarcodeNo.setEnabled(false);
                } else {
                    et_productBarcodeNo.setEnabled(true);
                    btn_barcodeScanner.setEnabled(true);
                }
            }
        });

        btn_productSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });

        scanIntegrator = new IntentIntegrator(getActivity());
        btn_barcodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanIntegrator.initiateScan();
            }
        });

        iv_productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGalery();
            }
        });


    }

    public void openGalery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && data != null) {

            filePath = data.getData();
            iv_productImage.setBackgroundResource(android.R.color.transparent);

            Picasso.get().load(filePath).into(iv_productImage);


        } else {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                et_productBarcodeNo.setText(scanContent);


                if (scanContent == null) scanContent = "null";


                Query query = firebaseReference.child("products").child(auth.getUid()).orderByKey().equalTo(scanContent);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            Products products = postSnapshot.getValue(Products.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("barcodeNo", products.getProductBarcodeNo());
                            bundle.putString("name", products.getProductName());
                            bundle.putString("category", products.getProductCategory());
                            bundle.putString("comePrice", products.getProductComePrice());
                            bundle.putString("quantity", products.getProductQuantity());
                            bundle.putString("salePrice", products.getProductSalePrice());
                            bundle.putString("unit", products.getProductUnit());
                            bundle.putString("senderFragment", "addProduct");
                            if (products.getProductImageUrl() != null)
                                bundle.putString("imageUri", products.getProductImageUrl());

                            changeFragment.changeWithParameter(new UpdateProduct(), bundle);

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




/*                query.addValueEventListener(new ValueEventListener() {



                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            Products products = postSnapshot.getValue(Products.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("barcodeNo", products.getProductBarcodeNo());
                            bundle.putString("name", products.getProductName());
                            bundle.putString("category", products.getProductCategory());
                            bundle.putString("comePrice", products.getProductComePrice());
                            bundle.putString("quantity", products.getProductQuantity());
                            bundle.putString("salePrice", products.getProductSalePrice());
                            bundle.putString("unit", products.getProductUnit());
                            bundle.putString("senderFragment", "addProduct");
                            if (products.getProductImageUrl() != null)
                                bundle.putString("imageUri", products.getProductImageUrl());

                            changeFragment.changeWithParameter(new UpdateProduct(), bundle);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }


  */
            }

        }
    }


    public void addProduct() {

        String productBarcodeNo = et_productBarcodeNo.getText().toString();
        String productName = et_productName.getText().toString();
        String productUnit = et_productUnit.getText().toString();
        String productCategory = et_productCategory.getText().toString();
        String productQuantity = et_productQuantity.getText().toString();
        String productSalePrice = et_productSalePrice.getText().toString();
        String productComePrice = et_productComePrice.getText().toString();


        if (cb_checkBarcode.isChecked()) {

            if (productName.equals("") || productUnit.equals("") || productCategory.equals("") ||
                    productQuantity.equals("") || productSalePrice.equals("") || productComePrice.equals("")) {
                Toast.makeText(getContext(), "Lütfen boş alanları doldurunuz", Toast.LENGTH_LONG).show();
            } else {
                saveDatabase("non-barcode " + productName, productName, productUnit, productCategory, productQuantity, productSalePrice, productComePrice);
            }

        } else {

            if (productBarcodeNo.equals("") || productName.equals("") || productUnit.equals("") || productCategory.equals("") ||
                    productQuantity.equals("") || productSalePrice.equals("") || productComePrice.equals("")) {
                Toast.makeText(getContext(), "Lütfen boş alanları doldurunuz", Toast.LENGTH_LONG).show();
            } else {
                saveDatabase(productBarcodeNo, productName, productUnit, productCategory, productQuantity, productSalePrice, productComePrice);
            }

        }

    }

    public void saveDatabase(final String productBarcodeNo, final String productName, final String productUnit, final String productCategory, final String productQuantity, final String productSalePrice, final String productComePrice) {

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
        dlgAlert.setMessage("Ürün Eklensin mi?\n" +
                "Barkod no\t\t: " + productBarcodeNo + "\n" +
                "Adı\t\t: " + productName + "\n" +
                "Birim\t\t: " + productUnit + "\n" +
                "Kategori\t\t: " + productCategory + "\n" +
                "Miktar\t\t: " + productQuantity + "\n" +
                "Alış Fiyatı\t\t: " + productComePrice + "\n" +
                "Satış Fiyatı\t\t: " + productSalePrice);
        dlgAlert.setTitle("Kontrol!");
        dlgAlert.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                et_productName.setText("");
                et_productCategory.setText("");
                et_productComePrice.setText("");
                et_productQuantity.setText("");
                et_productSalePrice.setText("");
                et_productUnit.setText("");
                et_productBarcodeNo.setText("");
                et_productBarcodeNo.setEnabled(true);
                btn_barcodeScanner.setEnabled(true);
                if (filePath != null) {
                    final StorageReference reference = storageReference.child("productImages").child(productBarcodeNo + ".jpg");
                    reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUri = uri.toString();

                                    Products sentProduct = new Products(productBarcodeNo, productName, productUnit, productCategory, productQuantity, productSalePrice, productComePrice, imageUri);
                                    firebaseReference.child("products").child(auth.getUid()).child(productBarcodeNo).setValue(sentProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //   Toast.makeText(getContext(), "Ürün başarılı bir şekilde eklendi.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {
                    Products sentProduct = new Products(productBarcodeNo, productName, productUnit, productCategory, productQuantity, productSalePrice, productComePrice, null);
                    firebaseReference.child("products").child(auth.getUid()).child(productBarcodeNo).setValue(sentProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            et_productName.setText("");
                            et_productCategory.setText("");
                            et_productComePrice.setText("");
                            et_productQuantity.setText("");
                            et_productSalePrice.setText("");
                            et_productUnit.setText("");
                            et_productBarcodeNo.setText("");
                            et_productBarcodeNo.setEnabled(true);
                            btn_barcodeScanner.setEnabled(true);
                            // Toast.makeText(getContext(), "Ürün başarılı bir şekilde eklendi.", Toast.LENGTH_LONG).show();
                        }


                    });

                }
            }

        });
        dlgAlert.setNegativeButton("İptal", null);
        dlgAlert.create().show();

    }
}