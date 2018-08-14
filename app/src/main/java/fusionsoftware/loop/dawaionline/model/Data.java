package fusionsoftware.loop.dawaionline.model;


public class Data {
    private String Name;

    private int LoginID;

    private int Otp;

    private int Role;

    private String PhoneNumber;

    private String EmailID;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getLoginID() {
        return LoginID;
    }

    public void setLoginID(int LoginID) {
        this.LoginID = LoginID;
    }

    public int getOtp() {
        return Otp;
    }

    public void setOtp(int Otp) {
        this.Otp = Otp;
    }

    public int getRole() {
        return Role;
    }

    public void setRole(int Role) {
        this.Role = Role;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String EmailID) {
        this.EmailID = EmailID;
    }

    private int CityId;

    private String CityName;

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    private int LocalityId;
    private String LocalityName;


    public int getLocalityId() {
        return LocalityId;
    }

    public void setLocalityId(int localityId) {
        LocalityId = localityId;
    }

    public String getLocalityName() {
        return LocalityName;
    }

    public void setLocalityName(String localityName) {
        LocalityName = localityName;
    }

    private int StoreId;
    private String StoreName;
    private String StorePhoneNumber;
    private String StoreEmailId;
    private String StoreAddress;
    private String StoreStatus;

    private String OpeningTime;

    private String ClosingTime;


    public int getStoreId() {
        return StoreId;
    }

    public void setStoreId(int storeId) {
        StoreId = storeId;
    }

//    public String getStoreName() {
//        return StoreName;
//    }
//
//    public void setStoreName(String storeName) {
//        StoreName = storeName;
//    }


    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String StoreName) {
        this.StoreName = StoreName;
    }


    public String getStorePhoneNumber() {
        return StorePhoneNumber;
    }

    public void setStorePhoneNumber(String storePhoneNumber) {
        StorePhoneNumber = storePhoneNumber;
    }

    public String getStoreEmailId() {
        return StoreEmailId;
    }

    public void setStoreEmailId(String storeEmailId) {
        StoreEmailId = storeEmailId;
    }

    public String getStoreAddress() {
        return StoreAddress;
    }

    public void setStoreAddress(String storeAddress) {
        StoreAddress = storeAddress;
    }

    public String getStoreStatus() {
        return StoreStatus;
    }

    public void setStoreStatus(String storeStatus) {
        StoreStatus = storeStatus;
    }

    public String getOpeningTime() {
        return OpeningTime;
    }

    public void setOpeningTime(String openingTime) {
        OpeningTime = openingTime;
    }

    public String getClosingTime() {
        return ClosingTime;
    }

    public void setClosingTime(String closingTime) {
        ClosingTime = closingTime;
    }

    private int CategoryId;
    private String CategoryName;

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    private int ProductId;
    private String ProductName;
    private float UnitPrice;
    private float GST;
    private float Discount;
    private float PromoDiscount;
    private String TaxType;
    private String UOM;

    private float countValue = 1;

    public float getCountValue() {
        return countValue;
    }

    public void setCountValue(float countValue) {
        this.countValue = countValue;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public float getPromoDiscount() {
        return PromoDiscount;
    }

    public void setPromoDiscount(float promoDiscount) {
        PromoDiscount = promoDiscount;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public float getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        UnitPrice = unitPrice;
    }

    public float getGST() {
        return GST;
    }

    public void setGST(float GST) {
        this.GST = GST;
    }

    public float getDiscount() {
        return Discount;
    }

    public void setDiscount(float discount) {
        Discount = discount;
    }

    public String getTaxType() {
        return TaxType;
    }

    public void setTaxType(String taxType) {
        TaxType = taxType;
    }

    private String ProductDetails;

    public String getProductDetails() {
        return ProductDetails;
    }

    public void setProductDetails(String productDetails) {
        ProductDetails = productDetails;
    }

    private String TransactionId;

    private String PaymentMode;

    private String PaymentOrderId;

    private String CreatedOn;

    private float TotalAmount;

    private String Id;

    private String PaymentId;

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String TransactionId) {
        this.TransactionId = TransactionId;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String PaymentMode) {
        this.PaymentMode = PaymentMode;
    }

    public String getPaymentOrderId() {
        return PaymentOrderId;
    }

    public void setPaymentOrderId(String PaymentOrderId) {
        this.PaymentOrderId = PaymentOrderId;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String CreatedOn) {
        this.CreatedOn = CreatedOn;
    }

    public float getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(float TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getPaymentId() {
        return PaymentId;
    }

    public void setPaymentId(String PaymentId) {
        this.PaymentId = PaymentId;
    }

    private Boolean isSelectGm;
    private Boolean isSelectKg;
    private Boolean isSelectManualy;

    public Boolean getSelectGm() {
        return isSelectGm;
    }

    public void setSelectGm(Boolean selectGm) {
        isSelectGm = selectGm;
    }

    public Boolean getSelectKg() {
        return isSelectKg;
    }

    public void setSelectKg(Boolean selectKg) {
        isSelectKg = selectKg;
    }

    public Boolean getSelectManualy() {
        return isSelectManualy;
    }

    public void setSelectManualy(Boolean selectManualy) {
        isSelectManualy = selectManualy;
    }

    private int PromoCodeId;
    private String PromoCode;

    public int getPromoCodeId() {
        return PromoCodeId;
    }

    public void setPromoCodeId(int promoCodeId) {
        PromoCodeId = promoCodeId;
    }

    public String getPromoCode() {
        return PromoCode;
    }

    public void setPromoCode(String promoCode) {
        PromoCode = promoCode;
    }

    //    get favourite store by user .........................
    private String StorePicturesUrl;

    public String getStorePicturesUrl() {
        return StorePicturesUrl;
    }

    public void setStorePicturesUrl(String StorePicturesUrl) {
        this.StorePicturesUrl = StorePicturesUrl;
    }
// get my order history data..............................

    private String OrderTime;

    private String OrderStatus;

    private float Quantiy;

    private String OrderNumber;

    private float TotalPrice;

    private float GrandTotal;

    private float SpecialDiscount;

    private float TotalGST;

    public float getTotalGST() {
        return TotalGST;
    }

    public void setTotalGST(float totalGST) {
        TotalGST = totalGST;
    }

    private float SubTotal;

    public float getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(float subTotal) {
        SubTotal = subTotal;
    }


    public float getSpecialDiscount() {
        return SpecialDiscount;
    }

    public void setSpecialDiscount(float specialDiscount) {
        SpecialDiscount = specialDiscount;
    }

    private int OrderId;

    public float getGrandTotal() {
        return GrandTotal;
    }

    public void setGrandTotal(float grandTotal) {
        GrandTotal = grandTotal;
    }

    public int getLoginId() {
        return LoginId;
    }

    public void setLoginId(int loginId) {
        LoginId = loginId;
    }

    private int LoginId;

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String OrderTime) {
        this.OrderTime = OrderTime;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    private float Quantity;

    public float getQuantity() {
        return Quantity;
    }

    public void setQuantity(float Quantity) {
        this.Quantity = Quantity;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String OrderNumber) {
        this.OrderNumber = OrderNumber;
    }

    public float getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(float TotalPrice) {
        this.TotalPrice = TotalPrice;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int OrderId) {
        this.OrderId = OrderId;
    }

    private String CategoryPictures;
    private String CategoryDescription;
    private String ProductPicturesUrl;

    public String getCategoryPictures() {
        return CategoryPictures;
    }

    public void setCategoryPictures(String categoryPictures) {
        CategoryPictures = categoryPictures;
    }

    public String getCategoryDescription() {
        return CategoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        CategoryDescription = categoryDescription;
    }

    public String getProductPicturesUrl() {
        return ProductPicturesUrl;
    }

    public void setProductPicturesUrl(String productPicturesUrl) {
        ProductPicturesUrl = productPicturesUrl;
    }

    private String ProfilePictureUrl;

    public String getProfilePictureUrl() {
        return ProfilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        ProfilePictureUrl = profilePictureUrl;
    }

    private String PaymentURL;

    public String getPaymentURL() {
        return PaymentURL;
    }

    public void setPaymentURL(String PaymentURL) {
        this.PaymentURL = PaymentURL;
    }

    private boolean FavouriteStore;

    public boolean isFavouriteStore() {
        return FavouriteStore;
    }

    public void setFavouriteStore(boolean favouriteStore) {
        FavouriteStore = favouriteStore;
    }

    private OrderDetails[] OrderDetails;

    public OrderDetails[] getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(OrderDetails[] orderDetails) {
        OrderDetails = orderDetails;
    }

    private float shippingCharge;

    public float getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(float shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    private float NetPrice;


    public float getNetPrice() {
        return NetPrice;
    }

    public void setNetPrice(float netPrice) {
        NetPrice = netPrice;
    }


    private String MenuName;

    private String ImageUrl;

    private int MenuId;

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String MenuName) {
        this.MenuName = MenuName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public int getMenuId() {
        return MenuId;
    }

    public void setMenuId(int MenuId) {
        this.MenuId = MenuId;
    }


    @Override
    public String toString() {
        return "ClassPojo [Name = " + Name + ", LoginID = " + LoginID + ", Otp = " + Otp + ", Role = " + Role + ", PhoneNumber = " + PhoneNumber + ", EmailID = " + EmailID + ",LocalityId = " + LocalityId + ", StorePhoneNumber = " + StorePhoneNumber + ", StoreId = " + StoreId + ", StoreName = " + StoreName + ", StorePicturesUrl = " + StorePicturesUrl + ",StoreAddress = " + StoreAddress + ", StoreEmailId = " + StoreEmailId + ",OrderTime = " + OrderTime + ", OrderStatus = " + OrderStatus + ", Quantity = " + Quantity + ", StoreId = " + StoreId + ", LoginId = " + LoginId + ",OrderNumber = " + OrderNumber + ", TotalPrice = " + TotalPrice + ",  Discount = " + Discount + ",OrderId = " + OrderId + ",ProductId = " + ProductId + "]";
    }
}