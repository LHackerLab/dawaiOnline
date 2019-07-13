package fusionsoftware.loop.dawaionline.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.activity.LoginActivity;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.fragments.MyAllOrderHistoryFragment;
import fusionsoftware.loop.dawaionline.fragments.MyBasketFragment;
import fusionsoftware.loop.dawaionline.fragments.ParentFragment;
import fusionsoftware.loop.dawaionline.fragments.SelectCategoryFragment;
import fusionsoftware.loop.dawaionline.fragments.TrackOrderFragment;
import fusionsoftware.loop.dawaionline.fragments.UserAddressListFragment;
import fusionsoftware.loop.dawaionline.fragments.UserProfileFragment;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.FontManager;


/**
 * Created by u on 7/26/2017.
 */

public class NavMenuCustomAdapter extends BaseAdapter {
    private Context context;
    List<String> icondatalist;
    //    String nav_menu[];
    private LayoutInflater inflater;
    HashSet<Integer> selectedPosition = new HashSet<>();
    List<String> stringList;
    DbHelper dbHelper;

    public NavMenuCustomAdapter(Context context, String[] nav_menu, List<String> icondatalist) {
        this.context = context;
        this.icondatalist = icondatalist;
//        this.nav_menu = nav_menu;
        inflater = LayoutInflater.from(context);
    }

    public NavMenuCustomAdapter(DashboardActivity context, List<String> stringList, List<String> icondatalist) {
        this.context = context;
        this.icondatalist = icondatalist;
        this.stringList = stringList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return icondatalist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            Typeface materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
            view = inflater.inflate(R.layout.item_list_nav_menu, viewGroup, false);
            Typeface regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
            holder.forTitle = (TextView) view.findViewById(R.id.nav_menutitle);
            holder.menuItemLayout = (LinearLayout) view.findViewById(R.id.menuItemLayout);
            holder.forIcon = (TextView) view.findViewById(R.id.nav_menuicon);
            holder.forIcon.setTypeface(materialDesignIcons);
            holder.forTitle.setTypeface(regular);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.forTitle.setTag(position);
        holder.forTitle.setText(stringList.get(position));
        holder.forIcon.setText(icondatalist.get(position));
//----------fill selected value------
        if (selectedPosition.contains(position)) {
            holder.menuItemLayout.setBackgroundColor(Color.parseColor("#BDBDBD"));
            holder.forTitle.setTextColor(Color.parseColor("#388e3c"));
            holder.forIcon.setTextColor(Color.parseColor("#388e3c"));
        } else {
            holder.menuItemLayout.setBackgroundColor(Color.WHITE);
            holder.forIcon.setTextColor(Color.parseColor("#BDBDBD"));
            holder.forTitle.setTextColor(Color.parseColor("#212121"));
        }
        holder.forTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (position == 0) {
//                        setUpHomeFragment();
                        ParentFragment parentFragment = new ParentFragment();
                        moveFragment(parentFragment);
                    }
                    else if (position == 1) {
                        UserAddressListFragment fragment = UserAddressListFragment.newInstance(false, 0);
                        moveFragment(fragment);
                    }
                   else if (position == 2) {
//                        Toast.makeText(context, "Track", Toast.LENGTH_SHORT).show();
                        TrackOrderFragment trackOrderFragment=new TrackOrderFragment();
                        moveFragment(trackOrderFragment);
//                        TrackOrderFragment orderFragment = TrackOrderFragment.newInstance("", "");
//                        moveFragment(orderFragment);
                    }
                   else if (position == 3) {
//                        Toast.makeText(context, "History", Toast.LENGTH_SHORT).show();
                        MyAllOrderHistoryFragment orderHistoryFragment=new MyAllOrderHistoryFragment();
                        moveFragment(orderHistoryFragment);
//                        MyAllOrderHistoryFragment orderHistoryFragment = MyAllOrderHistoryFragment.newInstance(0, "");
//                        moveFragment(orderHistoryFragment);
                    }
                    else if (position == 4) {
                        MyBasketFragment fragment = MyBasketFragment.newInstance("", "");
                        moveFragment(fragment);
                    }

                    else if (position == 5) {
                        UserProfileFragment fragment = UserProfileFragment.newInstance("", "");
                        moveFragment(fragment);
                    }

                   else if (position == 6) {
                       DbHelper dbHelper=new DbHelper(context);
                       dbHelper.deleteAllBasketOrderData();
                        SharedPreferences preferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent=new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }

                int pos = (int) view.getTag();
                if (selectedPosition.contains(pos)) {
                    //selectedPosition.remove(pos);
                    //selectedPosition.clear();
                } else {
                    selectedPosition.clear();
                    selectedPosition.add(pos);
                }
                notifyDataSetChanged();
                DashboardActivity rootActivity = (DashboardActivity) context;
                rootActivity.NavHide();

            }
        });
        return view;
    }

    //open home fragement
    private void setUpHomeFragment() {
//        SelectCategoryFragment fragment = SelectCategoryFragment.newInstance("", 0);
//        moveFragment(fragment);
    }

    //move to fragment
    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public class ViewHolder {
        public TextView forTitle;
        public TextView forIcon;
        public LinearLayout menuItemLayout;
    }
}


