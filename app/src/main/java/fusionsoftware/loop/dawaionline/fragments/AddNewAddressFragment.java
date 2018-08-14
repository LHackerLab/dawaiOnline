package fusionsoftware.loop.dawaionline.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.Addresses;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.Contants;
import fusionsoftware.loop.dawaionline.utilities.FontManager;
import fusionsoftware.loop.dawaionline.utilities.Utility;


/**
 * Created by LALIT on 8/13/2017.
 */
public class AddNewAddressFragment extends Fragment implements View.OnClickListener {
    private int addressId;
    private Boolean editFlag;

    // TODO: Rename and change types and number of parameters
    public static AddNewAddressFragment newInstance(int addressId, Boolean editFlag) {
        AddNewAddressFragment fragment = new AddNewAddressFragment();
        Bundle args = new Bundle();
        args.putInt("addressId", addressId);
        args.putBoolean("editFlag", editFlag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            addressId = getArguments().getInt("addressId");
            editFlag = getArguments().getBoolean("editFlag");
        }
    }

    private EditText addNewAddress, addNewPinCode, addNewPhoneNumber, addNewLandMark;
    private View view;
    private TextView address_icon, down_arrow, saveaddress, select_city, select_locality, down_arrow_locality;
    private String address, phone, pinCode, landMark;
    private TextInputLayout layout_address, layout_pincode, layout_phone, layout_landmark;
    private LinearLayout save_address, selectCityLayout, selectlocalityLayout;
    private Context context;
    private Button btn_proceed;
    private Boolean localityFlag = false;
    private Typeface materialdesignicons_font, regular, medium;
    private int cityId = 0;
    private int localityId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        view = inflater.inflate(R.layout.fragment_add_new_address, container, false);
        init();//intilization............
        getLocality();
        return view;
    }

    private void getLocality() {
        DbHelper dbHelper = new DbHelper(context);
        SharedPreferences locationPrefs = context.getSharedPreferences("LocationPreferences", Context.MODE_PRIVATE);
        if (locationPrefs != null) {
            localityId = locationPrefs.getInt("LocalityId", 0);
            Data data = dbHelper.getAllLocalitiesDataById(localityId);
            if (data != null) {
                select_locality.setText(data.getLocalityName());
            }
        }
    }

    //set icons
    private void setIcon() {
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        address_icon.setTypeface(materialdesignicons_font);
        address_icon.setText(Html.fromHtml("&#xf5f8;"));
//        down_arrow.setTypeface(materialdesignicons_font);
//        down_arrow.setText(Html.fromHtml("&#xf140;"));
        down_arrow_locality.setTypeface(materialdesignicons_font);
        down_arrow_locality.setText(Html.fromHtml("&#xf140;"));
        medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        saveaddress.setTypeface(medium);
        addNewLandMark.setTypeface(regular);
        addNewPinCode.setTypeface(regular);
        addNewPhoneNumber.setTypeface(regular);
        addNewAddress.setTypeface(regular);

    }

    //intilization............
    public void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setScreenFavourite(false);
        rootActivity.setScreenLocation(false);
        rootActivity.setItemCart();
//        proceed = (Button) v.findViewById(R.id.btn_proceed);
        addNewAddress = (EditText) view.findViewById(R.id.edt_address);
        addNewPhoneNumber = (EditText) view.findViewById(R.id.edt_phone);
        addNewPinCode = (EditText) view.findViewById(R.id.edt_pincode);
        addNewLandMark = (EditText) view.findViewById(R.id.edt_landmark);
        layout_address = (TextInputLayout) view.findViewById(R.id.layout_address);
        layout_phone = (TextInputLayout) view.findViewById(R.id.layout_phone);
        layout_pincode = (TextInputLayout) view.findViewById(R.id.layout_pincode);
        layout_landmark = (TextInputLayout) view.findViewById(R.id.layout_landmark);
        save_address = (LinearLayout) view.findViewById(R.id.save_address);
        address_icon = (TextView) view.findViewById(R.id.address_icon);
        saveaddress = (TextView) view.findViewById(R.id.saveaddress);
//        down_arrow = (TextView) view.findViewById(R.id.down_arrow_icon);
        down_arrow_locality = (TextView) view.findViewById(R.id.down_arrow_icon_locality);
        //  select_city = (TextView) view.findViewById(R.id.select_city_textView);
        select_locality = (TextView) view.findViewById(R.id.select_locality_textView);
//        select_city.setTypeface(regular);
        select_locality.setTypeface(regular);
        save_address.setOnClickListener(this);
        // selectCityLayout = (LinearLayout) view.findViewById(R.id.selectCityLayout);
        selectlocalityLayout = (LinearLayout) view.findViewById(R.id.selectlocalityLayout);
//        selectCityLayout.setOnClickListener(this);
        selectlocalityLayout.setOnClickListener(this);
        if (editFlag) {//only for edit
            saveaddress.setText("Update Address");
            setValueForEditAddrss();
        } else {
            saveaddress.setText("Save Address");
        }

        setIcon();
    }

    //sho prefilds value for edit
    private void setValueForEditAddrss() {
        DbHelper dbHelper = new DbHelper(context);
        Addresses addresses = dbHelper.getAllAddressesData(addressId);
        if (addresses != null) {
            addNewAddress.setText(addresses.getCompleteAddress());
            addNewPhoneNumber.setText(addresses.getPhoneNumber());
            addNewPinCode.setText(addresses.getZipCode());
            addNewLandMark.setText(addresses.getLandMark());
            //
            //    int cityid = addresses.getCityId();
            int localityid = addresses.getLocalityId();
//            Data data = dbHelper.getCityDataById(cityid);
//            if (data != null) {
//                select_city.setText(data.getCityName());
//            }
            Data mData = dbHelper.getAllLocalitiesDataById(localityid);
            if (mData != null) {
                select_locality.setText(mData.getLocalityName());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_address:
                if (editFlag) {
                    setUpdateAddress();
                } else {
                    setAddNewAddress();
                }
                break;
//            case R.id.selectCityLayout:
//                selectCity();
//                break;
            case R.id.selectlocalityLayout:
                selectlocality();
                break;
        }

    }

    private void selectlocality() {
        new AlertDialog.Builder(context)
                .setTitle("Change Location")
                .setCancelable(false)
                .setMessage("Would you like to change your location ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LocationFragment fragment = LocationFragment.newInstance("", true);
                        moveFragmentWithTag(fragment, "LocationFragment");
                        //getActivity().getSupportFragmentManager().popBackStack();//back to screen
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // user doesn't want to change the Location
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void moveFragmentWithTag(Fragment fragment, String tag) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    //validation for new address............
    private boolean isValidate() {
        address = addNewAddress.getText().toString();
        pinCode = addNewPinCode.getText().toString();
        phone = addNewPhoneNumber.getText().toString();
        landMark = addNewLandMark.getText().toString();
//        if (select_city.length() == 0) {
//            Utility.alertForErrorMessage("Please Select Any City", context);
//            return false;
//        }
        if (select_locality.length() == 0) {
            Utility.alertForErrorMessage("Please Select Any Locality", context);
            return false;
        } else if (address.length() == 0) {
            layout_address.setError("Please Enter Address");
            requestFocus(layout_address);
            return false;
        } else {
            layout_address.setErrorEnabled(false);
        }
        if (phone.length() != 10) {
            layout_phone.setError("Please Enter Valid Phone Number ");
            requestFocus(layout_phone);
            return false;
        } else {
            layout_phone.setErrorEnabled(false);
        }
        if (pinCode.length() == 0) {
            layout_pincode.setError("Please Enter Pincode");
            requestFocus(layout_pincode);
            return false;
        } else if (pinCode.length() != 6) {
            layout_pincode.setError("Please Enter  Valid Pincode");
            requestFocus(layout_pincode);
            return false;
        } else {
            layout_pincode.setErrorEnabled(false);
        }
        return true;
    }

    //add new address..............
    public void setAddNewAddress() {
        if (isValidate()) {
            if (Utility.isOnline(context)) {
                final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
                dotDialog.show();
                DbHelper dbHelper = new DbHelper(context);
                Data data = dbHelper.getUserData();
                int loginId = data.getLoginID();
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.SetNewAddressService(loginId, address, phone, pinCode, landMark, localityId, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        if (isComplete) {
                            Toast.makeText(context, Contants.ADD_NEW_ADDRESS, Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().popBackStack();//back to profile screen
                        } else {
                            Utility.alertForErrorMessage(Contants.DoNot_NEW_ADDRESS, context);
                        }
                        if (dotDialog.isShowing()) {
                            dotDialog.dismiss();
                        }
                    }
                });
            } else {
               Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);

            }
        }
    }

    //update address..............
    public void setUpdateAddress() {
        // check validation for new address............
        if (isValidate()) {
            if (Utility.isOnline(context)) {
                final Utility utility = new Utility();
                //utility.customProgressDialog(getActivity());
                DbHelper dbHelper = new DbHelper(context);
                Data data = dbHelper.getUserData();
                int loginId = data.getLoginID();
                ServiceCaller serviceCaller = new ServiceCaller(context);
                //get login id from data base ..................
                serviceCaller.updateAddressService(addressId, loginId, address, phone, pinCode, landMark, localityId, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        if (isComplete) {
                            // utility.customProgressDialogDismiss();
                            Toast.makeText(context, "Address Update Successfully", Toast.LENGTH_LONG).show();
                            getFragmentManager().popBackStack();//back to previous screen
                        } else {
                            Utility.alertForErrorMessage("Address not Update Successfully", context);// do not add address message.......
                        }
                    }
                });
            } else {
                Utility.alertForErrorMessage(Contants.OFFLINE_MESSAGE, context);//offline check..........
            }
        }
    }

    //regquest focus ................
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //select city
//    private void selectCity() {
//        final Dialog dialog = new Dialog(context);
//        dialog.setContentView(R.layout.custom_dialog_location);
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(true);
//        ListView listView = (ListView) dialog.findViewById(R.id.cityList);
//        DbHelper dbhelper = new DbHelper(context);
//        final List<Data> cityList = dbhelper.getAllCityData();
//        if (cityList != null) {
//            SelectCityAdapter cityAdapter = new SelectCityAdapter(context, cityList);
//            listView.setAdapter(cityAdapter);
//            dialog.show();
//        }
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                select_city.setText(cityList.get(pos).getCityName());
//                cityId = cityList.get(pos).getCityId();
//                dialog.dismiss();
//            }
//        });
//

}





