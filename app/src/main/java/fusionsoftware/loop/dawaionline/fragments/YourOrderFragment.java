package fusionsoftware.loop.dawaionline.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.adapter.YourOrderAdpater;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.Addresses;
import fusionsoftware.loop.dawaionline.model.ContentData;
import fusionsoftware.loop.dawaionline.model.ContentDataAsArray;
import fusionsoftware.loop.dawaionline.model.CreateOrderDetails;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.MyBasket;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.Contants;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;

/**
 * Created by LALIT on 8/14/2017.
 */
public class YourOrderFragment extends Fragment implements View.OnClickListener {
    private String completeAddress, phone, zipcode;

    public static YourOrderFragment newInstance(String completeAddress, String phone, String zipcode) {
        YourOrderFragment fragment = new YourOrderFragment();
        Bundle args = new Bundle();
        args.putString("completeAddress", completeAddress);
        args.putString("phone", phone);
        args.putString("zipcode", zipcode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            completeAddress = getArguments().getString("completeAddress");
            phone = getArguments().getString("phone");
            zipcode = getArguments().getString("zipcode");
        }
    }


    YourOrderAdpater adapter;
    EditText edt_promoCode;
    View view;
    TextView arrow_icon, tv_continue, your_order, tv_editOrder,tv_promoCode,   tv_specialDiscount_charges, tv_specialDiscount,
            rupee_icon, icon_rupees, tv_rupees_icon, rupees_icon,  tv_done, tv_apply, tv_cancel,  shipping, shipping_charges,
            tv_deliverTo, tv_edit_Icon, tv_deliveryAddress, quantity, dish_name, price, action, total, total_amount, grand_amount, grand_total, tv_payOnline;
    private Context context;
    private int loginID;
    private LinearLayout  layout_promoCode, tv_continueLayout, layout_done;
    private Boolean editFlag = false;
    private double SubTotalPrice = 0;
    double totalPrice = 0.0,  dis=0.0, grandTotal = 0.0, totalDiscount=0.0;
    DbHelper dbHelper;
    DashboardActivity rootActivity;
    float shippingChareges = 0;
    String cityName;
    private ArrayList<CreateOrderDetails> orderDetailsesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        view = inflater.inflate(R.layout.fragment_add_yourorder, container, false);
        init();
        return view;
    }


    //set icons..........
    private void setIcons() {
        Typeface italic = FontManager.getFontTypeface(context, "fonts/roboto.italic.ttf");
        Typeface medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        Typeface regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        Typeface bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getActivity().getApplicationContext(), "fonts/materialdesignicons-webfont.otf");
        arrow_icon.setTypeface(materialdesignicons_font);
        arrow_icon.setText(Html.fromHtml("&#xf054;"));
        rupee_icon.setTypeface(materialdesignicons_font);
        rupee_icon.setText(Html.fromHtml("&#xf1af;"));
        icon_rupees.setTypeface(materialdesignicons_font);
        icon_rupees.setText(Html.fromHtml("&#xf1af;"));
//        rupees_icon.setTypeface(materialdesignicons_font);
//        rupees_icon.setText(Html.fromHtml("&#xf1af;"));

        tv_rupees_icon.setTypeface(materialdesignicons_font);
        tv_rupees_icon.setText(Html.fromHtml("&#xf1af;"));
        tv_edit_Icon.setTypeface(materialdesignicons_font);
        tv_edit_Icon.setText(Html.fromHtml("&#xf3eb;"));
//        tv_cancel.setTypeface(materialdesignicons_font);
//        tv_cancel.setText(Html.fromHtml("&#xf156;"));
        tv_done.setTypeface(materialdesignicons_font);
        tv_done.setText(Html.fromHtml("&#xf12c;"));
        tv_deliverTo.setTypeface(medium);
        tv_deliveryAddress.setTypeface(regular);
        your_order.setTypeface(bold);
        //  tv_editOrder.setTypeface(medium);
        quantity.setTypeface(regular);
        dish_name.setTypeface(regular);
        action.setTypeface(regular);
        price.setTypeface(regular);
        total.setTypeface(regular);
        total_amount.setTypeface(regular);
//        shipping.setTypeface(regular);
        tv_specialDiscount.setTypeface(regular);
//        shipping_charges.setTypeface(regular);
        tv_specialDiscount_charges.setTypeface(regular);
        grand_amount.setTypeface(medium);
        grand_total.setTypeface(medium);
//        edt_promoCode.setTypeface(regular);
//        tv_promoCode.setTypeface(regular);
//        tv_apply.setTypeface(medium);
    }

    //initlization..............
    private void init() {
        rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(false);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenCartDot(false);
//        edt_promoCode = view.findViewById(R.id.edt_promoCode);
//        tv_promoCode = view.findViewById(R.id.tv_promoCode);
//        tv_apply = view.findViewById(R.id.tv_apply);
//        tv_apply.setOnClickListener(this);
        tv_continue = view.findViewById(R.id.tv_continue);
        arrow_icon = view.findViewById(R.id.arrow_icon);
        your_order = view.findViewById(R.id.your_order);
        rupee_icon = view.findViewById(R.id.rupee_icon);
        icon_rupees = view.findViewById(R.id.icon_rupees);
        rupees_icon = view.findViewById(R.id.rupees_icon);
        quantity = view.findViewById(R.id.quantity);
        dish_name = view.findViewById(R.id.dish_name);
        action = view.findViewById(R.id.action);
        price = view.findViewById(R.id.price);
        total = view.findViewById(R.id.total);
        total_amount = view.findViewById(R.id.total_amount);
//        shipping = view.findViewById(R.id.shipping);
        tv_specialDiscount = view.findViewById(R.id.tv_specialDiscount);
//        shipping_charges = view.findViewById(R.id.shipping_charges);
        tv_specialDiscount_charges = view.findViewById(R.id.tv_specialDiscount_charges);
        grand_amount = view.findViewById(R.id.grant_amount);
        grand_total = view.findViewById(R.id.grand_total);
        tv_rupees_icon = view.findViewById(R.id.tv_rupees_icon);
        tv_deliverTo = view.findViewById(R.id.tv_deliverTo);
        tv_deliveryAddress = view.findViewById(R.id.tv_deliveryAddress);
        tv_edit_Icon = view.findViewById(R.id.tv_edit_icon);
//        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_done = view.findViewById(R.id.tv_done);
//        layout_promoCode = view.findViewById(R.id.layout_promoCode);
        layout_done = view.findViewById(R.id.layout_done);
        tv_continueLayout = view.findViewById(R.id.tv_continueLayout);
        tv_continueLayout.setOnClickListener(this);
        tv_edit_Icon.setOnClickListener(this);
//        tv_cancel.setOnClickListener(this);
        layout_done.setOnClickListener(this);
        setIcons();
        RecyclerView recyclerView = view.findViewById(R.id.listrecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        dbHelper = new DbHelper(context);
//        Data userData = dbHelper.getUserData();
//        loginID = userData.getLoginID();
        getShippingData();
        List<Result> orderList = dbHelper.GetAllBasketOrderData();
        if (orderList != null && orderList.size() > 0) {
            adapter = new YourOrderAdpater(context, orderList);
            recyclerView.setAdapter(adapter);
            getCalculation(orderList);
            orderDetails(orderList);
        }
        tv_deliveryAddress.setText(completeAddress + ", " + phone + ", " + zipcode);
    }

    private void orderDetails(List<Result> orderList) {
        for (Result order : orderList) {
            CreateOrderDetails orderDetails = new CreateOrderDetails();
            orderDetails.setProductId(order.getProductId());
            orderDetails.setQuantity(order.getQuantity());
            orderDetailsesList.add(orderDetails);
        }
    }

    private void getShippingData() {
        rootActivity.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityName = adapterView.getSelectedItem().toString();
                rootActivity.updateCityPres(cityName);
                setValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getCalculation(List<Result> orderList) {

        for (Result myBasket : orderList) {
            int productId = myBasket.getId();
            Result myBaskets = dbHelper.getBasketOrderData(productId);
            float  discount=myBaskets.getProduct_dis();;
            double total = myBaskets.getProduct_mrp();;
            double qty = myBaskets.getQuantity();
            SubTotalPrice = (total * qty);
            totalPrice=totalPrice+SubTotalPrice;
            dis = qty * discount;
            totalDiscount=totalDiscount+dis;
//            clearValue();
            setValues();
        }
    }

    void clearValue() {
        shippingChareges = 0;
        totalPrice = 0;
        dis = 0;
        grandTotal = 0;

    }

    void setValues() {
        //shareredpres city get..
        SharedPreferences sharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        String cityname = sharedPreferences.getString("city", null);
        DbHelper dbHelper = new DbHelper(context);
        Result resultList = dbHelper.getCityDataByCityName(cityname);
//        if (resultList != null) {
//            shippingChareges = resultList.getShippingCharge();
//        }
        DecimalFormat format = new DecimalFormat("0.0");
        total_amount.setText(format.format(totalPrice));
        tv_specialDiscount_charges.setText(format.format(totalDiscount));
        grandTotal = totalPrice - totalDiscount ;
        grand_amount.setText(format.format(grandTotal));
//        shipping_charges.setText(format.format(shippingChareges));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_continueLayout:
                createNewOrder();
                break;
//            case R.id.tv_cancel:
//                edt_promoCode.setText("");
//                tv_apply.setVisibility(View.VISIBLE);
//                tv_cancel.setVisibility(View.GONE);
//                break;
            case R.id.layout_done:
                break;
        }
    }


    //create new Order

    public void createNewOrder() {
        if (Utility.isOnline(context)) {
            final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
            dotDialog.show();
//            final DbHelper dbHelper = new DbHelper(context);
//            Data data = dbHelper.getUserData();
//            int loginId = data.getLoginID();
//            ServiceCaller serviceCaller = new ServiceCaller(context);
//            serviceCaller.createOrderService(1, 1, orderDetailsesList, totalPrizzzzzce, dis, shippingChareges, grandTotal,
//                    new IAsyncWorkCompletedCallback() {
//                        @Override
//                        public void onDone(String result, boolean isComplete) {
//                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
//                            if (isComplete) {

//                                if (result != null) {
                                OrderConfirmFragment fragment = OrderConfirmFragment.newInstance(completeAddress, zipcode, phone, "", totalPrice, dis, shippingChareges, grandTotal);
                                moveFragmentWithTag(fragment, "OrderPlacedFragment");

//                                } else {
//                                    Utility.alertForErrorMessage("Order not Placed Successfully", context);
//                                }
//                            } else {
//                                Utility.alertForErrorMessage("Order not Placed Successfully", context);
//                            }
                            if (dotDialog.isShowing()) {
                                dotDialog.dismiss();
                            }
                        }
//                    });


//        } else {
//            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);
//        }
    }

    //
    private void moveFragmentWithTag(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }


    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
