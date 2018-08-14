package fusionsoftware.loop.dawaionline.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.adapter.SelectLocalityAdapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.Contants;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


/**
 * Created by LALIT on 8/14/2017.
 */
public class LocationFragment extends Fragment implements View.OnClickListener {

    private String mParam1;
    private Boolean navigationFlag=false;

    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, Boolean param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putBoolean("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("param1");
            navigationFlag = getArguments().getBoolean("param2");
        }
    }

    View view;
    TextView search_icon, title, select_city_textView;
    LinearLayout selectLocalityLayout;
    List<Data> LocalityArrayLists;
    ArrayList<Data> mAllLocalityData;
    RecyclerView recyclerView;
    Dialog dialog;
    EditText locality_search;
    LinearLayout location_layout;
    private Context context;
    private SelectLocalityAdapter localityAdapter;
    Typeface materialdesignicons_font, regular;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.fragment_location, container, false);
        init();
        GetAllLocalitiesService();// for localitys...............

        return view;
    }


    //initialize views..................
    private void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        if (rootActivity != null) {
            rootActivity.setScreencart(false);
            rootActivity.setScreenSave(false);
            rootActivity.setScreenFavourite(false);
            rootActivity.setScreenLocation(false);
            rootActivity.setScreenCartDot(false);
        }
        search_icon = (TextView) view.findViewById(R.id.search_icon);
        locality_search = (EditText) view.findViewById(R.id.locality_search);
        selectLocalityLayout = (LinearLayout) view.findViewById(R.id.selectLocality);
        recyclerView = (RecyclerView) view.findViewById(R.id.locality_list);
        location_layout = (LinearLayout) view.findViewById(R.id.location_layout);
        title = (TextView) view.findViewById(R.id.tv_title);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        setIcon();

    }

    private void setvalue() {
        final DbHelper dbHelper = new DbHelper(context);
        selectLocalityLayout.setOnClickListener(this);
        locality_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                String charText = locality_search.getText().toString().toLowerCase(Locale.getDefault());
                if (charText != null) {
                    LocalityArrayLists.clear();
                    List<Data> searchList;
                    searchList = dbHelper.getAllLocalitiesBySearchField(charText);
                    if (charText.length() == 0) {
                        LocalityArrayLists.addAll(mAllLocalityData);
                    } else {
                        LocalityArrayLists.addAll(searchList);
                    }
                    localityAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        selectLocality();
    }

    //    set icons.................
    private void setIcon() {
        Typeface bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        title.setTypeface(bold);
        search_icon.setTypeface(materialdesignicons_font);
        search_icon.setText(Html.fromHtml("&#xf349;"));
        locality_search.setTypeface(regular);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //  case R.id.selectCityLayout:
            //selectCity();// for citys.....................
            // break;
        }
    }

    //select city
    private void selectCity() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_location);
        dialog.setTitle("City List");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        ListView listView = (ListView) dialog.findViewById(R.id.cityList);
        dialog.show();

    }

    //    select Locality
    private void selectLocality() {
        DbHelper dbHelper = new DbHelper(context);
        mAllLocalityData = new ArrayList<Data>();
        LocalityArrayLists = new ArrayList<Data>();
        LocalityArrayLists = dbHelper.getAllLocalitiesData();
        if (LocalityArrayLists != null) {
            mAllLocalityData.addAll(LocalityArrayLists);
            localityAdapter = new SelectLocalityAdapter(context, LocalityArrayLists,navigationFlag);
            recyclerView.setAdapter(localityAdapter);
            locality_search.setText("");
        } else {
            nodataFound();
        }
    }

    private void nodataFound() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        nodata.setTypeface(regular);
        nodataIcon.setTypeface(materialdesignicons_font);
        nodataIcon.setText(Html.fromHtml("&#xf187;"));
        nodata.setText("Any location not found  ");
        location_layout.setGravity(Gravity.CENTER);
        location_layout.removeAllViews();
        location_layout.addView(view);
    }


    //    GetAllLocalities api
    private void GetAllLocalitiesService() {
        if (Utility.isOnline(context)) {
            final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.getAllLocalitiesService(Contants.LIST_PAGE_SIZE, 1, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        setvalue();
                    } else {
                        nodataFound();
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            //Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            nodata.setText("No internet connection found");
            location_layout.setGravity(Gravity.CENTER);
            location_layout.removeAllViews();
            location_layout.addView(view);
        }
    }

}
