package fusionsoftware.loop.dawaionline.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.MyBasket;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


/**
 * Created by user on 8/9/2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private List<Data> productData;
    private Context context;
    //HashSet<Integer> selectedPosition = new HashSet<>();
    private int storeId;
    private int categoryId;
    private Typeface materialDesignIcons, medium, italic, regular;
    private ProductItemActionListener actionListener;

    public void setActionListener(ProductItemActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public ProductListAdapter(Context context, List<Data> productData, int storeId, int categoryId) {
        this.context = context;
        this.productData = productData;
        this.storeId = storeId;
        this.categoryId = categoryId;
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        this.italic = FontManager.getFontTypeface(context, "fonts/roboto.italic.ttf");
        this.materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Picasso.with(context).load(productData.get(position).getProductPicturesUrl()).resize(800, 800).placeholder(R.drawable.logo).into(viewHolder.productImage);
        Picasso.with(context).load(productData.get(position).getProductPicturesUrl()).resize(800, 800).placeholder(R.drawable.logo).into(viewHolder.productImageCopy);
        viewHolder.productTitle.setText(productData.get(position).getProductName());
        if (productData.get(position).getUnitPrice() != 0) {
            if (productData.get(position).getUOM() != null) {
                viewHolder.productPrice.setText(String.valueOf(productData.get(position).getUnitPrice()) + " " + productData.get(position).getUOM());
            } else {
                viewHolder.productPrice.setText(String.valueOf(productData.get(position).getUnitPrice()));
            }
        }
        if (productData.get(position).getProductDetails() != null) {
            viewHolder.productDetails.setText(productData.get(position).getProductDetails());
        } else {
            viewHolder.productDetails.setVisibility(View.GONE);


        }
        viewHolder.tv_discount.setText(String.valueOf(productData.get(position).getDiscount()) + "% off");
//        if (selectedPosition.contains(position)) {
//            viewHolder.orderLayout.setVisibility(View.VISIBLE);
//            viewHolder.icon_drop_down.setText(Html.fromHtml("&#xf143;"));
//        } else {
//            viewHolder.orderLayout.setVisibility(View.GONE);
//            viewHolder.icon_drop_down.setText(Html.fromHtml("&#xf140;"));
//        }
        if (productData.get(position).getCountValue() != 0) {

            if (productData.get(position).getUOM() != null && productData.get(position).getUOM().equals("kg")) {
                viewHolder.textView_nos.setText(String.valueOf(productData.get(position).getCountValue()));
            } else {
                DecimalFormat df = new DecimalFormat("0");
                String value = df.format(productData.get(position).getCountValue());
                viewHolder.textView_nos.setText(value);
            }
        }

        viewHolder.increase_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float count = productData.get(position).getCountValue();
                if (productData.get(position).getUOM() != null && productData.get(position).getUOM().equals("kg")) {
                    productData.get(position).setCountValue((float) (count + 0.5));
                    //  addProduct((float) (count + 0.5), position, true);
                } else {
                    int count1 = (int) productData.get(position).getCountValue();
                    productData.get(position).setCountValue(count1 + 1);
                    //   addProduct(count1 + 1, position, true);
                }
                notifyDataSetChanged();
            }
        });
        viewHolder.decrement_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productData.get(position).getUOM() != null && productData.get(position).getUOM().equals("kg")) {
                    float count = productData.get(position).getCountValue();
                    if (count > 0.5) {
                        productData.get(position).setCountValue((float) (count - 0.5));
                        // addProduct((float) (count - 0.5), position, true);
                    }
                } else {
                    int count1 = (int) productData.get(position).getCountValue();
                    if (count1 > 1) {
                        productData.get(position).setCountValue(count1 - 1);
                        // addProduct(count1 - 1, position, true);
                    }
                }
                notifyDataSetChanged();
            }

        });
//        viewHolder.icon_drop_down.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (selectedPosition.contains(position)) {
//                    selectedPosition.clear();
//                } else {
//                    selectedPosition.clear();
//                    selectedPosition.add(position);
//                    notifyItemChanged(position);
//                }
//                notifyDataSetChanged();
//            }
//        });
        viewHolder.textView_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, productData.get(position).getCountValue() + " " + productData.get(position).getUOM() + productName + " ADD Into Your Card", Toast.LENGTH_LONG).show();
                final DashboardActivity rootActivity = (DashboardActivity) context;
                if (rootActivity != null) {
                    if (actionListener != null)
                        actionListener.onItemTap(viewHolder.productImageCopy);
                    addItemToCart(position);

                    Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vb.vibrate(20);
                    rootActivity.setItemCart();
                }

            }

        });
    }

    // add items in cart.............
    public void addItemToCart(int position) {
        DbHelper dbHelper = new DbHelper(context);
        MyBasket myBasket = new MyBasket();
        myBasket.setProductId(productData.get(position).getProductId());
        String productName = productData.get(position).getProductName();
        myBasket.setProductName(productName);
        myBasket.setStoreId(storeId);
        myBasket.setCategoryId(categoryId);
        if (productData.get(position).getUOM() != null && productData.get(position).getUOM().equalsIgnoreCase("kg")) {
            myBasket.setQuantity(productData.get(position).getCountValue());
        } else {
            myBasket.setQuantity((float) productData.get(position).getCountValue());
        }
        myBasket.setPrice(productData.get(position).getUnitPrice());
        myBasket.setDiscount(productData.get(position).getDiscount());
        myBasket.setUOM(productData.get(position).getUOM());
        if (getCurrentDateTime() != null) {
            myBasket.setOrderTime(getCurrentDateTime());
        }
        Data storeData = dbHelper.getStoreData(storeId);//get store details
        if (storeData != null) {
            storeData.setCategoryId(categoryId);
            dbHelper.upsertSelectedStoreData(storeData);
            dbHelper.upsertBasketOrderData(myBasket);
        }
        notifyDataSetChanged();
    }

    //add product in database
    private void addProduct(float quantity, int position, Boolean orderAddOrDelete) {
        DbHelper dbHelper = new DbHelper(context);
        MyBasket myBasket = new MyBasket();
        myBasket.setProductId(productData.get(position).getProductId());
        String productName = productData.get(position).getProductName();
        myBasket.setProductName(productName);
        myBasket.setStoreId(storeId);
        myBasket.setCategoryId(categoryId);
        //myBasket.setQuantity(productQuantity);
        myBasket.setPrice(productData.get(position).getUnitPrice());
        myBasket.setDiscount(productData.get(position).getDiscount());
        myBasket.setUOM(productData.get(position).getUOM());
        if (getCurrentDateTime() != null) {
            myBasket.setOrderTime(getCurrentDateTime());
        }
        Data storeData = dbHelper.getStoreData(storeId);//get store details
        if (storeData != null) {
            storeData.setCategoryId(categoryId);
            storeData.setStoreId(storeId);
            dbHelper.upsertSelectedStoreData(storeData);
            dbHelper.upsertBasketOrderData(myBasket);
        }
        notifyDataSetChanged();

    }


    //calculate product price with the help of Quantity
    private String getProductPrice(int position) {
        productData.get(position).getUnitPrice();
        return "";
    }

    //save basket id till order not place and basket not clear
    public void saveBasketIdpreference(int id) {
        SharedPreferences preferenceForId = context.getSharedPreferences("SaveBasketId", context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferenceForId.edit();
        edit.putInt("BasketId", id);
        edit.commit();
    }

    //get current date time
    private String getCurrentDateTime() {
        String dateTimeStr = null;
        Date currentTime = Calendar.getInstance().getTime();
        String dateTime = Utility.convertDate(currentTime);
        String[] startArray = dateTime.split(",");
        //get value based on array position Wednesday,15,Jun,6,30,AM,2000,06 index(0,1,2,3,4,5,6,7)
        dateTimeStr = startArray[1] + "" + startArray[2] + " " + startArray[6];
        return dateTimeStr;
    }

    @Override
    public int getItemCount() {
        return productData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle, tv_notice, tv_or, tv_discount, productPrice, icon_drop_down, done_icon, rupees_icon, productDetails, textView_addToCart,
                selectItem, wieght_1kg, increase_Product, textView_nos, decrement_Product;
        ImageView productImage, productImageCopy;
        EditText enterManually;
        LinearLayout orderLayout, linearLayout_liter, laout_or, layout_enter_manually, linearLayout_weight1;
        CardView card_view;

        public ViewHolder(View view) {
            super(view);
            productTitle = (TextView) view.findViewById(R.id.productTitle);
            productDetails = (TextView) view.findViewById(R.id.productDetails);
            productPrice = (TextView) view.findViewById(R.id.productPrice);
            productImage = (ImageView) view.findViewById(R.id.productImage);
            productImageCopy = (ImageView) view.findViewById(R.id.productImageCopy);
            icon_drop_down = (TextView) view.findViewById(R.id.icon_drop_down);
            rupees_icon = (TextView) view.findViewById(R.id.rupees_icon);
            tv_notice = (TextView) view.findViewById(R.id.tv_notice);
            tv_or = (TextView) view.findViewById(R.id.tv_or);
            textView_addToCart = (TextView) view.findViewById(R.id.textView_addToCart);
            orderLayout = (LinearLayout) view.findViewById(R.id.orderLayout);
            card_view = (CardView) view.findViewById(R.id.card_view);
            tv_discount = (TextView) view.findViewById(R.id.tv_discount);
            textView_nos = (TextView) view.findViewById(R.id.textView_nos);
            increase_Product = (TextView) view.findViewById(R.id.increase_Product);
            decrement_Product = (TextView) view.findViewById(R.id.decrement_Product);
            productTitle.setTypeface(medium);
//            icon_drop_down.setTypeface(materialDesignIcons);
//            icon_drop_down.setText(Html.fromHtml("&#xf140;"));
            rupees_icon.setTypeface(materialDesignIcons);
            rupees_icon.setText(Html.fromHtml("&#xf1af;"));
            productDetails.setTypeface(regular);
            productPrice.setTypeface(medium);
            textView_addToCart.setTypeface(medium);
            tv_notice.setTypeface(regular);
            tv_discount.setTypeface(italic);
        }
    }

    public interface ProductItemActionListener {
        void onItemTap(ImageView imageView);
    }
}
