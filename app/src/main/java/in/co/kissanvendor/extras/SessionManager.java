package in.co.kissanvendor.extras;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import in.co.kissanvendor.activities.LandingActivity;


/**
 * Created by Narendra on 6/8/2017.
 */

public class SessionManager {

    SharedPreferences sharedprefernce;
    SharedPreferences.Editor editor;
    SharedPreferences sharedprefernceCoupon;
    SharedPreferences.Editor editorCoupon;
    Context context;
    int PRIVATE_MODE=0;

    private static final String PREF_NAME="sharedcheckLogin";
    private static final String PREF_NAME2="sharedcheckLogin2";
    private static final String User_Id="userid";
    private static final String UserName ="uname";
    private static final String Email="email";
    private static final String Phone="phone";
    private static final String ProfilePic="img";
    private static final String LogKeyExp="lkexp";
    private static final String Token="token";
    private static final String DisplayName ="dname";
    private static final String RestaurantAddress="restaurantaddress";
    private static final String RestaurantLatitude="restaurantlatitude";
    private static final String RestaurantLongitude="restaurantlongitude";
    private static final String GSTIN="gstin";
    private static final String FoodLisenceNumber="foodlisencenumber";
    private static final String PriceForTwo="pricefortwo";
    private static final String PreparationTime="preparationtime";
    private static final String OwnerName="ownername";
    private static final String RestaurantStatus ="RestaurantStatus";
    private static final String DeliveryboyOnlineStatus ="DeliveryboyOnlineStatus";
    private static final String PanCardNo="pancardno";
    private static final String EmailVerifyStatus="emailverifystatus";
    private static final String PanCardPhoto="pancardphoto";
    private static final String Otp="otp";
    private static final String OtpKey="otpkey";
    private static final String IS_LOGIN="islogin";
    private static final String FirstName="FirstName";
    private static final String locality="locality";
    private static final String Address1="Address1";
    private static final String Street="Street";
    private static final String City="City";
    private static final String State="State";
    private static final String PostCode="PostCode";
    private static final String country="country";
    private static final String PinCode="PinCode";
    private static final String shiipingEmail="shiipingEmail";
    private static final String CartBillno="CartBillno";
    private static final String CartRestaurantName="CartRestaurantName";
    private static final String CartRestaurantAddress="CartRestaurantAddress";
    private static final String CartRestaurantCusin="CartRestaurantCusin";
    private static final String CartRestaurantImage="CartRestaurantImage";
    private static final String isCartEmpty="isCartEmpty";


    private static final String IsExclusive="isexclusive";
    private static final String Cuisins="cuisins";
    private static final String CouponCode="CouponCode";
    private static final String CouponPrice="CouponPrice";
    private static final String FCMId="FCMId";

    private static final String PickPin="PickPin";
    private static final String PickCity="PickCity";
    private static final String DropPin="DropPin";
    private static final String DropCity="DropCity";

    private static final String DefaultAddressId="DefaultAddressId";
    private static final String DefaultAddress="DefaultAddress";
    private static final String DefaultCompleteAddress="DefaultCompleteAddress";
    private static final String DefaultAddressLandmark="DefaultAddressLandmark";
    private static final String DefaultLat="DefaultLat";
    private static final String DefaultLon="DefaultLon";
    private static final String AddressType="AddressType";
    private static final String PAddressState="PAddressState";
    private static final String PAddressStateId="PAddressStateId";
    private static final String PAddressCity="PAddressCity";
    private static final String PAddressCityId="PAddressCityId";
    private static final String PAddressPhone="PAddressPhone";

    private static final String DAddressName="DAddressName";
    private static final String DAddressHouse="DAddressHouse";
    private static final String DAddressLandmark="DAddressLandmark";
    private static final String DAddressState="DAddressState";
    private static final String DAddressStateId="DAddressStateId";
    private static final String DAddressCity="DAddressCity";
    private static final String DAddressCityId="DAddressCityId";
    private static final String DAddressPhone="DAddressPhone";

    private static final String DocType="DocType";
    private static final String DocDescription="DocDescription";
    private static final String DocWeight="DocWeight";
    private static final String DocDimension="DocDimension";
    private static final String PickDate="PickDate";
    private static final String PickTime="PickTime";



    public SessionManager(Context context){

        this.context =  context;
        sharedprefernce = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor=sharedprefernce.edit();

        sharedprefernceCoupon=context.getSharedPreferences(PREF_NAME2,PRIVATE_MODE);
        editorCoupon=sharedprefernceCoupon.edit();

    }

    public Boolean isLogin(){
        return sharedprefernce.getBoolean(IS_LOGIN, false);

    }
    public void setLogin(){

        editor.putBoolean(IS_LOGIN, true);
        editor.commit();

    }


    public void setFCMId(String fcmid){

        editorCoupon.putString(FCMId,fcmid);
        editorCoupon.commit();
    }

    public String getFCMId(){

        return sharedprefernceCoupon.getString(FCMId,"");
    }

    public void setUserID(String id ){
     editor.putString(User_Id,id);
     editor.commit();
    }
    public String getUserID(){
        return  sharedprefernce.getString(User_Id,"DEFAULT");
    }





    public void setUserName(String uname){

        editor.putString(UserName,uname);
        editor.commit();
    }

    public String getUserName(){

        return sharedprefernce.getString(UserName,"Default");
    }

    public void setImgUrl(String img){

        editor.putString(ProfilePic,img);
        editor.commit();
    }

    public String getImgUrl(){

        return sharedprefernce.getString(ProfilePic,"Default");
    }




    public void setEmail(String email){

        editor.putString(Email,email);
        editor.commit();
    }

    public String getEmail(){

        return sharedprefernce.getString(Email,"");
    }


    public void setLogKeyExp(String lkexp){

        editor.putString(LogKeyExp,lkexp);
        editor.commit();
    }

    public String getLogKeyExp(){

        return sharedprefernce.getString(LogKeyExp,"Default");
    }


    public void setToken(String lkey){

        editor.putString(Token,lkey);
        editor.commit();
    }

    public String getToken(){

        return sharedprefernce.getString(Token,"");
    }


    public void setDisplayName(String dname){

        editor.putString(DisplayName,dname);
        editor.commit();
    }

    public String getDisplayName(){

        return sharedprefernce.getString(DisplayName,"Default");
    }
    public void setRestaurantStatus(String stat){

        editor.putString(RestaurantStatus,stat);
        editor.commit();
    }

    public String getRestaurantStatus(){

        return sharedprefernce.getString(RestaurantStatus,"Default");
    }
    public void setDeliveryboyOnlineStatus(String stat){

        editor.putString(DeliveryboyOnlineStatus,stat);
        editor.commit();
    }

    public String getDeliveryboyOnlineStatus(){

        return sharedprefernce.getString(DeliveryboyOnlineStatus,"false");
    }


    public void setPanCardNo(String panno){

        editor.putString(PanCardNo,panno);
        editor.commit();
    }


    public String getPanCardNo(){

        return sharedprefernce.getString(PanCardNo,"");
    }


    public void setEmailVerifyStatus(String emailVerifyStatus){

        editor.putString(EmailVerifyStatus,emailVerifyStatus);
        editor.commit();
    }


    public String getEmailVerifyStatus(){

        return sharedprefernce.getString(EmailVerifyStatus,"");
    }

    public void setPanCardPhoto(String panpthoto){

        editor.putString(PanCardPhoto,panpthoto);
        editor.commit();
    }


    public String getPanCardPhoto(){

        return sharedprefernce.getString(PanCardPhoto,"");
    }


    public void setOtp(String oTp){

        editor.putString(Otp,oTp);
        editor.commit();
    }
    public String getOtp(){

        return sharedprefernce.getString(Otp,"");
    }



    public void setOtpKey(String otpKey){

        editor.putString(OtpKey,otpKey);
        editor.commit();
    }
    public String getOtpKey(){

        return sharedprefernce.getString(OtpKey,"");
    }

    public void setFirstName(String name){
        editor.putString(FirstName,name);
        editor.commit();

    }
    public String getFirstName(){
        return  sharedprefernce.getString(FirstName,"");
    }

    public void setlocality(String name){
        editor.putString(locality,name);
        editor.commit();

    }
    public String getlocality(){
        return  sharedprefernce.getString(locality,"");
    }

    public void setAddress1(String name){
        editor.putString(Address1,name);
        editor.commit();

    }
    public String getAddress1(){
        return  sharedprefernce.getString(Address1,"");
    }

    public String getStreet(){
        return  sharedprefernce.getString(Street,"");
    }

    public void setStreet(String name){
        editor.putString(Street,name);
        editor.commit();

    }


    public void setPinCode(String name){
        editor.putString(PinCode,name);
        editor.commit();

    }
    public String getPinCode(){
        return  sharedprefernce.getString(PinCode,"");
    }

    public void setCity(String name){
        editor.putString(City,name);
        editor.commit();

    }
    public String getCity(){
        return  sharedprefernce.getString(City,"");
    }

    public void setState(String name){
        editor.putString(State,name);
        editor.commit();

    }
    public String getState(){
        return  sharedprefernce.getString(State,"");
    }

    public void setPostCode(String name){
        editor.putString(PostCode,name);
        editor.commit();

    }
    public String getPostCode(){
        return  sharedprefernce.getString(PostCode,"");
    }

    public void setcountry(String name){
        editor.putString(country,name);
        editor.commit();

    }
    public String getcountry(){
        return  sharedprefernce.getString(country,"INDIA");
    }

    public void setCartRestaurantName(String name){
        editor.putString(CartRestaurantName,name);
        editor.commit();

    }
    public String getCartRestaurantName(){
        return  sharedprefernce.getString(CartRestaurantName,"");
    }

    public void setCartRestaurantAddress(String name){
        editor.putString(CartRestaurantAddress,name);
        editor.commit();

    }
    public String getCartRestaurantAddress(){
        return  sharedprefernce.getString(CartRestaurantAddress,"");
    }

    public void setCartRestaurantCusine(String name){
        editor.putString(CartRestaurantCusin,name);
        editor.commit();

    }
    public String getCartRestaurantCusin(){
        return  sharedprefernce.getString(CartRestaurantCusin,"");
    }

    public void setCartRestaurantImage(String name){
        editor.putString(CartRestaurantImage,name);
        editor.commit();

    }
    public String getCartRestaurantImage(){
        return  sharedprefernce.getString(CartRestaurantImage,"");
    }

    public void setCartBillno(String name){
        editor.putString(CartBillno,name);
        editor.commit();

    }
    public String getCartBillno(){
        return  sharedprefernce.getString(CartBillno,"");
    }

    public void setisCartEmpty(String name){
        editor.putString(isCartEmpty,name);
        editor.commit();

    }
    public String getisCartEmpty(){
        return  sharedprefernce.getString(isCartEmpty,"yes");
    }

    public void setshiipingEmaile(String name){
        editor.putString(shiipingEmail,name);
        editor.commit();

    }
    public String getshiipingEmail(){
        return  sharedprefernce.getString(shiipingEmail,"Email");
    }

    public void setPhone(String name){
        editor.putString(Phone,name);
        editor.commit();

    }
    public String getPhone(){
        return  sharedprefernce.getString(Phone,"Phone");
    }


    public void setRestaurantAddress(String resname){
        editor.putString(RestaurantAddress,resname);
        editor.commit();

    }
    public String getRestaurantAddress(){
        return  sharedprefernce.getString(RestaurantAddress,"");
    }

    public void setRestaurantLatitude(String reslat){
        editor.putString(RestaurantLatitude,reslat);
        editor.commit();

    }
    public String getRestaurantLatitude(){
        return  sharedprefernce.getString(RestaurantLatitude,"0.00");
    }

    public void setRestaurantLongitude(String reslong){
        editor.putString(RestaurantLongitude,reslong);
        editor.commit();

    }
    public String getRestaurantLongitude(){
        return  sharedprefernce.getString(RestaurantLongitude,"0.00");
    }

    public void setGSTIN(String gst){
        editor.putString(GSTIN,gst);
        editor.commit();

    }
    public String getGSTIN(){
        return  sharedprefernce.getString(GSTIN,"");
    }

    public void setFoodLisenceNumber(String lisencenumber){
        editor.putString(FoodLisenceNumber,lisencenumber);
        editor.commit();

    }
    public String getFoodLisenceNumber(){
        return  sharedprefernce.getString(FoodLisenceNumber,"");
    }

    public void setPriceForTwo(String pricefortwo){
        editor.putString(PriceForTwo,pricefortwo);
        editor.commit();

    }
    public String getPriceForTwo(){
        return  sharedprefernce.getString(PriceForTwo,"0.00");
    }

    public void setPreparationTime(String preptime){
        editor.putString(PreparationTime,preptime);
        editor.commit();

    }
    public String getPreparationTime(){
        return  sharedprefernce.getString(PreparationTime,"0");
    }

    public void setOwnerName(String ownername){
        editor.putString(OwnerName,ownername);
        editor.commit();

    }
    public String getOwnerName(){
        return  sharedprefernce.getString(OwnerName,"INDIA");
    }

    public void setIsExclusivee(String exclusive){
        editor.putString(IsExclusive,exclusive);
        editor.commit();

    }
    public String getIsExclusive(){
        return  sharedprefernce.getString(IsExclusive,"");
    }

    public void setCuisins(String cusin){
        editor.putString(Cuisins,cusin);
        editor.commit();

    }
    public String getCuisins(){
        return  sharedprefernce.getString(Cuisins,"");
    }



    public void setCouponCode(String name){
        editor.putString(CouponCode,name);
        editor.commit();

    }

    public String getCouponCode(){
        return  sharedprefernce.getString(CouponCode,"Phone");
    }



    public void setCouponPrice(String name){
        editor.putString(CouponPrice,name);
        editor.commit();

    }

    public String getCouponPrice(){
        return  sharedprefernce.getString(CouponPrice,"Phone");
    }

    public void clearCoupon(){

        editorCoupon.clear();
        editorCoupon.commit();

    }

    public void setPickPin(String id ){
        editor.putString(PickPin,id);
        editor.commit();
    }
    public String getPickPin(){
        return  sharedprefernce.getString(PickPin,"DEFAULT");
    }

    public void setPickCity(String id ){
        editor.putString(PickCity,id);
        editor.commit();
    }
    public String getPickCity(){
        return  sharedprefernce.getString(PickCity,"DEFAULT");
    }
    public void setDropPin(String id ){
        editor.putString(DropPin,id);
        editor.commit();
    }
    public String getDropPin(){
        return  sharedprefernce.getString(DropPin,"DEFAULT");
    }
    public void setDropCity(String id ){
        editor.putString(DropCity,id);
        editor.commit();
    }
    public String getDropCity(){
        return  sharedprefernce.getString(DropCity,"DEFAULT");
    }


    public void setDefaultAddressId(String id ){
        editor.putString(DefaultAddressId,id);
        editor.commit();
    }
    public String getDefaultAddressId(){
        return  sharedprefernce.getString(DefaultAddressId,"");
    }

    public void setDefaultAddress(String id ){
        editor.putString(DefaultAddress,id);
        editor.commit();
    }
    public String getDefaultAddress(){
        return  sharedprefernce.getString(DefaultAddress,"");
    }

    public void setDefaultCompleteAddress(String id ){
        editor.putString(DefaultCompleteAddress,id);
        editor.commit();
    }
    public String getDefaultCompleteAddress(){
        return  sharedprefernce.getString(DefaultCompleteAddress,"");
    }
    public void setDefaultAddressLandmark(String land ){
        editor.putString(DefaultAddressLandmark,land);
        editor.commit();
    }
    public String getDefaultAddressLandmark(){
        return  sharedprefernce.getString(DefaultAddressLandmark,"");
    }
    public void setPAddressState(String id ){
        editor.putString(PAddressState,id);
        editor.commit();
    }
    public String getPAddressState(){
        return  sharedprefernce.getString(PAddressState,"DEFAULT");
    }
    public void setPAddressStateId(String id ){
        editor.putString(PAddressStateId,id);
        editor.commit();
    }
    public String getPAddressStateId(){
        return  sharedprefernce.getString(PAddressStateId,"DEFAULT");
    }
    public void setPAddressCity(String id ){
        editor.putString(PAddressCity,id);
        editor.commit();
    }
    public String getPAddressCity(){
        return  sharedprefernce.getString(PAddressCity,"DEFAULT");
    }
    public void setPAddressCityId(String id ){
        editor.putString(PAddressCityId,id);
        editor.commit();
    }
    public String getPAddressCityId(){
        return  sharedprefernce.getString(PAddressCityId,"DEFAULT");
    }
    public void setPAddressPhone(String id ){
        editor.putString(PAddressPhone,id);
        editor.commit();
    }
    public String getPAddressPhone(){
        return  sharedprefernce.getString(PAddressPhone,"DEFAULT");
    }

    public void setDefaultLat(String lat ){
        editor.putString(DefaultLat,lat);
        editor.commit();
    }
    public String getDefaultLat(){
        return  sharedprefernce.getString(DefaultLat,"0.00");
    }

    public void setDefaultLon(String lon ){
        editor.putString(DefaultLon,lon);
        editor.commit();
    }
    public String getDefaultLon(){
        return  sharedprefernce.getString(DefaultLon,"0.00");
    }

    public void setAddressType(String ty ){
        editor.putString(AddressType,ty);
        editor.commit();
    }
    public String getAddressType(){
        return  sharedprefernce.getString(AddressType,"");
    }


    public void setDAddressName(String id ){
        editor.putString(DAddressName,id);
        editor.commit();
    }
    public String getDAddressName(){
        return  sharedprefernce.getString(DAddressName,"DEFAULT");
    }
    public void setDAddressHouse(String id ){
        editor.putString(DAddressHouse,id);
        editor.commit();
    }
    public String getDAddressHouse(){
        return  sharedprefernce.getString(DAddressHouse,"DEFAULT");
    }
    public void setDAddressLandmark(String id ){
        editor.putString(DAddressLandmark,id);
        editor.commit();
    }
    public String getDAddressLandmark(){
        return  sharedprefernce.getString(DAddressLandmark,"DEFAULT");
    }
    public void setDAddressState(String id ){
        editor.putString(DAddressState,id);
        editor.commit();
    }
    public String getDAddressState(){
        return  sharedprefernce.getString(DAddressState,"DEFAULT");
    }
    public void setDAddressStateId(String id ){
        editor.putString(DAddressStateId,id);
        editor.commit();
    }
    public String getDAddressStateId(){
        return  sharedprefernce.getString(DAddressStateId,"DEFAULT");
    }
    public void setDAddressCity(String id ){
        editor.putString(DAddressCity,id);
        editor.commit();
    }
    public String getDAddressCity(){
        return  sharedprefernce.getString(DAddressCity,"DEFAULT");
    }
    public void setDAddressCityId(String id ){
        editor.putString(DAddressCityId,id);
        editor.commit();
    }
    public String getDAddressCityId(){
        return  sharedprefernce.getString(DAddressCityId,"DEFAULT");
    }
    public void setDAddressPhone(String id ){
        editor.putString(DAddressPhone,id);
        editor.commit();
    }
    public String getDAddressPhone(){
        return  sharedprefernce.getString(DAddressPhone,"DEFAULT");
    }


    public void setDocType(String id ){
        editor.putString(DocType,id);
        editor.commit();
    }
    public String getDocType(){
        return  sharedprefernce.getString(DocType,"DEFAULT");
    }
    public void setDocDescription(String id ){
        editor.putString(DocDescription,id);
        editor.commit();
    }
    public String getDocDescription(){
        return  sharedprefernce.getString(DocDescription,"DEFAULT");
    }
    public void setDocWeight(String id ){
        editor.putString(DocWeight,id);
        editor.commit();
    }
    public String getDocWeight(){
        return  sharedprefernce.getString(DocWeight,"DEFAULT");
    }
    public void setDocDimension(String id ){
        editor.putString(DocDimension,id);
        editor.commit();
    }
    public String getDocDimension(){
        return  sharedprefernce.getString(DocDimension,"DEFAULT");
    }
    public void setPickDate(String id ){
        editor.putString(PickDate,id);
        editor.commit();
    }
    public String getPickDate(){
        return  sharedprefernce.getString(PickDate,"DEFAULT");
    }
    public void setPickTime(String id ){
        editor.putString(PickTime,id);
        editor.commit();
    }
    public String getPickTime(){
        return  sharedprefernce.getString(PickTime,"DEFAULT");
    }


    public void clear(){


    editor.clear();
        editor.commit();
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LandingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }

}


