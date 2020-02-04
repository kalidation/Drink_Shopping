package com.example.drinkshop.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.drinkshop.Adapters.CartAdapter;
import com.example.drinkshop.Model.DataMessage;
import com.example.drinkshop.Model.MyResponse;
import com.example.drinkshop.Model.OrderResponse;
import com.example.drinkshop.Model.Tocken;
import com.example.drinkshop.R;
import com.example.drinkshop.Retrofit.FCMClient;
import com.example.drinkshop.Retrofit.RetrofitClient;
import com.example.drinkshop.Retrofit.RetrofitScalarsClient;
import com.example.drinkshop.Storage.RoomDataBase.Model.Cart;
import com.example.drinkshop.Storage.SharedPreferences.SharedPrefManager;
import com.example.drinkshop.Utils.Common;
import com.example.drinkshop.Utils.RecyclerItemTouchHelper;
import com.example.drinkshop.Utils.RecyclerItemTouchHelperListner;
import com.example.drinkshop.Utils.SpaceItemDecoration;
import com.example.drinkshop.Utils.onClickInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListner {

    private static final int PAYMENT_REQUEST_CODE = 7777;
    public long backSeconde;
    public Toast toast;

    public RecyclerView recyclerView;
    public CartAdapter cartAdapter;
    public Button placeOrder;

    private onClickInterface onclickInterface;

    public RadioButton globaleradioButtonOtherAddress;
    public ListView listView;

    public List<Cart> localCarts;

    public CompositeDisposable compositeDisposable;

    //Global String for payment
    String token, amount, orderAddress, orderComment;
    HashMap<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        compositeDisposable = new CompositeDisposable();
        recyclerView = findViewById(R.id.RecyclerView_Cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this, CartActivity.this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        onclickInterface = new onClickInterface() {
            @Override
            public void setClick(int postion, RecyclerView.ViewHolder holder) {
                onSwipe(holder, 1 << 2, postion);
            }
        };

        loadCart();

        placeOrder = findViewById(R.id.btn_place_order);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(CartActivity.this).getUser().getPhone() != "") {
                    sendOrder();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    builder.setMessage("Login Requiered");
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(CartActivity.this, LoginActivity.class));
                        }
                    });
                    builder.show();
                }
            }
        });
        loadToken();
    }

    private void loadToken() {

        final android.app.AlertDialog waitingDialog = new SpotsDialog(CartActivity.this);
        waitingDialog.show();
        waitingDialog.setMessage("Please Wait ... ");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Common.API_TOKEN_URL, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                waitingDialog.dismiss();
                placeOrder.setEnabled(false);
                Toast.makeText(CartActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                waitingDialog.dismiss();

                token = responseString;
                placeOrder.setEnabled(true);

            }
        });
    }

    private void sendOrder() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(CartActivity.this);
        dialog.setTitle("Send Order");

        View send_order_layout = LayoutInflater.from(this).inflate(R.layout.send_order_layout, null);

        final EditText editTextComment = send_order_layout.findViewById(R.id.edit_text_order_comment);
        final EditText editTextOtherAddress = send_order_layout.findViewById(R.id.edit_text_order_other_address);

        final RadioButton radioButtonUserAddress = send_order_layout.findViewById(R.id.radio_button_user_address);
        final RadioButton radioButtonOtherAddress = send_order_layout.findViewById(R.id.radio_button_other_address);
        final RadioButton radioButtonCreditCard = send_order_layout.findViewById(R.id.radio_button_credit_card);
        final RadioButton radioButtonCOD = send_order_layout.findViewById(R.id.radio_button_COD);

        globaleradioButtonOtherAddress = radioButtonOtherAddress;

        radioButtonUserAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextOtherAddress.setEnabled(false);
                }
            }
        });

        radioButtonOtherAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextOtherAddress.setEnabled(true);
                }
            }
        });

        dialog.setView(send_order_layout);
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("Send Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orderComment = editTextComment.getText().toString();

                if (radioButtonCreditCard.isChecked()) {

                    if (radioButtonUserAddress.isChecked()) {
                        orderAddress = SharedPrefManager.getInstance(CartActivity.this).getUser().getAddress();
                    } else if (radioButtonOtherAddress.isChecked()) {
                        orderAddress = editTextOtherAddress.getText().toString();
                    } else {
                        orderAddress = "";
                    }

                    //Todo : PAYMENT
                    DropInRequest dropInRequest = new DropInRequest().clientToken(token);
                    startActivityForResult(dropInRequest.getIntent(CartActivity.this), PAYMENT_REQUEST_CODE);

                    //final String finalOrderAddress = orderAddress;
                }else if(radioButtonCOD.isChecked()){
                    if (radioButtonUserAddress.isChecked()) {
                        orderAddress = SharedPrefManager.getInstance(CartActivity.this).getUser().getAddress();
                    } else if (radioButtonOtherAddress.isChecked()) {
                        orderAddress = editTextOtherAddress.getText().toString();
                    } else {
                        orderAddress = "";
                    }

                    compositeDisposable.add(
                            Common.cartRepository.getCartItems()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Consumer<List<Cart>>() {
                                        @Override
                                        public void accept(List<Cart> carts) throws Exception {
                                            if (!orderAddress.isEmpty())
                                                sendtoServerByCOD(
                                                        Common.cartRepository.totalPrice(),
                                                        orderComment,
                                                        orderAddress,
                                                        carts);
                                            else
                                                globaleradioButtonOtherAddress.setError("Address Required");
                                        }
                                    })
                    );

                }
            }
        });
        dialog.show();
    }

    private void sendtoServerByCOD(float totalPrice, String orderComment, String finalOrderAddress, List<Cart> carts) {

        if (carts.size() > 0) {
            // StringBuilder orderDetail = new StringBuilder();
            // orderDetail.append("");
            String orderDetail = new Gson().toJson(carts);
           /* for (int i = 0; i < carts.size(); i++) {
                orderDetail
                        .append("Detail for Cart Number : " + i + "\n")
                        .append("Name : " + carts.get(i).name + "\n")
                        .append("Amount : " + carts.get(i).amount + "\n")
                        .append("Price : " + carts.get(i).price + "\n")
                        .append("Extras : " + carts.get(i).toppingExtras + "\n")
                        .append("\n");
            }*/


            Call<OrderResponse> call = new RetrofitClient().getApi().insertOrder(
                    orderDetail.toString(),
                    totalPrice,
                    orderComment,
                    finalOrderAddress,
                    SharedPrefManager.getInstance(CartActivity.this).getUser().getPhone(),
                    "On Delivery"
            );
            call.enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, final Response<OrderResponse> response1) {

                    RetrofitClient.getInstance().getApi().getToken("server_app_01")
                            .enqueue(new Callback<Tocken>() {
                                @Override
                                public void onResponse(Call<Tocken> call, Response<Tocken> response) {
                                    Map<String, String> contenSend = new HashMap<>();
                                    contenSend.put("title", "DrinkShop");
                                    contenSend.put("message", "You Have New Order" + response1.body().getOrderId());


                                    Log.e("jsondata", "onResponse: " + response.body().getToken());

                                    DataMessage dataMessage = new DataMessage();
                                    dataMessage.setTo(response.body().getToken());
                                    dataMessage.setData(contenSend);

                                    // String data = new Gson().toJson(dataMessage);

                                    // Log.e("jsondata", "onResponse: "+data );
                                    //  Log.e("jsondata", "onResponse: "+data );
                                    FCMClient.getInstance().getApi().sendNotification(dataMessage)
                                            .enqueue(new Callback<MyResponse>() {
                                                @Override
                                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                    if (response.code() == 200) {
                                                        if (response.body().success == 1) {
                                                            Toast.makeText(CartActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                                                            Log.i("messageid", "onResponse: " + response.body().results.get(0).message_id);
                                                            Common.cartRepository.emptyCart();
                                                        } else {
                                                            Toast.makeText(CartActivity.this, "Failed ", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                                    Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                }

                                @Override
                                public void onFailure(Call<Tocken> call, Throwable t) {
                                    Toast.makeText(CartActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                if (Common.cartRepository.totalPrice() > 0) {
                    amount = String.valueOf(Common.cartRepository.totalPrice());
                    params = new HashMap<>();

                    params.put("amount", amount);
                    params.put("nonce", strNonce);

                    sendPayment();

                } else {
                    Toast.makeText(this, "Payment Amount is 0", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Payment Cancelled ", Toast.LENGTH_SHORT).show();
            } else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.e("ERROR", error.getMessage());
            }
        }

    }

    private void sendPayment() {
        RetrofitScalarsClient
                .getInstance()
                .getApi()
                .payment(params.get("nonce"), params.get("amount"))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().toString().contains("Successful")) {
                            Toast.makeText(CartActivity.this, "Transaction successful", Toast.LENGTH_SHORT).show();

                            compositeDisposable.add(
                                    Common.cartRepository.getCartItems()
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new Consumer<List<Cart>>() {
                                                @Override
                                                public void accept(List<Cart> carts) throws Exception {
                                                    if (!orderAddress.isEmpty())
                                                        sendtoServer(
                                                                Common.cartRepository.totalPrice(),
                                                                orderComment,
                                                                orderAddress,
                                                                carts);
                                                    else
                                                        globaleradioButtonOtherAddress.setError("Address Required");
                                                }
                                            })
                            );

                        } else {
                            Toast.makeText(CartActivity.this, "Transaction Failed", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("KHALEDBUG", response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("KHALEDBUG", t.getMessage());

                    }
                });

    }

    private void sendtoServer(float totalPrice, String orderComment, String finalOrderAddress, List<Cart> carts) {

        if (carts.size() > 0) {
            // StringBuilder orderDetail = new StringBuilder();
            // orderDetail.append("");
            String orderDetail = new Gson().toJson(carts);
           /* for (int i = 0; i < carts.size(); i++) {
                orderDetail
                        .append("Detail for Cart Number : " + i + "\n")
                        .append("Name : " + carts.get(i).name + "\n")
                        .append("Amount : " + carts.get(i).amount + "\n")
                        .append("Price : " + carts.get(i).price + "\n")
                        .append("Extras : " + carts.get(i).toppingExtras + "\n")
                        .append("\n");
            }*/


            Call<OrderResponse> call = new RetrofitClient().getApi().insertOrder(
                    orderDetail.toString(),
                    totalPrice,
                    orderComment,
                    finalOrderAddress,
                    SharedPrefManager.getInstance(CartActivity.this).getUser().getPhone(),
                    "Braintree"
            );
            call.enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, final Response<OrderResponse> response1) {

                    RetrofitClient.getInstance().getApi().getToken("server_app_01")
                            .enqueue(new Callback<Tocken>() {
                                @Override
                                public void onResponse(Call<Tocken> call, Response<Tocken> response) {
                                    Map<String, String> contenSend = new HashMap<>();
                                    contenSend.put("title", "DrinkShop");
                                    contenSend.put("message", "You Have New Order" + response1.body().getOrderId());


                                    Log.e("jsondata", "onResponse: " + response.body().getToken());

                                    DataMessage dataMessage = new DataMessage();
                                    dataMessage.setTo(response.body().getToken());
                                    dataMessage.setData(contenSend);

                                    // String data = new Gson().toJson(dataMessage);

                                    // Log.e("jsondata", "onResponse: "+data );
                                    //  Log.e("jsondata", "onResponse: "+data );
                                    FCMClient.getInstance().getApi().sendNotification(dataMessage)
                                            .enqueue(new Callback<MyResponse>() {
                                                @Override
                                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                                    if (response.code() == 200) {
                                                        if (response.body().success == 1) {
                                                            Toast.makeText(CartActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                                                            Log.i("messageid", "onResponse: " + response.body().results.get(0).message_id);
                                                            Common.cartRepository.emptyCart();
                                                        } else {
                                                            Toast.makeText(CartActivity.this, "Failed ", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                                    Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                }

                                @Override
                                public void onFailure(Call<Tocken> call, Throwable t) {
                                    Toast.makeText(CartActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void loadCart() {
        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception {
                                displayCartItem(carts);
                            }
                        })
        );
    }

    private void displayCartItem(List<Cart> carts) {
        localCarts = carts;
        cartAdapter = new CartAdapter(this, carts, onclickInterface);
        recyclerView.setAdapter(cartAdapter);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }


    public void onBackPressed() {
        startActivity(new Intent(CartActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.MyViewHolder) {
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    /*
                      Get Name of selected item to show it ins Snackbar &&
                      Get Cart item of  selected item to delete it from ROOM
                      Get index of item selected to delete it from Cart adapter
                     */
                    String name = localCarts.get(viewHolder.getAdapterPosition()).name;
                    final Cart deleteitem = localCarts.get(viewHolder.getAdapterPosition());
                    final int deleteIndx = viewHolder.getAdapterPosition();

                    //Delete From adapter
                    cartAdapter.removeItem(deleteIndx);
                    //Delete  From ROOMDATABASE
                    Common.cartRepository.deleteCartItem(deleteitem);

                    Snackbar snackbar = Snackbar.make(recyclerView, name + " Removed From Favorites ", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Cancel This Action", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cartAdapter.restoreItem(deleteitem, deleteIndx);
                            Common.cartRepository.insertToCart(deleteitem);
                        }
                    });
                    snackbar.setActionTextColor(Color.WHITE);
                    snackbar.show();
                    break;
            }
        }
    }

}
