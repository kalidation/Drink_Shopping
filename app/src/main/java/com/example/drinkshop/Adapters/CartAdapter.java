package com.example.drinkshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.drinkshop.R;
import com.example.drinkshop.Storage.RoomDataBase.Model.Cart;
import com.example.drinkshop.Storage.RoomDataBase.Model.Favorite;
import com.example.drinkshop.Utils.Common;
import com.example.drinkshop.Utils.onClickInterface;

import java.util.List;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private Context context;
    private List<Cart> cartlist;
    private com.example.drinkshop.Utils.onClickInterface onClickInterface;

    public CartAdapter(Context context, List<Cart> cartlist , onClickInterface onClickInterface) {
        this.context = context;
        this.cartlist = cartlist;
        this.onClickInterface = onClickInterface;
    }

    public CartAdapter() {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder (LayoutInflater.from(context).inflate(R.layout.cart_layout_item,  parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.number.setNumber(String.valueOf(cartlist.get(position).amount));
        Glide.with(context).load(cartlist.get(position).link).into(holder.imagecartproduct);
        holder.textViewName.setText(cartlist.get(position).name+" * "+cartlist.get(position).amount);
        holder.textViewSugar.setText("Sugar: "+cartlist.get(position).sugar+"%");
        holder.textViewIce.setText("Ice: "+cartlist.get(position).ice+"%");
        holder.textViewSize.setText(cartlist.get(position).size == 0 ? "Size M" : "Size L");
        holder.textViewPrice.setText("$"+cartlist.get(position).price);

        final double priceOneCup = cartlist.get(position).price / cartlist.get(position).amount;

        holder.number.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart= cartlist.get(position);
                cart.amount = newValue;
                cart.price = priceOneCup * newValue ;
                Common.cartRepository.updateCart(cart);
            }
        });

        holder.textViewPrice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickInterface.setClick(position , holder);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagecartproduct ;
        TextView textViewName , textViewSize , textViewSugar , textViewIce , textViewPrice ;
        ElegantNumberButton number;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagecartproduct = itemView.findViewById(R.id.image_cart_product);
            textViewName = itemView.findViewById(R.id.txt_cart_name);
            textViewSugar = itemView.findViewById(R.id.txt_cart_sugar);
            textViewIce = itemView.findViewById(R.id.txt_cart_ice);
            textViewSize = itemView.findViewById(R.id.txt_cart_size);
            textViewPrice = itemView.findViewById(R.id.txt_cart_price);

            number = itemView.findViewById(R.id.number_counter_cart);

        }
    }

    public void removeItem(int position){
        cartlist.remove(position);
        //notifyItemRemoved(position);
    }

    public void restoreItem(Cart item , int position){
        cartlist.add(position,item);
        //notifyItemInserted(position);
    }
}
