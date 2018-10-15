package fusionsoftware.loop.dawaionline.fragments;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import fusionsoftware.loop.dawaionline.adapter.ProductListAdapter;
import fusionsoftware.loop.dawaionline.adapter.SelectCatagoryAdapter;
import fusionsoftware.loop.dawaionline.adapter.SlidingImageSecondAdapter;
import fusionsoftware.loop.dawaionline.adapter.SlidingImage_Adapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.cartanimation.CircleAnimationUtil;
import fusionsoftware.loop.dawaionline.database.DbHelper;
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
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    ProductListAdapter adapter;

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
        linearLayout = view.findViewById(R.id.linearLayout);
        search.setOnClickListener(this);
        layout_medicin.setOnClickListener(this);
        layout_lab.setOnClickListener(this);
        layout_healthrecord.setOnClickListener(this);
        layout_earn.setOnClickListener(this);
        layout_healtharticle.setOnClickListener(this);
        layout_otc.setOnClickListener(this);
        btn_orderPre.setOnClickListener(this);
        getViewPagerData();
        getAllProductList();
//        viewPagerSetUp();
    }

    private void getViewPagerData() {
        if (Utility.isOnline(context)) {
//            final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
//            dotDialog.show();
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
//                        if (dotDialog.isShowing()) {
//                            dotDialog.dismiss();
//                        }
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
                showSearchDialog();
//                Toast.makeText(context, "Search Fragment Here", Toast.LENGTH_SHORT).show();
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
                AddDoctorPrescriptionFragment addDoctorPrescriptionFragment = AddDoctorPrescriptionFragment.newInstance("", "");
                moveFragment(addDoctorPrescriptionFragment);
                break;
        }
    }

    private void showSearchDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_search);
        recyclerView = dialog.findViewById(R.id.recycler_view);
        linearLayout = dialog.findViewById(R.id.linearLayout);
        EditText edt_search = dialog.findViewById(R.id.edt_search);
        TextView tv_close = dialog.findViewById(R.id.tv_close);
        tv_close.setOnClickListener(v -> {
            dialog.cancel();
        });
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<Result> productList = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(context);
        productList = dbHelper.GetAllSearchProductData();
        if (productList != null && productList.size() > 0) {
            adapter = new ProductListAdapter(context, productList);
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
                                Toast.makeText(context, "Add To Cart", Toast.LENGTH_SHORT).show();
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
        }
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    //get all Product list data
    private void getAllProductList() {
        if (Utility.isOnline(context)) {
//            final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
//            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callSearchProductService(new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
//                    if (isComplete) {
//                        if (productList != null && productList.size() > 0) {
//                            ProductListAdapter adapter = new ProductListAdapter(context, productList);
//                            recyclerView.setAdapter(adapter);
//                        }
//                    }
//                    Toast.makeText(context, workName, Toast.LENGTH_SHORT).show();
//                    if (dotDialog.isShowing()) {
//                        dotDialog.dismiss();
//                    }
                }
            });
        } else {
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

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
