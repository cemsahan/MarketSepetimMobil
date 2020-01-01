package com.example.customermodule.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.customermodule.Models.Order;
import com.example.customermodule.Models.Products;
import com.example.customermodule.Models.Users;
import com.example.customermodule.R;

import java.util.List;

public class OldOrderAdapter extends RecyclerView.Adapter<OldOrderAdapter.ViewHolder> {


    List<Order> orderList;
    Activity activity;
    Context context;

    public OldOrderAdapter(List<Order> orderList, Activity activity, Context context) {

        this.orderList = orderList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public OldOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.oldorderlayout, parent, false);


        return new OldOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OldOrderAdapter.ViewHolder holder, int position) {

        String date = orderList.get(position).getDate();
        String sitiation = orderList.get(position).getSituation();
        if (sitiation.equals("0")) holder.tv_oldDate.setText(date + "\nBekliyor");
        else holder.tv_oldDate.setText(date + "\nTamamlandı");
        String orderString = "";
        double totalPrice = 0;
        List<Products> products = orderList.get(position).getOrders();
        for (Products products1 : products) {
            String intermediateRow = String.format("%s x %s \t \t \t = \t \t \t %s TL", products1.getOrderQuantity(), products1.getProductName(), products1.getOrderQuantity() * Double.parseDouble(products1.getProductSalePrice()));
            totalPrice += products1.getOrderQuantity() * Double.parseDouble(products1.getProductSalePrice());
            orderString += intermediateRow + "\n";

        }
        holder.tv_oldTotalPrice.setText("Toplam Fiyat : "+totalPrice+" TL");
        holder.tv_oldOrders.setText(orderString);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tv_oldDate;
        TextView tv_oldOrders;
        TextView tv_oldTotalPrice;
        LinearLayout linearLayout_oldOrderDetail;


        ViewHolder(final View itemView) {

            super(itemView);
            tv_oldDate = itemView.findViewById(R.id.tv_oldDate);
            tv_oldOrders = itemView.findViewById(R.id.tv_oldOrders);
            tv_oldTotalPrice = itemView.findViewById(R.id.tv_oldTotalPrice);
            linearLayout_oldOrderDetail = itemView.findViewById(R.id.linearLayout_oldOrderDetail);


        }

    }


}
