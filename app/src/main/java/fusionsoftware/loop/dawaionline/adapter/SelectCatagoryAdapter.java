package fusionsoftware.loop.dawaionline.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
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

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.fragments.ProductListFragment;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.CircleImageView;
import fusionsoftware.loop.dawaionline.utilities.FontManager;


/**
 * Created by LALIT on 8/10/2017.
 */

public class SelectCatagoryAdapter extends RecyclerView.Adapter<SelectCatagoryAdapter.MyViewHolder> {
    private Typeface materialDesignIcons, regular, medium;
    private Boolean addNewAddressFlage;

    private Context mContext;
    private List<Result> categoryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imageView;
        public CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_catagory_name);
            imageView = view.findViewById(R.id.image_catagory);
            card_view = view.findViewById(R.id.card_view);
            title.setTypeface(medium);
        }
    }

    public SelectCatagoryAdapter(Context mContext, List<Result> categoryList) {
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
    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.title.setText(categoryList.get(position).getCategory());
//        Glide.with(mContext).load(categoryList.get(position).getProductSubTitle()).into(holder.imageView);
        if (categoryList.get(position).getPic() != null && !categoryList.get(position).getPic().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(categoryList.get(position).getPic()).resize(200, 200).transform(new CircleTransform()).placeholder(R.drawable.logo).into(holder.imageView);
        } else {
            Picasso.with(mContext).load(R.drawable.logo).into(holder.imageView);

        }
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductListFragment fragment = ProductListFragment.newInstance(categoryList.get(position).getId(), categoryList.get(position).getMain_category());
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
//}