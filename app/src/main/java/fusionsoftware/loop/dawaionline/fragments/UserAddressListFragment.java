package fusionsoftware.loop.dawaionline.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.activity.LoginActivity;
import fusionsoftware.loop.dawaionline.adapter.UserAddressListAdapter;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.Addresses;
import fusionsoftware.loop.dawaionline.model.ContentDataAsArray;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.MyPojo;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.Contants;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


/**
 * Created by LALIT on 8/13/2017.
 */
public class UserAddressListFragment extends Fragment implements View.OnClickListener {
    private Boolean navigateFlag;
    private int storeId;

    // TODO: Rename and change types and number of parameters
    public static UserAddressListFragment newInstance(Boolean flag, int storeId) {
        UserAddressListFragment fragment = new UserAddressListFragment();
        Bundle args = new Bundle();
        args.putBoolean("NavigateFlag", flag);
        args.putInt("StoreId", storeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            navigateFlag = getArguments().getBoolean("NavigateFlag");
            storeId = getArguments().getInt("StoreId");
        }
    }


    RecyclerView reycyle_view;
    View view;
    private Context context;
    TextView addNewAddress, addressIcon;
    Typeface materialdesignicons_font, medium, regular;
    LinearLayout linearLayout;
    String phoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        view = inflater.inflate(R.layout.fragment_user_address_list, container, false);
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreenCartDot(true);
        rootActivity.setScreencart(true);
        rootActivity.setItemCart();
        rootActivity.setScreenSave(false);
        reycyle_view = (RecyclerView) view.findViewById(R.id.reycyle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        reycyle_view.setLayoutManager(linearLayoutManager);
        init();
        getAllAddress();//get all address.........

        return view;
    }


    private void init() {
        addNewAddress = view.findViewById(R.id.tv_add_new_address);
        addressIcon = view.findViewById(R.id.address_icon);
        linearLayout = view.findViewById(R.id.linearLayout);
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        addNewAddress.setTypeface(medium);
        addressIcon.setTypeface(materialdesignicons_font);
        addressIcon.setText(Html.fromHtml("&#xf5f8;"));
        addNewAddress.setOnClickListener(this);
    }

    private void addNewAddress() {
        AddNewAddressFragment fragment = AddNewAddressFragment.newInstance(0, false, "", "", "", "", "", "");//0, and false for add new address
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    //get all address.........
    public void getAllAddress() {
        final List<Result> clientlist = new ArrayList<>();
        if (Utility.isOnline(context)) {
            final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
            dotDialog.show();
            SharedPreferences sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
            phoneNumber = sharedPreferences.getString("Login", "");
            if (phoneNumber != null) {
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.callGetAllAddressService(phoneNumber, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        if (isComplete) {
                            if (!workName.trim().equalsIgnoreCase("no")) {
                                MyPojo myPojo = new Gson().fromJson(workName, MyPojo.class);
                                for (Result result : myPojo.getResult()) {
                                    clientlist.addAll(Arrays.asList(result));
                                }

                                if (clientlist != null && clientlist.size() > 0) {
                                    UserAddressListAdapter itemAdapter = new UserAddressListAdapter(context, clientlist, navigateFlag);
                                    reycyle_view.setAdapter(itemAdapter);
                                    itemAdapter.notifyDataSetChanged();
                                } else {
                                    noDataFoundUI();
                                }
                            }

                                else {
                                noDataFoundUI();

                            }

                        } else {
                            noDataFoundUI();
                        }
                        try {
                            if (dotDialog.isShowing() && dotDialog != null) {
                                dotDialog.dismiss();
                            }
                        } catch (Exception ex) {
                            Log.e(Contants.LOG_TAG, ex.getMessage(), ex);
                        }
                    }

                });
//                }
            } else {
                if (dotDialog.isShowing() && dotDialog != null) {
                    dotDialog.dismiss();
                }
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else {
            //  Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getActivity());//off line msg....
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.no_data_found, null);
            TextView nodata = (TextView) view.findViewById(R.id.nodata);
            nodata.setText("No internet connection found");
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.removeAllViews();
            linearLayout.addView(view);
        }
    }


    @Override
    public void onResume() {
        getActivity().registerReceiver(mReceiverLocation, new IntentFilter("data"));
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReceiverLocation);
        super.onPause();
    }

    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String flag = intent.getStringExtra("flag");
                if (flag.equals("true")) {
                    getAllAddress();
                }
                Log.e("data", flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void noDataFoundUI() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodataIcon = (TextView) view.findViewById(R.id.nodataIcon);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        nodata.setTypeface(regular);
        nodataIcon.setTypeface(materialdesignicons_font);
        nodataIcon.setText(Html.fromHtml("&#xf187;"));
        nodata.setText("Your Address Not Found");
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.removeAllViews();
        linearLayout.addView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_new_address:
                addNewAddress();
                break;
        }
    }
}

