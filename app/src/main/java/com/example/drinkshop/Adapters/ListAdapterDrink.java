package com.example.drinkshop.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.drinkshop.Activities.DrinkActivity;
import com.example.drinkshop.Model.Category;
import com.example.drinkshop.Model.Drink;
import com.example.drinkshop.R;
import com.example.drinkshop.Storage.RoomDataBase.Model.Cart;
import com.example.drinkshop.Storage.RoomDataBase.Model.Favorite;
import com.example.drinkshop.Storage.SharedPreferences.SharedPrefManager;
import com.example.drinkshop.Utils.Common;
import com.example.drinkshop.Utils.OnRefreshViewListner;
import com.google.gson.Gson;

public class ListAdapterDrink extends ListAdapter<Drink, ListAdapterDrink.MyViewModel> {
    private Context context;
    private OnRefreshViewListner mRefreshListner;

    public ListAdapterDrink(@NonNull DiffUtil.ItemCallback diffCallback , Context context) {
        super(diffCallback);
        this.mRefreshListner = (OnRefreshViewListner)context;
    }

    @NonNull
    @Override
    public ListAdapterDrink.MyViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ListAdapterDrink.MyViewModel(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.drink_layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ListAdapterDrink.MyViewModel holder, final int position) {

        holder.bind(getItem(position));
        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.AddtoCartDialog(getItem(position));
            }
        });

        holder.btn_add_to_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.favoriteRepositry.isFavorite(Integer.parseInt(getItem(position).getID())) == 0) {
                    holder.add_Or_Remove_Favorite(getItem(position), true);
                    holder.btn_add_to_favorite.setImageResource(R.drawable.ic_favorite);
                } else {
                    holder.add_Or_Remove_Favorite(getItem(position), false);
                    holder.btn_add_to_favorite.setImageResource(R.drawable.ic_favorite_border);
                }
            }
        });
    }

    public class MyViewModel extends RecyclerView.ViewHolder {

        TextView textViewName, textViewPrice;
        ImageView imageViewDrink;
        ImageView btn_add_to_cart;
        ImageView btn_add_to_favorite;

        public MyViewModel(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_drink_name);
            textViewPrice = itemView.findViewById(R.id.text_drink_price);
            imageViewDrink = itemView.findViewById(R.id.image_drink);
            btn_add_to_cart = itemView.findViewById(R.id.add_to_cart);
            btn_add_to_favorite = itemView.findViewById(R.id.add_to_favorite);
        }

        public void bind(Drink drink) {
            textViewName.setText(drink.getName());
            textViewPrice.setText(drink.getPrice());

            Glide.with(context).load(drink.getLink()).into(imageViewDrink);

            if (Common.favoriteRepositry.isFavorite(Integer.parseInt(drink.getID())) != 0) {
                btn_add_to_favorite.setImageResource(R.drawable.ic_favorite);
            } else {
                btn_add_to_favorite.setImageResource(R.drawable.ic_favorite_border);
            }
        }

        private void add_Or_Remove_Favorite(Drink drink, boolean b) {

            Drink drinka = new Drink(
                    drink.getID(),
                    drink.getName(),
                    drink.getLink(),
                    drink.getPrice(),
                    drink.getMenuID()
            );

            Favorite favorite = new Favorite();
            favorite.id = Integer.parseInt(drink.getID());
            favorite.name = drink.getName();
            favorite.link = drink.getLink();
            favorite.price = drink.getPrice();
            favorite.menuId = drink.getMenuID();

            if (b) {
                Common.favoriteRepositry.insertToCart(favorite);
                Toast.makeText(context, "Added To Favorite", Toast.LENGTH_SHORT).show();
            } else {
                Common.favoriteRepositry.delete(favorite);
                Toast.makeText(context, "Removed From Favorite", Toast.LENGTH_SHORT).show();
            }
        }

        private void AddtoCartDialog(final Drink drink) {

            if(!Common.toppingList.isEmpty()) {

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


                Glide.with(context).load(drink.getLink()).into(imageViewCartProduct);
                textViewPrductName.setText(drink.getName());

                builder.setView(view);
                builder.setNegativeButton("ADD TO CART", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Common.size != -1) {
                            if (Common.sugar != -1) {
                                if (Common.ice != -1) {
                                    showConfirmDialog(drink, count.getNumber(), Common.size, Common.sugar, Common.ice);
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
        }

        private void  showConfirmDialog(final Drink drink, final String countNumber, final int size, final int sugar, final int ice) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.confirm_add_to_cart_layout, null);

            ImageView imageViewCartProduct = view.findViewById(R.id.image_confirm_cart_product);
            TextView textViewPrductName = view.findViewById(R.id.txt_cart_product_name);
            TextView textViewPrductPrice = view.findViewById(R.id.txt_cart_product_price);
            TextView textViewProductSize = view.findViewById(R.id.txt_cart_product_size);
            TextView textViewPrductSugar = view.findViewById(R.id.txt_cart_product_sugar);
            TextView textViewPrductIce = view.findViewById(R.id.txt_cart_product_ice);
            final TextView textViewPrductExtras = view.findViewById(R.id.txt_list_topping);

            Glide.with(context).load(drink.getLink()).into(imageViewCartProduct);
            textViewPrductName.setText(drink.getName() + " * " + countNumber);

            textViewPrductSugar.setText("Sugar : " + sugar + "%");
            textViewPrductIce.setText("ice : " + ice + "%");
            textViewProductSize.setText(size == 0 ? "Size M" : "Size L");

            /*
             * Todo: PRICE
             */

            double price = (Double.parseDouble(drink.getPrice()) * Double.parseDouble(countNumber) +
                    Common.toppingPrice);

            if (Common.size == 1) {
                price += (3.0 * Double.valueOf(countNumber));
            }


            textViewPrductExtras.setText("\n");

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
                        cart.name = drink.getName();
                        cart.amount = Integer.parseInt(countNumber);
                        cart.ice = ice;
                        cart.sugar = sugar;
                        cart.size = size;
                        cart.price = finalPrice;
                        cart.toppingExtras = textViewPrductExtras.getText().toString();
                        cart.link = drink.getLink();

                        Common.cartRepository.insertToCart(cart);
                        Log.d("KHALED DEBUG", new Gson().toJson(cart));

                        Toast.makeText(context, "Save item Success", Toast.LENGTH_SHORT).show();
                        mRefreshListner.refreshView();
                    }catch (Exception ex){
                        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("KHALED", ex.getMessage());
                    }
                }
            });
            alertDialog.setView(view);
            alertDialog.show();
        }


    }

}

