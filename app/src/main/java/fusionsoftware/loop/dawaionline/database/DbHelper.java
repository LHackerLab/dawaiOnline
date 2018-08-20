package fusionsoftware.loop.dawaionline.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import fusionsoftware.loop.dawaionline.model.Addresses;
import fusionsoftware.loop.dawaionline.model.Data;
import fusionsoftware.loop.dawaionline.model.MyBasket;
import fusionsoftware.loop.dawaionline.model.OrderDetails;
import fusionsoftware.loop.dawaionline.model.Result;
import fusionsoftware.loop.dawaionline.utilities.Contants;


/**
 * Created by lalit on 7/25/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = Contants.DATABASE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS userData");
        db.execSQL("DROP TABLE IF EXISTS cityData");
        db.execSQL("DROP TABLE IF EXISTS MyOrderDataEntity");
        db.execSQL("DROP TABLE IF EXISTS searchProductEntity");
        db.execSQL("DROP TABLE IF EXISTS categoryEntity");
        db.execSQL("DROP TABLE IF EXISTS MyOrderHistoryDataEntity");
        db.execSQL("DROP TABLE IF EXISTS TrackOrderDataEntity");
        db.execSQL("DROP TABLE IF EXISTS MenuListDataEntity");
        onCreate(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_user_TABLE = "CREATE TABLE userData(LoginId INTEGER,PhoneNumber TEXT,Name TEXT,Otp INTEGER,EmailID TEXT,ProfilePictureUrl TEXT)";
        db.execSQL(CREATE_user_TABLE);
        String CREATE_city_TABLE = "CREATE TABLE cityData(cityId INTEGER,cityName TEXT,shippingCharge REAL)";
        db.execSQL(CREATE_city_TABLE);
        String CREATE_MyOrder_TABLE = "CREATE TABLE MyOrderDataEntity(ProductId INTEGER,ProductName TEXT,Quantity REAL,Price REAL, OrderTime TEXT,CategoryId INTEGER,discount REAL,CategoryName TEXT)";
        db.execSQL(CREATE_MyOrder_TABLE);
        String searchProductEntity = "CREATE TABLE searchProductEntity(ProductId INTEGER,ProductName TEXT,UnitPrice REAL,Discount REAL,ProductDetails TEXT,CategoryId INTEGER,ProductPicturesUrl TEXT,ProductSubTitle TEXT)";
        db.execSQL(searchProductEntity);
        String CREATE_MyOrderHistory_TABLE = "CREATE TABLE MyOrderHistoryDataEntity(OrderId INTEGER,OrderNumber TEXT,StoreId INTEGER,ProductId INTEGER,LoginId INTEGER,Quantity REAL,OrderTime TEXT,TotalPrice REAL,NetPrice REAL,SpecialDiscount REAL,SubTotal REAL,TotalGST REAL,GrandTotal REAL,shippingCharge REAL,PromoDiscount REAL,OrderStatus TEXT,UOM TEXT,OrderDetails TEXT,StoreName TEXT)";//NetPrice REAL,SpecialDiscount REAL,SubTotal REAL,TotalGST REAL,GrandTotal REAL,shippingCharge REAL,PromoDiscount REAL,
        db.execSQL(CREATE_MyOrderHistory_TABLE);
        String CREATE_TrackOrder_TABLE = "CREATE TABLE TrackOrderDataEntity(OrderId INTEGER,OrderNumber TEXT,StoreId INTEGER,ProductId INTEGER,LoginId INTEGER,Quantity REAL,OrderTime TEXT,TotalPrice REAL,NetPrice REAL,SpecialDiscount REAL,SubTotal REAL,TotalGST REAL,GrandTotal REAL,shippingCharge REAL,PromoDiscount REAL,OrderStatus TEXT,UOM TEXT,OrderDetails TEXT,StoreName TEXT)";//UOM TEXT,
        db.execSQL(CREATE_TrackOrder_TABLE);

        String CREATE_Menu_List_TABLE = "CREATE TABLE MenuListDataEntity(MenuId INTEGER, MenuName TEXT,ImageUrl TEXT)";
        db.execSQL(CREATE_Menu_List_TABLE);

        String categoryEntity = "CREATE TABLE categoryEntity(CategoryId INTEGER, CategoryName TEXT,CategoryPictures TEXT)";
        //            "MenuId","MenuName","ImageUrl"
        db.execSQL(categoryEntity);

    }

    //--------------------------userDataData---------------
    public boolean upsertUserData(Result ob) {
        boolean done = false;
        Result data = null;
        if (ob.getLoginId() != 0) {
            data = getUserDataByLoginId(ob.getLoginId());
            if (data == null) {
                done = insertUserData(ob);
            } else {
                done = updateUserData(ob);
            }
        }
        return done;
    }

    //insert userData data.............
    public boolean insertUserData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("LoginId", ob.getLoginId());
        values.put("PhoneNumber", ob.getPhoneNumber());
        values.put("Name", ob.getName());
        values.put("otp", ob.getOtp());
        values.put("EmailId", ob.getEmailID());
        values.put("ProfilePictureUrl", ob.getProfilePictureUrl());

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("userData", null, values);
        db.close();
        return i > 0;
    }

    // for userData data.............
    private void populateUserformation(Cursor cursor, Result ob) {
        ob.setLoginId(cursor.getInt(0));
        ob.setPhoneNumber(cursor.getString(1));
        ob.setName(cursor.getString(2));
        ob.setOtp(cursor.getInt(3));
        ob.setEmailID(cursor.getString(4));
        ob.setProfilePictureUrl(cursor.getString(6));
    }

    //userData data
    public Result getUserData() {

        String query = "Select * FROM userData";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateUserformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //userData data
    public Result getUserDataByLoginId(int id) {

        String query = "Select * FROM userData WHERE LoginId = " + id + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateUserformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //update user data
    public boolean updateUserData(Result ob) {
        ContentValues values = new ContentValues();

        values.put("LoginId", ob.getLoginId());
        values.put("PhoneNumber", ob.getPhoneNumber());
        values.put("Name", ob.getName());
        values.put("otp", ob.getOtp());
        values.put("EmailId", ob.getEmailID());
        values.put("ProfilePictureUrl", ob.getProfilePictureUrl());

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("userData", values, "loginId = '" + ob.getLoginId() + "' ", null);

        db.close();
        return i > 0;
    }

    // delete Address Data from addressId
    public boolean deleteUserData(int LoginId) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "LoginId = " + LoginId + " ";
        db.delete("userData", query, null);
        db.close();
        return result;
    }

    //.......................city data...............................


    //--------------------------cityDataData---------------
    public boolean upsertCityData(Result ob) {
        boolean done = false;
        Result data = null;
        if (ob.getCityId() != 0) {
            data = getCityDataByCityName(ob.getCityName());
            if (data == null) {
                done = insertCityData(ob);
            } else {
                done = updateCityData(ob);
            }
        }
        return done;
    }


    //insert city data.............
    public boolean insertCityData(Result ob) {
        ContentValues values = new ContentValues();

        values.put("cityId", ob.getCityId());
        values.put("cityName", ob.getCityName());
        values.put("shippingCharge", ob.getShippingCharge());

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("cityData", null, values);
        db.close();
        return i > 0;
    }

    // for city data.............
    private void populateCityformation(Cursor cursor, Result ob) {
        ob.setCityId(cursor.getInt(0));
        ob.setCityName(cursor.getString(1));
        ob.setShippingCharge(cursor.getFloat(2));
    }

    //city data
    public Result getCityData() {

        String query = "Select * FROM cityData";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateCityformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //city data
    public Result getCityDataByCityName(String name) {
        Result ob = new Result();
        String query = "Select * FROM cityData WHERE  cityName= '" + name + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateCityformation(cursor, ob);

            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    // city list data
    public List<Result> GetAllCityData() {
        ArrayList<Result> list = new ArrayList<Result>();
        String query = "Select * FROM cityData ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populateCityformation(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //update city data
    public boolean updateCityData(Result ob) {
        ContentValues values = new ContentValues();

        values.put("cityId", ob.getCityId());
        values.put("cityName", ob.getCityName());
        values.put("shippingCharge", ob.getShippingCharge());

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("cityData", values, "cityId = '" + ob.getCityId() + "' ", null);

        db.close();
        return i > 0;
    }

    // delete city data
    public boolean deleteCityData(int cityId) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "cityId = " + cityId + " ";
        db.delete("cityData", query, null);
        db.close();
        return result;
    }


    //------------basket Order data----------------
    public boolean upsertBasketOrderData(MyBasket ob) {
        boolean done = false;
        MyBasket data = null;
        if (ob.getProductId() != 0) {
            data = getBasketOrderData(ob.getProductId());
            if (data == null) {
                done = insertBasketOrderData(ob);
            } else {
                done = updateBasketOrderData(ob);
            }
        }
        return done;
    }

    //GetAll Basket Order
    private void populateBasketOrderData(Cursor cursor, MyBasket ob) {
        ob.setProductId(cursor.getInt(0));
        ob.setProductName(cursor.getString(1));
        ob.setQuantity(cursor.getFloat(2));
        ob.setPrice(cursor.getFloat(3));
        ob.setOrderTime(cursor.getString(4));
        ob.setCategoryId(cursor.getInt(5));
        ob.setDiscount(cursor.getFloat(6));
        ob.setCategoryName(cursor.getString(7));
    }

    //show  Basket Order list data
    public List<MyBasket> GetAllBasketOrderData() {
        ArrayList<MyBasket> list = new ArrayList<MyBasket>();
        String query = "Select * FROM MyOrderDataEntity ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                MyBasket ob = new MyBasket();
                populateBasketOrderData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //show  Basket Order list data
    public List<MyBasket> GetAllBasketOrderDataBasedOnCategoryId(int CategoryId) {
        ArrayList<MyBasket> list = new ArrayList<MyBasket>();
        String query = "Select * FROM MyOrderDataEntity WHERE CategoryId= " + CategoryId + "";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                MyBasket ob = new MyBasket();
                populateBasketOrderData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert Basket Order data
    public boolean insertBasketOrderData(MyBasket ob) {
        ContentValues values = new ContentValues();
        populateBasketOrderValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("MyOrderDataEntity", null, values);
        db.close();
        return i > 0;
    }

    //Basket Order data by id
    public MyBasket getBasketOrderData(int id) {
        String query = "Select * FROM MyOrderDataEntity WHERE ProductId= " + id + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MyBasket data = new MyBasket();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateBasketOrderData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    private void populateBasketOrderValue(MyBasket ob, ContentValues values) {
        values.put("ProductId", ob.getProductId());
        values.put("ProductName", ob.getProductName());
        values.put("Quantity", ob.getQuantity());
        values.put("Price", ob.getPrice());
        values.put("OrderTime", ob.getOrderTime());
        values.put("CategoryId", ob.getCategoryId());
        values.put("discount", ob.getDiscount());
        values.put("CategoryName", ob.getCategoryName());

    }

    //ProductId,ProductName,BasketId,StoreId,Quantity,Price,OrderTime  MyOrderDataEntity
    //update Basket Order data
    public boolean updateBasketOrderData(MyBasket ob) {
        ContentValues values = new ContentValues();
        populateBasketOrderValue(ob, values);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("MyOrderDataEntity", values, "ProductId = " + ob.getProductId() + "", null);
        db.close();
        return i > 0;
    }

    // delete Basket Order Data By Product Id ...........
    public boolean deleteBasketOrderDataByProductId(int productId) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "ProductId = " + productId + " ";
        db.delete("MyOrderDataEntity", query, null);
        db.close();
        return result;
    }

    // delete Basket Order Data By category Id ...........
    public boolean deleteBasketOrderDataByCategoryId(int category) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "CategoryId = " + category + " ";
        db.delete("MyOrderDataEntity", query, null);
        db.close();
        return result;
    }


    // delete all  Data
    public boolean deleteAllBasketOrderData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MyOrderDataEntity", null, null);
        db.close();
        return result;
    }

    // get Basket OrderData
    public MyBasket getBasketOrderData() {

        String query = "Select * FROM MyOrderDataEntity";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MyBasket data = new MyBasket();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateBasketOrderData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }


    //------------SearchProduct data----------------
    public boolean upsertSearchProductData(Result ob) {
        boolean done = false;
        Result data = null;
        if (ob.getProductId() != 0) {
            data = getSearchProductData(ob.getProductId());
            if (data == null) {
                done = insertSearchProductData(ob);
            } else {
                done = updateSearchProductData(ob);
            }
        }
        return done;
    }

    //GetAll SearchProduct Order
    private void populateSearchProductData(Cursor cursor, Result ob) {
        ob.setProductId(cursor.getInt(0));
        ob.setProductName(cursor.getString(1));
        ob.setUnitPrice(cursor.getFloat(2));
        ob.setDiscount(cursor.getFloat(3));
        ob.setProductDetails(cursor.getString(4));
        ob.setCategoryId(cursor.getInt(5));
        ob.setProductPicturesUrl(cursor.getString(6));
        ob.setProductSubTitle(cursor.getString(7));
    }

    //show  SearchProduct  list data
    public List<Result> GetAllSearchProductData() {
        ArrayList<Result> list = new ArrayList<Result>();
        String query = "Select * FROM searchProductEntity ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populateSearchProductData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert SearchProduct data
    public boolean insertSearchProductData(Result ob) {
        ContentValues values = new ContentValues();
        populateSearchProductValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("searchProductEntity", null, values);
        db.close();
        return i > 0;
    }

    //SearchProduct by id
    public Result getSearchProductData(int id) {
        String query = "Select * FROM searchProductEntity WHERE ProductId= " + id + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateSearchProductData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    private void populateSearchProductValue(Result ob, ContentValues values) {
        values.put("ProductId", ob.getProductId());
        values.put("ProductName", ob.getProductName());
        values.put("UnitPrice", ob.getUnitPrice());
        values.put("Discount", ob.getDiscount());
        values.put("ProductDetails", ob.getProductDetails());
        values.put("CategoryId", ob.getCategoryId());
        values.put("ProductPicturesUrl", ob.getProductPicturesUrl());
        values.put("ProductSubTitle", ob.getProductSubTitle());

    }

    //update SearchProduct
    public boolean updateSearchProductData(Result ob) {
        ContentValues values = new ContentValues();
        populateSearchProductValue(ob, values);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("searchProductEntity", values, "ProductId = " + ob.getProductId() + "", null);
        db.close();
        return i > 0;
    }


    //------------category data----------------
    public boolean upsertCategoryData(Result ob) {
        boolean done = false;
        Result data = null;
        if (ob.getCategoryId() != 0) {
            data = getCategoryData(ob.getCategoryId());
            if (data == null) {
                done = insertCategoryData(ob);
            } else {
                done = updateCategoryData(ob);
            }
        }
        return done;
    }

    //GetAll Category Order
    private void populateCategoryData(Cursor cursor, Result ob) {
        ob.setCategoryId(cursor.getInt(0));
        ob.setCategoryName(cursor.getString(1));
        ob.setCategoryPictures(cursor.getString(2));
    }

    //show  Category  list data
    public List<Result> GetAllCategoryData() {
        ArrayList<Result> list = new ArrayList<Result>();
        String query = "Select * FROM categoryEntity ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populateCategoryData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert Category data
    public boolean insertCategoryData(Result ob) {
        ContentValues values = new ContentValues();
        populateCategoryValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("categoryEntity", null, values);
        db.close();
        return i > 0;
    }

    //Category by id
    public Result getCategoryData(int id) {
        String query = "Select * FROM categoryEntity WHERE CategoryId= " + id + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateCategoryData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    private void populateCategoryValue(Result ob, ContentValues values) {
        values.put("CategoryId", ob.getCategoryId());
        values.put("CategoryName", ob.getCategoryName());
        values.put("CategoryPictures", ob.getCategoryPictures());

    }

    //update Category
    public boolean updateCategoryData(Result ob) {
        ContentValues values = new ContentValues();
        populateCategoryValue(ob, values);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("categoryEntity", values, "CategoryId = " + ob.getCategoryId() + "", null);
        db.close();
        return i > 0;
    }

    //----------------My order history Data-----------......................

    // upsert for my all order history data..............
    public boolean upsertMyAllOrderHistoryData(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getOrderId() != 0) {
            data = getMyAllOrderHistoryDataByOrderId(ob.getOrderId());
            if (data == null) {
                done = insertMyAllOrderHistoryData(ob);
            } else {
                done = updateMyAllOrderHistoryData(ob);
            }
        }
        return done;
    }


    //Get All My Order history................
    private void populateMyAllOrderHistoryData(Cursor cursor, Data ob) {
        ob.setOrderId(cursor.getInt(0));
        ob.setOrderNumber(cursor.getString(1));
        ob.setStoreId(cursor.getInt(2));
        ob.setProductId(cursor.getInt(3));
        ob.setLoginId(cursor.getInt(4));
        ob.setQuantity(cursor.getFloat(5));
        ob.setOrderTime(cursor.getString(6));
        ob.setTotalPrice(cursor.getFloat(7));
        ob.setNetPrice(cursor.getFloat(8));
        ob.setSpecialDiscount(cursor.getFloat(9));
        ob.setSubTotal(cursor.getFloat(10));
        ob.setTotalGST(cursor.getFloat(11));
        ob.setGrandTotal(cursor.getFloat(12));
        ob.setShippingCharge(cursor.getFloat(13));
        ob.setPromoDiscount(cursor.getFloat(14));
        ob.setOrderStatus(cursor.getString(15));
        OrderDetails[] orderDetailses = new Gson().fromJson(cursor.getString(16), new TypeToken<OrderDetails[]>() {
        }.getType());
        ob.setOrderDetails(orderDetailses);
        ob.setStoreName(cursor.getString(18));
//        ob.setOrderId(cursor.getInt(0));
//        ob.setOrderNumber(cursor.getString(1));
//        ob.setStoreId(cursor.getInt(2));
//        ob.setProductId(cursor.getInt(3));
//        ob.setLoginId(cursor.getInt(4));
//        ob.setQuantity(cursor.getFloat(5));
//        ob.setOrderTime(cursor.getString(6));
//        ob.setTotalPrice(cursor.getFloat(7));
//        ob.setOrderStatus(cursor.getString(8));
//        OrderDetails[] orderDetailses = new Gson().fromJson(cursor.getString(9), new TypeToken<OrderDetails[]>() {
//        }.getType());
//        ob.setOrderDetails(orderDetailses);
    }

    //get  Myorder history data.................
    public List<Data> GetMyAllOrderHistoryData() {

        String query = "Select * FROM MyOrderHistoryDataEntity";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        List<Data> list = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateMyAllOrderHistoryData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //get  Myorder history data.................
    public List<Data> GetMyAllOrderHistoryDataByOrderId(String OrderNumber) {

        String query = "Select * FROM MyOrderHistoryDataEntity WHERE  OrderNumber= '" + OrderNumber + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        List<Data> list = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateMyAllOrderHistoryData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert MyOrder history data..................
    public boolean insertMyAllOrderHistoryData(Data ob) {
        ContentValues values = new ContentValues();
        populateMyAllOrderHistoryValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("MyOrderHistoryDataEntity", null, values);
        db.close();
        return i > 0;
    }

    //MyOrder  history data by order id..................
    public Data getMyAllOrderHistoryDataByOrderId(int OrderId) {

        String query = "Select * FROM MyOrderHistoryDataEntity WHERE OrderId= " + OrderId + "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateMyAllOrderHistoryData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //OrderId INTEGER,OrderNumber TEXT,StoreId INTEGER,ProductId INTEGER,LoginId INTEGER,Quantity REAL,
    // OrderTime TEXT,TotalPrice REAL,NetPrice REAL,SpecialDiscount REAL,SubTotal REAL,TotalGST REAL,
    // GrandTotal REAL,shippingCharge REAL,PromoDiscount REAL,OrderStatus TEXT,OrderDetails TEXT)";
    private void populateMyAllOrderHistoryValue(Data ob, ContentValues values) {
        values.put("OrderId", ob.getOrderId());
        values.put("OrderNumber", ob.getOrderNumber());
        values.put("StoreId", ob.getStoreId());
        values.put("ProductId", ob.getProductId());
        values.put("LoginId", ob.getLoginId());
        values.put("Quantity", ob.getQuantity());
        values.put("OrderTime", ob.getOrderTime());
        values.put("TotalPrice", ob.getTotalPrice());
        values.put("NetPrice", ob.getNetPrice());
        values.put("SpecialDiscount", ob.getSpecialDiscount());
        values.put("SubTotal", ob.getSubTotal());
        values.put("TotalGST", ob.getTotalGST());
        values.put("GrandTotal", ob.getGrandTotal());
        values.put("shippingCharge", ob.getShippingCharge());
        values.put("PromoDiscount", ob.getPromoDiscount());
        values.put("Orderstatus", ob.getOrderStatus());
        if (ob.getOrderDetails() != null) {
            String orderDetails = new Gson().toJson(ob.getOrderDetails());
            values.put("OrderDetails", orderDetails);
        }
        values.put("StoreName", ob.getStoreName());
//        values.put("OrderId", ob.getOrderId());
//        values.put("OrderNumber", ob.getOrderNumber());
//        values.put("StoreId", ob.getStoreId());
//        values.put("ProductId", ob.getProductId());
//        values.put("LoginId", ob.getLoginId());
//        values.put("Quantity", ob.getQuantity());
//        values.put("OrderTime", ob.getOrderTime());
//        values.put("TotalPrice", ob.getTotalPrice());
//        values.put("Orderstatus", ob.getOrderStatus());
//        if (ob.getOrderDetails() != null) {
//            String orderDetails = new Gson().toJson(ob.getOrderDetails());
//            values.put("OrderDetails", orderDetails);
//        }
    }

    //update MyOrder  history data..............
    public boolean updateMyAllOrderHistoryData(Data ob) {
        ContentValues values = new ContentValues();
        populateMyAllOrderHistoryValue(ob, values);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("MyOrderHistoryDataEntity", values, "OrderId = " + ob.getOrderId() + "", null);
        db.close();
        return i > 0;
    }

    // delete all  Data
    public boolean deleteMyAllOrderHistoryData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MyOrderHistoryDataEntity", null, null);
        db.close();
        return result;
    }
    //----------------------Get All Order By User(Track Order) Data------------------

    // -------------upsert for get all order by user Data.......................
    public boolean upsertGetAllOrderByUserData(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getOrderId() != 0) {
            data = getGetAllOrderByUserDataByOrderId(ob.getOrderId());
            if (data == null) {
                done = insertGetAllOrderByUserData(ob);
            } else {
                done = updateGetAllOrderByUserData(ob);
            }
        }
        return done;
    }

    // -------------populate for get all order by user Data.......................
    private void populateGetAllOrderByUserData(Cursor cursor, Data ob) {
//        ob.setOrderId(cursor.getInt(0));
//        ob.setOrderNumber(cursor.getString(1));
//        ob.setStoreId(cursor.getInt(2));
//        ob.setProductId(cursor.getInt(3));
//        ob.setLoginId(cursor.getInt(4));
//        ob.setQuantity(cursor.getFloat(5));
//        ob.setOrderTime(cursor.getString(6));
//        ob.setTotalPrice(cursor.getFloat(7));
//        ob.setOrderStatus(cursor.getString(8));
//        ob.setStoreName(cursor.getString(9));
//        OrderDetails[] orderDetailses = new Gson().fromJson(cursor.getString(10), new TypeToken<OrderDetails[]>() {
//        }.getType());
//        ob.setOrderDetails(orderDetailses);
        //  ob.setUOM(cursor.getString(11));

        ob.setOrderId(cursor.getInt(0));
        ob.setOrderNumber(cursor.getString(1));
        ob.setStoreId(cursor.getInt(2));
        ob.setProductId(cursor.getInt(3));
        ob.setLoginId(cursor.getInt(4));
        ob.setQuantity(cursor.getFloat(5));
        ob.setOrderTime(cursor.getString(6));
        ob.setTotalPrice(cursor.getFloat(7));
        ob.setNetPrice(cursor.getFloat(8));
        ob.setSpecialDiscount(cursor.getFloat(9));
        ob.setSubTotal(cursor.getFloat(10));
        ob.setTotalGST(cursor.getFloat(11));
        ob.setGrandTotal(cursor.getFloat(12));
        ob.setShippingCharge(cursor.getFloat(13));
        ob.setPromoDiscount(cursor.getFloat(14));
        ob.setOrderStatus(cursor.getString(15));
        ob.setUOM(cursor.getString(16));
        OrderDetails[] orderDetailses = new Gson().fromJson(cursor.getString(17), new TypeToken<OrderDetails[]>() {
        }.getType());
        ob.setOrderDetails(orderDetailses);
        ob.setStoreName(cursor.getString(18));
    }

    //  Get All Order By User data..................
    public List<Data> getGetAllOrderByUserData() {

        String query = "Select * FROM TrackOrderDataEntity";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        List<Data> list = new ArrayList<Data>();
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateGetAllOrderByUserData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert Get All Order By User Data..................

    public boolean insertGetAllOrderByUserData(Data ob) {
        ContentValues values = new ContentValues();
        populateGetAllOrderByUserValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("TrackOrderDataEntity", null, values);
        db.close();
        return i > 0;
    }
    //get all order by user daqta by order id.............

    public Data getGetAllOrderByUserDataByOrderId(int OrderId) {

        String query = "Select * FROM TrackOrderDataEntity WHERE OrderId= " + OrderId + "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateGetAllOrderByUserData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    public Data getGetAllOrderByUserDataForStoreId() {

        String query = "Select * FROM TrackOrderDataEntity";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateGetAllOrderByUserData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //get  Myorder history data.................
    public List<Data> getGetAllOrderByUserDataByOrderNumber(String OrderNumber) {
        String query = "Select * FROM TrackOrderDataEntity WHERE OrderNumber= '" + OrderNumber + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        List<Data> list = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateGetAllOrderByUserData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //populate get all order by user data............
    private void populateGetAllOrderByUserValue(Data ob, ContentValues values) {
//        values.put("OrderId", ob.getOrderId());
//        values.put("OrderNumber", ob.getOrderNumber());
//        values.put("StoreId", ob.getStoreId());
//        values.put("ProductId", ob.getProductId());
//        values.put("LoginId", ob.getLoginId());
//        values.put("Quantity", ob.getQuantity());
//        values.put("OrderTime", ob.getOrderTime());
//        values.put("TotalPrice", ob.getTotalPrice());
//        values.put("Orderstatus", ob.getOrderStatus());
//        values.put("StoreName",ob.getStoreName());
//        if (ob.getOrderDetails() != null) {
//            String orderDetails = new Gson().toJson(ob.getOrderDetails());
//            values.put("OrderDetails", orderDetails);
//        }
        //  values.put("UOM", ob.getUOM());

        values.put("OrderId", ob.getOrderId());
        values.put("OrderNumber", ob.getOrderNumber());
        values.put("StoreId", ob.getStoreId());
        values.put("ProductId", ob.getProductId());
        values.put("LoginId", ob.getLoginId());
        values.put("Quantity", ob.getQuantity());
        values.put("OrderTime", ob.getOrderTime());
        values.put("TotalPrice", ob.getTotalPrice());
        values.put("NetPrice", ob.getNetPrice());
        values.put("SpecialDiscount", ob.getSpecialDiscount());
        values.put("SubTotal", ob.getSubTotal());
        values.put("TotalGST", ob.getTotalGST());
        values.put("GrandTotal", ob.getGrandTotal());
        values.put("shippingCharge", ob.getShippingCharge());
        values.put("PromoDiscount", ob.getPromoDiscount());
        values.put("Orderstatus", ob.getOrderStatus());
        values.put("UOM", ob.getUOM());
        if (ob.getOrderDetails() != null) {
            String orderDetails = new Gson().toJson(ob.getOrderDetails());
            values.put("OrderDetails", orderDetails);
        }
        values.put("StoreName", ob.getStoreName());
    }

    //update get all order by user data............
    public boolean updateGetAllOrderByUserData(Data ob) {
        ContentValues values = new ContentValues();
        populateGetAllOrderByUserValue(ob, values);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("TrackOrderDataEntity", values, "OrderId = " + ob.getOrderId() + "", null);

        db.close();
        return i > 0;
    }

    // delete deleteFavouriteStore Data By store Id
    public boolean deleteGetAllOrderDataById(String OrderNumber) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "OrderNumber = '" + OrderNumber + "' ";
        db.delete("TrackOrderDataEntity", query, null);
        db.close();
        return result;
    }

    // delete all  Data
    public boolean deleteTrackOrderData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("TrackOrderDataEntity", null, null);
        db.close();
        return result;
    }
}
