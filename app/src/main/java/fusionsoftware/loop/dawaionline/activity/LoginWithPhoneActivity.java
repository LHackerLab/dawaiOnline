package fusionsoftware.loop.dawaionline.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
public class LoginWithPhoneActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_proceed, phone_icon, see_memu_icon, tv_seeMenu, tv_hint_phone, tv_skipToMenu, tv_description;
    Typeface materialdesignicons_font, medium, regular, italic;
    EditText edt_phone;
    private Boolean CheckOrientation = false;
    String phone;
    LinearLayout see_menu;
    String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);// Removes action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);// Removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_with_phone);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        init();//component intilization
        setIcon();//set icon in textview
    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(LoginWithPhoneActivity.this)) {
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
        see_memu_icon.setTypeface(materialdesignicons_font);
        see_memu_icon.setText(Html.fromHtml("&#xf4a3;"));
        medium = FontManager.getFontTypeface(this, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(this, "fonts/roboto.regular.ttf");
        italic = FontManager.getFontTypeface(this, "fonts/roboto.italic.ttf");
        tv_hint_phone.setTypeface(regular);
        tv_proceed.setTypeface(medium);
        tv_seeMenu.setTypeface(medium);
        tv_skipToMenu.setTypeface(regular);
        tv_description.setTypeface(italic);
        edt_phone.setTypeface(regular);

    }

    //intilization.............
    private void init() {
        tv_proceed = (TextView) findViewById(R.id.tv_proceed);
        phone_icon = (TextView) findViewById(R.id.phone_icon);
        see_memu_icon = (TextView) findViewById(R.id.see_memu_icon);
        tv_seeMenu = (TextView) findViewById(R.id.tv_see_memu);
        tv_hint_phone = (TextView) findViewById(R.id.tv_hint_phone);
        tv_skipToMenu = (TextView) findViewById(R.id.tv_skipToMenu);
        tv_description = (TextView) findViewById(R.id.tv_description);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        see_menu = (LinearLayout) findViewById(R.id.see_memu);
        tv_proceed.setOnClickListener(this);
        see_menu.setOnClickListener(this);
    }

    //onclick................
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_proceed:
                loginWithPhone();//sign up with phone method.......
                break;
            case R.id.see_memu:
                startActivity(new Intent(LoginWithPhoneActivity.this, DashboardActivity.class));
                break;
        }
    }

    //sign up with phone method.......
    private void loginWithPhone() {
        phone = edt_phone.getText().toString();
        if (validation()) {
            //check internet connection
            if (Utility.isOnline(this)) {
                final BallTriangleDialog dotDialog = new BallTriangleDialog(LoginWithPhoneActivity.this);
                dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(this);
                serviceCaller.callLoginService(phone, token, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        if (isComplete) {
                            Toast.makeText(LoginWithPhoneActivity.this, Contants.SEND_MESSAGE, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginWithPhoneActivity.this, OTPVerifyActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Utility.alertForErrorMessage(Contants.Dont_SEND_MESSAGE, LoginWithPhoneActivity.this);
                        }
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                    }
                });
            } else {
                Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, this);//off line msg....
            }
        }
    }

    //validation for phone
    private boolean validation() {
        //  String phone = edt_phone.getText().toString();
        if (phone.isEmpty()) {
            edt_phone.setError("Please Enter Mobile Number");
            return false;
        } else if (phone.length() != 10) {
            edt_phone.setError("Please Enter Valid  Mobile Number 10 Digits");
            return false;
        } /*else if (phone.length() > 10) {
            edt_phone.setError("Enter Valid Mobile Number 10 Digits");
            return false;
        }*/
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

}


