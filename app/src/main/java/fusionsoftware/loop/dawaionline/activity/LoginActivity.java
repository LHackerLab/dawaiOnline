package fusionsoftware.loop.dawaionline.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.Contants;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;

/**
 * Created by LALIT on 8/9/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_proceed, phone_icon, tv_or, tv_see_menu_icon,  SeeMenu;
    Typeface materialdesignicons_font, medium, regular, italic, bold, nova;
    EditText edt_phone;
    private Boolean CheckOrientation = false;
    String phone;
    LinearLayout layout_seeMenu;
//    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);// Removes action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);// Removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_login);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
//        checkRuntimePermission();
        init();//component intilization
        setIcon();//set icon in textview
    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(LoginActivity.this)) {
            CheckOrientation = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            CheckOrientation = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    //set icons
    private void setIcon() {
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        phone_icon.setTypeface(materialdesignicons_font);
        phone_icon.setText(Html.fromHtml("&#xf3f2;"));
        medium = FontManager.getFontTypeface(this, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(this, "fonts/roboto.regular.ttf");
        italic = FontManager.getFontTypeface(this, "fonts/roboto.italic.ttf");
        bold = FontManager.getFontTypeface(this, "fonts/roboto.bold.ttf");
        nova = FontManager.getFontTypeface(this, "fonts/ProximaNova-Regular.otf");
        tv_proceed.setTypeface(nova);
//        tv_description.setTypeface(nova);
        edt_phone.setTypeface(nova);
        tv_or.setTypeface(nova);
        tv_see_menu_icon.setTypeface(materialdesignicons_font);
        tv_see_menu_icon.setText(Html.fromHtml("&#xf4a3;"));
//        SeeMenu.setTypeface(nova);
    }

    //intilization.............
    private void init() {
        tv_proceed = (TextView) findViewById(R.id.tv_proceed);
        phone_icon = (TextView) findViewById(R.id.phone_icon);
//        tv_description = (TextView) findViewById(R.id.tv_description);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        tv_or = (TextView) findViewById(R.id.tv_or);
        tv_see_menu_icon = (TextView) findViewById(R.id.tv_see_menu_icon);
//        SeeMenu = (TextView) findViewById(R.id.tv_see_menu_text);
        layout_seeMenu = (LinearLayout) findViewById(R.id.layout_seeMenu);
        tv_proceed.setOnClickListener(this);
        layout_seeMenu.setOnClickListener(this);
    }

    //onclick................
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_proceed:
                loginWithPhone();//sign up with phone method.......
                break;
//            case R.id.layout_facebook:
//                facebookLogin();
//                break;
//            case R.id.layout_seeMenu:
//                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
//                break;
        }
    }

    //sign up with phone method.......
    private void loginWithPhone() {
        if (validation()){
            if (Utility.isOnline(LoginActivity.this)){
               BallTriangleDialog dialog=new BallTriangleDialog(LoginActivity.this);
                dialog.show();
                ServiceCaller serviceCaller=new ServiceCaller(LoginActivity.this);
                serviceCaller.callLoginService(phone, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        dialog.dismiss();
                        if (isComplete){
                            Toast.makeText(LoginActivity.this, Contants.SEND_MESSAGE, Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences("PhoneNumber", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("p_numb", phone);
                            editor.apply();
                            Intent intent=new Intent(LoginActivity.this, OTPVerifyActivity.class);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                            edt_phone.setText("");
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
            else {
                Toast.makeText(this, Contants.OFFLINE_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
        }

//        if (validation()) {
            //check internet connection
//            if (Utility.isOnline(this)) {
//                final BallTriangleDialog dotDialog = new BallTriangleDialog(LoginActivity.this);
//                dotDialog.show();
//                ServiceCaller serviceCaller = new ServiceCaller(this);
//                serviceCaller.callLoginService(phone, new IAsyncWorkCompletedCallback() {
//                    @Override
//                    public void onDone(String workName, boolean isComplete) {
////                        Toast.makeText(LoginActivity.this, workName, Toast.LENGTH_SHORT).show();
//                        if (isComplete) {
//                            //sendDeviceTokenRegistrationToServer();
////                            Toast.makeText(LoginActivity.this, Contants.SEND_MESSAGE, Toast.LENGTH_LONG).show();
////                            Intent intent = new Intent(LoginActivity.this, OTPVerifyActivity.class);
////                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                            startActivity(intent);
////                        } else {
//                            Toast.makeText(LoginActivity.this, "User Skip To Menu For Demo", Toast.LENGTH_SHORT).show();
////                            Utility.alertForErrorMessage(Contants.Dont_SEND_MESSAGE, LoginActivity.this);
//                        }
//                        if (dotDialog.isShowing()) {
//                            dotDialog.dismiss();
//                        }
//                    }
//                });
//            } else {
//                Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, this);//off line msg....
//            }
//        }
    }

    //validation for phone
    private boolean validation() {
        phone = edt_phone.getText().toString();
        if (phone.isEmpty()) {
            edt_phone.setError("Please Enter Mobile Number");
            return false;
        } else if (phone.length() != 10) {
            edt_phone.setError("Please Enter Valid  Mobile Number");
            return false;
        }
        return true;
    }

    //for hid keyboard when tab outside edittext box
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    //check storage and camera run time permission
//    private Boolean checkRuntimePermission() {
//        List<String> permissionsNeeded = new ArrayList<String>();
//
//        final List<String> permissionsList = new ArrayList<String>();
//        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
//            permissionsNeeded.add("Send SMS");
//        if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS))
//            permissionsNeeded.add("Receive SMS");
//        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
//            permissionsNeeded.add("Read Phone State");
//        if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
//            permissionsNeeded.add("Read SMS");
//
//        if (permissionsList.size() > 0) {
//            if (permissionsNeeded.size() > 0) {
                // Need Rationale
//                String message = "You Need To Grant Access To " + permissionsNeeded.get(0);
//                for (int i = 1; i < permissionsNeeded.size(); i++) {
//                    message = message + ", " + permissionsNeeded.get(i);
//                    showMessageOKCancel(message,
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    ActivityCompat.requestPermissions(LoginActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
//                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//                                }
//                            });
//                }
//                return false;
//            }
//            ActivityCompat.requestPermissions(LoginActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
//                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//            return false;
//        }
//        return true;
//    }

    //add run time permission
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, permission))
                return false;
        }
        return true;
    }

    //show permission alert
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
//            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
//                Map<String, Integer> perms = new HashMap<String, Integer>();
//                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
//                for (int i = 0; i < permissions.length; i++)
//                    perms.put(permissions[i], grantResults[i]);
//                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                    selectImage();
//                } else {
//                     Permission Denied
//                    Toast.makeText(LoginActivity.this, "Permission is Denied", Toast.LENGTH_SHORT)
//                            .show();
//
//                }
//            }
//            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}


