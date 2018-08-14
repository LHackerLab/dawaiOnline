package fusionsoftware.loop.dawaionline.framework;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import fusionsoftware.loop.dawaionline.database.DbHelper;
import fusionsoftware.loop.dawaionline.model.Addresses;
import fusionsoftware.loop.dawaionline.model.CacheServiceCallData;
import fusionsoftware.loop.dawaionline.model.ContentData;
import fusionsoftware.loop.dawaionline.model.ContentDataAsArray;
import fusionsoftware.loop.dawaionline.model.CreateOrderDetails;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.utilities.Contants;
import fusionsoftware.loop.dawaionline.utilities.Utility;


/**
 * Created by lalit on 7/25/2017.
 */
public class ServiceCaller {
    Context context;

    public ServiceCaller(Context context) {
        this.context = context;
    }

    //call login data
    public void callLoginService(String phone, String token, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.Login;
        JSONObject obj = new JSONObject();
        try {
            obj.put("PhoneNumber", phone);
            obj.put("DeviceId", token);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseAndSaveLoginData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("loginService done", false);
                }
            }
        });
    }

    //parse and save login data
    public void parseAndSaveLoginData(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentData data = new Gson().fromJson(result, ContentData.class);
                Log.d(Contants.LOG_TAG, "---" + data);
                if (data != null) {
                    if (data.getData() != null) {
                        DbHelper dbhelper = new DbHelper(context);
                        dbhelper.upsertUserData(data.getData());
                        flag = true;
                    }
                }
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("LoginService done", true);
                } else {
                    workCompletedCallback.onDone("LoginService done", false);
                }
            }
        }.execute();
    }

// for otp verification

    public void callLoginServiceOTPVerify(String Otp, String PhoneNumber, int LoginID, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.VerifyOTP;
        JSONObject obj = new JSONObject();
        try {
            obj.put("Otp", Otp);
            obj.put("PhoneNumber", PhoneNumber);
            obj.put("LoginID", LoginID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                ContentData data = new Gson().fromJson(result, ContentData.class);
                if (data != null) {
                    if (data.getResponse().getSuccess()) {
                        workCompletedCallback.onDone("loginServiceOtpVerify done", true);
                    } else {
                        workCompletedCallback.onDone("loginServiceVerify done", false);
                    }
                } else {
                    workCompletedCallback.onDone("loginServiceVerify done", false);
                }
            }
        });
    }

    //for get user profile................
    public void getUserProfileService(int LoginID, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.GetUserProfile;
        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginID", LoginID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseGetUserProfileService(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("getUserProfileService done", false);
                }
            }
        });
    }

    //parse and save get user profile
    public void parseGetUserProfileService(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentData data = new Gson().fromJson(result, ContentData.class);
                if (data != null) {
                    if (data.getResponse() != null && data.getResponse().getSuccess()) {
                        if (data.getData() != null) {
                            DbHelper dbhelper = new DbHelper(context);
                            dbhelper.upsertUserData(data.getData());
                            flag = true;
                        }
                    }
                }
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("getUserProfileService done", true);
                } else {
                    workCompletedCallback.onDone("getUserProfileService done", false);
                }
            }
        }.execute();
    }

    //update user profile
    public void UpdateUserProfileService(int LoginID, String Name, String Phone, String Email, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.UpdateProfile;
        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginID", LoginID);
            obj.put("Name", Name);
            obj.put("PhoneNumber", Phone);
            obj.put("EmailID", Email);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseUpdateUserProfileService(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("UpdateUserProfileService done", false);
                }
            }
        });
    }

    //parse and save update user profile
    public void parseUpdateUserProfileService(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentData data = new Gson().fromJson(result, ContentData.class);
                if (data != null) {
                    if (data.getResponse() != null && data.getResponse().getSuccess()) {
                        if (data.getData() != null) {
                            DbHelper dbhelper = new DbHelper(context);
                            dbhelper.upsertUserData(data.getData());
                            flag = true;
                        }
                    }
                }
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("UpdateUserProfileService done", true);
                } else {
                    workCompletedCallback.onDone("UpdateUserProfileService done", false);
                }
            }
        }.execute();
    }

    //Set AddNewAddress  data
    public void SetNewAddressService(int LoginId, String completeAddress, String phoneNumber, String zipcode, String landmark, int localityId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.AddNewAddress;
        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginID", LoginId);
            obj.put("CompleteAddress", completeAddress);
            obj.put("PhoneNumber", phoneNumber);
            obj.put("ZipCode", zipcode);
            obj.put("LandMark", landmark);
            obj.put("LocalityId", localityId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    workCompletedCallback.onDone("SetNewAddressService done", true);
                } else {
                    workCompletedCallback.onDone("SetNewAddressService done", false);
                }
            }
        });
    }

    //Set update Address  data
    public void updateAddressService(int addressId, int LoginId, String completeAddress, String phoneNumber, String zipcode, String landmark, int LocalityId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.UpdateAddress;
        JSONObject obj = new JSONObject();
        try {
            obj.put("AddressId", addressId);
            obj.put("LoginID", LoginId);
            obj.put("CompleteAddress", completeAddress);
            //obj.put("CityId", cityId);
            obj.put("LandMark", landmark);
            obj.put("PhoneNumber", phoneNumber);
            obj.put("ZipCode", zipcode);
            obj.put("LocalityId", LocalityId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    workCompletedCallback.onDone("updateAddressService done", true);
                } else {
                    workCompletedCallback.onDone("updateAddressService done", false);
                }
            }
        });
    }

    //get all address data...........
    public void callGetAllAddressService(int LoginId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.GetAllAddress;
        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginId", LoginId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseAndSavesGetAllAddressData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("callGetAllAddressService done", false);
                }
            }
        });
    }

    //parse and save GetAllAddress data
    public void parseAndSavesGetAllAddressData(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentData data = new Gson().fromJson(result, ContentData.class);
                DbHelper dbHelper = new DbHelper(context);
                dbHelper.deleteAllAddressData();
                if (data != null) {
                    for (Addresses address : data.getAddresses()) {
                        if (address != null) {
                            dbHelper.upsertGetAllAddress(address);
                        }
                    }
                    flag = true;
                }
                return flag;
            }


            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("callGetAllAddressService done", true);
                } else {
                    workCompletedCallback.onDone("callGetAllAddressService done", false);
                }
            }
        }.execute();
    }


    //call select city data
    public void callGetCitiesDataService(final int pageSize, final int pageIndex, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.SELECT_CITY_URL;
        DbHelper dbHelper = new DbHelper(context);
        CacheServiceCallData cacheServiceCallData = dbHelper.getCacheServiceCallByUrl(url);

        JSONObject obj = new JSONObject();
        try {
            obj.put("PageSize", pageSize);
            obj.put("PageNumber", pageIndex);
            if (cacheServiceCallData != null && cacheServiceCallData.getServerRequestDateTime() != null) {
                obj.put("ServerRequestDateTime", cacheServiceCallData.getServerRequestDateTime());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String callerUrl, final String result, String error) {

                final ContentDataAsArray data = new Gson().fromJson(result, ContentDataAsArray.class);
                if (data != null) {
                    Log.d(Contants.LOG_TAG, "data downloaded for City list is :" + data.getData().length);
                    if (data.getData().length > 0) {
                        parseAndSaveGetCitiesData(data);
                        final int currentpageIndex = pageIndex + 1;
                        callGetCitiesDataService(pageSize, currentpageIndex, new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String workName, boolean isComplete) {
                                Log.d(Contants.LOG_TAG, "service caller complete called");
                                if (isComplete) {
                                    Log.d(Contants.LOG_TAG, "City List call all done: records downloaded: " + currentpageIndex * pageSize);
                                }
                                if (workName.equalsIgnoreCase("listService recursion done")) {
                                    Log.d(Contants.LOG_TAG, "recurse service caller workitemcallback called.");
                                    workCompletedCallback.onDone("listService recursion done", true);
                                }
                            }
                        });

                    } else {
                        updateLastRequestDateTime(url, data);
                        Log.d(Contants.LOG_TAG, "service caller workitemcallback called.");
                        workCompletedCallback.onDone("listService recursion done", true);
                    }
                } else {
                    workCompletedCallback.onDone("listService recursion done", false);
                }
            }

        });
    }

    //parse and save city data.............
    public void parseAndSaveGetCitiesData(final ContentDataAsArray data) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                DbHelper dbhelper = new DbHelper(context);
                if (data.getData() != null && data.getData().length != 0) {
                    for (Data cityData : data.getData()) {
                        dbhelper.upsertCityData(cityData);
                    }
                }
                return true;
            }
        }.execute();

    }

    //update last request date time based on url
    private void updateLastRequestDateTime(String url, ContentDataAsArray data) {
        DbHelper dbhelper = new DbHelper(context);
        CacheServiceCallData cacheServiceCallData = new CacheServiceCallData();
        cacheServiceCallData.setUrl(url);
        if (data.getResponse() != null) {
            cacheServiceCallData.setServerRequestDateTime(data.getResponse().getServerResponseTime());
        }
        dbhelper.upsertCacheServiceCallData(cacheServiceCallData);
    }

    //////////////call select localities data
    public void getAllLocalitiesService(final int pageSize, final int pageIndex, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.GET_ALL_LOCALITIES_URL;
        DbHelper dbHelper = new DbHelper(context);
        CacheServiceCallData cacheServiceCallData = dbHelper.getCacheServiceCallByUrl(url);
        JSONObject obj = new JSONObject();
        try {
            obj.put("PageSize", pageSize);
            obj.put("PageNumber", pageIndex);
            if (cacheServiceCallData != null && cacheServiceCallData.getServerRequestDateTime() != null) {
                obj.put("ServerRequestDateTime", cacheServiceCallData.getServerRequestDateTime());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String callerUrl, final String result, String error) {

                final ContentDataAsArray data = new Gson().fromJson(result, ContentDataAsArray.class);
                if (data != null) {
                    Log.d(Contants.LOG_TAG, "data downloaded for localities list is :" + data.getData().length);
                    if (data.getData().length > 0) {
                        parseAndSaveGetAllLocalitiesData(data);
                        final int currentpageIndex = pageIndex + 1;
                        getAllLocalitiesService(pageSize, currentpageIndex, new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String workName, boolean isComplete) {
                                Log.d(Contants.LOG_TAG, "service caller complete called");
                                if (isComplete) {
                                    Log.d(Contants.LOG_TAG, "localities List call all done: records downloaded: " + currentpageIndex * pageSize);
                                }
                                if (workName.equalsIgnoreCase("listService recursion done")) {
                                    Log.d(Contants.LOG_TAG, "recurse service caller workitemcallback called.");
                                    workCompletedCallback.onDone("listService recursion done", true);
                                }
                            }
                        });

                    } else {
                        updateLastRequestDateTime(url, data);
                        Log.d(Contants.LOG_TAG, "service caller workitemcallback called.");
                        workCompletedCallback.onDone("listService recursion done", true);
                    }
                } else {
                    workCompletedCallback.onDone("listService recursion done", false);
                }
            }

        });
    }

    //parse and save getAll Localities data
    public void parseAndSaveGetAllLocalitiesData(final ContentDataAsArray data) {
        DbHelper dbhelper = new DbHelper(context);
        if (data.getData() != null && data.getData().length != 0) {
            for (Data localtiesData : data.getData()) {
                dbhelper.upsertGetAllLocalitiesData(localtiesData);
            }
        }

    }

    //call Delete address data
    public void callDeleteAddressDataService(int addressId, int loginId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.DeleteAddress;
        JSONObject obj = new JSONObject();
        try {
            obj.put("AddressId", addressId);
            obj.put("LoginID", loginId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    ContentData d = new Gson().fromJson(result, ContentData.class);
                    if (d.getResult() != null) {
                        if (d.getResult().getSuccess()) {
                            workCompletedCallback.onDone("Address delete done", true);
                        } else {
                            workCompletedCallback.onDone("Address delete not done", false);
                        }
                    } else {
                        workCompletedCallback.onDone("Address delete not done", false);
                    }
                }
            }
        });
    }

    //get all store list data...........
    public void callGetAllStoreListService(int localityId, int loginId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.GetAllStoreByLocality;
        JSONObject obj = new JSONObject();
        try {
            obj.put("LocalityId", localityId);
            obj.put("LoginID", loginId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseAndSavesStoreListData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("callGetAllStoreListService done", false);
                }
            }
        });
    }

    //parse and save get all store list data
    public void parseAndSavesStoreListData(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentDataAsArray data = new Gson().fromJson(result, ContentDataAsArray.class);
                DbHelper dbHelper = new DbHelper(context);
                if (data != null) {
                    if (data.getResponse() != null && data.getResponse().getSuccess()) {
                        dbHelper.deleteAllStoreData();
                        for (Data storeData : data.getData()) {
                            if (storeData != null) {
                                dbHelper.upsertAllStore(storeData);
                            }
                        }
                        flag = true;
                    }
                }
                return flag;
            }


            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("callGetAllStoreListService done", true);
                } else {
                    workCompletedCallback.onDone("callGetAllStoreListService done", false);
                }
            }
        }.execute();
    }


    //call all menulist by store id
    public void callGetAllMenuListService(int storeId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.GetAllMenuListByStoreId;
        JSONObject obj = new JSONObject();
        try {
            obj.put("StoreId", storeId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseAndSaveAllMenuListData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("callGetAllMenuListService done", false);
                }
            }
        });
    }

    //parse and save All CategoryList data
    public void parseAndSaveAllMenuListData(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentDataAsArray data = new Gson().fromJson(result, ContentDataAsArray.class);
                if (data != null) {
                    if (data.getData() != null) {
                        DbHelper dbHelper = new DbHelper(context);
                        dbHelper.deleteAllMenuData();
                        for (Data menuData : data.getData()) {
                            if (menuData != null) {
                                dbHelper.upsertMenuData(menuData);
                            }
                        }
                        flag = true;
                    }
                }
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("callAllCategoryListService done", true);
                } else {
                    workCompletedCallback.onDone("callAllCategoryListService done", false);
                }
            }
        }.execute();
    }


    //call All CategoryList data
    public void callAllCategoryListService(int storeId, int menuId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.GetAllCategoryByMenuId;
        JSONObject obj = new JSONObject();
        try {
            obj.put("MenuId", menuId);
            obj.put("StoreId", storeId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseAndSaveAllCategoryListData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("callAllCategoryListService done", false);
                }
            }
        });
    }

    //parse and save All CategoryList data
    public void parseAndSaveAllCategoryListData(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentDataAsArray data = new Gson().fromJson(result, ContentDataAsArray.class);
                if (data != null) {
                    if (data.getResponse() != null && data.getResponse().getSuccess()) {
                        DbHelper dbHelper = new DbHelper(context);
                        dbHelper.deleteAllCategoryData();
                        for (Data categoryData : data.getData()) {
                            if (categoryData != null) {
                                dbHelper.upsertAllCategory(categoryData);
                            }
                        }
                        flag = true;
                    }
                }
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("callAllCategoryListService done", true);
                } else {
                    workCompletedCallback.onDone("callAllCategoryListService done", false);
                }
            }
        }.execute();
    }

    //call All CategoryList data
    public void callAllProductListService(int storeId, int categoryId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.GetAllProductByCategory;
        JSONObject obj = new JSONObject();
        try {
            obj.put("StoreId", storeId);
            obj.put("CategoryId", categoryId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseAndSaveAllProductListData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("callAllProductListService done", false);
                }
            }
        });
    }

    //parse and save All product data
    public void parseAndSaveAllProductListData(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentDataAsArray data = new Gson().fromJson(result, ContentDataAsArray.class);
                if (data != null) {
                    if (data.getResponse() != null && data.getResponse().getSuccess()) {
                        DbHelper dbHelper = new DbHelper(context);
                        dbHelper.deleteAllProductData();
                        for (Data productData : data.getData()) {
                            if (productData != null) {
                                dbHelper.upsertAllProduct(productData);
                            }
                        }
                        flag = true;
                    }
                }
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("callAllProductListService done", true);
                } else {
                    workCompletedCallback.onDone("callAllProductListService done", false);
                }
            }
        }.execute();
    }

    //apply promo code
    public void applyPromoCodeService(String code, int loginId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.ApplyPromoCode;
        JSONObject obj = new JSONObject();
        try {
            obj.put("PromoCode", code);
            obj.put("LoginId", loginId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    workCompletedCallback.onDone(result, true);
                } else {
                    workCompletedCallback.onDone("loginService done", false);
                }
            }
        });
    }

    //...........Get FavouriteStoreByUser  data....................

    public void callGetFavouriteStoreByUserService(int LoginID, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.GetFavouriteStoreByUser;
        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginID", LoginID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseAndSaveFavouriteStoreByUserData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("callGetFavouriteStoreByUserService done", false);
                }
            }
        });
    }

    //parse and save Favourite Store By User data
    public void parseAndSaveFavouriteStoreByUserData(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentData data = new Gson().fromJson(result, ContentData.class);
                if (data != null) {
                    if (data.getResponse() != null && data.getResponse().getSuccess()) {
                        DbHelper dbHelper = new DbHelper(context);
                        if (data.getData() != null) {
                            dbHelper.deleteFavouriteStoreData();
                            dbHelper.upsertFavouriteStoreData(data.getData());
                            flag = true;
                        }
                    }
                }
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("callGetFavouriteStoreByUserService done", true);
                } else {
                    workCompletedCallback.onDone("callGetFavouriteStoreByUserService done", false);
                }
            }
        }.execute();
    }
    //.................................Add update favourite store ..............................


    public void callAddUpdateFavouriteStoreByUserService(int LoginID, int StoreId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.AddUpdateFavourite;
        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginID", LoginID);
            obj.put("FavouriteStoreId", StoreId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseAndSaveAddUpdateFavouriteStoreByUserData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("callAddUpdateFavouriteStoreByUserService done", false);
                }
            }
        });
    }

    //parse and save Favourite Store By User data
    public void parseAndSaveAddUpdateFavouriteStoreByUserData(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentData data = new Gson().fromJson(result, ContentData.class);
                if (data != null) {
                    if (data.getResponse() != null && data.getResponse().getSuccess()) {
                        DbHelper dbHelper = new DbHelper(context);
                        if (data.getData() != null) {
                            dbHelper.upsertFavouriteStoreData(data.getData());
                        }
                    }
                    flag = true;
                }
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("callAddUpdateFavouriteStoreByUserService done", true);
                } else {
                    workCompletedCallback.onDone("callAddUpdateFavouriteStoreByUserService done", false);
                }
            }
        }.execute();
    }
    //............................. my all order history.................................

    //get My All Order  history data................
    public void callMyAllOrderHistoryService(int LoginId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.GetMyAllOrderHistory;
        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginId", LoginId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseAndSaveMyAllOrderHistoryData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("callMyAllOrderHistoryService done", false);
                }
            }
        });
    }

    //parse and save All MyOrder   History data
    public void parseAndSaveMyAllOrderHistoryData(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentDataAsArray data = new Gson().fromJson(result, ContentDataAsArray.class);
                if (data != null) {
                    if (data.getResponse() != null && data.getResponse().getSuccess()) {
                        DbHelper dbHelper = new DbHelper(context);
                        for (Data objData : data.getData()) {
                            if (objData != null) {
                                dbHelper.upsertMyAllOrderHistoryData(objData);
                            }
                        }
                        flag = true;

                    }
                }

                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("callMyAllOrderHistoryService done", true);
                } else {
                    workCompletedCallback.onDone("callMyAllOrderHistoryService done", false);
                }
            }
        }.execute();
    }
    //............................. get all order by User (Track Order).................................

    //get all order by User  data................
    public void callGetAllOrderByUserService(int LoginId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.GetAllOrderByUser;
        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginId", LoginId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseAndSaveGetAllOrderByUserServiceData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("callGetAllOrderByUserService done", false);
                }
            }
        });
    }

    //parse and save Get All Order By User Service data
    public void parseAndSaveGetAllOrderByUserServiceData(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {


            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentDataAsArray data = new Gson().fromJson(result, ContentDataAsArray.class);
                if (data != null) {
                    if (data.getResponse() != null && data.getResponse().getSuccess()) {
                        DbHelper dbHelper = new DbHelper(context);
                        for (Data objData : data.getData()) {
                            if (objData != null) {
                                dbHelper.upsertGetAllOrderByUserData(objData);
                            }
                        }
                        flag = true;
                    }
                }

                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("callGetAllOrderByUserService done", true);
                } else {
                    workCompletedCallback.onDone("callGetAllOrderByUserService done", false);
                }
            }
        }.execute();
    }

    //create order service
    public void createOrderService(int StoreId, int addressId, int LoginId, int PromoCodeId, ArrayList<CreateOrderDetails> orderDetailsesList, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.CreateOrder;
        JSONObject obj = new JSONObject();
        try {
            obj.put("StoreId", StoreId);
            obj.put("AddressId", addressId);
            obj.put("LoginID", LoginId);
            obj.put("PromoCodeId", PromoCodeId);
            String orderListStr = new Gson().toJson(orderDetailsesList);
            JSONArray orderjsArray = new JSONArray(orderListStr);
            if (orderjsArray != null) {
                obj.put("orderDetails", orderjsArray);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    workCompletedCallback.onDone(result, true);
                } else {
                    workCompletedCallback.onDone("createOrderService done", false);
                }
            }
        });
    }

    //GetOrderByOrderNumber
    public void GetOrderByOrderNumberService(String OrderNumber, int StoreId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.GetOrderByOrderNumber;
        JSONObject obj = new JSONObject();
        try {
            obj.put("OrderNumber", OrderNumber);
            obj.put("StoreId", StoreId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                     workCompletedCallback.onDone(result, true);
//                    Log.d(Contants.LOG_TAG, "dataahjhhh*****" + result);
//                    parseAndSaveGetOrderByOrderNumber(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("GetOrderByOrderNumber done", false);
                }
            }
        });
    }

//    //parse and save GetOrderByOrderNumber
//    public void parseAndSaveGetOrderByOrderNumber(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
//        new AsyncTask<Void, Void, Boolean>() {
//            @Override
//            protected Boolean doInBackground(Void... voids) {
//                Boolean flag = false;
//                ContentData contentData = new Gson().fromJson(result, ContentData.class);
//                if (contentData != null) {
//                    Data orderData = contentData.getData();
//                    if (orderData != null) {
//                        DbHelper dbHelper = new DbHelper(context);
//                        dbHelper.upsertGetAllOrderByUserData(orderData);
//                    }
//                    flag = true;
//                }
//                return flag;
//            }
//
//            @Override
//            protected void onPostExecute(Boolean flag) {
//                super.onPostExecute(flag);
//                if (flag) {
//                    workCompletedCallback.onDone("GetOrderByOrderNumberService done", true);
//                } else {
//                    workCompletedCallback.onDone("GetOrderByOrderNumberService done", false);
//                }
//            }
//        }.execute();
//    }

    // track order status data..................................
//GetOrderByOrderNumber
    public void callTrackOrderStatusService(String OrderNumber, int StoreId, int LoginID, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.TrackOrderStatus;
        JSONObject obj = new JSONObject();
        try {
            obj.put("OrderNumber", OrderNumber);
            obj.put("StoreId", StoreId);
            obj.put("LoginID", LoginID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    // workCompletedCallback.onDone(result, true);
                    parseAndSavecallTrackOrderStatusService(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("callTrackOrderStatusService done", false);
                }
            }
        });
    }

    //parse and save GetOrderByOrderNumber
    public void parseAndSavecallTrackOrderStatusService(final String result, final IAsyncWorkCompletedCallback workCompletedCallback) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean flag = false;
                ContentData contentData = new Gson().fromJson(result, ContentData.class);
                if (contentData != null) {
                    Data orderData = contentData.getData();
                    if (orderData != null) {
                        DbHelper dbHelper = new DbHelper(context);
                        dbHelper.upsertGetAllOrderByUserData(orderData);
                    }
                    flag = true;
                }
                return flag;
            }

            @Override
            protected void onPostExecute(Boolean flag) {
                super.onPostExecute(flag);
                if (flag) {
                    workCompletedCallback.onDone("callTrackOrderStatusService done", true);
                } else {
                    workCompletedCallback.onDone("callTrackOrderStatusService done", false);
                }
            }
        }.execute();
    }

    //upload image  service
    public void UploadImageService(int LoginId, String base64Image, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.UploadProfilePicture;
        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginID", LoginId);
            obj.put("ImageUrl", base64Image);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    workCompletedCallback.onDone("UploadImageService done", true);
                } else {
                    workCompletedCallback.onDone("UploadImageService done", false);
                }
            }
        });
    }

    //call social login data
    public void callSocialLoginService(String email, String personName, String phone, String mAccessToken, String FirebaseToken, String fb_url, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.SocialUserLogin;
        JSONObject obj = new JSONObject();
        try {
            obj.put("PhoneNumber", phone);
            obj.put("Name", personName);
            obj.put("EmailID", email);
            obj.put("FbToken", mAccessToken);
            obj.put("DeviceId", FirebaseToken);
            obj.put("ProfilePictureUrl", fb_url);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    parseAndSaveLoginData(result, workCompletedCallback);
                } else {
                    workCompletedCallback.onDone("loginService done", false);
                }
            }
        });
    }

    //make usfavorite store
    public void makeUnFavoriteStoreService(int loginId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.RemoveFavouriteStoreByUser;
        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginID", loginId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    workCompletedCallback.onDone(result, true);
                } else {
                    workCompletedCallback.onDone("PaymentGateway done", false);
                }
            }
        });
    }

    //get payment url
    public void getPaymentUrlService(String orderNo, int loginId, int storeId, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.CreatePaymentOrder;
        JSONObject obj = new JSONObject();
        try {
            obj.put("StoreId", storeId);
            obj.put("OrderNumber", orderNo);
            obj.put("LoginID", loginId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    workCompletedCallback.onDone(result, true);
                } else {
                    workCompletedCallback.onDone("PaymentGateway done", false);
                }
            }
        });
    }


    //confirm payment details service...........
    //get payment url
    public void sendConfirmDetailsToServerService(int loginId, int OrderId, String OrderNo, int StoreId, String transaction_id, String payment_id, String PaymentOrderId, String PaymentMode, final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.PaymentConfirmation;
        JSONObject obj = new JSONObject();
        try {
            obj.put("LoginId", loginId);
            obj.put("OrderId", OrderId);
            obj.put("OrderNumber", OrderNo);
            obj.put("StoreId", StoreId);
            obj.put("TransactionId", transaction_id);
            obj.put("PaymentId", payment_id);
            obj.put("PaymentOrderId", PaymentOrderId);
            obj.put("PaymentMode", PaymentMode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    workCompletedCallback.onDone(result, true);
                } else {
                    workCompletedCallback.onDone("PaymentConfirm done", false);
                }
            }
        });
    }


    //send device token to server
    public void callSendDeviceTokenRegistrationToServer(final IAsyncWorkCompletedCallback workCompletedCallback) {

        final String url = Contants.SERVICE_BASE_URL + Contants.Notification;
        String deviceToken = Utility.getDeviceIDFromSharedPreferences(context);
        DbHelper dbHelper = new DbHelper(context);
        Data userData = dbHelper.getUserData();
        int loginId = 0;
        if (userData != null) {
            loginId = userData.getLoginId();
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put("DeviceId", deviceToken);
            if (loginId != 0) {
                //obj.put("LoginId", loginId);
            }
            // obj.put("DeviceName", Utility.getDeviceName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    ContentData data = new Gson().fromJson(result, ContentData.class);
                    if (data != null) {
                        workCompletedCallback.onDone("DeviceTokenAddService done", true);
                    } else {
                        workCompletedCallback.onDone("DeviceTokenAddService done", false);
                    }
                } else {
                    workCompletedCallback.onDone("DeviceTokenAddService done", false);
                }
            }
        });
    }


    public void getCODService(String orderNo, int loginId, int storeId, String PaymentMode, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final String url = Contants.SERVICE_BASE_URL + Contants.PaymentConfirmforCOD;
        JSONObject obj = new JSONObject();
        try {
            obj.put("StoreId", storeId);
            obj.put("OrderNumber", orderNo);
            obj.put("LoginId", loginId);
            obj.put("PaymentMode", PaymentMode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Contants.LOG_TAG, "Payload*****" + obj);
        new ServiceHelper().callService(url, obj, new IServiceSuccessCallback() {
            @Override
            public void onDone(String doneWhatCode, String result, String error) {
                if (result != null) {
                    workCompletedCallback.onDone(result, true);
                } else {
                    workCompletedCallback.onDone("PaymentGateway done", false);
                }
            }
        });
    }
}
