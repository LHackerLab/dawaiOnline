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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.adapter.SelectStoreAdapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.Contants;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


public class SelectStoreFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private int localityId;
    //  private int LoginId;
    private String param;


    public SelectStoreFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SelectStoreFragment newInstance(int localityId, String param) {
        SelectStoreFragment fragment = new SelectStoreFragment();
        Bundle args = new Bundle();
        args.putInt("LocalityId", localityId);
        args.putString("param", param);
//        args.putInt("LoginId", LoginId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            localityId = getArguments().getInt("LocalityId");
            param = getArguments().getString("param");
//            LoginId = getArguments().getInt("LoginId");
        }
    }

    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    Typeface materialDesignIcons, regular, bold;
    DbHelper dbHelper;
    Data data;
    int LoginId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_select_store, container, false);
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        init();// initlization...........
        getAllStoreList();

        return view;
    }

    // initlization...........
    private void init() {

        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setItemCart();
        rootActivity.setScreenLocation(true);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenSave(false);
        recyclerView = (RecyclerView) view.findViewById(R.id.selectStore_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        title.setTypeface(bold);
    }

    //get all store recyclerView.........
    public void getAllStoreList() {
        if (Utility.isOnline(context)) {
            dbHelper = new DbHelper(context);
            data = dbHelper.getUserData();
            if (data != null) {
                LoginId = data.getLoginID();
            }
            final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callGetAllStoreListService(localityId, LoginId, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        DbHelper dbHelper = new DbHelper(context);
                        List<Data> storelist = dbHelper.GetAllStoreData();
                        if (storelist != null) {
                            SelectStoreAdapter itemAdapter = new SelectStoreAdapter(context, storelist);
                            recyclerView.setAdapter(itemAdapter);
                        } else {
                            noDataFoundUI();
                        }
                    } else {
                        noDataFoundUI();
                    }
                    try {
                        if (dotDialog.isShowing() && dotDialog != null) {
                            dotDialog.dismiss();
                        }
                    } catch (Exception ex) {
                        Log.e(Contants.LOG_TAG, ex.getMessage(), ex);
                    }

                }

            });

        } else {
            //Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);//off line msg....
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            nodata.setText("No internet connection found");
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.removeAllViews();
            linearLayout.addView(view);
        }
    }

    private void noDataFoundUI() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        nodataIcon.setTypeface(materialDesignIcons);
        nodataIcon.setText(Html.fromHtml("&#xf187;"));
        nodata.setText("Any Store not found.");
        nodata.setTypeface(regular);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.removeAllViews();
        linearLayout.addView(view);
    }

    //for auto get otp from message
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("favorite")) {
                Boolean favoriteFlg = intent.getBooleanExtra("favoriteFlg", false);
                //Do whatever you want with the code here
                if (favoriteFlg) {
                    getAllStoreList();
                }
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter("favorite"));
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }
}
