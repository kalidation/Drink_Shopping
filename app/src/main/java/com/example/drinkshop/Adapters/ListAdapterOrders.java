package com.example.drinkshop.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkshop.Model.Order;
import com.example.drinkshop.R;
import com.example.drinkshop.Storage.RoomDataBase.Model.Cart;
import com.example.drinkshop.Utils.CartDiffCallBack;
import com.example.drinkshop.Utils.SpaceItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ListAdapterOrders extends ListAdapter<Order, ListAdapterOrders.MyViewModel> {
    private Context context;
    private boolean isOpen;

    public ListAdapterOrders(@NonNull DiffUtil.ItemCallback<Order> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListAdapterOrders.MyViewModel(LayoutInflater.from(context).inflate(R.layout.order_layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewModel holder, final int position) {
        holder.bind(getItem(position));
        holder.Check();
        holder.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Chose An Option : ");
                menu.setHeaderIcon(R.drawable.ic_menu_manage);
                menu.add(Menu.NONE , R.id.cancel_order , Integer.valueOf(getItem(position).getOrderid()),"Delete" );
            }
        });
    }

    class MyViewModel extends RecyclerView.ViewHolder {

        private TextView textViewID, textViewComment, textViewPrice, textViewDetail, textViewStatus;
        private ImageView imageView;
        private RecyclerView recyclerView;

        MyViewModel(@NonNull View itemView) {
            super(itemView);
            textViewID = itemView.findViewById(R.id.text_view_order_id);
            textViewComment = itemView.findViewById(R.id.text_view_order_commentary);
            textViewPrice = itemView.findViewById(R.id.text_view_order_price);
            textViewDetail = itemView.findViewById(R.id.text_view_order_detail);
            textViewStatus = itemView.findViewById(R.id.text_view_order_status);
            imageView = itemView.findViewById(R.id.show_more);
            recyclerView = itemView.findViewById(R.id.recyclerView_orders_detail);

        }


        void bind(Order order) {
            textViewID.setText("#" + order.getOrderid());
            textViewPrice.setText("$" + order.getPrice());
            textViewComment.setText(order.getComment());

            recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL ,false));
            recyclerView.addItemDecoration(new SpaceItemDecoration(10));
            ListAdapterOrderDetail listAdapterOrderDetail = new ListAdapterOrderDetail(new CartDiffCallBack());
            recyclerView.setAdapter(listAdapterOrderDetail);

            List<Cart> orderDetail = new Gson().fromJson(order.getDetail(),new TypeToken<List<Cart>>(){}.getType());
            Log.i("bind", "bind: "+orderDetail.get(0).name);
            listAdapterOrderDetail.submitList(orderDetail);
        }

        void Check() {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOpen) {
                        recyclerView.setVisibility(View.GONE);
                        textViewDetail.setText("Show Details");
                        //textViewDetail.setVisibility(View.GONE);
                        imageView.setImageResource(R.drawable.ic_expand_more);
                        // imageViewClose.setVisibility(View.GONE);
                        isOpen = false;
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        textViewDetail.setText("Close Details");
                        //textViewDetail.setVisibility(View.VISIBLE);
                        //imageViewClose.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        isOpen = true;
                    }
                }
            });

            /*imageViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isOpen){
                        textViewDetail.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        isOpen = !isOpen;
                    }
                    if(isOpen){
                        textViewDetail.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        isOpen = !isOpen;
                    }
                }
            });*/
        }

        void setOnCreateContextMenuListener(View.OnCreateContextMenuListener listener) {
            itemView.setOnCreateContextMenuListener(listener);
        }

    }
}
