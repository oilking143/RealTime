package talent.jump.model

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import talent.jump.data.*

interface
ApiRequest {
    @Headers("x-us-platform:2")
    @POST("/api/login")
    fun Login(@Body data: JsonObject): Call<LoginResponse>

    @Headers("x-us-platform:2")
    @POST("/api/verify/email/send")
    fun verifyMail(@Body data: JsonObject): Call<VerifyMailReaponse>

    @Headers("x-us-platform:2")
    @POST("/api/verify/email")
    fun verifyMailCode(@Body data: JsonObject): Call<VerifyMailCodeResponse>

    @Headers("x-us-platform:2")
    @POST("/api/verify/forget_password/send")
    fun frogetPassword(@Body data: JsonObject): Call<ForgetPassResponse>

    @Headers("x-us-platform:2")
    @POST("/api/register")
    fun MembershipRegister(@Body data: JsonObject): Call<RegisterResponse>

    @Multipart
    @Headers("x-us-platform:2","Accept:multipart/formdata")
    @POST("/api/medias")
    fun PostMedias(@Part part: MultipartBody.Part): Call<PostmediaResponse>

    @Headers("x-us-platform:2")
    @POST("/api/posts")
    fun createPost(@Body data: JsonObject): Call<CreatePostResponse>

    @Headers("x-us-platform:2")
    @POST("/api/comments")
    fun createComments(@Body data: JsonObject): Call<CommentResponse>

    @Headers("x-us-platform:2")
    @DELETE("/api/comments/{id}")
    fun deleteComments(@Path("id") id: String): Call<DeleteResponse>

    @Headers("x-us-platform:2")
    @GET("/api/user")
    fun getMe(): Call<UserResponse>

    @Headers("x-us-platform:2")
    @GET("/api/sellers/ecpay/tokens")
    fun getTokens(): Call<TokenResponse>

    @Headers("x-us-platform:2")
    @GET("/api/wallet")
    fun getwallet(): Call<WalletResponse>

    @Headers("x-us-platform:2")
    @GET("/api/user/fans?")
    fun getFans(@Query("page") page:String,@Query("per_page") per_page:String
    ): Call<FansListResponse>

    @Headers("x-us-platform:2")
    @GET("/api/user/follows?")
    fun getFollows(@Query("page") page:String,@Query("per_page") per_page:String
    ): Call<FollowersResponse>

    @Headers("x-us-platform:2")
    @GET("/api/posts/{id}/comments?")
    fun getListPosts(@Path("id") id: String,@Query("before_id") before_id:String
                 ,@Query("after_id") after_id:String,@Query("number") number:String): Call<ListPostCommentResponse>

    @Headers("x-us-platform:2")
    @GET("/api/streams?")
    fun getLiveStream(@Query("page") page:String,@Query("per_page") per_page:String,
                      @Query("type") type:String,@Query("version") version:String): Call<LiveStreamListResponse>

    @Headers("x-us-platform:2")
    @GET("/api/posts?")
    fun getPosts(@Query("user_id") page:String,@Query("number") per_page:String,
                      @Query("before_id") type:String): Call<PostListResponse>

    @Headers("x-us-platform:2")
    @GET("/api/search?")
    fun getSearch(@Query("query") query:String): Call<SearchResponse>

    @Headers("x-us-platform:2")
    @PUT("/api/user/password/reset")
    fun resetPassword(@Body data: JsonObject): Call<ResetResponse>


    @Headers("x-us-platform:2")
    @POST("/api/user/stream_auth")
    fun streamAuth(): Call<AuthRegistResponse>

    @Headers("x-us-platform:2")
    @POST("/api/verify/forget_password")
    fun updatePassword(@Body data: JsonObject): Call<LoginResponse>

    @Headers("x-us-platform:2")
    @POST("/api/streams")
    fun createStreams(@Body data: JsonObject): Call<CreateStreamResponse>

    @Headers("x-us-platform:2")
    @POST("/api/user/profile_photo")
    fun upLoadPhoto(@Body data: JsonObject): Call<String>

    @Headers("x-us-platform:2")
    @POST("/api/user/verify/email/send")
    fun verifyPersonalMailSend(): Call<String>

    @Headers("x-us-platform:2")
    @POST("/api/streams/{id}/viewers")
    open fun getViewer(@Path("id") id: String,@Body data: JsonObject): Call<ViewersResponse>

    @Headers("x-us-platform:2")
    @POST("/api/streams/{id}/messages")
    open fun createmessages(@Path("id") id: String,@Body data: JsonObject): Call<VerifyMailReaponse>

    @Headers("x-us-platform:2")
    @POST("/api/streams/{id}/heart")
    open fun giveheart(@Path("id") id: String,@Body data: JsonObject): Call<heartResponse>

    @Headers("x-us-platform:2")
    @POST("/api/streams/{id}/mute")
    open fun muteUser(@Path("id") id: String,@Body data: JsonObject): Call<muteResponse>

    @Headers("x-us-platform:2")
    @PUT("/api/user/verify/email")
    fun verifyPersonalMail(@Body data: JsonObject): Call<LoginResponse>

    @Headers("x-us-platform:2")
    @PUT("/api/user")
    fun updateUser(@Body data: JsonObject): Call<UpdateResponse>

    @Headers("x-us-platform:2")
    @PUT("/api/user/password")
    fun updateOldPassword(@Body data: JsonObject): Call<UpdateResponse>
}