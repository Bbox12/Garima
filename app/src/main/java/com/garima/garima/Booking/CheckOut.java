package com.garima.garima.Booking;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.garima.garima.Activities.Mainactivity;
import com.garima.garima.Model.main;
import com.garima.garima.R;
import com.garima.garima.helper.ConnectionDetector;
import com.garima.garima.helper.MyViewPager;
import com.garima.garima.helper.PrefManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class CheckOut extends AppCompatActivity implements View.OnClickListener ,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, PlaceSelectionListener {
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(25.00000, 91.00000), new LatLng(27.99999, 91.99999));
    private static final int REQUEST_PICK_FROM = 10016;
    Boolean isInternetPresent=false;
    private ConnectionDetector cd;
    private PrefManager pref;
    private double My_lat=0,My_long=0;
    private String _phoneNo;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private String postUrl;
    private TextView canteen_address,canteen_name;
    private ImageView canteen_image;
    private RecyclerView _moreRv;
    private LinearLayout L1, L2;
    private LinearLayout R1;
    private TextView _moneyValue,_itemValue,_discount;
    private TextView discount,_moreValue,_servicable,_reason,minimum_order,minimum_person,canteen_time,canteen_time1;
    private DecimalFormat df = new DecimalFormat("0.00");
    private DecimalFormat dft = new DecimalFormat("0");
    private ArrayList<main> mItems = new ArrayList<main>();
    private Button _cancel,_confirm;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoading=false;
    private int _last=0;
    private int myID=0;
    private boolean _end=false;
    SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("yyy-MM-dd");
    private int hour;
    private TextView _tAmount,_dAmount,_payAmount,_cAmount;
    private String _total;
    private double _less,_more;
    private DatabaseReference mDatabase;
    private double Distance=0;
    private String mobileIp;
    private String OTP;
    private int j=0;
    private String commaSeparatedValues;
    private RelativeLayout _can;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private final int REQUEST_LOCATION = 200;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private PendingResult<LocationSettingsResult> result;
    private LocationSettingsRequest.Builder builder;
    private boolean mResolvingError = false;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    private final int REQUEST_CHECK_SETTINGS = 300;
    private Button Ride_now,Ride_cancel;
    private TextView My_address;
    private Animation expandIn;
    private Animation myAnim;
    private Animation animFadein, animFadeout;
    private Animation animslideIn, animslideup;
    private ImageView Heart_t1, Heart_t2;
    private String Favourite;
    private TextView Drop, Pick;
    private RelativeLayout rMap;
    private RelativeLayout rideButtons;
    private Animation animslideU,animslideD;
    private MyViewPager viewPager;
    private ViewPagerAdapter adapter;
    private int _palce;
    private EditText Home, Work, Other;
    private boolean first=false;
    private int AUTOCOMPLETE_REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.checkout_layout);
        viewPager = findViewById(R.id.viewPagerVertical);
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        cd=new ConnectionDetector(getApplicationContext());
        pref=new PrefManager(getApplicationContext());
        HashMap<String, String> user=pref.getUserDetails();
        _phoneNo=user.get(PrefManager.KEY_MOBILE);
        progressBar=findViewById(R.id.progressBar3_eats);
        rideButtons = findViewById(R.id.linearLayout);
        _can=findViewById(R.id.can);
        coordinatorLayout=findViewById(R.id.main_content);
        _cancel=findViewById(R.id._cancel_book);
        _confirm=findViewById(R.id._confirm_book);
        Home = findViewById(R.id.place_home1);
        Work = findViewById(R.id.place_work1);
        Other = findViewById(R.id.place_other1);
        _confirm.setEnabled(false);
        _cancel.setEnabled(false);
        animslideD = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down1);
        animslideU = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up1);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        _moreRv=findViewById(R.id.moreRv);
        _moreRv.setNestedScrollingEnabled(false);
        canteen_image=findViewById(R.id._image);
        canteen_name=findViewById(R.id._name);
        canteen_address=findViewById(R.id._address);
        _moneyValue=findViewById(R.id.canteen_amount);
        _itemValue=findViewById(R.id._noofItems);
        _discount=findViewById(R.id.discount);
        _tAmount=findViewById(R.id._tamount);
        _dAmount=findViewById(R.id._damount);
        _cAmount=findViewById(R.id._camount);
        _payAmount=findViewById(R.id._payamount);
        My_address = findViewById(R.id.del_address);
        My_address.setSelected(true);
        My_address.setOnClickListener(this);
        Ride_now=findViewById(R.id.ride_now);


        Ride_now.setOnClickListener(this);
        Ride_cancel=findViewById(R.id.ride_cancel);


        Ride_cancel.setOnClickListener(this);
        Intent i=getIntent();
        j=i.getIntExtra("from",0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(j==0) {
                    Intent i = new Intent(CheckOut.this, Mainactivity.class);
                    i.putExtra("my_lat", My_lat);
                    i.putExtra("my_long", My_long);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                }else{
                    Intent i = new Intent(CheckOut.this, Mainactivity.class);
                    i.putExtra("my_lat", My_lat);
                    i.putExtra("my_long", My_long);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                }
            }
        });

        mobileIp = getMobileIPAddress();
        if (TextUtils.isEmpty(mobileIp)) {
            mobileIp = getWifiIPAddress();
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(CheckOut.this);
        createLocationCallback();
        buildLocationSettingsRequest();
        rebuildGoogleApiClient();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            onConnected(null);
        }


    }

    @Override
    public void onPlaceSelected(Place place) {

    }


    @Override
    public void onError(Status status) {

    }


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public Object instantiateItem(View collection, int position) {

            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.L1;
                    break;
                case 1:
                    resId = R.id.L2;
                    break;

            }
            return findViewById(resId);
        }
    }

    protected LocationRequest createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }


    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                updateLocationUI(mCurrentLocation);
            }
        };
    }
    private void startLocationUpdat() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(mFusedLocationClient!=null ) {
            mFusedLocationClient.requestLocationUpdates(createLocationRequest(), new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {

                            List<Location> locationList = locationResult.getLocations();
                            if (locationList.size() > 0) {
                                Location location = locationList.get(locationList.size() - 1);
                                Log.i("MapsActivity", "Location: " + location);
                                mCurrentLocation = location;
                                updateLocationUI(mCurrentLocation);
                            }

                        }
                    },
                    Looper.myLooper());
        }
    }

    private void updateLocationUI(Location mCurrentLocatio) {


            My_lat = mCurrentLocatio.getLatitude();
            My_long =mCurrentLocatio.getLongitude();
            if(!first) {
                first=true;
                pref.setDropAt1(getCompleteAddressString(My_lat, My_long));
                pref.setDropLat1(String.valueOf(My_lat));
                pref.setDropLong1(String.valueOf(My_long));
                My_address.setText(pref.getDropAt1());


        }


    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String strAdd="";
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size()!=0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }
                strAdd = strReturnedAddress.toString();
                Log.w(" address", strReturnedAddress.toString());
            } else {
                strAdd= LATITUDE +","+ LONGITUDE;
                Log.w(" address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            strAdd= LATITUDE +","+ LONGITUDE;
            Log.w(" loction address", "Canont get Address!");
        }
        return strAdd;
    }


    public String getWifiIPAddress() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ip);
    }


    public static String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.w("eat", "hi");

        }
        return null;
    }


    @Override
    public void onConnected(Bundle bundle) {
        builder = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    startLocationUpdat();
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(CheckOut.this, REQUEST_CHECK_SETTINGS);
                                break;
                            } catch (IntentSender.SendIntentException e) {
                            } catch (ClassCastException e) {
                                Log.w(" ClassCastException", "Canont get Address!"+e.getLocalizedMessage());
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.

                            break;
                    }
                }}
        });
    }









    @Override
    public void onConnectionSuspended(int i) {
        googleApiClientConnectionStateChange(false);
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (mResolvingError) {

        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        } else {
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            dialog.cancel();
        }
    }
    private void stopLocationUpdates() {

        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                    }
                });
    }
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);

    }

    protected synchronized void rebuildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this /* ConnectionCallbacks */)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        googleApiClientConnectionStateChange(true);
                    }
                })
                .addApi(LocationServices.API)
                .build();

    }

    private void googleApiClientConnectionStateChange(final boolean connected) {
        final Context appContext = this.getApplicationContext();

    }
    @Override
    protected void onStart(){
        super.onStart();

    }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    protected void onStop(){
        super.onStop();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }

    @Override
    protected void onResume(){
        super.onResume();
        isInternetPresent=cd.isConnectingToInternet();
        if(isInternetPresent){
            if(j==1){
              _confirm.setVisibility(View.GONE);
                _cancel.setVisibility(View.GONE);
                _confirm.setEnabled(false);
                _cancel.setEnabled(false);
                _moreRv.setOnTouchListener(null);
            }else{
                _confirm.setVisibility(View.VISIBLE);
                _cancel.setVisibility(View.VISIBLE);
                _confirm.setEnabled(true);
                _cancel.setEnabled(true);
                _cancel.setOnClickListener(this);
                _confirm.setOnClickListener(this);

            }
            if(pref.getCharge()==0){
                _can.setVisibility(View.GONE);
            }else{
                _can.setVisibility(View.VISIBLE);
                _cAmount.setText(df.format(pref.getCharge()));
            }
            myID=pref.get_cID1();
            _itemValue.setText("No of items "+ pref.get_food_items());
            if(pref.getcDiscount()!=null ){
                if(Double.parseDouble(pref.getcDiscount())!=0) {
                    _discount.setVisibility(View.VISIBLE);
                    _discount.setText(dft.format(Double.parseDouble(pref.getcDiscount())) + "% discount on all orders");
                    double tot = Double.parseDouble(_tAmount.getText().toString().trim()) * .01 * Double.parseDouble(pref.getcDiscount());
                    _dAmount.setText(df.format(tot));
                }else{
                    _discount.setVisibility(View.GONE);
                }
            }else{
                _discount.setVisibility(View.GONE);
            }





            if(pref.get_packagesharedPreferences()!=null) {
                mItems.clear();
                Set<String> set = pref.get_packagesharedPreferences();
                int Rate=0;
                for (String s : set) {
                    String[] pars = s.split("\\_");
                        main items=new main();
                        items.setID(Integer.parseInt(pars[0]));
                        items.setNoofItems((int) Double.parseDouble(pars[1]));
                    items.setPrice((int) Double.parseDouble(pars[2]));
                    Rate= (int) (Double.parseDouble(pars[2])+Rate);
                    items.setName((pars[3]));
                    mItems.add(items);
                }
                initCollapsingToolbar();
                _moreRv.setVisibility(View.VISIBLE);
                BookingAdapter sAdapter1 = new BookingAdapter(CheckOut.this, mItems);
                sAdapter1.notifyDataSetChanged();
                sAdapter1.setPref(pref);
                sAdapter1.setFrom(j);
                mLayoutManager= new LinearLayoutManager(CheckOut.this, RecyclerView.VERTICAL, false);
                _moreRv.setLayoutManager(mLayoutManager);
                _moreRv.setItemAnimator(new DefaultItemAnimator());
                _moreRv.setAdapter(sAdapter1);
                _moreRv.setHasFixedSize(true);
                _tAmount.setText(df.format(Rate));

            }

            if(pref.getPickLat1()==null){

                get_C();

            }else{

                _total=df.format(Double.parseDouble(_tAmount.getText().toString())-Double.parseDouble(_dAmount.getText().toString())
                        +Double.parseDouble(String.valueOf(pref.getCharge())));
                Log.w("Dis", _total);

                _payAmount.setText("\u20B9"+_total);
                _moneyValue.setText(_total);
                _confirm.setEnabled(true);
                _cancel.setEnabled(true);

            }

            startLocationUpdat();



        }else{
            Snackbar snackbar=Snackbar
                    .make(coordinatorLayout,"No internet connection!",Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY",new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            recreate();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    }

    private void get_C() {
        pref.setPickAt1(pref.getcAddress());

        _total=df.format(Double.parseDouble(_tAmount.getText().toString())-Double.parseDouble(_dAmount.getText().toString())
                +Double.parseDouble(String.valueOf(pref.getCharge())));
        Log.w("Dis", _total);

        _payAmount.setText("\u20B9"+_total);
        _moneyValue.setText(_total);
        _confirm.setEnabled(true);
        _cancel.setEnabled(true);

    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.toolbar_layout);


        AppBarLayout appBarLayout = findViewById(R.id.app_bar_main);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed

    }




    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ride_cancel:
                if(viewPager.getCurrentItem()==1){
                    viewPager.setCurrentItem(0);
                }
                break;

            case R.id.ride_now:

               if(!TextUtils.isEmpty(My_address.getText().toString())){
                       if(TextUtils.isEmpty(Work.getText().toString())){
                           Work.setError("Please input House.Flat No");
                       }else{
                            go( My_address.getText().toString(),Work.getText().toString(),
                                    Other.getText().toString());
                       }

                   

               }else{
                   if(TextUtils.isEmpty(Home.getText().toString())){
                       My_address.setError("Address Missing");
                       Home.setError("Address Missing");
                   }else {
                       if(TextUtils.isEmpty(Work.getText().toString())){
                           Work.setError("Please input House.Flat No");
                       }else{
                           if(TextUtils.isEmpty(Work.getText().toString())){
                               Other.setError("Please input a Landmark");
                           }else{
                               go( Home.getText().toString(),Work.getText().toString(),
                                       Other.getText().toString());
                           }
                       }

                   }
               }


                break;




            case R.id.del_address:

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

// Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;


            case R.id._cancel_book:
                if (!CheckOut.this.isFinishing()) {
                    new AlertDialog.Builder(CheckOut.this, R.style.AlertDialogTheme)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("Are you sure?")
                            .setMessage("Press Ok to cancel your order")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pref.set_food_items(0);
                                    pref.set_food_money(0);
                                    pref.setPref1(null);
                                    pref.setPref2(null);
                                    pref.setPref3(null);
                                    pref.setPref4(null);
                                    pref.packagesharedPreferences(null);
                                    if(j==0) {
                                        Intent i = new Intent(CheckOut.this, Mainactivity.class);
                                        i.putExtra("my_lat", My_lat);
                                        i.putExtra("my_long", My_long);
                                        startActivity(i);
                                        finish();
                                        overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                                    }else{
                                        Intent i = new Intent(CheckOut.this, Mainactivity.class);
                                        i.putExtra("my_lat", My_lat);
                                        i.putExtra("my_long", My_long);
                                        startActivity(i);
                                        finish();
                                        overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                                    }
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }


                break;


            case R.id._confirm_book:
                    viewPager.setCurrentItem(1);
                break;

            default:
                break;
        }
    }

    private void go(String s1,String s2,String s3){
        if(!TextUtils.isEmpty(_payAmount.getText().toString())) {
            String[] pars = _total.split("\\.");
            if (Integer.parseInt(pars[0]) > pref.getMinimumOrder()) {
                pref.setDropAt1(s1+","+s2+","+s3);
                Toast.makeText(getApplicationContext(),"Thkis is a Test",Toast.LENGTH_SHORT).show();
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Order value is less than minimum value of " + pref.getMinimumOrder(), Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent o = new Intent(CheckOut.this, Mainactivity.class);
                                startActivity(o);
                                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                                finish();
                            }
                        });

                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (place != null) {
                    if (!TextUtils.isEmpty(place.getAddress())) {
                        _palce=1;
                        LatLng queriedLocation = place.getLatLng();
                        My_address.setText(place.getAddress());
                        My_lat=queriedLocation.latitude;
                        My_long=queriedLocation.longitude;
                        pref.setDropAt1(My_address.getText().toString());
                        pref.setDropLat1(String.valueOf(My_lat));
                        pref.setDropLong1(String.valueOf(My_long));
                    }
                }
                Log.i("hi", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                if (status.getStatusMessage() != null) {
                    Log.i("error", status.getStatusMessage());
                }
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_white, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_refresh1:
                recreate();
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode== KeyEvent.KEYCODE_BACK)   {
                Intent o = new Intent(CheckOut.this, Mainactivity.class);
                o.putExtra("my_lat", My_lat);
                o.putExtra("my_long", My_long);
                startActivity(o);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);

        }
        return true;
    }


}






