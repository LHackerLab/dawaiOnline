package fusionsoftware.loop.dawaionline.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myshimmer.LalitRecyclerView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.adapter.SelectCatagoryAdapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.ContentDataAsArray;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.MyPojo;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


/**
 * Created by LALIT on 8/10/2017.
 */

public class SelectCategoryFragment extends Fragment {

    private String categoryName;
    private int storeId;


    // TODO: Rename and change types and number of parameters
    public static SelectCategoryFragment newInstance(String categoryName, int storeId) {
        SelectCategoryFragment fragment = new SelectCategoryFragment();
        Bundle args = new Bundle();
        args.putString("catName", categoryName);
        args.putInt("StoreId", storeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = getArguments().getString("catName");
            storeId = getArguments().getInt("StoreId");
        }
    }

    private SelectCatagoryAdapter adapter;
    private List<Result> categoryList;
    LalitRecyclerView recyclerView;
    StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private Context context;
    private LinearLayout linearLayout;
    View view;
    Typeface materialDesignIcons, regular;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        view = inflater.inflate(R.layout.fragment_select_catagory, container, false);
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setItemCart();
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        init();
        return view;
    }

    //set data in reycle view
    private void init() {
        categoryList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText("Choose Category");
        Typeface bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        title.setTypeface(bold);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setHasFixedSize(false);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.showShimmerAdapter();
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        getAllCategoryList();
    }

    //get all category list data
    private void getAllCategoryList() {
        if (Utility.isOnline(context)) {
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callAllCategoryListService(categoryName, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete){
                        DbHelper dbHelper = new DbHelper(context);
                        MyPojo myPojo=new Gson().fromJson(workName, MyPojo.class);
                        for (Result result:myPojo.getResult()){
                            categoryList = dbHelper.GetAllCategoryData();
                            categoryList.addAll(Arrays.asList(result));
                        }
                        if (categoryList != null && categoryList.size() > 0) {
                            adapter = new SelectCatagoryAdapter(context, categoryList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(adapter);
                            recyclerView.hideShimmerAdapter();
                        } else {
                            noDataFound();
                        }
                    } else {
                        noDataFound();
                    }

                    }

            });

        }
    }

    private void noDataFound() {
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodataIcon = view.findViewById(R.id.nodataIcon);
        TextView nodata = view.findViewById(R.id.nodata);
        nodata.setTypeface(regular);
        nodataIcon.setTypeface(materialDesignIcons);
        nodataIcon.setText(Html.fromHtml("&#xf187;"));
        nodata.setText("Any Category not found for this Store");
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.removeAllViews();
        linearLayout.addView(view);
    }

}
