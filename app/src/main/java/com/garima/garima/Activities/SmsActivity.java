package com.garima.garima.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.garima.garima.R;
import com.garima.garima.helper.AppController;
import com.garima.garima.helper.Config_URL;
import com.garima.garima.helper.ConnectionDetector;
import com.garima.garima.helper.MyViewPager;
import com.garima.garima.helper.PrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;



/**
 * Created by parag on 13/01/17.
 */
public class SmsActivity extends AppCompatActivity implements View.OnClickListener {


    private static String Name_giver = "";
    private static String _PhoneNo_giver = "";
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private MyViewPager viewPager;
    private ViewPagerAdapter adapter;
    private EditText _PhoneNo;
    private Button First_button;
    private ProgressBar progressBar;
    private PrefManager pref;
    private Button Second_button;
    private EditText inputOtp;

    private double My_long = 0, My_lat = 0;
    private EditText inputName;
    private String mobileIp = "";
    private CoordinatorLayout coordinatorLayout;
    private boolean permissionsAllowd=false;
    private boolean doubleBackToExitPressedOnce=false;
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
        } // for now eat exceptions
        return null;
    }

    private static boolean isValidPhoneNumber(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            String[] str2 = phone.split("");
            int j=0;
            for (int i=0;i<str2.length;i++)
            {
                if (!TextUtils.isEmpty(str2[i]))
                {
                    j++;
                }

            }
            check = j == 10 || j == 13;
        }
        return check;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sms);
        coordinatorLayout = findViewById(R.id
                .smscor);
        viewPager = findViewById(R.id.viewPagerVertical);
        inputOtp = findViewById(R.id.inputOtp);
        inputName = findViewById(R.id.u_name);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        progressBar = findViewById(R.id.progress_rider);
        pref = new PrefManager(this);
        _PhoneNo = findViewById(R.id.u_mobile);

        First_button = findViewById(R.id.first_button);
        First_button.setOnClickListener(this);
        Second_button = findViewById(R.id.second_butona);
        Second_button.setOnClickListener(this);
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);


        mobileIp = getMobileIPAddress();
        if (TextUtils.isEmpty(mobileIp)) {
            mobileIp = getWifiIPAddress();
        }



    }

    public String getWifiIPAddress() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ip);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            if (pref.isLoggedIn()) {
                Intent o = new Intent(SmsActivity.this, Splash_screen.class);
                o.putExtra("my_lat", My_lat);
                o.putExtra("my_long", My_long);
                startActivity(o);
                o.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                o.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                o.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
            }else {
                First_button.setOnClickListener(this);
                Second_button.setOnClickListener(this);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }

                });

            }
        } else {
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), "No Internet,Connect to internet", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();

                        }
                    });
            snackbar.setActionTextColor(Color.YELLOW);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.first_button:
                _PhoneNo_giver = "+91" + _PhoneNo.getText().toString();
                Name_giver = inputName.getText().toString();

                if (TextUtils.isEmpty(_PhoneNo_giver)) {
                    _PhoneNo.setError("Empty");
                } else if (isValidPhoneNumber(_PhoneNo_giver)) {


                        pref.setIsWaitingForSms(true);
                        validateForm();
                        break;


                } else {
                    _PhoneNo.setError("Error!");

                }
            case R.id.second_butona:
                //verifyOtp();
                final String code = inputOtp.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    inputOtp.setError("Cannot be empty");
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    Second_button.setEnabled(false);
                    StringRequest eventoReq = new StringRequest(com.android.volley.Request.Method.POST, Config_URL.URL_VERIFY_USER_OTP,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.w("volley", response);

                                    String[] pars = response.split("error");
                                    boolean success = pars[1].contains("false");
                                    progressBar.setVisibility(View.GONE);
                                    if (success) {
                                        String[] pars_ = pars[1].split("false,");
                                        JSONObject jObj = null;
                                        try {
                                            jObj = new JSONObject("{".concat(pars_[1]));
                                            JSONObject user = jObj.getJSONObject("user");
                                            pref.setKeyResponsibility(user.getInt("Responsibility"));
                                            pref.setRegID(user.getString("RegNo"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        pref.createLogin("91" + _PhoneNo.getText().toString());
                                        Intent o = new Intent(SmsActivity.this, Splash_screen.class);
                                        o.putExtra("my_lat", My_lat);
                                        o.putExtra("my_long", My_long);
                                        startActivity(o);
                                        finish();
                                        overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);

                                    } else {
                                        viewPager.setCurrentItem(0);
                                        Second_button.setEnabled(true);
                                        Snackbar.make(getWindow().getDecorView().getRootView(), "Error!Please check internet connection", Snackbar.LENGTH_LONG).show();

                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d("uuu", "Error: " + error.getMessage());
                            vollyerror(error);
                        }

                    }){
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("_mId",  _PhoneNo.getText().toString());
                            params.put("otp",code);
                            return params;
                        }

                    };
                    AppController.getInstance().addToRequestQueue(eventoReq);

                }

                break;



        }
    }

    private void validateForm() {
        StringRequest eventoReq = new StringRequest(com.android.volley.Request.Method.POST, Config_URL.URL_REQUEST_MOBILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w("volley", response);
                            String[] pars = response.split("error");
                            boolean success = pars[1].contains("false");
                        if (success) {

                            viewPager.setCurrentItem(1);
                        } else {

                            Snackbar.make(getWindow().getDecorView().getRootView(), "Please check mobile no.", Snackbar.LENGTH_LONG).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("uuu", "Error: " + error.getMessage());
                 vollyerror(error);
            }

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("_mId",  _PhoneNo.getText().toString());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(eventoReq);

    }

    private void vollyerror(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Network is unreachable. Please try another time", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Refresh", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        } else if (error instanceof AuthFailureError) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Network is unreachable. Please try another time", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Refresh", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        } else if (error instanceof ServerError) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Server Error.Please try another time", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Refresh", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        } else if (error instanceof NetworkError) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Network Error. Please try another time", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Refresh", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        } else if (error instanceof ParseError) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Data Error. Please try another time", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Refresh", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }


    }
    @Override
    protected void onStop() {
        super.onStop();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_white, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh1) {
            recreate();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Press Back again to Exit.", Toast.LENGTH_SHORT).show();

            doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                    finish();
                }
            }, 1000);

        }

    }

    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        public Object instantiateItem(@NonNull View collection, int position) {

            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.first_pager;
                    break;
                case 1:
                    resId = R.id.second_pager_;
                    break;

            }
            return findViewById(resId);
        }
    }



}

