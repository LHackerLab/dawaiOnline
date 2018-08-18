package fusionsoftware.loop.dawaionline.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.adapter.NavMenuCustomAdapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.fragments.MyBasketFragment;
import fusionsoftware.loop.dawaionline.fragments.ParentFragment;
import fusionsoftware.loop.dawaionline.fragments.SelectCategoryFragment;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.ContentDataAsArray;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.MyBasket;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;

import static fusionsoftware.loop.dawaionline.utilities.Utility.isOnline;


public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ListView list_menuitem;
    private Boolean CheckOrientation = false;
    public TextView title, cart, save, username, phoneNumber, cart_dot, logout, LogoutIcon;
    private Typeface regular, materialdesignicons_font, medium;
    DrawerLayout drawer;
    ImageView image_logo, profileImage;
    private LinearLayout logoutLayout;
    public Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chechPortaitAndLandSacpe();//chech Portait And LandSacpe Orientation
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        init(toolbar);
        setIcons();
        Intent intent = getIntent();
        int NavigateValue = intent.getIntExtra("NavigateFlag", 0);
        String orderNumber = intent.getStringExtra("orderNumber");
        int orderId = intent.getIntExtra("orderId", 0);
        int storeId = intent.getIntExtra("storeId", 0);
        int LoginId = intent.getIntExtra("LoginId", 0);
        String storeName = intent.getStringExtra("storeName");
//        if (NavigateValue == 1) {//come from payment and notification  onclick
//            TrackOrderStatusFragment fragment = TrackOrderStatusFragment.newInstance(orderNumber, storeId, orderId, storeName,LoginId);
//            moveFragment(fragment);
//        } else {
        setUpDashboardFragment();
        getCityList();
//        }
        getUserProfileService();//get user profile
        setItemCart();// add item in cart...........
        Listmenu();//list menu
        setUserDetail();
    }

    private void getCityList() {
        final List<String> stringList = new ArrayList<>();
        if (Utility.isOnline(this)) {
            final BallTriangleDialog dotDialog = new BallTriangleDialog(this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(this);
            serviceCaller.callCityService(new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        DbHelper dbHelper = new DbHelper(DashboardActivity.this);
                        List<Result> resultList = dbHelper.GetAllCityData();
                        for (Result result : resultList) {
                            stringList.addAll(Arrays.asList(result.getCityName()));
                        }
                        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(DashboardActivity.this, android.R.layout.simple_spinner_dropdown_item, stringList);
                        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(stringArrayAdapter);
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(this, "No internet connection found", Toast.LENGTH_SHORT).show();
        }
    }


    public void setUserDetail() {
        DbHelper dbHelper = new DbHelper(DashboardActivity.this);
        Data data = dbHelper.getUserData();
        if (data != null) {
            showLogin(false);
            phoneNumber.setText(data.getPhoneNumber());
            username.setText(data.getName());
            String url = data.getProfilePictureUrl();
            Picasso.with(this).load(url).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).into(profileImage);
        } else {
            showLogin(true);
        }
    }

    //add item in  cart or not.........
    public void setItemCart() {
        DbHelper dbHelper = new DbHelper(this);
        List<MyBasket> myBasket = dbHelper.GetAllBasketOrderData();
        if (myBasket != null && myBasket.size() > 0) {
            //for no of added item in basket
            cart_dot.setText(String.valueOf(myBasket.size()));
            cart_dot.setVisibility(View.VISIBLE);
        } else {
            cart_dot.setVisibility(View.GONE);
        }
    }

    //set icons........
    private void setIcons() {
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        medium = FontManager.getFontTypeface(this, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(this, "fonts/roboto.regular.ttf");
        title.setTypeface(medium);
        save.setTypeface(regular);
        cart.setTypeface(materialdesignicons_font);
        cart.setText(Html.fromHtml("&#xf111;"));
        logout.setTypeface(regular);
        LogoutIcon.setTypeface(materialdesignicons_font);
    }

    private void init(Toolbar toolbar) {
        profileImage = (ImageView) findViewById(R.id.profileImage);
        title = (TextView) toolbar.findViewById(R.id.title);
        cart = (TextView) toolbar.findViewById(R.id.cart);
        save = (TextView) toolbar.findViewById(R.id.save);
        image_logo = (ImageView) toolbar.findViewById(R.id.image_logo);
        username = (TextView) findViewById(R.id.username);
        logout = (TextView) findViewById(R.id.logout);
        LogoutIcon = (TextView) findViewById(R.id.LogoutIcon);
        logoutLayout = (LinearLayout) findViewById(R.id.logoutLayout);
        logoutLayout.setOnClickListener(this);
        phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        cart_dot = (TextView) findViewById(R.id.cart_dot);
        spinner = findViewById(R.id.spinner);
        cart.setOnClickListener(this);
    }

    //chech Portait And LandSacpe Orientation
    public void chechPortaitAndLandSacpe() {
        if (CompatibilityUtility.isTablet(DashboardActivity.this)) {
            CheckOrientation = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            CheckOrientation = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    //open default fragment
    private void setUpDashboardFragment() {
        ParentFragment fragment = ParentFragment.newInstance(0, 0);
        moveFragment(fragment);
    }

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                //.addToBackStack(null)
                .commit();
    }

    //list in menu..
    private void Listmenu() {
        list_menuitem = (ListView) findViewById(R.id.list_menu);
        List<String> icondatalist = new ArrayList<>();
        icondatalist.add(String.valueOf(Html.fromHtml("&#xf2dc;")));
        // icondatalist.add(String.valueOf(Html.fromHtml("&#xf09c;")));
//        icondatalist.add(String.valueOf(Html.fromHtml("&#xF34e;")));
//        icondatalist.add(String.valueOf(Html.fromHtml("&#xf2d1;")));
        icondatalist.add(String.valueOf(Html.fromHtml("&#xf5f8;")));
        icondatalist.add(String.valueOf(Html.fromHtml("&#xf53e;")));
        icondatalist.add(String.valueOf(Html.fromHtml("&#xf2da;")));
        icondatalist.add(String.valueOf(Html.fromHtml("&#xF076;")));
        icondatalist.add(String.valueOf(Html.fromHtml("&#xf004;")));
        List<String> stringList = new ArrayList<>();
        stringList.add("Home");
        // stringList.add("Notifications");
//        stringList.add("Change Location");
//        stringList.add("My Favourite Stores");
        stringList.add("My Addresses");
        stringList.add("Track My Order");
        stringList.add("My Order History");
        stringList.add("My Basket");
        stringList.add("My Profile");
        //..................
        NavMenuCustomAdapter custom_adapter = new NavMenuCustomAdapter(DashboardActivity.this, stringList, icondatalist);
        list_menuitem.setAdapter(custom_adapter);
    }

    public void showLogin(boolean showALogin) {
        if (showALogin) {
            LogoutIcon.setText(Html.fromHtml("&#xf342;"));
        } else {
            LogoutIcon.setText(Html.fromHtml("&#xf343;"));
        }
    }

    // set titiles........
    public void setScreenTitle(String heading) {
        this.setTitle(heading);
        title.setText(heading);
    }

    // set card icon.....
    public void setScreencart(boolean screencart) {
        if (screencart) {
            this.cart.setVisibility(View.VISIBLE);
        } else {
            this.cart.setVisibility(View.GONE);
        }
    }

    //set save ....
    public void setScreenSave(boolean screenSave) {
        if (screenSave) {
            this.save.setVisibility(View.VISIBLE);
        } else {
            this.save.setVisibility(View.GONE);
        }


    }


    //set cart dot....
    public void setScreenCartDot(boolean screenCartDot) {
        if (screenCartDot) {
            this.cart_dot.setVisibility(View.VISIBLE);
        } else {
            this.cart_dot.setVisibility(View.GONE);
        }


    }


    @Override
    public void onBackPressed() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag("OrderPlacedFragment");
        if (fragment != null) {//order placed done so start flow again
            Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    //get user profile
    private void getUserProfileService() {
        if (isOnline(DashboardActivity.this)) {
            DbHelper dbHelper = new DbHelper(DashboardActivity.this);
            Data data = dbHelper.getUserData();
            if (data != null) {
                ServiceCaller serviceCaller = new ServiceCaller(DashboardActivity.this);
                serviceCaller.getUserProfileService(data.getLoginID(), new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                    }
                });
            }
        }
    }

    //hide navigation view
    public void NavHide() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyBasketFragment()).addToBackStack(null).commit();
                break;
            case R.id.logoutLayout:
                logout();
                break;
        }
    }

    private void logout() {
        DbHelper dbHelper = new DbHelper(DashboardActivity.this);
        Data data = dbHelper.getUserData();
        if (data != null) {
            deleteUserData();// delete user details from database............
        } else {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));// move login activity.....
        }
    }


    // delete user details from database............
    private void deleteUserData() {
        final DbHelper dbHelper = new DbHelper(DashboardActivity.this);
        Data data = dbHelper.getUserData();
        final int loginId = data.getLoginID();
        new AlertDialog.Builder(DashboardActivity.this)
                .setTitle("Logout")
                .setCancelable(false)
                .setMessage("Would you like to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // logout
                        dbHelper.deleteUserData(loginId);
                        dbHelper.deleteAllBasketOrderData();
                        dbHelper.deleteMyAllOrderHistoryData();
                        dbHelper.deleteTrackOrderData();
                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        showLogin(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // user doesn't want to logout
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
