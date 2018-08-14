package fusionsoftware.loop.dawaionline.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.Contants;

public class SplashScreenActivity extends AppCompatActivity {
    private Boolean CheckOrientation = false;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);// Removes action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);// Removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_splash_screen);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        getHSAKey();
        //get firebase token
        waitForLogin(); //wait for 3 seconds
    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(SplashScreenActivity.this)) {
            CheckOrientation = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            CheckOrientation = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    //wait for 3 seconds
    private void waitForLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("NoVerifyOrNotPreferences", Context.MODE_PRIVATE);
                Boolean NoVerify = false;
                if (prefs != null) {
                    NoVerify = prefs.getBoolean("NoVerify", false);
                }
                if (NoVerify) {
                    DbHelper dbhelper = new DbHelper(SplashScreenActivity.this);
                    Data data = dbhelper.getUserData();
                    if (data != null) {
                        Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }, 3000);
    }

    public void getHSAKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(Contants.LOG_TAG, "KeyHash*****:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    //send device token to server
    private void sendDeviceTokenRegistrationToServer() {

        if (token != null && !token.equals("")) {
            final ServiceCaller serviceCaller = new ServiceCaller(SplashScreenActivity.this);
            serviceCaller.callSendDeviceTokenRegistrationToServer(new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        if (Contants.IS_DEBUG_LOG) {
                            Log.d(Contants.LOG_TAG, " FirebaseToken Add successfully*****: " + token);
                        }
                    }
                }
            });
        }
    }
}
