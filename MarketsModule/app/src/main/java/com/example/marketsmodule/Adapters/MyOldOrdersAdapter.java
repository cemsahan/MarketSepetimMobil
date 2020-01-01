package com.example.marketsmodule.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.marketsmodule.R;
import com.example.marketsmodule.models.Customer;
import com.example.marketsmodule.models.Order;
import com.example.marketsmodule.models.Products;

import java.util.List;

public class MyOldOrdersAdapter extends RecyclerView.Adapter<MyOldOrdersAdapter.ViewHolder> {


    List<Order> orderList;
    Activity activity;
    Context context;

    public MyOldOrdersAdapter(List<Order> orderList, Activity activity, Context context) {

        this.orderList = orderList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public MyOldOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.oldorderlayout, parent, false);


        return new MyOldOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOldOrdersAdapter.ViewHolder holder, int position) {

        Customer customer = orderList.get(position).getCustomerInfo();
        String customerName = customer.getUserName() + " " + customer.getUserSurname();
        String customerNo = customer.getUserPhoneNo();


        String customerInfo = "Adı Soyadı : " + customerName + "\n" +
                "Telefon No : " + customerNo;
        if (!orderList.get(position).getStatement().equals("")) customerInfo+="\nSipariş Açıklaması : " + orderList.get(position).getStatement() ;


        holder.tv_CustomerInfo.setText(customerInfo);

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
        holder.tv_oldTotalPrice.setText("Toplam Fiyat : " + totalPrice + " TL");
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
        TextView tv_CustomerInfo;


        ViewHolder(final View itemView) {

            super(itemView);
            tv_oldDate = itemView.findViewById(R.id.tv_oldDate);
            tv_oldOrders = itemView.findViewById(R.id.tv_oldOrders);
            tv_oldTotalPrice = itemView.findViewById(R.id.tv_oldTotalPrice);
            tv_CustomerInfo = itemView.findViewById(R.id.tv_CustomerInfo);


        }

    }


}
