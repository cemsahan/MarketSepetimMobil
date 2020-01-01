package com.example.customermodule.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.customermodule.Models.OrderList;
import com.example.customermodule.Models.Products;
import com.example.customermodule.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {


    List<Products> productsList;
    Activity activity;
    Context context;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference firebaseReference;

    public ProductsAdapter(List<Products> productsList, Activity activity, Context context) {

        this.productsList = productsList;
        this.activity = activity;
        this.context = context;

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
        holder.tv_productPrice.setText("Fiyatı : " + productsList.get(position).getProductSalePrice() + " TL");
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
        TextView tv_productPrice;
        LinearLayout linearLayout_product;


        ViewHolder(final View itemView) {

            super(itemView);

            iv_productImage = itemView.findViewById(R.id.iv_productImage);
            tv_productName = itemView.findViewById(R.id.tv_productName);
            tv_productBarcodeNo = itemView.findViewById(R.id.tv_productBarcodeNo);
            tv_productCategory = itemView.findViewById(R.id.tv_productCategory);
            tv_productUnit = itemView.findViewById(R.id.tv_productUnit);
            tv_productQuantity = itemView.findViewById(R.id.tv_productQuantity);
            tv_productPrice = itemView.findViewById(R.id.tv_productPrice);
            linearLayout_product = itemView.findViewById(R.id.linearLayout_product);

            linearLayout_product.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    auth = FirebaseAuth.getInstance();
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseReference = firebaseDatabase.getReference();

                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);

                    final EditText input = new EditText(context);
                    input.setText("1");
                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                    dlgAlert.setView(input);
                    dlgAlert.setMessage("Kaç tane alacaksınız ?");
                    dlgAlert.setTitle("");
                    dlgAlert.setPositiveButton("İptal", null);
                    dlgAlert.setNegativeButton("Sepete ekle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int orderQuantity=Integer.parseInt(input.getText().toString());
                            int productQuantity=Integer.parseInt(productsList.get(getAdapterPosition()).getProductQuantity());
                            if(orderQuantity<=productQuantity) {
                                Products sendProduct = productsList.get(getAdapterPosition());
                                sendProduct.setOrderQuantity(orderQuantity);
                                OrderList.orderList.add(sendProduct);
                            }else{

                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                                dlgAlert.setMessage("Marketimizde bu kadar ürün yok");
                                dlgAlert.setTitle("");
                                dlgAlert.setPositiveButton("Tamam", null);
                                dlgAlert.create().show();

                            }
                        }
                    });dlgAlert.create().show();


                }
            });


        }

    }


}
