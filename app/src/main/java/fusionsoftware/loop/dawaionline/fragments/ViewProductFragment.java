package fusionsoftware.loop.dawaionline.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
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
    TextView txt_name, txt_price, txt_discount, txt_stock, decrement_Product, textView_nos, increase_Product, textView_addToCart, txt_subtitile;
    ImageView product_img;
    WebView productDetails;
    int count = 1;

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


    private void init() {
        txt_name = view.findViewById(R.id.txt_name);
        txt_price = view.findViewById(R.id.txt_price);
        txt_subtitile = view.findViewById(R.id.txt_subtitile);
        txt_discount = view.findViewById(R.id.txt_discount);
        product_img = view.findViewById(R.id.product_img);
        txt_stock=view.findViewById(R.id.txt_stock);
        decrement_Product = view.findViewById(R.id.decrement_Product);
        textView_nos = view.findViewById(R.id.textView_nos);
        increase_Product = view.findViewById(R.id.increase_Product);
        productDetails = view.findViewById(R.id.productDetails);
        textView_addToCart = view.findViewById(R.id.textView_addToCart);

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
    }


//    public void addItemToCart(int position) {
//        DbHelper dbHelper = new DbHelper(context);
//        Result myBasket = new Result();
//        myBasket.setId(resultList.get(position).getId());
//        myBasket.setMc_name(resultList.get(position).getMc_name());
//        myBasket.setProduct_name(resultList.get(position).getProduct_name());
//        myBasket.setProduct_subtitle(resultList.get(position).getProduct_subtitle());
//        myBasket.setProduct_mrp(resultList.get(position).getProduct_mrp());
//        myBasket.setProduct_dis(resultList.get(position).getProduct_dis());
//        myBasket.setQuantity(resultList.get(position).getCountValue());
//        myBasket.setProduct_details(resultList.get(position).getProduct_details());
//        myBasket.setPic(resultList.get(position).getPic());
//        myBasket.setP_qty(resultList.get(position).getP_qty());
//        myBasket.setIsoutofstock(resultList.get(position).getIsoutofstock());
//        myBasket.setProduct_comp_name(resultList.get(position).getProduct_comp_name());
//
////        if (categoryName != null && !categoryName.equals("")) {
////            myBasket.setCategoryName(categoryName);
////        } else {
////            Result result = dbHelper.getCategoryData(FilteruserList.get(position).getCategoryId());
////            myBasket.setCategoryName(result.getCategoryName());
////        }
//        dbHelper.upsertBasketOrderData(myBasket);
//        notify();
//    }

    private void getProductDataById() {
        if (Utility.isOnline(context)) {
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setMessage("Fetching Product...");
            dialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callAllProductListServiceById(productId, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    dialog.dismiss();
                    if (isComplete) {
                        MyPojo myPojo = new Gson().fromJson(workName, MyPojo.class);
                        for (Result result : myPojo.getResult()) {
                            txt_name.setText(result.getProduct_name());
                            txt_subtitile.setText(result.getProduct_subtitle());
                            txt_price.setText("\u20B9" + result.getProduct_mrp());
                            txt_discount.setText("Discount- \u20B9" + String.valueOf(result.getProduct_dis()) + " off");
//                            txt_stock.setText(resultList.get(i).getIsoutofstock());
                            productDetails.getSettings().setJavaScriptEnabled(true);
                            productDetails.loadData(result.getProduct_details(), "text/html", "UTF-8");

                            textView_addToCart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final DashboardActivity rootActivity = (DashboardActivity) context;
                                    if (rootActivity != null) {
//                                        if (actionListener != null)
//                                            actionListener.onItemTap(viewHolder.productImageCopy);
//                                        addItemToCart(i);
                                        Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                                        assert vb != null;
                                        vb.vibrate(20);
                                        rootActivity.setItemCart();
                                    }

                                }

                            });

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


}
