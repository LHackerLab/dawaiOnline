package fusionsoftware.loop.dawaionline.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import fusionsoftware.loop.dawaionline.R;
import fusionsoftware.loop.dawaionline.activity.DashboardActivity;
import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.CompatibilityUtility;
import fusionsoftware.loop.dawaionline.utilities.FontManager;


/**
 * Created by LALIT on 8/11/2017.
 */

public class UserProfileFragment extends Fragment implements View.OnClickListener {
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("param1");
            mParam2 = getArguments().getString("param2");
        }
    }

    TextView user_icon, phone_icon, email_icon, tv_UserName, tv_UserPhone,
            textView_UserEmail, tv_editProfile, name, phone, email;
    private Context context;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (CompatibilityUtility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        init(view);
        return view;
    }

    // initilization ...............
    private void init(View view) {
        DashboardActivity rootActivity = (DashboardActivity) getActivity();
        rootActivity.setScreencart(true);
        rootActivity.setItemCart();
        rootActivity.setScreenSave(false);
        user_icon = (TextView) view.findViewById(R.id.user_icon);
        phone_icon = (TextView) view.findViewById(R.id.phone_icon);
        email_icon = (TextView) view.findViewById(R.id.email_icon);
        tv_UserName = (TextView) view.findViewById(R.id.textView_UserName);
        tv_UserPhone = (TextView) view.findViewById(R.id.textView_UserPhoneNo);
        textView_UserEmail = (TextView) view.findViewById(R.id.textView_UserEmail);
        name = (TextView) view.findViewById(R.id.tv_username);
        email = (TextView) view.findViewById(R.id.email);
        phone = (TextView) view.findViewById(R.id.phone);
        tv_editProfile = (TextView) view.findViewById(R.id.tv_editProfile);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        setIcon();
        setValue();
        tv_editProfile.setOnClickListener(this);// set onclick....
    }

    //set icon
    private void setIcon() {
        Typeface medium = FontManager.getFontTypeface(context, "fonts/roboto.medium.ttf");
        Typeface regular = FontManager.getFontTypeface(context, "fonts/roboto.regular.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(getActivity(), "fonts/materialdesignicons-webfont.otf");
        user_icon.setTypeface(materialdesignicons_font);
        user_icon.setText(Html.fromHtml("&#xf004;"));
        tv_UserName.setTypeface(medium);
        phone_icon.setTypeface(materialdesignicons_font);
        phone_icon.setText(Html.fromHtml("&#xf3f2;"));
        tv_UserPhone.setTypeface(medium);
        email_icon.setTypeface(materialdesignicons_font);
        email_icon.setText(Html.fromHtml("&#xf1ee;"));
        textView_UserEmail.setTypeface(medium);
        tv_editProfile.setTypeface(materialdesignicons_font);
        tv_editProfile.setText(Html.fromHtml("&#xf3eb;"));
        name.setTypeface(regular);
        email.setTypeface(regular);
        phone.setTypeface(regular);
    }


    //set user value
    private void setValue() {
        DbHelper dbHelper = new DbHelper(context);
        Result data = dbHelper.getUserData();
        if (data != null) {
            tv_UserName.setText(data.getName());
            tv_UserPhone.setText(data.getPhoneNumber());
            textView_UserEmail.setText(data.getEmailID());
            String url = data.getProfilePictureUrl();
            if(url!=null && !url.equals("")) {
                Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE).into(imageView);
            }
        }
    }

    //onclick...............
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_editProfile://update profile........
                UpdateProfileFragment fragment = UpdateProfileFragment.newInstance("", "");
                moveFragment(fragment);
                break;
        }
    }

    //move to fragment
    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
