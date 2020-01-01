package com.example.marketsmodule.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marketsmodule.R;
import com.example.marketsmodule.models.Products;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {


    Activity activity;
    Context context;
    List<Products> productsList;

    public OrderDetailsAdapter(List<Products> productsList, Activity activity, Context context) {

        this.activity = activity;
        this.context = context;
        this.productsList=productsList;
    }

    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orderdetaillayout, parent, false);


        return new OrderDetailsAdapter.ViewHolder(view);
    }


    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {

        String productName=productsList.get(position).getProductName();
        String barcodeNo=productsList.get(position).getProductBarcodeNo();
        int orderQuantity=productsList.get(position).getOrderQuantity();
        int price=Integer.parseInt(productsList.get(position).getProductSalePrice());


        holder.tv_productName.setText(productName);
        holder.tv_productBarcodeNo.setText(barcodeNo);
        holder.tv_productQuantity.setText("Miktar : " + orderQuantity);
        holder.tv_productPrice.setText("Toplam fiyat : " + orderQuantity*price + " TL");

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_productBarcodeNo;
        TextView tv_productName;
        TextView tv_productQuantity;
        TextView tv_productPrice;


        ViewHolder(final View itemView) {

            super(itemView);
            tv_productBarcodeNo = itemView.findViewById(R.id.tv_productBarcodeNo);
            tv_productName = itemView.findViewById(R.id.tv_productName);
            tv_productQuantity = itemView.findViewById(R.id.tv_productQuantity);
            tv_productPrice = itemView.findViewById(R.id.tv_productPrice);



                }
            }




    }



