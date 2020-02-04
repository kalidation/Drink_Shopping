package com.example.drinkshop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drinkshop.Adapters.DrinkAdapter;
import com.example.drinkshop.Adapters.ListAdapterDrink;
import com.example.drinkshop.Model.Category;
import com.example.drinkshop.Model.Drink;
import com.example.drinkshop.Model.DrinkResponse;
import com.example.drinkshop.R;
import com.example.drinkshop.Retrofit.RetrofitClient;
import com.example.drinkshop.Storage.SharedPreferences.SharedPrefManager;
import com.example.drinkshop.Utils.Common;
import com.example.drinkshop.Utils.DrinkDiffCallBack;
import com.example.drinkshop.Utils.OnRefreshViewListner;
import com.example.drinkshop.Utils.SpaceItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DrinkActivity extends AppCompatActivity implements OnRefreshViewListner {

    public RecyclerView recyclerView;

    public TextView textViewCategoryName;
    public ImageView imageViewCategory;
    public ImageView cartIcon;
    public ListAdapterDrink adapterDrink;

    public int globalID;

    public CompositeDisposable compositeDisposable = new CompositeDisposable();

    public NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);


        //Toolbar toolbar = findViewById(R.id.toolbardrink);
        //setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.RecyclerView_drink);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        recyclerView.setHasFixedSize(true);

        adapterDrink = new ListAdapterDrink(new DrinkDiffCallBack()  , DrinkActivity.this);
        recyclerView.setAdapter(adapterDrink);

        textViewCategoryName = findViewById(R.id.text_select_category);
        imageViewCategory = findViewById(R.id.image_select_category);

        Category category = SharedPrefManager.getInstance(this).getCategory();
        setCategoryInfo(category);
        globalID = category.getID();
        getDrinkList(category.getID());
        getMenuEveryChange();
    }

    private void getDrinkList(int id) {
       /* compositeDisposable.add(RetrofitClient.getInstance().getApi().getDrink(String.valueOf(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DrinkResponse>() {
                    @Override
                    public void accept(DrinkResponse drinkResponse) throws Exception {
                        displayDrinks(drinkResponse.getDrinks());
                    }
                })); */

        RetrofitClient.getInstance().getApi().getDrink((String.valueOf(id)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DrinkResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(DrinkResponse drinkResponse) {
                        displayDrinks(drinkResponse.getDrinks());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(DrinkActivity.this, "There is an error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void displayDrinks(List<Drink> drinks) {
        adapterDrink.submitList(drinks);
    }

    private void getMenuEveryChange() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Drink");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getDrinkList(globalID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setCategoryInfo(Category category) {
        textViewCategoryName.setText(category.getName());
    }

    private void updateCartCount() {

        if (badge == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Common.cartRepository.countCartItems() == 0) {
                    badge.setVisibility(View.INVISIBLE);
                } else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRepository.countCartItems()));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart_menu:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        View view = menu.findItem(R.id.cart_menu).getActionView();
        badge = view.findViewById(R.id.badge);
        cartIcon = view.findViewById(R.id.cart_icon);
        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DrinkActivity.this, CartActivity.class));
            }
        });

        updateCartCount();
        return true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateCartCount();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateCartCount();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateCartCount();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DrinkActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void refreshView() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}
