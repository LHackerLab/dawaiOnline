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
import android.widget.TextView;

import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.fragments.SelectStoreFragment;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


/**
 * Created by user on 8/11/2017.
 */

public class SelectLocalityAdapter extends RecyclerView.Adapter<SelectLocalityAdapter.MyViewHolder> {


    private List<Data> localityList;
    private Boolean addNewAddressFlage;
    Context context;
    Typeface regular;
    Boolean navigationFlag;

    public SelectLocalityAdapter(Context context, List<Data> localityList, Boolean navigationFlag) {
        this.context = context;
        this.localityList = localityList;
        this.navigationFlag = navigationFlag;
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_locality_list_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (localityList != null) {
            holder.localityName.setText(localityList.get(position).getLocalityName());
        }
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.setLocationId(context, localityList.get(position).getLocalityId());

               /* if (navigationFlag) {
                    ((FragmentActivity) context).getSupportFragmentManager().popBackStack();//back to screen
                } else {
                    SelectStoreFragment fragment = SelectStoreFragment.newInstance(localityList.get(position).getLocalityId(), "");
                    moveFragment(fragment);
                }*/
                SelectStoreFragment fragment = SelectStoreFragment.newInstance(localityList.get(position).getLocalityId(), "");
                moveFragment(fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return localityList.size();

    }

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView localityName;
        CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            localityName = (TextView) itemView.findViewById(R.id.locality_text_view);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            localityName.setTypeface(regular);
        }
    }
}
