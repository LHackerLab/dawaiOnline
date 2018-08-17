package fusionsoftware.loop.dawaionline.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.fragments.ProductListFragment;
import fusionsoftware.loop.dawaionline.model.MyBasket;
import fusionsoftware.loop.dawaionline.utilities.FontManager;


/**
 * Created by LALIT on 8/14/2017.
 */
public class YourOrderAdpater extends RecyclerView.Adapter<YourOrderAdpater.ViewHolder> {
    private List<MyBasket> orderDataList;
    private Context context;
    private boolean isEditDeleteRequired;

    public YourOrderAdpater(Context context, List<MyBasket> categoriesItemsDatas) {
        this.context = context;
        this.orderDataList = categoriesItemsDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_order_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        // holder.quality_item.setText(String.valueOf(orderDataList.get(i).getQuantity()) + " " + orderDataList.get(i).getUOM());
        DecimalFormat df = new DecimalFormat("0");
        String value = df.format(orderDataList.get(i).getQuantity());
        holder.quality_item.setText(value);
        holder.nameofdish_item.setText(orderDataList.get(i).getProductName());
        holder.price_item.setText(orderDataList.get(i).getPrice() + "");
//        holder.edit_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ProductListFragment fragment = ProductListFragment.newInstance(orderDataList.get(i).getCategoryId(), orderDataList.get(i).getStoreId());
//                moveFragment(fragment);
//            }
//        });
//        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DbHelper dbHelper = new DbHelper(context);
//               // dbHelper.deleteBasketOrderDataByProductId(orderDataList.get(i).getProductId());
//                orderDataList.remove(i);
//                if (orderDataList.size() == 0) {//delete store data if all item deleted from this store
//                    dbHelper.deleteSelectedStoreById(storeId);
//                    Intent intent = new Intent(context, DashboardActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
//                notifyDataSetChanged();
//
//            }
//        });
//        if (isEditDeleteRequired) {
//            holder.layout_action.setVisibility(View.VISIBLE);
//        } else {
//            holder.layout_action.setVisibility(View.GONE);
//        }
    }


    public boolean setEditDeleteRequired(boolean isEditDeleteRequired) {
        this.isEditDeleteRequired = isEditDeleteRequired;
        return true;
    }

    //move to fragment
    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return orderDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView quality_item, nameofdish_item, price_item, edit_icon, delete_icon, rupees_icon;
        LinearLayout layout_action;

        public ViewHolder(View view) {
            super(view);
            edit_icon = (TextView) view.findViewById(R.id.edit_icon);
            delete_icon = (TextView) view.findViewById(R.id.delete_icon);
            quality_item = (TextView) view.findViewById(R.id.quality_item);
            rupees_icon = (TextView) view.findViewById(R.id.rupees_icon);
            nameofdish_item = (TextView) view.findViewById(R.id.nameofdish_item);
            price_item = (TextView) view.findViewById(R.id.price_item);
            Typeface regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
            Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
            edit_icon.setTypeface(materialdesignicons_font);
            edit_icon.setText(Html.fromHtml("&#xf3eb;"));
            delete_icon.setTypeface(materialdesignicons_font);
            delete_icon.setText(Html.fromHtml("&#xf5e8;"));
            rupees_icon.setTypeface(materialdesignicons_font);
            rupees_icon.setText(Html.fromHtml("&#xf1af;"));
            quality_item.setTypeface(regular);
            nameofdish_item.setTypeface(regular);
            price_item.setTypeface(regular);
            layout_action = (LinearLayout) view.findViewById(R.id.layout_action);
            //delete_icon=(TextView) view.findViewById(R.id.delete_icon);
        }
    }
}
