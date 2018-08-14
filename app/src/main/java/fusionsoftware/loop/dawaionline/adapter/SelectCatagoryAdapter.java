package fusionsoftware.loop.dawaionline.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.fragments.ProductListFragment;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.utilities.FontManager;


/**
 * Created by LALIT on 8/10/2017.
 */

public class SelectCatagoryAdapter extends RecyclerView.Adapter<SelectCatagoryAdapter.MyViewHolder> {
    private Typeface materialDesignIcons, regular, medium;
    private Boolean addNewAddressFlage;

    private Context mContext;
    private List<Data> categoryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView discription, title;
        public ImageView imageView;
        public CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            discription = (TextView) view.findViewById(R.id.tv_catagory_description);
            title = (TextView) view.findViewById(R.id.tv_catagory_name);
            imageView = (ImageView) view.findViewById(R.id.image_catagory);
            card_view = (CardView) view.findViewById(R.id.card_view);
            discription.setTypeface(regular);
            title.setTypeface(medium);
        }
    }

    public SelectCatagoryAdapter(Context mContext, List<Data> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.medium = FontManager.getFontTypeface(mContext, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(mContext, "fonts/roboto.regular.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_catagory, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (categoryList.get(position).getCategoryName() != null) {
            holder.title.setText(categoryList.get(position).getCategoryName());
            holder.title.setVisibility(View.VISIBLE);
        } else {
            holder.title.setVisibility(View.GONE);
        }

        if (categoryList.get(position).getCategoryPictures() != null) {
            Picasso.with(mContext).load(categoryList.get(position).getCategoryPictures()).resize(100, 100).placeholder(R.drawable.logo).into(holder.imageView);
        }
        if (categoryList.get(position).getCategoryDescription() != null) {
            holder.discription.setText(categoryList.get(position).getCategoryDescription());
            holder.discription.setVisibility(View.VISIBLE);
        } else {
            holder.discription.setVisibility(View.GONE);
        }
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductListFragment fragment = ProductListFragment.newInstance(categoryList.get(position).getCategoryId(), categoryList.get(position).getStoreId());
                moveFragment(fragment);
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
        return categoryList.size();
    }
}