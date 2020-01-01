package com.example.marketsmodule.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marketsmodule.R;
import com.example.marketsmodule.fragments.EditViewProduct;
import com.example.marketsmodule.fragments.UpdateProduct;
import com.example.marketsmodule.models.Products;
import com.example.marketsmodule.utils.ChangeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {


    List<Products> productsList;
    Activity activity;
    Context context;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;
    ChangeFragment changeFragment;

    public ProductsAdapter(List<Products> productsList, Activity activity, Context context) {

        this.productsList = productsList;
        this.activity = activity;
        this.context = context;
        changeFragment = new ChangeFragment(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.productslayout, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_productName.setText(productsList.get(position).getProductName());
        holder.tv_productBarcodeNo.setText("Barkod no : " + productsList.get(position).getProductBarcodeNo());
        holder.tv_productCategory.setText("Kategori : " + productsList.get(position).getProductCategory());
        holder.tv_productUnit.setText("Birim : " + productsList.get(position).getProductUnit());
        holder.tv_productQuantity.setText("Miktar : " + productsList.get(position).getProductQuantity());
        holder.tv_productComePrice.setText("Alış Fiyatı : " + productsList.get(position).getProductComePrice() + " TL");
        holder.tv_productSalePrice.setText("Satış Fiyatı : " + productsList.get(position).getProductSalePrice() + " TL");
        if (productsList.get(position).getProductImageUrl() != null) {
            holder.iv_productImage.setBackgroundResource(android.R.color.transparent);
            Picasso.get().load(productsList.get(position).getProductImageUrl()).into(holder.iv_productImage);
        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView iv_productImage;
        TextView tv_productName;
        TextView tv_productBarcodeNo;
        TextView tv_productCategory;
        TextView tv_productUnit;
        TextView tv_productQuantity;
        TextView tv_productComePrice;
        TextView tv_productSalePrice;
        LinearLayout linearLayout_product;


        ViewHolder(View itemView) {

            super(itemView);

            iv_productImage = itemView.findViewById(R.id.iv_productImage);
            tv_productName = itemView.findViewById(R.id.tv_productName);
            tv_productBarcodeNo = itemView.findViewById(R.id.tv_productBarcodeNo);
            tv_productCategory = itemView.findViewById(R.id.tv_productCategory);
            tv_productUnit = itemView.findViewById(R.id.tv_productUnit);
            tv_productQuantity = itemView.findViewById(R.id.tv_productQuantity);
            tv_productComePrice = itemView.findViewById(R.id.tv_productComePrice);
            tv_productSalePrice = itemView.findViewById(R.id.tv_productSalePrice);
            linearLayout_product = itemView.findViewById(R.id.linearLayout_product);

            linearLayout_product.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    auth = FirebaseAuth.getInstance();
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseReference = firebaseDatabase.getReference();

                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                    dlgAlert.setMessage("Yapmak istediğiniz işlemi seçiniz");
                    dlgAlert.setTitle("Seçim ekranı");
                    dlgAlert.setPositiveButton("İptal", null);
                    dlgAlert.setNegativeButton("Düzenle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Bundle bundle = new Bundle();
                            bundle.putString("barcodeNo", productsList.get(getAdapterPosition()).getProductBarcodeNo());
                            bundle.putString("name", productsList.get(getAdapterPosition()).getProductName());
                            bundle.putString("category", productsList.get(getAdapterPosition()).getProductCategory());
                            bundle.putString("comePrice", productsList.get(getAdapterPosition()).getProductComePrice());
                            bundle.putString("quantity", productsList.get(getAdapterPosition()).getProductQuantity());
                            bundle.putString("salePrice", productsList.get(getAdapterPosition()).getProductSalePrice());
                            bundle.putString("unit", productsList.get(getAdapterPosition()).getProductUnit());
                            bundle.putString("imageUri", productsList.get(getAdapterPosition()).getProductImageUrl());
                            bundle.putString("senderFragment", "adapter");
                            changeFragment.changeWithParameter(new UpdateProduct(), bundle);


                        }
                    });
                    dlgAlert.setNeutralButton("Sil", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                            dlgAlert.setMessage(tv_productBarcodeNo.getText().toString() + "\nÜrünü silmek üzeresiniz");
                            dlgAlert.setTitle("Kontrol!");
                            dlgAlert.setPositiveButton("İptal", null);
                            dlgAlert.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String barcodeNo = productsList.get(getAdapterPosition()).getProductBarcodeNo();
                                    firebaseReference.child("products").child(auth.getUid()).child(barcodeNo).removeValue();
                                    Toast.makeText(context, "Ürün silindi", Toast.LENGTH_LONG).show();
                                    changeFragment.change(new EditViewProduct());
                                }
                            });
                            dlgAlert.create().show();


                        }
                    });
                    dlgAlert.create().show();


                }
            });


        }

    }


}
