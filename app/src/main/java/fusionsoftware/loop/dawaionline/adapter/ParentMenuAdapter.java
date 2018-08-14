package fusionsoftware.loop.dawaionline.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.fragments.SelectCategoryFragment;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.utilities.FontManager;


/**
 * Created by SONALI on 11/6/2017.
 */

public class ParentMenuAdapter extends RecyclerView.Adapter<ParentMenuAdapter.MyViewHolder> {
    private Typeface materialDesignIcons, regular, medium;
    private Boolean addNewAddressFlage;

    private Context mContext;
    private List<Data> menuList;
    private int StoreId;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_parent_name;
        public ImageView img_parent_manu;
        public CardView card_parent_menu;

        public MyViewHolder(View view) {
            super(view);
            tv_parent_name = (TextView) view.findViewById(R.id.tv_parent_name);
            img_parent_manu = (ImageView) view.findViewById(R.id.img_parent_manu);
            card_parent_menu = (CardView) view.findViewById(R.id.card_parent_menu);
            tv_parent_name.setTypeface(medium);
        }
    }

    public ParentMenuAdapter(Context mContext, List<Data> menuList,int StoreId) {
        this.mContext = mContext;
        this.menuList = menuList;
        this.StoreId=StoreId;
        this.medium = FontManager.getFontTypeface(mContext, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(mContext, "fonts/roboto.regular.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parent_menu_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//       holder.title.setText(categoryList.get(position).getCategoryName());
//        if (categoryList.get(position).getCategoryDescription() != null) {
//            holder.tv_parent_name.setVisibility(View.VISIBLE);
            holder.tv_parent_name.setText(menuList.get(position).getMenuName());
//        } else {
//            holder.tv_parent_name.setVisibility(View.GONE);
//        }
       Picasso.with(mContext).load(menuList.get(position).getImageUrl()).resize(100, 100).placeholder(R.drawable.sweet_image).into(holder.img_parent_manu);
        holder.card_parent_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SelectCategoryFragment fragment = SelectCategoryFragment.newInstance(menuList.get(position).getMenuId(),StoreId);
                moveFragment(fragment);
//                categoryList.get(position).getStoreId()
                //categoryList.get(position).getCategoryId(),

            }
        });
    }

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return menuList.size();
//
    }
}
