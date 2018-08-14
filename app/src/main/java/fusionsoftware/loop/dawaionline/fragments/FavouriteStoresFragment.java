package fusionsoftware.loop.dawaionline.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.adapter.FavouriteStoresAdapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


public class FavouriteStoresFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private String mParam2;
    private String mParam1;

    public FavouriteStoresFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FavouriteStoresFragment newInstance(String param1, String param2) {
        FavouriteStoresFragment fragment = new FavouriteStoresFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("ARG_PARAM1");
            mParam2 = getArguments().getString("ARG_PARAM2");
        }
    }

    private Context context;
    private RecyclerView recyclerView;
    private View view;
    private LinearLayout favourite_layout;
    Typeface materialdesignicons_font, regular;
    FavouriteStoresAdapter itemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_favourite_stores, container, false);
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
        getFavouriteStoreList();
        return view;
    }


    // initlization...........
    private void init() {
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenLocation(true);
        rootActivity.setItemCart();
        favourite_layout = (LinearLayout) view.findViewById(R.id.favourite_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.favouriteStore_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Typeface bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setTypeface(bold);
    }

    private void setValue() {
        DbHelper dbHelper = new DbHelper(context);
        List<Data> favouriteStoresList = dbHelper.getAllFavouriteStoresData();
        if (favouriteStoresList != null && favouriteStoresList.size() > 0) {
            itemAdapter = new FavouriteStoresAdapter(context, favouriteStoresList);
            recyclerView.setAdapter(itemAdapter);
        } else {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            nodata.setTypeface(regular);
            nodataIcon.setTypeface(materialdesignicons_font);
            nodataIcon.setText(Html.fromHtml("&#xf187;"));
            nodata.setText("Any Store Favourite not found");
            favourite_layout.setGravity(Gravity.CENTER);
            favourite_layout.removeAllViews();
            favourite_layout.addView(view);
        }
    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(mReceiverLocation, new IntentFilter("data"));
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReceiverLocation);
        super.onPause();
    }

    //come from favorite adapter
    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equalsIgnoreCase("data")) {
                    Boolean flag = intent.getBooleanExtra("flag", false);
                    if (flag) {
                        setValue();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //get all Favourite store recyclerView.........
    public void getFavouriteStoreList() {
        if (Utility.isOnline(getActivity())) {
            final DbHelper dbHelper = new DbHelper(context);
            Data data = dbHelper.getUserData();
            if (data != null) {
                int loginId = data.getLoginID();
                if (loginId != 0) {
                    final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
                    dotDialog.show();
                    ServiceCaller serviceCaller = new ServiceCaller(context);
                    serviceCaller.callGetFavouriteStoreByUserService(loginId, new IAsyncWorkCompletedCallback() {
                        @Override
                        public void onDone(String workName, boolean isComplete) {
                            setValue();
                            if (dotDialog.isShowing()) {
                                dotDialog.dismiss();
                            }
                        }
                    });
                }
            }
        } else {
            noInternet();
        }
    }

    private void noInternet() {
        // Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getActivity());//off line msg....
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        nodata.setText("No internet Connection found");
        favourite_layout.setGravity(Gravity.CENTER);
        favourite_layout.removeAllViews();
        favourite_layout.addView(view);
    }

}

