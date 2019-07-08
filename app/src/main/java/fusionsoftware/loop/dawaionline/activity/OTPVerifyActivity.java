package fusionsoftware.loop.dawaionline.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.MyPojo;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.Contants;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;

/**
 * Created by LALIT on 8/9/2017.
 */
public class OTPVerifyActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_proceed, lock_icon, tv_hint_otp, tv_dontReciveOtp, tv_resend, tv_description;
    Typeface materialdesignicons_font, medium, regular, italic;
    EditText edt_otp;
    LinearLayout onclick_resend_ly;
    private Boolean CheckOrientation = false;
    private String otpData, phone;
    String token;
    Result results;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);// Removes action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);// Removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_otp_verify);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        Bundle bundle=getIntent().getExtras();
        phone=bundle.getString("phone");
        results=new Result();
        init();//component intilization
        setIcon();//set icon in textview

    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(OTPVerifyActivity.this)) {
            CheckOrientation = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            CheckOrientation = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    //set icon in textview
    private void setIcon() {
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        lock_icon.setTypeface(materialdesignicons_font);
        lock_icon.setText(Html.fromHtml("&#xf341;"));
        medium = FontManager.getFontTypeface(this, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(this, "fonts/roboto.regular.ttf");
        italic = FontManager.getFontTypeface(this, "fonts/roboto.italic.ttf");
        tv_proceed.setTypeface(medium);
        tv_resend.setTypeface(medium);
//        tv_dontReciveOtp.setTypeface(regular);
//        tv_hint_otp.setTypeface(regular);
//        tv_description.setTypeface(italic);
        edt_otp.setTypeface(regular);


    }

    //component intilization
    private void init() {
        tv_proceed = (TextView) findViewById(R.id.tv_proceed);
        lock_icon = (TextView) findViewById(R.id.lock_icon);
//        tv_dontReciveOtp = (TextView) findViewById(R.id.tv_dontReciveOtp);
        tv_resend = (TextView) findViewById(R.id.tv_resend);
//        tv_hint_otp = (TextView) findViewById(R.id.tv_hint_otp);
//        tv_description = (TextView) findViewById(R.id.tv_description);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        onclick_resend_ly = (LinearLayout) findViewById(R.id.resend_layout);
        tv_proceed.setOnClickListener(this);//set onclick
//        onclick_resend_ly.setOnClickListener(this);//set onclick
        // get data from data base........


    }

    //onclick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_proceed:
                if (validation()) {
                    verifyOTP(otpData);
                }
                break;
//            case R.id.resend_layout:
//                resendOtp();//  resend otp....................
//                break;
        }
    }

    //sign up with phone method.......
//    private void resendOtp() {
//        //check online.....
//        if (Utility.isOnline(this)) {
//            DbHelper dbHelper = new DbHelper(OTPVerifyActivity.this);
//            Result data = dbHelper.getUserData();
//            String phone = data.getPhoneNumber();//get phone number.........
//            final BallTriangleDialog dotDialog = new BallTriangleDialog(OTPVerifyActivity.this);
//            dotDialog.show();
//            ServiceCaller serviceCaller = new ServiceCaller(this);
//            serviceCaller.callLoginService(phone, new IAsyncWorkCompletedCallback() {
//                @Override
//                public void onDone(String workName, boolean isComplete) {
//                    if (isComplete) {
//                        Toast.makeText(OTPVerifyActivity.this, Contants.SEND_MESSAGE, Toast.LENGTH_LONG).show();
//                    } else {
//                        Utility.alertForErrorMessage(Contants.Dont_SEND_MESSAGE, OTPVerifyActivity.this);
//                    }
//                    if (dotDialog.isShowing()) {
//                        dotDialog.dismiss();
//                    }
//                }
//            });
//        } else {
//            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, this);//off line msg....
//        }
//
//    }

    //validation for phone
    private boolean validation() {
        otpData = edt_otp.getText().toString();
        if (otpData.length() == 0) {
            Utility.alertForErrorMessage("Please Enter OTP", OTPVerifyActivity.this);
            return false;
        }
        else if (otpData.length()!=4){
            Utility.alertForErrorMessage("Please Enter Valid OTP", OTPVerifyActivity.this);
            return false;
        }
        return true;
    }

    //  resend otp....................
    private void verifyOTP(String otp) {
        if (validation()){
            if (Utility.isOnline(this)){
                BallTriangleDialog dialog=new BallTriangleDialog(OTPVerifyActivity.this);
                dialog.show();
                ServiceCaller serviceCaller=new ServiceCaller(this);
                serviceCaller.callOtpVerifyService(phone, otp, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        dialog.dismiss();
                        if (isComplete){
                            if (!workName.trim().equalsIgnoreCase("no")) {
                                if (!workName.equals("")) {
                                MyPojo myPojo = new Gson().fromJson(workName, MyPojo.class);
                                for (Result result : myPojo.getResult()) {
                                    if (result.getStatus().equalsIgnoreCase("1")) {
                                        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("Login", phone);
                                        editor.apply();
                                        Toast.makeText(OTPVerifyActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(OTPVerifyActivity.this, DashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                        edt_otp.setText("");

                                    } else {
                                        Toast.makeText(OTPVerifyActivity.this, "Something went wrong please contact to Admin", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                                else {
                                    Toast.makeText(OTPVerifyActivity.this, "Please Enter 6 Digit Otp", Toast.LENGTH_SHORT).show();
                                }

                    }

                            else {
                                Toast.makeText(OTPVerifyActivity.this, Contants.Dont_SEND_MESSAGE, Toast.LENGTH_SHORT).show();
                            }
                            
                        }
                        else {
                            Toast.makeText(OTPVerifyActivity.this, Contants.DoNot_SEND_OTP_MESSAGE, Toast.LENGTH_SHORT).show();
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

//        if (Utility.isOnline(this)) {
//            DbHelper dbHelper = new DbHelper(OTPVerifyActivity.this);
//            Result data = dbHelper.getUserData();
//            if (data != null) {
//                final BallTriangleDialog dotDialog = new BallTriangleDialog(OTPVerifyActivity.this);
//                dotDialog.show();
//                String phone = data.getPhoneNumber();
//                ServiceCaller serviceCaller = new ServiceCaller(OTPVerifyActivity.this);
//                serviceCaller.callLoginServiceOTPVerify(otp, phone, new IAsyncWorkCompletedCallback() {
//                    @Override
//                    public void onDone(String workName, boolean isComplete) {
//                        if (isComplete) {
//                            if (workName.equals("success")) {
//                                Utility.setPhoneNoVerifyOrNotSharedPreferences(OTPVerifyActivity.this, true);
//                                //Toast.makeText(OTPVerifyActivity.this, Contants.SEND_OTP_MESSAGE, Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(OTPVerifyActivity.this, DashboardActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
//                        } else {
//                            Utility.alertForErrorMessage(Contants.DoNot_SEND_OTP_MESSAGE, OTPVerifyActivity.this);
//                        }
//                        if (dotDialog.isShowing()) {
//                            dotDialog.dismiss();
//                        }
//                    }
//                });
//            }
//        } else {
//            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, this);//off line msg....
//        }
    }

    //for auto get otp from message
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                String otpMessage = intent.getStringExtra("message");
                //Do whatever you want with the code here
                if (otpMessage != null && !otpMessage.equals("")) {
                    verifyOTP(otpMessage);
                }
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DbHelper dbHelper = new DbHelper(this);
        Result data = dbHelper.getUserData();
        if (data != null) {
            int loginId = data.getLoginId();
            dbHelper.deleteUserData(loginId);
        }
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
//                // Need Rationale
//                String message = "You Need To Grant Access To " + permissionsNeeded.get(0);
//                for (int i = 1; i < permissionsNeeded.size(); i++) {
//                    message = message + ", " + permissionsNeeded.get(i);
//                    showMessageOKCancel(message,
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    ActivityCompat.requestPermissions(OTPVerifyActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
//                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//                                }
//                            });
//                }
//                return false;
//            }
//            ActivityCompat.requestPermissions(OTPVerifyActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
//                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//            return false;
//        }
//        return true;
//    }

    //add run time permission
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(OTPVerifyActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(OTPVerifyActivity.this, permission))
                return false;
        }
        return true;
    }

    //show permission alert
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(OTPVerifyActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
//                Map<String, Integer> perms = new HashMap<String, Integer>();
//                // Initial
//                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
//                // Fill with results
//                for (int i = 0; i < permissions.length; i++) {
//                    perms.put(permissions[i], grantResults[i]);
//                    if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                        // All Permissions Granted
//                        //selectImage();
//                    } else {
//                        // Permission Denied
//                        Toast.makeText(OTPVerifyActivity.this, "Permission is Denied", Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                }
//            }
//            break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

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
}
