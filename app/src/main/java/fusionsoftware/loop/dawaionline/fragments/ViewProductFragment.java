package fusionsoftware.loop.dawaionline.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.adapter.ProductListAdapter;
import fusionsoftware.loop.dawaionline.adapter.SelectCatagoryAdapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.MyPojo;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.Contants;
import fusionsoftware.loop.dawaionline.utilities.Utility;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ViewProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int productId;
    private String mParam2;

    public ViewProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewProductFragment newInstance(int productId, String param2) {
        ViewProductFragment fragment = new ViewProductFragment();
        Bundle args = new Bundle();
        args.putInt("productId", productId);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt("productId");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Context context;
    View view;
    TextView txt_name, txt_price, txt_discount, txt_stock, decrement_Product, textView_nos, increase_Product, textView_addToCart, txt_subtitile, textView_app_name, textView_about;
    ImageView product_img, productImageCopy;
    WebView productDetails;
    CardView card_quantity;
    int count = 1;
    Result result;
    private ProductItemActionListener actionListener;
    List<Result> resultList;
    boolean isImageFitToScreen;
    View blue_view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_view_product, container, false);
        init();
        getProductDataById();
        setProductValue();
        return view;

    }

    public void setActionListener(ProductItemActionListener actionListener) {
        this.actionListener = actionListener;
    }


    private void init() {
        resultList=new ArrayList<>();
        txt_name = view.findViewById(R.id.txt_name);
        txt_price = view.findViewById(R.id.txt_price);
        txt_subtitile = view.findViewById(R.id.txt_subtitile);
        txt_discount = view.findViewById(R.id.txt_discount);
        product_img = view.findViewById(R.id.product_img);
        productImageCopy = view.findViewById(R.id.productImageCopy);
        txt_stock=view.findViewById(R.id.txt_stock);
        decrement_Product = view.findViewById(R.id.decrement_Product);
        textView_nos = view.findViewById(R.id.textView_nos);
        increase_Product = view.findViewById(R.id.increase_Product);
        productDetails = view.findViewById(R.id.productDetails);
        textView_addToCart = view.findViewById(R.id.textView_addToCart);
        card_quantity = view.findViewById(R.id.card_quantity);
        blue_view = view.findViewById(R.id.blue_view);
        textView_app_name = view.findViewById(R.id.textView_app_name);
        textView_about = view.findViewById(R.id.textView_about);

        increase_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = count + 1;
                setProductValue();
            }
        });

        decrement_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count > 1) {
                    count = count - 1;
                }
                setProductValue();
            }

        });

        textView_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DashboardActivity rootActivity = (DashboardActivity) context;
                if (rootActivity != null) {
                    if (actionListener != null)
                        actionListener.onItemTap(productImageCopy);
                    addItemToCart();
                    Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    assert vb != null;
                    vb.vibrate(20);
                    rootActivity.setItemCart();
                }

            }

        });

        product_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageFitToScreen){
                    isImageFitToScreen=false;
                    product_img.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    product_img.setAdjustViewBounds(true);
                }

                else {
                    isImageFitToScreen=true;
                    product_img.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    product_img.setScaleType(ImageView.ScaleType.FIT_XY);
//                    product_img.setForegroundGravity(Cent);
                    txt_name.setVisibility(View.GONE);
                    txt_subtitile.setVisibility(View.GONE);
                    txt_discount.setVisibility(View.GONE);
                    txt_price.setVisibility(View.GONE);
                    txt_stock.setVisibility(View.GONE);
                    increase_Product.setVisibility(View.GONE);
                    decrement_Product.setVisibility(View.GONE);
                    textView_nos.setVisibility(View.GONE);
                    productDetails.setVisibility(View.GONE);
                    textView_addToCart.setVisibility(View.GONE);
                    productImageCopy.setVisibility(View.GONE);
                    card_quantity.setVisibility(View.GONE);
                    textView_app_name.setVisibility(View.GONE);
                    blue_view.setVisibility(View.GONE);
                    textView_about.setVisibility(View.GONE);

                }
            }
        });

    }

    public void addItemToCart() {
        DbHelper dbHelper = new DbHelper(context);
        Result myBasket = new Result();
        myBasket.setId(resultList.get(0).getId());
        myBasket.setMc_name(resultList.get(0).getMc_name());
        myBasket.setProduct_name(resultList.get(0).getProduct_name());
        myBasket.setProduct_subtitle(resultList.get(0).getProduct_subtitle());
        myBasket.setProduct_mrp(resultList.get(0).getProduct_mrp());
        myBasket.setProduct_dis(resultList.get(0).getProduct_dis());
        myBasket.setQuantity(count);
        myBasket.setProduct_details(resultList.get(0).getProduct_details());
        myBasket.setPic(resultList.get(0).getPic());
        myBasket.setP_qty(resultList.get(0).getP_qty());
        myBasket.setIsoutofstock(resultList.get(0).getIsoutofstock());
        myBasket.setProduct_comp_name(resultList.get(0).getProduct_comp_name());

//        if (categoryName != null && !categoryName.equals("")) {
//            myBasket.setCategoryName(categoryName);
//        } else {
//            Result result = dbHelper.getCategoryData(FilteruserList.get(position).getCategoryId());
//            myBasket.setCategoryName(result.getCategoryName());
//        }
        dbHelper.upsertBasketOrderData(myBasket);
//        notify();
    }


    private void getProductDataById() {
        if (Utility.isOnline(context)) {
            BallTriangleDialog dialog=new BallTriangleDialog(context);
            dialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callAllProductListServiceById(productId, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    dialog.dismiss();
                    if (isComplete) {
                        MyPojo myPojo = new Gson().fromJson(workName, MyPojo.class);
                        for ( Result result : myPojo.getResult()) {
                            resultList.addAll(Arrays.asList(result));
                            txt_name.setText(result.getProduct_name());
                            txt_subtitile.setText(result.getProduct_subtitle());
                            txt_price.setText("\u20B9" + result.getProduct_mrp());
                            txt_discount.setText("Discount- \u20B9" + String.valueOf(result.getProduct_dis()) + " off");
//                            txt_stock.setText(resultList.get(i).getIsoutofstock());
                            productDetails.getSettings().setJavaScriptEnabled(true);
                            productDetails.loadData(result.getProduct_details(), "text/html", "UTF-8");

                        }

                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(context, Contants.OFFLINE_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void setProductValue() {
        textView_nos.setText("" + count);
        txt_stock.setText("Quantity-" + count);
    }

    public interface ProductItemActionListener {
        void onItemTap(ImageView imageView);
    }


}
