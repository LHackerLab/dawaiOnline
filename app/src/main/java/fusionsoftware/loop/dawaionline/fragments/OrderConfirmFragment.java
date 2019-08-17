package fusionsoftware.loop.dawaionline.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.activity.PaymentWebviewActivity;
import fusionsoftware.loop.dawaionline.adapter.OrderConfirmAdapter;
import fusionsoftware.loop.dawaionline.adapter.YourOrderAdpater;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.Addresses;
import fusionsoftware.loop.dawaionline.model.ContentData;
import fusionsoftware.loop.dawaionline.model.CreateOrderDetails;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.MyBasket;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.Contants;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;

public class OrderConfirmFragment extends Fragment implements View.OnClickListener {
    private String result;
    private String completeAddress, zipCode, phone;
    private double totalPrice, dis, grandTotal;
    private float shippingChareges;
    // TODO: Rename and change types and number of parameters

    public static OrderConfirmFragment newInstance(String completeAddress, String zipCode, String phone, String result, double totalPrice, double dis, float shippingChareges, double grandTotal) {
        OrderConfirmFragment fragment = new OrderConfirmFragment();
        Bundle args = new Bundle();
        args.putString("completeAddress", completeAddress);
        args.putString("zipCode", zipCode);
        args.putString("phone", phone);
        args.putString("Result", result);
        args.putDouble("totalPrice", totalPrice);
        args.putDouble("dis", dis);
        args.putFloat("shippingChareges", shippingChareges);
        args.putDouble("grandTotal", grandTotal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            completeAddress = getArguments().getString("completeAddress");
            zipCode = getArguments().getString("zipCode");
            phone = getArguments().getString("phone");
            result = getArguments().getString("Result");
            totalPrice = getArguments().getDouble("totalPrice");
            dis = getArguments().getDouble("dis");
            shippingChareges = getArguments().getFloat("shippingChareges");
            grandTotal = getArguments().getDouble("grandTotal");

        }
    }

    OrderConfirmAdapter adapter;
    View view;
    TextView font_cash, payonline, your_order, tv_specialDiscount, tv_gst, total_gst,
            rupee_icon, icon_rupees, tv_rupees_icon, rupees_icon, tv_rupees_icon_gst, net_price_amount, tv_net_price,
            tv_deliverTo, tv_deliveryAddress, quantity, dish_name, price, net_price_rupees_icon, icon_rupees_promo,
            total, total_amount, shipping, shipping_charges, grand_amount, grand_total, sub_total, sub_amount, icon_rupees_sub,
            tv_payOnline, tv_cod, tv_discount, tv_discount_rupees_icon, tv_Discount_charges, tv_promocode, tv_promocode_value;
    private Context context;
    private int loginID;
    private String OrderNumber = "1";
    LinearLayout layout_promodiscount, layout_promocode, layout_discount;
    private int storeId;
    private float NetPayable;
    int orderId;
    private LinearLayout codLayout, onlineLayout;
    private ArrayList<CreateOrderDetails> orderDetailsesList;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        view = inflater.inflate(R.layout.fragment_order_confirm, container, false);
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
        font_cash.setTypeface(materialdesignicons_font);
        font_cash.setText(Html.fromHtml("&#xf1af;"));
        payonline.setTypeface(materialdesignicons_font);
        payonline.setText(Html.fromHtml("&#xf19c;"));
        rupee_icon.setTypeface(materialdesignicons_font);
        rupee_icon.setText(Html.fromHtml("&#xf1af;"));
        icon_rupees.setTypeface(materialdesignicons_font);
        icon_rupees.setText(Html.fromHtml("&#xf1af;"));
        rupees_icon.setTypeface(materialdesignicons_font);
        rupees_icon.setText(Html.fromHtml("&#xf1af;"));
        net_price_rupees_icon.setTypeface(materialdesignicons_font);
        net_price_rupees_icon.setText(Html.fromHtml("&#xf1af;"));
        tv_rupees_icon_gst.setTypeface(materialdesignicons_font);
        tv_rupees_icon_gst.setText(Html.fromHtml("&#xf1af;"));
        icon_rupees_promo.setTypeface(materialdesignicons_font);
        icon_rupees_promo.setText(Html.fromHtml("&#xf1af;"));
        tv_discount_rupees_icon.setTypeface(materialdesignicons_font);
        tv_discount_rupees_icon.setText(Html.fromHtml("&#xf1af;"));
        tv_deliverTo.setTypeface(medium);
        tv_deliveryAddress.setTypeface(regular);
        your_order.setTypeface(bold);
        quantity.setTypeface(regular);
        dish_name.setTypeface(regular);
        price.setTypeface(regular);
        total.setTypeface(regular);
        total_amount.setTypeface(regular);
        tv_discount.setTypeface(regular);
        icon_rupees_sub.setTypeface(materialdesignicons_font);
        icon_rupees_sub.setText(Html.fromHtml("&#xf1af;"));
        tv_promocode.setTypeface(regular);
        tv_promocode_value.setTypeface(regular);
        shipping.setTypeface(regular);
        shipping_charges.setTypeface(regular);
        grand_amount.setTypeface(medium);
        tv_cod.setTypeface(bold);
        tv_payOnline.setTypeface(bold);
        tv_net_price.setTypeface(medium);
        net_price_amount.setTypeface(medium);
        grand_total.setTypeface(medium);
        sub_total.setTypeface(medium);
        sub_amount.setTypeface(medium);
    }


    //initlization..............

    private void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(false);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenCartDot(false);
        font_cash = (TextView) view.findViewById(R.id.font_cash);
        payonline = (TextView) view.findViewById(R.id.arrow_icon);
        your_order = (TextView) view.findViewById(R.id.your_order);
        rupee_icon = (TextView) view.findViewById(R.id.rupee_icon);
        icon_rupees = (TextView) view.findViewById(R.id.icon_rupees);
        rupees_icon = (TextView) view.findViewById(R.id.rupees_icon);
        quantity = (TextView) view.findViewById(R.id.quantity);
        dish_name = (TextView) view.findViewById(R.id.dish_name);
        price = (TextView) view.findViewById(R.id.price);
        total = (TextView) view.findViewById(R.id.total);
        total_amount = (TextView) view.findViewById(R.id.total_amount);
        tv_net_price = (TextView) view.findViewById(R.id.tv_net_price);
        net_price_amount = (TextView) view.findViewById(R.id.net_price_amount);
        layout_discount = (LinearLayout) view.findViewById(R.id.layout_discount);
        layout_promodiscount = (LinearLayout) view.findViewById(R.id.layout_net_price);
        layout_promocode = (LinearLayout) view.findViewById(R.id.layout_gst);

        icon_rupees_promo = (TextView) view.findViewById(R.id.icon_rupees_promo);
        tv_promocode = (TextView) view.findViewById(R.id.tv_promocode);
        tv_promocode_value = (TextView) view.findViewById(R.id.tv_promocode_value);
        tv_specialDiscount = (TextView) view.findViewById(R.id.tv_specialDiscount);
        shipping_charges = (TextView) view.findViewById(R.id.shipping_charges);
        shipping = (TextView) view.findViewById(R.id.shipping);
        tv_discount = (TextView) view.findViewById(R.id.tv_discount);
        tv_Discount_charges = (TextView) view.findViewById(R.id.tv_Discount_charges);
        tv_gst = (TextView) view.findViewById(R.id.tv_gst);
        total_gst = (TextView) view.findViewById(R.id.total_gst);
        net_price_rupees_icon = (TextView) view.findViewById(R.id.net_price_rupees_icon);
        tv_rupees_icon_gst = (TextView) view.findViewById(R.id.tv_rupees_icon_gst);
        sub_total = (TextView) view.findViewById(R.id.sub_total);
        icon_rupees_sub = (TextView) view.findViewById(R.id.icon_rupees_sub);
        sub_amount = (TextView) view.findViewById(R.id.sub_amount);
        grand_amount = (TextView) view.findViewById(R.id.grant_amount);
        tv_cod = (TextView) view.findViewById(R.id.tv_cod);
        grand_total = (TextView) view.findViewById(R.id.grand_total);
        tv_payOnline = (TextView) view.findViewById(R.id.tv_continue);
        tv_rupees_icon = (TextView) view.findViewById(R.id.tv_rupees_icon);
        tv_discount_rupees_icon = (TextView) view.findViewById(R.id.tv_discount_rupees_icon);
        codLayout = (LinearLayout) view.findViewById(R.id.codLayout);
        tv_deliverTo = (TextView) view.findViewById(R.id.tv_deliverTo);
        tv_deliveryAddress = (TextView) view.findViewById(R.id.tv_deliveryAddress);
        codLayout.setOnClickListener(this);
        onlineLayout = (LinearLayout) view.findViewById(R.id.onlineLayout);
        onlineLayout.setOnClickListener(this);
        //tv_editOrder.setOnClickListener(this);
        setIcons();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listrecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        setDetails();
        orderDetailsesList = new ArrayList<CreateOrderDetails>();
        DbHelper dbHelper = new DbHelper(context);
        List<Result> orderList = dbHelper.GetAllBasketOrderData();
        if (orderList != null && orderList.size() > 0) {
            adapter = new OrderConfirmAdapter(context, orderList);
            recyclerView.setAdapter(adapter);
        }
        tv_deliveryAddress.setText(completeAddress + "," + phone + "," + zipCode);
    }

    //get all details and set values
    private void setDetails() {
//        ContentData data = new Gson().fromJson(result, ContentData.class);
//        if (data != null) {
//            Result objData = data.getResult();
//            if (objData != null) {
//                OrderNumber = objData.getOrderNumber();
//                orderId = objData.getOrderId();
//                NetPayable = objData.getNetPrice();
//                total_amount.setText(String.valueOf(objData.getTotalPrice()));
//                grand_amount.setText(String.valueOf(objData.getGrandTotal()));
//                tv_Discount_charges.setText(String.valueOf(objData.getDiscount()));
//                shipping_charges.setText(String.valueOf(objData.getShippingCharge()));
//                tv_promocode_value.setText(String.valueOf(objData.getPromoDiscount()));
//                net_price_amount.setText(String.valueOf(objData.getNetPrice()));
//                sub_amount.setText(String.valueOf(objData.getSubTotal()));
//                total_gst.setText(String.valueOf(objData.getTotalGST()));

                total_amount.setText(String.valueOf(totalPrice));
                tv_Discount_charges.setText(String.valueOf(dis));
                shipping_charges.setText(String.valueOf(shippingChareges));
                sub_amount.setText(String.valueOf(totalPrice - dis));
                net_price_amount.setText(String.valueOf(grandTotal));
//            }
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.onlineLayout:
                getInitiatePayment();
                break;
            case R.id.codLayout:
                getCOD();
                break;
        }
    }

    //get Initiate payment for geting payment url
    private void getInitiatePayment() {
        OrderPlacedFragment fragmentm = OrderPlacedFragment.newInstance(OrderNumber, "");
        moveFragmentWithTag(fragmentm, "OrderPlacedFragment");
        clearBasketData();
//        if (Utility.isOnline(context)) {
//            final BallTriangleDialog spotsDialog = new BallTriangleDialog(context);
//            spotsDialog.show();
//            final ServiceCaller serviceCaller = new ServiceCaller(context);
//            serviceCaller.getPaymentUrlService(OrderNumber,1, new IAsyncWorkCompletedCallback() {
//                @Override
//                public void onDone(String result, boolean isComplete) {
//                    if (isComplete) {
//                        ContentData data = new Gson().fromJson(result, ContentData.class);
//                        if (data != null) {
//                            Data mainData = data.getData();
//                            if (mainData != null) {
//                                if (mainData.getPaymentURL() != null) {
//                                    clearBasketData();
//                                    Intent intent = new Intent(context, PaymentWebviewActivity.class);
//                                    intent.putExtra(PaymentWebviewActivity.KEY_REQUESTED_URL, mainData.getPaymentURL());
//                                    intent.putExtra("OrderNo", OrderNumber);
//                                    intent.putExtra("NetPayable", NetPayable);
//                                    intent.putExtra("OrderId", orderId);
//                                    intent.putExtra("storeId", storeId);
//                                    startActivity(intent);
//                                }
//                            }
//                        }
//                    }
//                    if (spotsDialog.isShowing()) {
//                        spotsDialog.dismiss();
//                    }
//                }
//            });
//
//        } else {
//            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);
//        }
    }

    private void getCOD() {
//        if (Utility.isOnline(context)) {
//            String mode = "COD";
//            final BallTriangleDialog spotsDialog = new BallTriangleDialog(context);
//            spotsDialog.show();
//            final ServiceCaller serviceCaller = new ServiceCaller(context);
//            serviceCaller.getCODService(OrderNumber, 1, mode, new IAsyncWorkCompletedCallback() {
//                @Override
//                public void onDone(String result, boolean isComplete) {
//                    if (isComplete) {
//                        ContentData data = new Gson().fromJson(result, ContentData.class);
//                        if (data != null) {
                        clearBasketData();
                        OrderPlacedFragment fragmentm = OrderPlacedFragment.newInstance(OrderNumber, "");
                        moveFragmentWithTag(fragmentm, "OrderPlacedFragment");
//                        }
//                    }
//                    if (spotsDialog.isShowing()) {
//                        spotsDialog.dismiss();
//                    }
//                    }
//                }
//            });
//
//        } else {
//            Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);
//        }
    }

    //remove data from cart.................
    private void clearBasketData() {
        final DbHelper dbHelper = new DbHelper(context);
        dbHelper.deleteAllBasketOrderData();
    }


    private void moveFragmentWithTag(Fragment fragment, String tag) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
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
