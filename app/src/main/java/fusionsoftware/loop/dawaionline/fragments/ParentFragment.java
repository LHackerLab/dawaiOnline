package fusionsoftware.loop.dawaionline.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.adapter.SelectCatagoryAdapter;
import fusionsoftware.loop.dawaionline.adapter.SlidingImageSecondAdapter;
import fusionsoftware.loop.dawaionline.adapter.SlidingImage_Adapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.ContentDataAsArray;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;
import fusionsoftware.loop.dawaionline.viewpagerindicator.CirclePageIndicator;


/**
 * Created by LALIT on 8/10/2017.
 */

public class ParentFragment extends Fragment implements View.OnClickListener {

    private int menuId;
    private int storeId;


    // TODO: Rename and change types and number of parameters
    public static ParentFragment newInstance(int menuId, int storeId) {
        ParentFragment fragment = new ParentFragment();
        Bundle args = new Bundle();
        args.putInt("MenuId", menuId);
        args.putInt("StoreId", storeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            menuId = getArguments().getInt("MenuId");
            storeId = getArguments().getInt("StoreId");
        }
    }

    private Context context;
    View view;
    Typeface materialDesignIcons, regular;
    RelativeLayout layout_medicin, layout_lab, layout_earn, layout_healthrecord, layout_healtharticle, layout_otc;
    TextView search;
    Button btn_orderPre;
    CirclePageIndicator circlePageIndicator, circlePageIndicatorTwo;
    private static ViewPager mPager, pagertwo;
    private static int currentPage = 0, currentPageTwo = 0;
    private static int NUM_PAGES = 0, NUM_PAGESTwo = 0;
    private ArrayList<String> ImagesArray, ImagesArrayTwo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        view = inflater.inflate(R.layout.fragment_parent_menu, container, false);
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        init();
        return view;
    }

    //set data in reycle view
    private void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenCartDot(true);
        rootActivity.setItemCart();
        ImagesArray = new ArrayList<String>();
        ImagesArrayTwo = new ArrayList<String>();
        search = view.findViewById(R.id.tv_search);
        layout_medicin = view.findViewById(R.id.layout_medicin);
        layout_lab = view.findViewById(R.id.layout_lab);
        layout_earn = view.findViewById(R.id.layout_earn);
        layout_healthrecord = view.findViewById(R.id.layout_healthrecord);
        layout_healtharticle = view.findViewById(R.id.layout_healtharticle);
        layout_otc = view.findViewById(R.id.layout_otc);
        btn_orderPre = view.findViewById(R.id.btn_orderPre);
        search.setOnClickListener(this);
        layout_medicin.setOnClickListener(this);
        layout_lab.setOnClickListener(this);
        layout_healthrecord.setOnClickListener(this);
        layout_earn.setOnClickListener(this);
        layout_healtharticle.setOnClickListener(this);
        layout_otc.setOnClickListener(this);
        btn_orderPre.setOnClickListener(this);
        getViewPagerData();
//        viewPagerSetUp();
    }

    private void getViewPagerData() {
        if (Utility.isOnline(context)) {
            final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callViewPagerService(new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        ContentDataAsArray contentDataAsArray = new Gson().fromJson(workName, ContentDataAsArray.class);
                        for (Result result : contentDataAsArray.getResults()) {
                            ImagesArray.addAll(Arrays.asList(result.getFirstViewPager()));
                            ImagesArrayTwo.addAll(Arrays.asList(result.getSecondViewPager()));
                        }
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                        viewPagerSetUp();
                    }
                }
            });
        } else

        {
            Toast.makeText(context, "No internet connection found", Toast.LENGTH_SHORT).show();
        }

    }

    private void viewPagerSetUp() {
        //------viwepagerss settings...........
        mPager = (ViewPager) view.findViewById(R.id.pager);
        pagertwo = (ViewPager) view.findViewById(R.id.pagertwo);
        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        circlePageIndicatorTwo = (CirclePageIndicator) view.findViewById(R.id.indicatortwo);
        NUM_PAGES = ImagesArray.size();
        NUM_PAGESTwo = ImagesArrayTwo.size();
        if (ImagesArray != null && ImagesArray.size() > 0) {
            mPager.setAdapter(new SlidingImage_Adapter(context, ImagesArray));
            circlePageIndicator.setViewPager(mPager);
        }
        if (ImagesArrayTwo != null && ImagesArrayTwo.size() > 0) {
            pagertwo.setAdapter(new SlidingImageSecondAdapter(context, ImagesArrayTwo));
            circlePageIndicatorTwo.setViewPager(pagertwo);
        }
        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        circlePageIndicator.setRadius(5 * density);
        circlePageIndicatorTwo.setRadius(5 * density);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
//for secnd view pager
                if (currentPageTwo == NUM_PAGESTwo) {
                    currentPageTwo = 0;
                }
                pagertwo.setCurrentItem(currentPageTwo++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new

                                    TimerTask() {
                                        @Override
                                        public void run() {
                                            handler.post(Update);
                                        }
                                    }, 3000, 3000);

        // Pager listener over indicator
        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener()

        {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
        // Pager listener over indicator
        circlePageIndicatorTwo.setOnPageChangeListener(new ViewPager.OnPageChangeListener()

        {

            @Override
            public void onPageSelected(int position) {
                currentPageTwo = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                Toast.makeText(context, "Search Fragment Here", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_medicin:
                SelectCategoryFragment fragment = SelectCategoryFragment.newInstance(0, 0);
                moveFragment(fragment);
                break;
            case R.id.layout_lab:
                LabTestsFragment fragmentest = LabTestsFragment.newInstance("", "");
                moveFragment(fragmentest);
                break;
            case R.id.layout_otc:
                SelectCategoryFragment fragmentotc = SelectCategoryFragment.newInstance(0, 0);
                moveFragment(fragmentotc);
                break;
            case R.id.layout_healtharticle:
                HealthArticleFragment healthArticleFragment = HealthArticleFragment.newInstance("", "");
                moveFragment(healthArticleFragment);
                break;
            case R.id.layout_healthrecord:
                HealthRecordFragment healthFragment = HealthRecordFragment.newInstance("", "");
                moveFragment(healthFragment);
                break;
            case R.id.layout_earn:
                break;
            case R.id.btn_orderPre:
                Toast.makeText(context, "Order Preciption Here", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
