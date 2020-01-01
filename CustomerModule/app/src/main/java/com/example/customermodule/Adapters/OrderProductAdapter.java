package com.example.customermodule.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder> {


    List<Products> productsList;
    Activity activity;
    Context context;
    TextView tv_totalPrice;

    public OrderProductAdapter(List<Products> productsList, Activity activity, Context context,TextView tv_totalPrice) {

        this.productsList = productsList;
        this.activity = activity;
        this.context = context;
        this.tv_totalPrice=tv_totalPrice;
    }

    @Override
    public OrderProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orderproductslayout, parent, false);


        return new OrderProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductAdapter.ViewHolder holder, int position) {

        String productName=productsList.get(position).getProductName();
        String barcodeNo=productsList.get(position).getProductBarcodeNo();
        int orderQuantity=productsList.get(position).getOrderQuantity();
        int price=Integer.parseInt(productsList.get(position).getProductSalePrice());


        holder.tv_productName.setText(productName);
        holder.tv_productBarcodeNo.setText(barcodeNo);
        holder.tv_productQuantity.setText("Miktar : " + orderQuantity);
        holder.tv_productPrice.setText("Toplam fiyat : " + orderQuantity*price + " TL");
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
        TextView tv_productQuantity;
        TextView tv_productPrice;
        LinearLayout linearLayout_orderproduct;


        ViewHolder(final View itemView) {

            super(itemView);

            iv_productImage = itemView.findViewById(R.id.iv_productImage);
            tv_productName = itemView.findViewById(R.id.tv_productName);
            tv_productBarcodeNo = itemView.findViewById(R.id.tv_productBarcodeNo);
            tv_productQuantity = itemView.findViewById(R.id.tv_productQuantity);
            tv_productPrice = itemView.findViewById(R.id.tv_productPrice);
            linearLayout_orderproduct = itemView.findViewById(R.id.linearLayout_orderproduct);

            linearLayout_orderproduct.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {


                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);

                    final EditText input = new EditText(context);
                    input.setText(productsList.get(getAdapterPosition()).getOrderQuantity()+"");
                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                    dlgAlert.setView(input);
                    dlgAlert.setMessage("Miktar Güncelle");
                    dlgAlert.setTitle("Kontrol");

                    dlgAlert.setPositiveButton("İptal",null);
                    dlgAlert.setNeutralButton("Sil", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            OrderList.orderList.remove(productsList.get(getAdapterPosition()));
                            notifyDataSetChanged();
                            int totalPrice= 0;
                            for (Products product : OrderList.orderList) {
                                int rowPrice = 0;
                                rowPrice = product.getOrderQuantity() * Integer.parseInt(product.getProductSalePrice());
                                totalPrice += rowPrice;
                            }
                            tv_totalPrice.setText("Toplam fiyat : " + totalPrice + " TL");
                        }
                    });
                    dlgAlert.setNegativeButton("Miktar Değiştir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            int orderQuantity=Integer.parseInt(input.getText().toString());
                            Products sendProduct=productsList.get(getAdapterPosition());
                            sendProduct.setOrderQuantity(orderQuantity);
                            OrderList.orderList.get(getAdapterPosition()).setOrderQuantity(orderQuantity);

                            notifyDataSetChanged();
                            int totalPrice= 0;
                            for (Products product : OrderList.orderList) {
                                int rowPrice = 0;
                                rowPrice = product.getOrderQuantity() * Integer.parseInt(product.getProductSalePrice());
                                totalPrice += rowPrice;
                            }
                            tv_totalPrice.setText("Toplam fiyat : " + totalPrice + " TL");


                        }
                    });dlgAlert.create().show();

                }
            });


        }

    }


}
