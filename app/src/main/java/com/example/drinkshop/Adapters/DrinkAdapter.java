package com.example.drinkshop.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.drinkshop.Model.Drink;
import com.example.drinkshop.R;
import com.example.drinkshop.Storage.RoomDataBase.Model.Cart;
import com.example.drinkshop.Storage.RoomDataBase.Model.Favorite;
import com.example.drinkshop.Utils.Common;
import com.example.drinkshop.Utils.OnRefreshViewListner;
import com.google.gson.Gson;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.MyViewHolder> {

    private Context context;
    private List<Drink> drinks;

    private OnRefreshViewListner mRefreshListner;

    public DrinkAdapter(Context context, List<Drink> drinks) {
        this.context = context;
        this.drinks = drinks;
        this.mRefreshListner = (OnRefreshViewListner)context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.drink_layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.textViewName.setText(drinks.get(position).getName());
        holder.textViewPrice.setText(drinks.get(position).getPrice());

        Glide.with(context).load(drinks.get(position).getLink()).into(holder.imageViewDrink);

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddtoCartDialog(position);
            }
        });

        if(Common.favoriteRepositry.isFavorite(Integer.parseInt( drinks.get(position).getID()))  != 0){
            holder.btn_add_to_favorite.setImageResource(R.drawable.ic_favorite);
        }else{
            holder.btn_add_to_favorite.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.btn_add_to_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.favoriteRepositry.isFavorite(Integer.parseInt( drinks.get(position).getID()))  == 0){
                    add_Or_Remove_Favorite(drinks.get(position) , true);
                    holder.btn_add_to_favorite.setImageResource(R.drawable.ic_favorite);
                }else{
                    add_Or_Remove_Favorite(drinks.get(position) , false );
                    holder.btn_add_to_favorite.setImageResource(R.drawable.ic_favorite_border);
                }
            }
        });

    }

    private void add_Or_Remove_Favorite(Drink drink, boolean b) {

        Drink drinka = new Drink(
           drink.getID(),
           drink.getName(),
                drink.getLink(),
                drink.getPrice(),
                drink.getMenuID()
        ) ;

        Favorite favorite = new Favorite();
        favorite.id = Integer.parseInt(drink.getID());
        favorite.name = drink.getName();
        favorite.link = drink.getLink();
        favorite.price = drink.getPrice();
        favorite.menuId = drink.getMenuID();

        if(b){
            Common.favoriteRepositry.insertToCart(favorite);
            Toast.makeText(context, "Added To Favorite", Toast.LENGTH_SHORT).show();
        }else{
            Common.favoriteRepositry.delete(favorite);
            Toast.makeText(context, "Removed From Favorite", Toast.LENGTH_SHORT).show();
        }
    }


    private void AddtoCartDialog(final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_cart_layout, null);

        final TextView test = view.findViewById(R.id.test);

        ImageView imageViewCartProduct = view.findViewById(R.id.img_cart_product);
        TextView textViewPrductName = view.findViewById(R.id.txt_cart_product_name);
        final ElegantNumberButton count = view.findViewById(R.id.number_btn);
        EditText editTextComment = view.findViewById(R.id.edit_text_cart_comment);

        RadioButton radioButton_sizeM = view.findViewById(R.id.radio_btn_sizeM);
        RadioButton radioButton_sizeL = view.findViewById(R.id.radio_btn_sizeL);

        radioButton_sizeM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.size = 0;
                }
            }
        });

        radioButton_sizeL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.size = 1;
                }
            }
        });

        RadioButton radioButton_suger100 = view.findViewById(R.id.radio_btn_100);
        RadioButton radioButton_suger70 = view.findViewById(R.id.radio_btn_70);
        RadioButton radioButton_suger50 = view.findViewById(R.id.radio_btn_50);
        RadioButton radioButton_suger30 = view.findViewById(R.id.radio_btn_30);
        RadioButton radioButton_suger0 = view.findViewById(R.id.radio_btn_free);

        radioButton_suger100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.sugar = 100;
                }
            }
        });

        radioButton_suger70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.sugar = 70;
                }
            }
        });

        radioButton_suger50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.sugar = 50;
                }
            }
        });

        radioButton_suger30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.sugar = 30;
                }
            }
        });

        radioButton_suger0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.sugar = 0;
                }
            }
        });

        RadioButton radioButton_ice100 = view.findViewById(R.id.radio_btn_ice_100);
        RadioButton radioButton_ice70 = view.findViewById(R.id.radio_btn_ice_70);
        RadioButton radioButton_ice50 = view.findViewById(R.id.radio_btn_ice_50);
        RadioButton radioButton_ice30 = view.findViewById(R.id.radio_btn_ice_30);
        final RadioButton radioButton_ice0 = view.findViewById(R.id.radio_btn_ice_free);


        radioButton_ice100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.ice = 100;
                }
            }
        });

        radioButton_ice70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.ice = 70;
                }
            }
        });

        radioButton_ice50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.ice = 50;
                }
            }
        });

        radioButton_ice30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.ice = 30;
                }
            }
        });

        radioButton_ice0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.ice = 0;
                }
            }
        });

        RecyclerView recyclerTopping = view.findViewById(R.id.RecyclerView_Topping);
        recyclerTopping.setLayoutManager(new LinearLayoutManager(context));
        recyclerTopping.setHasFixedSize(true);

        MultiChoiceAdapter multiChoiceAdapter = new MultiChoiceAdapter(context, Common.toppingList);
        recyclerTopping.setAdapter(multiChoiceAdapter);


        Glide.with(context).load(drinks.get(position).getLink()).into(imageViewCartProduct);
        textViewPrductName.setText(drinks.get(position).getName());

        builder.setView(view);
        builder.setNegativeButton("ADD TO CART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Common.size != -1) {
                    if (Common.sugar != -1) {
                        if (Common.ice != -1) {
                            showConfirmDialog(position, count.getNumber(), Common.size, Common.sugar, Common.ice);
                            Common.toppingNames.clear();
                            Common.sugar = -1;
                            Common.ice = -1;
                            Common.size = -1;
                            //dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Ice Requierd", Toast.LENGTH_SHORT).show();
                            Common.size = -1;
                            Common.sugar = -1;
                        }
                    } else {
                        Toast.makeText(context, "sugar Requierd", Toast.LENGTH_SHORT).show();
                        Common.ice = -1;
                        Common.size = -1;
                    }
                } else {
                    Toast.makeText(context, "size Requierd", Toast.LENGTH_SHORT).show();
                    Common.sugar = -1;
                    Common.ice = -1;
                }

               /* if (Common.sugar == -1) {
                    Toast.makeText(context, "Choose sugar please", Toast.LENGTH_LONG).show();
                    //return;
                }

                if (Common.ice == -1) {
                    Toast.makeText(context, "Choose ice please", Toast.LENGTH_LONG).show();
                    //return;
                }

                showConfirmDialog(position, count.getNumber(), Common.size, Common.sugar, Common.ice ,dialog);
                dialog.dismiss(); */
            }
        });

        builder.show();
    }

    private void  showConfirmDialog(final int position, final String countNumber, final int size, final int sugar, final int ice) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.confirm_add_to_cart_layout, null);

        ImageView imageViewCartProduct = view.findViewById(R.id.image_confirm_cart_product);
        TextView textViewPrductName = view.findViewById(R.id.txt_cart_product_name);
        TextView textViewPrductPrice = view.findViewById(R.id.txt_cart_product_price);
        TextView textViewProductSize = view.findViewById(R.id.txt_cart_product_size);
        TextView textViewPrductSugar = view.findViewById(R.id.txt_cart_product_sugar);
        TextView textViewPrductIce = view.findViewById(R.id.txt_cart_product_ice);
        final TextView textViewPrductExtras = view.findViewById(R.id.txt_list_topping);

        Glide.with(context).load(drinks.get(position).getLink()).into(imageViewCartProduct);
        textViewPrductName.setText(drinks.get(position).getName() + " * " + countNumber);

        textViewPrductSugar.setText("Sugar : " + sugar + "%");
        textViewPrductIce.setText("ice : " + ice + "%");
        textViewProductSize.setText(size == 0 ? "Size M" : "Size L");

        /*
        * Todo: PRICE
         */

        double price = (Double.parseDouble(drinks.get(position).getPrice()) * Double.parseDouble(countNumber) +
                Common.toppingPrice);

        if (Common.size == 1) {
            price += (3.0 * Double.valueOf(countNumber));
        }


        textViewPrductExtras.setText("Your Extras: \n");

        /*
         * Todo: Topping_List
         */

        int i = 1;
        for (String list : Common.toppingNames) {
            textViewPrductExtras.append(i + "- " + list + "\n");
            i++;
        }

        /*
         * Todo: Confirm_Cart
         */

        final double finalPrice = Math.round(price);
        textViewPrductPrice.setText("$" + finalPrice);
        alertDialog.setNegativeButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    Cart cart = new Cart();
                    cart.name = drinks.get(position).getName();
                    cart.amount = Integer.parseInt(countNumber);
                    cart.ice = ice;
                    cart.sugar = sugar;
                    cart.size = size;
                    cart.price = finalPrice;
                    cart.toppingExtras = textViewPrductExtras.getText().toString();
                    cart.link = drinks.get(position).getLink();

                    Common.cartRepository.insertToCart(cart);
                    Log.d("KHALED DEBUG", new Gson().toJson(cart));

                    Toast.makeText(context, "Save item Success", Toast.LENGTH_SHORT).show();
                    mRefreshListner.refreshView();
                }catch (Exception ex){
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewPrice;
        ImageView imageViewDrink;
        ImageView btn_add_to_cart;
        ImageView btn_add_to_favorite;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_drink_name);
            textViewPrice = itemView.findViewById(R.id.text_drink_price);
            imageViewDrink = itemView.findViewById(R.id.image_drink);
            btn_add_to_cart = itemView.findViewById(R.id.add_to_cart);
            btn_add_to_favorite = itemView.findViewById(R.id.add_to_favorite);

        }
    }
}
