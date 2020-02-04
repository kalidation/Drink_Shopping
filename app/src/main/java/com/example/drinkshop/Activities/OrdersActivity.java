package com.example.drinkshop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.drinkshop.Adapters.ListAdapterDrink;
import com.example.drinkshop.Adapters.ListAdapterOrders;
import com.example.drinkshop.Model.Drink;
import com.example.drinkshop.Model.DrinkResponse;
import com.example.drinkshop.Model.Order;
import com.example.drinkshop.Model.OrdersResponse;
import com.example.drinkshop.R;
import com.example.drinkshop.Retrofit.RetrofitClient;
import com.example.drinkshop.Storage.SharedPreferences.SharedPrefManager;
import com.example.drinkshop.Utils.DrinkDiffCallBack;
import com.example.drinkshop.Utils.OrderDiffCallBack;
import com.example.drinkshop.Utils.SpaceItemDecoration;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CompositeDisposable compositeDisposable;
    private ListAdapterOrders adapterOrders;

    private BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        compositeDisposable = new CompositeDisposable();

        recyclerView = findViewById(R.id.recyclerView_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        recyclerView.setHasFixedSize(true);

        adapterOrders = new ListAdapterOrders(new OrderDiffCallBack(), OrdersActivity.this);
        recyclerView.setAdapter(adapterOrders);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.new_status:
                        getOrders("0");
                        break;
                    case R.id.cancel_status:
                        getOrders("-1");
                        break;
                    case R.id.processing_status:
                        getOrders("1");
                        break;
                    case R.id.shipping_status:
                        getOrders("2");
                        break;
                    case R.id.shipped_status:
                        getOrders("3");
                        break;
                }
                return true;
            }
        });

        getOrders("0");

    }

    private void getOrders(String status) {

        RetrofitClient.getInstance().getApi().getOrders(SharedPrefManager.getInstance(OrdersActivity.this).getUser().getPhone(), status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrdersResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(OrdersResponse ordersResponse) {
                        displayDrinks(ordersResponse.getOrders());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void displayDrinks(List<Order> orders) {
        Log.d("displayDrinks", "displayDrinks: " + orders.size());
        adapterOrders.submitList(orders);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel_order :
                RetrofitClient.getInstance().getApi().cancelOrder(SharedPrefManager.getInstance(OrdersActivity.this).getUser().getPhone(),String.valueOf(item.getOrder()))
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(OrdersActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                getOrders("0");
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("onFailure: ", "onFailure: "+t.getMessage());
                            }
                        });
                break;
        }
        return super.onContextItemSelected(item);
    }
}
