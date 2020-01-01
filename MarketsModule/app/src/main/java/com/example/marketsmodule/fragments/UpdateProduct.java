package com.example.marketsmodule.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.marketsmodule.R;
import com.example.marketsmodule.models.Products;
import com.example.marketsmodule.utils.ChangeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class UpdateProduct extends Fragment {

    View view;
    EditText et_productBarcodeNo;
    EditText et_productName;
    EditText et_productUnit;
    EditText et_productCategory;
    EditText et_productQuantity;
    EditText et_productSalePrice;
    EditText et_productComePrice;
    Button btn_productSave;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    ChangeFragment changeFragment;
    ImageView iv_productImage;
    Uri filePath;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    String imageUri = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_product, container, false);
        define();

        return view;
    }


    public void define() {
        iv_productImage = view.findViewById(R.id.iv_productImage);
        et_productBarcodeNo = view.findViewById(R.id.et_productBarcodeNo);
        et_productBarcodeNo.setEnabled(false);
        et_productName = view.findViewById(R.id.et_productName);
        et_productUnit = view.findViewById(R.id.et_productUnit);
        et_productCategory = view.findViewById(R.id.et_productCategory);
        et_productQuantity = view.findViewById(R.id.et_productQuantity);
        et_productSalePrice = view.findViewById(R.id.et_productSalePrice);
        et_productComePrice = view.findViewById(R.id.et_productComePrice);
        btn_productSave = view.findViewById(R.id.btn_productSave);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseReference = firebaseDatabase.getReference();
        changeFragment = new ChangeFragment(getContext());
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        et_productBarcodeNo.setText(getArguments().getString("barcodeNo"));
        et_productName.setText(getArguments().getString("name"));
        et_productUnit.setText(getArguments().getString("unit"));
        et_productCategory.setText(getArguments().getString("category"));
        et_productQuantity.setText(getArguments().getString("quantity"));
        et_productSalePrice.setText(getArguments().getString("salePrice"));
        et_productComePrice.setText(getArguments().getString("comePrice"));

        imageUri = getArguments().getString("imageUri");

        if (imageUri != null) {

            iv_productImage.setBackgroundResource(android.R.color.transparent);

        }
        Picasso.get().load(imageUri).into(iv_productImage);

        iv_productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalery();
            }
        });


        btn_productSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatateProduct();
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
        }
    }


    public void updatateProduct() {

        String productBarcodeNo = et_productBarcodeNo.getText().toString();
        String productName = et_productName.getText().toString();
        String productUnit = et_productUnit.getText().toString();
        String productCategory = et_productCategory.getText().toString();
        String productQuantity = et_productQuantity.getText().toString();
        String productSalePrice = et_productSalePrice.getText().toString();
        String productComePrice = et_productComePrice.getText().toString();


        if (productBarcodeNo.equals("") || productName.equals("") || productUnit.equals("") || productCategory.equals("") ||
                productQuantity.equals("") || productSalePrice.equals("") || productComePrice.equals("")) {
            Toast.makeText(getContext(), "Lütfen boş alanları doldurunuz", Toast.LENGTH_LONG).show();
        } else {
            updateDatabase(productBarcodeNo, productName, productUnit, productCategory, productQuantity, productSalePrice, productComePrice);
        }

    }


    public void updateDatabase(final String productBarcodeNo, final String productName, final String productUnit, final String productCategory, final String productQuantity, final String productSalePrice, final String productComePrice) {

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
        dlgAlert.setMessage("Ürün güncellensin mi?\n" +
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


                if (filePath != null) {
                    final StorageReference reference = storageReference.child("productImages").child(productBarcodeNo + ".jpg");
                    reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUri = uri.toString();

                                    Products sentProduct = new Products(productBarcodeNo, productName, productUnit, productCategory, productQuantity, productSalePrice, productComePrice, imageUri);
                                    firebaseReference.child("products").child(auth.getUid()).child(productBarcodeNo).setValue(sentProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (getArguments().getString("senderFragment").equals("addProduct")) {
                                                changeFragment.change(new AddProduct());

                                            } else {
                                                changeFragment.change(new EditViewProduct());
                                            }

                                            //   Toast.makeText(getContext(), "Ürün başarılı bir şekilde eklendi.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {
                    Products sentProduct = new Products(productBarcodeNo, productName, productUnit, productCategory, productQuantity, productSalePrice, productComePrice, imageUri);
                    firebaseReference.child("products").child(auth.getUid()).child(productBarcodeNo).setValue(sentProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            et_productBarcodeNo.setEnabled(true);
                            if (getArguments().getString("senderFragment").equals("addProduct")) {
                                changeFragment.change(new AddProduct());

                            } else {
                                changeFragment.change(new EditViewProduct());
                            }
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
















