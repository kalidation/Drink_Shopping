package com.example.drinkshop.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drinkshop.R;
import com.example.drinkshop.Storage.RoomDataBase.Model.Cart;

public class ListAdapterOrderDetail extends ListAdapter<Cart,ListAdapterOrderDetail.MyViewHolder > {

        private Context context;

    protected ListAdapterOrderDetail(@NonNull DiffUtil.ItemCallback<Cart> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ListAdapterOrderDetail.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_order_layout_item , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(getItem(position));
        Log.i("carts", "onBindViewHolder: "+getItem(position).name);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imagecartproduct ;
        TextView textViewName , textViewSize , textViewSugar , textViewIce , textViewPrice ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagecartproduct = itemView.findViewById(R.id.image_cart_product);
            textViewName = itemView.findViewById(R.id.txt_cart_name);
            textViewSugar = itemView.findViewById(R.id.txt_cart_sugar);
            textViewIce = itemView.findViewById(R.id.txt_cart_ice);
            textViewSize = itemView.findViewById(R.id.txt_cart_size);
            textViewPrice = itemView.findViewById(R.id.txt_cart_price);
        }

        void bind(Cart cart){
            textViewName.setText(cart.name +" * : "+cart.amount );
            textViewSugar.setText("Sugar: "+String.valueOf(cart.sugar));
            textViewIce.setText("Ice: "+String.valueOf(cart.ice));
            textViewSize.setText("Size: "+String.valueOf(cart.size));
            textViewPrice.setText("$"+String.valueOf(String.valueOf(cart.price)));
            Glide.with(context).load(cart.link).into(imagecartproduct);
        }
    }
}
