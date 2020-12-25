package com.example.math.retrofit;

import com.example.math.retrofit.mymodels.GetVersionRespons;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    /*@Headers("Content-Type: application/json")
    @POST("account/register")
    Call<RegisterResponse> registerRequest(@Body RegisterSender registerSender);

    @Headers("Content-Type: application/json")
    @POST("account/activate")
    Call<VerifyCodeResponse> sendVerify(@Body VerifyCodeSender verifyCodeSender);

    @Headers("Content-Type: application/json")
    @POST("home/get")
    Call<HomeRequest> getHome(@Header("Authorization") String token, @Body HomeSender homeSender);

    @Headers("Content-Type: application/json")
    @POST("account/login")
    Call<LoginRequest> login(@Body LoginSender loginSender);

    @Headers("Content-Type: application/json")
    @POST("account/postprofile")
    Call<PostProfileRequest> postProfile(@Header("Authorization") String token, @Body PostProfileSender postProfileSender);

    @Headers("Content-Type: application/json")
    @POST("Magzine/GetList")
    Call<MagazineRequest> getMagazineList(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("Content/GetBlogContents")
    Call<BlogListRequest> getBlogList(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("Content/Getvideos")
    Call<VideoListRequest> getVideoList(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("Content/Getpodcasts")
    Call<PodcastListRequest> getPodcastList(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("faq/get")
    Call<QuestionListRequest> getQuestionList(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("SupportRequest/post")
    Call<PostQuestionRequest> postQuestion(@Header("Authorization") String token, @Body PostRequestSender postRequestSender);

    @Headers("Content-Type: application/json")
    @POST("Content/PostLike")
    Call<LikeRequest> likePost(@Header("Authorization") String token, @Body LikeSender sender);

    @Headers("Content-Type: application/json")
    @POST("Content/GetComments")
    Call<CommentRequest> getComments(@Header("Authorization") String token, @Body CommentSender sender);

    @Headers("Content-Type: application/json")
    @POST("Content/PostComment")
    Call<SendCommentRequest> sendComment(@Header("Authorization") String token, @Body SendCommentSender sender);

    @Headers("Content-Type: application/json")
    @POST("ContentGroup/Get")
    Call<ContentRequest> getContentList();

    @Headers("Content-Type: application/json")
    @POST("Content/GetContentByGroup")
    Call<GroupDetailsRequest> getGroup(@Header("Authorization") String token, @Body GroupDetailsSender sender);

    @Headers("Content-Type: application/json")
    @POST("account/GetProfileBasicData")
    Call<BasicDataResponse> getBasicData();

    @Headers("Content-Type: application/json")
    @POST("account/GetProfile")
    Call<GetProfileResponse> getProfile(@Header("Authorization") String token);


    @Headers("Content-Type: application/json")
    @POST("Content/GetContentBySearch")
    Call<GroupDetailsRequest> search(@Header("Authorization") String token, @Body SearchSender sender);*/

    @Headers("Content-Type: application/json")
    @GET("apis/getVersion.php")
    Call<GetVersionRespons> getVersion(@Query("version") String version);
}
