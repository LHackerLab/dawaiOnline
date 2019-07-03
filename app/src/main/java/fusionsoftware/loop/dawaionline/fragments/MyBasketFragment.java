package fusionsoftware.loop.dawaionline.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.adapter.BasketAdapter;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.MyBasket;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.FontManager;


public class MyBasketFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static MyBasketFragment newInstance(String param1, String param2) {
        MyBasketFragment fragment = new MyBasketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Typeface materialDesignIcons, medium, regular, bold;
    TextView arrow_icon, tv_continue, tv_yourOrder, grand_total, checkOutallIcon;
    LinearLayout layout_basket, continuelayout, layoutcheck, allcheckoutLayout;
    private Context context;
    View view;
    private double totalPrice = 0;
    private List<MyBasket> basketdata;
    BasketAdapter adapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.fragment_my_basket, container, false);
        initViews();//initilization........
        return view;
    }


    //set the icons
    private void setIcon() {
        arrow_icon.setTypeface(materialDesignIcons);
        checkOutallIcon.setTypeface(materialDesignIcons);
        arrow_icon.setText(Html.fromHtml("&#xf054;"));
        checkOutallIcon.setText(Html.fromHtml("&#xf054;"));
        tv_continue.setTypeface(medium);
        tv_yourOrder.setTypeface(bold);
    }

    //initilization........
    private void initViews() {
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreenTitle("My Basket");
        rootActivity.setScreencart(false);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenCartDot(false);
//        rootActivity.setItemCart();
        arrow_icon = (TextView) view.findViewById(R.id.arrow_icon);
        tv_continue = (TextView) view.findViewById(R.id.tv_continue);
        tv_yourOrder = (TextView) view.findViewById(R.id.tv_yourOrder);
        grand_total = (TextView) view.findViewById(R.id.grand_total);
        continuelayout = (LinearLayout) view.findViewById(R.id.continuelayout);
        layoutcheck = (LinearLayout) view.findViewById(R.id.layoutcheck);
        layout_basket = (LinearLayout) view.findViewById(R.id.layout_basket);
        allcheckoutLayout = (LinearLayout) view.findViewById(R.id.allcheckoutLayout);
        checkOutallIcon = view.findViewById(R.id.checkOutallIcon);
        continuelayout.setOnClickListener(this);
        allcheckoutLayout.setOnClickListener(this);
        grand_total.setTypeface(bold);
        setIcon();//set the icons
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DbHelper dbHelper = new DbHelper(context);
        List<MyBasket> orderList = dbHelper.GetAllBasketOrderData();
//        if (orderList != null && orderList.size() > 0) {
//            shortBasketData(orderList);
//        } else {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            nodata.setTypeface(regular);
            nodataIcon.setTypeface(materialDesignIcons);
            nodataIcon.setText(Html.fromHtml("&#xf187;"));
            nodata.setText("No Order Found In Basket");
            layout_basket.setGravity(Gravity.CENTER);
            layout_basket.removeAllViews();
            layout_basket.addView(view);
            continuelayout.setVisibility(View.VISIBLE);
            layoutcheck.setVisibility(View.GONE);
//        }
    }

    //sort  data.................
    private void shortBasketData(List<MyBasket> resultList) {
        List<MyBasket> newList = new ArrayList<MyBasket>();
        for (MyBasket c : resultList) {
//            if (!categoryDataExist(newList, c.getCategoryName())) {
//                newList.add(c);
//            }
        }
        adapter = new BasketAdapter(context, newList, MyBasketFragment.this);
        recyclerView.setAdapter(adapter);
    }

    public void setMostTotal(double mostTotal) {
        DecimalFormat df = new DecimalFormat("0.0");
        grand_total.setText("\u20B9" + df.format(mostTotal));
    }

//    private boolean categoryDataExist(List<MyBasket> newList, String name) {
//        for (MyBasket c : newList) {
//            if (c.getCategoryName().equalsIgnoreCase(name)) {
//                return true;
//            }
//        }
//        return false;
//    }

    //notifiy adapter if data delete
    private void notifiyAdapter() {
//        if (adapter != null) {
//            adapter.notifyDataSetChanged();
//        }
    }

    //    get message from basketinneradapter for notify adapter
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equalsIgnoreCase("basketItem")) {
//                boolean basketFlag = intent.getBooleanExtra("basketFlag", false);
////                Do whatever you want with the code here
//                if (basketFlag) {
//                    notifiyAdapter();
//                }
//            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter("basketItem"));
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continuelayout:
                setUpHomeFragment();
                break;
            case R.id.allcheckoutLayout:
               // addPricedis();// add price and dicsount in shareprefernces..
                UserAddressListFragment fragment = UserAddressListFragment.newInstance(true, 0);
                moveFragment(fragment);
                break;
        }
    }

    private void addPricedis() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("pricePres", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("total", "");
        editor.putString("dis", "");
        editor.apply();
    }

    //open home fragement
    private void setUpHomeFragment() {
        ParentFragment fragment = ParentFragment.newInstance(0, 0);
        moveFragment(fragment);
    }

    //move to fragment
    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
