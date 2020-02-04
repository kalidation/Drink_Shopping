package com.example.drinkshop.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.drinkshop.Adapters.CategoryAdapter;
import com.example.drinkshop.Adapters.ListAdapterCategory;
import com.example.drinkshop.Adapters.SliderAdapterExample;
import com.example.drinkshop.Model.AvatarResponse;
import com.example.drinkshop.Model.Banner;
import com.example.drinkshop.Model.BannerResponse;
import com.example.drinkshop.Model.Category;
import com.example.drinkshop.Model.CategoryResponse;
import com.example.drinkshop.Model.DrinkResponse;
import com.example.drinkshop.Model.User;
import com.example.drinkshop.R;
import com.example.drinkshop.Retrofit.RetrofitClient;
import com.example.drinkshop.Storage.RoomDataBase.DataSource.CartRepository;
import com.example.drinkshop.Storage.RoomDataBase.DataSource.FavoriteRepositry;
import com.example.drinkshop.Storage.RoomDataBase.Local.CartDataSource;
import com.example.drinkshop.Storage.RoomDataBase.Local.DrinkShopDataBase;
import com.example.drinkshop.Storage.RoomDataBase.Local.FavoriteDataSource;
import com.example.drinkshop.Storage.SharedPreferences.SharedPrefManager;
import com.example.drinkshop.Utils.Common;
import com.example.drinkshop.Utils.ItemDiffCallBack;
import com.example.drinkshop.Utils.SpaceItemDecoration;
import com.example.drinkshop.Utils.UploadCallBack;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.nex3z.notificationbadge.NotificationBadge;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import edmt.dev.afilechooser.utils.FileUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UploadCallBack {

    private static final int PICK_FILE_REQUEST = 222;
    private AppBarConfiguration mAppBarConfiguration;
    public DrawerLayout drawer;
    public NavController navController;

    public TextView textViewPhone;
    public TextView textViewName;

    public RecyclerView recyclerView;
    ListAdapterCategory adapter;

    public CompositeDisposable compositeDisposable = new CompositeDisposable();
    public CompositeDisposable compositeDisposable1 = new CompositeDisposable();

    public NotificationBadge badge;
    public ImageView cartIcon;
    public CircleImageView imageViewAvatar;
    public Switch aSwitch;

    public Uri fileUri;

    public DatabaseReference databaseReference;
    public StorageReference storageReference;
    public StorageTask uploadTask;

    public long backSeconde;
    public Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //TODO : BOT ANDROID STUDIO ---------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        //TODO : INIT STORAGE FIRBASE

        storageReference = FirebaseStorage.getInstance().getReference("User_Avatar");
        databaseReference = FirebaseDatabase.getInstance().getReference("User_Avatar");

        //TODO : SLIDER_CODE ---------------------------

        getBannerImage();

        //TODO : NAV_HEADER ----------------------------

        View headrView = navigationView.getHeaderView(0);
        textViewPhone = headrView.findViewById(R.id.text_view_phone_user);
        textViewName = headrView.findViewById(R.id.text_view_name_user);
        imageViewAvatar = headrView.findViewById(R.id.image_of_user);

        aSwitch = headrView.findViewById(R.id.switche);

        if (SharedPrefManager.getInstance(this).getDark()) {
            aSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            aSwitch.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    SharedPrefManager.getInstance(HomeActivity.this).isDark(true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SharedPrefManager.getInstance(HomeActivity.this).isDark(false);
                }
            }
        });

        if (SharedPrefManager.getInstance(this).getUser().getPhone() != "") {

            textViewPhone.setText(SharedPrefManager.getInstance(this).getUser().getPhone());
            textViewName.setText(SharedPrefManager.getInstance(this).getUser().getName());

            imageViewAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImage();
                }
            });

            if (!SharedPrefManager.getInstance(this).getUser().getAvatar().isEmpty()) {
                Glide.with(this)
                        .load(SharedPrefManager.getInstance(this).getUser().getAvatar())
                        .into(imageViewAvatar);
            }
        }


        //TODO : CATEGORY_RECYCLER_VIEW ------------------------

        recyclerView = findViewById(R.id.recyclerView_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));

        adapter = new ListAdapterCategory(new ItemDiffCallBack());
        recyclerView.setAdapter(adapter);

        getMenu();

        getMenuEveryChange();
        //TODO : TOOPING LIST -------------------------------

        getToppingList();


        //TODO : Init Database

        initDB();
    }


    //TODO : Methods / End Of onCreate Method

    private void getMenuEveryChange() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Menu");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initDB() {
        Common.dataBase = DrinkShopDataBase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.dataBase.cartDAO()));
        Common.favoriteRepositry = FavoriteRepositry.getInstance(FavoriteDataSource.getInstance(Common.dataBase.favoriteDAO()));

    }

    public void getBannerImage() {
        compositeDisposable.add(RetrofitClient.getInstance().getApi().getBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BannerResponse>() {
                    @Override
                    public void accept(BannerResponse bannerResponse) throws Exception {
                        displayBanner(bannerResponse.getBanner());
                    }
                }));
    }

    public void displayBanner(List<Banner> banners) {
        SliderView sliderView = findViewById(R.id.imageSlider);
        SliderAdapterExample adapter = new SliderAdapterExample(HomeActivity.this, banners);
        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    public void getMenu() {
        compositeDisposable.add(RetrofitClient.getInstance().getApi().getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CategoryResponse>() {
                    @Override
                    public void accept(CategoryResponse categoryResponse) throws Exception {
                        displayMenu(categoryResponse.getCategories());
                    }
                }));

      /*RetrofitClient.getInstance().getApi().getCategory()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Observer<CategoryResponse>() {
                  @Override
                  public void onSubscribe(Disposable d) {
                      compositeDisposable1.add(d);
                  }

                  @Override
                  public void onNext(CategoryResponse categoryResponse) {
                        displayMenu(categoryResponse.getCategories());
                  }

                  @Override
                  public void onError(Throwable e) {
                      Toast.makeText(HomeActivity.this, "There is en Error", Toast.LENGTH_SHORT).show();
                  }

                  @Override
                  public void onComplete() {

                  }
              });*/

    }

    public void displayMenu(List<Category> categories) {

        //CategoryAdapter adapter = new CategoryAdapter(this, categories);
        //recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        adapter.submitList(categories);
    }

    public void getToppingList() {
        /*compositeDisposable.add(RetrofitClient.getInstance().getApi().getDrink("7")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DrinkResponse>() {
                    @Override
                    public void accept(DrinkResponse drinkResponse) throws Exception {
                        Common.toppingList = drinkResponse.getDrinks();
                    }
                }));*/

        RetrofitClient.getInstance().getApi().getDrink("7")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DrinkResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(DrinkResponse drinkResponse) {
                        Common.toppingList = drinkResponse.getDrinks();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void chooseImage() {

        startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(), "Select File"), PICK_FILE_REQUEST);

    }

    private void uploadFile() {
        /*if(fileUri != null){
            File file = FileUtils.getFile(this,fileUri);

            String fileName = (Common.currentUser.getPhone()+" / "+FileUtils.getExtension(fileUri.toString()));

            ProgressRequestBody progressRequestBody = new ProgressRequestBody(file,this);

            final MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file",fileName,progressRequestBody);

            final MultipartBody.Part userPhone = MultipartBody.Part
                    .createFormData("Phone",SharedPrefManager.getInstance(this).getUser().getPhone());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Call<AvatarResponse> call =
                    RetrofitClient
                            .getInstance()
                            .getApi()
                            .uploadavatar(userPhone,body);
                    call.enqueue(new Callback<AvatarResponse>() {
                        @Override
                        public void onResponse(Call<AvatarResponse> call, Response<AvatarResponse> response) {
                            AvatarResponse avatar = response.body();
                            Toast.makeText(
                                    HomeActivity.this,
                                    avatar.getMessage()+" : "+avatar.getAvatarName(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<AvatarResponse> call, Throwable t) {
                            Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        }*/
        final AlertDialog dialog = new SpotsDialog(HomeActivity.this);
        dialog.show();
        dialog.setMessage("Wait Please ...");
        if (fileUri != null) {
            final StorageReference imageReference = storageReference.child(SharedPrefManager.getInstance(HomeActivity.this).getUser().getPhone() + "." + getFileExtension(fileUri));
            uploadTask = imageReference.putFile(fileUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();
                        final String imageUri = task.getResult().toString();
                        Log.d("Imageuri", imageUri);
                        //databaseReference.child(SharedPrefManager.getInstance(HomeActivity.this).getUser().getPhone()).setValue(imageUri);
                        Call<AvatarResponse> call = RetrofitClient.getInstance().getApi().uploadavatar(SharedPrefManager.getInstance(HomeActivity.this).getUser().getPhone(), imageUri);
                        call.enqueue(new Callback<AvatarResponse>() {
                            @Override
                            public void onResponse(Call<AvatarResponse> call, Response<AvatarResponse> response) {
                                Toast.makeText(HomeActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                User user = new User(
                                        SharedPrefManager.getInstance(HomeActivity.this).getUser().getPhone(),
                                        SharedPrefManager.getInstance(HomeActivity.this).getUser().getName(),
                                        SharedPrefManager.getInstance(HomeActivity.this).getUser().getBirthdate(),
                                        SharedPrefManager.getInstance(HomeActivity.this).getUser().getAddress(),
                                        imageUri
                                );
                                SharedPrefManager.getInstance(HomeActivity.this).saveUser(user);
                            }

                            @Override
                            public void onFailure(Call<AvatarResponse> call, Throwable t) {

                            }
                        });
                    } else {
                        Toast.makeText(HomeActivity.this, "Failed to upload the image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    // TODO : Override Methods -------------------------------------------------------


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data != null) {
                    fileUri = data.getData();
                    if (fileUri != null && !fileUri.getPath().isEmpty()) {
                        Log.d("File ", "File selected ");
                        imageViewAvatar.setImageURI(fileUri);
                        uploadFile();
                    } else {
                        Toast.makeText(this, "Can not Upload File to Server", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        compositeDisposable1.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        compositeDisposable1.clear();
        super.onStop();
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
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        updateCartCount();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cart_menu:
                return true;
            case R.id.search_menu:
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        drawer.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.nav_sign_out:
                SharedPrefManager.getInstance(this).logOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_favorite:
                Intent intent1 = new Intent(HomeActivity.this, FavoriteActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_orders:
                Intent intent2 = new Intent(HomeActivity.this, OrdersActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_location:
                startActivity(new Intent(HomeActivity.this,NearbyStore.class));
        }
        return true;
    }

    @Override
    public void onProgressUpdate(int pertantage) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backSeconde + 2000 > System.currentTimeMillis()) {
                toast.cancel();
                super.onBackPressed();
                return;
            } else {
                toast = Toast.makeText(this, "Please Click Back Again to Exit", Toast.LENGTH_SHORT);
                toast.show();
            }
            backSeconde = System.currentTimeMillis();
        }
    }
}
