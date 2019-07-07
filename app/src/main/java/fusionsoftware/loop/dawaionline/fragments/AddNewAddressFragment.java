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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.balltrianglecustomprogress.BallTriangleDialog;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.framework.IAsyncWorkCompletedCallback;
import fusionsoftware.loop.dawaionline.framework.ServiceCaller;
import fusionsoftware.loop.dawaionline.model.Addresses;
import fusionsoftware.loop.dawaionline.model.ContentDataAsArray;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.Result;
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

    private EditText addNewAddress, addNewPinCode, addNewPhoneNumber, addNewLandMark, addNewName, addNewLocality, addNewCity;
    private View view;
    private TextView address_icon, saveaddress, select_locality;
    private String name, address, phone, pinCode, landMark, city, locality;
    private TextInputLayout layout_address, layout_pincode, layout_phone, layout_landmark;
    private LinearLayout save_address, selectCityLayout;
    private Context context;
    private Button btn_proceed;
    private Boolean localityFlag = false;
    private Typeface materialdesignicons_font, regular, medium;
//    Spinner spinner;

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
        return view;
    }

    //set icons
    private void setIcon() {
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        address_icon.setTypeface(materialdesignicons_font);
        address_icon.setText(Html.fromHtml("&#xf5f8;"));
        medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        saveaddress.setTypeface(medium);
        addNewLandMark.setTypeface(regular);
        addNewPinCode.setTypeface(regular);
//        addNewPhoneNumber.setTypeface(regular);
        addNewAddress.setTypeface(regular);

    }

    //intilization............
    public void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
//        rootActivity.setScreenFavourite(false);
//        rootActivity.setScreenLocation(false);
        rootActivity.setItemCart();
        addNewName = (EditText) view.findViewById(R.id.edt_name);
        addNewLocality = (EditText) view.findViewById(R.id.edt_locality);
        addNewAddress = (EditText) view.findViewById(R.id.edt_address);
        addNewPhoneNumber = (EditText) view.findViewById(R.id.edt_phone);
        addNewPinCode = (EditText) view.findViewById(R.id.edt_pincode);
        addNewCity = (EditText) view.findViewById(R.id.edt_city);
        addNewLandMark = (EditText) view.findViewById(R.id.edt_landmark);
        layout_address = (TextInputLayout) view.findViewById(R.id.layout_address);
        layout_phone = (TextInputLayout) view.findViewById(R.id.layout_phone);
        layout_pincode = (TextInputLayout) view.findViewById(R.id.layout_pincode);
        layout_landmark = (TextInputLayout) view.findViewById(R.id.layout_landmark);
        save_address = (LinearLayout) view.findViewById(R.id.save_address);
        address_icon = (TextView) view.findViewById(R.id.address_icon);
        saveaddress = (TextView) view.findViewById(R.id.saveaddress);
//        select_locality = (TextView) view.findViewById(R.id.select_locality_textView);
//        select_locality.setTypeface(regular);
//        spinner = view.findViewById(R.id.spinner);
        save_address.setOnClickListener(this);
        if (editFlag) {//only for edit
            saveaddress.setText("Update Address");
            setValueForEditAddrss();
        } else {
            saveaddress.setText("Save Address");
        }
        setIcon();
        getCityList();
    }

    //sho prefilds value for edit
    private void setValueForEditAddrss() {
        DbHelper dbHelper = new DbHelper(context);
//        Addresses addresses = dbHelper.getAllAddressesData(addressId);
//        if (addresses != null) {
//            addNewAddress.setText(addresses.getCompleteAddress());
//            addNewPhoneNumber.setText(addresses.getPhoneNumber());
//            addNewPinCode.setText(addresses.getZipCode());
//            addNewLandMark.setText(addresses.getLandMark());
//        }
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
        }

    }

    private void getCityList() {
        final List<String> stringList = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(context);
        List<Result> resultList = dbHelper.GetAllCityData();
        for (Result result : resultList) {
            stringList.addAll(Arrays.asList(result.getCityName()));
        }
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, stringList);
//        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////        spinner.setAdapter(stringArrayAdapter);
////        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////            @Override
////            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                city = adapterView.getSelectedItem().toString();
////            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


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
        name = addNewName.getText().toString();
        address = addNewAddress.getText().toString();
        pinCode = addNewPinCode.getText().toString();
//        phone = addNewPhoneNumber.getText().toString();
        landMark = addNewLandMark.getText().toString();
        locality=addNewLocality.getText().toString();
        city=addNewCity.getText().toString();

        if (name.length() == 0) {
            addNewAddress.setError("Please Enter Name");
            requestFocus(addNewName);
            return false;
//        } else {
//            layout_address.setErrorEnabled(false);
        }

       else if (address.length() == 0) {
            addNewAddress.setError("Please Enter Address");
            requestFocus(addNewAddress);
            return false;
//        } else {
//            layout_address.setErrorEnabled(false);
        }

       else if (landMark.length() == 0) {
            addNewAddress.setError("Please Enter Landmark");
            requestFocus(addNewAddress);
            return false;
//        } else {
//            layout_address.setErrorEnabled(false);
        }

        else if (city.length() == 0) {
            addNewAddress.setError("Please Enter City");
            requestFocus(addNewCity);
            return false;
//        } else {
//            layout_address.setErrorEnabled(false);
        }


        else  if (pinCode.length() == 0) {
            addNewPinCode.setError("Please Enter Pincode");
            requestFocus(addNewPinCode);
            return false;
        }
        else if (pinCode.length() != 6) {
            addNewPinCode.setError("Please Enter  Valid Pincode");
            requestFocus(addNewPinCode);
            return false;
//        } else {
//            layout_pincode.setErrorEnabled(false);
        }


        else if (locality.length() == 0) {
            addNewPhoneNumber.setError("Please Enter Locality");
            requestFocus(addNewPhoneNumber);
            return false;
//        } else {
//            layout_phone.setErrorEnabled(false);
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
                Result data = dbHelper.getUserData();
//                int loginId = data.getLoginID();
                int loginId = 1;
                SharedPreferences sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
                phone=sharedPreferences.getString("Login", "");
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.callAddNewAddressService(address, landMark, city, name, pinCode, locality, phone, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        if (isComplete) {
                            if (workName.trim().equalsIgnoreCase("yes")){
                                Toast.makeText(context, Contants.ADD_NEW_ADDRESS, Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager().popBackStack();//back to previes screen
                            }
                            else {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

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
                Result data = dbHelper.getUserData();
                int loginId = data.getLoginId();
                ServiceCaller serviceCaller = new ServiceCaller(context);
                //get login id from data base ..................
                serviceCaller.updateAddressService(addressId, loginId, address, phone, pinCode, landMark, new IAsyncWorkCompletedCallback() {
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

}





