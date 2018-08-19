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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.MyBasket;
import fusionsoftware.loop.dawaionline.model.Response;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


/**
 * Created by user on 8/9/2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> implements Filterable {

    private List<Result> productData, FilteruserList;
    private Context context;
    //HashSet<Integer> selectedPosition = new HashSet<>();
    private Typeface materialDesignIcons, medium, italic, regular;
    private ProductItemActionListener actionListener;
    String categoryName;

    public ProductListAdapter(Context context, List<Result> productList) {
        this.context = context;
        this.productData = productList;
        this.FilteruserList = productList;
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        this.italic = FontManager.getFontTypeface(context, "fonts/roboto.italic.ttf");
        this.materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
    }

    public void setActionListener(ProductItemActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public ProductListAdapter(Context context, List<Result> productData, String categoryName) {
        this.context = context;
        this.productData = productData;
        this.FilteruserList = productData;
        this.categoryName = categoryName;
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
        Picasso.with(context).load(FilteruserList.get(position).getProductPicturesUrl()).resize(800, 800).placeholder(R.drawable.logo).into(viewHolder.productImage);
        Picasso.with(context).load(FilteruserList.get(position).getProductPicturesUrl()).resize(800, 800).placeholder(R.drawable.logo).into(viewHolder.productImageCopy);
        viewHolder.productTitle.setText(FilteruserList.get(position).getProductName());
        viewHolder.productSubTitle.setText(FilteruserList.get(position).getProductSubTitle());
        viewHolder.productPrice.setText(String.valueOf(FilteruserList.get(position).getUnitPrice()));
        viewHolder.productDetails.setText(FilteruserList.get(position).getProductDetails());
        viewHolder.tv_discount.setText(String.valueOf(FilteruserList.get(position).getDiscount()) + "% off");
        if (FilteruserList.get(position).getCountValue() != 0) {
            DecimalFormat df = new DecimalFormat("0");
            String value = df.format(FilteruserList.get(position).getCountValue());
            viewHolder.textView_nos.setText(value);
        }

        viewHolder.increase_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count1 = (int) FilteruserList.get(position).getCountValue();
                FilteruserList.get(position).setCountValue(count1 + 1);
                //   addProduct(count1 + 1, position, true);
                notifyDataSetChanged();
            }
        });
        viewHolder.decrement_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count1 = (int) FilteruserList.get(position).getCountValue();
                if (count1 > 1) {
                    FilteruserList.get(position).setCountValue(count1 - 1);
                    // addProduct(count1 - 1, position, true);
                }
                notifyDataSetChanged();
            }

        });
        viewHolder.textView_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DashboardActivity rootActivity = (DashboardActivity) context;
                if (rootActivity != null) {
                    if (actionListener != null)
                        actionListener.onItemTap(viewHolder.productImageCopy);
                    addItemToCart(position);
                    Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    assert vb != null;
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
        myBasket.setProductId(FilteruserList.get(position).getProductId());
        String productName = FilteruserList.get(position).getProductName();
        myBasket.setProductName(productName);
        myBasket.setCategoryId(FilteruserList.get(position).getCategoryId());
        myBasket.setQuantity(FilteruserList.get(position).getCountValue());
        myBasket.setPrice(FilteruserList.get(position).getUnitPrice());
        myBasket.setDiscount(FilteruserList.get(position).getDiscount());
        myBasket.setOrderTime(getCurrentDateTime());
        if (categoryName != null && !categoryName.equals("")) {
            myBasket.setCategoryName(categoryName);
        } else {
            Result result = dbHelper.getCategoryData(FilteruserList.get(position).getCategoryId());
            myBasket.setCategoryName(result.getCategoryName());
        }
        dbHelper.upsertBasketOrderData(myBasket);
        notifyDataSetChanged();
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
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().trim();
                // name match condition. this might differ depending on your requirement
                // here we are looking for name or phone number match
                if (charString.isEmpty()) {
                    FilteruserList = productData;
                } else {
                    List<Result> filteredList = new ArrayList<>();
                    for (Result row : productData) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getProductName().toLowerCase().trim().contains(charString.toLowerCase()) | row.getProductSubTitle().toLowerCase().trim().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }


                    FilteruserList = filteredList;
                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = FilteruserList;
                return filterResults;
            }


            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                FilteruserList = (ArrayList<Result>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return FilteruserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productTitle, tv_or, tv_discount, productPrice, rupees_icon, productDetails, textView_addToCart,
                productSubTitle, increase_Product, textView_nos, decrement_Product;
        ImageView productImage, productImageCopy;
        LinearLayout orderLayout;
        CardView card_view;

        public ViewHolder(View view) {
            super(view);
            productTitle = (TextView) view.findViewById(R.id.productTitle);
            productDetails = (TextView) view.findViewById(R.id.productDetails);
            productPrice = (TextView) view.findViewById(R.id.productPrice);
            productImage = (ImageView) view.findViewById(R.id.productImage);
            productImageCopy = (ImageView) view.findViewById(R.id.productImageCopy);
            rupees_icon = (TextView) view.findViewById(R.id.rupees_icon);
            tv_or = (TextView) view.findViewById(R.id.tv_or);
            textView_addToCart = (TextView) view.findViewById(R.id.textView_addToCart);
            orderLayout = (LinearLayout) view.findViewById(R.id.orderLayout);
            card_view = (CardView) view.findViewById(R.id.card_view);
            tv_discount = (TextView) view.findViewById(R.id.tv_discount);
            textView_nos = (TextView) view.findViewById(R.id.textView_nos);
            increase_Product = (TextView) view.findViewById(R.id.increase_Product);
            decrement_Product = (TextView) view.findViewById(R.id.decrement_Product);
            productSubTitle = (TextView) view.findViewById(R.id.productSubTitle);
            productTitle.setTypeface(medium);
            rupees_icon.setTypeface(materialDesignIcons);
            rupees_icon.setText(Html.fromHtml("&#xf1af;"));
            productDetails.setTypeface(regular);
            productPrice.setTypeface(medium);
            textView_addToCart.setTypeface(medium);
            productSubTitle.setTypeface(regular);
            tv_discount.setTypeface(italic);
        }
    }

    public interface ProductItemActionListener {
        void onItemTap(ImageView imageView);
    }
}
