package fusionsoftware.loop.dawaionline.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.fragments.ProductListFragment;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.CircleImageView;
import fusionsoftware.loop.dawaionline.utilities.FontManager;


/**
 * Created by LALIT on 8/10/2017.
 */

public class LabTestAdapter extends RecyclerView.Adapter<LabTestAdapter.MyViewHolder> {
    private Typeface materialDesignIcons, regular, medium;
    private Boolean addNewAddressFlage;

    private Context mContext;
    private List<Result> categoryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CircleImageView imageView;
        public CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_catagory_name);
            imageView = (CircleImageView) view.findViewById(R.id.image_catagory);
            card_view = (CardView) view.findViewById(R.id.card_view);
            title.setTypeface(medium);
        }
    }

    public LabTestAdapter(Context mContext, List<Result> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.medium = FontManager.getFontTypeface(mContext, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(mContext, "fonts/roboto.regular.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lab_test, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.title.setText(categoryList.get(position).getCategoryName());
        Picasso.with(mContext).load(categoryList.get(position).getCategoryPictures()).resize(100, 100).placeholder(R.drawable.logo).into(holder.imageView);
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Pending...", Toast.LENGTH_SHORT).show();
//                ProductListFragment fragment = ProductListFragment.newInstance(categoryList.get(position).getCategoryId(), categoryList.get(position).getCategoryName());
//                moveFragment(fragment);
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