package com.garima.garima.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.garima.garima.R;
import com.garima.garima.helper.ConnectionDetector;
import com.garima.garima.helper.PrefManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Member_window extends AppCompatActivity implements View.OnClickListener {

    private ConnectionDetector cd;
    private PrefManager pref;
    private String _phoneNo;
    private CoordinatorLayout coordinatorLayout;
    private ImageView profile_picture_collapse;
    private TextView _profile_name_collapse;
    private CardView _sc1,_sc2,_sc3,_sc4,_sc5,_sc6,_sc7,_sc8,_sc9;
    private Toolbar toolbar;
    private String mobileIp;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeader;
    private TextView txtName;
    private ImageView Edit_profile;
    private ImageView profile_image;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id._sc1:
                break;
            case R.id._sc2:
                Intent i = new Intent(Member_window.this, Member_List.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                break;
            case R.id._sc3:
                Intent j = new Intent(Member_window.this, Add_Member.class);
                startActivity(j);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                break;
            case R.id._sc4:
                Intent k = new Intent(Member_window.this, Remove_Member.class);
                startActivity(k);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                break;
            case R.id._sc5:
                Intent l = new Intent(Member_window.this, View_Stocks.class);
                startActivity(l);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                break;
            case R.id._sc6:
                Intent m = new Intent(Member_window.this, View_Invoice.class);
                startActivity(m);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                break;
            case R.id._sc7:
                Intent n = new Intent(Member_window.this, View_Commisions.class);
                startActivity(n);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                break;
            case R.id._sc8:
                Intent o = new Intent(Member_window.this, View_Growth.class);
                startActivity(o);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                break;
            case R.id._sc9:
                Intent p = new Intent(Member_window.this, Mainactivity.class);
                startActivity(p);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        cd = new ConnectionDetector(getApplicationContext());
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _phoneNo = user.get(PrefManager.KEY_MOBILE);
        coordinatorLayout=findViewById(R.id
                .main_content);
        profile_picture_collapse=findViewById(R.id.profile_picture_collapse);
        _profile_name_collapse=findViewById(R.id._profile_name_collapse);
        _sc1=findViewById(R.id._sc1);
        _sc2=findViewById(R.id._sc2);
        _sc3=findViewById(R.id._sc3);
        _sc4=findViewById(R.id._sc4);
        _sc5=findViewById(R.id._sc5);
        _sc6=findViewById(R.id._sc6);
        _sc7=findViewById(R.id._sc7);
        _sc8=findViewById(R.id._sc8);
        _sc9=findViewById(R.id._sc9);

        _sc1.setOnClickListener(this);
        _sc2.setOnClickListener(this);
        _sc3.setOnClickListener(this);
        _sc4.setOnClickListener(this);
        _sc5.setOnClickListener(this);
        _sc6.setOnClickListener(this);
        _sc7.setOnClickListener(this);
        _sc8.setOnClickListener(this);
        _sc9.setOnClickListener(this);

        drawer = findViewById(R.id.drawer_topics);
        navigationView = findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        txtName = navHeader.findViewById(R.id.name_profile);
        Edit_profile = navHeader.findViewById(R.id.edit_profile);
        Edit_profile.setVisibility(View.GONE);
        setUpNavigationView();

        toolbar = findViewById(R.id.toolbar_logged);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initCollapsingToolbar();
        mobileIp = getMobileIPAddress();
        if (TextUtils.isEmpty(mobileIp)) {
            mobileIp = getWifiIPAddress();
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Member_window.this, Mainactivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
            }
        });
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
        } // for now eat exceptions
        return null;
    }
    public String getWifiIPAddress() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = null;
        if (wifiMgr != null) {
            wifiInfo = wifiMgr.getConnectionInfo();
        }
        int ip = 0;
        if (wifiInfo != null) {
            ip = wifiInfo.getIpAddress();
        }
        return Formatter.formatIpAddress(ip);
    }



    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.toolbar_layout);
        if(pref.getDate()!=null) {
            final String date_ = parseDateToddMMyyyy(pref.getDate());

            collapsingToolbar.setTitleEnabled(true);
            collapsingToolbar.setTitle(date_);
        }
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_reg);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);
                    isShow = false;
                }
            }
        });

    }
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void setUpNavigationView() {

        //Setting Navigation View Item Selected Listener to handle the bean click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            // This method will trigger on bean Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        if (_phoneNo == null) {
                            Edit_profile.setVisibility(View.GONE);
                        } else {
                            Intent o = new Intent(Member_window.this, Update_profile.class);
                            startActivity(o);
                            finish();
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                        drawer.closeDrawers();
                        //recreate();
                        break;

                    case R.id.nav_reminder:
                        drawer.closeDrawers();

                        break;
                    case R.id.nav_refer:
                        drawer.closeDrawers();
                        if (pref.isLoggedIn()) {
                            Intent o2 = new Intent(Member_window.this, Refer_and_Earn_page.class);
                            startActivity(o2);
                            finish();
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        } else {
                            Intent o = new Intent(Member_window.this, SmsActivity.class);
                            startActivity(o);
                            finish();
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                        break;


                    case R.id.logout:

                        break;
                    default:
                }
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode== KeyEvent.KEYCODE_BACK)   {
            if(!Member_window.this.isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Member_window.this, R.style.AlertDialogTheme)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("Are you sure to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                                finishAffinity();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        }
        return true;
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
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
