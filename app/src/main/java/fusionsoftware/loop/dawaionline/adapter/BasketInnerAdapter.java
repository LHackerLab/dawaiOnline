package fusionsoftware.loop.dawaionline.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.fragments.MyBasketFragment;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.MyBasket;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.FontManager;

/**
 * Created by LALIT on 9/7/2017.
 */

public class BasketInnerAdapter extends RecyclerView.Adapter<BasketInnerAdapter.ViewHolder> {
    private List<Result> basketItemdata, categoryData;
    private Context context;
    private Typeface materialDesignIcons, medium, regular, bold;
    private String mc_Name;
    double total, grandTotal = 0.0;
    MyBasketFragment myBasketFragment;

    public BasketInnerAdapter(Context context, List<Result> basketItemdata, List<Result> categoryData, String mc_Name, MyBasketFragment myBasketFragment) {
        this.context = context;
        this.basketItemdata = basketItemdata;
        this.categoryData = categoryData;
        this.mc_Name = mc_Name;
        this.myBasketFragment = myBasketFragment;
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        this.bold = FontManager.getFontTypeface(context, "fonts/roboto.bold.ttf");
        this.materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_basket_inner, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        DecimalFormat df = new DecimalFormat("0.0");
        String qty = df.format(basketItemdata.get(i).getQuantity());
        viewHolder.Quantity.setText(qty);
        //calculate discounts & total....
        String price = df.format(basketItemdata.get(i).getProduct_mrp());
        total = Double.parseDouble(price) * Double.parseDouble(qty);
        viewHolder.total.setText("\u20B9" + df.format(total));//total price
        float dis = basketItemdata.get(i).getProduct_dis();
        double discount = (total / 100.0f) * dis;//calculate discount value
        viewHolder.discount.setText("\u20B9" + df.format(discount));
        viewHolder.grand_total.setText(df.format(total - discount));//grand total.
        grandTotal = grandTotal + (total - discount);// all categorygrand total....
        myBasketFragment.setMostTotal(grandTotal);

        viewHolder.item_name.setText(basketItemdata.get(i).getProduct_name());
        viewHolder.itemPrice.setText(String.valueOf(basketItemdata.get(i).getProduct_mrp()));
        viewHolder.increase_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count1 =  (int) basketItemdata.get(i).getQuantity();
                basketItemdata.get(i).setQuantity(count1 + 1);
                addProduct(i);
                notifyDataSetChanged();
            }
        });
        viewHolder.decrement_Product.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                int count1 =  (int)basketItemdata.get(i).getQuantity();
//                if (count1 > 1) {
//                    basketItemdata.get(i).setQuantity(count1 - 1);
//                    addProduct(i);
//                }
                notifyDataSetChanged();
            }
        });
        viewHolder.icon_delete.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(context);
                dbHelper.deleteBasketOrderDataById(basketItemdata.get(i).getId());
                basketItemdata.remove(i);
                if (basketItemdata.size() == 0) {//delete category data if all item deleted from this ctegory
                    dbHelper.deleteBasketOrderDataByCategoryName(mc_Name);
                    categoryData.remove(mc_Name);
                    if (categoryData.size() == 0) {
                        ((FragmentActivity) context).getSupportFragmentManager().popBackStack();//back to previes screen
                    } else {
                        Intent myIntent = new Intent("basketItem");
                        myIntent.putExtra("basketFlag", true);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                    }
                }
                grandTotal = 0;
                notifyDataSetChanged();
            }
        });
    }

    //add product in database
    private void addProduct(int position) {
        float Quantity = 0;
        DbHelper dbHelper = new DbHelper(context);
        Result myBasket = new Result();
        myBasket.setId(basketItemdata.get(position).getId());
        String productName = basketItemdata.get(position).getProduct_name();
        myBasket.setProduct_name(productName);
        myBasket.setMc_name(basketItemdata.get(position).getMc_name());
        Quantity = basketItemdata.get(position).getQuantity();
        myBasket.setQuantity(Quantity);
        myBasket.setProduct_mrp(basketItemdata.get(position).getProduct_mrp());
        myBasket.setProduct_dis(basketItemdata.get(position).getProduct_dis());
//        myBasket.setCategoryName(basketItemdata.get(position).getCategoryName());
        dbHelper.upsertBasketOrderData(myBasket);
//        if (Quantity == 0) {//if quantity 0 then delete order in data base
//            dbHelper.deleteBasketOrderDataByProductId(basketItemdata.get(position).getProductId());//delete item
//        }
        grandTotal = 0;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return basketItemdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView quantity_tv, Item_tv, price_tv, Quantity, item_name, itemPrice, total, discount, icon_rupeesgrandtotal, grand_total,
                icon_delete, icon_rupees, decrement_Product, increase_Product, tv_Total, tv_discount, tv_grandTotal;

        public ViewHolder(View view) {
            super(view);
            quantity_tv = (TextView) view.findViewById(R.id.quantity_tv);
            price_tv = (TextView) view.findViewById(R.id.price_tv);
            Item_tv = (TextView) view.findViewById(R.id.Item_tv);
            Quantity = (TextView) view.findViewById(R.id.Quantity);
            item_name = (TextView) view.findViewById(R.id.item_name);
            itemPrice = (TextView) view.findViewById(R.id.itemPrice);
            icon_delete = (TextView) view.findViewById(R.id.tv_delete);
            icon_rupees = (TextView) view.findViewById(R.id.icon_rupees);
            increase_Product = (TextView) view.findViewById(R.id.increase_Product);
            decrement_Product = (TextView) view.findViewById(R.id.decrement_Product);
            icon_rupeesgrandtotal = (TextView) view.findViewById(R.id.icon_rupeesgrandtotal);
            grand_total = (TextView) view.findViewById(R.id.grand_total);
            total = (TextView) view.findViewById(R.id.total);
            discount = (TextView) view.findViewById(R.id.discount);
            tv_Total = (TextView) view.findViewById(R.id.tv_Total);
            tv_discount = (TextView) view.findViewById(R.id.tv_discount);
            tv_grandTotal = (TextView) view.findViewById(R.id.tv_grandTotal);
            increase_Product.setTypeface(materialDesignIcons);
            decrement_Product.setTypeface(materialDesignIcons);
            increase_Product.setText(Html.fromHtml("&#xf419;"));
            decrement_Product.setText(Html.fromHtml("&#xf377;"));
            icon_delete.setTypeface(materialDesignIcons);
            icon_rupees.setTypeface(materialDesignIcons);
            icon_rupeesgrandtotal.setTypeface(materialDesignIcons);
            icon_delete.setText(Html.fromHtml("&#xf15a;"));
            icon_rupees.setText(Html.fromHtml("&#xf1af;"));
            icon_rupeesgrandtotal.setText(Html.fromHtml("&#xf1af;"));
            quantity_tv.setTypeface(medium);
            Item_tv.setTypeface(medium);
            Quantity.setTypeface(regular);
            item_name.setTypeface(regular);
            itemPrice.setTypeface(regular);
            total.setTypeface(regular);
            discount.setTypeface(regular);
            grand_total.setTypeface(regular);
            price_tv.setTypeface(medium);
            tv_Total.setTypeface(medium);
            tv_discount.setTypeface(medium);
            tv_grandTotal.setTypeface(bold);
        }
    }
}

