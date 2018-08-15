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
import fusionsoftware.loop.dawaionline.utilities.Contants;


/**
 * Created by lalit on 7/25/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = Contants.DATABASE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS userData");
        db.execSQL("DROP TABLE IF EXISTS GetAllAddressEntity");
        db.execSQL("DROP TABLE IF EXISTS ProductListDataEntity");
        db.execSQL("DROP TABLE IF EXISTS MyOrderDataEntity");
        db.execSQL("DROP TABLE IF EXISTS MyOrderHistoryDataEntity");
        db.execSQL("DROP TABLE IF EXISTS TrackOrderDataEntity");
        db.execSQL("DROP TABLE IF EXISTS MenuListDataEntity");
        onCreate(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_user_TABLE = "CREATE TABLE userData(LoginID INTEGER,PhoneNumber TEXT,Name TEXT,Otp INTEGER,EmailID TEXT,Role INTEGER,ProfilePictureUrl TEXT)";
        db.execSQL(CREATE_user_TABLE);
        String CREATE_getAllAddress_TABLE = "CREATE TABLE GetAllAddressEntity(AddressId INTEGER,CompleteAddress TEXT,CityId INTEGER,ZipCode TEXT,LoginID INTEGER,PhoneNumber TEXT,LandMark TEXT,LocalityId INTEGER,LocalityName TEXT)";//
        db.execSQL(CREATE_getAllAddress_TABLE);
        String CREATE_ProductList_TABLE = "CREATE TABLE ProductListDataEntity(ProductId INTEGER, ProductName TEXT,CategoryId INTEGER,UnitPrice REAL,Discount REAL,GST REAL,TaxType TEXT,UOM TEXT,ProductDetails TEXT,ImageUrl TEXT)";
        //ProductId,ProductName,CategoryId,UnitPrice,Discount,GST,TaxType,ProductDetails ProductListDataEntity
        db.execSQL(CREATE_ProductList_TABLE);

        String CREATE_MyOrder_TABLE = "CREATE TABLE MyOrderDataEntity(ProductId INTEGER,ProductName TEXT, StoreId INTEGER,Quantity REAL,Price REAL, OrderTime TEXT,CategoryId INTEGER,discount REAL,UOM TEXT)";
        //ProductId,ProductName,StoreId,Quantity,Price,OrderTime,CategoryId,discount  MyOrderDataEntity
        db.execSQL(CREATE_MyOrder_TABLE);
        String CREATE_MyOrderHistory_TABLE = "CREATE TABLE MyOrderHistoryDataEntity(OrderId INTEGER,OrderNumber TEXT,StoreId INTEGER,ProductId INTEGER,LoginId INTEGER,Quantity REAL,OrderTime TEXT,TotalPrice REAL,NetPrice REAL,SpecialDiscount REAL,SubTotal REAL,TotalGST REAL,GrandTotal REAL,shippingCharge REAL,PromoDiscount REAL,OrderStatus TEXT,UOM TEXT,OrderDetails TEXT,StoreName TEXT)";//NetPrice REAL,SpecialDiscount REAL,SubTotal REAL,TotalGST REAL,GrandTotal REAL,shippingCharge REAL,PromoDiscount REAL,
        db.execSQL(CREATE_MyOrderHistory_TABLE);
        String CREATE_TrackOrder_TABLE = "CREATE TABLE TrackOrderDataEntity(OrderId INTEGER,OrderNumber TEXT,StoreId INTEGER,ProductId INTEGER,LoginId INTEGER,Quantity REAL,OrderTime TEXT,TotalPrice REAL,NetPrice REAL,SpecialDiscount REAL,SubTotal REAL,TotalGST REAL,GrandTotal REAL,shippingCharge REAL,PromoDiscount REAL,OrderStatus TEXT,UOM TEXT,OrderDetails TEXT,StoreName TEXT)";//UOM TEXT,
        db.execSQL(CREATE_TrackOrder_TABLE);

        String CREATE_Menu_List_TABLE = "CREATE TABLE MenuListDataEntity(MenuId INTEGER, MenuName TEXT,ImageUrl TEXT)";
        //            "MenuId","MenuName","ImageUrl"
        db.execSQL(CREATE_Menu_List_TABLE);

    }

    //--------------------------userDataData---------------
    public boolean upsertUserData(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getLoginID() != 0) {
            data = getUserDataByLoginId(ob.getLoginID());
            if (data == null) {
                done = insertUserData(ob);
            } else {
                done = updateUserData(ob);
            }
        }
        return done;
    }

    // for user data..........
    private void populateUserData(Cursor cursor, Data ob) {
        ob.setLoginID(cursor.getInt(0));
        ob.setPhoneNumber(cursor.getString(1));
        ob.setName(cursor.getString(2));
        ob.setOtp(cursor.getInt(3));
        ob.setEmailID(cursor.getString(4));
        ob.setRole(cursor.getInt(5));
        ob.setProfilePictureUrl(cursor.getString(6));
    }

    //insert userData data.............
    public boolean insertUserData(Data ob) {
        ContentValues values = new ContentValues();

        values.put("LoginId", ob.getLoginID());
        values.put("Phonenumber", ob.getPhoneNumber());
        values.put("Name", ob.getName());
        values.put("otp", ob.getOtp());
        values.put("EmailId", ob.getEmailID());
        values.put("Role", ob.getRole());
        values.put("ProfilePictureUrl", ob.getProfilePictureUrl());

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("userData", null, values);
        db.close();
        return i > 0;
    }

    // for userData data.............
    private void populateUserformation(Cursor cursor, Data ob) {
        ob.setLoginID(cursor.getInt(0));
        ob.setPhoneNumber(cursor.getString(1));
        ob.setName(cursor.getString(2));
        ob.setOtp(cursor.getInt(3));
        ob.setEmailID(cursor.getString(4));
        ob.setRole(cursor.getInt(5));
        ob.setProfilePictureUrl(cursor.getString(6));
    }

    //userData data
    public Data getUserData() {

        String query = "Select * FROM userData";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();

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
    public Data getUserDataByLoginId(int id) {

        String query = "Select * FROM userData WHERE LoginId = " + id + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();

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
    public boolean updateUserData(Data ob) {
        ContentValues values = new ContentValues();

        values.put("LoginId", ob.getLoginID());
        values.put("Phonenumber", ob.getPhoneNumber());
        values.put("Name", ob.getName());
        values.put("otp", ob.getOtp());
        values.put("EmailId", ob.getEmailID());
        values.put("Role", ob.getRole());
        values.put("ProfilePictureUrl", ob.getProfilePictureUrl());

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("userData", values, "loginId = '" + ob.getLoginID() + "' ", null);

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

    //--------------------------GetAllAddress---------------
    public boolean upsertGetAllAddress(Addresses ob) {
        boolean done = false;
        Addresses data = null;
        if (ob.getAddressId() != 0) {
            data = getAllAddressesData(ob.getAddressId());
            if (data == null) {
                done = insertGetAddressData(ob);
            } else {
                done = updateGetAddressData(ob);
            }
        }
        return done;
    }

    //GetAll Address
    private void populateGetAddressData(Cursor cursor, Addresses ob) {
        ob.setAddressId(cursor.getInt(0));
        ob.setCompleteAddress(cursor.getString(1));
        ob.setCityId(cursor.getInt(2));
        ob.setZipCode(cursor.getString(3));
        ob.setLoginID(cursor.getInt(4));
        ob.setPhoneNumber(cursor.getString(5));
        ob.setLandMark(cursor.getString(6));
        ob.setLocalityId(cursor.getInt(7));
        ob.setLocalityName(cursor.getString(8));

    }

    //show  Addresses list data
    public List<Addresses> GetAllAddressesData() {
        ArrayList list = new ArrayList<>();
        String query = "Select * FROM GetAllAddressEntity";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Addresses ob = new Addresses();
                populateGetAddressData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //AddressId,CompleteAddress,CityId,ZipCode,LoginID,PhoneNumber,LandMark GetAllAddressEntity
    //insert GetAllAddress data
    public boolean insertGetAddressData(Addresses ob) {
        ContentValues values = new ContentValues();

        values.put("AddressId", ob.getAddressId());
        values.put("CompleteAddress", ob.getCompleteAddress());
        values.put("CityId", ob.getCityId());
        values.put("ZipCode", ob.getZipCode());
        values.put("LoginID", ob.getLoginID());
        values.put("PhoneNumber", ob.getPhoneNumber());
        values.put("LandMark", ob.getLandMark());
        values.put("LocalityId", ob.getLocalityId());
        values.put("LocalityName", ob.getLocalityName());

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("GetAllAddressEntity", null, values);
        db.close();
        return i > 0;
    }


    //GetAllAddress data by id
    public Addresses getAllAddressesData(int addressId) {

        String query = "Select * FROM GetAllAddressEntity WHERE AddressId= " + addressId + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Addresses data = new Addresses();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateGetAddressData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }


    //update GetAddAddress data
    public boolean updateGetAddressData(Addresses ob) {
        ContentValues values = new ContentValues();

        values.put("AddressId", ob.getAddressId());
        values.put("CompleteAddress", ob.getCompleteAddress());
        values.put("CityId", ob.getCityId());
        values.put("ZipCode", ob.getZipCode());
        values.put("LoginID", ob.getLoginID());
        values.put("PhoneNumber", ob.getPhoneNumber());
        values.put("LandMark", ob.getLandMark());
        values.put("LocalityId", ob.getLocalityId());
        values.put("LocalityName", ob.getLocalityName());

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("GetAllAddressEntity", values, "AddressId = " + ob.getAddressId() + " ", null);

        db.close();
        return i > 0;
    }

    // delete Address Data from addressId
    public boolean deleteAddressData(int addressId) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "addressId = '" + addressId + "' ";
        db.delete("GetAllAddressEntity", query, null);
        db.close();
        return result;
    }

    // delete all Address Data
    public boolean deleteAllAddressData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("GetAllAddressEntity", null, null);
        db.close();
        return result;
    }


    //----------------product data-----------
    public boolean upsertAllProduct(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getProductId() != 0) {
            data = getProductData(ob.getProductId());
            if (data == null) {
                done = insertProductData(ob);
            } else {
                done = updateProductData(ob);
            }
        }
        return done;
    }

    //GetAll Product
    private void populateProductData(Cursor cursor, Data ob) {
        ob.setProductId(cursor.getInt(0));
        ob.setProductName(cursor.getString(1));
        ob.setCategoryId(cursor.getInt(2));
        ob.setUnitPrice(cursor.getFloat(3));
        ob.setDiscount(cursor.getFloat(4));
        ob.setGST(cursor.getFloat(5));
        ob.setTaxType(cursor.getString(6));
        ob.setUOM(cursor.getString(7));
        ob.setProductDetails(cursor.getString(8));
        ob.setProductPicturesUrl(cursor.getString(9));
    }

    //ProductId,ProductName,CategoryId,UnitPrice,Discount,GST,TaxType ProductListDataEntity
    //show Product list data
    public List<Data> GetAllProductBasedOnCategoryIdData(int categoryId) {
        ArrayList<Data> list = new ArrayList<Data>();
        String query = "Select * FROM ProductListDataEntity WHERE CategoryId= " + categoryId + "";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateProductData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert Product data
    public boolean insertProductData(Data ob) {
        ContentValues values = new ContentValues();
        populateProductValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("ProductListDataEntity", null, values);
        db.close();
        return i > 0;
    }

    //Product data by id
    public Data getProductData(int productId) {

        String query = "Select * FROM ProductListDataEntity WHERE ProductId= " + productId + "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateProductData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    private void populateProductValue(Data ob, ContentValues values) {
        values.put("ProductId", ob.getProductId());
        values.put("ProductName", ob.getProductName());
        values.put("CategoryId", ob.getCategoryId());
        values.put("UnitPrice", ob.getUnitPrice());
        values.put("Discount", ob.getDiscount());
        values.put("GST", ob.getGST());
        values.put("TaxType", ob.getTaxType());
        values.put("UOM", ob.getUOM());
        values.put("ProductDetails", ob.getProductDetails());
        values.put("ImageUrl", ob.getProductPicturesUrl());
    }

    //update Product data
    public boolean updateProductData(Data ob) {
        ContentValues values = new ContentValues();
        populateProductValue(ob, values);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("ProductListDataEntity", values, "ProductId = " + ob.getProductId() + "", null);

        db.close();
        return i > 0;
    }

    public boolean deleteAllProductData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ProductListDataEntity", null, null);
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
        ob.setStoreId(cursor.getInt(2));
        ob.setQuantity(cursor.getFloat(3));
        ob.setPrice(cursor.getFloat(4));
        ob.setOrderTime(cursor.getString(5));
        ob.setCategoryId(cursor.getInt(6));
        ob.setDiscount(cursor.getFloat(7));
        ob.setUOM(cursor.getString(8));
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
    public List<MyBasket> GetAllBasketOrderDataBasedOnStoreId(int storeId) {
        ArrayList<MyBasket> list = new ArrayList<MyBasket>();
        String query = "Select * FROM MyOrderDataEntity WHERE StoreId= " + storeId + "";

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
        values.put("StoreId", ob.getStoreId());
        values.put("Quantity", ob.getQuantity());
        values.put("Price", ob.getPrice());
        values.put("OrderTime", ob.getOrderTime());
        values.put("CategoryId", ob.getCategoryId());
        values.put("discount", ob.getDiscount());
        values.put("UOM", ob.getUOM());

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

    // delete Basket Order Data By Store Id ...........
    public boolean deleteBasketOrderDataByStoreId(int storeId) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "StoreId = " + storeId + " ";
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
