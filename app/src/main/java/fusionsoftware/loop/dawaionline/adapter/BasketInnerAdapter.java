package fusionsoftware.loop.dawaionline.adapter;

import android.content.Context;
import android.content.Intent;
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
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.MyBasket;
import fusionsoftware.loop.dawaionline.utilities.FontManager;

/**
 * Created by LALIT on 9/7/2017.
 */

public class BasketInnerAdapter extends RecyclerView.Adapter<BasketInnerAdapter.ViewHolder> {
    private List<MyBasket> basketItemdata, categoryData;
    private Context context;
    private Typeface materialDesignIcons, medium, regular, bold;
    private int categoryPosition,categoryId;

    public BasketInnerAdapter(Context context, List<MyBasket> basketItemdata, List<MyBasket> categoryData, int categoryPosition,int categoryId) {
        this.context = context;
        this.basketItemdata = basketItemdata;
        this.categoryData = categoryData;
        this.categoryPosition = categoryPosition;
        this.categoryId = categoryId;
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
        if (basketItemdata.get(i).getQuantity() != 0) {
            DecimalFormat df = new DecimalFormat("0");
            String value = df.format(basketItemdata.get(i).getQuantity());
            viewHolder.Quantity.setText(value);
        }

        viewHolder.item_name.setText(basketItemdata.get(i).getProductName());
        viewHolder.itemPrice.setText(String.valueOf(basketItemdata.get(i).getPrice()));
        viewHolder.increase_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count1 = (int) basketItemdata.get(i).getQuantity();
                basketItemdata.get(i).setQuantity(count1 + 1);
                addProduct(i);
                notifyDataSetChanged();
            }
        });
        viewHolder.decrement_Product.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                int count1 = (int) basketItemdata.get(i).getQuantity();
                if (count1 > 1) {
                    basketItemdata.get(i).setQuantity(count1 - 1);
                    addProduct(i);
                }
                notifyDataSetChanged();
            }
        });
        viewHolder.icon_delete.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(context);
                dbHelper.deleteBasketOrderDataByProductId(basketItemdata.get(i).getProductId());
                basketItemdata.remove(i);
                if (basketItemdata.size() == 0) {//delete store data if all item deleted from this store
                    dbHelper.deleteBasketOrderDataByCategoryId(categoryId);
                    categoryData.remove(categoryPosition);
                    if (categoryData.size() == 0) {
                        ((FragmentActivity) context).getSupportFragmentManager().popBackStack();//back to profile screen
                    } else {
                        Intent myIntent = new Intent("basketItem");
                        myIntent.putExtra("basketFlag", true);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                    }
                    }
                    notifyDataSetChanged();
            }
        });
    }

    //add product in database
    private void addProduct(int position) {
        float Quantity = 0;
        DbHelper dbHelper = new DbHelper(context);
        MyBasket myBasket = new MyBasket();
        myBasket.setProductId(basketItemdata.get(position).getProductId());
        String productName = basketItemdata.get(position).getProductName();
        myBasket.setProductName(productName);
        myBasket.setCategoryId(basketItemdata.get(position).getCategoryId());
        Quantity = basketItemdata.get(position).getQuantity();
        myBasket.setQuantity(Quantity);
        myBasket.setPrice(basketItemdata.get(position).getPrice());
        myBasket.setDiscount(basketItemdata.get(position).getDiscount());
        myBasket.setCategoryName(basketItemdata.get(position).getCategoryName());
        dbHelper.upsertBasketOrderData(myBasket);
//        if (Quantity == 0) {//if quantity 0 then delete order in data base
//            dbHelper.deleteBasketOrderDataByProductId(basketItemdata.get(position).getProductId());//delete item
//        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return basketItemdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView quantity_tv, Item_tv, price_tv, Quantity, item_name, itemPrice, icon_delete, icon_rupees, decrement_Product, increase_Product;

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
            increase_Product.setTypeface(materialDesignIcons);
            decrement_Product.setTypeface(materialDesignIcons);
            increase_Product.setText(Html.fromHtml("&#xf419;"));
            decrement_Product.setText(Html.fromHtml("&#xf377;"));
            icon_delete.setTypeface(materialDesignIcons);
            icon_rupees.setTypeface(materialDesignIcons);
            icon_delete.setText(Html.fromHtml("&#xf15a;"));
            icon_rupees.setText(Html.fromHtml("&#xf1af;"));
            quantity_tv.setTypeface(medium);
            Item_tv.setTypeface(medium);
            Quantity.setTypeface(regular);
            item_name.setTypeface(regular);
            itemPrice.setTypeface(regular);
            price_tv.setTypeface(medium);
        }
    }
}

