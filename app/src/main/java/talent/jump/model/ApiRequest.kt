package talent.jump.model

import com.google.gson.JsonObject
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
    @POST("/api/verify/forget_password/send")
    fun frogetPassword(@Body data: JsonObject): Call<ForgetPassResponse>

    @Headers("x-us-platform:2")
    @POST("/api/register")
    fun MembershipRegister(@Body data: JsonObject): Call<RegisterResponse>

    @Headers("x-us-platform:2")
    @GET("/api/user")
    fun getMe(): Call<UserResponse>


    @Headers("x-us-platform:2")
    @GET("/api/streams?")
    fun getLiveStream(@Query("page") page:String,@Query("per_page") per_page:String,
                      @Query("type") type:String,@Query("version") version:String): Call<StreamListResponse>

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
    open fun getViewer(@Path("id") id: String,@Body data: JsonObject): Call<String>

    @Headers("x-us-platform:2")
    @POST("/api/streams/{id}/messages")
    open fun createmessages(@Path("id") id: String,@Body data: JsonObject): Call<VerifyMailReaponse>

    @Headers("x-us-platform:2")
    @PUT("/api/user/verify/email")
    fun verifyPersonalMail(@Body data: JsonObject): Call<LoginResponse>
}