package com.garima.garima.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

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
import com.garima.garima.helper.PrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by parag on 30/08/16.
 */
public class Splash_screen extends AppCompatActivity {
    public static final int MULTIPLE_PERMISSIONS = 10;
    private static final String TAG = Splash_screen.class.getSimpleName();

    Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private PrefManager pref;
    private double My_lat = 0, My_long = 0;
    private boolean permissionsAllowd;
    private RelativeLayout Splash;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private boolean dont=false;
    private int Online;
    private String _phoneNo;
    private boolean launced=false;
    private String regId;
    private double User_review = 0;
    private String User_image,User_name;
    private String User_ID;
    private boolean going=false;
    private int version_=1,imp=0;
    private int version_1=0,_imp_1=0;
    private TextView _t2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        setContentView(R.layout.splash_screen);
        cd = new ConnectionDetector(getApplicationContext());

        _phoneNo = user.get(PrefManager.KEY_MOBILE);
        progressBar = findViewById(R.id.progressBarSplash);
        coordinatorLayout = findViewById(R.id
                .cor_home_main);

        _t2=findViewById(R.id._t2);


        String html = "<a href=\"http://www.theprognosis.in\">Prognosis</a>";
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        _t2.setText(result);
        _t2. setMovementMethod(LinkMovementMethod.getInstance());

    }
    Thread splashTread;
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l = findViewById(R.id.splash);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();


        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                  launchHomeScreen();
                } catch (InterruptedException e) {
                    // do nothing
                }

            }
        };
        splashTread.start();
    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

              StartAnimations();


    }




    private void launchHomeScreen() {
            if (_phoneNo != null) {

                StringRequest eventoReq = new StringRequest(com.android.volley.Request.Method.POST, Config_URL.GET_DETAILS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.w("volley", response);


                                try {

                                    JSONObject jsonObj = new JSONObject(response);
                                    JSONArray login = jsonObj.getJSONArray("login");

                                    JSONArray version = jsonObj.getJSONArray("version");

                                    for (int i = 0; i < version.length(); i++) {
                                        JSONObject c = version.getJSONObject(i);

                                        if (!c.isNull("Version")) {
                                            version_1=((c.getInt("Version")));
                                            _imp_1=(c.getInt("Importance"));
                                            pref.setDate(c.getString("Date"));
                                        }
                                    }



                                    for (int i = 0; i < login.length(); i++) {
                                        JSONObject c = login.getJSONObject(i);

                                        if (!c.isNull("Phone_No")) {
                                            pref.setProfile(c.getString("Photo"));
                                            pref.setName(c.getString("Name"));
                                            pref.setID1(c.getInt("ID"));
                                            pref.setcAddress(c.getString("Address"));
                                            pref.setReferalCode(c.getString("Reference_Code"));
                                            pref.setUser_refernce_code_coupon_Amt(c.getInt("User_refernce_code_coupon_Amt"));
                                        }
                                    }



                                } catch (final JSONException e) {
                                    Log.e("HI", "Json parsing error: " + e.getMessage());


                                }

                                progressBar.setVisibility(View.GONE);
                                go_trough();

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
                        params.put("_mId", _phoneNo.substring(2));
                        return params;
                    }

                };
                AppController.getInstance().addToRequestQueue(eventoReq);

            } else {

                Intent o = new Intent(Splash_screen.this, SmsActivity.class);
                o.putExtra("mylat", My_lat);
                o.putExtra("mylong", My_long);
                startActivity(o);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }


    }



    private void go_trough() {
        if (_imp_1 == 1) {
            if (!Splash_screen.this.isFinishing()) {
                new AlertDialog.Builder(Splash_screen.this, R.style.AlertDialogTheme)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("Update Garima")
                        .setMessage("A new version of Garima is available!")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        } else {

            if (version_1!= version_) {
                if (!Splash_screen.this.isFinishing()) {
                    new AlertDialog.Builder(Splash_screen.this, R.style.AlertDialogTheme)
                            .setTitle("Its time to update Garima")
                            .setMessage("A new version of Garima is available!")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    launchHomeScreen();

                                    dialog.cancel();
                                }
                            })
                            .show();
                }
            } else {
                go_again();
            }
        }
    }

    private void go_again() {



        if(pref.getRegID()!=null) {
            Intent i = new Intent(Splash_screen.this, Mainactivity.class);
            i.putExtra("my_lat", My_lat);
            i.putExtra("my_long", My_long);
            startActivity(i);
            overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
        }else{
            Intent i = new Intent(Splash_screen.this, Update_form.class);
            i.putExtra("my_lat", My_lat);
            i.putExtra("my_long", My_long);
            startActivity(i);
            overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

}




