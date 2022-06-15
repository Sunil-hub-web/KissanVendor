package in.co.kissanvendor.extras;


import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface VendorApi {

    // image upload
    @Multipart
    @POST("vendorapp/vendor/product/image/upload")
    Call<ImageResponse> uploadImage(@Header("auth-token") String token, @Part MultipartBody.Part photo);



    // get user profile
    @GET("view/profile/")
    Call<ApiResponse> getDetails(@Header("auth-token") String token);

    // home route


}
