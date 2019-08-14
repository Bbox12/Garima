package com.garima.garima.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;



/**
 * Created by parag on 29/12/17.
 */

public class Update_form extends AppCompatActivity implements View.OnClickListener {


    public static final int MULTIPLE_PERMISSIONS = 10;
    private static final String TAG = SmsActivity.class.getSimpleName();
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private ImageView ProfileImage;
    private String Name, _PhoneNo;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private String Name_giver = "";
    private boolean permissionsAllowd = false;
    private String _PhoneNo_giver = "";
    private double My_lat = 0, My_long = 0;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private PrefManager pref;
    private String User_image, User_name,User_postoffice,User_policestation,User_address,User_bank,User_pin,User_garimaID;
    private String filePath_;
    private CoordinatorLayout coordinatorLayout;
    private EditText _mobile,_name,_address,_postoffice,_policestation,_garimaID,_pin,
    son_of,nominee,age,experience,panchayat,district,nationality,acno,ifsc,bname,branchname,tmembers,tadults,tminors,thandicaped,tolders;
    private AutoCompleteTextView relationship,education,caste;
    private String _relationship,_caste,_education;
    private boolean valid=false;

    private static boolean isValidPhoneNumber(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            check = phone.length() >= 6 && phone.length() <= 12 && phone.length() >= 12;
        } else {
            check = false;
        }
        return check;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        My_lat = i.getDoubleExtra("mylat", 0);
        My_long = i.getDoubleExtra("mylong", 0);

        setContentView(R.layout.update_form);
        coordinatorLayout=findViewById(R.id.coll);
        progressBar=findViewById(R.id.progressBar2);
        ProfileImage = findViewById(R.id.profile_picture);
        ProfileImage.setOnClickListener(this);
        cd = new ConnectionDetector(getApplicationContext());
        pref = new PrefManager(getApplicationContext());
        HashMap<String, String> user = pref.getUserDetails();
        _PhoneNo_giver = user.get(PrefManager.KEY_MOBILE);
        toolbar = findViewById(R.id.toolbar_mode);
        _PhoneNo = user.get(PrefManager.KEY_MOBILE);
         Name = user.get(PrefManager.KEY_IMEI);
        _name = findViewById(R.id.name);
        _pin = findViewById(R.id.pin);
        _mobile = findViewById(R.id.p_mobile);
        _address = findViewById(R.id.address);
        _policestation= findViewById(R.id.policestation);
        _postoffice = findViewById(R.id.postoffice);
        bname = findViewById(R.id.bname);
        branchname=findViewById(R.id.branchname);
        son_of=findViewById(R.id.son_of);
        nominee=findViewById(R.id.nominee);
        age=findViewById(R.id.age);
        experience=findViewById(R.id.experience);
        panchayat=findViewById(R.id.panchayat);
        district=findViewById(R.id.district);
        nationality=findViewById(R.id.nationality);
        tolders=findViewById(R.id.told);
        acno=findViewById(R.id.acno);
        ifsc=findViewById(R.id.ifsc);
        tmembers=findViewById(R.id.tmember);
        tadults=findViewById(R.id.tadults);
        tminors=findViewById(R.id.tminors);
        thandicaped=findViewById(R.id.thandi);
        relationship=findViewById(R.id.relationship);
        education=findViewById(R.id.educational);
        caste=findViewById(R.id.caste);
        relationship.setOnClickListener(this);
        education.setOnClickListener(this);
        caste.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Update_form.this, Mainactivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
            }
        });
        if (_PhoneNo != null) {
            _mobile.setText(_PhoneNo);
            progressBar.setVisibility(View.VISIBLE);
            StringRequest eventoReq = new StringRequest(com.android.volley.Request.Method.POST, Config_URL.GET_DETAILS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.w("volley", response);


                            try {

                                JSONObject jsonObj = new JSONObject(response);
                                JSONArray login = jsonObj.getJSONArray("login");





                                for (int i = 0; i < login.length(); i++) {
                                    JSONObject c = login.getJSONObject(i);

                                    if (!c.isNull("Phone_No")) {
                                        User_image = c.getString("Photo");
                                        User_name = c.getString("Name");
                                        User_address = c.getString("Address");
                                        User_postoffice = c.getString("Postoffice");
                                        User_policestation = c.getString("Policestation");
                                        User_bank = c.getString("Bank_name");
                                        User_pin = c.getString("Pin");
                                        User_garimaID = c.getString("gID");
                                    }
                                }



                            } catch (final JSONException e) {
                                Log.e("HI", "Json parsing error: " + e.getMessage());


                            }

                            progressBar.setVisibility(View.GONE);
                            ArrayList<String>mRelation=new ArrayList<String>();
                            ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getBaseContext(),
                                    android.R.layout.simple_list_item_1, mRelation);

                            final String[] selection = new String[1];
                            relationship.setAdapter(mAdapter);
                            relationship.setCursorVisible(false);
                            relationship.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    relationship.showDropDown();
                                    selection[0] = (String) parent.getItemAtPosition(position);
                                    _relationship=selection[0];

                                }
                            });

                            ArrayList<String>mCaste=new ArrayList<String>();
                            ArrayAdapter<String> mAdapter1 = new ArrayAdapter<String>(getBaseContext(),
                                    android.R.layout.simple_list_item_1, mCaste);

                            final String[] selection1 = new String[1];
                            caste.setAdapter(mAdapter);
                            caste.setCursorVisible(false);
                            caste.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    caste.showDropDown();
                                    selection1[0] = (String) parent.getItemAtPosition(position);
                                    _caste=selection1[0];

                                }
                            });

                            ArrayList<String>mEducation=new ArrayList<String>();
                            ArrayAdapter<String> mAdapter2 = new ArrayAdapter<String>(getBaseContext(),
                                    android.R.layout.simple_list_item_1, mEducation);

                            final String[] selection2 = new String[1];
                            education.setAdapter(mAdapter);
                            education.setCursorVisible(false);
                            education.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    education.showDropDown();
                                    selection2[0] = (String) parent.getItemAtPosition(position);
                                    _education=selection2[0];

                                }
                            });


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("uuu", "Error: " + error.getMessage());
                    volley_error(error);


                }

            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("_mId", _PhoneNo.substring(2));
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(eventoReq);


        }
    }

    private void volley_error(VolleyError error) {
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
    protected void onResume() {
        super.onResume();
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            ProfileImage.setOnClickListener(this);
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "No Internet,Connect to internet", Snackbar.LENGTH_INDEFINITE).show();

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.profile_picture:
                if (permissionsAllowd) {
                    selectImage();
                } else {
                    checkAndRequestPermissions();
                }
                break;

            case R.id.relationship:
                relationship.showDropDown();

                break;

            case R.id.caste:
                caste.showDropDown();
                break;

            case R.id.educational:
                education.showDropDown();

                break;


        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Update_form.this, R.style.AlertDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ProfileImage.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProfileImage.setImageBitmap(thumbnail);
    }

    private void checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);

        } else {
            permissionsAllowd = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        selectImage();

                    }
                }
            }
            break;

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save:
                if(valid_form()){
                    StringRequest eventoReq = new StringRequest(com.android.volley.Request.Method.POST, Config_URL.POST_MEMBER_DATA,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.w("volley", response);

                                    String[] pars = response.split("error");
                                    boolean success = pars[1].contains("false");
                                    progressBar.setVisibility(View.GONE);
                                    if (success) {
                                        Intent i = new Intent(Update_form.this,Mainactivity.class);
                                        startActivity(i);
                                        finish();
                                        overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d("uuu", "Error: " + error.getMessage());
                            volley_error(error);
                        }

                    }){
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name",  _name.getText().toString());
                            params.put("Mobile",_mobile.getText().toString());
                            params.put("Relative_name",  son_of.getText().toString());
                            params.put("Caste",caste.getText().toString());
                            params.put("Education",  education.getText().toString());
                            params.put("Address",_address.getText().toString());
                            params.put("Postoffice",_postoffice.getText().toString());
                            params.put("Policestation",_policestation.getText().toString());
                            params.put("Bank_name",  bname.getText().toString());
                            params.put("Pin",  _pin.getText().toString());
                            params.put("ward",panchayat.getText().toString());
                            params.put("nation", "Indian");
                            params.put("acc_no",acno.getText().toString());
                            params.put("branch",  branchname.getText().toString());
                            params.put("ifsc",ifsc.getText().toString());
                            params.put("nominee",nominee.getText().toString());
                            params.put("relation",  relationship.getText().toString());
                            params.put("age",age.getText().toString());
                            params.put("as_on",  "2019");
                            params.put("experience",experience.getText().toString());
                            params.put("district",district.getText().toString());
                            params.put("Total_family_member",tmembers.getText().toString());
                            params.put("Total_family_adults",  tadults.getText().toString());
                            params.put("Total_family_minors",tminors.getText().toString());
                            params.put("Total_family_seniors",  tolders.getText().toString());
                            params.put("Total_family_handicapped",  thandicaped.getText().toString());
                            return params;
                        }

                    };
                    AppController.getInstance().addToRequestQueue(eventoReq);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean valid_form() {

        if(TextUtils.isEmpty(son_of.getText().toString())){
            if(!TextUtils.isEmpty(nominee.getText().toString())){
                if(!TextUtils.isEmpty(age.getText().toString())){
                    if(!TextUtils.isEmpty(caste.getText().toString())){
                        if(!TextUtils.isEmpty(education.getText().toString())){
                            if(!TextUtils.isEmpty(experience.getText().toString())){
                                if(!TextUtils.isEmpty(_address.getText().toString())){
                                    if(!TextUtils.isEmpty(panchayat.getText().toString())){
                                        if(!TextUtils.isEmpty(district.getText().toString())){
                                            if(!TextUtils.isEmpty(_postoffice.getText().toString())){
                                                if(!TextUtils.isEmpty(_policestation.getText().toString())){
                                                    if(!TextUtils.isEmpty(_pin.getText().toString())){
                                                        if(!TextUtils.isEmpty(acno.getText().toString())){
                                                            if(!TextUtils.isEmpty(bname.getText().toString())){
                                                                if(!TextUtils.isEmpty(branchname.getText().toString())){
                                                                    if(!TextUtils.isEmpty(ifsc.getText().toString())){
                                                                        if(TextUtils.isEmpty(tmembers.getText().toString())){
                                                                            tmembers.setText("0");
                                                                        }
                                                                        if(TextUtils.isEmpty(tolders.getText().toString())){
                                                                            tmembers.setText("0");
                                                                        }
                                                                        if(TextUtils.isEmpty(thandicaped.getText().toString())){
                                                                            tmembers.setText("0");
                                                                        }
                                                                        if(TextUtils.isEmpty(tminors.getText().toString())){
                                                                            tmembers.setText("0");
                                                                        }
                                                                        if(TextUtils.isEmpty(tadults.getText().toString())){
                                                                            tmembers.setText("0");
                                                                        }
                                                                        valid=true;
                                                                    }else {
                                                                        ifsc.setError("Empty");
                                                                    }
                                                                }else {
                                                                    branchname.setError("Empty");
                                                                }
                                                            }else {
                                                                bname.setError("Empty");
                                                            }
                                                        }else {
                                                            acno.setError("Empty");
                                                        }
                                                    }else {
                                                        _pin.setError("Empty");
                                                    }
                                                }else {
                                                    _policestation.setError("Empty");
                                                }
                                            }else {
                                                _postoffice.setError("Empty");
                                            }
                                        }else {
                                            district.setError("Empty");
                                        }
                                    }else {
                                        panchayat.setError("Empty");
                                    }
                                }else {
                                    _address.setError("Empty");
                                }
                            }else {
                                experience.setError("Empty");
                            }
                        }else {
                            education.setError("Empty");
                        }
                    }else {
                        caste.setError("Empty");
                    }
                }else {
                    age.setError("Empty");
                }
            }else {
                nominee.setError("Empty");
            }
        }else {
            son_of.setError("Empty");
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Update_form.this, Mainactivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
    }


}
