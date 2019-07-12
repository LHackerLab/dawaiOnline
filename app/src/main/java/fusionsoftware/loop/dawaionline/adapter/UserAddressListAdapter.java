package fusionsoftware.loop.dawaionline.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.fragments.AddNewAddressFragment;
import fusionsoftware.loop.dawaionline.fragments.YourOrderFragment;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.Addresses;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.Contants;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


/**
 * Created by user on 8/14/2017.
 */
public class UserAddressListAdapter extends RecyclerView.Adapter<UserAddressListAdapter.ViewHolder> {
    private List<Result> addresslist;
    private Context context;
    Typeface roboto_light, regular, materialdesignicons_font, medium;
    private LayoutInflater layoutInflater;
    private Boolean navigateFlag=false;


    public UserAddressListAdapter(Context context, List<Result> addresslist, Boolean navigateFlag) {
        this.context = context;
        this.addresslist = addresslist;
        this.navigateFlag = navigateFlag;
        layoutInflater = (LayoutInflater.from(context));
        roboto_light = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/roboto.light.ttf");
        this.medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        this.regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_getall_addresss, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.completename.setText(addresslist.get(position).getPatient_name());
        holder.completeaddress.setText(addresslist.get(position).getLocality() + ", " + addresslist.get(position).getAddress() + ", " + addresslist.get(position).getLandmark() + ", " + addresslist.get(position).getCity());
        holder.Zipcode.setText(addresslist.get(position).getPincode());
        holder.phonenumber.setText(addresslist.get(position).getMobile());
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewAddressFragment fragment = AddNewAddressFragment.newInstance(addresslist.get(position).getId(), true, addresslist.get(position).getLocality(), addresslist.get(position).getAddress(), addresslist.get(position).getLandmark(),addresslist.get(position).getCity(), addresslist.get(position).getPincode(), addresslist.get(position).getPatient_name());//AddressId and true for edit address
                moveFragment(fragment);
            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertForDeletetMessage(position);
            }
        });
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navigateFlag) {
                    YourOrderFragment fragment = YourOrderFragment.newInstance(addresslist.get(position).getAddress(), addresslist.get(position).getMobile(), addresslist.get(position).getPincode());
                    moveFragment(fragment);
                }
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


    //alert for delete
    public void alertForDeletetMessage(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alert = builder.create();
        View view = alert.getLayoutInflater().inflate(R.layout.delete_alert, null);
        TextView dec1 = (TextView) view.findViewById(R.id.dec1);
        dec1.setTypeface(regular);

        TextView ok = view.findViewById(R.id.Ok);
        ok.setTypeface(regular);

        alert.setCustomTitle(view);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                deleteAddress(position);
            }
        });
        alert.show();
    }

    private void deleteAddress(int position) {

        if (Utility.isOnline(context)) {
            if (addresslist.get(position).getId() != 0) {
                callDeleteAddressService(position);
            } else {
            }
        } else {
            Toast.makeText(context, Contants.OFFLINE_MESSAGE, Toast.LENGTH_LONG).show();
        }
        notifyDataSetChanged();

    }


    //delete address
    private void callDeleteAddressService(final int position) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
            String phone = sharedPreferences.getString("key", null);
            final ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callDeleteAddressService(addresslist.get(position).getId(), phone, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        if (workName.trim().equalsIgnoreCase("yes")) {
                            addresslist.remove(position);
                            Intent intent = new Intent("data");
                            intent.putExtra("flag", "true");
                            context.sendBroadcast(intent);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Address not Deleted", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, "Address not Deleted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//        }
    }

    @Override
    public int getItemCount() {
        return addresslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView completename, completeaddress, Zipcode, phonenumber, tv_delete, tv_edit;
        LinearLayout mainLayout;

        public ViewHolder(View view) {
            super(view);
            completename = view.findViewById(R.id.completename);
            completeaddress = view.findViewById(R.id.completeaddress);
            Zipcode = view.findViewById(R.id.Zipcode);
            phonenumber = view.findViewById(R.id.phonenumber);
            tv_edit = view.findViewById(R.id.tv_edit);
            tv_delete = view.findViewById(R.id.tv_delete);
            mainLayout = view.findViewById(R.id.mainLayout);
            completeaddress.setTypeface(medium);
            Zipcode.setTypeface(medium);
            tv_edit.setTypeface(medium);
            tv_delete.setTypeface(medium);
            phonenumber.setTypeface(medium);
        }
    }
}
