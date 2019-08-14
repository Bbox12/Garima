package com.garima.garima.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.garima.garima.Adapters.memberRv;
import com.garima.garima.Model.Members;
import com.garima.garima.R;
import com.garima.garima.helper.AppController;
import com.garima.garima.helper.Config_URL;
import com.garima.garima.helper.PrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Remove_Member extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView MemberRv;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private AutoCompleteTextView _responsibility;
    private ProgressBar progressBar;
    private String _phoneNo;
    private PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _phoneNo = user.get(PrefManager.KEY_MOBILE);
        setContentView(R.layout.member_list);
        MemberRv=findViewById(R.id.memberListRv);
        MemberRv.setNestedScrollingEnabled(false);
        coordinatorLayout=findViewById(R.id
                .main_content);
        _responsibility=findViewById(R.id.responsibility);
        _responsibility.setOnClickListener(this);
        progressBar=findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar_logged);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Remove_Member.this, Member_window.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final ArrayList<String> StackHolders=new ArrayList<String>();
        final ArrayList<Members>Members=new ArrayList<Members>();
        StringRequest eventoReq = new StringRequest(com.android.volley.Request.Method.POST, Config_URL.GET_MEMBERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.w("volley", response);


                        try {

                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray Responsibility = jsonObj.getJSONArray("Responsibility");

                            JSONArray members = jsonObj.getJSONArray("members");

                            for (int i = 0; i < Responsibility.length(); i++) {
                                JSONObject c = Responsibility.getJSONObject(i);

                                if (!c.isNull("Category")) {
                                    StackHolders.add((c.getString("Category")));
                                }
                            }



                            for (int i = 0; i < members.length(); i++) {
                                JSONObject c = members.getJSONObject(i);

                                if (!c.isNull("Phone_No")) {
                                    Members item=new Members();
                                    item.setName(c.getString("Name"));
                                    item.setPhoto(c.getString("Photo"));
                                    item.setAddress(c.getString("Address"));
                                    Members.add(item);
                                }
                            }



                        } catch (final JSONException e) {
                            Log.e("HI", "Json parsing error: " + e.getMessage());


                        }

                        progressBar.setVisibility(View.GONE);
                        if(!StackHolders.isEmpty()){
                            ArrayAdapter<String> mAdapter1 = new ArrayAdapter<String>(getBaseContext(),
                                    android.R.layout.simple_list_item_1, StackHolders);

                            final String[] selection1 = new String[1];
                            _responsibility.setAdapter(mAdapter1);
                            _responsibility.setCursorVisible(true);
                            _responsibility.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    _responsibility.showDropDown();
                                    selection1[0] = (String) parent.getItemAtPosition(position);


                                }
                            });
                        }
                        if(!Members.isEmpty()){
                            MemberRv.setVisibility(View.VISIBLE);
                            memberRv sAdapter = new memberRv(Remove_Member.this, Members);
                            sAdapter.notifyDataSetChanged();
                            sAdapter.setFrom(2);
                            MemberRv.setAdapter(sAdapter);
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(Remove_Member.this);
                            mLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            MemberRv.setLayoutManager(mLayoutManager);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode== KeyEvent.KEYCODE_BACK)   {
            if(!Remove_Member.this.isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Remove_Member.this, R.style.AlertDialogTheme)
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.responsibility:
                _responsibility.showDropDown();
                break;
            default:
                break;
        }

    }
}
