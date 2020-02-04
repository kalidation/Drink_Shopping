package com.example.drinkshop.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.drinkshop.Adapters.FavoriteAdapter;
import com.example.drinkshop.R;
import com.example.drinkshop.Storage.RoomDataBase.Model.Favorite;
import com.example.drinkshop.Utils.Common;
import com.example.drinkshop.Utils.RecyclerItemTouchHelper;
import com.example.drinkshop.Utils.RecyclerItemTouchHelperListner;
import com.example.drinkshop.Utils.SpaceItemDecoration;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavoriteActivity extends AppCompatActivity implements RecyclerItemTouchHelperListner {

    RelativeLayout rootLayout;

    RecyclerView recyclerView ;
    CompositeDisposable compositeDisposable;

    FavoriteAdapter adapter;

    Button button;

    List<Favorite> localFavorites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        rootLayout = findViewById(R.id.rootlayout);

        compositeDisposable = new CompositeDisposable();
        recyclerView = findViewById(R.id.RecyclerView_favorite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this ,FavoriteActivity.this );
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        getFavorite();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getFavorite();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void getFavorite() {
        compositeDisposable.add(
                Common.favoriteRepositry.getFavoriteItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Favorite>>() {
                    @Override
                    public void accept(List<Favorite> favorites) throws Exception {
                        displayfavorite(favorites);
                    }
                })
        );

    }

    private void displayfavorite(List<Favorite> favorites) {
        localFavorites = favorites;
        adapter = new FavoriteAdapter(this,favorites);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof FavoriteAdapter.MyViewHolder){
            switch (direction){
                case ItemTouchHelper.LEFT:
                    String name = localFavorites.get(viewHolder.getAdapterPosition()).name;

                    final Favorite deleteitem = localFavorites.get(viewHolder.getAdapterPosition());
                    final int deleteIndx = viewHolder.getAdapterPosition();

                    //Delete From adapter
                    adapter.removeItem(deleteIndx);
                    //Delete  From ROOMDATABASE
                    Common.favoriteRepositry.delete(deleteitem);

                    Snackbar snackbar = Snackbar.make(recyclerView,name+" Removed From Favorites ",Snackbar.LENGTH_LONG);
                    snackbar.setAction("Cancel Delete", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.restoreItem(deleteitem,deleteIndx);
                            Common.favoriteRepositry.insertToCart(deleteitem);
                        }
                    });
                    snackbar.setActionTextColor(Color.WHITE);
                    snackbar.show();
                    break;
            }
        }
    }
}
