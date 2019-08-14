package com.garima.garima.Activities;

import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.garima.garima.Adapters.MainAdapter;
import com.garima.garima.Booking.CheckOut;
import com.garima.garima.Model.main;
import com.garima.garima.R;
import com.garima.garima.helper.AppController;
import com.garima.garima.helper.Config_URL;
import com.garima.garima.helper.ConnectionDetector;
import com.garima.garima.helper.PrefManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;




public class Mainactivity extends AppCompatActivity implements View.OnClickListener {

    Boolean isInternetPresent=false;
    private ConnectionDetector cd;
    private PrefManager pref;
    private boolean mResolvingError=false;
    private double My_lat=0,My_long=0;
    private String _phoneNo;
    private CoordinatorLayout coordinatorLayout;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    private ArrayList<main> CanteenArray = new ArrayList<main>();
    private int currentPage=0;
    private Toolbar toolbar;
    private TextView[] dots;
    private int _palce=0;
    private RecyclerView _moreRv;
    private NestedScrollView Nscroll;
    private boolean _time=false;
    private WebView webView;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View navHeader;
    private TextView txtName;
    private ImageView Edit_profile;
    private ImageView profile_image;
    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayout layoutBottomSheet;
    private TextView _moneyValue,_itemValue;
    private Button _checkout;

    @Override
 protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        cd=new ConnectionDetector(getApplicationContext());
        pref=new PrefManager(getApplicationContext());
        HashMap<String, String> user=pref.getUserDetails();
        _phoneNo=user.get(PrefManager.KEY_MOBILE);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        _moreRv=findViewById(R.id.moreRv);
        _moreRv.setNestedScrollingEnabled(false);
        coordinatorLayout=findViewById(R.id
        .cor_home_main);
        _checkout=findViewById(R.id.checkout);
        _checkout.setOnClickListener(this);
        _moneyValue=findViewById(R.id._moneyValue);
        _itemValue=findViewById(R.id._itemValue);
        layoutBottomSheet=findViewById(R.id._added);
        drawer = findViewById(R.id.drawer_layout_main);
        navigationView = findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        txtName = navHeader.findViewById(R.id.name_profile);
        Edit_profile = navHeader.findViewById(R.id.edit_profile);
        Edit_profile.setVisibility(View.GONE);
        profile_image = navHeader.findViewById(R.id.img_profile);
        Nscroll=findViewById(R.id.Nscroll);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        viewPager.setVisibility(View.GONE);
        _moreRv.setVisibility(View.GONE);
        Nscroll.setSmoothScrollingEnabled(true);
        _moreRv.setNestedScrollingEnabled(false);
        initCollapsingToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Mainactivity.this, Splash_screen.class);
                i.putExtra("my_lat", My_lat);
                i.putExtra("my_long", My_long);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
            }
        });

        setUpNavigationView();


                        }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.toolbar_layout);

                collapsingToolbar.setTitleEnabled(false);
                collapsingToolbar.setTitle("");


        AppBarLayout appBarLayout = findViewById(R.id.app_bar_main);
        appBarLayout.setExpanded(true);

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
                    collapsingToolbar.setTitleEnabled(true);
                    collapsingToolbar.setTitle("Garima");
                } else if (isShow) {
                        collapsingToolbar.setTitleEnabled(false);
                        collapsingToolbar.setTitle("");
                        collapsingToolbar.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);
                    isShow = false;
                }
            }
        });

    }




    private void addBottomDots() {
        dots = new TextView[ImagesArray.size()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[0]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[0].setTextColor(colorsActive[0]);
    }
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (position == ImagesArray.size() - 1) {
                viewPager.setCurrentItem(position);
            } else {
                viewPager.setCurrentItem(position);

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };



    @Override
protected void onStart(){
        super.onStart();


        }

@Override
public void onPause(){
    mShimmerViewContainer.stopShimmerAnimation();
    viewPager.invalidate();
        super.onPause();

        }

@Override
protected void onStop(){
        super.onStop();
    mShimmerViewContainer.stopShimmerAnimation();
    viewPager.invalidate();

        }

@Override
protected void onDestroy(){
        super.onDestroy();
    mShimmerViewContainer.stopShimmerAnimation();

        }

@Override
protected void onResume() {
    super.onResume();

    mShimmerViewContainer.setVisibility(View.VISIBLE);
    mShimmerViewContainer.startShimmerAnimation();

    if (_phoneNo != null) {
        if (pref.getName() != null) {
                txtName.setText(String.valueOf(pref.getName()));
                getmain();
        } else {
            txtName.setText(_phoneNo);
        }
        if (pref.getProfile() != null) {
            Picasso
                    .with(Mainactivity.this)
                    .load(pref.getProfile())
                    .resize(72, 72)
                    .centerInside() // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                    .into(profile_image);
        }
    }
}






    private void getmain(){
        ImagesArray.clear();
        CanteenArray.clear();
        StringRequest eventoReq = new StringRequest(com.android.volley.Request.Method.POST, Config_URL.GET_ALL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.w("volley", response);

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray items = jsonObj.getJSONArray("items");
                            JSONArray ads = jsonObj.getJSONArray("ads");

                            if (ads.length() != 0) {
                                for (int i = 0; i < ads.length(); i++) {
                                    JSONObject c = ads.getJSONObject(i);
                                    if (!c.isNull("Photo")) {
                                        ImagesArray.add(c.getString("Photo"));
                                    }

                                }
                            }
                            if (items.length() != 0) {
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject c = items.getJSONObject(i);
                                    if (!c.isNull("Name")) {
                                        main item = new main();
                                        item.setID(c.getInt("ID"));
                                        item.setName(c.getString("Name"));
                                        item.setPhoto(c.getString("Photo"));
                                        item.setPrice(c.getDouble("Price"));
                                        item.setDiscount(c.getDouble("Discount"));
                                        item.setFinal_Price(c.getDouble("Final_Price"));
                                        CanteenArray.add(item);
                                    }

                                }
                            }

                        } catch (final JSONException e) {
                            Log.e("HI", "Json parsing error: " + e.getMessage());
                        }
                         if (ImagesArray.size() > 0) {


                            pref.setCount(ImagesArray.size());
                            addBottomDots();
                            viewPager.setVisibility(View.VISIBLE);
                            MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
                            viewPager.setAdapter(myViewPagerAdapter);
                            viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == ImagesArray.size()) {
                                        currentPage = 0;
                                    }
                                    viewPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 3000, 2000);

                        }
                        if (CanteenArray.size() > 0) {
                            _moreRv.setVisibility(View.VISIBLE);
                             MainAdapter sAdapter1 = new MainAdapter(Mainactivity.this, CanteenArray);
                                sAdapter1.notifyDataSetChanged();
                                sAdapter1.setPref(pref);
                                sAdapter1.setValues(_moneyValue, _itemValue);
                                sAdapter1.setLinearLayout_item(layoutBottomSheet);
                                sAdapter1.setButton(_checkout);
                                sAdapter1.setPhone(_phoneNo);
                                sAdapter1.setHasStableIds(true);
                                sAdapter1.setHasStableIds(true);
                            _moreRv.setAdapter(sAdapter1);
                            _moreRv.setHasFixedSize(true);
                            StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(getSpan(), StaggeredGridLayoutManager.VERTICAL);
                            //LinearLayoutManager horizontalLayoutManagae= new LinearLayoutManager(Mainactivity.this, RecyclerView.VERTICAL, false);
                            _moreRv.setLayoutManager(mLayoutManager);
                            _moreRv.setItemAnimator(new DefaultItemAnimator());
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("uuu", "Error: " + error.getMessage());
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

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("_mId", _phoneNo);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(eventoReq);

    }

    public int getSpan() {
        int orientation = getScreenOrientation(getApplicationContext());
        if (isTV(getApplicationContext())) return 2;
        if (isTablet(getApplicationContext()))
            return orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 2;
        return orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 2;
    }

    public static boolean isTV(Context context) {
        return ((UiModeManager) context
                .getSystemService(Context.UI_MODE_SERVICE))
                .getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
    }

    public static int getScreenOrientation(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels <
                context.getResources().getDisplayMetrics().heightPixels ?
                Configuration.ORIENTATION_PORTRAIT : Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.checkout:

                    Intent o = new Intent(Mainactivity.this, CheckOut.class);
                    startActivity(o);
                    finish();
                    overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);

                break;
        }

    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        MyViewPagerAdapter() {

        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert layoutInflater != null;
            View view = layoutInflater.inflate(R.layout.welcome_slide1, container, false);
            final ImageView imageView = view
                    .findViewById(R.id.wlcm1);


            if(ImagesArray.size()!=0) {

                Picasso.Builder builder = new Picasso.Builder(Mainactivity.this);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(ImagesArray.get(position)).into(imageView);
            }

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return pref.getCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }



        @Override
        public void registerDataSetObserver(@NonNull DataSetObserver observer) {
            super.registerDataSetObserver(observer);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
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
                            Intent o = new Intent(Mainactivity.this, Update_profile.class);
                            o.putExtra("mylat", My_lat);
                            o.putExtra("mylong", My_long);
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
                            Intent o2 = new Intent(Mainactivity.this, Refer_and_Earn_page.class);
                            o2.putExtra("mylat", My_lat);
                            o2.putExtra("mylong", My_long);
                            startActivity(o2);
                            finish();
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        } else {
                            Intent o = new Intent(Mainactivity.this, SmsActivity.class);
                            o.putExtra("mylat", My_lat);
                            o.putExtra("mylong", My_long);
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
            if(!Mainactivity.this.isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Mainactivity.this, R.style.AlertDialogTheme)
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






