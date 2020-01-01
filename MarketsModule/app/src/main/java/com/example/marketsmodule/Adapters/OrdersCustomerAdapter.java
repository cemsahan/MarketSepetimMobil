package com.example.marketsmodule.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.marketsmodule.Activities.MainActivity;
import com.example.marketsmodule.Activities.OrderDetailsActivity;
import com.example.marketsmodule.R;
import com.example.marketsmodule.models.Customer;
import com.example.marketsmodule.models.Order;
import com.example.marketsmodule.models.OrderContent;
import com.example.marketsmodule.models.Products;
import com.example.marketsmodule.utils.ChangeFragment;

import java.util.List;

public class OrdersCustomerAdapter extends RecyclerView.Adapter<OrdersCustomerAdapter.ViewHolder> {


    List<Order> orderList;
    Activity activity;
    Context context;

    public OrdersCustomerAdapter(List<Order> orderList, Activity activity, Context context) {

        this.orderList = orderList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public OrdersCustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customerlayout, parent, false);


        return new OrdersCustomerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersCustomerAdapter.ViewHolder holder, int position) {
        String currentName=orderList.get(position).getCustomerInfo().getUserName()+" "+orderList.get(position).getCustomerInfo().getUserSurname();
        String currentPhoneNo=orderList.get(position).getCustomerInfo().getUserPhoneNo();
        String statement=orderList.get(position).getStatement();
        String date=orderList.get(position).getDate();

        holder.tv_customerName.setText(currentName);
        holder.tv_customerNo.setText(currentPhoneNo);
        holder.tv_statement.setText(statement);
        holder.tv_date.setText(date);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_customerName;
        TextView tv_customerNo;
        LinearLayout linearLayout_orderCustomer;
        TextView tv_date;
        TextView tv_statement;

        ViewHolder(final View itemView) {

            super(itemView);
            tv_customerName=itemView.findViewById(R.id.tv_customerName);
            tv_customerNo=itemView.findViewById(R.id.tv_customerNo);
            linearLayout_orderCustomer = itemView.findViewById(R.id.linearLayout_orderCustomer);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_statement = itemView.findViewById(R.id.tv_statement);

            linearLayout_orderCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderContent.order=orderList.get(getAdapterPosition());
                    Intent intent=new Intent(context,OrderDetailsActivity.class);
                    context.startActivity(intent);

                }
            });



        }

    }


}
