package fusionsoftware.loop.dawaionline.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.fragments.OrderHistoryDetailFragment;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


/**
 * Created by HP on 20-08-2017.
 */

public class MyAllOrderHistoryAdapter extends RecyclerView.Adapter<MyAllOrderHistoryAdapter.ViewHolder> {
    private Context context;
    private Typeface materialDesignIcons, medium, regular;
    //private  String []status;
    private List<Data> myOrderData;

    public MyAllOrderHistoryAdapter(Context context, List<Data> myOrderData) {
        this.context = context;
        this.myOrderData = myOrderData;
        this.materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_all_order_history, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        holder.tv_order_number.setText(myOrderData.get(i).getOrderNumber());
        holder.tv_status.setText(myOrderData.get(i).getOrderStatus());
        Date startDateTime = null;
        String sdateTime = null;
        try {
            startDateTime = Utility.toCalendar(myOrderData.get(i).getOrderTime());
            sdateTime = Utility.convertDate(startDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //get value based on array position Wednesday,15,Jun,6,30,AM,2000,06 index(0,1,2,3,4,5,6,7)
        if (sdateTime != null) {
            String[] startArray = sdateTime.split(",");
            holder.tv_date.setText(startArray[1] + "-" + startArray[2] + "-" + startArray[6]);
        }

        holder.tv_amount.setText(String.valueOf(myOrderData.get(i).getNetPrice()));
        holder.tv_store_name.setText(myOrderData.get(i).getStoreName());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderHistoryDetailFragment fragment = OrderHistoryDetailFragment.newInstance(myOrderData.get(i).getOrderId(), myOrderData.get(i).getOrderNumber(), myOrderData.get(i).getStoreName(), myOrderData.get(i).getStoreId());
                moveFragment(fragment);
            }
        });
    }

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return myOrderData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_number, tv_amount, tv_store_name, tv_status, tv_date, rupee_icon;
        CardView card_view;

        public ViewHolder(View view) {
            super(view);
            tv_order_number = (TextView) view.findViewById(R.id.tv_order_number);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            tv_store_name = (TextView) view.findViewById(R.id.tv_store_name);
            rupee_icon = (TextView) view.findViewById(R.id.rupee_icon);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            card_view = (CardView) view.findViewById(R.id.card_view);

            //set Typeface..............
            rupee_icon.setTypeface(materialDesignIcons);
            rupee_icon.setText(Html.fromHtml("&#xf1af;"));
            tv_order_number.setTypeface(medium);
            tv_amount.setTypeface(medium);
            tv_store_name.setTypeface(regular);
            tv_status.setTypeface(medium);
            tv_date.setTypeface(regular);
        }
    }
}
