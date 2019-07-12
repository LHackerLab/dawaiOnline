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
    private String localityName;
    private String addrss;
    private String landmark;
    private String cityName;
    private String pincode;
    private String fullName;

    // TODO: Rename and change types and number of parameters
    public static AddNewAddressFragment newInstance(int addressId, Boolean editFlag, String localityName, String addrss, String landmark, String cityName, String pincode, String fullName) {
        AddNewAddressFragment fragment = new AddNewAddressFragment();
        Bundle args = new Bundle();
        args.putInt("addressId", addressId);
        args.putBoolean("editFlag", editFlag);
        args.putString("localityName", localityName);
        args.putString("addrss", addrss);
        args.putString("landmark", landmark);
        args.putString("cityName", cityName);
        args.putString("pincode", pincode);
        args.putString("fullName", fullName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            addressId = getArguments().getInt("addressId");
            editFlag = getArguments().getBoolean("editFlag");
            localityName = getArguments().getString("localityName");
            addrss = getArguments().getString("addrss");
            landmark = getArguments().getString("landmark");
            cityName = getArguments().getString("cityName");
            pincode = getArguments().getString("pincode");
            fullName = getArguments().getString("fullName");
        }
    }

    private EditText addNewAddress, addNewPinCode, addNewPhoneNumber, addNewLandMark, addNewName, addNewLocality, addNewCity;
    private View view;
    private TextView address_icon, saveaddress;
    private String name, address, pinCode, landMark, city, locality, prefPhone;
    private LinearLayout save_address;
    private Context context;
    private Typeface materialdesignicons_font, regular, medium;

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
        addNewAddress.setTypeface(regular);

    }

    //intilization............
    public void init() {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setScreenSave(false);
        rootActivity.setItemCart();
        addNewName = view.findViewById(R.id.edt_name);
        addNewLocality = view.findViewById(R.id.edt_locality);
        addNewAddress = view.findViewById(R.id.edt_address);
        addNewPhoneNumber = view.findViewById(R.id.edt_phone);
        addNewPinCode = view.findViewById(R.id.edt_pincode);
        addNewCity = view.findViewById(R.id.edt_city);
        addNewLandMark = view.findViewById(R.id.edt_landmark);
        save_address = view.findViewById(R.id.save_address);
        address_icon = view.findViewById(R.id.address_icon);
        saveaddress = view.findViewById(R.id.saveaddress);
        SharedPreferences sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE);
        prefPhone=sharedPreferences.getString("key", null);
        save_address.setOnClickListener(this);
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
        addNewName.setText("" + fullName);
        addNewAddress.setText("" + addrss);
        addNewPinCode.setText("" + pincode);
        addNewLandMark.setText("" + landmark);
        addNewLocality.setText("" + localityName);
        addNewCity.setText("" + cityName);
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

    private void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    //validation for new address............
    private boolean isValidate() {
        name = addNewName.getText().toString();
        address = addNewAddress.getText().toString();
        pinCode = addNewPinCode.getText().toString();
        landMark = addNewLandMark.getText().toString();
        locality = addNewLocality.getText().toString();
        city = addNewCity.getText().toString();

        if (name.length() == 0) {
            addNewName.setError("Please Enter Name");
            requestFocus(addNewName);
            return false;

        } else if (locality.length() == 0) {
            addNewLocality.setError("Please Enter Locality");
            requestFocus(addNewLocality);
            return false;
        } else if (address.length() == 0) {
            addNewAddress.setError("Please Enter Address");
            requestFocus(addNewAddress);
            return false;
        } else if (landMark.length() == 0) {
            addNewLandMark.setError("Please Enter Landmark");
            requestFocus(addNewLandMark);
            return false;
        } else if (city.length() == 0) {
            addNewCity.setError("Please Enter City");
            requestFocus(addNewCity);
            return false;
        } else if (pinCode.length() == 0) {
            addNewPinCode.setError("Please Enter Pincode");
            requestFocus(addNewPinCode);
            return false;
        } else if (pinCode.length() != 6) {
            addNewPinCode.setError("Please Enter Valid Pincode");
            requestFocus(addNewPinCode);
            return false;
        }


        return true;
    }


    //add new address..............
    public void setAddNewAddress() {
        if (isValidate()) {
            if (Utility.isOnline(context)) {
                final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
                dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.callAddNewAddressService(address, landMark, city, name, pinCode, locality, prefPhone, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        if (isComplete) {
                            if (workName.trim().equalsIgnoreCase("yes")) {
                                Toast.makeText(context, Contants.ADD_NEW_ADDRESS, Toast.LENGTH_LONG).show();
                                UserAddressListFragment addressListFragment=new UserAddressListFragment();
                                        moveFragment(addressListFragment);
                                        addNewName.setText("");
                                        addNewLocality.setText("");
                                        addNewAddress.setText("");
                                        addNewLandMark.setText("");
                                        addNewCity.setText("");
                                        addNewPinCode.setText("");
                            } else {
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
        if (isValidate()) {
            if (Utility.isOnline(context)) {
                final BallTriangleDialog dotDialog = new BallTriangleDialog(context);
                dotDialog.show();
                ServiceCaller serviceCaller = new ServiceCaller(context);
                serviceCaller.callUpdateAddressService(addressId, address, landMark, city, name, pinCode, locality, prefPhone, new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String workName, boolean isComplete) {
                        dotDialog.dismiss();
                        if (isComplete) {
                            Toast.makeText(context, "Address Update Successfully", Toast.LENGTH_LONG).show();
                            UserAddressListFragment addressListFragment=new UserAddressListFragment();
                            moveFragment(addressListFragment);
                            addNewName.setText("");
                            addNewLocality.setText("");
                            addNewAddress.setText("");
                            addNewLandMark.setText("");
                            addNewCity.setText("");
                            addNewPinCode.setText("");

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





