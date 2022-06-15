package in.co.kissanvendor.extras;

public class ServerLinks {

    public static final String MAIN_URL = "https://kisaanandfactory.com/api/v1/";
    public static final String SignUp_url = MAIN_URL+"vendorapp/auth/register";
    public static final String SignIn_url = MAIN_URL+"vendorapp/auth/login";
    public static final String getProfileData_url = MAIN_URL+"vendorapp/view/profile";
    public static final String getCategories_url = MAIN_URL+"vendorapp/view-service-category";
    public static final String getProductTypes_url = MAIN_URL+"adminapp/product/sub-category/all?CategoryId=";
    public static final String addProduct_url = MAIN_URL+"vendorapp/vendor/product/add";
    public static final String productImageUpload_url = MAIN_URL+"vendorapp/vendor/product/image/upload";
    public static final String getProduct_url = MAIN_URL+"vendorapp/vendor/product/all";
    public static final String getHomeData_url = MAIN_URL+"vendorapp/home";
    public static final String forgotPassword_url = MAIN_URL+"vendorapp/auth/forgot-password";
    public static final String resetPassword_url = MAIN_URL+"vendorapp/auth/reset-password";
    public static final String updateProfile_url = MAIN_URL+"vendorapp/view/edit";
    public static final String orderHistory_url = MAIN_URL+"vendorapp/view/myOrders/all";
    public static final String productDelete_url = MAIN_URL+"vendorapp/vendor/product/delete/";
    public static final String verify_otp = "https://kisaanandfactory.com/api/v1/vendorapp/auth/register/verify-otp";
    public static final String editproduct = "https://kisaanandfactory.com/api/v1/vendorapp/vendor/product/edit/";
    public static final String statusupdate = "https://kisaanandfactory.com/api/v1/vendorapp/view/myOrders/status/update/";
    public static final String sociallogin = "https://kisaanandfactory.com/api/v1/vendorapp/auth/register/social-login";
//    public static final String orderHistory_url = MAIN_URL+"vendorapp/view/payment-history";
}
