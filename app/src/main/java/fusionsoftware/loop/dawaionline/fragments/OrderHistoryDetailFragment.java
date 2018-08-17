package fusionsoftware.loop.dawaionline.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.adapter.OrderHistoryDetailAdapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.ContentData;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.OrderDetails;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


public class OrderHistoryDetailFragment extends Fragment {

    private int OrderId, StoreId;
    private String OrderNumber;
    private String StoreName;


    public OrderHistoryDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OrderHistoryDetailFragment newInstance(int OrderId, String OrderNumber, String StoreName, int StoreId) {
        OrderHistoryDetailFragment fragment = new OrderHistoryDetailFragment();
        Bundle args = new Bundle();
        args.putInt("OrderId", OrderId);
        args.putString("OrderNumber", OrderNumber);
        args.putString("StoreName", StoreName);
        args.putInt("StoreId", StoreId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            OrderId = getArguments().getInt("OrderId");
            OrderNumber = getArguments().getString("OrderNumber");
            StoreName = getArguments().getString("StoreName");
            StoreId = getArguments().getInt("StoreId");
        }
    }

    View view;
    TextView rupee_icon, icon_rupees,
            tv_cancelOrder, shippingCharges, rupees_icon, quantity,
            dish_name, price, total, tv_storeName, tv_placed, tv_status, tv_ex_delivery,
            total_amount, shipping, tv_orderNumber, tv_title, tv_promocode_value, tv_promocode, tv_gst, total_gst, sub_total, sub_amount,
            grandTotal, grandAmount, tv_Discount_charges, tv_discount, tv_net_price, net_price_amount, icon_rupees_promo, tv_rupees_icon_gst, icon_rupees_sub, tv_discount_rupees_icon, net_price_rupees_icon;
    private Context context;
    LinearLayout cancleOrder, layout_orderDetails;
    List<Data> data;
    OrderHistoryDetailAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        view = inflater.inflate(R.layout.fragment_order_history_detail, container, false);
        init();//initlization..............
        setIcons();//set icons..........
        GetOrderByOrderNumber();
        return view;
    }

    private void setOrderDetailsData(Data data) {
//        DbHelper dbHelper = new DbHelper(context);
//        Data orderData = dbHelper.getMyAllOrderHistoryDataByOrderId(OrderId);
        if (data != null) {
            tv_storeName.setText(data.getStoreName());
            //tv_orderNumber.setText(": " + data.getOrderNumber());
            tv_status.setText(data.getOrderStatus());
            total_amount.setText(String.valueOf(data.getTotalPrice()));
            grandAmount.setText(String.valueOf(data.getGrandTotal()));
            tv_Discount_charges.setText(String.valueOf(data.getSpecialDiscount()));
            shippingCharges.setText(String.valueOf(data.getShippingCharge()));
            tv_promocode_value.setText(String.valueOf(data.getPromoDiscount()));
            net_price_amount.setText(String.valueOf(data.getNetPrice()));
            sub_amount.setText(String.valueOf(data.getSubTotal()));
            total_gst.setText(String.valueOf(data.getTotalGST()));


            Date startDateTime = null;
            String sdateTime = null;
            try {
                startDateTime = Utility.toCalendar(data.getOrderTime());
                sdateTime = Utility.convertDate(startDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //get value based on array position Wednesday,15,Jun,6,30,AM,2000,06 index(0,1,2,3,4,5,6,7)
            if (sdateTime != null) {
                String[] startArray = sdateTime.split(",");
                tv_placed.setText("Placed on: " + startArray[1] + "-" + startArray[2] + "-" + startArray[6]);
            }

            OrderDetails[] orderDetailses = data.getOrderDetails();
            if (orderDetailses != null) {
                adapter = new OrderHistoryDetailAdapter(context, orderDetailses);
                recyclerView.setAdapter(adapter);
            }
        }
    }
//

    //set icons..........
    private void setIcons() {
        Typeface medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        Typeface regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        Typeface bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getActivity().getApplicationContext(), "fonts/materialdesignicons-webfont.otf");

        rupee_icon.setTypeface(materialdesignicons_font);
        rupee_icon.setText(Html.fromHtml("&#xf1af;"));
        icon_rupees.setTypeface(materialdesignicons_font);
        icon_rupees.setText(Html.fromHtml("&#xf1af;"));
        rupees_icon.setTypeface(materialdesignicons_font);
        rupees_icon.setText(Html.fromHtml("&#xf1af;"));
        quantity.setTypeface(regular);
        dish_name.setTypeface(regular);
        price.setTypeface(regular);
        tv_cancelOrder.setTypeface(medium);
        shippingCharges.setTypeface(regular);
        grandTotal.setTypeface(medium);
        total.setTypeface(regular);
        tv_storeName.setTypeface(medium);
        grandAmount.setTypeface(medium);
        tv_placed.setTypeface(regular);
        tv_status.setTypeface(regular);
        tv_ex_delivery.setTypeface(regular);
        tv_title.setTypeface(bold);
        tv_orderNumber.setTypeface(bold);
        total_amount.setTypeface(regular);
        shipping.setTypeface(regular);
        tv_promocode.setTypeface(regular);
        tv_promocode_value.setTypeface(regular);
        tv_gst.setTypeface(regular);
        total_gst.setTypeface(regular);
        sub_total.setTypeface(regular);
        sub_amount.setTypeface(regular);
        tv_Discount_charges.setTypeface(regular);
        tv_discount.setTypeface(regular);
        tv_net_price.setTypeface(medium);
        net_price_amount.setTypeface(medium);
        icon_rupees_promo.setTypeface(materialdesignicons_font);
        icon_rupees_promo.setText(Html.fromHtml("&#xf1af;"));
        tv_rupees_icon_gst.setTypeface(materialdesignicons_font);
        tv_rupees_icon_gst.setText(Html.fromHtml("&#xf1af;"));
        icon_rupees_sub.setTypeface(materialdesignicons_font);
        icon_rupees_sub.setText(Html.fromHtml("&#xf1af;"));
        tv_discount_rupees_icon.setTypeface(materialdesignicons_font);
        tv_discount_rupees_icon.setText(Html.fromHtml("&#xf1af;"));
        net_price_rupees_icon.setTypeface(materialdesignicons_font);
        net_price_rupees_icon.setText(Html.fromHtml("&#xf1af;"));

        cancleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Cancel Order")
                        .setCancelable(false)
                        .setMessage("Are you Sure! you Want To Cancel Order?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DbHelper dbHelper = new DbHelper(context);
                                //dbHelper.deleteGetAllOrderDataById(OrderNumber);
                                getActivity().getSupportFragmentManager().popBackStack();//back to profile screen
                                Toast.makeText(context, "Your Order Cancel Successfully", Toast.LENGTH_SHORT).show();

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
        });
    }

    //initlization..............
    private void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(false);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenCartDot(false);
        rootActivity.setItemCart();
        tv_cancelOrder = view.findViewById(R.id.tv_cancel_order);
        rupee_icon = view.findViewById(R.id.rupee_icon);
        icon_rupees = view.findViewById(R.id.icon_rupees);
        rupees_icon = view.findViewById(R.id.rupees_icon);
        quantity = view.findViewById(R.id.quantity);
        dish_name = view.findViewById(R.id.dish_name);
        grandTotal = view.findViewById(R.id.grand_total);
        total = view.findViewById(R.id.total);
        price = view.findViewById(R.id.price);
        tv_storeName = view.findViewById(R.id.tv_storeName);
        tv_placed = view.findViewById(R.id.tv_placed);
        tv_status = view.findViewById(R.id.tv_status);
        tv_ex_delivery = view.findViewById(R.id.tv_ex_delivery);
        total_amount = view.findViewById(R.id.total_amount);
        shipping = view.findViewById(R.id.shipping);
        grandAmount = view.findViewById(R.id.grand_amount);
        shippingCharges = view.findViewById(R.id.shipping_charges);
        tv_title = view.findViewById(R.id.tv_title);
        tv_orderNumber = view.findViewById(R.id.tv_orderNumber);
        cancleOrder = view.findViewById(R.id.cancel_order);
        layout_orderDetails = view.findViewById(R.id.layout_orderDetails);
        recyclerView = view.findViewById(R.id.listrecycler);

        icon_rupees_promo = view.findViewById(R.id.icon_rupees_promo);
        tv_promocode = view.findViewById(R.id.tv_promocode);
        tv_promocode_value = view.findViewById(R.id.tv_promocode_value);

        tv_gst = view.findViewById(R.id.tv_gst);
        total_gst = view.findViewById(R.id.total_gst);
        tv_rupees_icon_gst = view.findViewById(R.id.tv_rupees_icon_gst);

        sub_total = view.findViewById(R.id.sub_total);
        icon_rupees_sub = view.findViewById(R.id.icon_rupees_sub);
        sub_amount = view.findViewById(R.id.sub_amount);

        tv_discount = view.findViewById(R.id.tv_discount);
        tv_Discount_charges = view.findViewById(R.id.tv_Discount_charges);
        tv_discount_rupees_icon = view.findViewById(R.id.tv_discount_rupees_icon);

        tv_net_price = view.findViewById(R.id.tv_net_price);
        net_price_amount = view.findViewById(R.id.net_price_amount);
        net_price_rupees_icon = view.findViewById(R.id.net_price_rupees_icon);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    //GetOrderByOrderNumber api method....
    private void GetOrderByOrderNumber() {
        if (Utility.isOnline(context)) {
            final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.GetOrderByOrderNumberService(OrderNumber, StoreId, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentData contentData = new Gson().fromJson(result, ContentData.class);
                        if (contentData != null) {
                            Data data = contentData.getData();
                            if (data != null) {
                                setOrderDetailsData(data);
                            }
                        }
                    } else {
                        Utility.alertForErrorMessage("Order Details Not Found", context);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            // Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            nodata.setText("No internet connection found");
            layout_orderDetails.setGravity(Gravity.CENTER);
            layout_orderDetails.removeAllViews();
            layout_orderDetails.addView(view);
        }
    }

}
