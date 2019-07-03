package fusionsoftware.loop.dawaionline.fragments;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.adapter.ProductListAdapter;
import fusionsoftware.loop.dawaionline.adapter.SelectCatagoryAdapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.cartanimation.CircleAnimationUtil;
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


public class ProductListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    // TODO: Rename and change types of parameters
    private int id;
    private String categoryName;


    public ProductListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProductListFragment newInstance(int id, String categoryName) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("categoryName", categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
            categoryName = getArguments().getString("categoryName");
        }
    }

    private LinearLayout orderPlaceLayout, searchItemLayout;
    private LinearLayout linearLayout;
    RecyclerView recyclerView;
    private Context context;
    View view;
    TextView tv_placeOrder, title;
    private List<Result> productList;
    Typeface materialDesignIcons, bold;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_list, container, false);
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setItemCart();
        rootActivity.setScreenSave(false);
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        init();
        getAllProductList();
        return view;
    }

    // initilization............
    private void init() {
        productList = new ArrayList<>();
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.CategoriesItems_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        // orderPlaceLayout = (LinearLayout) view.findViewById(R.id.orderPlaceLayout);
        //  tv_placeOrder = (TextView) view.findViewById(R.id.tv_placeOrder);

        bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        title = (TextView) view.findViewById(R.id.tv_title);

        // tv_placeOrder.setTypeface(bold);
        // orderPlaceLayout.setOnClickListener(this);

    }


    private void setProductData(List<Result> productList) {
        title.setText(categoryName);
        title.setTypeface(bold);
        if (productList != null && productList.size() > 0) {
            ProductListAdapter adapter = new ProductListAdapter(context, productList,categoryName);
            recyclerView.setAdapter(adapter);
            adapter.setActionListener(new ProductListAdapter.ProductItemActionListener() {
                @Override
                public void onItemTap(ImageView imageView) {
                    DashboardActivity rootActivity = (DashboardActivity) context;
                    if (rootActivity != null) {
                        TextView textView = (TextView) rootActivity.cart.findViewById(R.id.cart);
                        CircleAnimationUtil circleAnimationUtil = new CircleAnimationUtil();
                        circleAnimationUtil.attachActivity(getActivity()).setTargetView(imageView).setMoveDuration(200).setDestView(textView).setAnimationListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                //addItemToCart();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).startAnimation();
                        //  circleAnimationUtil.setOriginRect(133,50);
                    }
                }
            });
        } else {
            noDataFound();
        }
    }

    private void noDataFound() {
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);

        nodataIcon.setTypeface(materialDesignIcons);
        nodataIcon.setText(Html.fromHtml("&#xf187;"));
        nodata.setText("Any product not found for this Category");

        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.removeAllViews();
        linearLayout.addView(view);
    }


    //get all Product list data
    private void getAllProductList() {
        if (Utility.isOnline(context)) {
            ProgressDialog dialog=new ProgressDialog(context);
            dialog.setMessage("Loading Data...");
            dialog.show();
            ServiceCaller serviceCaller=new ServiceCaller(context);
            serviceCaller.callAllProductListService(categoryName, 9, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    dialog.dismiss();
//                    Toast.makeText(context, workName, Toast.LENGTH_SHORT).show();
                    if (isComplete){
                        MyPojo myPojo=new Gson().fromJson(workName, MyPojo.class);
                        for (Result result:myPojo.getResult()){
                            productList.addAll(Arrays.asList(result));
                        }
                        setProductData(productList);
                    }

                    else {
                        noDataFound();
                    }
                }
            });
        }

        else {
            // Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);//off line msg....
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            nodata.setText("No internet connection found");
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.removeAllViews();
            linearLayout.addView(view);
        }

    }

}
