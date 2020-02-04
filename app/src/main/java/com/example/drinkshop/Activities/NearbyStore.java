package com.example.drinkshop.Activities;

import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.example.drinkshop.Model.Store;
import com.example.drinkshop.R;
import com.example.drinkshop.Retrofit.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NearbyStore extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;

    CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_store);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        disposable = new CompositeDisposable();

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            buildLocationRequest();
                            buildLocationCallBack();

                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(NearbyStore.this);

                            //Start update Location
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(NearbyStore.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }).check();

    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Add a marker in Your Location and move the camera
                LatLng yourLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                mMap.addMarker(new MarkerOptions().position(yourLocation).title("Your Location"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(yourLocation,17.0f));

                getNearbyStores(locationResult.getLastLocation());

            }
        };
    }

    private void getNearbyStores(Location location) {
        RetrofitClient.getInstance().getApi().getNearbyStores(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Store>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                     disposable.add(d);
                    }
                    @Override
                    public void onNext(List<Store> stores) {
                        VectorDrawable vectorDrawable = (VectorDrawable)getDrawable(R.drawable.ic_action_name);
                        int h = vectorDrawable.getIntrinsicHeight();
                        int w = vectorDrawable.getIntrinsicWidth();

                        vectorDrawable.setBounds(0,0,w,h);

                        Bitmap bm = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bm);
                        vectorDrawable.draw(canvas);
                        for(Store store:stores){
                            LatLng storeLocation = new LatLng(Double.valueOf(store.getLat()),Double.valueOf(store.getLing()));

                            mMap.addMarker(new MarkerOptions()
                                    .position(storeLocation)
                                    .title(store.getName())
                                    .snippet(new StringBuilder("Distance :"+store.getDistance())+"KM")
                                    .icon(BitmapDescriptorFactory.fromBitmap(bm))
                            );
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    protected void onPause() {
        if(locationCallback != null)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(locationCallback != null)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Looper.myLooper());
    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        disposable.clear();
        super.onStop();
    }
}
