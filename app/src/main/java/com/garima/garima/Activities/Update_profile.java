package com.garima.garima.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.squareup.picasso.Picasso;

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

public class Update_profile extends AppCompatActivity implements View.OnClickListener {


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
    private EditText _mobile,_name,_address,_postoffice,_policestation,_bank,_garimaID,_pin;

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

        setContentView(R.layout.profile_update);
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
        _bank = findViewById(R.id.bank);
        _garimaID = findViewById(R.id.garimaID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Update_profile.this, Mainactivity.class);
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
                            if (User_name != null) {
                                _name.setText(User_name);
                            }
                            if (User_address != null && !User_address.contains("null")) {
                                _address.setText(User_address);
                            }
                            if (User_pin != null && !User_pin.contains("null")) {
                                _pin.setText(User_pin);
                            }

                            if (User_postoffice != null && !User_postoffice.contains("null")) {
                                _postoffice.setText(User_postoffice);
                            }
                            if (User_policestation != null && !User_policestation.contains("null")) {
                                _policestation.setText(User_policestation);
                            }
                            if (User_bank != null && !User_bank.contains("null")) {
                                _bank.setText(User_bank);
                            }
                            if (User_garimaID != null && !User_garimaID.contains("null")) {
                                _garimaID.setText(User_garimaID);
                            }

                            if (User_image != null && !TextUtils.isEmpty(User_image)) {
                                Picasso.Builder builder = new Picasso.Builder(Update_profile.this);
                                builder.listener(new Picasso.Listener() {
                                    @Override
                                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                        exception.printStackTrace();
                                    }
                                });
                                builder.build().load(User_image).into(ProfileImage);
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
                    params.put("_mId", _PhoneNo.substring(2));
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(eventoReq);


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

        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Update_profile.this, R.style.AlertDialogTheme);
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


                // Fill with actual results from user
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
                Intent i = new Intent(Update_profile.this,Mainactivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Update_profile.this, Mainactivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.slide_out2, R.anim.slide_in2);
    }


}
