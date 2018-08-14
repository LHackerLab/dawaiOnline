package fusionsoftware.loop.dawaionline.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.activity.LoginActivity;
import fusionsoftware.loop.dawaionline.adapter.TrackOrderAdapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


public class TrackOrderFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static TrackOrderFragment newInstance(String param1, String param2) {
        TrackOrderFragment fragment = new TrackOrderFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("param1");
            mParam2 = getArguments().getString("param2");
        }
    }

    Context context;
    View view;
    RecyclerView track_order_recycler_view;
    LinearLayout track_Order_Layout;
    private Typeface materialDesignIcons, regular;
    private List<Data> mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        view = inflater.inflate(R.layout.fragment_track_order, container, false);
        init();  //initialization.......
        getAllOrderByUser();    // get all order by user...........

        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setItemCart();
        rootActivity.setScreenLocation(true);
        return view;
    }

    // initliozation..............
    private void init() {
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        track_order_recycler_view = (RecyclerView) view.findViewById(R.id.track_order_recycler_view);
        track_Order_Layout = (LinearLayout) view.findViewById(R.id.track_Order_Layout);
        track_order_recycler_view.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        track_order_recycler_view.setLayoutManager(layoutManager);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        Typeface bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        title.setTypeface(bold);
    }

    // get all order by user...........
    public void getAllOrderByUser() {
        mData = new ArrayList<Data>();
        if (Utility.isOnline(context)) {
            DbHelper dbHelper = new DbHelper(context);
            final Data data = dbHelper.getUserData();
            if (data != null) {
                int loginId = data.getLoginID();
                if (loginId != 0) {
                    final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
                    dotDialog.show();
                    ServiceCaller serviceCaller = new ServiceCaller(context);
                    serviceCaller.callGetAllOrderByUserService(loginId, new IAsyncWorkCompletedCallback() {
                        @Override
                        public void onDone(String workName, boolean isComplete) {
                            if (isComplete) {
                                manipulateData();
                            } else {
                                // for empty data .............
                                noDataFound();
                            }
                            try {
                                if (dotDialog.isShowing()) {
                                    dotDialog.dismiss();
                                }
                            }catch (Exception x){
                                x.printStackTrace();
                            }
                        }

                    });
                }
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else {
            //  Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);//offline check..........
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            nodata.setText("No internet connection found");
            track_Order_Layout.setGravity(Gravity.CENTER);
            track_Order_Layout.removeAllViews();
            track_Order_Layout.addView(view);
        }

    }

    private void noDataFound() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        nodata.setTypeface(regular);
        nodataIcon.setTypeface(materialDesignIcons);
        nodataIcon.setText(Html.fromHtml("&#xf187;"));
        nodata.setText("No Order Yet Pending");

        track_Order_Layout.setGravity(Gravity.CENTER);
        track_Order_Layout.removeAllViews();
        track_Order_Layout.addView(view);
    }

    //manipulate data and short
    private void manipulateData() {
        DbHelper dbHelper = new DbHelper(context);
        List<Data> trackData = dbHelper.getGetAllOrderByUserData();
        Collections.sort(trackData, new Comparator<Data>() {
            //2016-07-08T12:06:30.664+00:00
            // 2017-09-13T12:32:04
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ", Locale.ENGLISH);
            //SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aaa");
            public int compare(Data m1, Data m2) {
                Date date1 = null;
                Date date2 = null;
                int shortdate = 0;
                try {
                    date1 = Utility.toCalendar(m1.getOrderTime());
                    date2 = Utility.toCalendar(m2.getOrderTime());
                    if (date1 != null && date2 != null) {
                        shortdate = date1.getTime() > date2.getTime() ? -1 : 1;//descending
                        //shortdate = date1.getTime() > date2.getTime() ? 1 : -1;     //ascending
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return shortdate;
            }
        });
        if (trackData != null) {
            for (Data data : trackData) {
                mData.add(data);
            }
        }
        if (mData != null) {
            TrackOrderAdapter trackOrderAdapter = new TrackOrderAdapter(context, mData);
            track_order_recycler_view.setAdapter(trackOrderAdapter);
        }
    }
}
