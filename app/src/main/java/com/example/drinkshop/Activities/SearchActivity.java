package com.example.drinkshop.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.example.drinkshop.Adapters.DrinkAdapter;
import com.example.drinkshop.Model.Drink;
import com.example.drinkshop.Model.DrinkResponse;
import com.example.drinkshop.R;
import com.example.drinkshop.Retrofit.DrinkShopApi;
import com.example.drinkshop.Retrofit.RetrofitClient;
import com.example.drinkshop.Utils.OnRefreshViewListner;
import com.example.drinkshop.Utils.SpaceItemDecoration;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

public class SearchActivity extends AppCompatActivity implements OnRefreshViewListner {

    List<String> suggestList = new ArrayList<>();
    List<Drink> drinkslist = new ArrayList<>();
    List<String> newSuggest = new ArrayList<>();

    RecyclerView recyclerView;

    CompositeDisposable compositeDisposable;

    MaterialSearchBar searchBar;

    DrinkAdapter searchAdapter,adapter;

    DrinkShopApi api;

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.RecyclerView_search);
        searchBar = findViewById(R.id.search_barr);

        compositeDisposable = new CompositeDisposable();
        api = RetrofitClient.getInstance().getApi();

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.addItemDecoration(new SpaceItemDecoration(8));

        searchBar.setHint("Search Your Drink");

        loadAllDrinks();

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList){
                    if(search.toLowerCase().contains(searchBar.getText().toLowerCase()) && !searchBar.getText().toLowerCase().isEmpty()){
                        suggest.add(search);
                    }
                }
                searchBar.updateLastSuggestions(suggest);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager methodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
        List<Drink> result = new ArrayList<>();
        for(Drink drink : drinkslist){
            if(drink.getName().toLowerCase().contains(text)){
                result.add(drink);
            }
        }
        searchAdapter = new DrinkAdapter(this,result);
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadAllDrinks() {
        compositeDisposable.add(RetrofitClient.getInstance().getApi().getAlldrink()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<DrinkResponse>() {
            @Override
            public void accept(DrinkResponse drinkResponse) throws Exception {
                displayAllDrinks(drinkResponse.getDrinks());
                createSuggest(drinkResponse.getDrinks());
            }
        }));

    }

    private void createSuggest(List<Drink> drinks) {
        for (Drink drink : drinks){
            suggestList.add(drink.getName());
            //searchBar.setLastSuggestions(suggestList);
        }
    }

    private void displayAllDrinks(List<Drink> drinks) {
        drinkslist = drinks;
        adapter = new DrinkAdapter(this,drinks);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void refreshView() {
    }

}
